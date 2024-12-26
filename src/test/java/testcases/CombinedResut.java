package testcases;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;
import base.BaseTest;

import javax.mail.*;
import javax.mail.internet.*;
import java.io.File;
import java.time.Duration;
import java.util.Properties;

public class CombinedResut extends BaseTest {

    private ExtentReports extent;
    private String reportPath = System.getProperty("user.dir") + "\\ExtentReport.html";
    private String screenshotDir = System.getProperty("user.dir") + "\\screenshots";

    @Test
    public void combinedTestExecution() {
        setupReport();

        ExtentTest promptTest = extent.createTest("Prompt Workflow Test", "Execute prompt workflow");
        executePromptWorkflow(promptTest);

        ExtentTest scrollTest = extent.createTest("Scroll and Copy Test", "Validate scrolling and copying");
        executeScrollAndCopy(scrollTest);

        extent.flush();
        sendEmailWithAttachment(reportPath);
    }

    private void setupReport() {
        ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);
        extent = new ExtentReports();
        extent.attachReporter(sparkReporter);
    }

    private void executePromptWorkflow(ExtentTest test) {
        try {
            driver.manage().window().maximize();
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

            WebElement promptField = wait.until(ExpectedConditions.elementToBeClickable(By.id("prompt")));
            promptField.sendKeys("write essay on India");
            test.info("Entered prompt.");

            WebElement enterButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='response-container']/section[3]/div/div/button")));
            enterButton.click();
            test.info("Clicked Enter button.");

            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='response-container']/section[2]")));
            test.info("Response visible.");

            captureScreenshot(test, "PromptWorkflow");
            test.pass("Prompt workflow completed.");
        } catch (Exception e) {
            test.fail("Prompt workflow failed: " + e.getMessage());
        }
    }

    private void executeScrollAndCopy(ExtentTest test) {
        try {
            JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
            WebElement copyButton = driver.findElement(By.xpath("//*[@id='response-container']/section[2]/section/div[1]/div[3]/button[2]/span"));
            jsExecutor.executeScript("arguments[0].scrollIntoView(true);", copyButton);
            copyButton.click();
            test.info("Copy button clicked.");

            captureScreenshot(test, "ScrollAndCopy");
            test.pass("Scroll and Copy Test completed.");
        } catch (Exception e) {
            test.fail("Scroll and Copy Test failed: " + e.getMessage());
        }
    }

    private void captureScreenshot(ExtentTest test, String testName) {
        try {
            File directory = new File(screenshotDir);
            if (!directory.exists()) {
                directory.mkdirs();
            }
            String screenshotPath = screenshotDir + "\\" + testName + "_" + System.currentTimeMillis() + ".png";
            File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(srcFile, new File(screenshotPath));
            test.addScreenCaptureFromPath(screenshotPath, "Screenshot for " + testName);
        } catch (Exception e) {
            test.warning("Failed to capture screenshot: " + e.getMessage());
        }
    }

    private void sendEmailWithAttachment(String filePath) {
        String from = "your_email@gmail.com";
        String password = "your_email_password";
        String to = "recipient_email@gmail.com";
        String host = "smtp.gmail.com";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject("Automation Test Report");

            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText("Please find the attached test report.");

            MimeBodyPart attachmentPart = new MimeBodyPart();
            attachmentPart.attachFile(new File(filePath));

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);
            multipart.addBodyPart(attachmentPart);

            message.setContent(multipart);
            Transport.send(message);
            System.out.println("Email sent successfully.");
        } catch (Exception e) {
            System.err.println("Email sending failed: " + e.getMessage());
        }
    }
} 
