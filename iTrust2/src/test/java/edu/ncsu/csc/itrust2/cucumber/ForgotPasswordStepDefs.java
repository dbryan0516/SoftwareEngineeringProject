package edu.ncsu.csc.itrust2.cucumber;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.URLName;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import edu.ncsu.csc.itrust2.utils.HibernateDataGenerator;

/**
 *
 * @author Joshua Kayani (jkayani)
 *
 */
public class ForgotPasswordStepDefs {

    /** Email address from which to send reset emails */
    private static final String ITRUST2_EMAIL_ADDRESS  = "itrust22045@gmail.com";

    /** Terrible thing to do - password for the email account */
    private static final String ITRUST2_EMAIL_PASSWORD = "pass4itrust";

    /** The subject line of the reset password emails iTrust2 sends */
    private static final String ITRUST2_EMAIL_SUBJECT  = "iTrust2-TP-204-5 Password Reset";

    /** Gmail's IMAP address */
    private static final String GMAIL_IMAP_ADDRESS     = "imap.gmail.com";

    private WebDriver           driver;
    private final String        baseUrl                = "http://localhost:8080/iTrust2";
    private WebDriverWait       wait;
    private String              resetToken             = "";

    @Given ( "^the required agents exist$" )
    public void loadUsers () {
        HibernateDataGenerator.refreshDB();

        // Presumably this causes the User to not be logged in at the start of
        // the test
        driver = new HtmlUnitDriver( true );
        wait = new WebDriverWait( driver, 10 );
    }

    @When ( "^I navigate to iTrust2$" )
    public void openiTrust () {
        driver.get( baseUrl );
    }

    @When ( "^I select the Forgot Password link$" )
    public void clickResetPassword () {
        driver.findElement( By.linkText( "Forgot Password?" ) ).click();
        assertTrue( driver.getTitle().contains( "Forgot Password" ) );
    }

    @When ( "^I enter my username (.+)$" )
    public void inputUsername ( String username ) {
        final WebElement usernameElm = driver.findElement( By.id( "username" ) );
        usernameElm.clear();
        usernameElm.sendKeys( username );
    }

    @When ( "^I submit the reset request$" )
    public void submitResetRequest () {
        wait.until( ExpectedConditions.elementToBeClickable( By.id( "submitResetRequest" ) ) );
        driver.findElement( By.id( "submitResetRequest" ) ).click();
    }

    @When ( "^I successfully submit the reset request$" )
    public void submitResetRequestSuccess () {
        submitResetRequest();
        wait.until( ExpectedConditions.textToBePresentInElementLocated( By.id( "requestResetStatus" ),
                "Password reset request email sent" ) );
        assertEquals( "", driver.findElement( By.id( "requestResetErrorMsg" ) ).getText() );
    }

    @When ( "^I retrieve the reset token$" )
    public void getResetToken () throws Exception {

        /*
         * Thanks:
         * http://javapapers.com/java/receive-email-in-java-using-javamail-gmail
         * -imap-example/
         */
        final Properties props = new Properties();
        final URLName url = new URLName( "imaps", GMAIL_IMAP_ADDRESS, 993, "INBOX", ITRUST2_EMAIL_ADDRESS,
                ITRUST2_EMAIL_PASSWORD );
        final Session session = Session.getInstance( props );
        final Store store = session.getStore( url );

        // Connect to Gmail via IMAP
        store.connect();

        // Grab their messages
        final Folder inbox = store.getFolder( url );
        inbox.open( Folder.READ_ONLY );
        final Message[] messages = inbox.getMessages();

        // Sort them from latest to earliest received
        Arrays.sort( messages, new Comparator<Message>() {

            @Override
            public int compare ( Message o1, Message o2 ) {
                try {
                    return o2.getReceivedDate().compareTo( o1.getReceivedDate() );
                }
                catch ( final MessagingException e ) {
                    fail();
                }
                return -1;
            }
        } );

        // Extract the token from the latest one that matches our subject line
        for ( final Message m : messages ) {
            if ( m.getSubject().equals( ITRUST2_EMAIL_SUBJECT ) ) {
                final String body = m.getContent().toString();
                resetToken = body.substring( body.lastIndexOf( "Token: " ) + 7, body.lastIndexOf( "\r" ) );
                break;
            }
        }

        // Disconnect from Gmail
        assertTrue( resetToken != null && !resetToken.isEmpty() );
        inbox.close();
        store.close();
    }

    @When ( "^I (.+) enter my reset token, (.+) new password (.+), and (.+) repeated new password (.+)$" )
    public void doChangePasswordForm ( String doToken, String doPassword1, String newPassword1, String doPassword2,
            String newPassword2 ) {
        final WebElement tokenElm = driver.findElement( By.id( "passwordToken" ) );
        final WebElement newPassword1Elm = driver.findElement( By.id( "newPassword1" ) );
        final WebElement newPassword2Elm = driver.findElement( By.id( "newPassword2" ) );
        tokenElm.clear();
        newPassword1Elm.clear();
        newPassword2Elm.clear();
        if ( doToken.equals( "do" ) ) {
            tokenElm.sendKeys( resetToken );
        }
        if ( doPassword1.equals( "do" ) ) {
            newPassword1Elm.sendKeys( newPassword1 );
        }
        if ( doPassword2.equals( "do" ) ) {
            newPassword2Elm.sendKeys( newPassword2 );
        }
    }

    @When ( "^I submit the new password$" )
    public void submitChangePassword () {
        driver.findElement( By.id( "changePassword" ) ).click();
    }

    @Then ( "^a password reset success message is displayed$" )
    public void verifyResetSuccess () {
        wait.until( ExpectedConditions.textToBePresentInElementLocated( By.id( "changePasswordStatus" ),
                "Password updated successfully" ) );
        assertEquals( "", driver.findElement( By.id( "changePasswordErrorMsg" ) ).getText() );
    }

    @Then ( "^a reset request failure message is displayed$" )
    public void submitResetRequestFailure () {
        wait.until( ExpectedConditions.textToBePresentInElementLocated( By.id( "requestResetStatus" ),
                "An error occurred" ) );
        assertNotEquals( "", driver.findElement( By.id( "requestResetErrorMsg" ) ).getText() );
    }

    @Then ( "^a password reset failure message is displayed$" )
    public void verifyResetFailure () {
        wait.until( ExpectedConditions.textToBePresentInElementLocated( By.id( "changePasswordStatus" ),
                "An error occurred" ) );
        assertNotEquals( "", driver.findElement( By.id( "changePasswordErrorMsg" ) ).getText() );
    }

    @Then ( "^I can log in with username (.+) and password (.+)$" )
    public void attemptLoginNewCredentials ( String username, String password ) {

        // Go to login page
        openiTrust();

        // Log in
        final WebElement userElm = driver.findElement( By.name( "username" ) );
        userElm.clear();
        userElm.sendKeys( username );
        final WebElement passwordElm = driver.findElement( By.name( "password" ) );
        passwordElm.clear();
        passwordElm.sendKeys( password );
        final WebElement submit = driver.findElement( By.className( "btn" ) );
        submit.click();

        assertTrue( driver.getPageSource().contains( "Welcome to iTrust2" ) );
    }
}
