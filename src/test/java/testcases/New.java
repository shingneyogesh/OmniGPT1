package testcases;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import base.BaseTest;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import javax.mail.*;
import javax.mail.internet.*;
import java.io.File;
import java.time.Duration;
import java.util.Properties;

public class New  extends BaseTest {

    private static final Logger logger = LogManager.getLogger(HistorySection.class);
    private ExtentReports extent;
    private String reportPath = System.getProperty("user.dir") + "\\ExtentReport.html";
    private String screenshotDir = System.getProperty("user.dir") + "\\screenshots";

    @Test
    public void executeEditPromptWorkflow() {
        setupReport();
        logger.info("New Chat Started.");

        ExtentTest test = extent.createTest("Click On New Chat  ", "New Chat with reporting");

        try {
            performEditPrompt(test);
            captureScreenshot(test, "Click On New Chat  Sectiont");
            test.pass("Click On New Chat Section test passed successfully.");
        } catch (Exception e) {
            logger.error("Error during Click On History Section: ", e);
            test.fail("Click On History Section test failed: " + e.getMessage());
        } finally {
            extent.flush();
            closeWebDriver();
            sendEmailWithAttachment(reportPath);
        }
    }

    private void setupReport() {
        logger.info("Setting up Extent report...");
        ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);
        extent = new ExtentReports();
        extent.attachReporter(sparkReporter);
    }

    private void performEditPrompt(ExtentTest test) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

        try {
        	
        	Thread.sleep(5000);
            // Start by entering the prompt
            WebElement promptField = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='prompt']")));
            promptField.sendKeys("write on Indian history");
            logger.info("Entered the prompt: 'write on Indian history'");
            test.info("Entered the prompt 'write on Indian history'.");

            // Click the Enter button to start the prompt generation
            WebElement enterButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='response-container']/section[3]/div/div/button")));
            enterButton.click();
            logger.info("Clicked the Enter button.");
            test.info("Clicked the Enter button.");
        	Thread.sleep(25000);


            // Wait for the response to be generated
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='response-container']/section[2]")));
            logger.info("Response container is visible.");
            test.info("Response container is visible.");

            // Click on the stop button to stop the prompt
            WebElement stopButton = driver.findElement(By.xpath("//*[@id='response-container']/section[3]/div/div/button/*[name()='svg']"));
            stopButton.click();
            

            logger.info("Clicked the Stop button.");
            test.info("Clicked the Stop button.");

            // Wait before editing the prompt
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='response-container']/section[3]/div/div/button/*[name()='svg']")));

            Thread.sleep(5000);


            // Now click on the close sidebar button
            WebElement closeSidebarButton = driver.findElement(By.xpath("//*[@id='response-container']/section[1]/nav/div[1]/div[1]/button[1]"));
            closeSidebarButton.click();
            logger.info("Clicked the  close sidebar button button.");
            test.info("Clicked the  close sidebar  button.");

            Thread.sleep(5000);

            // Click on New Chat button 
            WebElement chatButton = driver.findElement(By.xpath("//button[@class='relative p-2 rounded-md hover:bg-gray-200 dark:hover:bg-gray-200 group']//*[name()='svg']"));
            chatButton.click();  
            logger.info("Clicked on New Chat buttont.");
            test.info(" Click on New Chat buttont.");
            Thread.sleep(10000);
                        

            WebElement newPromptInput = driver.findElement(By.xpath("//*[@id='prompt']"));
            newPromptInput.sendKeys("write ancient world history");
            logger.info("Clicked the  New Propmt  button button.");
            test.info("Clicked the New Prompt button.");


            Thread.sleep(3000);  

            WebElement newEnterButton = driver.findElement(By.xpath("//*[@id='response-container']/section[3]/div/div/button"));
            newEnterButton.click();
            
            logger.info("Clicked the  Enter   button button.");
            test.info("Clicked the Enter button.");


            Thread.sleep(10000);


            

        } catch (Exception e) {
            logger.error("Error while performing the Edit Prompt workflow: ", e);
            test.fail("Error in Edit Prompt workflow: " + e.getMessage());
        }
    }

    private void captureScreenshot(ExtentTest test, String testName) {
        try {
            logger.info("Capturing screenshot for test: " + testName);
            File directory = new File(screenshotDir);
            if (!directory.exists()) {
                directory.mkdirs();
            }
            String screenshotPath = screenshotDir + "\\" + testName + "_" + System.currentTimeMillis() + ".png";
            File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(srcFile, new File(screenshotPath));
            test.addScreenCaptureFromPath(screenshotPath, "Screenshot for " + testName);
            logger.info("Screenshot captured successfully for test: " + testName);
        } catch (Exception e) {
            logger.error("Failed to capture screenshot for test: " + testName, e);
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
            logger.info("Composing email...");
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject("Automation Test Report");

            // Email body
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText("Hi,\n\nPlease find the attached test report.\n\nThanks,\nAutomation Team");

            // Attachment
            MimeBodyPart attachmentPart = new MimeBodyPart();
            attachmentPart.attachFile(new File(filePath));

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);
            multipart.addBodyPart(attachmentPart);

            message.setContent(multipart);

            // Send email
            Transport.send(message);
            logger.info("Email sent successfully with the report attached.");
        } catch (Exception e) {
            logger.error("Failed to send email: ", e);
        }
    }

    private void closeWebDriver() {
        if (driver != null) {
           // driver.quit();
        }
    }
}