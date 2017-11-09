package edu.ncsu.csc.itrust2.unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

import edu.ncsu.csc.itrust2.forms.admin.NDCForm;
import edu.ncsu.csc.itrust2.models.persistent.NDC;

/**
 * Just tests the NDC code format rules
 * 
 * @author Joshua Kayani (jkayani)
 *
 */
public class NDCTest {
    @Test
    public void testNDC () throws Exception {
        final NDCForm form = new NDCForm();
        form.setId( "1" );
        form.setCode( "11324-003" );
        form.setDescription( "Carbon Dioxide" );
        NDC ndc = null;

        /* INVALID - too few digits and dashes */
        try {
            ndc = new NDC( form );
            fail();
        }
        catch ( final IllegalArgumentException e ) {
            assertEquals( null, ndc );
        }

        /* INVALID - too many digits and dashes */
        form.setCode( "123456-789-00" );
        try {
            ndc = new NDC( form );
            fail();
        }
        catch ( final IllegalArgumentException e ) {
            assertEquals( null, ndc );
        }

        /* VALID - 1234-5678-90 */
        form.setCode( "1234-5678-90" );
        try {
            ndc = new NDC( form );
        }
        catch ( final IllegalArgumentException e ) {
            assertEquals( "1234-5678-90", ndc );
            assertEquals( 1, (long) ndc.getId() );
            assertEquals( "Carbon Dioxide", ndc.getDescription() );
        }

        /* VALID - 12345-678-90 */
        form.setCode( "12345-678-90" );
        try {
            ndc = new NDC( form );
        }
        catch ( final IllegalArgumentException e ) {
            assertEquals( "12345-678-90", ndc );
            assertEquals( 1, (long) ndc.getId() );
            assertEquals( "Carbon Dioxide", ndc.getDescription() );
        }

        /* VALID - 12345-6789-0 */
        form.setCode( "12345-6789-0" );
        try {
            ndc = new NDC( form );
        }
        catch ( final IllegalArgumentException e ) {
            assertEquals( "12345-6789-0", ndc );
            assertEquals( 1, (long) ndc.getId() );
            assertEquals( "Carbon Dioxide", ndc.getDescription() );
        }
    }
}
