package edu.ncsu.csc.itrust2.cucumber;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

/**
 * Steps for documenting a prescription at /hcp/documentPrescription
 *
 * @author Joshua Kayani (jkayani)
 *
 */
public class DocumentPrescriptionStepDefs {

    private final WebDriver driver  = new HtmlUnitDriver( true );
    private final String    baseUrl = "http://localhost:8080/iTrust2";
    private final long      timeout = 1500;

    @Given ( "The HCP is authenticated and at the document prescription page" )
    public void loadDocumentPrescriptionPage () throws InterruptedException {
        driver.get( baseUrl );
        final WebElement username = driver.findElement( By.name( "username" ) );
        username.clear();
        username.sendKeys( "hcp" );
        final WebElement password = driver.findElement( By.name( "password" ) );
        password.clear();
        password.sendKeys( "123456" );
        final WebElement submit = driver.findElement( By.className( "btn" ) );
        submit.click();
        driver.get( baseUrl + "/hcp/documentPrescription" );
        Thread.sleep( timeout );
        System.out.println( "\n\n\n" + driver.getTitle() + "\n\n\n" );
        Assert.assertTrue( driver.getPageSource().contains( "Document a new Prescription" ) );
    }

    @When ( "^I select by username patient (.+)$" )
    public void selectPatient ( String patient ) throws InterruptedException {
        Thread.sleep( timeout );
        try {
            driver.findElement( By.cssSelector( "input[value='" + patient + "']" ) ).click();
        }
        catch ( final Exception e ) {
            Assert.fail( e.getMessage() );
        }
    }

    @When ( "I select a drug (.+) by NDC code" )
    public void selectDrug ( String drug ) throws InterruptedException {
        Thread.sleep( timeout );
        try {
            driver.findElement( By.cssSelector( "input[value='" + drug + "']" ) ).click();
        }
        catch ( final Exception e ) {
            Assert.fail( e.getMessage() );
        }
    }

    @When ( "I input start date (.+), end date (.+), dosage (.+), and renewals (.+)" )
    public void inputPrescriptionData ( String sDate, String eDate, String dosage, String renewals )
            throws InterruptedException {
        try {
            driver.findElement( By.id( "startDate" ) ).clear();
            driver.findElement( By.id( "startDate" ) ).sendKeys( sDate );

            driver.findElement( By.id( "endDate" ) ).clear();
            driver.findElement( By.id( "endDate" ) ).sendKeys( eDate );

            driver.findElement( By.id( "dosage" ) ).clear();
            driver.findElement( By.id( "dosage" ) ).sendKeys( dosage );

            driver.findElement( By.id( "numRenewals" ) ).clear();
            driver.findElement( By.id( "numRenewals" ) ).sendKeys( renewals );
        }
        catch ( final Exception e ) {
            Assert.fail( e.getMessage() );
        }
    }

    @When ( "I submit the prescription" )
    public void submitPrescription () {
        try {
            driver.findElement( By.name( "submit" ) ).click();
        }
        catch ( final Exception e ) {
            Assert.fail( e.getMessage() );
        }
    }

    @Then ( "The prescription was submitted successfully" )
    public void seeSuccess () throws InterruptedException {
        Thread.sleep( timeout );
        try {
            Assert.assertEquals( "", driver.findElement( By.name( "errorMsg" ) ).getText() );
            Assert.assertEquals( "Prescription successfully submitted",
                    driver.findElement( By.name( "success" ) ).getText() );
        }
        catch ( final Exception e ) {
            Assert.fail( e.getMessage() );
        }
    }

    @Then ( "The prescription was NOT submitted successfully" )
    public void seeFailure () throws InterruptedException {
        Thread.sleep( timeout );
        try {
            Assert.assertEquals( "An error occurred", driver.findElement( By.name( "success" ) ).getText() );
            Assert.assertNotEquals( "", driver.findElement( By.name( "errorMsg" ) ).getText() );
        }
        catch ( final Exception e ) {
            Assert.fail( e.getMessage() );
        }
    }
}
