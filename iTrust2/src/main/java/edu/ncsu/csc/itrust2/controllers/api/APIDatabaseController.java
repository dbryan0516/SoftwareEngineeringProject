package edu.ncsu.csc.itrust2.controllers.api;

import edu.ncsu.csc.itrust2.forms.admin.ICDForm;
import edu.ncsu.csc.itrust2.forms.admin.NDCForm;
import edu.ncsu.csc.itrust2.models.persistent.ICD;
import edu.ncsu.csc.itrust2.models.persistent.NDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class APIDatabaseController extends APIController{

    @GetMapping ( BASE_PATH + "/addICD" )
    public ResponseEntity addICDEntry(@RequestBody final ICDForm icdf){
        return new ResponseEntity( null, HttpStatus.OK );
    }

    @GetMapping ( BASE_PATH + "/updateICD/{id}" )
    public ResponseEntity updateICDEntry(@PathVariable ("id") final Long id, @RequestBody final ICDForm icdf){
        return new ResponseEntity( null, HttpStatus.OK );
    }

    @GetMapping (BASE_PATH + "/addNDC")
    public ResponseEntity addNDCEntry(@RequestBody final NDCForm ndcf){
        return new ResponseEntity( null, HttpStatus.OK );
    }

    @GetMapping (BASE_PATH + "/updateNDC/{id}")
    public ResponseEntity updateNDCEntry(@PathVariable ("id") final Long id, @RequestBody final NDCForm ndcf){
        return new ResponseEntity( null, HttpStatus.OK );

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
