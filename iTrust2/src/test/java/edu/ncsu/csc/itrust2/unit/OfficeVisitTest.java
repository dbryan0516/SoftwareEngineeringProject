package edu.ncsu.csc.itrust2.unit;

import edu.ncsu.csc.itrust2.forms.hcp.OfficeVisitForm;
import edu.ncsu.csc.itrust2.models.enums.AppointmentType;
import edu.ncsu.csc.itrust2.models.persistent.OfficeVisit;
import edu.ncsu.csc.itrust2.models.persistent.Prescription;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class OfficeVisitTest {
    @Test public void testValidDiagnosis () throws Exception {
        OfficeVisitForm form = new OfficeVisitForm();
        form.setDate( "01/01/2017" );
        form.setTime( "2:30 PM" );
        form.setHcp( "hcp" );
        form.setPatient( "patient" );
        form.setNotes( "Test office visit" );
        form.setType( AppointmentType.GENERAL_CHECKUP.toString() );
        form.setHospital( "General Hospital" );
        // guaranteed
        form.setIcd( "Cholera" );

        OfficeVisit visit = new OfficeVisit( form );
        assertEquals( "Cholera", visit.getIcd().getDescription() );
    }

    @Test public void testInvalidDiagnosis () throws Exception {
        OfficeVisitForm form = new OfficeVisitForm();
        form.setDate( "01/01/2017" );
        form.setTime( "2:30 PM" );
        form.setHcp( "hcp" );
        form.setPatient( "patient" );
        form.setNotes( "Test office visit" );
        form.setType( AppointmentType.GENERAL_CHECKUP.toString() );
        form.setHospital( "General Hospital" );
        // guaranteed
        form.setIcd( "-7" );

        int numOfficeVisits = OfficeVisit.getOfficeVisits().size();
        try {
            OfficeVisit visit = new OfficeVisit( form );
            visit.save();
            fail( "Invalid ICD should have failed." );
        }
        catch ( Exception e ) {
            assertEquals( numOfficeVisits, OfficeVisit.getOfficeVisits().size() );
        }
    }

    @Test public void testValidPrescription () throws Exception {
        OfficeVisitForm form = new OfficeVisitForm();
        form.setDate( "01/01/2017" );
        form.setTime( "2:30 PM" );
        form.setHcp( "hcp" );
        form.setPatient( "patient" );
        form.setNotes( "Test office visit" );
        form.setType( AppointmentType.GENERAL_CHECKUP.toString() );
        form.setHospital( "General Hospital" );
        // guaranteed
        form.setPrescription( Prescription.getForPatient( "patient" ).get( 0 ).getId().toString() );

        OfficeVisit visit = new OfficeVisit( form );
        assertEquals( "Androxy", visit.getPrescription().getNdc().getDescription() );
    }

    @Test public void testInvalidPrescription () throws Exception {
        OfficeVisitForm form = new OfficeVisitForm();
        form.setDate( "01/01/2017" );
        form.setTime( "2:30 PM" );
        form.setHcp( "hcp" );
        form.setPatient( "patient" );
        form.setNotes( "Test office visit" );
        form.setType( AppointmentType.GENERAL_CHECKUP.toString() );
        form.setHospital( "General Hospital" );
        // guaranteed
        form.setPrescription( "-1" );

        int numOfficeVisits = OfficeVisit.getOfficeVisits().size();
        try {
            OfficeVisit visit = new OfficeVisit( form );
            visit.save();
            fail( "Invalid Prescription should have failed." );
        }
        catch ( Exception e ) {
            assertEquals( numOfficeVisits, OfficeVisit.getOfficeVisits().size() );
        }
    }
}
