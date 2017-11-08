package edu.ncsu.csc.itrust2.controllers.api;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

import edu.ncsu.csc.itrust2.forms.hcp.PrescriptionForm;
import edu.ncsu.csc.itrust2.models.enums.TransactionType;
import edu.ncsu.csc.itrust2.models.persistent.Prescription;
import edu.ncsu.csc.itrust2.models.persistent.User;
import edu.ncsu.csc.itrust2.utils.LoggerUtil;

/**
 * API endpoints for prescriptions.
 *
 * @author gtstewar
 */
@RestController
@SuppressWarnings ( { "unchecked", "rawtypes" } )
public class APIPrescriptionController extends APIController {

    /**
     * Retrieves list of prescriptions for a given patient - must be hcp logged
     * in
     *
     * @param id
     *            - id of patient to get prescription of
     * @return list of prescriptions
     */
    @GetMapping ( BASE_PATH + "/prescriptions/hcp/{id}" )
    @PreAuthorize ( "hasRole('ROLE_HCP')" )
    public List<Prescription> getPrescriptionsByPatient ( @PathVariable ( "id" ) final String id ) {
        return Prescription.getForPatient( id );
    }

    /**
     * Retrieves a list of all prescriptions for current patient logged in
     *
     * @return list of prescriptions
     */
    @GetMapping ( BASE_PATH + "/prescriptions/patient/myprescriptions" )
    @PreAuthorize ( "hasRole('ROLE_PATIENT')" )
    public List<Prescription> getMyPrescriptions () {
        final User self = User.getByName( SecurityContextHolder.getContext().getAuthentication().getName() );
        return Prescription.getForPatient( self.getId() );
    }

    /**
     * Updates the Prescription with the id provided by overwriting it with the
     * new Prescription that is provided. If the ID provided does not match the
     * ID set in the Prescription provided, the update will not take place
     *
     * @param id
     *            The ID of the prescription to be updated
     * @param form
     *            The updated prescription to save
     * @return response
     */
    @PutMapping ( BASE_PATH + "/prescriptions/{id}" )
    @PreAuthorize ( "hasRole('ROLE_HCP')" )
    public ResponseEntity updatePrescription ( @PathVariable final Long id, @RequestBody final PrescriptionForm form ) {
        final Gson builder = new Gson();
        try {
            final Prescription prescription = new Prescription( form );
            if ( null != prescription.getId() && !id.equals( prescription.getId() ) ) {
                return new ResponseEntity(
                        builder.toJson( "The ID provided does not match the ID of the Presciption provided" ),
                        HttpStatus.CONFLICT );
            }
            final Prescription dbPrescription = Prescription.getById( id );
            if ( null == dbPrescription ) {
                return new ResponseEntity(
                        builder.toJson( "The prescription to be updated does not exist in the database" ),
                        HttpStatus.NOT_FOUND );
            }
            // validate data that is not checked in persistent object
            if ( prescription.getStartDate().after( prescription.getEndDate() ) ) {
                return new ResponseEntity(
                        builder.toJson(
                                "start date must be after curent date and end date can't be before start date" ),
                        HttpStatus.BAD_REQUEST );
            }
            else if ( prescription.getDosage() <= 0 ) {
                return new ResponseEntity( builder.toJson( "Dosage must be positive" ), HttpStatus.BAD_REQUEST );
            }
            else if ( prescription.getNumRenewals() < 0 ) {
                return new ResponseEntity( builder.toJson( "Renewals must be greater than or equal to zero" ),
                        HttpStatus.BAD_REQUEST );
            }
            prescription.save(); /* Will overwrite existing request */
            // log appropriately
            final String hcp = SecurityContextHolder.getContext().getAuthentication().getName();
            final String patient = prescription.getPatient().getUsername();
            LoggerUtil.log( TransactionType.PRESCRIPTION_EDIT, hcp, patient,
                    hcp + " edited a prescription for " + patient );
            return new ResponseEntity( builder.toJson( "Prescription was added successfully" ), HttpStatus.OK );

        }
        catch ( final Exception e ) {
            return new ResponseEntity(
                    builder.toJson( "Could not update " + form.toString() + " because of " + e.getMessage() ),
                    HttpStatus.BAD_REQUEST );
        }
    }

    /**
     * Creates and saves a new Prescription from the RequestBody provided.
     *
     * @param form
     *            The prescription to be validated and saved
     * @return response
     */
    @PostMapping ( BASE_PATH + "/prescriptions" )
    @PreAuthorize ( "hasRole('ROLE_HCP')" )
    public ResponseEntity createPrescription ( @RequestBody final PrescriptionForm form ) {
        final Gson builder = new Gson();
        try {
            final Prescription prescription = new Prescription( form );
            if ( null != Prescription.getById( prescription.getId() ) ) {
                return new ResponseEntity(
                        builder.toJson( "Office visit with the id " + prescription.getId() + " already exists" ),
                        HttpStatus.CONFLICT );
            }
            // validate data that is not checked in persistent object
            if ( prescription.getStartDate().after( prescription.getEndDate() ) ) {
                return new ResponseEntity(
                        builder.toJson( "Dates must be after current date and end date must be after start date" ),
                        HttpStatus.BAD_REQUEST );
            }
            else if ( prescription.getDosage() <= 0 ) {
                return new ResponseEntity( builder.toJson( "Dosage must be positive" ), HttpStatus.BAD_REQUEST );
            }
            else if ( prescription.getNumRenewals() < 0 ) {
                return new ResponseEntity( builder.toJson( "Renewals must be greater than or equal to 0" ),
                        HttpStatus.BAD_REQUEST );
            }
            // save to db
            prescription.save();
            // log
            final String hcp = SecurityContextHolder.getContext().getAuthentication().getName();
            final String patient = prescription.getPatient().getUsername();
            LoggerUtil.log( TransactionType.PRESCRIPTION_CREATE, hcp, patient,
                    hcp + " created a prescription for " + patient );
            return new ResponseEntity( builder.toJson( "Prescription added successfully" ), HttpStatus.OK );
        }
        catch ( final Exception e ) {
            return new ResponseEntity( builder.toJson( e.getMessage() ), HttpStatus.BAD_REQUEST );
        }
    }

    // // delete method just in case
    // /**
    // * Deletes the Prescription with the id provided. This will remove all
    // * traces from the system and cannot be reversed.
    // *
    // * @param id
    // * The id of the Prescription to delete
    // * @return response
    // */
    // @DeleteMapping ( BASE_PATH + "/prescriptions/{id}" )
    // public ResponseEntity deleteOfficeVisitByID ( @PathVariable final Long id
    // ) {
    // final Prescription prescription = Prescription.getById( id );
    // if ( null == prescription ) {
    // return new ResponseEntity( "No prescription found for " + id,
    // HttpStatus.NOT_FOUND );
    // }
    // try {
    // prescription.delete();
    // return new ResponseEntity( id, HttpStatus.OK );
    // }
    // catch ( final Exception e ) {
    // return new ResponseEntity( "Could not delete " + id + " because of " +
    // e.getMessage(),
    // HttpStatus.BAD_REQUEST );
    // }
    //
    // }
    //
    // /**
    // * Deletes all the Prescriptions from the db. This will remove all traces
    // * from the system and cannot be reversed.
    // *
    // * @param id
    // * The id of the Prescription to delete
    // * @return response
    // */
    // @DeleteMapping ( BASE_PATH + "/prescriptions" )
    // public void deleteAllOfficeVisits () {
    // Prescription.deleteAll( Prescription.class );
    // }

}
