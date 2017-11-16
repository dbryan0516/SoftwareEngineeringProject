package edu.ncsu.csc.itrust2.cucumber;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import edu.ncsu.csc.itrust2.config.RootConfiguration;
import edu.ncsu.csc.itrust2.mvc.config.WebMvcConfiguration;
import edu.ncsu.csc.itrust2.utils.HibernateDataGenerator;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * Tests the Change Password feature file.
 *
 * @author Galen Abell (gjabell)
 */
@ContextConfiguration( classes = { RootConfiguration.class, WebMvcConfiguration.class } )
@WebAppConfiguration
public class ChangePasswordStepDefs {
    private final WebDriver driver = new HtmlUnitDriver( true );
    private final String baseURL = "http://localhost:8080/iTrust2";

    private final WebDriverWait wait = new WebDriverWait( driver, 2 );

    private final String oldPassword = "123456";

    @Given( "^The required agents exist$" )
    public void agentsExist() {
        HibernateDataGenerator.refreshDB();
    }

    @When( "^I log in as a Patient$" )
    public void loginAsPatient() {
        login( "patient" );
    }

    @When( "^I log in as an HCP" )
    public void loginAsHCP() {
        login( "hcp" );
    }

    @When( "^I log in as an Admin" )
    public void loginAsAdmin() {
        login( "admin" );
    }

    @When( "^I navigate to the Change Password page$" )
    public void navigateToChangePassword() {
        wait.until( ExpectedConditions.titleContains( "Home" ) );
        ( (JavascriptExecutor) driver ).executeScript( "document.getElementById('changePassword').click();" );
    }

    @When( "^I enter my current password, new password, and repeated new password$" )
    public void enterPasswords() {
        WebElement passwordToken = driver.findElement( By.id( "passwordToken" ) );
        passwordToken.clear();
        passwordToken.sendKeys( oldPassword );
        WebElement newPassword1 = driver.findElement( By.id( "newPassword1" ) );
        newPassword1.clear();
        newPassword1.sendKeys( "1234567" );
        WebElement newPassword2 = driver.findElement( By.id( "newPassword2" ) );
        newPassword2.clear();
        newPassword2.sendKeys( "1234567" );
    }

    @When( "^I submit the information$" )
    public void submit() {
        driver.findElement( By.name( "submit" ) ).click();
    }

    @Then( "^A corresponding success message is displayed$" )
    public void hasSuccess() {
        wait.until( ExpectedConditions.textToBe( By.name( "success" ), "Password updated successfully" ) );
        HibernateDataGenerator.refreshDB();
    }

    private void login( final String usernameString ) {
        driver.get( baseURL );
        final WebElement username = driver.findElement( By.name( "username" ) );
        username.clear();
        username.sendKeys( usernameString );
        final WebElement password = driver.findElement( By.name( "password" ) );
        password.clear();
        password.sendKeys( oldPassword );
        final WebElement submit = driver.findElement( By.className( "btn" ) );
        submit.click();
    }
}
