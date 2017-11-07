package edu.ncsu.csc.itrust2.controllers.hcp;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * This class serves up any webpages that deal with HCP's creating Prescriptions
 * for Patients, with or without OfficeVisits
 *
 * @author Joshua Kayani (jkayani)
 *
 */
@Controller
public class PrescriptionController {

    /**
     * Serves the modifyDatabase page for the admin to modify NDC and ICD codes
     *
     * @return The path to the right webpage
     */
    @GetMapping ( "/hcp/documentPrescription" )
    @PreAuthorize ( "hasRole('ROLE_HCP')" )
    public String documentPrescription () {
        return "/hcp/documentPrescription";
    }
}
