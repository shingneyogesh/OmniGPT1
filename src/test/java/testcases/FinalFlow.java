package testcases;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

public class FinalFlow extends BaseTest {

    private static final Logger logger = LogManager.getLogger(FinalFlow.class);
    private ExtentReports extent;
    private String reportPath = System.getProperty("user.dir") + "\\ExtentReport.html";
    private String screenshotDir = System.getProperty("user.dir") + "\\screenshots";

    @Test
    public void executeAllTests() {
        setupReport();
        logger.info("Starting all tests...");

        ExtentTest promptTest = extent.createTest("Prompt Workflow Test", "Execute prompt workflow with reporting");
        executePromptWorkflow(promptTest);

        ExtentTest copyButtonTest = extent.createTest("Copy Button Test", "Validate the functionality of the Copy button");
        testCopyButton(copyButtonTest);

        ExtentTest readAloudTest = extent.createTest("Read Aloud Test", "Validate the Read Aloud functionality");
        testReadAloud(readAloudTest);

        ExtentTest regeneratePromptTest = extent.createTest("Regenerate Prompt Test", "Validate the Regenerate Prompt functionality");
        testRegeneratePrompt(regeneratePromptTest);

        ExtentTest closeSidebarTest = extent.createTest("Close Sidebar Test", "Validate the Close Sidebar functionality");
        testCloseSidebar(closeSidebarTest);

        extent.flush();
        logger.info("All tests completed. Sending email with the report...");
        sendEmailWithAttachment(reportPath);
    }

    private void setupReport() {
        logger.info("Setting up Extent report...");
        ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);
        extent = new ExtentReports();
        extent.attachReporter(sparkReporter);
    }

    private void executePromptWorkflow(ExtentTest test) {
        try {
            logger.info("Executing prompt workflow...");
            driver.manage().window().maximize();
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

            WebElement promptField = wait.until(ExpectedConditions.elementToBeClickable(By.id("prompt")));
            promptField.sendKeys("write essay on India");
            logger.info("Entered prompt: 'write essay on India'");
            test.info("Entered the prompt 'write essay on India'.");

            WebElement enterButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(
                    "//*[@id='response-container']/section[3]/div/div/button")));
            enterButton.click();
            logger.info("Clicked the Enter button.");
            test.info("Clicked the Enter button.");

            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='response-container']/section[2]")));
            logger.info("Response container is visible.");
            test.info("Response container is visible.");

            captureScreenshot(test);
            test.pass("Prompt workflow test completed successfully.");
        } catch (Exception e) {
            logger.error("Error during prompt workflow test: ", e);
            test.fail("Prompt workflow test failed: " + e.getMessage());
        }
    }

    private void testCopyButton(ExtentTest test) {
        try {
            logger.info("Testing Copy Button functionality...");
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
            JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;

            WebElement copyButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(
                    "//section[@class='flex-1 overflow-y-auto custom-scrollbar']//button[2]//*[name()='svg']")));
            jsExecutor.executeScript("arguments[0].scrollIntoView(true);", copyButton);
            copyButton.click();
            logger.info("Copy button clicked successfully.");
            test.info("Copy button clicked successfully.");

            captureScreenshot(test);
            test.pass("Copy Button test completed successfully.");
        } catch (Exception e) {
            logger.error("Error during Copy Button test: ", e);
            test.fail("Copy Button test failed: " + e.getMessage());
        }
    }

    private void testReadAloud(ExtentTest test) {
        try {
            logger.info("Testing Read Aloud functionality...");
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
            JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;

            WebElement readAloudButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(
                    "/html/body/div[1]/div/div[2]/div/section[2]/section/div[1]/div[3]/button[1]/*[name()='svg']")));
            jsExecutor.executeScript("arguments[0].scrollIntoView(true);", readAloudButton);
            readAloudButton.click();
            logger.info("Read Aloud clicked successfully.");
            test.info("Read Aloud button clicked successfully.");

            Thread.sleep(5000); // Simulate reading aloud

            WebElement stopReadAloudButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(
                    "//*[@id='response-container']/section[2]/section/div[1]/div[3]/button[1]/span")));
            stopReadAloudButton.click();
            logger.info("Read Aloud stopped successfully.");
            test.info("Read Aloud stopped successfully.");

            captureScreenshot(test);
            test.pass("Read Aloud test completed successfully.");
        } catch (Exception e) {
            logger.error("Error during Read Aloud test: ", e);
            test.fail("Read Aloud test failed: " + e.getMessage());
        }
    }

    private void testRegeneratePrompt(ExtentTest test) {
        try {
            logger.info("Testing Regenerate Prompt functionality...");
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

            WebElement regeneratePromptButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(
                    "//button[@class='text-gray-400 mr-3']//*[name()='svg']")));
            regeneratePromptButton.click();
            logger.info("Regenerate prompt button clicked successfully.");
            test.info("Regenerate prompt button clicked successfully.");

            captureScreenshot(test);
            test.pass("Regenerate Prompt test completed successfully.");
        } catch (Exception e) {
            logger.error("Error during Regenerate Prompt test: ", e);
            test.fail("Regenerate Prompt test failed: " + e.getMessage());
        }
    }

    private void testCloseSidebar(ExtentTest test) {
        try {
            logger.info("Testing Close Sidebar functionality...");
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

            WebElement closeSidebarButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(
                    "//*[@id='response-container']/section[1]/nav/div[1]/div[1]/button[1]")));
            closeSidebarButton.click();
            logger.info("Close Sidebar button clicked successfully.");
            test.info("Close Sidebar button clicked successfully.");

            captureScreenshot(test);
            test.pass("Close Sidebar test completed successfully.");
        } catch (Exception e) {
            logger.error("Error during Close Sidebar test: ", e);
            test.fail("Close Sidebar test failed: " + e.getMessage());
        }
    }

    private void captureScreenshot(ExtentTest test) {
        try {
            logger.info("Capturing screenshot...");
            File directory = new File(screenshotDir);
            if (!directory.exists()) {
                directory.mkdirs();
            }
            String screenshotPath = screenshotDir + "\\screenshot.png";
            File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(srcFile, new File(screenshotPath));
            test.addScreenCaptureFromPath(screenshotPath, "Test Screenshot");
            logger.info("Screenshot captured successfully.");
        } catch (Exception e) {
            logger.error("Failed to capture screenshot: ", e);
            test.warning("Failed to capture screenshot: " + e.getMessage());
        }
    }

    private void sendEmailWithAttachment(String filePath) {
        String from = "shingne.yogesh@gmail.com";
        String password = "wikf xvph jumm zvzm";
        String to = "yogeshshingne7@gmail.com";
        String host = "smtp.gmail.com";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject("Test Report");
            message.setText("Hi,\n\nPlease find the attached test report.\n\nRegards,\nAutomation Team");

            MimeBodyPart messageBodyPart = new MimeBodyPart();
            Multipart multipart = new MimeMultipart();

            MimeBodyPart attachmentBodyPart = new MimeBodyPart();
            attachmentBodyPart.attachFile(new File(filePath));
            multipart.addBodyPart(attachmentBodyPart);

            message.setContent(multipart);
            Transport.send(message);
            logger.info("Email sent successfully with the report.");
        } catch (Exception e) {
            logger.error("Failed to send email: ", e);
        }
    }
}