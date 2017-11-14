package edu.ncsu.csc.itrust2.forms.user;

/**
 * Form used to pass password change information from the frontend to the backend.
 *
 * @author Galen Abell (gjabell)
 */
public class PasswordChangeForm {
    /**
     * Represents the password reset token, or the User's current password.
     */
    private String passwordToken;

    /**
     * The User's new password.
     */
    private String newPassword1;

    /**
     * The User's new password, repeated.
     */
    private String newPassword2;

    /**
     * Default constructor.
     */
    public PasswordChangeForm() {
    }

    /**
     * Getter for property 'passwordToken'.
     *
     * @return Value for property 'passwordToken'.
     */
    public String getPasswordToken() {
        return passwordToken;
    }

    /**
     * Setter for property 'passwordToken'.
     *
     * @param passwordToken Value to set for property 'passwordToken'.
     */
    public void setPasswordToken( String passwordToken ) {
        this.passwordToken = passwordToken;
    }

    /**
     * Getter for property 'newPassword1'.
     *
     * @return Value for property 'newPassword1'.
     */
    public String getNewPassword1() {
        return newPassword1;
    }

    /**
     * Setter for property 'newPassword1'.
     *
     * @param newPassword1 Value to set for property 'newPassword1'.
     */
    public void setNewPassword1( String newPassword1 ) {
        this.newPassword1 = newPassword1;
    }

    /**
     * Getter for property 'newPassword2'.
     *
     * @return Value for property 'newPassword2'.
     */
    public String getNewPassword2() {
        return newPassword2;
    }

    /**
     * Setter for property 'newPassword2'.
     *
     * @param newPassword2 Value to set for property 'newPassword2'.
     */
    public void setNewPassword2( String newPassword2 ) {
        this.newPassword2 = newPassword2;
    }
}
