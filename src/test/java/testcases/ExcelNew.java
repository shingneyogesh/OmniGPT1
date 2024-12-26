package testcases;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Iterator;

public class ExcelNew {
    public static void main(String[] args) {
        // Set the path to ChromeDriver
        System.setProperty("webdriver.chrome.driver", "path/to/chromedriver"); // Update this with your ChromeDriver path

        // Configure ChromeOptions to avoid potential issues
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*"); // Bypass potential CORS issues
        options.addArguments("--disable-gpu"); // Optional: Disable GPU rendering if encountering issues
        options.addArguments("--start-maximized"); // Start in maximized mode

        WebDriver driver = new ChromeDriver(options);

        try {
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

            // Load Excel data
            String excelFilePath = "data.xlsx"; // Replace with your Excel file path
            FileInputStream inputStream = new FileInputStream(excelFilePath);
            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheet("Sheet1"); // Replace with your sheet name

            // Iterate through Excel rows
            Iterator<Row> rowIterator = sheet.iterator();
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                Cell promptCell = row.getCell(0); // Assuming the prompt text is in column A
                String promptText = promptCell.getStringCellValue();

                driver.get("http://54.241.118.206/");

                Thread.sleep(2000);

                // Locate the search prompt and input text from Excel
                WebElement searchPrompt = driver.findElement(By.xpath("//*[@id='prompt']"));
                searchPrompt.sendKeys(promptText);

                Thread.sleep(2000);

                // Locate the Enter button and click it
                WebElement enterButton = driver.findElement(By.xpath("//*[@id='response-container']/section[3]/div/div/button"));
                enterButton.click();

                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='response-container']/section[2]")));

                Thread.sleep(30000);

                JavascriptExecutor javascriptExecutor = (JavascriptExecutor) driver;

                // Locate and click the "Copy" button
                By copyButtonLocator = By.xpath("//section[@class='flex-1 overflow-y-auto custom-scrollbar']//button[2]//*[name()='svg']");
                WebElement copyButton = driver.findElement(copyButtonLocator);
                javascriptExecutor.executeScript("arguments[0].scrollIntoView(true);", copyButton);
                wait.until(ExpectedConditions.elementToBeClickable(copyButton));
                copyButton.click();
                System.out.println("Copy button clicked successfully for prompt: " + promptText);

                Thread.sleep(5000);
            }

            // Close workbook
            workbook.close();

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            // Ensure the browser is closed properly
            driver.quit();
        }
    }
}
