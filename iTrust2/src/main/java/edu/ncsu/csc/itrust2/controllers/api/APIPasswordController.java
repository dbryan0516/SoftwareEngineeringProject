package edu.ncsu.csc.itrust2.controllers.api;

import edu.ncsu.csc.itrust2.forms.user.PasswordChangeForm;
import edu.ncsu.csc.itrust2.models.persistent.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

/**
 * API controller for changing or resetting a User's password.
 *
 * @author Galen Abell (gjabell)
 */
@RestController
@SuppressWarnings( "unchecked" )
public class APIPasswordController extends APIController {
    /**
     * Username is incorrect
     */
    private static final ResponseEntity INCORRECT_USERNAME = new ResponseEntity( gson.toJson( "Username is incorrect" ), HttpStatus.NOT_FOUND );
    /**
     * Current password is incorrect
     */
    private static final ResponseEntity INCORRECT_CURRENT_PASSWORD = new ResponseEntity( gson.toJson( "Current password is incorrect" ), HttpStatus.CONFLICT );
    /**
     * User's reset token is invalid, too old, or doesn't match.
     */
    private static final ResponseEntity INVALID_RESET_TOKEN = new ResponseEntity( gson.toJson( "Reset token is invalid" ), HttpStatus.CONFLICT );
    /**
     * New password(s) is invalid
     */
    private static final ResponseEntity INVALID_NEW_PASSWORD = new ResponseEntity( gson.toJson( "Password is invalid (must be between 6 and 20 characters)" ), HttpStatus.CONFLICT );
    /**
     * New passwords don't match
     */
    private static final ResponseEntity MISMATCHED_NEW_PASSWORDS = new ResponseEntity( gson.toJson( "New passwords don't match" ), HttpStatus.CONFLICT );
    /**
     * Password updated successfully
     */
    private static final ResponseEntity PASSWORD_UPDATE_SUCCESS = new ResponseEntity( gson.toJson( "Password updated successfully" ), HttpStatus.OK );

    /**
     * The password encoder / decoder.
     */
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    /**
     * API endpoint for changing a logged-in User's password.
     *
     * @param pForm The password form containing the password info.
     * @return Returns a ResponseEntity based on the result of the operation.
     */
    @PutMapping( BASE_PATH + "/password" )
    public ResponseEntity changePassword( @RequestBody final PasswordChangeForm pForm ) {
        // get the currently logged in User
        final User user = User.getByName( SecurityContextHolder.getContext().getAuthentication().getName() );
        if ( user == null )
            return INCORRECT_USERNAME;
        if ( !encoder.matches( pForm.getPasswordToken(), user.getPassword() ) )
            return INCORRECT_CURRENT_PASSWORD;
        if ( invalidPassword( pForm.getNewPassword1() ) || invalidPassword( pForm.getNewPassword2() ) )
            return INVALID_NEW_PASSWORD;
        if ( !pForm.getNewPassword1().equals( pForm.getNewPassword2() ) )
            return MISMATCHED_NEW_PASSWORDS;
        user.setPassword( encoder.encode( pForm.getNewPassword1() ) );
        try {
            user.save();
            return PASSWORD_UPDATE_SUCCESS;
        } catch ( final Exception e ) {
            return new ResponseEntity( gson.toJson( "Could not update password because of " + e.getMessage() ), HttpStatus.BAD_REQUEST );
        }
    }

    /**
     * API endpoint for resetting a User's password.
     *
     * @param id    The id of the User's whose password to reset.
     * @param pForm The password form containing the password info.
     * @return Returns a ResponseEntity based on the result of the operation.
     */
    @PostMapping( BASE_PATH + "/password/{id}" )
    public ResponseEntity resetPassword( @PathVariable final String id, @RequestBody final PasswordChangeForm pForm ) {
        final User user = User.getByName( id );
        if ( user == null )
            return INCORRECT_USERNAME;
        if ( user.getResetToken() == null || !user.getResetToken().equals( pForm.getPasswordToken() ) || System.currentTimeMillis() > user.getResetTimeout() )
            return INVALID_RESET_TOKEN;
        if ( invalidPassword( pForm.getNewPassword1() ) || invalidPassword( pForm.getNewPassword2() ) )
            return INVALID_NEW_PASSWORD;
        if ( !pForm.getNewPassword1().equals( pForm.getNewPassword2() ) )
            return MISMATCHED_NEW_PASSWORDS;
        user.setPassword( encoder.encode( pForm.getNewPassword1() ) );
        user.setResetToken( null );
        user.setResetTimeout( null );
        try {
            user.save();
            return PASSWORD_UPDATE_SUCCESS;
        } catch ( final Exception e ) {
            return new ResponseEntity( gson.toJson( "Could not update password because of " + e.getMessage() ), HttpStatus.BAD_REQUEST );
        }
    }

    /**
     * Checks if the given password is valid.
     *
     * @param password The password to check.
     * @return Returns true if password is between 6 and 20 characters, otherwise false.
     */
    private boolean invalidPassword( final String password ) {
        return password.length() < 6 || password.length() > 20;
    }
}
