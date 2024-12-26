package testcases;

import com.aventstack.extentreports.ExtentTest;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;
import base.BaseTest;

import java.io.File;
import java.time.Duration;

public class AllScript extends BaseTest {

    private static final Logger logger = LogManager.getLogger(AllScript.class);
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
    public void executeAllTests() {
        ExtentTest test = extent.createTest("Execute All Tests", "Combined execution of all test scenarios");
        try {
            logger.info("Starting all tests...");
            test.info("Starting all tests...");

            executePromptWorkflow(test);
            testCopyButton(test);
            testReadAloud(test);
            testRegeneratePrompt(test);
            testCloseSidebar(test);

            test.pass("All tests executed successfully.");
        } catch (Exception e) {
            test.fail("Error during test execution: " + e.getMessage());
            logger.error("Error during test execution: ", e);
        }
    }

    private void executePromptWorkflow(ExtentTest test) {
        try {
            logger.info("Executing prompt workflow...");
            test.info("Executing prompt workflow...");
            captureScreenshot("StartPromptWorkflow");

            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
            
            Thread.sleep(5000);
            
            WebElement promptField = wait.until(ExpectedConditions.elementToBeClickable(By.id("prompt")));
            promptField.sendKeys("write essay on India");
            test.info("Entered prompt: 'write essay on India'");
            captureScreenshot("EnterPrompt");
            
            Thread.sleep(5000);


            WebElement enterButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='response-container']/section[3]/div/div/button")));
            enterButton.click();
            test.info("Clicked the Enter button");
            captureScreenshot("ClickEnterButton");
            
            Thread.sleep(20000);


            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='response-container']/section[2]")));
            test.info("Response container is visible");
            captureScreenshot("ResponseVisible");

            test.pass("Prompt workflow completed successfully.");
        } catch (Exception e) {
            test.fail("Prompt workflow failed: " + e.getMessage());
            logger.error("Prompt workflow failed: ", e);
        }
    }

    private void testCopyButton(ExtentTest test) {
        try {
            logger.info("Testing Copy Button...");
            test.info("Testing Copy Button...");
            captureScreenshot(test, "StartCopyButtonTest");
            
            Thread.sleep(5000);


            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
            WebElement copyButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(
                    "//section[@class='flex-1 overflow-y-auto custom-scrollbar']//button[2]//*[name()='svg']")));
            copyButton.click();
            test.info("Clicked the Copy Button");
            captureScreenshot(test, "CopyButtonClicked");
            
            Thread.sleep(5000);


            test.pass("Copy Button test completed successfully.");
        } catch (Exception e) {
            test.fail("Copy Button test failed: " + e.getMessage());
            logger.error("Copy Button test failed: ", e);
        }
    }

    private void testReadAloud(ExtentTest test) {
        try {
            logger.info("Testing Read Aloud functionality...");
            test.info("Testing Read Aloud functionality...");
            captureScreenshot(test, "StartReadAloudTest");

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
            WebElement readAloudButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(
                    "/html/body/div[1]/div/div[2]/div/section[2]/section/div[1]/div[3]/button[1]/*[name()='svg']")));
            readAloudButton.click();
            test.info("Clicked the Read Aloud button");
            captureScreenshot(test, "ReadAloudClicked");

            Thread.sleep(10000); // Simulate reading
            WebElement stopButton = wait.until(ExpectedConditions.elementToBeClickable((By.xpath(
                    "//*[@id='response-container']/section[2]/section/div[1]/div[3]/button[1]/span"))));
            stopButton.click();
            test.info("Stopped Read Aloud");
            captureScreenshot(test, "ReadAloudStopped");

            Thread.sleep(5000);

            test.pass("Read Aloud test completed successfully.");
        } catch (Exception e) {
            test.fail("Read Aloud test failed: " + e.getMessage());
            logger.error("Read Aloud test failed: ", e);
        }
    }

    private void testRegeneratePrompt(ExtentTest test) {
        try {
            logger.info("Testing Regenerate Prompt...");
            test.info("Testing Regenerate Prompt...");
            captureScreenshot(test, "StartRegeneratePromptTest");

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
            WebElement regenerateButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(
                    "//button[@class='text-gray-400 mr-3']//*[name()='svg']")));
            regenerateButton.click();
            test.info("Clicked the Regenerate Prompt button");
            captureScreenshot(test, "RegeneratePromptClicked");
            
            Thread.sleep(25000);


            test.pass("Regenerate Prompt test completed successfully.");
        } catch (Exception e) {
            test.fail("Regenerate Prompt test failed: " + e.getMessage());
            logger.error("Regenerate Prompt test failed: ", e);
        }
    }

    private void testCloseSidebar(ExtentTest test) {
        try {
            logger.info("Testing Close Sidebar...");
            test.info("Testing Close Sidebar...");
            captureScreenshot(test, "StartCloseSidebarTest");

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
            WebElement closeSidebarButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(
                    "//*[@id='response-container']/section[1]/nav/div[1]/div[1]/button[1]")));
            closeSidebarButton.click();
            test.info("Clicked the Close Sidebar button");
            captureScreenshot(test, "CloseSidebarClicked");
            
            Thread.sleep(5000);


            test.pass("Close Sidebar test completed successfully.");
        } catch (Exception e) {
            test.fail("Close Sidebar test failed: " + e.getMessage());
            logger.error("Close Sidebar test failed: ", e);
        }
    }
}
