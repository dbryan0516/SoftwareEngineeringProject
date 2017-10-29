package edu.ncsu.csc.itrust2.cucumber;

import static org.junit.Assert.assertTrue;

import java.util.Random;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.ui.Select;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

/**
 * Step definitions for AddHosptial feature
 */
public class AddHospitalStepDefs {

    private final WebDriver driver       = new HtmlUnitDriver( true );
    private final String    baseUrl      = "http://localhost:8080/iTrust2";

    private final String    hospitalName = "TimHortons" + ( new Random() ).nextInt();


    /**
     * Admin Login
     */
    @Given ( "An admin is authenticated" )
    public void authenticateAdmin () {
        driver.get( baseUrl );
        final WebElement username = driver.findElement( By.name( "username" ) );
        username.clear();
        username.sendKeys( "admin" );
        final WebElement password = driver.findElement( By.name( "password" ) );
        password.clear();
        password.sendKeys( "123456" );
        final WebElement submit = driver.findElement( By.className( "btn" ) );
        submit.click();
        }

    /**
     * Update Database Page
     */
    @Given ( "The admin is at the update database page" )
    public void updateDatabasePage () {
        ( (JavascriptExecutor) driver ).executeScript(
                "document.get('http://localhost:8080/iTrust2/modifyDatabase.html');" );
    }

    /**
     * Fill code forms
     */
    @When ( "I fill out the fields with code <NDC_code>, name <NDC_name>" )
    public void fillNDCfields () {
        final WebElement name = driver.findElement( By.id( "code" ) );
        code.clear();
        code.sendKeys( "<NDC_code>" );

        final WebElement address = driver.findElement( By.id( "name" ) );
        name.clear();
        name.sendKeys( "<NDC_name>" );

    }


    /**
     * Pick NDC in list
     */
    @When ( "I select NDC in the list" )
    public void selectNDC () {
        WebElement radioBtn = driver.findElement(By.id("NDC"));
        radioBtn.click();
    }

    /**
     * Click submit
     */
    @When ( "I click submit" )
    public void clickSubmit () {
        final WebElement submit = driver.findElement( By.className( "btn" ) );
        submit.click();

    }


    /**
     * See Success message
     */
    @Then ( "I see a success message" )
    public void createdSuccessfully () {
        assertTrue( driver.getPageSource().contains( "Database modification successful" ) );
    }


    /**
     * The NDC code is added
     */
    @Then ( "the code is added to the NDC database" )
    public void codeAdded () {
        BasicHealthMetrics actualBhm = null;
        for ( int i = 1; i <= 10; i++ ) {
            try {
                actualBhm = BasicHealthMetrics.getBasicHealthMetrics().get( 0 );
                break;
            }
            catch ( final Exception e ) {
                if ( i == 10 && actualBhm == null ) {
                    fail( "Could not get basic health metrics out of database" );
                }
                Thread.sleep( 1000 );
            }
        }
        assertEquals( expectedBhm.getWeight(), actualBhm.getWeight() );
        assertEquals( expectedBhm.getHeight(), actualBhm.getHeight() );
        assertEquals( expectedBhm.getSystolic(), actualBhm.getSystolic() );
        assertEquals( expectedBhm.getDiastolic(), actualBhm.getDiastolic() );
        assertEquals( expectedBhm.getHouseSmokingStatus(), actualBhm.getHouseSmokingStatus() );

    }

}
