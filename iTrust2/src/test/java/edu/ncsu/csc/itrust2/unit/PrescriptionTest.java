package edu.ncsu.csc.itrust2.unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import org.junit.Test;

import edu.ncsu.csc.itrust2.forms.admin.PrescriptionForm;
import edu.ncsu.csc.itrust2.models.persistent.NDC;
import edu.ncsu.csc.itrust2.models.persistent.Prescription;
import edu.ncsu.csc.itrust2.models.persistent.User;

public class PrescriptionTest {
    @Test
    public void testValidPrescription () throws Exception {
        final PrescriptionForm prescriptionForm = new PrescriptionForm();
        prescriptionForm.setId( "1" );
        // guaranteed to have these in the db from the db generator
        prescriptionForm.setNdcDescription( "Androxy" );
        prescriptionForm.setNdcCode( "0832-0086" );
        prescriptionForm.setPatient( "patient" );
        prescriptionForm.setOfficeVisit( null );
        prescriptionForm.setStartDate( "01/01/2017" );
        prescriptionForm.setEndDate( "01/01/2035" );
        prescriptionForm.setNumRenewals( 1000 );
        prescriptionForm.setDosage( 50000 );

        final Prescription prescription = new Prescription( prescriptionForm );
        assertEquals( 1, (long) prescription.getId() );
        assertEquals( NDC.getByDescription( "Androxy" ), prescription.getNdc() );
        assertEquals( User.getByName( "patient" ), prescription.getPatient() );
        assertNull( prescription.getVisit() );
        final SimpleDateFormat format = new SimpleDateFormat( "MM/dd/yyyy", Locale.ENGLISH );
        assertEquals( "01/01/2017", format.format( prescription.getStartDate() ) );
        assertEquals( "01/01/2035", format.format( prescription.getEndDate() ) );
        assertEquals( 1000, (int) prescription.getNumRenewals() );
        assertEquals( 50000, (int) prescription.getDosage() );
    }

    @Test
    public void testInvalidNDC () throws Exception {
        final PrescriptionForm prescriptionForm = new PrescriptionForm();
        prescriptionForm.setId( "1" );
        prescriptionForm.setNdcDescription( "!!!!!!!" );
        prescriptionForm.setNdcCode( "0832-0086" );
        prescriptionForm.setPatient( "patient" );
        prescriptionForm.setOfficeVisit( null );
        prescriptionForm.setStartDate( "01/01/2017" );
        prescriptionForm.setEndDate( "01/01/2035" );
        prescriptionForm.setNumRenewals( 1000 );
        prescriptionForm.setDosage( 50000 );

        final int numPrescriptions = Prescription.getForPatient( "patient" ).size();
        try {
            final Prescription prescription = new Prescription( prescriptionForm );
            prescription.save();
            fail( "Invalid NDC should have failed." );
        }
        catch ( final Exception e ) {
            assertEquals( numPrescriptions, Prescription.getForPatient( "patient" ).size() );
        }
    }

    @Test
    public void testInvalidPatient () throws Exception {
        final PrescriptionForm prescriptionForm = new PrescriptionForm();
        prescriptionForm.setId( "1" );
        prescriptionForm.setNdcDescription( "Androxy" );
        prescriptionForm.setNdcCode( "0832-0086" );
        prescriptionForm.setPatient( "!!!!!!" );
        prescriptionForm.setOfficeVisit( null );
        prescriptionForm.setStartDate( "01/01/2017" );
        prescriptionForm.setEndDate( "01/01/2035" );
        prescriptionForm.setNumRenewals( 1000 );
        prescriptionForm.setDosage( 50000 );

        final int numPrescriptions = Prescription.getForPatient( "patient" ).size();
        try {
            final Prescription prescription = new Prescription( prescriptionForm );
            prescription.save();
            fail( "Invalid Patient should have failed." );
        }
        catch ( final Exception e ) {
            assertEquals( numPrescriptions, Prescription.getForPatient( "patient" ).size() );
        }
    }

