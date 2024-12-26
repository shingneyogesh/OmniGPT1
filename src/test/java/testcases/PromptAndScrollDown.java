package testcases;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import base.BaseTest;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
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

public class PromptAndScrollDown extends BaseTest {

    private ExtentReports extent;
    private String reportPath = System.getProperty("user.dir") + "\\ExtentReport.html";
    private String screenshotDir = System.getProperty("user.dir") + "\\screenshots";

    public PromptAndScrollDown() {
        // Initialize ExtentReports
        ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);
        extent = new ExtentReports();
        extent.attachReporter(sparkReporter);
    }

    @Test
    public void testEnterPrompt() {
        ExtentTest test = extent.createTest("testEnterPrompt", "Validate the prompt functionality");
        try {
            // Enter text in the prompt
            WebElement searchPrompt = driver.findElement(By.xpath("//*[@id='prompt']"));
            searchPrompt.sendKeys("write on Indian history in 150 words ");
            test.info("Entered text into the prompt.");
            
            Thread.sleep(5000);

            WebElement enterButton = driver.findElement(By.xpath("//*[@id='response-container']/section[3]/div/div/button"));
            enterButton.click();
            test.info("Clicked the Enter button.");

            Thread.sleep(20000); // Wait for response to load

            // Take Screenshot
            String screenshotPath = takeScreenshot("PromptTest");
            test.addScreenCaptureFromPath(screenshotPath, "Test Screenshot");
            test.pass("Test completed successfully!");
        } catch (Exception e) {
            test.fail("Test failed due to an exception: " + e.getMessage());
        }
    }

    @Test
    public void testScrollDownAndGenerateReport() {
        ExtentTest test = extent.createTest("testScrollDownAndGenerateReport", "Validate scrolling and copying functionality with reporting");
        try {
            // Enter text in the prompt
            WebElement searchPrompt = driver.findElement(By.xpath("//*[@id='prompt']"));
            searchPrompt.sendKeys("write essay on India");
            test.info("Entered text into the search prompt.");

            Thread.sleep(5000);

            WebElement enterButton = driver.findElement(By.xpath("//*[@id='response-container']/section[3]/div/div/button"));
            enterButton.click();
            test.info("Clicked the Enter button.");

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='response-container']/section[2]")));
            test.info("Response container is visible.");

            Thread.sleep(30000); // Wait for content to load

            // Scroll to the Copy button
            By copyButtonLocator = By.xpath("//*[@id='response-container']/section[2]/section/div[1]/div[3]/button[2]/span");
            WebElement copyButton = driver.findElement(copyButtonLocator);
            JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
            jsExecutor.executeScript("arguments[0].scrollIntoView(true);", copyButton);
            test.info("Scrolled to the Copy button.");

            // Take Screenshot
            String screenshotPath = takeScreenshot("ScrollTest");
            test.addScreenCaptureFromPath(screenshotPath, "Test Screenshot");
            test.pass("Test completed successfully!");
        } catch (Exception e) {
            test.fail("Test failed due to an exception: " + e.getMessage());
        }
    }

    private String takeScreenshot(String fileName) throws IOException {
        File directory = new File(screenshotDir);
        if (!directory.exists()) {
            directory.mkdirs(); // Create directory if it doesn't exist
        }
        String screenshotPath = screenshotDir + "\\" + fileName + ".png";
        File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(srcFile, new File(screenshotPath));
        return screenshotPath;
    }

    @AfterClass
    public void tearDown() {
        extent.flush();
        sendEmailWithAttachment(reportPath);
    }

    private void sendEmailWithAttachment(String filePath) {
        String from = "shingne.yogesh@gmail.com"; // Replace with your email
        String password = "wikf xvph jumm zvzm"; // Replace with your email's app-specific password
        String to = "yogeshshinge7@gmail.com"; // Replace with recipient's email
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
