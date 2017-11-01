package edu.ncsu.csc.itrust2.cucumber;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.util.Random;

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
 * Step definitions for AddHosptial feature
 *
 * @author dwdeans
 * @author Joshua Kayani (jkayani@ncsu.edu)
 */
public class DBAdminStepDefs {

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
    @Given ( "NDC code 0832-0086 exists" )
    public void ndcExists () {
        try {
            Thread.sleep( 1500 );
            driver.findElement( By.cssSelector( "input[value='0832-0086']" ) );
        }
        catch ( final Exception e ) {
            Assert.fail();
        }
    }

    /**
     * The second drug exists. Hibernate's refresh DB method, which is run after
     * each test, should ensure this.
     */
    @Given ( "NDC code 0832-0087 exists" )
    public void ndc2Exists () {
        try {
            Thread.sleep( 1500 );
            driver.findElement( By.cssSelector( "input[value='0832-0087']" ) );
        }
        catch ( final Exception e ) {
            Assert.fail();
        }
    }

    /**
     * The first disease exists. Hibernate's refresh DB method, which is run
     * after each test, should ensure this.
     */
    @Given ( "ICD code A00 exists" )
    public void icdExists () {
        try {
            Thread.sleep( 1500 );
            driver.findElement( By.cssSelector( "input[value='A00']" ) );
        }
        catch ( final Exception e ) {
            Assert.fail();
        }
    }

    /**
     * The second disease exists. Hibernate's refresh DB method, which is run
     * after each test, should ensure this.
     */
    @Given ( "ICD code A01 exists" )
    public void icd2Exists () {
        try {
            Thread.sleep( 1500 );
            driver.findElement( By.cssSelector( "input[value='A01']" ) );
        }
        catch ( final Exception e ) {
            Assert.fail();
        }
    }

    /**
     * Select Oxycotin
     */
    @When ( "I select code 0832-0086" )
    public void selectOxycotin () {

        try {
            final WebElement radioBtn = driver.findElement( By.xpath( "//input[@value='0832-0086']" ) );
            radioBtn.click();

        }
        catch ( final Exception e ) {
            /* Assume the code already exists & carry on */
        }
    }

    /**
     * Select Cholera
     */
    @When ( "I select code A00" )
    public void selectCholera () {
        try {
            final WebElement radioBtn = driver.findElement( By.xpath( "//input[@value='A00']" ) );
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
     * Fill in new form for Oxycotin incorrectly
     *
     * @param code
     *            the code of the drug
     */
    @When ( "And I fill out the fields with code (.+), name Oxycontin Drugs" )
    public void fillCotinToCodonefieldsIncorrect ( final String code ) {
        final WebElement codeInput = driver.findElement( By.name( "code" ) );
        codeInput.clear();
        codeInput.sendKeys( code );

        final WebElement name = driver.findElement( By.name( "description" ) );
        name.clear();
        name.sendKeys( "Oxycontin Drugs" );

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
        selectEditNDC();
        Thread.sleep( 1500 );
        try {
            assertEquals( name, driver.findElement( By.cssSelector( "input[value='" + code + "']" ) )
                    .getAttribute( "data-medCode" ) );
        }
        catch ( final Exception e ) {
            Assert.fail();
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

        selectEditNDC();
        Thread.sleep( 1500 );
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
        selectEditICD();
        Thread.sleep( 1500 );
        try {
            assertEquals( name, driver.findElement( By.cssSelector( "input[value='" + code + "']" ) )
                    .getAttribute( "data-medCode" ) );
        }
        catch ( final Exception e ) {
            Assert.fail();
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

        selectEditICD();
        Thread.sleep( 1500 );
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
    @Then ( "the code becomes: code 66666-616-30, name Oxycodone instead of: code 0832-0086, name Androxy" )
    public void cotinToCodone () throws InterruptedException {

        selectEditNDC();
        Thread.sleep( 1500 );

        // Androxy shouldn't exist
        try {
            driver.findElement( By.cssSelector( "input[value='0832-0086']" ) );
            Assert.fail();
        }
        catch ( final Exception e ) {
            // Exception should be thrown
        }

        // Oxycontin should exist
        try {
            assertEquals( "Oxycodone", driver.findElement( By.cssSelector( "input[value='66666-616-30']" ) )
                    .getAttribute( "data-medCode" ) );

        }
        catch ( final Exception e ) {
            Assert.fail();
        }

    }

    /**
     * Oxycotin stays the same
     *
     * @throws InterruptedException
     */
    @Then ( "the code stays as: code 0832-0086, name Androxy" )
    public void cotinNoChange () throws InterruptedException {

        selectEditNDC();
        Thread.sleep( 1500 );

        try {
            assertEquals( "Androxy",
                    driver.findElement( By.cssSelector( "input[value='0832-0086']" ) ).getAttribute( "data-medCode" ) );
        }
        catch ( final Exception e ) {
            Assert.fail();
        }
    }

    /**
     * Cholera changed successfully
     *
     * @param newCode
     *            the new code for the ICD formerly known as Cholera
     *
     * @param newName
     *            the new name for the ICD formerly known as Cholera
     * @throws InterruptedException
     *
     */

    // the code becomes: code <ICD_new_code>, name <ICD_new_name> instead of:
    // code A00, name Cholera
    @Then ( "the code becomes: code (.+), name (.+) instead of: code A00, name Cholera" )
    public void choleraChange ( final String newCode, final String newName ) throws InterruptedException {

        selectEditICD();
        Thread.sleep( 1500 );

        try {
            assertEquals( newName, driver.findElement( By.cssSelector( "input[value='" + newCode + "']" ) )
                    .getAttribute( "data-medCode" ) );
        }
        catch ( final Exception e ) {
            Assert.fail();
        }

        try {
            assertNotEquals( "Cholera",
                    driver.findElement( By.cssSelector( "input[value='A00']" ) ).getAttribute( "data-medCode" ) );
        }
        catch ( final Exception e ) {
            // Exception should be thrown, sometimes.
        }

    }

    /**
     * Cholera not changed
     *
     * @throws InterruptedException
     */
    @Then ( "the code stays as: code A00, name Cholera" )
    public void choleraNoChange () throws InterruptedException {
        selectEditICD();
        Thread.sleep( 1500 );
        try {
            assertEquals( "Cholera",
                    driver.findElement( By.cssSelector( "input[value='A00']" ) ).getAttribute( "data-medCode" ) );
        }
        catch ( final Exception e ) {
            Assert.fail();
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
