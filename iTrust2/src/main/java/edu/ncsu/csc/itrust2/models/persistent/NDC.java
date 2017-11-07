package edu.ncsu.csc.itrust2.models.persistent;

import java.util.List;
import java.util.regex.Pattern;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotEmpty;

import edu.ncsu.csc.itrust2.forms.admin.NDCForm;
import edu.ncsu.csc.itrust2.utils.DomainObjectCache;

/**
 * Represents an NDC entry.
 *
 * @author gjabell
 * @author gtstewar
 */
@Entity
@Table ( name = "NDC" )
public class NDC extends DomainObject<NDC> {
    /**
     * In-memory cache for storing instances of the NDCs in the database to
     * avoid database transactions.
     */
    static private DomainObjectCache<Long, NDC> cache = new DomainObjectCache<>( NDC.class );

    /**
     * Get the NDC with the given ID.
     *
     * @param id
     *            The ID of the NDC.
     * @return Returns the NDC with the given id.
     */
    public static NDC getById ( final Long id ) {
        NDC ndc = cache.get( id );
        if ( ndc == null ) {
            try {
                ndc = getWhere( "id = '" + id + "'" ).get( 0 );
                cache.put( id, ndc );
            }
            catch ( final Exception e ) {
                // ignore the exception
            }
        }
        return ndc;
    }

    /**
     * Gets the NDC with the given code from the database.
     *
     * @param code
     *            The code of the NDC.
     * @return Returns the NDC with the given code, or null if it doesn't exist.
     */
    public static NDC getByCode ( final String code ) {
        final List<NDC> ndcs = getWhere( "code = '" + code + "'" );
        return ndcs.size() > 0 ? ndcs.get( 0 ) : null;
    }

    /**
     * Gets the NDC with the given description from the database.
     *
     * @param description
     *            The description of the NDC.
     * @return Returns the NDC with the given description, or null if it doesn't
     *         exist.
     */
    public static NDC getByDescription ( final String description ) {
        final List<NDC> ndcs = getWhere( "description = '" + description + "'" );
        return ndcs.size() > 0 ? ndcs.get( 0 ) : null;
    }

    /**
     * Retrieve a List of all NDCs in the database. Returns the NDCs sorted by
     * id.
     *
     * @return A List of all NDCs in the database.
     */
    @SuppressWarnings ( "unchecked" )
    public static List<NDC> getNDCs () {
        return (List<NDC>) getAll( NDC.class );
    }

    /**
     * Used by Hibernate to construct and load objects.
     */
    public NDC () {
    }

    /**
     * Retrieve a List of NDCs that meet the given where query. Query must be
     * valid SQL.
     *
     * @param where
     *            The Query by which to find NDCs.
     * @return The List of NDCs.
     */
    @SuppressWarnings ( "unchecked" )
    private static List<NDC> getWhere ( final String where ) {
        return (List<NDC>) getWhere( NDC.class, where );
    }

    /**
     * Constructs an NDC object from the given NDCForm.
     *
     * @param form
     *            The NDCForm.
     */
    public NDC ( final NDCForm form ) {
        if ( form.getId() != null ) {
            setId( Long.valueOf( form.getId() ) );
        }

        /*
         * Valid codes are in the form: 1234-5678-90, 12345-678-90, or
         * 12345-6789-0
         * https://www.accessdata.fda.gov/scripts/cder/ndc/default.cfm
         */
        if ( !Pattern.matches( "^(\\d{4}-\\d{4}-\\d{2})|(\\d{5}-\\d{4}-\\d{1})|(\\d{5}-\\d{3}-\\d{2})$",
                form.getCode() ) ) {
            throw new IllegalArgumentException( "NDC code doesn't follow valid NDC format" );
        }
        setCode( form.getCode() );
        setDescription( form.getDescription() );
    }

    /**
     * Retrieves the ID of the NDC.
     *
     * @return The IDC of the NDC.
     */
    @Override
    public Long getId () {
        return id;
    }

    /**
     * Sets the ID of the NDC (used by Hibernate).
     *
     * @param id
     *            The new ID of the NDC.
     */
    private void setId ( final Long id ) {
        this.id = id;
    }

    /**
     * Gets the NDC code.
     *
     * @return The NDC code.
     */
    public String getCode () {
        return code;
    }

    /**
     * Sets the NDC code.
     *
     * @param code
     *            The new NDC code.
     */
    public void setCode ( String code ) {
        this.code = code;
    }

    /**
     * Gets the NDC description.
     *
     * @return The NDC description.
     */
    public String getDescription () {
        return description;
    }

    /**
     * Sets the NDC description.
     *
     * @param description
     *            The new NDC description.
     */
    public void setDescription ( String description ) {
        this.description = description;
    }

    /**
     * Database id (primary key).
     */
    @Id
    @GeneratedValue ( strategy = GenerationType.AUTO )
    private Long   id;

    /**
     * The NDC code.
     */
    @NotEmpty
    private String code;

    /**
     * The NDC description.
     */
    @NotEmpty
    private String description;

    /**
     * Checks if the given object is equal to this.
     *
     * @param o
     *            The object on which to test.
     * @return Returns true if the other object has the same id, code, and
     *         description, otherwise false.
     */
    @Override
    public boolean equals ( Object o ) {
        return o != null && o instanceof NDC && this.id.equals( ( (NDC) o ).getId() )
                && this.code.equals( ( (NDC) o ).getCode() ) && this.description.equals( ( (NDC) o ).getDescription() );
    }
}
