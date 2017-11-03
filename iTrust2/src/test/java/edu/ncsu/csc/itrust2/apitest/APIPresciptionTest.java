/**
 *
 */
package edu.ncsu.csc.itrust2.apitest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import edu.ncsu.csc.itrust2.config.RootConfiguration;
import edu.ncsu.csc.itrust2.forms.admin.PrescriptionForm;
import edu.ncsu.csc.itrust2.forms.admin.UserForm;
import edu.ncsu.csc.itrust2.models.enums.Role;
import edu.ncsu.csc.itrust2.models.persistent.Prescription;
import edu.ncsu.csc.itrust2.mvc.config.WebMvcConfiguration;
import edu.ncsu.csc.itrust2.utils.HibernateDataGenerator;

/**
 * Test for the API functionality for interacting with Prescriptions
 *
 * @author Grady Stewart - gtstewar
 *
 */
@RunWith ( SpringJUnit4ClassRunner.class )
@ContextConfiguration ( classes = { RootConfiguration.class, WebMvcConfiguration.class } )
@WebAppConfiguration

public class APIPresciptionTest {

    private MockMvc               mvc;

    @Autowired
    private WebApplicationContext context;

    /**
     * Sets up test
     */
    @Before
    public void setup () {
        mvc = MockMvcBuilders.webAppContextSetup( context ).build();
        HibernateDataGenerator.refreshDB();
    }

    /**
     * Tests getting prescriptions from a non existent patient
     *
     * @throws Exception
     */
    @Test
    @WithMockUser ( username = "hcp", roles = { "USER", "HCP" } )
    public void testPrescriptionAPI () throws Exception {
        // create users
        final UserForm hcp = new UserForm( "hcp", "123456", Role.ROLE_HCP, 1 );
        mvc.perform( post( "/api/v1/users" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( hcp ) ) );

        final UserForm patient = new UserForm( "patient", "123456", Role.ROLE_PATIENT, 1 );
        mvc.perform( post( "/api/v1/users" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( patient ) ) );

        mvc.perform( delete( "/api/v1/prescriptions" ) );

        final PrescriptionForm pform = new PrescriptionForm();
        pform.setDosage( 100 );
        pform.setStartDate( "01/11/2018" );
        pform.setEndDate( "02/02/2018" );
        pform.setNdcDescription( "Androxy" );
        pform.setNdcCode( "0832-0086" );

        pform.setPatient( "patient" );
        pform.setNumRenewals( 2 );

        mvc.perform( post( "/api/v1/prescriptions" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( pform ) ) ).andExpect( status().isOk() );

        final ArrayList<Prescription> plist = (ArrayList<Prescription>) Prescription.getForPatient( "patient" );

        final PrescriptionForm pform2 = new PrescriptionForm( plist.get( plist.size() - 1 ) );
        pform2.setDosage( 200 );
        // System.out.println( "\n\n\n\n\n\n" );
        // System.out.println( pform2.getId() );
        // System.out.println( pform2.getStartDate() );
        // System.out.println( pform2.getEndDate() );
        // System.out.println( pform2.getPatient() );
        // System.out.println( pform2.getNdc() );
        // System.out.println( pform2.getDosage() );
        // System.out.println( pform2.getNumRenewals() );
        // System.out.println( "\n\n\n\n\n\n" );

        mvc.perform( put( "/api/v1/prescriptions/" + pform2.getId() ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( pform2 ) ) ).andExpect( status().isOk() );

        final PrescriptionForm pformInvalid = new PrescriptionForm();
        pformInvalid.setDosage( 100 );
        pformInvalid.setStartDate( "aaaaaaaaaaa" );
        pformInvalid.setEndDate( "02/02/2018" );
        pformInvalid.setNdcDescription( "Androxy" );
        pformInvalid.setNdcCode( "0832-0086" );
        pformInvalid.setPatient( "patient" );
        pformInvalid.setNumRenewals( 2 );

        mvc.perform( post( "/api/v1/prescriptions" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( pformInvalid ) ) ).andExpect( status().isBadRequest() );

        pformInvalid.setStartDate( "01/11/2018" );
        pformInvalid.setDosage( -1 );

        mvc.perform( post( "/api/v1/prescriptions" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( pformInvalid ) ) ).andExpect( status().isBadRequest() );

        pformInvalid.setNdcDescription( "Not a drug" );
        pformInvalid.setNdcCode( "not a drug" );
        pformInvalid.setDosage( 223 );

        mvc.perform( post( "/api/v1/prescriptions" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( pformInvalid ) ) ).andExpect( status().isBadRequest() );

        pformInvalid.setId( pform2.getId() );
        mvc.perform( put( "/api/v1/prescriptions/66" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( pformInvalid ) ) ).andExpect( status().isConflict() );

        pformInvalid.setDosage( 100 );
        pformInvalid.setStartDate( "aaaaaaaaaaa" );
        pformInvalid.setEndDate( "02/02/2018" );
        pformInvalid.setNdcDescription( "Androxy" );
        pformInvalid.setNdcCode( "0832-0086" );
        ;
        pformInvalid.setPatient( "patient" );
        pformInvalid.setNumRenewals( 2 );

        mvc.perform( put( "/api/v1/prescriptions/" + pform2.getId() ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( pformInvalid ) ) ).andExpect( status().isBadRequest() );

        pformInvalid.setStartDate( "01/11/2018" );
        pformInvalid.setDosage( -1 );

        mvc.perform( put( "/api/v1/prescriptions/" + pform2.getId() ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( pformInvalid ) ) ).andExpect( status().isBadRequest() );

        pformInvalid.setNdcDescription( "Not a drug" );
        pformInvalid.setNdcCode( "Not a drug" );
        pformInvalid.setDosage( 223 );

        mvc.perform( put( "/api/v1/prescriptions/" + pform2.getId() ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( pformInvalid ) ) ).andExpect( status().isBadRequest() );

        mvc.perform( delete( "/api/v1/prescriptions" ) );
    }

}
