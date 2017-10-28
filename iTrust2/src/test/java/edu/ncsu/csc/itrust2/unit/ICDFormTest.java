package edu.ncsu.csc.itrust2.unit;

import edu.ncsu.csc.itrust2.forms.admin.ICDForm;
import edu.ncsu.csc.itrust2.models.persistent.ICD;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ICDFormTest {
    @Test public void icdFormTest () throws Exception {
        ICD icd = new ICD();
        icd.setCode( "A00" );
        icd.setDescription( "Cholera" );

        ICDForm form = new ICDForm( icd );
        assertNull( form.getId() );
        assertEquals( "A00", form.getCode() );
        assertEquals( "Cholera", form.getDescription() );
    }
}
