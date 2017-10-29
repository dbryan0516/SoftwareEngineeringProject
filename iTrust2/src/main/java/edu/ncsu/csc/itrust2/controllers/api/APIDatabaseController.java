package edu.ncsu.csc.itrust2.controllers.api;

import edu.ncsu.csc.itrust2.forms.admin.ICDForm;
import edu.ncsu.csc.itrust2.forms.admin.NDCForm;
import edu.ncsu.csc.itrust2.models.persistent.ICD;
import edu.ncsu.csc.itrust2.models.persistent.NDC;
import edu.ncsu.csc.itrust2.utils.LoggerUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@SuppressWarnings ( { "unchecked", "rawtypes" } )
public class APIDatabaseController extends APIController{

    @PostMapping ( BASE_PATH + "/addICD" )
    public ResponseEntity addICDEntry(@RequestBody final ICDForm icdf){
        try {
            final ICD icd = new ICD( icdf );
            if ( null != ICD.getById( icd.getId() ) ) {
                return new ResponseEntity( "ICD with the id " + icd.getId() + " already exists",
                        HttpStatus.CONFLICT );
            }
            icd.save();
            //TODO: ADD LOG
//            LoggerUtil.log( TransactionType.APPOINTMENT_REQUEST_SUBMITTED, request.getPatient(), request.getHcp() );
            return new ResponseEntity( icd, HttpStatus.OK );
        }
        catch ( final Exception e ) {
            return new ResponseEntity(
                    "Error occured while validating or saving " + icdf.toString() + " because of " + e.getMessage(),
                    HttpStatus.BAD_REQUEST );
        }
    }

    @PutMapping ( BASE_PATH + "/updateICD/{id}" )
    public ResponseEntity updateICDEntry(@PathVariable ("id") final Long id, @RequestBody final ICDForm icdf){
        try {
            final ICD icd = new ICD( icdf );

            if ( null != icd.getId() && !id.equals( icd.getId() ) ) {
                return new ResponseEntity( "The ID provided does not match the ID of the NDC provided",
                        HttpStatus.CONFLICT );
            }
            final ICD dbICD = ICD.getById( id );
            if ( null == dbICD ) {
                return new ResponseEntity( "No ICD found for id " + id, HttpStatus.NOT_FOUND );
            }

            icd.save();
//            LoggerUtil.log( TransactionType.APPOINTMENT_REQUEST_UPDATED, request.getPatient(), request.getHcp() );
            return new ResponseEntity( icd, HttpStatus.OK );
        }
        catch ( final Exception e ) {
            return new ResponseEntity( "Could not update " + icdf.toString() + " because of " + e.getMessage(),
                    HttpStatus.BAD_REQUEST );
        }
    }

    @PostMapping (BASE_PATH + "/addNDC")
    public ResponseEntity addNDCEntry(@RequestBody final NDCForm ndcf){
        try {
        final NDC ndc = new NDC( ndcf );
        if ( null != NDC.getById( ndc.getId() ) ) {
            return new ResponseEntity( "NDC with the id " + ndc.getId() + " already exists",
                    HttpStatus.CONFLICT );
        }
        ndc.save();
        //TODO: ADD LOG
//            LoggerUtil.log( TransactionType.APPOINTMENT_REQUEST_SUBMITTED, request.getPatient(), request.getHcp() );
        return new ResponseEntity( ndc, HttpStatus.OK );
    }
    catch ( final Exception e ) {
        return new ResponseEntity(
                "Error occured while validating or saving " + ndcf.toString() + " because of " + e.getMessage(),
                HttpStatus.BAD_REQUEST );
    }
    }

    @PutMapping (BASE_PATH + "/updateNDC/{id}")
    public ResponseEntity updateNDCEntry(@PathVariable ("id") final Long id, @RequestBody final NDCForm ndcf){
        try {
            final NDC ndc = new NDC( ndcf );

            if ( null != ndc.getId() && !id.equals( ndc.getId() ) ) {
                return new ResponseEntity( "The ID provided does not match the ID of the NDC provided",
                        HttpStatus.CONFLICT );
            }
            final NDC dbNDC = NDC.getById( id );
            if ( null == dbNDC ) {
                return new ResponseEntity( "No NDC found for id " + id, HttpStatus.NOT_FOUND );
            }

            ndc.save();
//            LoggerUtil.log( TransactionType.APPOINTMENT_REQUEST_UPDATED, request.getPatient(), request.getHcp() );
            return new ResponseEntity( ndc, HttpStatus.OK );
        }
        catch ( final Exception e ) {
            return new ResponseEntity( "Could not update " + ndcf.toString() + " because of " + e.getMessage(),
                    HttpStatus.BAD_REQUEST );
        }
    }

    @GetMapping (BASE_PATH + "/ICDEntries")
    public List<ICD> getICDEntries(){
        return ICD.getICDs();
    }

    @GetMapping (BASE_PATH + "/ICDEntries")
    public List<NDC> getNDCEntries(){
        return NDC.getNDCs();
    }
}
