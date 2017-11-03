package edu.ncsu.csc.itrust2.models.persistent;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import edu.ncsu.csc.itrust2.forms.admin.PrescriptionForm;
import edu.ncsu.csc.itrust2.models.enums.Role;
import edu.ncsu.csc.itrust2.utils.DomainObjectCache;

/**
 * Represents a Prescription.
 *
 * @author gjabell
 * @author gtstewar
 */
@Entity
@Table ( name = "Prescriptions" )
public class Prescription extends DomainObject<Prescription> {
    /**
     * Caches the Prescriptions to prevent additional DB calls.
     */
    private static DomainObjectCache<Long, Prescription> cache = new DomainObjectCache<>( Prescription.class );

    /**
     * Gets a Prescription by its id.
     *
     * @param id
     *            The id of the Prescription.
     * @return Returns the Prescription with the given id.
     */
    public static Prescription getById ( final Long id ) {
        Prescription prescription = cache.get( id );
        if ( prescription == null ) {
            try {
                prescription = getWhere( "id = '" + id + "'" ).get( 0 );
                cache.put( id, prescription );
            }
            catch ( final Exception e ) {
                // do nothing
            }
        }
        return prescription;
    }

    /**
     * Gets the Prescriptions for a given patient.
     *
     * @param patientName
     *            The patient username.
     * @return Returns the list of Prescriptions.
     */
    public static List<Prescription> getForPatient ( final String patientName ) {
        return getWhere( " patient_id = '" + patientName + "'" );
    }

    /**
     * Gets the Prescriptions in the database.
     *
     * @return The list of Prescriptions.
     */
    @SuppressWarnings ( "unchecked" )
    public List<Prescription> getPrescriptions () {
        final List<Prescription> prescriptions = (List<Prescription>) getAll( Prescription.class );
        prescriptions.sort( Comparator.comparing( Prescription::getStartDate ) );
        return prescriptions;
    }

    /**
     * Get the list of Prescriptions by the given where query.
     *
     * @param where
     *            The where query.
     * @return Returns the list of Prescriptions.
     */
    @SuppressWarnings ( "unchecked" )
    private static List<Prescription> getWhere ( final String where ) {
        return (List<Prescription>) getWhere( Prescription.class, where );
    }

    /**
     * The default constructor.
     */
    public Prescription () {
    }

    /**
     * Creates a Prescription based on the given PrescriptionForm.
     *
     * @param form
     *            The PrescriptionForm.
     * @throws ParseException
     *             Throws a ParseException if there is an error during creation.
     */
    public Prescription ( final PrescriptionForm form ) throws ParseException {
        if ( form.getId() != null ) {
            setId( Long.parseLong( form.getId() ) );
        }
        if ( form.getNdcCode() == null || form.getNdcDescription() == null ) {
            throw new IllegalArgumentException( "ndc code and description cannot be null" );
        }
        setPatient( User.getByNameAndRole( form.getPatient(), Role.ROLE_PATIENT ) );
        setNdc( NDC.getByCode( form.getNdcCode() ) );
        if ( form.getOfficeVisit() != null ) {
            setVisit( OfficeVisit.getById( Long.parseLong( form.getOfficeVisit() ) ) );
        }
        final SimpleDateFormat sdf = new SimpleDateFormat( "MM/dd/yyyy", Locale.ENGLISH );
        setStartDate( sdf.parse( form.getStartDate() ) );
        setEndDate( sdf.parse( form.getEndDate() ) );
        setNumRenewals( form.getNumRenewals() );
        setDosage( form.getDosage() );
    }

    /**
     * Get the ID.
     *
     * @return The ID.
     */
    @Override
    public Long getId () {
        return id;
    }

    /**
     * Set the ID.
     *
     * @param id
     *            The new ID.
     */
    public void setId ( final Long id ) {
        this.id = id;
    }

    /**
     * Get the NDC.
     *
     * @return The NDC.
     */
    public NDC getNdc () {
        return ndc;
    }

    /**
     * Set the NDC.
     *
     * @param ndc
     *            The new NDC.
     */
    public void setNdc ( final NDC ndc ) {
        this.ndc = ndc;
    }

    /**
     * Get the Patient.
     *
     * @return The Patient.
     */
    public User getPatient () {
        return patient;
    }

    /**
     * Set the Patient.
     *
     * @param patient
     *            The new Patient.
     */
    public void setPatient ( final User patient ) {
        this.patient = patient;
    }

    /**
     * Get the OfficeVisit.
     *
     * @return The OfficeVisit.
     */
    public OfficeVisit getVisit () {
        return visit;
    }

    /**
     * Set the OfficeVisit.
     *
     * @param visit
     *            The new OfficeVisit.
     */
    public void setVisit ( final OfficeVisit visit ) {
        this.visit = visit;
    }

    /**
     * Get the start date.
     *
     * @return The start date.
     */
    public Date getStartDate () {
        return startDate;
    }

    /**
     * Set the start date.
     *
     * @param startDate
     *            The new start date.
     */
    public void setStartDate ( final Date startDate ) {
        this.startDate = startDate;
    }

    /**
     * Get the end date.
     *
     * @return The end date.
     */
    public Date getEndDate () {
        return endDate;
    }

    /**
     * Set the end date.
     *
     * @param endDate
     *            The new end date.
     */
    public void setEndDate ( final Date endDate ) {
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
     * Set the number of of renewals.
     *
     * @param numRenewals
     *            The new number of renewals.
     */
    public void setNumRenewals ( final Integer numRenewals ) {
        this.numRenewals = numRenewals;
    }

    /**
     * Get the dosage.
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
     *            The new dosage.
     */
    public void setDosage ( final Integer dosage ) {
        this.dosage = dosage;
    }

    /**
     * The primary key in the database.
     */
    @Id
    @GeneratedValue ( strategy = GenerationType.AUTO )
    private Long        id;

    /**
     * The NDC.
     */
    @NotNull
    @ManyToOne
    @JoinColumn ( name = "ndc_id" )
    private NDC         ndc;

    /**
     * The Patient.
     */
    @NotNull
    @ManyToOne
    @JoinColumn ( name = "patient_id" )
    private User        patient;

    /**
     * The OfficeVisit.
     */
    @OneToOne
    @JoinColumn ( name = "office_visit_id" )
    private OfficeVisit visit;

    /**
     * The start date.
     */
    @NotNull
    private Date        startDate;

    /**
     * The end date.
     */
    @NotNull
    private Date        endDate;

    /**
     * The number of renewals.
     */
    @NotNull
    private Integer     numRenewals;

    /**
     * The dosage.
     */
    @NotNull
    private Integer     dosage;
}
