package edu.ncsu.csc.itrust2.unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

import edu.ncsu.csc.itrust2.forms.hcp.OfficeVisitForm;
import edu.ncsu.csc.itrust2.models.enums.AppointmentType;
import edu.ncsu.csc.itrust2.models.persistent.OfficeVisit;

public class OfficeVisitTest {
    @Test
    public void testValidDiagnosis () throws Exception {
        final OfficeVisitForm form = new OfficeVisitForm();
        form.setDate( "01/01/2017" );
        form.setTime( "2:30 PM" );
        form.setHcp( "hcp" );
        form.setPatient( "patient" );
        form.setNotes( "Test office visit" );
        form.setType( AppointmentType.GENERAL_CHECKUP.toString() );
        form.setHospital( "General Hospital" );
        // guaranteed
        form.setIcd( "A00" );

        final OfficeVisit visit = new OfficeVisit( form );
        assertEquals( "Cholera", visit.getIcd().getDescription() );
    }

    @Test
    public void testInvalidDiagnosis () throws Exception {
        final OfficeVisitForm form = new OfficeVisitForm();
        form.setDate( "01/01/2017" );
        form.setTime( "2:30 PM" );
        form.setHcp( "hcp" );
        form.setPatient( "patient" );
        form.setNotes( "Test office visit" );
        form.setType( AppointmentType.GENERAL_CHECKUP.toString() );
        form.setHospital( "General Hospital" );
        // guaranteed
        form.setIcd( "-7" );

        final int numOfficeVisits = OfficeVisit.getOfficeVisits().size();
        try {
            final OfficeVisit visit = new OfficeVisit( form );
            visit.save();
            fail( "Invalid ICD should have failed." );
        }
        catch ( final Exception e ) {
            assertEquals( numOfficeVisits, OfficeVisit.getOfficeVisits().size() );
        }
    }
}
