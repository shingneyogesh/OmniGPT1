package testcases;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;

public class Excelfile {
    public static void main(String[] args) {
        WebDriver driver = new ChromeDriver();

        try {
            driver.get("http://54.241.118.206/");
            driver.manage().window().maximize();
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

            Thread.sleep(5000);
            // Load the data from Excel file
            String excelFilePath = "D:\\Eclipse_Project\\OmiGPT\\Excel\\data.xlsx";
            FileInputStream fis = new FileInputStream(new File(excelFilePath));
            Workbook workbook = new XSSFWorkbook(fis);
            Sheet sheet = workbook.getSheetAt(0); // Assuming data is in the first sheet

            // Iterate through rows and fetch data
            for (Row row : sheet) {
                Cell cell = row.getCell(0); // Assuming prompts are in the first column
                String prompt = cell.getStringCellValue();
                System.out.println("Using prompt: " + prompt);

                // Locate the search prompt and input text
                WebElement searchPrompt = driver.findElement(By.xpath("//*[@id='prompt']"));
                searchPrompt.clear(); // Clear the input box before entering a new prompt
                searchPrompt.sendKeys(prompt);

                // Locate the Enter button and click it
                WebElement enterButton = driver.findElement(By.xpath("//*[@id='response-container']/section[3]/div/div/button"));
                enterButton.click();

                // Wait for 2 seconds and click the Stop button
                Thread.sleep(2000); // Wait for 2 seconds
              //  WebElement stopButton = driver.findElement(By.xpath("//*[@id='response-container']/section[3]/div/div/button/*[name()='svg']"));
             //   stopButton.click();

                // Wait until the response container is updated
                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='response-container']/section[2]")));

                System.out.println("Prompt processed and stopped: " + prompt);

                // Wait for 5 seconds before processing the next prompt
                Thread.sleep(5000); // 5-second gap
            }

            // Close workbook and file stream
           // workbook.close();
         //   fis.close();

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            // Close the driver and browser
            // driver.quit();
        }
    }
}
