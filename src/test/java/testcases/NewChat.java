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

public class NewChat extends BaseTest {

    private static final Logger logger = LogManager.getLogger(NewChat.class);
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
    public void executeEditPromptWorkflow() {
        ExtentTest test = extent.createTest("New Chat Workflow", "Test to validate New Chat functionality with screenshots.");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

        try {
            logger.info("Starting New Chat Workflow Test...");

            // Input initial prompt
            WebElement promptField = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='prompt']")));
            promptField.sendKeys("write on Indian history");
            test.info("Entered prompt: 'write on Indian history'");

            captureScreenshot(test, "EnterPrompt");

            Thread.sleep(5000);
            // Click the Enter button
            WebElement enterButton = driver.findElement(By.xpath("//*[@id='response-container']/section[3]/div/div/button"));
            enterButton.click();
            test.info("Clicked the Enter button");

            captureScreenshot(test, "ClickEnterButton");

            Thread.sleep(25000);
            
         // Stop prompt generation
            // Stop prompt generation
            // Click on the stop button to stop the prompt
            WebElement stopButton = driver.findElement(By.xpath("//*[@id='response-container']/section[3]/div/div/button/*[name()='svg']"));
            stopButton.click();
            
            test.info("Clicked the Stop button");
            captureScreenshot(test, "StopPrompt");

            Thread.sleep(5000);

            // Now click on the close sidebar button
         //   WebElement closeSidebarButton = driver.findElement(By.xpath("//*[@id='response-container']/section[1]/nav/div[1]/div[1]/button[1]"));
           // closeSidebarButton.click();
         //   test.info("Clicked the  close sidebar  button.");
          //  captureScreenshot(test, "Click on Close Sidebar");

            Thread.sleep(5000);

            // Wait for response
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='response-container']/section[2]")));
            test.info("Response received");

            captureScreenshot(test, "ResponseReceived");

            Thread.sleep(5000);

            

            // Click on New Chat button
            WebElement newChatButton = driver.findElement(By.xpath("//button[@class='relative p-2 rounded-md hover:bg-gray-200 dark:hover:bg-gray-200 group']//*[name()='svg']"));
            newChatButton.click();
            test.info("Clicked the New Chat button");

            captureScreenshot(test, "NewChatButton");

            Thread.sleep(5000);

            // Input new prompt
            WebElement newPromptField = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='prompt']")));
            newPromptField.sendKeys("write ancient world history");
            test.info("Entered prompt: 'write ancient world history'");

            captureScreenshot(test, "EnterNewPrompt");

            Thread.sleep(5000);

            // Submit the new prompt
            WebElement newEnterButton = driver.findElement(By.xpath("//*[@id='response-container']/section[3]/div/div/button"));
            newEnterButton.click();
            test.info("Clicked the Enter button for new prompt");

            captureScreenshot(test, "SubmitNewPrompt");

            Thread.sleep(25000);

            // Validate response
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='response-container']/section[2]")));
            test.pass("New Chat Workflow executed successfully");

            captureScreenshot(test, "FinalResponse");

        } catch (Exception e) {
            captureScreenshot(test, "NewChatTestFailure");
            test.fail("Test Failed: " + e.getMessage());
            logger.error("Test failed", e);
        }
    }
}
