package edu.ncsu.csc.itrust2.controllers.api;

import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import edu.ncsu.csc.itrust2.forms.user.PasswordChangeForm;
import edu.ncsu.csc.itrust2.models.persistent.Patient;
import edu.ncsu.csc.itrust2.models.persistent.Personnel;
import edu.ncsu.csc.itrust2.models.persistent.User;

/**
 * API controller for changing or resetting a User's password.
 *
 * @author Galen Abell (gjabell)
 */
@RestController
@SuppressWarnings ( { "unchecked", "rawtypes" } )
public class APIPasswordController extends APIController {
    /**
     * Username is incorrect
     */
    private static final ResponseEntity INCORRECT_USERNAME         = new ResponseEntity(
            gson.toJson( "Username is incorrect" ), HttpStatus.NOT_FOUND );
    /**
     * Current password is incorrect
     */
    private static final ResponseEntity INCORRECT_CURRENT_PASSWORD = new ResponseEntity(
            gson.toJson( "Current password is incorrect" ), HttpStatus.CONFLICT );
    /**
     * User's reset token is invalid, too old, or doesn't match.
     */
    private static final ResponseEntity INVALID_RESET_TOKEN        = new ResponseEntity(
            gson.toJson( "Reset token is invalid" ), HttpStatus.CONFLICT );
    /**
     * New password(s) is invalid
     */
    private static final ResponseEntity INVALID_NEW_PASSWORD       = new ResponseEntity(
            gson.toJson( "Password is invalid (must be between 6 and 20 characters)" ), HttpStatus.CONFLICT );
    /**
     * New passwords don't match
     */
    private static final ResponseEntity MISMATCHED_NEW_PASSWORDS   = new ResponseEntity(
            gson.toJson( "New passwords don't match" ), HttpStatus.CONFLICT );
    /**
     * Password updated successfully
     */
    private static final ResponseEntity PASSWORD_UPDATE_SUCCESS    = new ResponseEntity(
            gson.toJson( "Password updated successfully" ), HttpStatus.OK );

    /** SMTP server through which password reset emails are sent */
    private static final String         GMAIL_SMTP_ADDRESS         = "smtp.gmail.com";

    /** Email address from which to send reset emails */
    private static final String         ITRUST2_EMAIL_ADDRESS      = "itrust22045@gmail.com";

    /** Terrible thing to do - password for the email account */
    private static final String         ITRUST2_EMAIL_PASSWORD     = "pass4itrust";

    /** Length of the reset tokens */
    private static final int            RESET_TOKEN_LENGTH         = 37;

    /**
     * Duration for which a reset token is valid (1 hour = 3600s = 3600e3 ms)
     */
    private static final long           RESET_TOKEN_DURATION_MSEC  = 3600000;
    /**
     * The password encoder / decoder.
     */
    private final BCryptPasswordEncoder encoder                    = new BCryptPasswordEncoder();

    /**
     * API endpoint for changing a logged-in User's password.
     *
     * @param pForm
     *            The password form containing the password info.
     * @return Returns a ResponseEntity based on the result of the operation.
     */
    @PutMapping ( BASE_PATH + "/password" )
    public ResponseEntity changePassword ( @RequestBody final PasswordChangeForm pForm ) {
        // get the currently logged in User
        final User user = User.getByName( SecurityContextHolder.getContext().getAuthentication().getName() );
        if ( user == null ) {
            return INCORRECT_USERNAME;
        }
        if ( !encoder.matches( pForm.getPasswordToken(), user.getPassword() ) ) {
            return INCORRECT_CURRENT_PASSWORD;
        }
        if ( invalidPassword( pForm.getNewPassword1() ) || invalidPassword( pForm.getNewPassword2() ) ) {
            return INVALID_NEW_PASSWORD;
        }
        if ( !pForm.getNewPassword1().equals( pForm.getNewPassword2() ) ) {
            return MISMATCHED_NEW_PASSWORDS;
        }
        user.setPassword( encoder.encode( pForm.getNewPassword1() ) );
        try {
            user.save();
            return PASSWORD_UPDATE_SUCCESS;
        }
        catch ( final Exception e ) {
            return new ResponseEntity( gson.toJson( "Could not update password because of " + e.getMessage() ),
                    HttpStatus.BAD_REQUEST );
        }
    }

