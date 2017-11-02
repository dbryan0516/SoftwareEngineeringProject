package edu.ncsu.csc.itrust2.cucumber;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import edu.ncsu.csc.itrust2.forms.hcp_patient.PatientForm;
import edu.ncsu.csc.itrust2.models.enums.BloodType;
import edu.ncsu.csc.itrust2.models.enums.Ethnicity;
import edu.ncsu.csc.itrust2.models.enums.Gender;
import edu.ncsu.csc.itrust2.models.persistent.Patient;
import edu.ncsu.csc.itrust2.utils.HibernateDataGenerator;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class EditPatientStepDefs {
    private final WebDriver     driver  = new HtmlUnitDriver( true );
    private final String        baseUrl = "http://localhost:8080/iTrust2";
    private final WebDriverWait wait    = new WebDriverWait( driver, 2 );

    @Given ( "^A default patient exists in the system$" ) public void validPatient () {
        HibernateDataGenerator.refreshDB();
        login( "patient", "123456" );
        ( (JavascriptExecutor) driver ).executeScript( "document.getElementById('editdemographics').click();" );
        final WebElement firstName = driver.findElement( By.id( "firstName" ) );
        firstName.clear();
        firstName.sendKeys( "John" );

        final WebElement lastName = driver.findElement( By.id( "lastName" ) );
        lastName.clear();
        lastName.sendKeys( "Deer" );

        final WebElement preferredName = driver.findElement( By.id( "preferredName" ) );
        preferredName.clear();

        final WebElement mother = driver.findElement( By.id( "mother" ) );
        mother.clear();

        final WebElement father = driver.findElement( By.id( "father" ) );
        father.clear();

        final WebElement email = driver.findElement( By.id( "email" ) );
        email.clear();
        email.sendKeys( "jd@gmail.com" );

        final WebElement address1 = driver.findElement( By.id( "address1" ) );
        address1.clear();
        address1.sendKeys( "Address" );

        final WebElement city = driver.findElement( By.id( "city" ) );
        city.clear();
        city.sendKeys( "Raleigh" );

        final WebElement state = driver.findElement( By.id( "state" ) );
        final Select dropdown = new Select( state );
        dropdown.selectByVisibleText( "NC" );

        final WebElement zip = driver.findElement( By.id( "zip" ) );
        zip.clear();
        zip.sendKeys( "27606" );

        final WebElement phone = driver.findElement( By.id( "phone" ) );
        phone.clear();
        phone.sendKeys( "123-456-7890" );

        final WebElement dob = driver.findElement( By.id( "dateOfBirth" ) );
        dob.clear();
        dob.sendKeys( "01/01/1972" );

        final WebElement submitDemo = driver.findElement( By.className( "btn" ) );
        submitDemo.click();

        final WebElement logout = driver.findElement( By.id( "logout" ) );
        logout.click();
    }

    @Given ( "^An HCP is authenticated$" ) public void authenticateHCP () {
        login( "hcp", "123456" );
    }

    @Given ( "^The HCP is at the Edit Patient page$" ) public void navigateToEditPatient () {
        ( (JavascriptExecutor) driver ).executeScript( "document.getElementById('editpatient').click();" );
    }

    @When ( "^I select a patient (.+)$" ) public void selectPatient ( final String patient ) {
        wait.until( ExpectedConditions.visibilityOfElementLocated( By.id( patient ) ) );
        final WebElement patientRadio = driver.findElement( By.id( patient ) );
        patientRadio.click();
    }

    @When ( "^I enter the fields (.+), (.+), (.+), (.+), (.+), (.+), (.+), (.+), (.+), (.+), (.+), (.+), and (.+)$" )
    public void enterDemographics ( final String firstName, final String lastName, final String preferredName,
            final String email, final String address1, final String city, final String state, final String zip,
            final String phone, final String dateOfBirth, final String bloodType, final String ethnicity,
            final String gender ) {
        final WebElement firstNameField = driver.findElement( By.id( "firstName" ) );
        firstNameField.clear();
        firstNameField.sendKeys( firstName );

        final WebElement lastNameField = driver.findElement( By.id( "lastName" ) );
        lastNameField.clear();
        lastNameField.sendKeys( lastName );

        final WebElement preferredNameField = driver.findElement( By.id( "preferredName" ) );
        preferredNameField.clear();
        preferredNameField.sendKeys( preferredName );

        final WebElement emailField = driver.findElement( By.id( "email" ) );
        emailField.clear();
        emailField.sendKeys( email );

        final WebElement address1Field = driver.findElement( By.id( "address1" ) );
        address1Field.clear();
        address1Field.sendKeys( address1 );

        final WebElement address2Field = driver.findElement( By.id( "address2" ) );
        address2Field.clear();

        final WebElement cityField = driver.findElement( By.id( "city" ) );
        cityField.clear();
        cityField.sendKeys( city );

        final WebElement stateField = driver.findElement( By.id( "state" ) );
        final Select dropdown = new Select( stateField );
        dropdown.selectByVisibleText( state );

        final WebElement zipField = driver.findElement( By.id( "zip" ) );
        zipField.clear();
        zipField.sendKeys( zip );

        final WebElement phoneField = driver.findElement( By.id( "phone" ) );
        phoneField.clear();
        phoneField.sendKeys( phone );

        final WebElement dateOfBirthField = driver.findElement( By.id( "dateOfBirth" ) );
        dateOfBirthField.clear();
        dateOfBirthField.sendKeys( dateOfBirth );

        final WebElement bloodTypeField = driver.findElement( By.id( "bloodType" ) );
        final Select bloodTypeSelect = new Select( bloodTypeField );
        bloodTypeSelect.selectByVisibleText( bloodType );

        final WebElement ethnicityField = driver.findElement( By.id( "ethnicity" ) );
        final Select ethnicitySelect = new Select( ethnicityField );
        ethnicitySelect.selectByVisibleText( ethnicity );

        final WebElement genderField = driver.findElement( By.id( "gender" ) );
        final Select genderSelect = new Select( genderField );
        genderSelect.selectByVisibleText( gender );
    }

    @When ( "^I submit$" ) public void submit () {
        final WebElement submitDemo = driver.findElement( By.name( "submit" ) );
        submitDemo.click();
    }

    @Then ( "^A success message is displayed$" ) public void hasSuccess () {
        try {
            Thread.sleep( 2000 );
        }
        catch ( InterruptedException e ) {
            e.printStackTrace();
        }
        final WebElement success = driver.findElement( By.name( "success" ) );
        assertEquals( "Demographics updated successfully", success.getText() );
    }

    @Then ( "^An error message is displayed$" ) public void hasError () {
        try {
            Thread.sleep( 2000 );
        }
        catch ( InterruptedException e ) {
            e.printStackTrace();
        }
        final WebElement error = driver.findElement( By.name( "errorMsg" ) );
        assertNotEquals( "", error.getText() );
    }

    @When ( "^The patient is updated in the database with values (.+), (.+), (.+), (.+), (.+), (.+), (.+), (.+), (.+), (.+), (.+), (.+), and (.+)$" )
    public void patientUpdated ( final String firstName, final String lastName, final String preferredName,
            final String email, final String address1, final String city, final String state, final String zip,
            final String phone, final String dateOfBirth, final String bloodType, final String ethnicity,
            final String gender ) {
        Patient patient = Patient.getPatient( "patient" );
        PatientForm patientForm = new PatientForm( patient );
        assertEquals( firstName, patientForm.getFirstName() );
        assertEquals( lastName, patientForm.getLastName() );
        assertEquals( preferredName, patientForm.getPreferredName() );
        assertEquals( email, patientForm.getEmail() );
        assertEquals( address1, patientForm.getAddress1() );
        assertEquals( city, patientForm.getCity() );
        assertEquals( state, patientForm.getState() );
        assertEquals( zip, patientForm.getZip() );
        assertEquals( phone, patientForm.getPhone() );
        assertEquals( dateOfBirth, patientForm.getDateOfBirth() );
        assertEquals( bloodType, BloodType.parse( patientForm.getBloodType() ).name() );
        assertEquals( ethnicity, Ethnicity.parse( patientForm.getEthnicity() ).name() );
        assertEquals( gender, Gender.parse( patientForm.getGender() ).name() );
    }

    @Then ( "^The database is refreshed$" ) public void refreshDatabase () {
        HibernateDataGenerator.refreshDB();
    }

    private void login ( final String username, final String password ) {
        driver.get( baseUrl );
        final WebElement userField = driver.findElement( By.name( "username" ) );
        userField.clear();
        userField.sendKeys( username );
        final WebElement passField = driver.findElement( By.name( "password" ) );
        passField.clear();
        passField.sendKeys( password );
        final WebElement submit = driver.findElement( By.className( "btn" ) );
        submit.click();
    }
}
