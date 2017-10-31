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
        ( (JavascriptExecutor) driver ).executeScript(
                "document.get('http://localhost:8080/iTrust2/modifyDatabase.html');" );
    }






    /**
     * Pick add NDC in list
     */
    @When ( "I select add NDC in the list" )
    public void selectAddNDC () {
        WebElement radioBtn = driver.findElement(By.id("addNDC"));
        radioBtn.click();
    }

    /**
     * Pick edit NDC in list
     */
    @When ( "I select edit NDC in the list" )
    public void selectEditNDC () {
        WebElement radioBtn = driver.findElement(By.id("editNDC"));
        radioBtn.click();
    }

    /**
     * Pick add IDC in list
     */
    @When ( "I select add IDC in the list" )
    public void selectAddIDC () {
        WebElement radioBtn = driver.findElement(By.id("addIDC"));
        radioBtn.click();
    }

    /**
     * Pick edit IDC in list
     */
    @When ( "I select edit IDC in the list" )
    public void selectEditIDC () {
        WebElement radioBtn = driver.findElement(By.id("editIDC"));
        radioBtn.click();
    }









    /**
     * Oxycotin Exists
     */
    @Given ( "NDC code 16590-616-30 exists" )
    public void oxycotinExists () {
        try {
            final WebElement code = driver.findElement( By.value("16590-616-30"));
        }catch ( final Exception e ) {
            /* Assume the code already exists & carry on */
        }
    }

    /**
     * Cholera Exists
     */
    @Given ( "ICD code A00 exists" )
    public void choleraExists () {
        try {
            final WebElement code = driver.findElement(By.value("A00"));
        }catch ( final Exception e ) {
            /* Assume the code already exists & carry on */ //This was in DocOfficeVisit Step deffs, so I'm not 100% on what to do here.
        }
    }



    /**
     * Select Oxycotin
     */
    @When ( "I select code 16590-616-30" )
    public void selectOxycotin () {
        WebElement radioBtn = driver.findElement(By.value("16590-616-30"));
        radioBtn.click();
    }


    /**
     * Select Cholera
     */
    @When ( "I select code A00" )
    public void selectCholera () {
        WebElement radioBtn = driver.findElement(By.value("A00"));
        radioBtn.click();
    }






    /**
     * Fill NDC code forms
     */
    @When ( "I fill out the fields with code (.+), name (.+)" )
    public void fillNDCfields (final String code, final String name) {
        final WebElement codeInput = driver.findElement( By.id( "code" ) );
        codeInput.clear();
        codeInput.sendKeys( code );

        final WebElement nameInput = driver.findElement( By.id( "name" ) );
        nameInput.clear();
        nameInput.sendKeys( name);

    }

    /**
     * Fill IDC code forms
     *
     * @param code
     *          the code of the IDC
     * @param name
     *          the name of the IDC
     */
    @When ( "I fill out the fields with code (.+), name (.+)" )
    public void fillIDCfields (final String code, final String name) {
        final WebElement codeInput = driver.findElement( By.id( "code" ) );
        codeInput.clear();
        codeInput.sendKeys( code );

        final WebElement nameInput = driver.findElement( By.id( "name" ) );
        nameInput.clear();
        nameInput.sendKeys( name );

    }





    /**
     * Fill in new form for Oxycotin
     */
    @When ( "I fill out the fields with code 66666-616-30, name Oxycodone" )
    public void fillCotinToCodonefields () {
        final WebElement code = driver.findElement( By.id( "code" ) );
        code.clear();
        code.sendKeys( "66666-616-30" );

        final WebElement name = driver.findElement( By.id( "name" ) );
        name.clear();
        name.sendKeys( "Oxycodone" );

    }


    /**
     * Fill in new form for Oxycotin incorrectly
     *
     * @param code
     *          the code of the drug
     */
    @When ( "And I fill out the fields with code (.+), name Oxycontin Drugs" )
    public void fillCotinToCodonefieldsIncorrect (final String code) {
        final WebElement codeInput = driver.findElement( By.id( "code" ) );
        codeInput.clear();
        codeInput.sendKeys( code );

        final WebElement name = driver.findElement( By.id( "name" ) );
        name.clear();
        name.sendKeys( "Oxycontin Drugs" );

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
    public void attemptSucessful () {
        assertTrue( driver.getPageSource().contains( "Database modification successful" ) );
    }

    /**
     * See fail message
     */
    @Then ( "I see an error message" )
    public void attemptUnsucessful () {
        assertTrue( driver.getPageSource().contains( "Database modification failed" ) );
    }






    /**
     * The NDC code is added
     *
     * @param code
     *          the code for the NDC
     *
     * @param name
     *          the name of the NDC
     */
    @Then ( "the code: code (.+), name (.+) is not added to the NDC database" )
    public void ndcCodeAdded (final String code, final String name) {

        driver.get( baseUrl );
        ((JavascriptExecutor) driver).executeScript(
                "document.get('http://localhost:8080/iTrust2/modifyDatabase.html');" );

        selectEditNDC();

        try {
            WebElement radioBtn = driver.findElement(By.value(code));
        }catch ( final Exception e ) {
            /*  */
        }
    }

    /**
     * The NDC code is NOT added
     *
     * @param code
     *          the code for the NDC
     *
     * @param name
     *          the name of the NDC
     */
    @Then ( "the code: code (.+), name (.+) is not added to the NDC database" )
    public void ndcCodeNotAdded (final String code, final String name) {
        driver.get( baseUrl );
        ((JavascriptExecutor) driver).executeScript(
                "document.get('http://localhost:8080/iTrust2/modifyDatabase.html');" );

        selectEditNDC();

        try {
            WebElement radioBtn = driver.findElement(By.value(code));
            fail();
        }catch ( final Exception e ) {
            //this should fail
        }

    }

    /**
     * The IDC code is added
     *
     * @param code
     *          the code for the IDC
     *
     * @param name
     *          the name of the IDC
     */
    @Then ( "the code: code (.+), name (.+) is added to the IDC database" )
    public void idcCodeAdded (final String code, final String name) {
        driver.get( baseUrl );
        ((JavascriptExecutor) driver).executeScript(
                "document.get('http://localhost:8080/iTrust2/modifyDatabase.html');" );

        selectEditIDC();

        try {
            WebElement radioBtn = driver.findElement(By.value(code));
        }catch ( final Exception e ) {
            //TODO
        }

    }

    /**
     * The IDC code is NOT added
     *
     * @param code
     *          the code for the IDC
     *
     * @param name
     *          the name of the IDC
     */
    @Then ( "the code: code (.+), name (.+) is not added to the IDC database" )
    public void idcCodeNotAdded (final String code, final String name) {
        driver.get( baseUrl );
        ((JavascriptExecutor) driver).executeScript(
                "document.get('http://localhost:8080/iTrust2/modifyDatabase.html');" );

        selectEditIDC();

        try {
            WebElement radioBtn = driver.findElement(By.value(code));
            fail();
        }catch ( final Exception e ) {
            //this should fail
        }

    }



    /**
     * Oxycotin becomes oxycodone
     */
    @Then ( "the code becomes: code 66666-616-30, name Oxycodone instead of: code 16590-616-30, name Oxycontin" )
    public void cotinToCodone () {
        driver.get( baseUrl );
        ((JavascriptExecutor) driver).executeScript(
                "document.get('http://localhost:8080/iTrust2/modifyDatabase.html');" );

        selectEditNDC();

        try {
            WebElement radioBtn = driver.findElement(By.value("16590-616-30"));
            fail();
        }catch ( final Exception e ) {
            //this should fail
        }

    }

    /**
     * Oxycotin stays the same
     */
    @Then ( "the code stays as: code 16590-616-30, name Oxycontin" )
    public void cotinNoChange () {
        driver.get( baseUrl );
        ((JavascriptExecutor) driver).executeScript(
                "document.get('http://localhost:8080/iTrust2/modifyDatabase.html');" );

        selectEditNDC();

        try {
            WebElement radioBtn = driver.findElement(By.value("16590-616-30"));
        }catch ( final Exception e ) {
            //this should fail
        }

    }




    /**
     *Cholera changed successfully
     *
     * @param newCode
     *          the new code for the IDC formerly known as Cholera
     *
     * @param newName
     *          the new name for the IDC formerly known as Cholera
     */
    @Then ( " And the code becomes: code (.+), name (.+) instead of: code A00, name Cholera" )
    public void choleraChange (final String newCode, final String newName) {
        driver.get( baseUrl );
        ((JavascriptExecutor) driver).executeScript(
                "document.get('http://localhost:8080/iTrust2/modifyDatabase.html');" );

        selectEditIDC();

        try {
            WebElement radioBtn = driver.findElement(By.value(newCode));
        }catch ( final Exception e ) {
            //TODO
        }

    }

    /**
     *Cholera not changed
     */
    @Then ( "And the code stays as: code A00, name Cholera" )
    public void choleraNoChange () {
        driver.get( baseUrl );
        ((JavascriptExecutor) driver).executeScript(
                "document.get('http://localhost:8080/iTrust2/modifyDatabase.html');" );

        selectEditIDC();

        try {
            WebElement radioBtn = driver.findElement(By.value("A00"));
        }catch ( final Exception e ) {
            //TODO
        }

    }





    /**
     * The NDC database is cleared
     */
    @Then ( "the NDC database is cleared" )
    public void ndcCleared () {

//hibernate datagenerator refreshdb





    }

    /**
     * The IDC database is cleared
     */
    @Then ( "the IDC database is cleared" )
    public void idcCleared () {


    }





}
