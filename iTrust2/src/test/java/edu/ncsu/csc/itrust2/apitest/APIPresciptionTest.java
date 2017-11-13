/**
 *
 */
package edu.ncsu.csc.itrust2.apitest;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

import com.google.gson.Gson;

import edu.ncsu.csc.itrust2.config.RootConfiguration;
import edu.ncsu.csc.itrust2.forms.admin.UserForm;
import edu.ncsu.csc.itrust2.forms.hcp.OfficeVisitForm;
import edu.ncsu.csc.itrust2.forms.hcp.PrescriptionForm;
import edu.ncsu.csc.itrust2.models.enums.AppointmentType;
import edu.ncsu.csc.itrust2.models.enums.HouseholdSmokingStatus;
import edu.ncsu.csc.itrust2.models.enums.Role;
import edu.ncsu.csc.itrust2.models.persistent.OfficeVisit;
import edu.ncsu.csc.itrust2.models.persistent.Prescription;
import edu.ncsu.csc.itrust2.mvc.config.WebMvcConfiguration;
import edu.ncsu.csc.itrust2.utils.HibernateDataGenerator;

/**
 * Test for the API functionality for interacting with Prescriptions
 *
 * @author Grady Stewart - gtstewar
 *
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

        // valid creation
        final PrescriptionForm pform = new PrescriptionForm();
        pform.setDosage( 100 );
        pform.setStartDate( "01/11/2018" );
        pform.setEndDate( "02/02/2018" );
        pform.setNdcDescription( "Androxy" );
        pform.setNdcCode( "0832-0086-00" );

        pform.setPatient( "patient" );
        pform.setNumRenewals( 2 );

        mvc.perform( post( "/api/v1/prescriptions" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( pform ) ) ).andExpect( status().isOk() );

        final ArrayList<Prescription> plist = (ArrayList<Prescription>) Prescription.getForPatient( "patient" );

        // valid edit
        final PrescriptionForm pform2 = new PrescriptionForm( plist.get( plist.size() - 1 ) );
        pform2.setDosage( 200 );
        mvc.perform( put( "/api/v1/prescriptions/" + pform2.getId() ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( pform2 ) ) ).andExpect( status().isOk() );

        // invalid creations
        final PrescriptionForm pformInvalid = new PrescriptionForm();
        pformInvalid.setDosage( 100 );
        pformInvalid.setStartDate( "aaaaaaaaaaa" );
        pformInvalid.setEndDate( "02/02/2018" );
        pformInvalid.setNdcDescription( "Androxy" );
        pformInvalid.setNdcCode( "0832-0086-00" );
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
        pformInvalid.setNdcCode( "0832-0086-00" );
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

    /**
     * Tests fetching a Prescription associated with an Office Visit
     *
     * @throws Exception
     */
    @Test
    @WithMockUser ( username = "hcp", roles = { "USER", "HCP" } )
    public void testPrescriptionWithOfficeVisit () throws Exception {

        /* Create an Office Visit with a Prescription */
        documentOfficeVisit( "patient", true );

        // Fetch the resulting id
        Long id = OfficeVisit.getForPatient( "patient" ).get( 0 ).getId();

        // Now, verify that the Prescription comes back with the Office Visit
        final PrescriptionForm p = new PrescriptionForm();
        p.setNdcDescription( "Androxy" );
        p.setNdcCode( "0832-0086-00" );
        p.setPatient( "patient" );
        p.setStartDate( "01/07/2018" );
        p.setEndDate( "01/07/2035" );
        p.setNumRenewals( 100 );
        p.setDosage( 3 );
        p.setOfficeVisit( id.toString() );
        final Prescription p2 = new Prescription( p );
        String resultJSON = mvc.perform( get( "/api/v1/prescriptions/officevisit/" + id ) ).andExpect( status().isOk() )
                .andReturn().getResponse().getContentAsString();
        final Prescription result = gson.fromJson( resultJSON, Prescription.class );
        p2.setId( result.getId() );

        // Prescription doesn't have an equals() method, so JSON string
        // comparison will have to do
        assertEquals( gson.toJson( p2 ), resultJSON );

        /* Test the API's response when an Office Visit has no Prescriptions */

        // Create an Office Visit w/o Prescription
        documentOfficeVisit( "patient", false );

        // Fetch the resulting id
        id = OfficeVisit.getForPatient( "patient" ).get( 1 ).getId();

        // Attempt to fetch it's non-existent Prescription
        resultJSON = mvc.perform( get( "/api/v1/prescriptions/officevisit/" + id ) ).andExpect( status().isOk() )
                .andReturn().getResponse().getContentAsString();
        assertEquals( "", resultJSON );
    }

    /**
     * Documents an office visit for either 'patient' or 'TimTheOneYearOld'.
     * Won't work for anyone else b/c BHM
     *
     * @param patient
     * @param includePrescription
     * @throws Exception
     */
    @WithMockUser ( username = "hcp", roles = { "USER", "HCP" } )
    private void documentOfficeVisit ( String patient, boolean includePrescription ) throws Exception {

        /* Create an Office Visit with a Prescription */
        final OfficeVisitForm visit = new OfficeVisitForm();
        visit.setDate( "4/16/2017" );
        visit.setTime( "9:50 AM" );
        visit.setHcp( "hcp" );
        visit.setPatient( patient );
        visit.setNotes( "Test office visit" );
        visit.setType( AppointmentType.GENERAL_CHECKUP.toString() );
        visit.setHospital( "General Hostpital" );

        // Add basic health metrics so that this will work for users 'patient'
        // and 'TimTheOneYearOld'
        visit.setHeight( new Float( 1 ) );
        visit.setWeight( new Float( 1 ) );
        visit.setHeadCircumference( new Float( 1 ) );
        visit.setHouseSmokingStatus( HouseholdSmokingStatus.INDOOR );

        // Add prescription if necessary
        if ( includePrescription ) {
            visit.setNdcDescription( "Androxy" );
            visit.setNdcCode( "0832-0086-00" );
            visit.setStartDate( "01/07/2018" );
            visit.setEndDate( "01/07/2035" );
            visit.setNumRenewals( 100 );
            visit.setDosage( 3 );
        }
        mvc.perform( post( "/api/v1/officevisits" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( visit ) ) ).andExpect( status().isOk() );
    }

    /**
     * Tests fetching a Prescription associated with an Office Visit for
     * currently logged in Patient
     *
     * @throws Exception
     */
    @Test
    @WithMockUser ( username = "patient", roles = { "USER", "PATIENT" } )
    public void testPrescriptionWithOfficeVisitByPatient () throws Exception {

        /* Create an Office Visit for Patient "patient" with a Prescription */
        documentOfficeVisit( "patient", true );

        // Fetch the resulting id
        Long id = OfficeVisit.getForPatient( "patient" ).get( 0 ).getId();

        // Now, verify that the Prescription comes back with the Office Visit
        final PrescriptionForm p = new PrescriptionForm();
        p.setNdcDescription( "Androxy" );
        p.setNdcCode( "0832-0086-00" );
        p.setPatient( "patient" );
        p.setStartDate( "01/07/2018" );
        p.setEndDate( "01/07/2035" );
        p.setNumRenewals( 100 );
        p.setDosage( 3 );
        p.setOfficeVisit( id.toString() );
        final Prescription p2 = new Prescription( p );
        final String resultJSON = mvc.perform( get( "/api/v1/prescriptions/patient/officevisit/" + id ) )
                .andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();
        final Prescription result = gson.fromJson( resultJSON, Prescription.class );
        p2.setId( result.getId() );
        assertEquals( gson.toJson( p2 ), resultJSON );

        /* Try to read another Patients Prescription */

        // Create an OfficeVisit for Patient "TimeTheOneYearOld" with a
        // Prescription
        documentOfficeVisit( "TimTheOneYearOld", true );
        id = OfficeVisit.getForPatient( "TimTheOneYearOld" ).get( 0 ).getId();

        // Attempt to read it - should come back empty
        assertEquals( "", mvc.perform( get( "/api/v1/prescriptions/patient/officevisit/" + id ) )
                .andExpect( status().isOk() ).andReturn().getResponse().getContentAsString() );

        /* Test API's response when a Office Visit has no Prescription */

        documentOfficeVisit( "patient", false );
        id = OfficeVisit.getForPatient( "patient" ).get( 1 ).getId();

        // Should come back empty, since there's no Prescription
        assertEquals( "", mvc.perform( get( "/api/v1/prescriptions/patient/officevisit/" + id ) )
                .andExpect( status().isOk() ).andReturn().getResponse().getContentAsString() );
    }
}
