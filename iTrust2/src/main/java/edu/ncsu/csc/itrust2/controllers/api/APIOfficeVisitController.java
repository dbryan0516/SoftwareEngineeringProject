package edu.ncsu.csc.itrust2.controllers.api;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import edu.ncsu.csc.itrust2.forms.hcp.OfficeVisitForm;
import edu.ncsu.csc.itrust2.forms.hcp.PrescriptionForm;
import edu.ncsu.csc.itrust2.models.enums.TransactionType;
import edu.ncsu.csc.itrust2.models.persistent.OfficeVisit;
import edu.ncsu.csc.itrust2.models.persistent.Prescription;
import edu.ncsu.csc.itrust2.models.persistent.User;
import edu.ncsu.csc.itrust2.utils.LoggerUtil;

/**
 * Class that provides REST API endpoints for the OfficeVisit model. In all
 * requests made to this controller, the {id} provided is a Long that is the
 * primary key id of the office visit requested.
 *
 * @author Kai Presler-Marshall
 *
 */
@RestController
@SuppressWarnings ( { "unchecked", "rawtypes" } )
public class APIOfficeVisitController extends APIController {

    /**
     * Retrieves a list of all OfficeVisits in the database
     *
     * @return list of office visits
     * @deprecated This should be removed as it provides too much information to
     *             the outside world without authentication.
     */
    @Deprecated
    @GetMapping ( BASE_PATH + "/officevisits" )
    public List<OfficeVisit> getOfficeVisits () {
        return OfficeVisit.getOfficeVisits();
    }

    /**
     * Retrieves a list of all OfficeVisits in the database
     *
     * @return list of office visits
     */
    @GetMapping ( BASE_PATH + "/officevisits/myofficevisits" )
    @PreAuthorize ( "hasRole('ROLE_PATIENT')" )
    public List<OfficeVisit> getMyOfficeVisits () {
        final User self = User.getByName( SecurityContextHolder.getContext().getAuthentication().getName() );
        return OfficeVisit.getForPatient( self.getId() );
    }

    /**
     * Retrieves the OfficeVisit specified by the id provided.
     *
     * @param id
     *            The (numeric) ID of the OfficeVisit desired
     * @return response
     */
    @GetMapping ( BASE_PATH + "/officevisits/{id}" )
    public ResponseEntity getOfficeVisit ( @PathVariable ( "id" ) final Long id ) {
        final OfficeVisit visit = OfficeVisit.getById( id );
        return null == visit ? new ResponseEntity( "No office visit found for id " + id, HttpStatus.NOT_FOUND )
                : new ResponseEntity( visit, HttpStatus.OK );
    }

    /**
     * Deletes all OfficeVisits in the system. This cannot be reverse; exercise
     * caution before calling it
     */
    @DeleteMapping ( BASE_PATH + "/officevisits" )
    public void deleteOfficeVisits () {
        OfficeVisit.deleteAll( OfficeVisit.class );
    }

    /**
     * Creates and saves a new OfficeVisit from the RequestBody provided.
     *
     * @param visitF
     *            The office visit to be validated and saved
     * @return response
     */
    @PostMapping ( BASE_PATH + "/officevisits" )
    public ResponseEntity createOfficeVisit ( @RequestBody final OfficeVisitForm visitF ) {
        // keep track of the status of office visit just in case prescription
        // doesn't save correctly
        boolean savedVisit = false;
        long officeVisitID = -1;
        try {
            final OfficeVisit visit = new OfficeVisit( visitF );
            if ( null != OfficeVisit.getById( visit.getId() ) ) {
                return new ResponseEntity( "Office visit with the id " + visit.getId() + " already exists",
                        HttpStatus.CONFLICT );
            }
            visit.save();
            officeVisitID = visit.getId();
            savedVisit = true;
            // check if prescription exists
            if ( visitF.getNdcCode() != null && visitF.getStartDate() != null && visitF.getEndDate() != null
                    && visitF.getDosage() != null && visitF.getNumRenewals() != null ) {
                // make prescription object and save
                final PrescriptionForm pform = new PrescriptionForm();
                pform.setNdcCode( visitF.getNdcCode() );
                pform.setNdcDescription( visitF.getNdcDescription() );
                pform.setStartDate( visitF.getStartDate() );
                pform.setEndDate( visitF.getEndDate() );
                pform.setDosage( visitF.getDosage() );
                pform.setNumRenewals( visitF.getNumRenewals() );
                pform.setPatient( visitF.getPatient() );
                pform.setOfficeVisit( visit.getId().toString() );
                final Prescription prescription = new Prescription( pform );
                prescription.save();
            }
            return new ResponseEntity( visit, HttpStatus.OK );
        }
        catch ( final Exception e ) {
            if ( savedVisit ) {
                OfficeVisit.getById( officeVisitID ).delete();
            }
            return new ResponseEntity( "Could not validate or save the OfficeVisit provided due to " + e.getMessage(),
                    HttpStatus.BAD_REQUEST );
        }
    }

