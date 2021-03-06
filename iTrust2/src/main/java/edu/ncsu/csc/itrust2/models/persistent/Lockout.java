package edu.ncsu.csc.itrust2.models.persistent;

import edu.ncsu.csc.itrust2.utils.DomainObjectCache;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
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

    static private DomainObjectCache<Long, Lockout> cache = new DomainObjectCache<>( Lockout.class );

    /**
     * returns the lockout logs for the number of locks in the past 24 hours
     * 
     *  @SuppressWarnings for Unchecked cast from List<capture#1-of ? extends
     *                   DomainObject> to List<Lockout> Because get all just
     *                   returns a list of DomainObjects, the cast is okay.
     * @param username the username
     * @return a list of logs
     */
    @SuppressWarnings ( "unchecked" )
    public static List<Lockout> getUserLockouts(String username){
        String where = "username='" + username + "' ORDER BY timestamp DESC";
        return (List<Lockout>) getWhere( Lockout.class, where );
    }

    /**
     * Get lockouts where the passed query is true
     *
     * @SuppressWarnings for Unchecked cast from List<capture#1-of ? extends
     *                   DomainObject> to List<Lockout> Because get all just
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
    public Long getId () {
        return id;
    }

    /**
     * Sets the id (needed for Hibernate).
     * @param id The new ID.
     */
    public void setId ( Long id ) {
        this.id = id;
    }

    /** For Hibernate */
    public Lockout () {
    }

    /**
     * The id of the Lockout.
     */
    @Id
    @GeneratedValue ( strategy = GenerationType.AUTO )
    private Long id;

    /**
     * The username of the user that is locked out
     */
    @NotNull
    private String username;

    /**
     * The timestamp when the user gets locked out
     */
    @NotNull
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
