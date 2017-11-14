package edu.ncsu.csc.itrust2.apitest;

import edu.ncsu.csc.itrust2.config.RootConfiguration;
import edu.ncsu.csc.itrust2.forms.user.PasswordChangeForm;
import edu.ncsu.csc.itrust2.models.persistent.User;
import edu.ncsu.csc.itrust2.mvc.config.WebMvcConfiguration;
import edu.ncsu.csc.itrust2.utils.HibernateDataGenerator;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Tests for the APIPasswordController.
 *
 * @author Galen Abell (gjabell)
 */
@RunWith( SpringJUnit4ClassRunner.class )
@ContextConfiguration( classes = { RootConfiguration.class, WebMvcConfiguration.class } )
@WebAppConfiguration
public class APIPasswordTest {
    /**
     * MockMvc for testing API.
     */
    private MockMvc mvc;

    /**
     * API Context.
     */
    @Autowired
    private WebApplicationContext context;

    /**
     * Setup before tests.
     */
    @Before
    public void setup() {
        mvc = MockMvcBuilders.webAppContextSetup( context ).build();
        HibernateDataGenerator.refreshDB();
    }

    /**
     * Tests the changePassword endpoint.
     *
     * @throws Exception Throws an Exception if something goes wrong.
     */
    @Test
    @WithMockUser( username = "patient", roles = { "USER", "HCP" } )
    public void testChangePassword() throws Exception {
        PasswordChangeForm form = new PasswordChangeForm();
        // Valid password change
        form.setPasswordToken( "123456" );
        form.setNewPassword1( "1234567" );
        form.setNewPassword2( "1234567" );

        mvc.perform( put( "/api/v1/password" )
                .contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( form ) ) )
                .andExpect( status().isOk() );

        // Invalid password - doesn't match current
        mvc.perform( put( "/api/v1/password" )
                .contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( form ) ) )
                .andExpect( status().isConflict() );

        // Invalid password - new password invalid
        form.setPasswordToken( "1234567" );
        form.setNewPassword1( "12345" );
        form.setNewPassword2( "12345" );

        mvc.perform( put( "/api/v1/password" )
                .contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( form ) ) )
                .andExpect( status().isConflict() );

        // Invalid password - new passwords don't match
        form.setNewPassword1( "123456" );
        form.setNewPassword2( "1234567" );

        mvc.perform( put( "/api/v1/password" )
                .contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( form ) ) )
                .andExpect( status().isConflict() );
    }

    /**
     * Tests the resetPassword endpoint.
     *
     * @throws Exception Throws an Exception if something goes wrong.
     */
    @Test
    public void testResetPassword() throws Exception {
        PasswordChangeForm form = new PasswordChangeForm();
        User patient = User.getWhere( "username='patient'" ).get( 0 );
        patient.setResetToken( "TOKEN" );
        patient.setResetTimeout( System.currentTimeMillis() + 600000 );
        patient.save();

        // Invalid password - new password is invalid
        form.setPasswordToken( "TOKEN" );
        form.setNewPassword1( "12345" );
        form.setNewPassword2( "12345" );

        mvc.perform( post( "/api/v1/password/patient" )
                .contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( form ) ) )
                .andExpect( status().isConflict() );

        // Invalid password - new passwords don't match
        form.setNewPassword1( "123456" );
        form.setNewPassword1( "1234567" );

        mvc.perform( post( "/api/v1/password/patient" )
                .contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( form ) ) )
                .andExpect( status().isConflict() );

        // Invalid token (tokens don't match)
        form.setPasswordToken( "TOEKN" );
        form.setNewPassword1( "1234567" );
        form.setNewPassword2( "1234567" );

        mvc.perform( post( "/api/v1/password/patient" )
                .contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( form ) ) )
                .andExpect( status().isConflict() );

        // valid password reset
        form.setPasswordToken( "TOKEN" );

        mvc.perform( post( "/api/v1/password/patient" )
                .contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( form ) ) )
                .andExpect( status().isOk() );

        // Invalid token (user has no token)
        mvc.perform( post( "/api/v1/password/patient" )
                .contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( form ) ) )
                .andExpect( status().isConflict() );

        // Invalid token (token is too old)
        patient.setResetToken( "TOKEN" );
        patient.setResetTimeout( System.currentTimeMillis() - 600000 );

        mvc.perform( post( "/api/v1/password/patient" )
                .contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( form ) ) )
                .andExpect( status().isConflict() );

        // Invalid username - username doesn't exist
        mvc.perform( post( "/api/v1/password/invalid" )
                .contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( form ) ) )
                .andExpect( status().isNotFound() );
    }
}
