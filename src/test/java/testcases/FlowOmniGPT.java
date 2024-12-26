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

public class FlowOmniGPT extends BaseTest {

    private ExtentReports extent;
    private String reportPath = System.getProperty("user.dir") + "\\ExtentReport.html";
    private String screenshotDir = System.getProperty("user.dir") + "\\screenshots";

    @Test
    public void combinedWorkflowTest() {
        setupReport();

        ExtentTest test1 = extent.createTest("Prompt Workflow Test", "Execute prompt workflow with reporting");
        executePromptWorkflow(test1);

        ExtentTest test2 = extent.createTest("Scroll Down and Copy Button Test", "Validate scrolling and copying functionality");
        executeScrollDownWorkflow(test2);

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
            JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
 
            Thread.sleep(5000);
            // Prompt Workflow
            WebElement promptField = wait.until(ExpectedConditions.elementToBeClickable(By.id("prompt")));
            promptField.sendKeys("write essay on India");
            test.info("Entered the prompt 'write essay on India'.");

            WebElement enterButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//*[@id='response-container']/section[3]/div/div/button")));
            enterButton.click();
            test.info("Clicked the Enter button.");

            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='response-container']/section[2]")));
            test.info("Response container is visible.");

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

            captureScreenshot(test);
            test.pass("Prompt workflow test completed successfully.");
        } catch (Exception e) {
            test.fail("Prompt workflow test failed: " + e.getMessage());
        }
    }

    private void executeScrollDownWorkflow(ExtentTest test) {
        try {
            driver.manage().window().maximize();
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
            JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
          
            Thread.sleep(5000);

            WebElement searchPrompt = driver.findElement(By.xpath("//*[@id='prompt']"));
            searchPrompt.sendKeys("write essay on India");
            test.info("Entered text into the search prompt.");

            WebElement enterButton = driver.findElement(By.xpath("//*[@id='response-container']/section[3]/div/div/button"));
            enterButton.click();
            test.info("Clicked the enter button.");

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='response-container']/section[2]")));
            test.info("Response container is visible.");

            By copyButtonLocator = By.xpath("//*[@id='response-container']/section[2]/section/div[1]/div[3]/button[2]/span");
            WebElement copyButton = driver.findElement(copyButtonLocator);
            jsExecutor.executeScript("arguments[0].scrollIntoView(true);", copyButton);
            
         // New prompt entry
            WebElement newPromptField = driver.findElement(By.xpath("//*[@id='prompt']"));
            newPromptField.clear();
            newPromptField.sendKeys("Population");
            test.info("Entered new prompt 'Population'");

            WebElement newEnterButton = driver.findElement(By.xpath("//*[@id='response-container']/section[3]/div/div/button"));
            newEnterButton.click();
            test.info("New prompt 'Population' submitted successfully.");

            // Close Sidebar
            WebElement closeSidebarButton = driver.findElement(By.xpath("//*[@id='response-container']/section[1]/nav/div[1]/div[1]/button[1]"));
            closeSidebarButton.click();
            test.info("Closed the sidebar");

            // Search Term Entry
            WebElement searchBox = driver.findElement(By.xpath("//*[@id='root']/div/div[1]/div/div/section/section[3]/div[1]/input"));
            searchBox.clear();
            searchBox.sendKeys("Write On India");
            test.info("Entered search term 'Write On India'");

            WebElement shareButton = driver.findElement(By.xpath("/html[1]/body[1]/div[1]/div[1]/div[2]/div[1]/section[1]/nav[1]/div[2]/div[2]/span[1]"));
            shareButton.click();
            test.info("Share Chat '");
            
            captureScreenshot(test);
            test.pass("OmniGPTFlow.");
        } catch (Exception e) {
            test.fail("Scroll down and copy button test failed: " + e.getMessage());
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
        String[] to = { "yogeshshingne7@gmail.com", "shingne.yogesh@gmail.com" }; // Add the email addresses of all 3 recipients
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
            
            // Add all recipients to the message
            for (String recipient : to) {
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
            }
            
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
}
