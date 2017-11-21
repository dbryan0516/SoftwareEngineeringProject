package edu.ncsu.csc.itrust2.controllers.hcp;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.ncsu.csc.itrust2.models.enums.TransactionType;
import edu.ncsu.csc.itrust2.utils.LoggerUtil;

/**
 * Controller class responsible for managing the behavior for the HCP Landing
 * Screen
 *
 * @author Kai Presler-Marshall
 *
 */
@Controller
public class HCPController {

    /**
     * Returns the Landing screen for the HCP
     *
     * @return The page to display
     */
    @RequestMapping ( value = "hcp/index" )
    @PreAuthorize ( "hasRole('ROLE_HCP')" )
    public String index () {
        return edu.ncsu.csc.itrust2.models.enums.Role.ROLE_HCP.getLanding();
    }

    /**
     * Returns the page for an HCP to view Patient's prescriptions.
     * 
     * @return The page to display.
     */
    @RequestMapping ( value = "hcp/viewPrescriptions" )
    @PreAuthorize ( "hasRole('ROLE_HCP')" )
    public String viewPrescriptions () {
        final String hcp = SecurityContextHolder.getContext().getAuthentication().getName();
        LoggerUtil.log( TransactionType.PRESCRIPTION_VIEW_HCP, hcp, hcp + " viewed all prescriptions they prescribed" );
        return "/hcp/viewPrescriptions";
    }
}