    /**
     * Deletes the OfficeVisit with the id provided. This will remove all traces
     * from the system and cannot be reversed.
     *
     * @param id
     *            The id of the OfficeVisit to delete
     * @return response
     */
    @DeleteMapping ( BASE_PATH + "/officevisits/{id}" )
    public ResponseEntity deleteOfficeVisit ( @PathVariable final Long id ) {
        final OfficeVisit visit = OfficeVisit.getById( id );
        if ( null == visit ) {
            return new ResponseEntity( "No office visit found for " + id, HttpStatus.NOT_FOUND );
        }
        try {
            // find prescription that office visit references if it exists
            final Prescription prescription = (Prescription) Prescription
                    .getWhere( Prescription.class, " office_visit_id = " + visit.getId() ).get( 0 );
            if ( prescription != null ) {
                // delete reference to office visit
                prescription.setVisit( null );
                prescription.save();
            }
            visit.delete();
            return new ResponseEntity( id, HttpStatus.OK );
        }
        catch ( final Exception e ) {
            return new ResponseEntity( "Could not delete " + id + " because of " + e.getMessage(),
                    HttpStatus.BAD_REQUEST );
        }

    }

    /**
     * Updates the OfficeVisit with the id provided by overwriting it with the
     * new OfficeVisit that is provided. If the ID provided does not match the
     * ID set in the OfficeVisit provided, the update will not take place
     *
     * @param id
     *            The ID of the OfficeVisit to be updated
     * @param form
     *            The updated OfficeVisit to save
     * @return response
     */
    @PutMapping ( BASE_PATH + "/officevisits/{id}" )
    public ResponseEntity updateOfficeVisit ( @PathVariable final Long id, @RequestBody final OfficeVisitForm form ) {
        try {
            final OfficeVisit visit = new OfficeVisit( form );
            if ( null != visit.getId() && !id.equals( visit.getId() ) ) {
                return new ResponseEntity( "The ID provided does not match the ID of the OfficeVisit provided",
                        HttpStatus.CONFLICT );
            }
            final OfficeVisit dbVisit = OfficeVisit.getById( id );
            if ( null == dbVisit ) {
                return new ResponseEntity( "No visit found for name " + id, HttpStatus.NOT_FOUND );
            }
            // It is possible that the HCP did not update the BHM but only the
            // other fields (date, time, etc) thus we need to check if the old
            // BHM is different from the new BHM before logging
            if ( !dbVisit.getBasicHealthMetrics().equals( visit.getBasicHealthMetrics() ) ) {
                LoggerUtil.log( TransactionType.OFFICE_VISIT_EDIT, form.getHcp(), form.getPatient(), form.getHcp()
                        + " updated basic health metrics for " + form.getPatient() + " from " + form.getDate() );
            }
            visit.save(); /* Will overwrite existing request */
            return new ResponseEntity( visit, HttpStatus.OK );
        }
        catch ( final Exception e ) {
            return new ResponseEntity( "Could not update " + form.toString() + " because of " + e.getMessage(),
                    HttpStatus.BAD_REQUEST );
        }
    }

    /**
     * This is used as a marker for the system to know that the HCP has viewed
     * the visit
     *
     * @param id
     *            The id of the office visit being viewed
     * @param form
     *            The office visit being viewed
     * @return OK if the office visit is found, NOT_FOUND otherwise
     */
    @PostMapping ( BASE_PATH + "/officevisits/hcp/view/{id}" )
    @PreAuthorize ( "hasRole('ROLE_HCP')" )
    public ResponseEntity viewOfficeVisitHCP ( @PathVariable final Long id, @RequestBody final OfficeVisitForm form ) {
        final OfficeVisit dbVisit = OfficeVisit.getById( id );
        if ( null == dbVisit ) {
            return new ResponseEntity( "No visit found for name " + id, HttpStatus.NOT_FOUND );
        }
        LoggerUtil.log( TransactionType.OFFICE_VISIT_HCP_VIEW, form.getHcp(), form.getPatient(),
                form.getHcp() + " viewed basic health metrics for " + form.getPatient() + " from " + form.getDate() );
        return new ResponseEntity( HttpStatus.OK );
    }

    /**
     * This is used as a marker for the system to know that the patient has
     * viewed the visit
     *
     * @param id
     *            The id of the office visit being viewed
     * @param form
     *            The office visit being viewed
     * @return OK if the office visit is found, NOT_FOUND otherwise
     */
    @PostMapping ( BASE_PATH + "/officevisits/patient/view/{id}" )
    @PreAuthorize ( "hasRole('ROLE_PATIENT')" )
    public ResponseEntity viewOfficeVisitPatient ( @PathVariable final Long id,
            @RequestBody final OfficeVisitForm form ) {
        final OfficeVisit dbVisit = OfficeVisit.getById( id );
        if ( null == dbVisit ) {
            return new ResponseEntity( "No visit found for name " + id, HttpStatus.NOT_FOUND );
        }
        LoggerUtil.log( TransactionType.OFFICE_VISIT_PATIENT_VIEW, form.getHcp(), form.getPatient(),
                form.getPatient() + " viewed their basic health metrics from " + form.getDate() );
        return new ResponseEntity( HttpStatus.OK );
    }

}
