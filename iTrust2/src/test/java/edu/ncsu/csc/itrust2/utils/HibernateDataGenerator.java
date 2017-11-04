package edu.ncsu.csc.itrust2.utils;

import java.text.ParseException;
import java.util.Calendar;

import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.boot.spi.MetadataImplementor;
import org.hibernate.tool.hbm2ddl.SchemaExport;

import edu.ncsu.csc.itrust2.forms.admin.PrescriptionForm;
import edu.ncsu.csc.itrust2.models.enums.Role;
import edu.ncsu.csc.itrust2.models.persistent.Hospital;
import edu.ncsu.csc.itrust2.models.persistent.ICD;
import edu.ncsu.csc.itrust2.models.persistent.NDC;
import edu.ncsu.csc.itrust2.models.persistent.Patient;
import edu.ncsu.csc.itrust2.models.persistent.Prescription;
import edu.ncsu.csc.itrust2.models.persistent.User;

/**
 * Newly revamped Test Data Generator. This class is used to generate database
 * records for the various different types of persistent objects that exist in
 * the system. Takes advantage of Hibernate persistence. To use, instantiate the
 * type of object in question, set all of its parameters, and then call the
 * save() method on the object.
 *
 * @author Kai Presler-Marshall
 *
 */
public class HibernateDataGenerator {

    /**
     * Starts the data generator program.
     *
     * @param args
     *            command line arguments
     */
    public static void main ( final String args[] ) {
        refreshDB();

        System.exit( 0 );
        return;
    }

    /**
     * Generate sample users for the iTrust2 system.
     */
    public static void refreshDB () {
        // using the config to drop/create taken from here:
        // https://stackoverflow.com/questions/20535423/how-to-manually-invoke-create-drop-from-jpa-on-hibernate
        // how to actually generate the schemaexport taken from here:
        // http://www.javarticles.com/2015/06/generating-database-schema-using-hibernate.html
        final SchemaExport export = new SchemaExport( (MetadataImplementor) new MetadataSources(
                new StandardServiceRegistryBuilder().configure( "/hibernate.cfg.xml" ).build() ).buildMetadata() );
        export.drop( true, true );
        export.create( true, true );

        DomainObjectCache.clearCaches();

        // TODO we might need to add a delay here

        final User hcp = new User( "hcp", "$2a$10$EblZqNptyYvcLm/VwDCVAuBjzZOI7khzdyGPBr08PpIi0na624b8.", Role.ROLE_HCP,
                1 );
        hcp.save();

        final User patient = new User( "patient", "$2a$10$EblZqNptyYvcLm/VwDCVAuBjzZOI7khzdyGPBr08PpIi0na624b8.",
                Role.ROLE_PATIENT, 1 );
        patient.save();

        final Patient tim = new Patient();
        final User timUser = new User( "TimTheOneYearOld",
                "$2a$10$EblZqNptyYvcLm/VwDCVAuBjzZOI7khzdyGPBr08PpIi0na624b8.", Role.ROLE_PATIENT, 1 );
        timUser.save();
        tim.setSelf( timUser );
        tim.setFirstName( "TimTheOneYearOld" );
        tim.setLastName( "Smith" );
        final Calendar timBirth = Calendar.getInstance();
        timBirth.add( Calendar.YEAR, -1 ); // tim is one year old
        tim.setDateOfBirth( timBirth );
        tim.save();

        final Patient bob = new Patient();
        bob.setFirstName( "BobTheFourYearOld" );
        final User bobUser = new User( "BobTheFourYearOld",
                "$2a$10$EblZqNptyYvcLm/VwDCVAuBjzZOI7khzdyGPBr08PpIi0na624b8.", Role.ROLE_PATIENT, 1 );
        bobUser.save();
        bob.setSelf( bobUser );
        bob.setLastName( "Smith" );
        final Calendar bobBirth = Calendar.getInstance();
        bobBirth.add( Calendar.YEAR, -4 ); // bob is four years old
        bob.setDateOfBirth( bobBirth );
        bob.save();

        final Patient alice = new Patient();
        alice.setFirstName( "AliceThirteen" );
        final User aliceUser = new User( "AliceThirteen",
                "$2a$10$EblZqNptyYvcLm/VwDCVAuBjzZOI7khzdyGPBr08PpIi0na624b8.", Role.ROLE_PATIENT, 1 );
        aliceUser.save();
        alice.setSelf( aliceUser );
        alice.setLastName( "Smith" );
        final Calendar aliceBirth = Calendar.getInstance();
        aliceBirth.add( Calendar.YEAR, -13 ); // alice is thirteen years old
        alice.setDateOfBirth( aliceBirth );
        alice.save();

        final Hospital hosp = new Hospital( "General Hostpital", "123 Main St", "12345", "NC" );
        hosp.save();

        final User admin = new User( "admin", "$2a$10$EblZqNptyYvcLm/VwDCVAuBjzZOI7khzdyGPBr08PpIi0na624b8.",
                Role.ROLE_ADMIN, 1 );
        admin.save();

        final NDC ndc = new NDC();
        ndc.setCode( "0832-0086" );
        ndc.setDescription( "Androxy" );
        ndc.save();

        final NDC ndc2 = new NDC();
        ndc2.setCode( "0832-0087" );
        ndc2.setDescription( "Androxy2" );
        ndc2.save();

        final ICD icd = new ICD();
        icd.setCode( "A00" );
        icd.setDescription( "Cholera" );
        icd.save();

        final ICD icd2 = new ICD();
        icd2.setCode( "A01" );
        icd2.setDescription( "Cholera2" );
        icd2.save();

        final PrescriptionForm prescriptionForm = new PrescriptionForm();
        prescriptionForm.setNdc( "Androxy" );
        prescriptionForm.setPatient( "patient" );
        prescriptionForm.setOfficeVisit( null );
        prescriptionForm.setStartDate( "01/01/2017" );
        prescriptionForm.setEndDate( "01/01/2035" );
        prescriptionForm.setNumRenewals( 1000 );
        prescriptionForm.setDosage( 50000 );

        try {
            final Prescription prescription = new Prescription( prescriptionForm );
            prescription.save();
        }
        catch ( final ParseException e ) {
            e.printStackTrace();
        }

    }

    /**
     * Generate sample users for the iTrust2 system.
     */
    public static void generateUsers () {
        final User hcp = new User( "hcp", "$2a$10$EblZqNptyYvcLm/VwDCVAuBjzZOI7khzdyGPBr08PpIi0na624b8.", Role.ROLE_HCP,
                1 );
        hcp.save();

        final User patient = new User( "patient", "$2a$10$EblZqNptyYvcLm/VwDCVAuBjzZOI7khzdyGPBr08PpIi0na624b8.",
                Role.ROLE_PATIENT, 1 );
        patient.save();

        final User admin = new User( "admin", "$2a$10$EblZqNptyYvcLm/VwDCVAuBjzZOI7khzdyGPBr08PpIi0na624b8.",
                Role.ROLE_ADMIN, 1 );
        admin.save();
    }
}
