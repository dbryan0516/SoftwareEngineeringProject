package edu.ncsu.csc.itrust2.forms.admin;

import edu.ncsu.csc.itrust2.models.persistent.Prescription;
import org.hibernate.validator.constraints.NotEmpty;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Represents a PrescriptionForm.
 *
 * @author gjabell
 * @author gtstewar
 */
public class PrescriptionForm implements Serializable {
    /**
     * Serial UID for serializable.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Default constructor for Hibernate.
     */
    public PrescriptionForm () {
    }

    /**
     * Makes a PrescriptionForm based on a Prescription.
     *
     * @param prescription The Prescription.
     */
    public PrescriptionForm ( Prescription prescription ) {
        setId( prescription.getId() == null ? null : prescription.getId().toString() );
        setNdc( prescription.getNdc().getDescription() );
        setPatient( prescription.getPatient().getUsername() );
        final SimpleDateFormat date = new SimpleDateFormat( "MM/dd/yyyy", Locale.ENGLISH );
        setOfficeVisit( prescription.getVisit() == null ? null : prescription.getVisit().getId().toString() );
        setStartDate( date.format( prescription.getStartDate() ) );
        setEndDate( date.format( prescription.getEndDate() ) );
        setNumRenewals( prescription.getNumRenewals() );
        setDosage( prescription.getDosage() );
    }

    /**
     * Get the ID.
     *
     * @return The ID.
     */
    public String getId () {
        return id;
    }

    /**
     * Set the ID.
     *
     * @param id The ID.
     */
    public void setId ( String id ) {
        this.id = id;
    }

    /**
     * Get the NDC.
     *
     * @return The NDC.
     */
    public String getNdc () {
        return ndc;
    }

    /**
     * Set the NDC.
     *
     * @param ndc The NDC.
     */
    public void setNdc ( String ndc ) {
        this.ndc = ndc;
    }

    /**
     * Get the Patient.
     *
     * @return The Patient.
     */
    public String getPatient () {
        return patient;
    }

    /**
     * Set the Patient.
     *
     * @param patient The Patient.
     */
    public void setPatient ( String patient ) {
        this.patient = patient;
    }

    /**
     * Get the OfficeVisit.
     *
     * @return The OfficeVisit.
     */
    public String getOfficeVisit () {
        return officeVisit;
    }

    /**
     * Set the OfficeVisit.
     *
     * @param officeVisit The OfficeVisit.
     */
    public void setOfficeVisit ( String officeVisit ) {
        this.officeVisit = officeVisit;
    }

    /**
     * Get the start date.
     *
     * @return The start date.
     */
    public String getStartDate () {
        return startDate;
    }

    /**
     * Set the start date.
     *
     * @param startDate The start date.
     */
    public void setStartDate ( String startDate ) {
        this.startDate = startDate;
    }

    /**
     * Get the end date.
     *
     * @return The end date.
     */
    public String getEndDate () {
        return endDate;
    }

    /**
     * Set the end date.
     *
     * @param endDate The end date.
     */
    public void setEndDate ( String endDate ) {
        this.endDate = endDate;
    }

    /**
     * Get the number of renewals.
     *
     * @return The number of renewals.
     */
    public Integer getNumRenewals () {
        return numRenewals;
    }

    /**
     * Sets the number of renewals.
     *
     * @param numRenewals The number of renewals.
     */
    public void setNumRenewals ( Integer numRenewals ) {
        this.numRenewals = numRenewals;
    }

    /**
     * Gets the dosage.
     *
     * @return The dosage.
     */
    public Integer getDosage () {
        return dosage;
    }

    /**
     * Sets the dosage.
     *
     * @param dosage The dosage.
     */
    public void setDosage ( Integer dosage ) {
        this.dosage = dosage;
    }

    /**
     * The ID of the Prescription.
     */
    @NotEmpty private String id;

    /**
     * The NDC of the Prescription.
     */
    @NotEmpty private String ndc;

    /**
     * The Patient of the Prescription.
     */
    @NotEmpty private String patient;

    /**
     * The office visit of the Prescription.
     */
    private String officeVisit;

    /**
     * The start date of the prescription.
     */
    @NotEmpty private String startDate;

    /**
     * The end date of the prescription.
     */
    @NotEmpty private String endDate;

    /**
     * The number of renewals of the prescription.
     */
    @NotEmpty private Integer numRenewals;

    /**
     * The dosage of the prescription.
     */
    @NotEmpty private Integer dosage;
}
