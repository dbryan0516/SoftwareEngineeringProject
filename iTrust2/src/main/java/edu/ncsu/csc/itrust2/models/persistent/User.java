package edu.ncsu.csc.itrust2.models.persistent;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import edu.ncsu.csc.itrust2.utils.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import edu.ncsu.csc.itrust2.forms.admin.UserForm;
import edu.ncsu.csc.itrust2.models.enums.Role;
import edu.ncsu.csc.itrust2.utils.DomainObjectCache;

/**
 * Basic class for a User in the system. This User class is a shared type that
 * is used for all users of the iTrust2 system and handles basic functionality
 * such as authenticating the user in the system. For specific rolls, an
 * additional record, such as a Patient or Personnel record, is created and
 * references the User object for that user. This allows the iTrust2 system to
 * keep only what information is needed for a particular type of user.
 *
 * @author Kai Presler-Marshall
 *
 */
@Entity
@Table ( name = "Users" )
public class User extends DomainObject<User> implements Serializable {

    /**
     * The UID of the user
     */
    private static final long                      serialVersionUID = 1L;

    /**
     * The maximum number of login attempts for a user
     */
    public static final int MAX_LOGIN_ATTEMPTS = 3;

    /**
     * The cache representation of the user in the database
     */
    static private DomainObjectCache<String, User> cache            = new DomainObjectCache<String, User>( User.class );

    /**
     * Get all users in the database
     *
     * @SuppressWarnings for Unchecked cast from List<capture#1-of ? extends
     *                   DomainObject> to List<User> Because get all just
     *                   returns a list of DomainObjects, the cast is okay.
     *
     * @return all users in the database
     */
    @SuppressWarnings ( "unchecked" )
    public static List<User> getUsers () {
        return (List<User>) getAll( User.class );
    }

    /**
     * Get the user by the username
     *
     * @param name
     *            the username of the user
     * @return the corresponding user with this username
     */
    public static User getByName ( final String name ) {
        User user = cache.get( name );
        if ( null == user ) {
            try {
                user = getWhere( "username = '" + name + "'" ).get( 0 );
                cache.put( name, user );
            }
            catch ( final Exception e ) {
                // Exception ignored
            }
        }
        return user;

    }

    /**
     * Get the user by name and role
     *
     * @param name
     *            the username
     * @param type
     *            the role of the user
     * @return the user with this role and name
     */
    public static User getByNameAndRole ( final String name, final Role type ) {
        return getByName( name ); /* Name is primary key, so this is safe */
    }

    /**
     * Get all HCPs in the database
     *
     * @return all HCPs in the database
     */
    public static List<User> getHCPs () {
        return getByRole( Role.ROLE_HCP );
    }

    /**
     * Get all patients in the database
     *
     * @return all patients in the database
     */
    public static List<User> getPatients () {
        return getByRole( Role.ROLE_PATIENT );
    }

    /**
     * Get users where the passed query is true
     *
     * @SuppressWarnings for Unchecked cast from List<capture#1-of ? extends
     *                   DomainObject> to List<User> Because get all just
     *                   returns a list of DomainObjects, the cast is okay.
     *
     * @param where
     *            the passed query
     * @return users where the passed query is true
     */
    @SuppressWarnings ( "unchecked" )
    public static List<User> getWhere ( final String where ) {
        return (List<User>) getWhere( User.class, where );
    }

    /**
     * Get all users with the passed role
     *
     * @param role
     *            the role to get the users of
     * @return the users with the passed role
     */
    public static List<User> getByRole ( final Role role ) {
        return getWhere( "role = '" + role.toString() + "'" );
    }

    /**
     * Saves the DomainObject into the database. If the object instance does not
     * exist a new record will be created in the database. If the object already
     * exists in the DB, then the existing record will be updated.
     */
    @Override
    @SuppressWarnings( "unchecked" )
    public void save() {
        final Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        session.saveOrUpdate( this );
        session.getTransaction().commit();
        session.close();

        getCache( this.getClass() ).put( this.getUsername(), this );
    }

    /** For Hibernate */
    public User () {
    }

    /**
     * All-argument constructor for User
     *
     * @param username
     *            Username
     * @param password
     *            The _already encoded_ password of the user.
     * @param role
     *            Role of the user
     * @param enabled
     *            1 if the user is enabled 0 if not
     */
    public User ( final String username, final String password, final Role role, final Integer enabled ) {
        setUsername( username );
        setPassword( password );
        setRole( role );
        setEnabled( enabled );
    }

    /**
     * Create a new user based off of the UserForm
     *
     * @param form
     *            the filled-in user form with user information
     */
    public User ( final UserForm form ) {
        setUsername( form.getUsername() );
        if ( !form.getPassword().equals( form.getPassword2() ) ) {
            throw new IllegalArgumentException( "Passwords do not match!" );
        }
        final PasswordEncoder pe = new BCryptPasswordEncoder();
        setPassword( pe.encode( form.getPassword() ) );
        setEnabled( null != form.getEnabled() ? 1 : 0 );
        setRole( Role.valueOf( form.getRole() ) );

    }

