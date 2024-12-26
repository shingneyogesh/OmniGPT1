package testcases;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import base.BaseTest;
import java.io.File;
import java.io.IOException;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

public class EmailandScreenShot extends BaseTest {
    @Test
    public void testEnterPrompt() throws IOException {
        // Initialize ExtentReports with ExtentSparkReporter
        String reportPath = "D:\\Eclipse_Project\\OmiGPT\\ExtentReport.html"; // Updated path
        ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);
        ExtentReports extent = new ExtentReports();
        extent.attachReporter(sparkReporter);
        ExtentTest test = extent.createTest("testEnterPrompt", "Validate the prompt functionality");

        try {
            // Test logic
            test.info("Test steps executed successfully.");
            
            WebElement searchPrompt = driver.findElement(By.xpath("//*[@id='prompt']"));
            searchPrompt.sendKeys("write on Indian history in 150 words ");

            Thread.sleep(3000);

            WebElement enterButton = driver.findElement(By.xpath("//*[@id='response-container']/section[3]/div/div/button"));
            enterButton.click();
            
            Thread.sleep(20000);

            // Take Screenshot
            String screenshotDir = System.getProperty("user.dir") + "\\screenshots";
            File directory = new File(screenshotDir);
            if (!directory.exists()) {
                directory.mkdirs(); // Create the directory if it does not exist
            }
            String screenshotPath = screenshotDir + "\\screenshot.png";
            File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(src, new File(screenshotPath));

            // Add Screenshot to Report
            test.addScreenCaptureFromPath(screenshotPath, "Test Screenshot");
            test.pass("Test completed successfully!");
        } catch (Exception e) {
            test.fail("Test failed due to an exception: " + e.getMessage());
        } finally {
            extent.flush();
            sendEmailWithAttachment(reportPath);
        }
    }

    protected void sendEmailWithAttachment(String filePath) {
        // Email Configuration
        String from = "shingne.yogesh@gmail.com"; // Replace with your email
        String password = "wikf xvph jumm zvzm"; // Replace with your email password or app-specific password
        String to = "yogesh@omnineura.ai"; // Replace with recipient email
        String host = "smtp.gmail.com";

        // Set properties
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", "587");

        // Authenticate
        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, password);
            }
        });

        try {
            // Create a message
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject("Extent Report - Test Execution");

            // Create message body
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText("Please find the attached Extent Report for test execution.");

            // Create multipart
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);

            // Add attachment
            MimeBodyPart attachmentPart = new MimeBodyPart();
            attachmentPart.attachFile(new File(filePath)); // Attach the report file
            multipart.addBodyPart(attachmentPart);

            // Set content
            message.setContent(multipart);

            // Send message
            Transport.send(message);
            System.out.println("Email sent successfully with the attachment.");
        } catch (Exception e) {
            System.err.println("Failed to send email: " + e.getMessage());
        }
    }
}
