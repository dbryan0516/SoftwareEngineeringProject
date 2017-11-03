package edu.ncsu.csc.itrust2.forms.admin;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Locale;

import org.hibernate.validator.constraints.NotEmpty;

import edu.ncsu.csc.itrust2.models.persistent.Prescription;

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
     * @param prescription
     *            The Prescription.
     */
    public PrescriptionForm ( final Prescription prescription ) {
        setId( prescription.getId() == null ? null : prescription.getId().toString() );
        setNdcDescription( prescription.getNdc().getDescription() );
        setNdcCode( prescription.getNdc().getCode() );
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
     * @param id
     *            The ID.
     */
    public void setId ( final String id ) {
        this.id = id;
    }

    /**
     * Get the NDC description.
     *
     * @return The NDC.
     */
    public String getNdcDescription () {
        return ndcDescription;
    }

    /**
     * Set the NDC description.
     *
     * @param ndc
     *            description.
     */
    public void setNdcDescription ( final String ndc ) {
        this.ndcDescription = ndc;
    }

    /**
     * @return the ndcCode
     */
    public String getNdcCode () {
        return ndcCode;
    }

    /**
     * @param ndcCode
     *            the ndcCode to set
     */
    public void setNdcCode ( final String ndcCode ) {
        this.ndcCode = ndcCode;
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
     * @param patient
     *            The Patient.
     */
    public void setPatient ( final String patient ) {
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
     * @param officeVisit
     *            The OfficeVisit.
     */
    public void setOfficeVisit ( final String officeVisit ) {
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
     * @param startDate
     *            The start date.
     */
    public void setStartDate ( final String startDate ) {
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
     * @param endDate
     *            The end date.
     */
    public void setEndDate ( final String endDate ) {
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
     * @param numRenewals
     *            The number of renewals.
     */
    public void setNumRenewals ( final Integer numRenewals ) {
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
     * @param dosage
     *            The dosage.
     */
    public void setDosage ( final Integer dosage ) {
        this.dosage = dosage;
    }

    /**
     * The ID of the Prescription.
     */
    @NotEmpty
    private String  id;

    /**
     * The description associated with the NDC of the Prescription.
     */
    @NotEmpty
    private String  ndcDescription;

    /**
     * The ndc code
     */
    @NotEmpty
    private String  ndcCode;

    /**
     * The Patient of the Prescription.
     */
    @NotEmpty
    private String  patient;

    /**
     * The office visit of the Prescription.
     */
    private String  officeVisit;

    /**
     * The start date of the prescription.
     */
    @NotEmpty
    private String  startDate;

    /**
     * The end date of the prescription.
     */
    @NotEmpty
    private String  endDate;

    /**
     * The number of renewals of the prescription.
     */
    @NotEmpty
    private Integer numRenewals;

    /**
     * The dosage of the prescription.
     */
    @NotEmpty
    private Integer dosage;
}
