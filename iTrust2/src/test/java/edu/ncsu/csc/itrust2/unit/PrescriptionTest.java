package edu.ncsu.csc.itrust2.unit;

import edu.ncsu.csc.itrust2.forms.admin.PrescriptionForm;
import edu.ncsu.csc.itrust2.models.persistent.NDC;
import edu.ncsu.csc.itrust2.models.persistent.Prescription;
import edu.ncsu.csc.itrust2.models.persistent.User;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import static org.junit.Assert.*;

public class PrescriptionTest {
    @Test public void testValidPrescription () throws Exception {
        PrescriptionForm prescriptionForm = new PrescriptionForm();
        prescriptionForm.setId( "1" );
        // guaranteed to have these in the db from the db generator
        prescriptionForm.setNdc( "Androxy" );
        prescriptionForm.setPatient( "patient" );
        prescriptionForm.setOfficeVisit( null );
        prescriptionForm.setStartDate( "01/01/2017" );
        prescriptionForm.setEndDate( "01/01/2035" );
        prescriptionForm.setNumRenewals( 1000 );
        prescriptionForm.setDosage( 50000 );

        Prescription prescription = new Prescription( prescriptionForm );
        assertEquals( 1, (long) prescription.getId() );
        assertEquals( NDC.getByDescription( "Androxy" ), prescription.getNdc() );
        assertEquals( User.getByName( "patient" ), prescription.getPatient() );
        assertNull( prescription.getVisit() );
        SimpleDateFormat format = new SimpleDateFormat( "MM/dd/yyyy", Locale.ENGLISH );
        assertEquals( "01/01/2017", format.format( prescription.getStartDate() ) );
        assertEquals( "01/01/2035", format.format( prescription.getEndDate() ) );
        assertEquals( 1000, (int) prescription.getNumRenewals() );
        assertEquals( 50000, (int) prescription.getDosage() );
    }

    @Test public void testInvalidNDC () throws Exception {
        PrescriptionForm prescriptionForm = new PrescriptionForm();
        prescriptionForm.setId( "1" );
        prescriptionForm.setNdc( "!!!!!!!" );
        prescriptionForm.setPatient( "patient" );
        prescriptionForm.setOfficeVisit( null );
        prescriptionForm.setStartDate( "01/01/2017" );
        prescriptionForm.setEndDate( "01/01/2035" );
        prescriptionForm.setNumRenewals( 1000 );
        prescriptionForm.setDosage( 50000 );

        int numPrescriptions = Prescription.getForPatient( "patient" ).size();
        try {
            Prescription prescription = new Prescription( prescriptionForm );
            prescription.save();
            fail( "Invalid NDC should have failed." );
        }
        catch ( Exception e ) {
            assertEquals( numPrescriptions, Prescription.getForPatient( "patient" ).size() );
        }
    }

    @Test public void testInvalidPatient () throws Exception {
        PrescriptionForm prescriptionForm = new PrescriptionForm();
        prescriptionForm.setId( "1" );
        prescriptionForm.setNdc( "Androxy" );
        prescriptionForm.setPatient( "!!!!!!" );
        prescriptionForm.setOfficeVisit( null );
        prescriptionForm.setStartDate( "01/01/2017" );
        prescriptionForm.setEndDate( "01/01/2035" );
        prescriptionForm.setNumRenewals( 1000 );
        prescriptionForm.setDosage( 50000 );

        int numPrescriptions = Prescription.getForPatient( "patient" ).size();
        try {
            Prescription prescription = new Prescription( prescriptionForm );
            prescription.save();
            fail( "Invalid Patient should have failed." );
        }
        catch ( Exception e ) {
            assertEquals( numPrescriptions, Prescription.getForPatient( "patient" ).size() );
        }
    }

    @Test public void testInvalidStartDate () throws Exception {
        PrescriptionForm prescriptionForm = new PrescriptionForm();
        prescriptionForm.setId( "1" );
        prescriptionForm.setNdc( "Androxy" );
        prescriptionForm.setPatient( "patient" );
        prescriptionForm.setOfficeVisit( null );
        prescriptionForm.setStartDate( "!!!!!!!" );
        prescriptionForm.setEndDate( "01/01/2035" );
        prescriptionForm.setNumRenewals( 1000 );
        prescriptionForm.setDosage( 50000 );

        int numPrescriptions = Prescription.getForPatient( "patient" ).size();
        try {
            new Prescription( prescriptionForm );
            fail( "Invalid StartDate should have failed." );
        }
        catch ( ParseException e ) {
            assertEquals( numPrescriptions, Prescription.getForPatient( "patient" ).size() );
        }
    }

    @Test public void testInvalidEndDate () throws Exception {
        PrescriptionForm prescriptionForm = new PrescriptionForm();
        prescriptionForm.setId( "1" );
        prescriptionForm.setNdc( "Androxy" );
        prescriptionForm.setPatient( "patient" );
        prescriptionForm.setOfficeVisit( null );
        prescriptionForm.setStartDate( "01/01/2017" );
        prescriptionForm.setEndDate( "!!!!!!!" );
        prescriptionForm.setNumRenewals( 1000 );
        prescriptionForm.setDosage( 50000 );

        int numPrescriptions = Prescription.getForPatient( "patient" ).size();
        try {
            new Prescription( prescriptionForm );
            fail( "Invalid EndDate should have failed." );
        }
        catch ( ParseException e ) {
            assertEquals( numPrescriptions, Prescription.getForPatient( "patient" ).size() );
        }
    }

    @Test public void testInvalidNumRenewals () throws Exception {
        PrescriptionForm prescriptionForm = new PrescriptionForm();
        prescriptionForm.setId( "1" );
        prescriptionForm.setNdc( "Androxy" );
        prescriptionForm.setPatient( "patient" );
        prescriptionForm.setOfficeVisit( null );
        prescriptionForm.setStartDate( "01/01/2017" );
        prescriptionForm.setEndDate( "01/01/2035" );
        prescriptionForm.setNumRenewals( null );
        prescriptionForm.setDosage( 50000 );

        int numPrescriptions = Prescription.getForPatient( "patient" ).size();
        try {
            Prescription prescription = new Prescription( prescriptionForm );
            prescription.save();
            fail( "Invalid NumRenewals should have failed." );
        }
        catch ( Exception e ) {
            assertEquals( numPrescriptions, Prescription.getForPatient( "patient" ).size() );
        }
    }

    @Test public void testInvalidDosage () throws Exception {
        PrescriptionForm prescriptionForm = new PrescriptionForm();
        prescriptionForm.setId( "1" );
        prescriptionForm.setNdc( "Androxy" );
        prescriptionForm.setPatient( "patient" );
        prescriptionForm.setOfficeVisit( null );
        prescriptionForm.setStartDate( "01/01/2017" );
        prescriptionForm.setEndDate( "01/01/2035" );
        prescriptionForm.setNumRenewals( 1000 );
        prescriptionForm.setDosage( null );

        int numPrescriptions = Prescription.getForPatient( "patient" ).size();
        try {
            Prescription prescription = new Prescription( prescriptionForm );
            prescription.save();
            fail( "Invalid Dosage should have failed." );
        }
        catch ( Exception e ) {
            assertEquals( numPrescriptions, Prescription.getForPatient( "patient" ).size() );
        }
    }
}
