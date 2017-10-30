package edu.ncsu.csc.itrust2.controllers.admin;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * This class serves up any webpages that deal with administering to the
 * database of NDC and ICD codes
 *
 * @author Joshua Kayani (jkayani)
 *
 */
@Controller
public class ModifyDatabaseController {

    /**
     * Serves the modifyDatabase page for the admin to modify NDC and ICD codes
     *
     * @return The path to the right webpage
     */
    @GetMapping ( "/admin/modifyDatabase" )
    @PreAuthorize ( "hasRole('ROLE_ADMIN')" )
    public String modifyDatabase () {
        return "/admin/modifyDatabase";
    }
}