    /**
     * API endpoint for resetting a User's password.
     *
     * @param id
     *            The id of the User's whose password to reset.
     * @param pForm
     *            The password form containing the password info.
     * @return Returns a ResponseEntity based on the result of the operation.
     */
    @PostMapping ( BASE_PATH + "/password/{id}" )
    public ResponseEntity resetPassword ( @PathVariable final String id, @RequestBody final PasswordChangeForm pForm ) {
        final User user = User.getByName( id );
        if ( user == null ) {
            return INCORRECT_USERNAME;
        }
        if ( user.getResetToken() == null || !user.getResetToken().equals( pForm.getPasswordToken() )
                || System.currentTimeMillis() > user.getResetTimeout() ) {
            return INVALID_RESET_TOKEN;
        }
        if ( invalidPassword( pForm.getNewPassword1() ) || invalidPassword( pForm.getNewPassword2() ) ) {
            return INVALID_NEW_PASSWORD;
        }
        if ( !pForm.getNewPassword1().equals( pForm.getNewPassword2() ) ) {
            return MISMATCHED_NEW_PASSWORDS;
        }
        user.setPassword( encoder.encode( pForm.getNewPassword1() ) );
        user.setResetToken( null );
        user.setResetTimeout( null );
        try {
            user.save();
            return PASSWORD_UPDATE_SUCCESS;
        }
        catch ( final Exception e ) {
            return new ResponseEntity( gson.toJson( "Could not update password because of " + e.getMessage() ),
                    HttpStatus.BAD_REQUEST );
        }
    }

    /**
     * Sends an email to the user that requested a password reset.
     *
     * @param userId
     *            The username of the User that requested the reset
     * @return HTTP 200 if the email was sent. HTTP 404 if the username wasn't a
     *         Patient. HTTP 400 if the user didn't have an email address. HTTP
     *         500 if the email can't be sent.
     */
    @GetMapping ( BASE_PATH + "/password/requestReset/{userId}" )
    public ResponseEntity requestPasswordReset ( @PathVariable final String userId ) {

        String email = null;

        // First, verify the user exists
        final User u = User.getByName( userId );
        if ( u == null ) {
            return new ResponseEntity( gson.toJson( "User with username " + userId + " was not found" ),
                    HttpStatus.NOT_FOUND );
        }
        else {

            // Verify their account is not disabled
            if ( u.getEnabled() != 1 ) {
                return new ResponseEntity( gson.toJson( "User " + userId + " 's account is disabled" ),
                        HttpStatus.BAD_REQUEST );
            }

            // Determine whether they're a Patient or Personnel, and get their
            // email
            final Patient p = Patient.getPatient( u );
            if ( p != null ) {
                email = p.getEmail();
            }
            else {
                final Personnel p2 = Personnel.getByName( userId );
                if ( p2 != null ) {
                    email = p2.getEmail();
                }
            }
        }

        // Next, we must ensure that this particular User has an email
        // address on file.
        if ( email == null ) {
            return new ResponseEntity( gson.toJson( "No email address found for user " + userId ),
                    HttpStatus.BAD_REQUEST );
        }

        // Now, we send an email via Gmail
        final Properties props = new Properties();
        props.put( "mail.smtp.host", GMAIL_SMTP_ADDRESS );
        props.put( "mail.smtp.auth", "true" );
        props.put( "mail.smtp.starttls.enable", "true" );
        props.put( "mail.smtp.port", "587" );
        final Session session = Session.getInstance( props );
        try {

            // Create a request token - note we're not ensuring uniqueness, b/c
            // what are the odds?.
            final String token = RandomStringUtils.randomAscii( RESET_TOKEN_LENGTH );
            u.setResetToken( token );

            // Set the timestamp for when the token will expire (1 hour from
            // issuance)
            u.setResetTimeout( System.currentTimeMillis() + ( RESET_TOKEN_DURATION_MSEC ) );
            u.save();

            // Put it in an email and send
            final MimeMessage msg = new MimeMessage( session );
            msg.setFrom( ITRUST2_EMAIL_ADDRESS );
            msg.setRecipients( Message.RecipientType.TO, email );
            msg.setSubject( "iTrust2-TP-204-5 Password Reset" );
            msg.setSentDate( new Date() );
            msg.setText( "Username: " + userId + "\n" + "Password Reset Token: " + token );
            Transport.send( msg, ITRUST2_EMAIL_ADDRESS, ITRUST2_EMAIL_PASSWORD );
        }
        catch ( final Exception e ) {
            return new ResponseEntity( gson.toJson( "The request email could not be sent" ),
                    HttpStatus.INTERNAL_SERVER_ERROR );
        }

        return new ResponseEntity( gson.toJson( "Password reset request email sent" ), HttpStatus.OK );
    }

    /**
     * Checks if the given password is valid.
     *
     * @param password
     *            The password to check.
     * @return Returns true if password is between 6 and 20 characters,
     *         otherwise false.
     */
    private boolean invalidPassword ( final String password ) {
        return password.length() < 6 || password.length() > 20;
    }
}
