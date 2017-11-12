package edu.ncsu.csc.itrust2.config;

import edu.ncsu.csc.itrust2.models.persistent.Lockout;
import edu.ncsu.csc.itrust2.models.persistent.User;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class CustomAuthenticationProvider implements AuthenticationProvider {
    /**
     * THe number of milliseconds in an hour.
     */
    private static final int MILLIS_IN_HOUR = 3600000;

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
    @Override public Authentication authenticate ( Authentication authentication ) throws AuthenticationException {
        String username = authentication.getName().trim();
        String password = authentication.getCredentials().toString().trim();

        User user = User.getByName( username );
        if ( user != null ) {
            // If the user is disabled, fail
            if ( user.getEnabled() == 0 )
                throw new DisabledException( "User is disabled. Please contact the system administrator." );
            // if the user's password is invalid:
            if ( !user.getPassword().equals( password ) ) {
                // check the total number of failed attempts
                int currentFailedAttempts = user.getNumFailAttempts();
                // if they are under 3 attempts, increase their failed attempt count
                if ( currentFailedAttempts < User.MAX_LOGIN_ATTEMPTS - 1 )
                    user.setNumFailAttempts( currentFailedAttempts + 1 );
                else // number of attempts > MAX_LOGIN_ATTEMPTS, so they are disabled
                    if ( currentFailedAttempts == User.MAX_LOGIN_ATTEMPTS - 1 ) {
                        // locked for the first time
                        long lockoutTime = System.currentTimeMillis();
                        user.setNumFailAttempts( User.MAX_LOGIN_ATTEMPTS );
                        user.setResetTimeout( lockoutTime );
                        Lockout lockout = new Lockout( username, lockoutTime );
                        lockout.save();
                        // TODO add permalockout check
                        throw new LockedException( "Account is locked for 1 hour due to failed login attempts." );
                    }
                    else
                        throw new LockedException( "Account is locked for 1 hour due to failed login attempts." );
                throw new BadCredentialsException( "Username or password is invalid." );
            }
            else {
                // check if they are locked
                long currentTime = System.currentTimeMillis();
                if ( currentTime >= user.getResetTimeout() + MILLIS_IN_HOUR ) {

                }
                return new UsernamePasswordAuthenticationToken( username, password );
            }
        }
        throw new UsernameNotFoundException( "Username or password is invalid." );
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
    @Override public boolean supports ( Class<?> authentication ) {
        return false;
    }
}
