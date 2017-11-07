package edu.ncsu.csc.itrust2.cucumber;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.util.List;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import edu.ncsu.csc.itrust2.utils.HibernateDataGenerator;

/**
 * Step definitions for Admin editing medical codes (NDC and ICD) feature
 *
 * @author dwdeans
 * @author Joshua Kayani (jkayani@ncsu.edu)
 */
public class DBAdminStepDefs {

    private final WebDriver  driver  = new HtmlUnitDriver( true );
    private final String     baseUrl = "http://localhost:8080/iTrust2";
    private static final int TIMEOUT = 1500;

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
        ( (JavascriptExecutor) driver ).executeScript( "document.getElementById('modifyDatabase').click();" );

    }

    /**
     * Pick add NDC in list
     */
    @Given ( "I select add NDC in the list" )
    public void selectAddNDC () {
        final WebElement radioBtn = driver.findElement( By.id( "addNDC" ) );
        radioBtn.click();

    }

    /**
     * Pick edit NDC in list
     */
    @Given ( "I select edit NDC in the list" )
    public void selectEditNDC () {
        final WebElement radioBtn = driver.findElement( By.id( "updateNDC" ) );
        radioBtn.click();
    }

    /**
     * Pick add ICD in list
     */
    @Given ( "I select add ICD in the list" )
    public void selectAddICD () {
        final WebElement radioBtn = driver.findElement( By.id( "addICD" ) );
        radioBtn.click();
    }

    /**
     * Pick edit ICD in list
     */
    @Given ( "I select edit ICD in the list" )
    public void selectEditICD () {
        final WebElement radioBtn = driver.findElement( By.id( "updateICD" ) );
        radioBtn.click();
    }

    /**
     * The first drug exists. Hibernate's refresh DB method, which is run after
     * each test, should ensure this.
     */
    @Given ( "NDC code 0832-0086-00 exists" )
    public void ndcExists () {
        try {
            Thread.sleep( TIMEOUT );
            assertEquals( "Androxy", driver.findElement( By.cssSelector( "input[value='0832-0086-00']" ) )
                    .getAttribute( "data-medCode" ) );
        }
        catch ( final Exception e ) {
            Assert.fail( e.getMessage() );
        }
    }

    /**
     * The second drug exists. Hibernate's refresh DB method, which is run after
     * each test, should ensure this.
     */
    @Given ( "NDC code 0832-0087-00 exists" )
    public void ndc2Exists () {
        try {
            Thread.sleep( TIMEOUT );
            assertEquals( "Androxy2", driver.findElement( By.cssSelector( "input[value='0832-0087-00']" ) )
                    .getAttribute( "data-medCode" ) );
        }
        catch ( final Exception e ) {
            Assert.fail( e.getMessage() );
        }
    }

    /**
     * The first disease exists. Hibernate's refresh DB method, which is run
     * after each test, should ensure this.
     */
    @Given ( "ICD code A00 exists" )
    public void icdExists () {
        try {
            Thread.sleep( TIMEOUT );
            assertEquals( "Cholera",
                    driver.findElement( By.cssSelector( "input[value='A00']" ) ).getAttribute( "data-medCode" ) );
        }
        catch ( final Exception e ) {
            Assert.fail( e.getMessage() );
        }
    }

    /**
     * The second disease exists. Hibernate's refresh DB method, which is run
     * after each test, should ensure this.
     */
    @Given ( "ICD code A01 exists" )
    public void icd2Exists () {
        try {
            Thread.sleep( TIMEOUT );
            assertEquals( "Cholera2",
                    driver.findElement( By.cssSelector( "input[value='A01']" ) ).getAttribute( "data-medCode" ) );
        }
        catch ( final Exception e ) {
            Assert.fail( e.getMessage() );
        }
    }

    /**
     * Select a medical code (NDC or ICD)
     */
    @When ( "I select code (.+)" )
    public void selectMedCode ( String code ) {

        try {
            final WebElement radioBtn = driver.findElement( By.xpath( "//input[@value='" + code + "']" ) );
            radioBtn.click();

        }
        catch ( final Exception e ) {
            /* Assume the code already exists & carry on */
        }
    }

    /**
     * Fill NDC form out
     *
     * @param code
     *            The actual NDC code
     * @param name
     *            The name of the drug
     */
    @When ( "I fill out the fields with code (.+), name (.+)" )
    public void fillFields ( final String code, final String name ) {
        final WebElement codeInput = driver.findElement( By.name( "code" ) );
        codeInput.clear();
        codeInput.sendKeys( code );

        final WebElement nameInput = driver.findElement( By.name( "description" ) );
        nameInput.clear();
        nameInput.sendKeys( name );

    }

    /**
     * Click submit
     */
    @When ( "I click submit" )
    public void clickSubmit () {
        final WebElement submit = driver.findElement( By.name( "submit" ) );
        submit.click();

    }

    /**
     * See Success message
     */
    @Then ( "I see a success message" )
    public void attemptSucessful () {

        final WebElement message = driver.findElement( By.id( "status" ) );

        while ( message.getText().equals( "" ) ) {
            // do nothing
        }
        assertEquals( "Database modification successful", message.getText() );
    }

    /**
     * See fail message
     */
    @Then ( "I see an error message" )
    public void attemptUnsucessful () {

        final WebElement message = driver.findElement( By.id( "status" ) );

        while ( message.getText().equals( "" ) ) {
            // do nothing
        }

        assertEquals( "Database modification failed", message.getText() );
    }

    /**
     * The NDC code is added
     *
     * @param code
     *            the code for the NDC
     *
     * @param name
     *            the name of the NDC
     * @throws InterruptedException
     */
    @Then ( "the code: code (.+), name (.+) is added to the NDC database" )
    public void ndcCodeAdded ( final String code, final String name ) throws InterruptedException {
        Thread.sleep( TIMEOUT );
        try {
            assertEquals( name, driver.findElement( By.cssSelector( "input[value='" + code + "']" ) )
                    .getAttribute( "data-medCode" ) );
        }
        catch ( final Exception e ) {
            Assert.fail( e.getMessage() );
        }
    }

    /**
     * The NDC code is NOT added
     *
     * @param code
     *            the code for the NDC
     *
     * @param name
     *            the name of the NDC
     * @throws InterruptedException
     */
    @Then ( "the code: code (.+), name (.+) is not added to the NDC database" )
    public void ndcCodeNotAdded ( final String code, final String name ) throws InterruptedException {

        Thread.sleep( TIMEOUT );
        try {
            assertNotEquals( name, driver.findElement( By.cssSelector( "input[value='" + code + "']" ) )
                    .getAttribute( "data-medCode" ) );
        }
        catch ( final Exception e ) {
            // Exception should be thrown, sometimes.
        }

    }

    /**
     * The ICD code is added
     *
     * @param code
     *            the code for the ICD
     *
     * @param name
     *            the name of the ICD
     */
    @Then ( "the code: code (.+), name (.+) is added to the ICD database" )
    public void icdCodeAdded ( final String code, final String name ) throws InterruptedException {
        Thread.sleep( TIMEOUT );
        try {
            assertEquals( name, driver.findElement( By.cssSelector( "input[value='" + code + "']" ) )
                    .getAttribute( "data-medCode" ) );
        }
        catch ( final Exception e ) {
            Assert.fail( e.getMessage() );
        }

    }

    /**
     * The ICD code is NOT added
     *
     * @param code
     *            the code for the ICD
     *
     * @param name
     *            the name of the ICD
     * @throws InterruptedException
     */
    @Then ( "the code: code (.+), name (.+) is not added to the ICD database" )
    public void icdCodeNotAdded ( final String code, final String name ) throws InterruptedException {

        Thread.sleep( TIMEOUT );
        try {
            assertNotEquals( code, driver.findElement( By.cssSelector( "input[value='" + code + "']" ) )
                    .getAttribute( "data-medCode" ) );
        }
        catch ( final Exception e ) {
            // Exception should be thrown, somtimes.
        }

    }

    /**
     * Oxycotin becomes oxycodone
     *
     * @throws InterruptedException
     */
    @Then ( "the code becomes: code (.+), name (.+) instead of: code (.+), name (.+)" )
    public void changeHappens ( String newCode, String newName, String oldCode, String oldName )
            throws InterruptedException {

        Thread.sleep( TIMEOUT );

        // The NDC pair (oldName, oldCode) should NOT exist
        final List<WebElement> oldNames = driver
                .findElements( By.cssSelector( "input[data-medCode='" + oldName + "']" ) );
        oldNames.forEach( elm -> assertNotEquals( oldCode, elm.getAttribute( "value" ) ) );

        // The NDC pair (newName, newCode) should exist
        try {
            assertEquals( newName, driver.findElement( By.cssSelector( "input[value='" + newCode + "']" ) )
                    .getAttribute( "data-medCode" ) );

        }
        catch ( final Exception e ) {
            Assert.fail( e.getMessage() );
        }

    }

    /**
     * Oxycotin stays the same
     *
     * @throws InterruptedException
     */
    @Then ( "the code stays as: code (.+), name (.+)" )
    public void changeNotHappen ( String code, String name ) throws InterruptedException {

        Thread.sleep( TIMEOUT );

        try {
            assertEquals( name, driver.findElement( By.cssSelector( "input[value='" + code + "']" ) )
                    .getAttribute( "data-medCode" ) );
        }
        catch ( final Exception e ) {
            Assert.fail( e.getMessage() );
        }
    }

    /**
     * The NDC database is cleared
     */
    @Then ( "the NDC database is cleared" )
    public void ndcCleared () {
        HibernateDataGenerator.refreshDB();

    }

    /**
     * The ICD database is cleared
     */
    @Then ( "the ICD database is cleared" )
    public void icdCleared () {
        HibernateDataGenerator.refreshDB();

    }

}
