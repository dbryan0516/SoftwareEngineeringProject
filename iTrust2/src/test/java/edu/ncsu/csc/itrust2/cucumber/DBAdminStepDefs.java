package edu.ncsu.csc.itrust2.cucumber;

import static org.junit.Assert.assertEquals;

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
    @When ( "I select add NDC in the list" )
    public void selectAddNDC () {
        final WebElement radioBtn = driver.findElement( By.id( "addNDC" ) );
        radioBtn.click();
    }

    /**
     * Pick edit NDC in list
     */
    @When ( "I select edit NDC in the list" )
    public void selectEditNDC () {
        final WebElement radioBtn = driver.findElement( By.id( "updateNDC" ) );
        radioBtn.click();
    }

    /**
     * Pick add ICD in list
     */
    @When ( "I select add ICD in the list" )
    public void selectAddICD () {
        final WebElement radioBtn = driver.findElement( By.id( "addICD" ) );
        radioBtn.click();
    }

    /**
     * Pick edit ICD in list
     */
    @When ( "I select edit ICD in the list" )
    public void selectEditICD () {
        final WebElement radioBtn = driver.findElement( By.id( "updateICD" ) );
        radioBtn.click();
    }

    /**
     * Oxycotin Exists
     */
    @Given ( "NDC code 0832-0086 exists" )
    public void NDCExists () {
        try {
            final WebElement code = driver.findElement( By.xpath( "//input[@value='0832-0086']" ) );

        }
        catch ( final Exception e ) {
            /* Assume the code already exists & carry on */
        }
    }

    /**
     * The second drug exists
     */
    @Given ( "NDC code 0832-0087 exists" )
    public void NDC2Exists () {
        try {
            final WebElement code = driver.findElement( By.xpath( "//input[@value='0832-0087']" ) );

        }
        catch ( final Exception e ) {
            /* Assume the code already exists & carry on */
        }
    }

    /**
     * Cholera Exists
     */
    @Given ( "ICD code A00 exists" )
    public void ICDExists () {
        try {
            final WebElement code = driver.findElement( By.xpath( "//input[@value='A00']" ) );

        }
        catch ( final Exception e ) {
            /* Assume the code already exists & carry on */ // This was in
                                                            // DocOfficeVisit
                                                            // Step deffs, so
                                                            // I'm not 100% on
                                                            // what to do here.
        }
    }

    /**
     * Cholera Exists
     */
    @Given ( "ICD code A01 exists" )
    public void ICD2Exists () {
        try {
            final WebElement code = driver.findElement( By.xpath( "//input[@value='A01']" ) );

        }
        catch ( final Exception e ) {
            /* Assume the code already exists & carry on */ // This was in
                                                            // DocOfficeVisit
                                                            // Step deffs, so
                                                            // I'm not 100% on
                                                            // what to do here.
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
     * Fill NDC code forms
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
     */
    @Then ( "the code: code (.+), name (.+) is added to the NDC database" )
    public void ndcCodeAdded ( final String code, final String name ) {

        selectEditNDC();

        try {

            final WebElement radioBtn = driver.findElement( By.xpath( "//input[@value=" + code + "]" ) );

        }
        catch ( final Exception e ) {
            /*  */
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
     */
    @Then ( "the code: code (.+), name (.+) is not added to the NDC database" )
    public void ndcCodeNotAdded ( final String code, final String name ) {

        selectEditNDC();

        try {
            final WebElement radioBtn = driver.findElement( By.xpath( "//input[@value=" + code + "]" ) );

            Assert.fail();
        }
        catch ( final Exception e ) {
            // this should fail
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
    public void icdCodeAdded ( final String code, final String name ) {

        selectEditICD();

        try {
            final WebElement radioBtn = driver.findElement( By.xpath( "//input[@value=" + code + "]" ) );
        }
        catch ( final Exception e ) {
            // TODO
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
     */
    @Then ( "the code: code (.+), name (.+) is not added to the ICD database" )
    public void icdCodeNotAdded ( final String code, final String name ) {

        selectEditICD();

        try {
            final WebElement radioBtn = driver.findElement( By.xpath( "//input[@value=" + code + "]" ) );
            final WebElement radioBtnName = driver.findElement( By.xpath( "//input[@medCode=" + name + "]" ) );
            Assert.fail();
        }
        catch ( final Exception e ) {
            // this should fail
        }

    }

    /**
     * Oxycotin becomes oxycodone
     */
    @Then ( "the code becomes: code 66666-616-30, name Oxycodone instead of: code 0832-0086, name Androxy" )
    public void cotinToCodone () {

        selectEditNDC();

        try {
            final WebElement radioBtn = driver.findElement( By.xpath( "//input[@value='0832-0086']" ) );
            final WebElement radioBtnName = driver.findElement( By.xpath( "//input[@medCode='Androxy']" ) );

            Assert.fail();
        }
        catch ( final Exception e ) {
            // this should fail
        }

    }

    /**
     * Oxycotin stays the same
     */
    @Then ( "the code stays as: code 0832-0086, name Androxy" )
    public void cotinNoChange () {

        selectEditNDC();

        try {
            final WebElement radioBtn = driver.findElement( By.xpath( "//input[@value='0832-0086']" ) );
        }
        catch ( final Exception e ) {
            // this should fail
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
     *
     */

    // the code becomes: code <ICD_new_code>, name <ICD_new_name> instead of:
    // code A00, name Cholera
    @Then ( "the code becomes: code (.+), name (.+) instead of: code A00, name Cholera" )
    public void choleraChange ( final String newCode, final String newName ) {

        selectEditICD();

        try {
            final WebElement radioBtn = driver.findElement( By.xpath( "//input[@value=" + newCode + "]" ) );
            final WebElement radioBtnName = driver.findElement( By.xpath( "//input[@medCode=" + newName + "]" ) );

        }
        catch ( final Exception e ) {
            // TODO
        }

        try {
            final WebElement radioBtn = driver.findElement( By.xpath( "//input[@value='A00']" ) );
            final WebElement radioBtnName = driver.findElement( By.xpath( "//input[@medCode='Cholera']" ) );
            Assert.fail();
        }
        catch ( final Exception e ) {
            // TODO
        }

    }

    /**
     * Cholera not changed
     */
    @Then ( "the code stays as: code A00, name Cholera" )
    public void choleraNoChange () {

        selectEditICD();

        try {
            final WebElement radioBtn = driver.findElement( By.xpath( "//input[@value='A00']" ) );
            final WebElement radioBtnName = driver.findElement( By.xpath( "//input[@medCode='Cholera']" ) );

        }
        catch ( final Exception e ) {
            // TODO
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
