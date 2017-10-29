package edu.ncsu.csc.itrust2.unit;

import edu.ncsu.csc.itrust2.forms.admin.NDCForm;
import edu.ncsu.csc.itrust2.models.persistent.NDC;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class NDCTest {
    @Test public void testNDC () throws Exception {
        NDCForm form = new NDCForm();
        form.setId( "1" );
        form.setCode( "11324-003" );
        form.setDescription( "Carbon Dioxide" );

        NDC ndc = new NDC( form );
        assertEquals( 1, (long) ndc.getId() );
        assertEquals( "11324-003", ndc.getCode() );
        assertEquals( "Carbon Dioxide", ndc.getDescription() );
    }
}
