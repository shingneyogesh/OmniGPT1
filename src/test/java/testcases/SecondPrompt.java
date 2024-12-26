package testcases;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import base.BaseTest;

public class SecondPrompt extends BaseTest {

    // DataProvider for supplying test data
    @DataProvider(name = "essayTopics")
    public Object[][] getEssayTopics() {
        return new Object[][] {
            
           {"write Smoke vs Sanity testing in table format"},
           
        };
    }

    // Test method with DataProvider
    @Test(dataProvider = "essayTopics")
    public void testEnterPrompt(String essayTopic) {
        try {
            // Wait before entering text into the prompt
            Thread.sleep(2000); // Wait for 2 seconds

            // Locate the search input field and enter text
            WebElement searchPrompt = driver.findElement(By.xpath("//*[@id='prompt']"));
            searchPrompt.sendKeys(essayTopic);

            // Wait after entering text into the prompt
            Thread.sleep(2000); // Wait for 2 seconds

            // Locate the enter button using the specified XPath
            WebElement enterButton = driver.findElement(By.xpath("//*[@id='response-container']/section[3]/div/div/button"));

            // Click the enter button
            enterButton.click();

            // Wait for response to load (adjust timing as needed)
            Thread.sleep(30000);

            // Optional: Add verification steps to check for successful form submission

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
