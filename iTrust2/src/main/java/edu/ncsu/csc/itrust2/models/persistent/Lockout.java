package edu.ncsu.csc.itrust2.models.persistent;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.List;

/**
 * Class for logging account lockouts. When a user gets locked out of their
 * account due to too many failed login attempts it gets logged here. If a user
 * reaches the MAX_LOCKOUT amount then their account is disabled and can only
 * be enabled by a system/database admin.
 *
 * @author dbryan
 */
@Entity
@Table( name = "Lockouts" )
public class Lockout extends DomainObject<Lockout> implements Serializable {

    /**
     * The UID of the lockout
     */
    private static final long                      serialVersionUID = 1L;

    /**
     * The maximum number a users account can be locked out in 24 hours
     */
    public static final int MAX_LOCKOUT = 3;

    /**
     * returns the lockout logs for the number of locks in the past 24 hours
     * 
     *  @SuppressWarnings for Unchecked cast from List<capture#1-of ? extends
     *                   DomainObject> to List<User> Because get all just
     *                   returns a list of DomainObjects, the cast is okay.
     * @param username the username
     * @return a list of logs
     */
    @SuppressWarnings ( "unchecked" )
    public static List<Lockout> getUserLockouts(String username){
        String where = "username=" + username + " ORDER BY timestamp DESC LIMIT " + MAX_LOCKOUT;
        return (List<Lockout>) getWhere( Lockout.class, where );
    }

    /**
     * Get lockouts where the passed query is true
     *
     * @SuppressWarnings for Unchecked cast from List<capture#1-of ? extends
     *                   DomainObject> to List<User> Because get all just
     *                   returns a list of DomainObjects, the cast is okay.
     *
     * @param where
     *            the passed query
     * @return lockouts where the passed query is true
     */
    @SuppressWarnings ( "unchecked" )
    public static List<Lockout> getWhere (final String where ) {
        return (List<Lockout>) getWhere( Lockout.class, where );
    }

    /**
     * Get the id of this lockout
     */
    @Override
    public String getId () {
        return null;
    }

    /** For Hibernate */
    public Lockout () {
    }

    /**
     * The username of the user that is locked out
     */
    private String username;

    /**
     * The timestamp when the user gets locked out
     */
    private Long timestamp;

    /**
     * public constructor
     * @param username the username of the lockout
     * @param timestamp the timestamp of the lockout
     */
    public Lockout(String username, Long timestamp){
        setUsername(username);
        setTimestamp(timestamp);
    }

    /**
     * Gets the username for this lockout
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username for this lockout
     * @param username the username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * gets the timestamp for this lockout
     * @return the timestamp
     */
    public Long getTimestamp() {
        return timestamp;
    }

    /**
     * Sets the timestamp for this lockout
     * @param timestamp the timestamp
     */
    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}
