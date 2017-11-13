package edu.ncsu.csc.itrust2.config;

import edu.ncsu.csc.itrust2.models.persistent.Lockout;
import edu.ncsu.csc.itrust2.models.persistent.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CustomAuthenticationProvider extends DaoAuthenticationProvider {
    /**
     * The user is disabled in the system.
     */
    private static final int USER_DISABLED = 0;

    /**
     * THe number of milliseconds in an hour.
     */
    private static final int MILLIS_IN_HOUR = 3600000;

    private static final String DISABLED_TEXT = "User is disabled. Please contact the system administrator.";
    private static final String LOCKED_TEXT = "Account is locked for 1 hour due to failed login attempts.";
    private static final String BAD_CREDENTIALS_TEXT = "Username or password is invalid.";

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * Performs authentication with the same contract as
     * {@link AuthenticationManager#authenticate(Authentication)}
     * .
     *
     * @param authentication the authentication request object.
     * @return a fully authenticated object including credentials. May return
     * <code>null</code> if the <code>AuthenticationProvider</code> is unable to support
     * authentication of the passed <code>Authentication</code> object. In such a case,
     * the next <code>AuthenticationProvider</code> that supports the presented
     * <code>Authentication</code> class will be tried.
     * @throws AuthenticationException if authentication fails.
     */
    @Override
    public Authentication authenticate( Authentication authentication ) throws AuthenticationException {
        String username = authentication.getName().trim();
        String password = authentication.getCredentials().toString().trim();

        User user = User.getByName( username );
        if ( user == null ) throw new UsernameNotFoundException( BAD_CREDENTIALS_TEXT );
        // user exists in system
        if ( user.getEnabled() == USER_DISABLED ) throw new DisabledException( DISABLED_TEXT );
        // user is enabled
        int numFailAttempts = user.getNumFailAttempts();
        if ( passwordEncoder.matches( password, user.getPassword() ) ) {
            // user's password is correct
            if ( numFailAttempts >= User.MAX_LOGIN_ATTEMPTS ) {
                // check if timeout has passed
                long currentTime = System.currentTimeMillis();
                long lockTime = user.getLockoutTimeout();
                if ( currentTime < lockTime ) throw new LockedException( LOCKED_TEXT );
            }
            // user isn't locked, clear num failed attempts and return a login token
            user.setNumFailAttempts( 0 );
            user.setLockoutTimeout( null );
            user.save();
            return new UsernamePasswordAuthenticationToken( username, password, Collections.singletonList( new SimpleGrantedAuthority( user.getRole().toString() ) ) );
        }
        // password is incorrect
        if ( numFailAttempts >= User.MAX_LOGIN_ATTEMPTS ) throw new LockedException( LOCKED_TEXT );
        // account not locked, attempts < MAX_LOGIN_ATTEMPTS
        if ( numFailAttempts < User.MAX_LOGIN_ATTEMPTS - 1 ) {
            // increase the number of failed attempts and return bad credentials
            user.setNumFailAttempts( numFailAttempts + 1 );
            user.save();
            throw new BadCredentialsException( BAD_CREDENTIALS_TEXT );
        }
        // attempts == MAX_LOGIN_ATTEMPTS - 1 (ie, this failed attempt will lock account)
        long currentTime = System.currentTimeMillis();
        Lockout lockout = new Lockout( username, currentTime + MILLIS_IN_HOUR * 24 );
        lockout.save();
        // find out how many lockouts they have in the past 24 hours
        List< Lockout > userLockouts = Lockout.getUserLockouts( username ).stream().filter( l -> l.getTimestamp() > currentTime ).collect( Collectors.toList() );
        boolean disabled = userLockouts.size() >= Lockout.MAX_LOCKOUT;
        if ( disabled )
            user.setEnabled( USER_DISABLED );
        user.setNumFailAttempts( User.MAX_LOGIN_ATTEMPTS );
        user.setLockoutTimeout( currentTime + MILLIS_IN_HOUR );
        user.save();
        if ( disabled )
            throw new DisabledException( DISABLED_TEXT );
        else
            throw new LockedException( LOCKED_TEXT );
    }

    /**
     * Returns <code>true</code> if this <Code>AuthenticationProvider</code> supports the
     * indicated <Code>Authentication</code> object.
     * <p>
     * Returning <code>true</code> does not guarantee an
     * <code>AuthenticationProvider</code> will be able to authenticate the presented
     * instance of the <code>Authentication</code> class. It simply indicates it can
     * support closer evaluation of it. An <code>AuthenticationProvider</code> can still
     * return <code>null</code> from the {@link #authenticate(Authentication)} method to
     * indicate another <code>AuthenticationProvider</code> should be tried.
     * </p>
     * <p>
     * Selection of an <code>AuthenticationProvider</code> capable of performing
     * authentication is conducted at runtime the <code>ProviderManager</code>.
     * </p>
     *
     * @param authentication
     * @return <code>true</code> if the implementation can more closely evaluate the
     * <code>Authentication</code> class presented
     */
    @Override
    public boolean supports( Class< ? > authentication ) {
        return authentication.equals( UsernamePasswordAuthenticationToken.class );
    }
}
