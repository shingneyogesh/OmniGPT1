package testcases;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;
import base.BaseTest;

import java.io.File;
import java.time.Duration;

public class HIstoryClick extends BaseTest {

    private static final Logger logger = LogManager.getLogger(HIstoryClick.class);
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
    public void combinedWorkflowTest() {
        ExtentTest test = extent.createTest("History Click Workflow Test", "Validates history click functionality with screenshots.");
        executePromptWorkflow(test);
        extent.flush();
    }

    private void executePromptWorkflow(ExtentTest test) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

        try {
            logger.info("Starting History Click Workflow Test...");

            // Input initial prompt
            WebElement promptField = wait.until(ExpectedConditions.elementToBeClickable(By.id("prompt")));
            promptField.sendKeys("write essay on India");
            test.info("Entered the prompt 'write essay on India'.");

            captureScreenshot(test, "EnterPrompt");

            WebElement enterButton = driver.findElement(By.xpath("//*[@id='response-container']/section[3]/div/div/button"));
            enterButton.click();
            test.info("Clicked the enter button.");

            captureScreenshot(test, "ClickEnterButton");

            Thread.sleep(15000);

            // Stop the generation
            WebElement stopButton = driver.findElement(By.xpath("//*[@id='response-container']/section[3]/div/div/button/*[name()='svg']"));
            stopButton.click();
            test.info("Clicked the stop button.");

            captureScreenshot(test, "StopPrompt");

            Thread.sleep(2000);

            // Click the share button
            WebElement shareButton = driver.findElement(By.xpath("/html/body/div/div/div[2]/div/section/nav/div[2]/div[2]/span"));
            shareButton.click();
            test.info("Clicked the share button.");

            captureScreenshot(test, "ClickShareButton");

            Thread.sleep(3000);

            // Create a link
            WebElement createLinkButton = driver.findElement(By.xpath("//*[@id='response-container']/div/div/div[2]/button"));
            createLinkButton.click();
            test.info("Clicked the create link button.");

            captureScreenshot(test, "CreateLinkButton");

            Thread.sleep(2000);

            // Close the link window
            WebElement closeButton = driver.findElement(By.xpath("/html/body/div/div/div[2]/div/div/div/div/button/*[name()='svg']"));
            closeButton.click();
            test.info("Closed the share window.");

            captureScreenshot(test, "CloseShareWindow");

            Thread.sleep(2000);

            // Edit the prompt
            WebElement editPromptButton = driver.findElement(By.xpath("/html/body/div/div/div[2]/div/section[2]/section/div/div/*[name()='svg']"));
            editPromptButton.click();
            test.info("Clicked the edit prompt button.");

            captureScreenshot(test, "ClickEditPromptButton");

            Thread.sleep(2000);

            WebElement promptTextArea = driver.findElement(By.xpath("//*[@id='response-container']/section[2]/section/div/div/div/div/textarea"));
            promptTextArea.clear();
            promptTextArea.sendKeys("give me information on USA");
            test.info("Updated the prompt.");

            captureScreenshot(test, "UpdatePrompt");

            Thread.sleep(2000);

            WebElement sendButton = driver.findElement(By.xpath("//*[@id='response-container']/section[2]/section/div/div/div/div/div/button[2]"));
            sendButton.click();
            test.info("Clicked the send button.");

            captureScreenshot(test, "SubmitUpdatedPrompt");

            Thread.sleep(20000);

            // Close the sidebar
            WebElement closeSidebarButton = driver.findElement(By.xpath("//*[@id='response-container']/section/nav/div/div/button"));
            closeSidebarButton.click();
            test.info("Closed the sidebar.");

            captureScreenshot(test, "CloseSidebar");

            Thread.sleep(10000);

            // Click the history section
            WebElement historySection = driver.findElement(By.xpath("//*[@id='root']/div/div/div/div/section/section/div[2]/div[1]"));
            historySection.click();
            test.info("Clicked on the history section.");

            captureScreenshot(test, "ClickHistorySection");

            test.pass("History Click Workflow Test completed successfully.");

        } catch (Exception e) {
            captureScreenshot(test, "HistoryClickTestFailure");
            test.fail("Test failed due to exception: " + e.getMessage());
            logger.error("Test failed", e);
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
