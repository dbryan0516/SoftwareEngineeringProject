package edu.ncsu.csc.itrust2.apitest;

import static org.junit.Assert.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.After;
import org.junit.Assert;
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
import edu.ncsu.csc.itrust2.forms.admin.ICDForm;
import edu.ncsu.csc.itrust2.forms.admin.NDCForm;
import edu.ncsu.csc.itrust2.models.persistent.ICD;
import edu.ncsu.csc.itrust2.models.persistent.NDC;
import edu.ncsu.csc.itrust2.mvc.config.WebMvcConfiguration;
import edu.ncsu.csc.itrust2.utils.HibernateDataGenerator;

/**
 * Test for the API functionality for interacting with appointment requests
 *
 * @author Kai Presler-Marshall
 *
 */
@RunWith ( SpringJUnit4ClassRunner.class )
@ContextConfiguration ( classes = { RootConfiguration.class, WebMvcConfiguration.class } )
@WebAppConfiguration
public class APIDatabaseTest {

    private MockMvc               mvc;

    @Autowired
    private WebApplicationContext context;

    /**
     * Sets up tests
     */
    @Before
    public void setup () {
        mvc = MockMvcBuilders.webAppContextSetup( context ).build();
    }

    /**
     * Reset DB after each test
     */
    @After
    public void tearDown () {
        HibernateDataGenerator.refreshDB();
    }

    /**
     * Tests that updating an ICD that doesn't exist returns the proper status
     *
     * @throws Exception
     *             failed test
     */
    @WithMockUser(roles="ADMIN")
    @Test
    public void testUpdateNonExistentICD () throws Exception {
        final ICDForm icdf = new ICDForm();
        icdf.setCode( "Z99" );
        icdf.setDescription( "Fake ICD" );
        mvc.perform( put( "/api/v1/updateICD/-1" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( icdf ) ) ).andExpect( status().isNotFound() );
    }

    /**
     * Tests that updating an NDC that doesn't exist returns the proper status
     *
     * @throws Exception
     *             failed test
     */
    @WithMockUser(roles="ADMIN")
    @Test
    public void testUpdateNonExistentNDC () throws Exception {
        final NDCForm ndcf = new NDCForm();
        ndcf.setCode( "0000-0000-00" );
        ndcf.setDescription( "Fake NDC" );
        mvc.perform( put( "/api/v1/updateNDC/-1" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( ndcf ) ) ).andExpect( status().isNotFound() );
    }

    /**
     * Tests that updating an ICD returns the proper status
     *
     * @throws Exception
     *             failed test
     */
    @WithMockUser(roles="ADMIN")
    @Test
    public void testICD () throws Exception {

        /* Create a VALID ICD */
        ICDForm icdf = new ICDForm();
        icdf.setCode( "A00" );
        icdf.setDescription( "Test object in APIDatabaseTest" );
        mvc.perform( post( "/api/v1/addICD" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( icdf ) ) );

        /* Update a VALID ICD */
        icdf.setDescription( "Test update object in APIDatabaseTest" );
        ICD icd = ICD.getByCode( "A00" );
        Long id = null;
        try {
            assert icd != null;
            id = icd.getId();
            icdf.setId( id.toString() );
        }
        catch ( final NullPointerException e ) {
            e.printStackTrace();
            fail();
        }
        mvc.perform( put( "/api/v1/updateICD/" + id.toString() ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( icdf ) ) );

        // Verify the update worked
        icd = ICD.getByCode( "A00" );
        try {
            assert icd != null;
            Assert.assertTrue( icd.getDescription().contains( "update" ) );
        }
        catch ( final Exception e ) {
            e.printStackTrace();
            fail();
        }
        mvc.perform( get( "/api/v1/ICDEntries" ) ).andExpect( status().isOk() )
                .andExpect( content().contentType( MediaType.APPLICATION_JSON_UTF8_VALUE ) );

        /* Try to create a INVALID ICD */
        icdf = new ICDForm();
        icdf.setCode( "A0" );
        icdf.setDescription( "Garbage object in APIDatabaseTest" );
        mvc.perform( post( "/api/v1/addICD" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( icdf ) ) ).andExpect( status().isBadRequest() );

        /* Try to update a VALID ICD to an INVALID ICD */
        icdf.setDescription( "Garbage update object in APIDatabaseTest" );
        icdf.setCode( "A" );
        icd = ICD.getByCode( "A00" );
        id = null;
        try {
            assert icd != null;
            id = icd.getId();
            icdf.setId( id.toString() );
        }
        catch ( final NullPointerException e ) {
            e.printStackTrace();
            fail();
        }
        mvc.perform( put( "/api/v1/updateICD/" + id.toString() ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( icdf ) ) ).andExpect( status().isBadRequest() );
    }

    /**
     * Tests that updating an NDC returns the proper status
     *
     * @throws Exception
     *             failed test
     */
    @WithMockUser(roles="ADMIN")
    @Test
    public void testNDC () throws Exception {

        /* Create a VALID NDC */
        NDCForm ndcf = new NDCForm();
        ndcf.setCode( "1234-5678-90" );
        ndcf.setDescription( "Test object in APIDatabaseTest" );
        mvc.perform( post( "/api/v1/addNDC" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( ndcf ) ) );

        /* Update a VALID NDC */
        ndcf.setDescription( "Test update object in APIDatabaseTest" );
        NDC ndc = NDC.getByCode( "1234-5678-90" );
        Long id = null;
        try {
            assert ndc != null;
            id = ndc.getId();
            ndcf.setId( id.toString() );
        }
        catch ( final NullPointerException e ) {
            e.printStackTrace();
            fail();
        }
        mvc.perform( put( "/api/v1/updateNDC/" + id.toString() ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( ndcf ) ) );

        // Verify the update worked
        ndc = NDC.getByCode( "1234-5678-90" );
        try {
            assert ndc != null;
            Assert.assertTrue( ndc.getDescription().contains( "update" ) );
        }
        catch ( final Exception e ) {
            e.printStackTrace();
            fail();
        }

        mvc.perform( get( "/api/v1/NDCEntries" ) ).andExpect( status().isOk() )
                .andExpect( content().contentType( MediaType.APPLICATION_JSON_UTF8_VALUE ) );

        /* Try to create an INVALID NDC */
        ndcf = new NDCForm();
        ndcf.setCode( "1234-5678-900" );
        ndcf.setDescription( "Garbage object in APIDatabaseTest" );
        mvc.perform( post( "/api/v1/addNDC" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( ndcf ) ) ).andExpect( status().isBadRequest() );

        /* Try to update a VALID NDC into an INVALID NDC */
        ndcf.setDescription( "Garbage update object in APIDatabaseTest" );
        ndcf.setCode( "1234-5678-900" );
        ndc = NDC.getByCode( "1234-5678-90" );
        id = null;
        try {
            assert ndc != null;
            id = ndc.getId();
            ndcf.setId( id.toString() );
        }
        catch ( final NullPointerException e ) {
            e.printStackTrace();
            fail();
        }
        mvc.perform( put( "/api/v1/updateNDC/" + id.toString() ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( ndcf ) ) ).andExpect( status().isBadRequest() );
    }
}
