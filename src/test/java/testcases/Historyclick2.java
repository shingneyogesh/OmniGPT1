package testcases;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;
import base.BaseTest;

import java.io.File;
import java.time.Duration;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

public class Historyclick2 extends BaseTest {

    private ExtentReports extent;
    private ExtentTest test;
    private String reportPath = System.getProperty("user.dir") + "\\ExtentReport.html";
    private String screenshotDir = System.getProperty("user.dir") + "\\screenshots";

    @Test
    public void combinedWorkflowTest() {
        setupReport();

        test = extent.createTest("Prompt Workflow Test", "Execute prompt workflow with reporting");
        executePromptWorkflow();

        extent.flush();
        sendEmailWithAttachment(reportPath);
    }

    private void setupReport() {
        ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);
        sparkReporter.config().setReportName("Automation Test Report");
        sparkReporter.config().setDocumentTitle("Test Results");
        extent = new ExtentReports();
        extent.attachReporter(sparkReporter);
    }

    private void executePromptWorkflow() {
        try {
            driver.manage().window().maximize();
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

            test.info("Starting the workflow test.");

            WebElement promptField = wait.until(ExpectedConditions.elementToBeClickable(By.id("prompt")));
            promptField.sendKeys("write essay on India");
            test.info("Entered the prompt 'write essay on India'.");

            WebElement enterButton = driver.findElement(By.xpath("//*[@id='response-container']/section[3]/div/div/button"));
            enterButton.click();
            test.info("Clicked the enter button.");
            Thread.sleep(15000);

            WebElement stopButton = driver.findElement(By.xpath("//*[@id='response-container']/section[3]/div/div/button/*[name()='svg']"));
            stopButton.click();
            test.info("Clicked the stop button.");
            Thread.sleep(2000);

            WebElement shareButton = driver.findElement(By.xpath("/html/body/div/div/div[2]/div/section/nav/div[2]/div[2]/span"));
            shareButton.click();
            test.info("Clicked the share button.");
            Thread.sleep(3000);

            WebElement createLinkButton = driver.findElement(By.xpath("//*[@id='response-container']/div/div/div[2]/button"));
            createLinkButton.click();
            test.info("Clicked the create link button.");
            Thread.sleep(2000);

            WebElement closeButton = driver.findElement(By.xpath("/html/body/div/div/div[2]/div/div/div/div/button/*[name()='svg']"));
            closeButton.click();
            test.info("Closed the share window.");
            Thread.sleep(2000);

            WebElement editPromptButton = driver.findElement(By.xpath("/html/body/div/div/div[2]/div/section[2]/section/div/div/*[name()='svg']"));
            editPromptButton.click();
            test.info("Clicked the edit prompt button.");
            Thread.sleep(2000);

            WebElement promptTextArea = driver.findElement(By.xpath("//*[@id='response-container']/section[2]/section/div/div/div/div/textarea"));
            promptTextArea.clear();
            promptTextArea.sendKeys("give me information on USA");
            test.info("Updated the prompt.");
            Thread.sleep(2000);

            WebElement sendButton = driver.findElement(By.xpath("//*[@id='response-container']/section[2]/section/div/div/div/div/div/button[2]"));
            sendButton.click();
            test.info("Clicked the send button.");
            Thread.sleep(20000);

            WebElement closeSidebarButton = driver.findElement(By.xpath("//*[@id='response-container']/section/nav/div/div/button"));
            closeSidebarButton.click();
            test.info("Closed the sidebar.");
            Thread.sleep(10000);

            WebElement historySection = driver.findElement(By.xpath("//*[@id='root']/div/div/div/div/section/section/div[2]/div[1]"));
            historySection.click();
            test.info("Clicked on the history section.");

            test.pass("Prompt Workflow executed successfully.");
        } catch (Exception e) {
            test.fail("Workflow test failed due to: " + e.getMessage());
            captureScreenshot(test);
        }
    }

    private void captureScreenshot(ExtentTest test) {
        try {
            File directory = new File(screenshotDir);
            if (!directory.exists()) {
                directory.mkdirs();
            }
            String screenshotPath = screenshotDir + "\\screenshot.png";
            File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(srcFile, new File(screenshotPath));
            test.addScreenCaptureFromPath(screenshotPath, "Test Screenshot");
        } catch (Exception e) {
            test.warning("Failed to capture screenshot: " + e.getMessage());
        }
    }

    private void sendEmailWithAttachment(String filePath) {
        String from = "shingne.yogesh@gmail.com";
        String password = "wikf xvph jumm zvzm";
        String to = "prem@omnineura.ai";
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

            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText("Please find the attached Extent Report for the test execution.");

            MimeBodyPart attachmentPart = new MimeBodyPart();
            attachmentPart.attachFile(new File(filePath));

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

    @AfterClass
    public void teardown() {
        if (driver != null) {
            driver.quit();
        }
        extent.flush();
    }
}
