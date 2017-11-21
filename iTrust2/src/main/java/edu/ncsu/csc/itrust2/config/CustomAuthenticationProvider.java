package edu.ncsu.csc.itrust2.config;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import edu.ncsu.csc.itrust2.models.enums.TransactionType;
import edu.ncsu.csc.itrust2.models.persistent.Lockout;
import edu.ncsu.csc.itrust2.models.persistent.User;
import edu.ncsu.csc.itrust2.utils.LoggerUtil;

/**
 * Provides Custom authentication for the iTrust2 login (to enable lockout and
 * account disable).
 *
 * @author Galen Abell (gjabell)
 */
public class CustomAuthenticationProvider implements AuthenticationProvider {
    /**
     * The user is disabled in the system.
     */
    private static final int      USER_DISABLED        = 0;

    /**
     * THe number of milliseconds in an hour.
     */
    private static final int      MILLIS_IN_HOUR       = 3600000;

    private static final String   DISABLED_TEXT        = "User is disabled. Please contact the system administrator.";
    private static final String   LOCKED_TEXT          = "Account is locked for 1 hour due to failed login attempts.";
    private static final String   BAD_CREDENTIALS_TEXT = "Username or password is invalid.";

    private final PasswordEncoder passwordEncoder      = new BCryptPasswordEncoder();

    /**
     * Performs authentication with the same contract as
     *
     * @param authentication
     *            the authentication request object.
     * @return a fully authenticated object including credentials. May return
     *         <code>null</code> if the <code>AuthenticationProvider</code> is
     *         unable to support authentication of the passed
     *         <code>Authentication</code> object. In such a case, the next
     *         <code>AuthenticationProvider</code> that supports the presented
     *         <code>Authentication</code> class will be tried.
     * @throws AuthenticationException
     *             if authentication fails.
     */
    @Override
    public Authentication authenticate ( final Authentication authentication ) throws AuthenticationException {
        final String username = authentication.getName().trim();
        final String password = authentication.getCredentials().toString().trim();

        User user;
        try {
            user = User.getWhere( "username='" + username + "'" ).get( 0 );
        }
        catch ( final IndexOutOfBoundsException e ) {
            throw new UsernameNotFoundException( BAD_CREDENTIALS_TEXT );
        }
        // user exists in system
        if ( user.getEnabled() == USER_DISABLED ) {
            throw new DisabledException( DISABLED_TEXT );
        }
        // user is enabled
        final int numFailAttempts = user.getNumFailAttempts();
        if ( passwordEncoder.matches( password, user.getPassword() ) ) {
            // user's password is correct
            if ( numFailAttempts >= User.MAX_LOGIN_ATTEMPTS ) {
                // check if timeout has passed
                final long currentTime = System.currentTimeMillis();
                final long lockTime = user.getLockoutTimeout();
                if ( currentTime < lockTime ) {
                    throw new LockedException( LOCKED_TEXT );
                }
            }
            // user isn't locked, clear num failed attempts and return a login
            // token
            user.setNumFailAttempts( 0 );
            user.setLockoutTimeout( null );
            user.save();
            return new UsernamePasswordAuthenticationToken( username, password,
                    Collections.singletonList( new SimpleGrantedAuthority( user.getRole().toString() ) ) );
        }
        // password is incorrect
        if ( numFailAttempts >= User.MAX_LOGIN_ATTEMPTS ) {
            throw new LockedException( LOCKED_TEXT );
        }
        // account not locked, attempts < MAX_LOGIN_ATTEMPTS
        if ( numFailAttempts < User.MAX_LOGIN_ATTEMPTS - 1 ) {
            // increase the number of failed attempts and return bad credentials
            user.setNumFailAttempts( numFailAttempts + 1 );
            user.save();
            throw new BadCredentialsException( BAD_CREDENTIALS_TEXT );
        }
        // attempts == MAX_LOGIN_ATTEMPTS - 1 (ie, this failed attempt will lock
        // account)
        final long currentTime = System.currentTimeMillis();
        final Lockout lockout = new Lockout( username, currentTime + MILLIS_IN_HOUR * 24 );
        lockout.save();
        LoggerUtil.log( TransactionType.LOCKOUT_TEMP, username );
        // find out how many lockouts they have in the past 24 hours
        final List<Lockout> userLockouts = Lockout.getUserLockouts( username ).stream()
                .filter( l -> l.getTimestamp() > currentTime ).collect( Collectors.toList() );
        final boolean disabled = userLockouts.size() >= Lockout.MAX_LOCKOUT;
        if ( disabled ) {
            user.setEnabled( USER_DISABLED );
            LoggerUtil.log( TransactionType.LOCKOUT_PERM, username );
        }
        user.setNumFailAttempts( User.MAX_LOGIN_ATTEMPTS );
        user.setLockoutTimeout( currentTime + MILLIS_IN_HOUR );
        user.save();
        if ( disabled ) {
            throw new DisabledException( DISABLED_TEXT );
        }
        else {
            throw new LockedException( LOCKED_TEXT );
        }
    }

    /**
     * Returns <code>true</code> if this <Code>AuthenticationProvider</code>
     * supports the indicated <Code>Authentication</code> object.
     * <p>
     * Returning <code>true</code> does not guarantee an
     * <code>AuthenticationProvider</code> will be able to authenticate the
     * presented instance of the <code>Authentication</code> class. It simply
     * indicates it can support closer evaluation of it. An
     * <code>AuthenticationProvider</code> can still return <code>null</code>
     * from the {@link #authenticate(Authentication)} method to indicate another
     * <code>AuthenticationProvider</code> should be tried.
     * </p>
     * <p>
     * Selection of an <code>AuthenticationProvider</code> capable of performing
     * authentication is conducted at runtime the <code>ProviderManager</code>.
     * </p>
     *
     * @param authentication
     *            The type of Authentication to test for support.
     * @return <code>true</code> if the implementation can more closely evaluate
     *         the <code>Authentication</code> class presented
     */
    @Override
    public boolean supports ( final Class< ? > authentication ) {
        return authentication.equals( UsernamePasswordAuthenticationToken.class );
    }
}
