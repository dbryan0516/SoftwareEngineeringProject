package edu.ncsu.csc.itrust2.cucumber;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import edu.ncsu.csc.itrust2.models.persistent.Lockout;
import edu.ncsu.csc.itrust2.models.persistent.User;
import edu.ncsu.csc.itrust2.utils.HibernateDataGenerator;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.junit.Assert.*;

public class PasswordLockoutStepDefs {
    private static final int MILLIS_IN_MINUTE = 60000;
    private static final int MILLIS_IN_HOUR = MILLIS_IN_MINUTE * 60;

    private final WebDriver driver = new HtmlUnitDriver( true );
    private final WebDriverWait wait = new WebDriverWait( driver, 5 );

    @Given( "^A normal patient exists in the system$" )
    public void normalPatientExists() {
        HibernateDataGenerator.refreshDB();
    }

    @Given( "^A patient with an old lockout exists in the system$" )
    public void patientWithOldLockoutExists() {
        HibernateDataGenerator.refreshDB();
        User patient = User.getWhere( "username='patient'" ).get( 0 );
        patient.setNumFailAttempts( User.MAX_LOGIN_ATTEMPTS );
        patient.setLockoutTimeout( System.currentTimeMillis() - MILLIS_IN_MINUTE );
        patient.save();
    }

    @Given( "^A patient with two lockouts exists in the system$" )
    public void patientWithTwoLockoutExists() {
        HibernateDataGenerator.refreshDB();
        Lockout lockout = new Lockout( "patient", System.currentTimeMillis() + MILLIS_IN_HOUR * 24 );
        lockout.save();
        lockout = new Lockout( "patient", System.currentTimeMillis() + MILLIS_IN_HOUR * 23 );
        lockout.save();
    }

    @When( "^I navigate to the Login page$" )
    public void navigateToLogin() {
        driver.get( "http://localhost:8080/iTrust2" );
        wait.until( ExpectedConditions.titleContains( "Login" ) );
    }

    @When( "^I incorrectly enter my password$" )
    public void incorrectlyEnterPassword() {
        login( "invalid" );
    }

    @When( "^I correctly enter my password$" )
    public void correctlyEnterPassword() {
        login( "123456" );
    }

    @Then( "^I am locked out of the system$" )
    public void isLockedOutOfSystem() {
        User patient = User.getWhere( "username='patient'" ).get( 0 );
        assertTrue( patient.getNumFailAttempts() >= User.MAX_LOGIN_ATTEMPTS );
        assertNotNull( patient.getLockoutTimeout() );
    }

    @Then( "^I cannot log in with my valid password$" )
    public void cannotLoginWithValidPassword() {
        login( "123456" );
        assertTrue( driver.getTitle().contains( "Login" ) );
        HibernateDataGenerator.refreshDB();
    }

    @Then( "^I am successfully logged in$" )
    public void successfullyLoggedIn() {
        // will timeout if the user doesn't change
        wait.until( ExpectedConditions.titleContains( "Patient" ) );
    }

    @Then( "^My lock history is cleared$" )
    public void lockHistoryCleared() {
        User patient = User.getWhere( "username='patient'" ).get( 0 );
        assertTrue( patient.getNumFailAttempts() == 0 );
        assertNull( patient.getLockoutTimeout() );
        HibernateDataGenerator.refreshDB();
    }

    @Then( "^My account is disabled$" )
    public void accountDisabled() {
        User patient = User.getWhere( "username='patient'" ).get( 0 );
        assertTrue( patient.getEnabled() == 0 );
    }

    private void login( String pass ) {
        final WebElement username = driver.findElement( By.name( "username" ) );
        username.clear();
        username.sendKeys( "patient" );
        final WebElement password = driver.findElement( By.name( "password" ) );
        password.clear();
        password.sendKeys( pass );
        final WebElement submit = driver.findElement( By.className( "btn" ) );
        submit.click();
    }
}
