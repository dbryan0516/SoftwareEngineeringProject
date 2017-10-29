package edu.ncsu.csc.itrust2.forms.admin;

import edu.ncsu.csc.itrust2.models.persistent.NDC;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * Represents an NDC form.
 *
 * @author gjabell
 * @author gtstewar
 */
public class NDCForm {
    /**
     * NDC id.
     */
    private String id;

    /**
     * NDC code.
     */
    @NotEmpty private String code;

    /**
     * NDC description.
     */
    @NotEmpty private String description;

    /**
     * Default constructor.
     */
    public NDCForm () {
    }

    /**
     * Constructs an NDC form based on the given NDC.
     *
     * @param ndc The NDC.
     */
    public NDCForm ( final NDC ndc ) {
        setId( ndc.getId() == null ? null : ndc.getId().toString() );
        setCode( ndc.getCode() );
        setDescription( ndc.getDescription() );
    }

    /**
     * Retrieves the ID of the NDC.
     *
     * @return The IDC of the NDC.
     */
    public String getId () {
        return id;
    }

    /**
     * Sets the ID of the NDC (used by Hibernate).
     *
     * @param id The new ID of the NDC.
     */
    public void setId ( String id ) {
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
     * @param code The new NDC code.
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
     * @param description The new NDC description.
     */
    public void setDescription ( String description ) {
        this.description = description;
    }
}
