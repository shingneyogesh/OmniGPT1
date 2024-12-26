package testcases;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import java.time.Duration;

public class EditPrompt {
    public static void main(String[] args) throws InterruptedException {

        // Initialize WebDriver
        WebDriver driver = new ChromeDriver();
        
        try {
            // Open the specified URL
            driver.get("http://35.164.174.72/");
            
            // Maximize the browser window
            driver.manage().window().maximize();
            
            // Set an implicit wait
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
            
            Thread.sleep(10000);

            // Locate the prompt field and enter text
            WebElement promptField = driver.findElement(By.xpath("//*[@id='prompt']"));
            promptField.sendKeys("write on Indian history");
            
            // Click the enter button to start generating the prompt
            WebElement enterButton = driver.findElement(By.xpath("//*[@id='response-container']/section[3]/div/div/button"));
            enterButton.click();
            
            // Wait for 15 seconds
            Thread.sleep(15000);
            
            // Locate and click the stop button with the updated XPath
            WebElement stopButton = driver.findElement(By.xpath("//*[@id='response-container']/section[3]/div/div/button/*[name()='svg']"));
            stopButton.click();
            
            // Wait for a short time to ensure stop action is processed
            Thread.sleep(2000);

            // Click the enter button again to restart prompt generation
            // enterButton.click();

            // Wait briefly before locating the share button
            Thread.sleep(2000);

            // Locate the share button using the provided XPath and click it
            WebElement shareButton = driver.findElement(By.xpath("/html[1]/body[1]/div[1]/div[1]/div[2]/div[1]/section[1]/nav[1]/div[2]/div[2]/span[1]"));
            shareButton.click();

            // Wait for the window to open (adjust as needed)
            Thread.sleep(3000); // Increase or decrease the wait depending on the response time

            // Now click the "create link" button in the new window or modal
            WebElement createLinkButton = driver.findElement(By.xpath("//*[@id='response-container']/div/div/div[2]/button"));
            createLinkButton.click();

            // Wait a brief moment before closing the window
            Thread.sleep(2000);

            // Locate and click the close button to close the "create link" window
            WebElement closeButton = driver.findElement(By.xpath("/html[1]/body[1]/div[1]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/button[1]/*[name()='svg'][1]"));
            closeButton.click();

            // Wait briefly before proceeding to edit the prompt
            Thread.sleep(2000);

            // Locate the "edit prompt" button and click it
            WebElement editPromptButton = driver.findElement(By.xpath("/html[1]/body[1]/div[1]/div[1]/div[2]/div[1]/section[2]/section[1]/div[1]/div[1]/*[name()='svg'][1]"));
            editPromptButton.click();

            // Wait for a moment to ensure the prompt is editable
            Thread.sleep(2000);

            // Optionally, you can now modify the prompt if necessary
            // For example, changing the text in the prompt field
         //   WebElement newPromptField = driver.findElement(By.xpath("//*[@id='prompt']"));
        //    newPromptField.clear();
          //.sendKeys("write on modern Indian history");

            // Wait for the prompt to be updated before replacing it with the new prompt
            Thread.sleep(2000);

            // Locate the prompt textarea and replace the previous prompt with the new one
            WebElement promptTextArea = driver.findElement(By.xpath("//*[@id='response-container']/section[2]/section/div[1]/div[1]/div/div/textarea"));
            promptTextArea.clear();
            promptTextArea.sendKeys("give me information on USA");

            // Wait to observe the result
            Thread.sleep(2000);

            // Locate the "Send" button using the given XPath and click it
            WebElement sendButton = driver.findElement(By.xpath("//*[@id='response-container']/section[2]/section/div[1]/div[1]/div/div/div/button[2]"));
            sendButton.click();

            // Wait briefly to observe the result
            Thread.sleep(2000);

        } finally {
            // Close the browser after a short delay to observe results
            Thread.sleep(2000);
            // driver.quit();
        }
    }
}
