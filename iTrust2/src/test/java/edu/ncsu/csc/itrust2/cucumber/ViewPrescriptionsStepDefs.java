package edu.ncsu.csc.itrust2.cucumber;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import edu.ncsu.csc.itrust2.utils.HibernateDataGenerator;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import static junit.framework.TestCase.assertTrue;

public class ViewPrescriptionsStepDefs {
    private final WebDriver driver = new HtmlUnitDriver(true);
    private final String baseUrl = "http://localhost:8080/iTrust2";
    private final WebDriverWait wait = new WebDriverWait(driver, 5);

    @Given("^The patient and prescriptions exist$")
    public void dataExists() {
        // reset database
        HibernateDataGenerator.refreshDB();

        login("patient", "123456");

        // add the patient's demographics
        ((JavascriptExecutor) driver).executeScript("document.getElementById('editdemographics').click();");
        try {
            final WebElement firstName = driver.findElement(By.id("firstName"));
            firstName.clear();
            firstName.sendKeys("First");

            final WebElement lastName = driver.findElement(By.id("lastName"));
            lastName.clear();
            lastName.sendKeys("Last");

            final WebElement preferredName = driver.findElement(By.id("preferredName"));
            preferredName.clear();

            final WebElement mother = driver.findElement(By.id("mother"));
            mother.clear();

            final WebElement father = driver.findElement(By.id("father"));
            father.clear();

            final WebElement email = driver.findElement(By.id("email"));
            email.clear();
            email.sendKeys("first.last@gmail.com");

            final WebElement address1 = driver.findElement(By.id("address1"));
            address1.clear();
            address1.sendKeys("Address");

            final WebElement city = driver.findElement(By.id("city"));
            city.clear();
            city.sendKeys("Raleigh");

            final WebElement state = driver.findElement(By.id("state"));
            final Select dropdown = new Select(state);
            dropdown.selectByVisibleText("NC");

            final WebElement zip = driver.findElement(By.id("zip"));
            zip.clear();
            zip.sendKeys("27607");

            final WebElement phone = driver.findElement(By.id("phone"));
            phone.clear();
            phone.sendKeys("123-456-7890");

            final WebElement dob = driver.findElement(By.id("dateOfBirth"));
            dob.clear();
            dob.sendKeys("01/01/1984");

            final WebElement submit2 = driver.findElement(By.className("btn"));
            submit2.click();
        } catch (final Exception e) {
            /*  */
        } finally {
            driver.findElement(By.id("logout")).click();
        }
    }

    @When("^I log into iTrust2 as a Patient$")
    public void loginAsPatient() {
        login("patient", "123456");
    }

    @When("^I log into iTrust2 as an HCP$")
    public void loginAsHCP() {
        login("hcp", "123456");
    }

    @When("^I navigate to the View Prescriptions page$")
    public void navigateToPrescriptionPage() {
        ((JavascriptExecutor) driver).executeScript("document.getElementById('viewPrescriptions').click();");
    }

    @When("^I select the patient from the list$")
    public void selectPatientFromList() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input[value=\"patient\"]")));
        final WebElement patient = driver.findElement(By.cssSelector("input[value=\"patient\"]"));
        patient.click();
    }

    @Then("^My prescriptions are correctly displayed$")
    public void myPrescriptionsDisplayedCorrectly() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("Androxy")));
        final WebElement prescription = driver.findElement(By.name("Androxy"));
        assertTrue(prescription.isDisplayed());
    }

    @Then("^The patient's prescriptions are displayed correctly$")
    public void patientPrescriptionsDisplayedCorrectly() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("Androxy")));
        final WebElement prescription = driver.findElement(By.name("Androxy"));
        assertTrue(prescription.isDisplayed());
    }

    private void login(String user, String pass) {
        driver.get(baseUrl);
        final WebElement username = driver.findElement(By.name("username"));
        username.clear();
        username.sendKeys(user);
        final WebElement password = driver.findElement(By.name("password"));
        password.clear();
        password.sendKeys(pass);
        final WebElement submit = driver.findElement(By.className("btn"));
        submit.click();
    }
}
