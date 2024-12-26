package testcases;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import base.BaseTest;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;

public class OmniGPTlog extends BaseTest {
    private static final Logger logger = LogManager.getLogger(OmniGPTlog.class);

    @Test
    public void testEnterPrompt() throws IOException {
        // Initialize ExtentReports with ExtentSparkReporter
        ExtentSparkReporter sparkReporter = new ExtentSparkReporter("ExtentReport.html");
        ExtentReports extent = new ExtentReports();
        extent.attachReporter(sparkReporter);
        ExtentTest test = extent.createTest("testEnterPrompt", "Validate the prompt functionality");

        try {
            logger.info("Test 'testEnterPrompt' started.");

            // Log initial test step in ExtentReports
            test.info("Test steps started.");

            // Locate the search input field and enter text
            WebElement searchPrompt = driver.findElement(By.xpath("//*[@id='prompt']"));
            searchPrompt.sendKeys("write on USA ");
            test.info("Entered prompt: 'write on USA '");
            logger.info("Entered prompt text: 'write on USA '");

            // Wait after entering text into the prompt
            Thread.sleep(3000); // Wait for 3 seconds
            test.info("Waited for 3 seconds after entering prompt.");
            logger.info("Waited for 3 seconds after entering prompt.");

            // Locate the enter button using the specified XPath
            WebElement enterButton = driver.findElement(By.xpath("//*[@id='response-container']/section[3]/div/div/button"));
            test.info("Located the Enter button.");

            // Click the enter button
            enterButton.click();
            test.info("Clicked the Enter button.");
            logger.info("Clicked the Enter button.");

            Thread.sleep(20000); // Wait for 20 seconds to get response

            // Capture a screenshot
            File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            String screenshotPath = "screenshot/screenshot.png";
            FileUtils.copyFile(src, new File(screenshotPath));
            test.addScreenCaptureFromPath(screenshotPath, "Test Screenshot");
            logger.info("Screenshot captured and saved to " + screenshotPath);

            // Mark test as passed
            test.pass("Test completed successfully!");
            logger.info("Test 'testEnterPrompt' completed successfully.");
        } catch (Exception e) {
            // If any exception occurs, mark the test as failed
            test.fail("Test failed due to an exception: " + e.getMessage());
            logger.error("Test execution failed: ", e);
        } finally {
            // Write the report and flush the extent report
            extent.flush();
            logger.info("Extent report flushed and saved.");
        }
    }
}
