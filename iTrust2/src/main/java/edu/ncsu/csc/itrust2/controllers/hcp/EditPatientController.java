package edu.ncsu.csc.itrust2.controllers.hcp;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Serves up webpages allowing the HCP to edit patient demographics.
 *
 * @author Galen Abell (gjabell)
 */
@Controller
public class EditPatientController {
    @GetMapping("/hcp/editPatient")
    @PreAuthorize("hasRole('ROLE_HCP')")
    public String editPatient() {
        return "/hcp/editPatient";
    }
}
