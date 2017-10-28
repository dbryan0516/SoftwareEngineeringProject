package edu.ncsu.csc.itrust2.forms.admin;

import edu.ncsu.csc.itrust2.models.persistent.ICD;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * Represents an ICD form.
 *
 * @author gjabell
 * @author gtstewar
 */
public class ICDForm {
    /**
     * ICD id.
     */
    private String id;

    /**
     * ICD code.
     */
    @NotEmpty private String code;

    /**
     * ICD description.
     */
    @NotEmpty private String description;

    /**
     * Default constructor.
     */
    public ICDForm () {
    }

    /**
     * Constructs an ICD form based on the given ICD.
     *
     * @param icd The ICD.
     */
    public ICDForm ( final ICD icd ) {
        setId( icd.getId() == null ? null : icd.getId().toString() );
        setCode( icd.getCode() );
        setDescription( icd.getDescription() );
    }

    /**
     * Retrieves the ID of the ICD.
     *
     * @return The IDC of the ICD.
     */
    public String getId () {
        return id;
    }

    /**
     * Sets the ID of the ICD (used by Hibernate).
     *
     * @param id The new ID of the ICD.
     */
    public void setId ( String id ) {
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
}