    @Test
    public void testInvalidStartDate () throws Exception {
        final PrescriptionForm prescriptionForm = new PrescriptionForm();
        prescriptionForm.setId( "1" );
        prescriptionForm.setNdcDescription( "Androxy" );
        prescriptionForm.setNdcCode( "0832-0086" );
        prescriptionForm.setPatient( "patient" );
        prescriptionForm.setOfficeVisit( null );
        prescriptionForm.setStartDate( "!!!!!!!" );
        prescriptionForm.setEndDate( "01/01/2035" );
        prescriptionForm.setNumRenewals( 1000 );
        prescriptionForm.setDosage( 50000 );

        final int numPrescriptions = Prescription.getForPatient( "patient" ).size();
        try {
            new Prescription( prescriptionForm );
            fail( "Invalid StartDate should have failed." );
        }
        catch ( final ParseException e ) {
            assertEquals( numPrescriptions, Prescription.getForPatient( "patient" ).size() );
        }
    }

    @Test
    public void testInvalidEndDate () throws Exception {
        final PrescriptionForm prescriptionForm = new PrescriptionForm();
        prescriptionForm.setId( "1" );
        prescriptionForm.setNdcDescription( "Androxy" );
        prescriptionForm.setNdcCode( "0832-0086" );
        prescriptionForm.setPatient( "patient" );
        prescriptionForm.setOfficeVisit( null );
        prescriptionForm.setStartDate( "01/01/2017" );
        prescriptionForm.setEndDate( "!!!!!!!" );
        prescriptionForm.setNumRenewals( 1000 );
        prescriptionForm.setDosage( 50000 );

        final int numPrescriptions = Prescription.getForPatient( "patient" ).size();
        try {
            new Prescription( prescriptionForm );
            fail( "Invalid EndDate should have failed." );
        }
        catch ( final ParseException e ) {
            assertEquals( numPrescriptions, Prescription.getForPatient( "patient" ).size() );
        }
    }

    @Test
    public void testInvalidNumRenewals () throws Exception {
        final PrescriptionForm prescriptionForm = new PrescriptionForm();
        prescriptionForm.setId( "1" );
        prescriptionForm.setNdcDescription( "Androxy" );
        prescriptionForm.setNdcCode( "0832-0086" );
        prescriptionForm.setPatient( "patient" );
        prescriptionForm.setOfficeVisit( null );
        prescriptionForm.setStartDate( "01/01/2017" );
        prescriptionForm.setEndDate( "01/01/2035" );
        prescriptionForm.setNumRenewals( null );
        prescriptionForm.setDosage( 50000 );

        final int numPrescriptions = Prescription.getForPatient( "patient" ).size();
        try {
            final Prescription prescription = new Prescription( prescriptionForm );
            prescription.save();
            fail( "Invalid NumRenewals should have failed." );
        }
        catch ( final Exception e ) {
            assertEquals( numPrescriptions, Prescription.getForPatient( "patient" ).size() );
        }
    }

    @Test
    public void testInvalidDosage () throws Exception {
        final PrescriptionForm prescriptionForm = new PrescriptionForm();
        prescriptionForm.setId( "1" );
        prescriptionForm.setNdcDescription( "Androxy" );
        prescriptionForm.setNdcCode( "0832-0086" );
        prescriptionForm.setPatient( "patient" );
        prescriptionForm.setOfficeVisit( null );
        prescriptionForm.setStartDate( "01/01/2017" );
        prescriptionForm.setEndDate( "01/01/2035" );
        prescriptionForm.setNumRenewals( 1000 );
        prescriptionForm.setDosage( null );

        final int numPrescriptions = Prescription.getForPatient( "patient" ).size();
        try {
            final Prescription prescription = new Prescription( prescriptionForm );
            prescription.save();
            fail( "Invalid Dosage should have failed." );
        }
        catch ( final Exception e ) {
            assertEquals( numPrescriptions, Prescription.getForPatient( "patient" ).size() );
        }
    }
}
