/**
 *
 */
package edu.ncsu.csc.itrust2.apitest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import edu.ncsu.csc.itrust2.config.RootConfiguration;
import edu.ncsu.csc.itrust2.forms.admin.NDCForm;
import edu.ncsu.csc.itrust2.forms.admin.PrescriptionForm;
import edu.ncsu.csc.itrust2.forms.admin.UserForm;
import edu.ncsu.csc.itrust2.models.enums.Role;
import edu.ncsu.csc.itrust2.mvc.config.WebMvcConfiguration;

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
    }

    /**
     * Tests getting prescriptions from a non existent patient
     *
     * @throws Exception
     */
    @Test
    public void testPrescriptionAPI () throws Exception {
        final UserForm hcp = new UserForm( "hcp", "123456", Role.ROLE_HCP, 1 );
        mvc.perform( post( "/api/v1/users" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( hcp ) ) );

        final UserForm patient = new UserForm( "patient", "123456", Role.ROLE_PATIENT, 1 );
        mvc.perform( post( "/api/v1/users" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( patient ) ) );

        final NDCForm ndcf = new NDCForm();
        ndcf.setCode( "ZZZZZZ" );
        ndcf.setDescription( "Major Disease" );
        mvc.perform( post( "/api/v1/addNDC" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( ndcf ) ) ).andExpect( status().isOk() );

        mvc.perform( delete( "/api/v1/prescriptions" ) );

        final PrescriptionForm pform = new PrescriptionForm();
        pform.setDosage( 100 );
        pform.setStartDate( "01/11/2018" );
        pform.setEndDate( "02/02/2018" );
        pform.setNdc( "Androxy" );
        pform.setPatient( "patient" );
        pform.setNumRenewals( 2 );

        mvc.perform( post( "/api/v1/prescriptions" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( pform ) ) ).andExpect( status().isOk() );

        pform.setDosage( 150 );
        pform.setId( "0" );

        mvc.perform( put( "/api/v1/officevisits/" + 1 ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( pform ) ) ).andExpect( status().isOk() );

        mvc.perform( delete( "/api/v1/prescriptions" ) );
    }

}
