package edu.ncsu.csc.itrust2.unit;

import edu.ncsu.csc.itrust2.forms.admin.NDCForm;
import edu.ncsu.csc.itrust2.models.persistent.NDC;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class NDCFormTest {
    @Test public void ndcFormTest () throws Exception {
        NDC ndc = new NDC();
        ndc.setCode( "11324-003" );
        ndc.setDescription( "Carbon Dioxide" );

        NDCForm form = new NDCForm( ndc );
        assertNull( form.getId() );
        assertEquals( "11324-003", form.getCode() );
        assertEquals( "Carbon Dioxide", form.getDescription() );
    }
}
