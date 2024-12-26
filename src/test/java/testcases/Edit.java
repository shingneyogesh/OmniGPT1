package testcases;

import base.BaseTest;
import com.aventstack.extentreports.ExtentTest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import java.io.File;
import java.time.Duration;

import org.apache.commons.io.FileUtils;

public class Edit extends BaseTest {
    private static final Logger logger = LogManager.getLogger(Edit.class);
    private static final String screenshotDir = System.getProperty("user.dir") + "\\screenshots";

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

    @Test
    public void editPromptTest() {
        ExtentTest test = extent.createTest("Edit Prompt Test", "Validates editing functionality of the prompt");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

        try {
            logger.info("Starting Edit Prompt Test...");

            // Input initial prompt
            WebElement promptField = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='prompt']")));
            promptField.sendKeys("write on Indian history");
            test.info("Entered prompt: 'write on Indian history'");

            captureScreenshot(test, "Enter Prompt");

            Thread.sleep(5000);
            // Click the Enter button
            WebElement enterButton = driver.findElement(By.xpath("//*[@id='response-container']/section[3]/div/div/button"));
            enterButton.click();
            test.info("Clicked the Enter button");

            captureScreenshot(test, "ClickEnterButton");

            Thread.sleep(20000);

            // Wait for response
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='response-container']/section[2]")));
            test.info("Response received");

            captureScreenshot(test, "ResponseReceived");

            // Stop prompt generation
            WebElement stopButton = driver.findElement(By.xpath("//*[@id='response-container']/section[3]/div/div/button/*[name()='svg']"));
            stopButton.click();
            test.info("Clicked the Stop button");

            captureScreenshot(test, "StopPrompt");

            Thread.sleep(5000);

            // Edit the prompt
            WebElement editPromptButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html[1]/body[1]/div[1]/div[1]/div[2]/div[1]/section[2]/section[1]/div[1]/div[1]/*[name()='svg'][1]")));
            editPromptButton.click();
            test.info("Clicked the Edit Prompt button");
            Thread.sleep(5000);

            

            WebElement promptTextArea = driver.findElement(By.xpath("//*[@id='response-container']/section[2]/section/div[1]/div[1]/div/div/textarea"));
            promptTextArea.clear();
            promptTextArea.sendKeys("give me information on the USA");
            test.info("Modified the prompt");

            Thread.sleep(5000);

            captureScreenshot(test, "EditPrompt");

            // Submit the modified prompt
            WebElement sendButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='response-container']/section[2]/section/div[1]/div[1]/div/div/div/button[2]")));
            sendButton.click();
            test.info("Submitted the modified prompt");

            Thread.sleep(25000);


            captureScreenshot(test, "SubmitModifiedPrompt");

            // Validate response
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='response-container']/section[2]")));
            test.pass("Edit Prompt Test Passed");

            Thread.sleep(25000);

            captureScreenshot(test, "FinalResponse");

            Thread.sleep(5000);

        } catch (Exception e) {
            captureScreenshot(test, "EditPromptTestFailure");
            test.fail("Test Failed: " + e.getMessage());
            logger.error("Test failed", e);
        }
    }
}
