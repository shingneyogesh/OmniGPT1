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
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;

public class EnhancedPromptWorkflow extends BaseTest {

    @Test
    public void executePromptWorkflowWithReport() {
        String reportPath = System.getProperty("user.dir") + "\\ExtentReport.html";
        String screenshotDir = System.getProperty("user.dir") + "\\screenshots";
        ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);
        ExtentReports extent = new ExtentReports();
        extent.attachReporter(sparkReporter);
        ExtentTest test = extent.createTest("executePromptWorkflowWithReport", "Complete prompt workflow with reporting and email functionality");

        try {
            // Maximize the browser window
            driver.manage().window().maximize();
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
            JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;

              Thread.sleep(5000);
            // Locate and interact with the prompt field
            WebElement promptField = wait.until(ExpectedConditions.elementToBeClickable(By.id("prompt")));
            promptField.sendKeys("write essay on India");
            test.info("Entered the prompt 'write essay on India'.");

            // Locate and click the Enter button
            WebElement enterButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//*[@id='response-container']/section[3]/div/div/button")));
            enterButton.click();
            test.info("Clicked the Enter button.");

            // Wait for the response container
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='response-container']/section[2]")));
            test.info("Response container is visible.");

            // Scroll to and click the "Copy" button
            WebElement copyButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(
                    "//section[@class='flex-1 overflow-y-auto custom-scrollbar']//button[2]//*[name()='svg']")));
            jsExecutor.executeScript("arguments[0].scrollIntoView(true);", copyButton);
            copyButton.click();
            test.info("Copy button clicked successfully.");

            // Scroll to and handle the "Read Aloud" button
            WebElement readAloudButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(
                    "/html/body/div[1]/div/div[2]/div/section[2]/section/div[1]/div[3]/button[1]/*[name()='svg']")));
            jsExecutor.executeScript("arguments[0].scrollIntoView(true);", readAloudButton);
            readAloudButton.click();
            test.info("Read Aloud button clicked successfully.");

            // Stop the "Read Aloud" after some time
            Thread.sleep(10000);
            WebElement stopReadAloudButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(
                    "//*[@id='response-container']/section[2]/section/div[1]/div[3]/button[1]/span")));
            stopReadAloudButton.click();
            test.info("Read Aloud stopped successfully.");

            // Handle the "Regenerate Prompt" functionality
            WebElement regeneratePromptButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(
                    "//button[@class='text-gray-400 mr-3']//*[name()='svg']")));
            regeneratePromptButton.click();
            test.info("Regenerate prompt button clicked.");

            // Enter a new prompt and submit
            WebElement newPromptField = wait.until(ExpectedConditions.elementToBeClickable(By.id("prompt")));
            newPromptField.clear();
            newPromptField.sendKeys("Population");
            WebElement newEnterButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//*[@id='response-container']/section[3]/div/div/button")));
            newEnterButton.click();
            test.info("New prompt 'Population' submitted successfully.");

            // Capture a screenshot
            File directory = new File(screenshotDir);
            if (!directory.exists()) {
                directory.mkdirs();
            }
            String screenshotPath = screenshotDir + "\\screenshot.png";
            File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(srcFile, new File(screenshotPath));
            test.addScreenCaptureFromPath(screenshotPath, "Test Screenshot");
            test.pass("Test completed successfully.");

        } catch (Exception e) {
            test.fail("Test failed due to an exception: " + e.getMessage());
        } finally {
            extent.flush();
            sendEmailWithAttachment(reportPath);
           // driver.quit();
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
            System.out.println("Email sent successfully with the test report.");
        } catch (Exception e) {
            System.err.println("Failed to send email: " + e.getMessage());
        }
    }
}
