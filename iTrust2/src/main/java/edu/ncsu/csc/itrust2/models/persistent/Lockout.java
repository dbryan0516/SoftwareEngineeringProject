package edu.ncsu.csc.itrust2.models.persistent;


import edu.ncsu.csc.itrust2.utils.DomainObjectCache;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.List;

@Entity
@Table( name = "Users" )
public class Lockout extends DomainObject<User> implements Serializable {

    /**
     * The UID of the lockout
     */
    private static final long                      serialVersionUID = 1L;

    /**
     * The cache representation of the user in the database
     */
    static private DomainObjectCache<String, User> cache            = new DomainObjectCache<String, User>( User.class );

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

    public Lockout(String username, Long timestamp){
        setUsername(username);
        setTimestamp(timestamp);
    }

    /**
     * Gets the username for this lockout
     * @return
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username for this lockout
     * @param username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * gets the timestamp for this lockout
     * @return
     */
    public Long getTimestamp() {
        return timestamp;
    }

    /**
     * Sets the timestamp for this lockout
     * @param timestamp
     */
    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}
