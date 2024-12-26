package testcases;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import base.BaseTest;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.mail.*;
import javax.mail.internet.*;
import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;

public class SignUpPromptScrollDown extends BaseTest {
    private WebDriver driver;
    private ExtentReports extent;
    private String reportPath = System.getProperty("user.dir") + "/ExtentReport.html";
    private String screenshotDir = System.getProperty("user.dir") + "/screenshots";

    @BeforeClass
    public void setUp() {
        // Initialize ExtentReports
        ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);
        extent = new ExtentReports();
        extent.attachReporter(sparkReporter);

        // Set up ChromeDriver
        driver.manage().window().maximize();
    }

    @Test
    public void testOmnigptFlow() {
        ExtentTest test = extent.createTest("testOmnigptFlow", "Automate Omnigpt prompt and report generation");
        try {
            // Navigate to the website (replace with actual URL)
            driver.get("https://yourwebsite.com");
            test.info("Navigated to the Omnigpt website.");

            // Enter text in the prompt
            WebElement searchPrompt = driver.findElement(By.xpath("//*[@id='prompt']"));
            searchPrompt.sendKeys("write on Indian history in 150 words");
            test.info("Entered text into the prompt.");

            // Click the submit button
            WebElement enterButton = driver.findElement(By.xpath("//*[@id='response-container']/section[3]/div/div/button"));
            enterButton.click();
            test.info("Clicked the Enter button.");

            // Wait for the response to load
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='response-container']/section[2]")));
            test.info("Response container is visible.");

            // Wait for content to load
            Thread.sleep(30000); // Wait for 30 seconds for the content to load

            // Take Screenshot
            String screenshotPath = takeScreenshot("OmnigptTest");
            test.addScreenCaptureFromPath(screenshotPath, "Test Screenshot");

            // Validate the content
            WebElement responseContent = driver.findElement(By.xpath("//*[@id='response-container']/section[2]"));
            if (responseContent.isDisplayed()) {
                test.pass("Response content is displayed successfully.");
            } else {
                test.fail("Response content not displayed.");
            }

        } catch (Exception e) {
            test.fail("Test failed due to an exception: " + e.getMessage());
        }
    }

    private String takeScreenshot(String fileName) throws IOException {
        File directory = new File(screenshotDir);
        if (!directory.exists()) {
            directory.mkdirs(); // Create directory if it doesn't exist
        }
        String screenshotPath = screenshotDir + "/" + fileName + ".png";
        File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(srcFile, new File(screenshotPath));
        return screenshotPath;
    }

    @AfterClass
    public void tearDown() {
        extent.flush();
        sendEmailWithAttachment(reportPath);
        if (driver != null) {
            driver.quit();
        }
    }

    private void sendEmailWithAttachment(String filePath) {
        String from = "shingne.yogesh@gmail.com"; // Replace with your email
        String password = "wikf xvph jumm zvzm"; // Replace with your email's app-specific password
        String to = "yogeshshingne7@gmail.com"; // Replace with recipient's email
        String host = "smtp.gmail.com";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, password);
            }
        });

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject("Extent Report - Omnigpt Flow Test Execution");

            // Message body
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText("Please find the attached Extent Report for the Omnigpt Flow test execution.");

            // Attachment
            MimeBodyPart attachmentPart = new MimeBodyPart();
            attachmentPart.attachFile(new File(filePath));

            // Multipart
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);
            multipart.addBodyPart(attachmentPart);

            message.setContent(multipart);

            Transport.send(message);
            System.out.println("Email sent successfully with the test report.");
        } catch (Exception e) {
            System.err.println("Failed to send email: " + e.getMessage());
        }
    }
}
