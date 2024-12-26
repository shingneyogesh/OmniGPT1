package utilities;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import base.BaseTest;

import org.testng.ITestResult;
import org.apache.commons.io.FileUtils;

public class Testutilities extends BaseTest {

    public static WebDriver driver; // Shared WebDriver instance
    public static Properties prop = new Properties(); // For Config.properties
    public static Properties loc = new Properties(); // For Locators.properties

    /**
     * Setup method to initialize the WebDriver and load properties files.
     */
    @BeforeMethod
    public void setup() throws IOException {
        if (driver == null) {
            // Load configuration properties
            try (FileReader configReader = new FileReader(System.getProperty("user.dir") + "\\src\\test\\resources\\configfiles\\Config.properties");
                 FileReader locReader = new FileReader(System.getProperty("user.dir") + "\\src\\test\\resources\\configfiles\\Locators.properties")) {
                prop.load(configReader);
                loc.load(locReader);
            }

            // Initialize WebDriver based on browser type
            String browser = prop.getProperty("browser").toLowerCase();
            switch (browser) {
                case "chrome":
                    driver = new ChromeDriver();
                    break;
                case "firefox":
                    driver = new FirefoxDriver();
                    break;
                default:
                    throw new IllegalArgumentException("Unsupported browser: " + browser);
            }

            // Open the test URL
            driver.manage().window().maximize();
            driver.get(prop.getProperty("testurl"));
        }
    }

    /**
     * Teardown method to close the WebDriver instance after each test.
     */
    @AfterMethod
    public void teardown(ITestResult result) {
        // Take a screenshot if the test fails
        if (ITestResult.FAILURE == result.getStatus()) {
            captureScreenshot(result);
        }

        if (driver != null) {
            driver.quit(); // Close all browser windows and terminate WebDriver
            driver = null; // Reset driver to null
        }
        System.out.println("Teardown successful");
    }

    /**
     * Captures a screenshot on failure and saves it with a unique timestamp.
     * 
     * @param result - The test result
     */
    private void captureScreenshot(ITestResult result) {
        // Get the test name for the screenshot file
        String testName = result.getMethod().getMethodName();
        
        // Create a timestamp for the screenshot
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        
        // Screenshot file path
        String screenshotPath = System.getProperty("user.dir") + "\\target\\screenshots\\" + testName + "_" + timestamp + ".png";
        
        // Capture the screenshot
        File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        
        try {
            // Create the directory if it doesn't exist
            File screenshotDir = new File(System.getProperty("user.dir") + "\\target\\screenshots\\");
            if (!screenshotDir.exists()) {
                screenshotDir.mkdirs();
            }
            
            // Copy the screenshot to the target directory
            FileUtils.copyFile(screenshot, new File(screenshotPath));
            System.out.println("Screenshot saved at: " + screenshotPath);
        } catch (IOException e) {
            System.out.println("Failed to capture screenshot: " + e.getMessage());
        }
    }
}
