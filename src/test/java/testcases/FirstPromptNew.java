package testcases;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import base.BaseTest;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

public class FirstPromptNew extends BaseTest {
    @Test
    public void testEnterPrompt() throws IOException {
        // Initialize ExtentReports with ExtentSparkReporter
        ExtentSparkReporter sparkReporter = new ExtentSparkReporter("ExtentReport.html");
        ExtentReports extent = new ExtentReports();
        extent.attachReporter(sparkReporter);
        ExtentTest test = extent.createTest("testEnterPrompt", "Validate the prompt functionality");

        try {
            // Your test logic here
            // For example: Locating and interacting with elements

            // Log a message
            test.info("Test steps executed successfully.");
            
            // Locate the search input field and enter text
            WebElement searchPrompt = driver.findElement(By.xpath("//*[@id='prompt']"));
            searchPrompt.sendKeys("write on Indian history");

            // Wait after entering text into the prompt
            Thread.sleep(3000); // Wait for 2 seconds

            // Locate the enter button using the specified XPath
            WebElement enterButton = driver.findElement(By.xpath("//*[@id='response-container']/section[3]/div/div/button"));

            // Click the enter button
            enterButton.click();
            
            Thread.sleep(30000);

            // Capture a screenshot
            File src = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
            String screenshotPath = "screenshot/screenshot.png";
            FileUtils.copyFile(src, new File(screenshotPath));

            // Attach the screenshot to the report
            test.addScreenCaptureFromPath(screenshotPath, "Test Screenshot");

            test.pass("Test completed successfully!");
        } catch (Exception e) {
            test.fail("Test failed due to an exception: " + e.getMessage());
        } finally {
            // Write the report
            extent.flush();
        }
    }
}
