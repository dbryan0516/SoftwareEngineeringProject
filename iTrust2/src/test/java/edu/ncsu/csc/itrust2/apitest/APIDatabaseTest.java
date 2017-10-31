package edu.ncsu.csc.itrust2.apitest;
import static org.junit.Assert.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import edu.ncsu.csc.itrust2.forms.admin.ICDForm;
import edu.ncsu.csc.itrust2.forms.admin.NDCForm;
import edu.ncsu.csc.itrust2.models.enums.State;
import edu.ncsu.csc.itrust2.models.persistent.ICD;
import edu.ncsu.csc.itrust2.models.persistent.NDC;
import org.junit.Assert;
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
import edu.ncsu.csc.itrust2.mvc.config.WebMvcConfiguration;

import javax.validation.constraints.AssertTrue;

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
     * Tests that updating an ICD that doesn't exist returns the proper
     * status
     *
     * @throws Exception failed test
     */
    @Test
    public void testUpdateNonExistentICD () throws Exception {
        ICDForm icdf = new ICDForm();
        icdf.setCode("ZZZZZZZ");
        icdf.setDescription("Fake ICD");
        mvc.perform( put( "/api/v1/updateICD/-1" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( icdf ) ) ).andExpect( status().isNotFound() );
    }

    /**
     * Tests that updating an NDC that doesn't exist returns the proper
     * status
     *
     * @throws Exception failed test
     */
    @Test
    public void testUpdateNonExistentNDC () throws Exception {
        NDCForm ndcf = new NDCForm();
        ndcf.setCode("ZZZZZZ");
        ndcf.setDescription("Fake NDC");
        mvc.perform( put( "/api/v1/updateNDC/-1" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( ndcf ) ) ).andExpect( status().isNotFound() );
    }

    /**
     * Tests that updating an ICD returns the proper status
     *
     * @throws Exception failed test
     */
    @Test
    public void testICD () throws Exception {
        ICDForm icdf = new ICDForm();
        icdf.setCode("TEST-1");
        icdf.setDescription("Test object in APIDatabaseTest");
        mvc.perform( post( "/api/v1/addICD" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( icdf ) ) );

        icdf.setDescription("Test update object in APIDatabaseTest");
        ICD icd = ICD.getByCode("TEST-1");
        Long id = null;
        try {
            assert icd != null;
            id = icd.getId();
            icdf.setId(id.toString());
        } catch (NullPointerException e){
            e.printStackTrace();
            fail();
        }
        mvc.perform( put( "/api/v1/updateICD/" + id.toString() ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( icdf ) ) );

        icd = ICD.getByCode("TEST-1");
        try {
            assert icd != null;
            Assert.assertTrue(icd.getDescription().contains("update"));
        } catch (Exception e){
            e.printStackTrace();
            fail();
        }

        mvc.perform( get( "/api/v1/ICDEntries" )).andExpect( status().isOk() )
                .andExpect( content().contentType( MediaType.APPLICATION_JSON_UTF8_VALUE ) );
    }

    /**
     * Tests that updating an NDC returns the proper status
     *
     * @throws Exception failed test
     */
    @Test
    public void testNDC () throws Exception {

        NDCForm ndcf = new NDCForm();
        ndcf.setCode("TEST-1");
        ndcf.setDescription("Test object in APIDatabaseTest");
        mvc.perform( post( "/api/v1/addNDC" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( ndcf ) ) );

        ndcf.setDescription("Test update object in APIDatabaseTest");
        NDC ndc = NDC.getByCode("TEST-1");
        Long id = null;
        try {
            assert ndc != null;
            id = ndc.getId();
            ndcf.setId(id.toString());
        } catch (NullPointerException e){
            e.printStackTrace();
            fail();
        }
        mvc.perform( put( "/api/v1/updateNDC/" + id.toString() ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( ndcf ) ) );

        ndc = NDC.getByCode("TEST-1");
        try {
            assert ndc != null;
            Assert.assertTrue(ndc.getDescription().contains("update"));
        } catch (Exception e){
            e.printStackTrace();
            fail();
        }

        mvc.perform( get( "/api/v1/NDCEntries" ))
                .andExpect( status().isOk() ).andExpect( content().contentType( MediaType.APPLICATION_JSON_UTF8_VALUE ) );
    }
}
