package edu.ncsu.csc.itrust2.unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

import edu.ncsu.csc.itrust2.forms.admin.ICDForm;
import edu.ncsu.csc.itrust2.models.persistent.ICD;

public class ICDTest {
    @Test
    public void testICD () throws Exception {
        final ICDForm form = new ICDForm();
        form.setId( "1" );
        form.setCode( "A00" );
        form.setDescription( "Cholera" );

        /* VALID ICD code */
        ICD icd = new ICD( form );
        assertEquals( 1, (long) icd.getId() );
        assertEquals( "A00", icd.getCode() );
        assertEquals( "Cholera", icd.getDescription() );

        /* VALID ICD code */
        form.setCode( "A00.01" );
        icd = new ICD( form );
        assertEquals( 1, (long) icd.getId() );
        assertEquals( "A00.01", icd.getCode() );
        assertEquals( "Cholera", icd.getDescription() );

        /* INVALID ICD code - not enough leading digits */
        form.setCode( "A0" );
        try {
            icd = new ICD( form );
            fail();
        }
        catch ( final IllegalArgumentException e ) {
            assertEquals( 1, (long) icd.getId() );
            assertEquals( "A00.01", icd.getCode() );
            assertEquals( "Cholera", icd.getDescription() );
        }

        /* INVALID ICD code - too many leading digits */
        form.setCode( "A000" );
        try {
            icd = new ICD( form );
            fail();
        }
        catch ( final IllegalArgumentException e ) {
            assertEquals( 1, (long) icd.getId() );
            assertEquals( "A00.01", icd.getCode() );
            assertEquals( "Cholera", icd.getDescription() );
        }

        /* INVALID ICD code - decimal w/o digits following */
        form.setCode( "A00." );
        try {
            icd = new ICD( form );
            fail();
        }
        catch ( final IllegalArgumentException e ) {
            assertEquals( 1, (long) icd.getId() );
            assertEquals( "A00.01", icd.getCode() );
            assertEquals( "Cholera", icd.getDescription() );
        }

        /* INVALID ICD code - too many decimals */
        form.setCode( "A00.01.01" );
        try {
            icd = new ICD( form );
            fail();
        }
        catch ( final IllegalArgumentException e ) {
            assertEquals( 1, (long) icd.getId() );
            assertEquals( "A00.01", icd.getCode() );
            assertEquals( "Cholera", icd.getDescription() );
        }
    }
}
