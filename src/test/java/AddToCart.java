import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;

public class AddToCart {

    public static void main(String[] args) throws IOException, InterruptedException {
        // Set the path to your ChromeDriver executable
        System.setProperty("webdriver.chrome.driver", "D:\\Eclipse_Project\\SecondJava\\src\\chromedriver.exe"); // Update path to your chromedriver
        
        WebDriver driver = new ChromeDriver();
        
        try {
            // Navigate to the Saucedemo website
            driver.get("https://www.saucedemo.com/");
            Thread.sleep(9000);

            // Read data from Excel file
            String filePath = "C:\\Users\\shing\\Desktop\\data.xlsx"; // Adjusted to use the uploaded file
            FileInputStream fis = new FileInputStream(new File(filePath));
            Workbook workbook = new XSSFWorkbook(fis);
            Sheet sheet = workbook.getSheet("Sheet1"); // Specify the sheet name "Sheet1"
            Iterator<Row> rowIterator = sheet.iterator();

            // Read data from Excel for username and password
            String username = "";
            String password = "";
            if (rowIterator.hasNext()) {
                Row row = rowIterator.next(); // Assuming the first row has the credentials
                username = row.getCell(0).getStringCellValue(); // Username from first column
                password = row.getCell(1).getStringCellValue(); // Password from second column
            }
            
            // Close the Excel workbook and file stream
            workbook.close();
            fis.close();

            // Login to the website using data from Excel
            WebElement usernameField = driver.findElement(By.cssSelector("#user-name"));
            usernameField.sendKeys(username);

            WebElement passwordField = driver.findElement(By.cssSelector("#password"));
            passwordField.sendKeys(password);

            WebElement loginButton = driver.findElement(By.cssSelector("#login-button"));
            loginButton.click();

            // Perform additional actions (e.g., adding items to the cart)
            Thread.sleep(2000);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Close the browser
            //driver.quit();
        }
    }
}
