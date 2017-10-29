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
            final WebElement code = driver.findElement(By.value("16590-616-30"));
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
    @When ( "I fill out the fields with code, name <NDC_name>" )
    public void fillNDCfields () {
        final WebElement code = driver.findElement( By.id( "code" ) );
        code.clear();
        code.sendKeys( "<NDC_code>" );

        final WebElement name = driver.findElement( By.id( "name" ) );
        name.clear();
        name.sendKeys( "<NDC_name>" );

    }

    /**
     * Fill IDC code forms
     */
    @When ( "I fill out the fields with code <IDC_code>, name <IDC_name>" )
    public void fillIDCfields () {
        final WebElement code = driver.findElement( By.id( "code" ) );
        code.clear();
        code.sendKeys( "<IDC_code>" );

        final WebElement name = driver.findElement( By.id( "name" ) );
        name.clear();
        name.sendKeys( "<IDC_name>" );

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
     */
    @When ( "And I fill out the fields with code <NDC_new_code>, name Oxycontin Drugs" )
    public void fillCotinToCodonefieldsIncorrect () {
        final WebElement code = driver.findElement( By.id( "code" ) );
        code.clear();
        code.sendKeys( "<NDC_new_code>" );

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
    public void createdSuccessfully () {
        assertTrue( driver.getPageSource().contains( "Database modification successful" ) );
    }

    /**
     * See fail message
     */
    @Then ( "I see an error message" )
    public void createdSuccessfully () {
        assertTrue( driver.getPageSource().contains( "Database modification failed" ) );
    }









    /**
     * The NDC code is added
     */
    @Then ( "the code: code <NDC_code>, name <NDC_name> is added to the NDC database" )
    public void ndcCodeAdded () {


    }

    /**
     * The NDC code is NOT added
     */
    @Then ( "the code: code <NDC_code>, name <NDC_name> is not added to the NDC database" )
    public void ndcCodeNotAdded () {


    }

    /**
     * The IDC code is added
     */
    @Then ( "the code: code <ICD_code>, name <ICD_name> is added to the ICD database " )
    public void idcCodeAdded () {


    }

    /**
     * The IDC code is NOT added
     */
    @Then ( "the code: code <IDC_code>, name <IDC_name> is not added to the IDC database" )
    public void idcCodeNotAdded () {


    }

    /**
     * Oxycotin becomes oxycodone
     */
    @Then ( "the code becomes: code 66666-616-30, name Oxycodone instead of: code 16590-616-30, name Oxycontin" )
    public void cotinToCodone () {


    }

    /**
     * Oxycotin stays the same
     */
    @Then ( "the code stays as: code 16590-616-30, name Oxycontin" )
    public void cotinNoChange () {


    }










    /**
     * The NDC database is cleared
     */
    @Then ( "the NDC database is cleared" )
    public void ndcCleared () {


    }

    /**
     * The IDC database is cleared
     */
    @Then ( "the IDC database is cleared" )
    public void idcCleared () {


    }





}
