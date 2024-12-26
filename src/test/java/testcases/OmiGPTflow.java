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
import org.testng.annotations.Test;

import javax.mail.*;
import javax.mail.internet.*;
import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;

public class OmiGPTflow extends BaseTest {

    private ExtentReports extent;
    private String reportPath = System.getProperty("user.dir") + "\\ExtentReport.html";
    private String screenshotDir = System.getProperty("user.dir") + "\\screenshots";

    public OmiGPTflow() {
        // Initialize ExtentReports
        ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);
        extent = new ExtentReports();
        extent.attachReporter(sparkReporter);
    }

    @Test
    public void executePromptWorkflow() {
        ExtentTest test = extent.createTest("executePromptWorkflow", "Execute prompt workflow with Extent Reports and email reporting");
        try {
            driver.manage().window().maximize();
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
            JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;

            // Locate and interact with the prompt field
            WebElement promptField = wait.until(ExpectedConditions.elementToBeClickable(By.id("prompt")));
            promptField.sendKeys("write essay on India");
            test.info("Entered text into the prompt.");

            // Locate and click the Enter button
            WebElement enterButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//*[@id='response-container']/section[3]/div/div/button")));
            enterButton.click();
            test.info("Clicked the Enter button.");

            // Wait for the response container to appear
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='response-container']/section[2]")));
            test.info("Response container appeared successfully.");

            // Take a screenshot
            String screenshotPath = takeScreenshot("PromptWorkflow");
            test.addScreenCaptureFromPath(screenshotPath, "Prompt Workflow Screenshot");

            test.pass("Prompt workflow executed successfully!");
        } catch (Exception e) {
            test.fail("Test failed due to an exception: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private String takeScreenshot(String fileName) throws IOException {
        File directory = new File(screenshotDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        String screenshotPath = screenshotDir + "\\" + fileName + ".png";
        File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(srcFile, new File(screenshotPath));
        return screenshotPath;
    }

    @AfterClass
    public void tearDown() {
        try {
            extent.flush();
            sendEmailWithAttachment(reportPath);
            System.out.println("Email sent successfully with the Extent Report.");
        } catch (Exception e) {
            System.err.println("Failed to send email: " + e.getMessage());
        } finally {
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
            message.setSubject("Extent Report - Test Execution");

            // Message body
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText("Please find the attached Extent Report for the test execution.");

            // Attachment
            MimeBodyPart attachmentPart = new MimeBodyPart();
            attachmentPart.attachFile(new File(filePath));

            // Multipart
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);
            multipart.addBodyPart(attachmentPart);

            message.setContent(multipart);

            Transport.send(message);
        } catch (Exception e) {
            System.err.println("Failed to send email: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