    /**
     * The username of the user
     */
    @Id
    @Length ( max = 20 )
    private String  username;

    /**
     * The password of the user
     */
    private String  password;

    /**
     * Whether or not the user is enabled
     */
    @Min ( 0 )
    @Max ( 1 )
    private Integer enabled;

    /**
     * The role of the user
     */
    @Enumerated ( EnumType.STRING )
    private Role    role;

    /**
     * The stored token for a user to reset their password with
     */
    private String resetToken;

    /**
     * The timeout value for the reset token (ie if currentTimeMillis is greater than this value, token is invalid).
     */
    private Long resetTimeout;

    /**
     * The number failed login attempts for a user
     */
    private int numFailAttempts;

    /**
     * The timeout for account lockout (ie if the currentTimeMillis is greater than this value, account is no longer locked).
     */
    private Long lockoutTimeout;

    /**
     * Get the username of this user
     *
     * @return the username of this user
     */
    public String getUsername () {
        return username;
    }

    /**
     * Set the username of this user
     *
     * @param username
     *            the username to set this user to
     */
    public void setUsername ( final String username ) {
        this.username = username;
    }

    /**
     * Get the password of this user
     *
     * @return the password of this user
     */
    public String getPassword () {
        return password;
    }

    /**
     * Set the password of this user
     *
     * @param password
     *            the password to set this user to
     */
    public void setPassword ( final String password ) {
        this.password = password;
    }

    /**
     * Get whether or not the user is enabled
     *
     * @return Whether or not the user is enabled
     */
    public Integer getEnabled () {
        return enabled;
    }

    /**
     * Set whether or not the user is enabled
     *
     * @param enabled
     *            Whether or not the user is enabled
     */
    public void setEnabled ( final Integer enabled ) {
        this.enabled = enabled;
    }

    /**
     * Get the role of this user
     *
     * @return the role of this user
     */
    public Role getRole () {
        return role;
    }

    /**
     * Set the role of this user
     *
     * @param role
     *            the role to set this user to
     */
    public void setRole ( final Role role ) {
        this.role = role;
    }

    /**
     * gets the reset token
     * @return the token
     */
    public String getResetToken() {
        return resetToken;
    }

    /**
     * sets the reset token
     * @param resetToken the token to set
     */
    public void setResetToken(String resetToken) {
        this.resetToken = resetToken;
    }

    /**
     * Gets the time that the user can log in after getting locked out
     * @return the reset timeout
     */
    public Long getResetTimeout() {
        return resetTimeout;
    }

    /**
     * Sets the timeout value
     * @param resetTimeout the timeout value
     */
    public void setResetTimeout(Long resetTimeout) {
        this.resetTimeout = resetTimeout;
    }

    /**
     * Gets the number of failed login attempts for a user
     * @return the number of failed attempts
     */
    public int getNumFailAttempts() {
        return numFailAttempts;
    }

    /**
     * Sets the number of failed attempts for a User
     * @param numFailAttempts the number of attempts to set
     */
    public void setNumFailAttempts(int numFailAttempts) {
        this.numFailAttempts = numFailAttempts;
    }

    /**
     * Gets the lockout timeout value.
     * @return The lockout timeout.
     */
    public Long getLockoutTimeout() {
        return lockoutTimeout;
    }

    /**
     * Sets the lockout timeout value.
     * @param lockoutTimeout The new lockout timeout.
     */
    public void setLockoutTimeout( Long lockoutTimeout ) {
        this.lockoutTimeout = lockoutTimeout;
    }

    /**
     * Get the hashCode of this user
     *
     * @return the hashCode of this user
     */
    @Override
    public int hashCode () {
        final int prime = 31;
        int result = 1;
        result = prime * result + ( ( enabled == null ) ? 0 : enabled.hashCode() );
        result = prime * result + ( ( password == null ) ? 0 : password.hashCode() );
        result = prime * result + ( ( role == null ) ? 0 : role.hashCode() );
        result = prime * result + ( ( username == null ) ? 0 : username.hashCode() );
        return result;
    }

    /**
     * Whether or not this user is equal to the passed User
     * 
     * @param obj
     *            the user to compate this user to
     */
    @Override
    public boolean equals ( final Object obj ) {
        if ( this == obj ) {
            return true;
        }
        if ( obj == null ) {
            return false;
        }
        if ( getClass() != obj.getClass() ) {
            return false;
        }
        final User other = (User) obj;
        if ( enabled == null ) {
            if ( other.enabled != null ) {
                return false;
            }
        }
        else if ( !enabled.equals( other.enabled ) ) {
            return false;
        }
        if ( password == null ) {
            if ( other.password != null ) {
                return false;
            }
        }
        else if ( !password.equals( other.password ) ) {
            return false;
        }
        if ( role != other.role ) {
            return false;
        }
        if ( username == null ) {
            if ( other.username != null ) {
                return false;
            }
        }
        else if ( !username.equals( other.username ) ) {
            return false;
        }
        return true;
    }

    /**
     * Get the id of this user (aka, the username)
     */
    @Override
    public String getId () {
        return getUsername();
    }

}
