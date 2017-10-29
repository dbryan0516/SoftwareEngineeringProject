package edu.ncsu.csc.itrust2.models.persistent;

import edu.ncsu.csc.itrust2.forms.admin.ICDForm;
import edu.ncsu.csc.itrust2.utils.DomainObjectCache;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.util.List;

/**
 * Represents an ICD entry.
 *
 * @author gjabell
 * @author gtstewar
 */
@Entity @Table ( name = "ICD" ) public class ICD extends DomainObject<ICD> {
    /**
     * In-memory cache for storing instances of the ICDs in the database to avoid database transactions.
     */
    static private DomainObjectCache<Long, ICD> cache = new DomainObjectCache<>( ICD.class );

    /**
     * Get the ICD with the given ID.
     *
     * @param id The ID of the ICD.
     * @return Returns the ICD with the given id.
     */
    public static ICD getById ( final Long id ) {
        ICD icd = cache.get( id );
        if ( icd == null ) {
            try {
                icd = getWhere( "id = '" + id + "'" ).get( 0 );
                cache.put( id, icd );
            }
            catch ( final Exception e ) {
                // ignore the exception
            }
        }
        return icd;
    }

    /**
     * Gets the ICD with the given code from the database.
     *
     * @param code The code of the ICD.
     * @return Returns the ICD with the given code, or null if it doesn't exist.
     */
    public static ICD getByCode ( final String code ) {
        List<ICD> icds = getWhere( "code = '" + code + "'" );
        return icds.size() > 0 ? icds.get( 0 ) : null;
    }

    /**
     * Gets the ICD with the given description from the database.
     *
     * @param description The description of the ICD.
     * @return Returns the ICD with the given description, or null if it doesn't exist.
     */
    public static ICD getByDescription ( final String description ) {
        List<ICD> icds = getWhere( "description = '" + description + "'" );
        return icds.size() > 0 ? icds.get( 0 ) : null;
    }

    /**
     * Retrieve a List of all ICDs in the database. Returns the ICDs sorted by id.
     *
     * @return A List of all ICDs in the database.
     */
    @SuppressWarnings ( "unchecked" ) public static List<ICD> getICDs () {
        return (List<ICD>) getAll( ICD.class );
    }

    /**
     * Used by Hibernate to construct and load objects.
     */
    public ICD () {
    }

    /**
     * Retrieve a List of ICDs that meet the given where query. Query must be valid SQL.
     *
     * @param where The Query by which to find ICDs.
     * @return The List of ICDs.
     */
    @SuppressWarnings ( "unchecked" ) private static List<ICD> getWhere ( final String where ) {
        return (List<ICD>) getWhere( ICD.class, where );
    }

    /**
     * Constructs an ICD object from the given ICDForm.
     *
     * @param form The ICDForm.
     */
    public ICD ( final ICDForm form ) {
        setId( Long.valueOf( form.getId() ) );
        setCode( form.getCode() );
        setDescription( form.getDescription() );
    }

    /**
     * Retrieves the ID of the ICD.
     *
     * @return The IDC of the ICD.
     */
    @Override public Long getId () {
        return id;
    }

    /**
     * Sets the ID of the ICD (used by Hibernate).
     *
     * @param id The new ID of the ICD.
     */
    private void setId ( final Long id ) {
        this.id = id;
    }

    /**
     * Gets the ICD code.
     *
     * @return The ICD code.
     */
    public String getCode () {
        return code;
    }

    /**
     * Sets the ICD code.
     *
     * @param code The new ICD code.
     */
    public void setCode ( String code ) {
        this.code = code;
    }

    /**
     * Gets the ICD description.
     *
     * @return The ICD description.
     */
    public String getDescription () {
        return description;
    }

    /**
     * Sets the ICD description.
     *
     * @param description The new ICD description.
     */
    public void setDescription ( String description ) {
        this.description = description;
    }

    /**
     * Database id (primary key).
     */
    @Id @GeneratedValue ( strategy = GenerationType.AUTO ) private Long id;

    /**
     * The ICD code.
     */
    @NotEmpty private String code;

    /**
     * The ICD description.
     */
    @NotEmpty private String description;

    /**
     * Checks if the given object is equal to this.
     *
     * @param o The object on which to test.
     * @return Returns true if the other object has the same id, code, and description, otherwise false.
     */
    @Override public boolean equals ( Object o ) {
        return o != null && o instanceof ICD && this.id.equals( ( (ICD) o ).getId() ) && this.code
                .equals( ( (ICD) o ).getCode() ) && this.description.equals( ( (ICD) o ).getDescription() );
    }
}
