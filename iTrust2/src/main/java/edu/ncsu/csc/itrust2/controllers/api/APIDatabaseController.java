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

import edu.ncsu.csc.itrust2.forms.admin.ICDForm;
import edu.ncsu.csc.itrust2.forms.admin.NDCForm;
import edu.ncsu.csc.itrust2.models.enums.TransactionType;
import edu.ncsu.csc.itrust2.models.persistent.ICD;
import edu.ncsu.csc.itrust2.models.persistent.NDC;
import edu.ncsu.csc.itrust2.utils.LoggerUtil;

/**
 * API controller for the admin to interact with NDC and ICD codes in the
 * database
 *
 * @author dbryan
 */
@RestController
@SuppressWarnings ( { "unchecked", "rawtypes" } )
public class APIDatabaseController extends APIController {

    private static final Gson GSON = new Gson();

    /**
     * Adds an ICD code to the database
     *
     * @param icdf
     *            the form from the frontend
     * @return response
     */
    @PreAuthorize ( "hasRole('ROLE_ADMIN')" )
    @PostMapping ( BASE_PATH + "/addICD" )
    public ResponseEntity addICDEntry ( @RequestBody final ICDForm icdf ) {
        try {
            final ICD icd = new ICD( icdf );
            if ( null != ICD.getById( icd.getId() ) ) {
                return new ResponseEntity( GSON.toJson( "ICD with the id " + icd.getId() + " already exists" ),
                        HttpStatus.CONFLICT );
            }
            if ( null != ICD.getByCode( icd.getCode() ) ) {
                return new ResponseEntity( GSON.toJson( "ICD with the code " + icd.getCode() + " already exists" ),
                        HttpStatus.CONFLICT );
            }

            icd.save();
            LoggerUtil.log( TransactionType.ICD_CREATE,
                    SecurityContextHolder.getContext().getAuthentication().getName() );
            return new ResponseEntity( GSON.toJson( icd ), HttpStatus.OK );
        }
        catch ( final Exception e ) {
            return new ResponseEntity( GSON.toJson( e.getMessage() ), HttpStatus.BAD_REQUEST );
        }
    }

    /**
     * Updates an ICD entry in the database
     *
     * @param id
     *            the id of the ICD entry to update
     * @param icdf
     *            the form from the frontend
     * @return response
     */
    @PreAuthorize ( "hasRole('ROLE_ADMIN')" )
    @PutMapping ( BASE_PATH + "/updateICD/{id}" )
    public ResponseEntity updateICDEntry ( @PathVariable ( "id" ) final Long id, @RequestBody final ICDForm icdf ) {
        try {
            final ICD icd = new ICD( icdf );

            if ( null != icd.getId() && !id.equals( icd.getId() ) ) {
                return new ResponseEntity( GSON.toJson( "The ID provided does not match the ID of the NDC provided" ),
                        HttpStatus.CONFLICT );
            }
            final ICD dbICD = ICD.getById( id );
            if ( null == dbICD ) {
                return new ResponseEntity( GSON.toJson( "No ICD found for id " + id ), HttpStatus.NOT_FOUND );
            }

            // if the new icd code matches another icd code, don't update
            if ( ICD.getByCode( icd.getCode() ) != null
                    && !ICD.getByCode( icd.getCode() ).getId().equals( icd.getId() ) ) {
                return new ResponseEntity( GSON.toJson( "An ICD with code " + icd.getCode() + " already exists" ),
                        HttpStatus.CONFLICT );
            }

            icd.save();
            LoggerUtil.log( TransactionType.ICD_UPDATE,
                    SecurityContextHolder.getContext().getAuthentication().getName() );
            return new ResponseEntity( GSON.toJson( icd ), HttpStatus.OK );
        }
        catch ( final Exception e ) {
            return new ResponseEntity( GSON.toJson( e.getMessage() ), HttpStatus.BAD_REQUEST );
        }
    }

    /**
     * Adds an NDC code to the database
     *
     * @param ndcf
     *            the form from the frontend
     * @return response
     */
    @PreAuthorize ( "hasRole('ROLE_ADMIN')" )
    @PostMapping ( BASE_PATH + "/addNDC" )
    public ResponseEntity addNDCEntry ( @RequestBody final NDCForm ndcf ) {
        try {
            final NDC ndc = new NDC( ndcf );
            if ( null != NDC.getById( ndc.getId() ) ) {
                return new ResponseEntity( GSON.toJson( "NDC with the id " + ndc.getId() + " already exists" ),
                        HttpStatus.CONFLICT );
            }
            if ( null != NDC.getByCode( ndc.getCode() ) ) {
                return new ResponseEntity( GSON.toJson( "NDC with the code " + ndc.getCode() + " already exists" ),
                        HttpStatus.CONFLICT );
            }

            ndc.save();
            LoggerUtil.log( TransactionType.NDC_CREATE,
                    SecurityContextHolder.getContext().getAuthentication().getName() );
            return new ResponseEntity( GSON.toJson( ndc ), HttpStatus.OK );
        }
        catch ( final Exception e ) {
            return new ResponseEntity( GSON.toJson( e.getMessage() ), HttpStatus.BAD_REQUEST );
        }
    }

    /**
     * Updates an NDC entry in the database
     *
     * @param id
     *            the ID of the NDC entry to update
     * @param ndcf
     *            the form from the frontend
     * @return response
     */
    @PreAuthorize ( "hasRole('ROLE_ADMIN')" )
    @PutMapping ( BASE_PATH + "/updateNDC/{id}" )
    public ResponseEntity updateNDCEntry ( @PathVariable ( "id" ) final Long id, @RequestBody final NDCForm ndcf ) {
        try {
            final NDC ndc = new NDC( ndcf );

            if ( null != ndc.getId() && !id.equals( ndc.getId() ) ) {
                return new ResponseEntity( GSON.toJson( "The ID provided does not match the ID of the NDC provided" ),
                        HttpStatus.CONFLICT );
            }
            final NDC dbNDC = NDC.getById( id );
            if ( null == dbNDC ) {
                return new ResponseEntity( GSON.toJson( "No NDC found for id " + id ), HttpStatus.NOT_FOUND );
            }

            // if the new icd code matches another icd code, don't update
            if ( NDC.getByCode( ndc.getCode() ) != null
                    && !NDC.getByCode( ndc.getCode() ).getId().equals( ndc.getId() ) ) {
                return new ResponseEntity( GSON.toJson( "An NDC with code " + ndc.getCode() + " already exists" ),
                        HttpStatus.CONFLICT );
            }

            ndc.save();
            LoggerUtil.log( TransactionType.NDC_UPDATE,
                    SecurityContextHolder.getContext().getAuthentication().getName() );
            return new ResponseEntity( GSON.toJson( ndc ), HttpStatus.OK );
        }
        catch ( final Exception e ) {
            return new ResponseEntity( GSON.toJson( e.getMessage() ), HttpStatus.BAD_REQUEST );
        }
    }

    /**
     * Retrieves a list of all ICD entries in the database
     *
     * @return list of ICD entries
     */
    @GetMapping ( BASE_PATH + "/ICDEntries" )
    public List<ICD> getICDEntries () {
        return ICD.getICDs();
    }

    /**
     * Retrieves a list of all NDC entries in the database
     *
     * @return list of NDC entries
     */
    @GetMapping ( BASE_PATH + "/NDCEntries" )
    public List<NDC> getNDCEntries () {
        return NDC.getNDCs();
    }
}
