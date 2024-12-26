package testcases;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import base.BaseTest;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.mail.*;
import javax.mail.internet.*;
import java.io.File;
import java.time.Duration;
import java.util.Properties;

public class SignupEmail extends BaseTest {
    private WebDriver driver;
    private ExtentReports extent;
    private ExtentTest test;

    @BeforeClass
    public void setUp() {
        // Set up ExtentReports
        String reportPath = System.getProperty("user.dir") + "/ExtentReport.html";
        ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);
        extent = new ExtentReports();
        extent.attachReporter(sparkReporter);

        // Set up ChromeDriver
        driver = new ChromeDriver();
        driver.manage().window().maximize();
    }

    @Test
    public void testSignUp() {
        test = extent.createTest("testSignUp", "Verify sign-up navigation");

        try {
            // Navigate to the website
         

            // Create WebDriverWait instance
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            // Locate and click on the dropdown button "gpt-4o-mini"
            WebElement dropdownButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[text()='gpt-4o-mini']")));
            dropdownButton.click();
            test.info("Clicked on the dropdown button.");

            // Wait for the "Sign up" link and click it
            WebElement signUpButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[contains(@class, 'py-2 px-4 rounded-full text-black bg-white border border-gray-400 hover:bg-gray-100') and text()='Sign up']")));
            signUpButton.click();
            test.info("Clicked on the 'Sign up' link.");

            // Wait for the expected sign-up page content
            WebElement expectedElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(text(), 'Expected Sign Up Content')]"))); // Replace with an appropriate element
            test.info("Navigated to the sign-up page.");

            // Validate the sign-up page content
            if (expectedElement.isDisplayed()) {
                test.pass("Navigated to the sign-up page successfully and content is displayed!");
            } else {
                test.fail("Content on the sign-up page is not displayed.");
            }
        } catch (Exception e) {
            test.fail("Test failed due to an exception: " + e.getMessage());
        }
    }

    @AfterClass
    public void tearDown() {
        // Flush the ExtentReports
        extent.flush();

        // Send the email with the report
        sendEmailWithAttachment(System.getProperty("user.dir") + "/ExtentReport.html");

        // Quit the browser
        if (driver != null) {
            driver.quit();
        }
    }

    private void sendEmailWithAttachment(String filePath) {
        // Email Configuration
        String from = "shingne.yogesh@gmail.com"; // Replace with your email
        String password = "wikf xvph jumm zvzm"; // Replace with your email's app-specific password
        String to = "prem@omnineura.ai"; // Replace with recipient's email
        String host = "smtp.gmail.com";

        // Set properties
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", "587");

        // Authenticate
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, password);
            }
        });

        try {
            // Create a message
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject("Test Automation Report - Sign Up Test");

            // Create the message body
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText("Please find the attached report for the Sign Up test.");

            // Create multipart
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);

            // Attach the report file
            MimeBodyPart attachmentPart = new MimeBodyPart();
            attachmentPart.attachFile(new File(filePath));
            multipart.addBodyPart(attachmentPart);

            // Set content
            message.setContent(multipart);

            // Send the email
            Transport.send(message);
            System.out.println("Email sent successfully with the test report.");
        } catch (Exception e) {
            System.err.println("Failed to send email: " + e.getMessage());
        }
    }
}
