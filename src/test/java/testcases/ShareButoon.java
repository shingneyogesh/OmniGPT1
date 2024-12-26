package testcases;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import base.BaseTest;

import java.time.Duration;

public class ShareButoon extends BaseTest {
    public static void main(String[] args) throws InterruptedException {

    	WebDriver driver = new ChromeDriver();
        
        try {
            // Open the specified URL
            
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
            
            // Wait for 5 seconds
            Thread.sleep(15000);
            
            // Locate and click the stop button with the updated XPath
            WebElement stopButton = driver.findElement(By.xpath("//*[@id='response-container']/section[3]/div/div/button/*[name()='svg']"));
            stopButton.click();
            
            // Wait for a short time to ensure stop action is processed
            Thread.sleep(5000);
            
            WebElement shareButton = driver.findElement(By.xpath("/html[1]/body[1]/div[1]/div[1]/div[2]/div[1]/section[1]/nav[1]/div[2]/div[2]/span[1]"));
            shareButton.click();


            
            
        } finally {
            // Close the browser after a short delay to observe results
            Thread.sleep(2000);
           // driver.quit();
        }
    }
}
