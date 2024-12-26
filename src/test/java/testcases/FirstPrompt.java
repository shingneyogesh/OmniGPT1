package testcases;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

import base.BaseTest;

import java.io.File;
import java.io.IOException;
import java.time.Duration;

public class FirstPrompt extends BaseTest {

    
 @Test 	
    public void testEnterPrompt() throws IOException {
        // Maximize the browser window



        try {
            // Wait before entering text into the prompt
            Thread.sleep(2000); // Wait for 2 seconds

            // Locate the search input field and enter text
            WebElement searchPrompt = driver.findElement(By.xpath("//*[@id='prompt']"));
            searchPrompt.sendKeys("write on world history");

            // Wait after entering text into the prompt
            Thread.sleep(2000); // Wait for 2 seconds

            // Locate the enter button using the specified XPath
            WebElement enterButton = driver.findElement(By.xpath("//*[@id='response-container']/section[3]/div/div/button"));

            // Click the enter button
            enterButton.click();
            
            Thread.sleep(30000);

            File srcl =((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(srcl, new File("screenshot//screenshot.png"));

            Thread.sleep(5000);
            // Optional: Add verification steps to check for successful form submission

        } catch (InterruptedException e) {
           // e.printStackTrace();
        }
    }
}
