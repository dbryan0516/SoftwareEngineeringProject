package edu.ncsu.csc.itrust2.unit;

import edu.ncsu.csc.itrust2.forms.admin.ICDForm;
import edu.ncsu.csc.itrust2.models.persistent.ICD;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ICDTest {
    @Test public void testICD () throws Exception {
        ICDForm form = new ICDForm();
        form.setId( "1" );
        form.setCode( "A00" );
        form.setDescription( "Cholera" );

        ICD icd = new ICD( form );
        assertEquals( 1, (long) icd.getId() );
        assertEquals( "A00", icd.getCode() );
        assertEquals( "Cholera", icd.getDescription() );
    }
}
