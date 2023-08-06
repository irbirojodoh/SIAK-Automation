import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.io.IOException;
import java.util.*;
import java.io.FileInputStream;
import java.util.Properties;


class MatkulData {
    String code;
    String name;
}
public class BotRijal {

    /**
     * Method to simulate the filling of IRS forms with dummy data.
     *
     * @param driver The WebDriver instance used to interact with the web application.
     */
    private static void dummyChecker(WebDriver driver){

        driver.navigate().to("file:///Users/rijal/Documents/SIAK%20Automation/Simulasi%20IRS/Pengisian%20IRS.htm");
        String[][] matkul = {
                {"673415-3", "IOT 2"},
                {"672866-4", "Kemjar 1"},
                {"672882-3", "Probstok 2"},
        };
        //Dummy Page
        try {
            while (true) {
                Actions mouse = new Actions(driver);
                System.out.println("masuk edit");
                for (String[] m : matkul) {
                    Thread.sleep(100);
                    driver.findElement(By.cssSelector("input[value='" + m[0] + "']")).click();
                    System.out.println(m[1]);
                }
                Thread.sleep(500);
                driver.findElement(By.name("submit")).click();
                Thread.sleep(3000);
                List<WebElement> successMsg = driver.findElements(By.xpath("//h3[text()='IRS berhasil tersimpan!']"));
                if (!successMsg.isEmpty()) {
                    System.out.println("IRS berhasil tersimpan!");
                    break; // Stop the loop since the IRS is successfully saved
                } else {
                    System.out.println("IRS belum tersimpan. Melanjutkan pengisian IRS kembali.");
                    // ... Continue filling the form again ...
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    /**
     * Reads the MatkulData from a JSON file and returns a list of MatkulData objects.
     *
     * @param filePath The file path to the JSON file containing the MatkulData.
     * @return A list of MatkulData objects read from the JSON file.
     */
    private static List<MatkulData> readMatkulDataFromFile(String filePath) {
        List<MatkulData> matkulList = null;
        try (FileReader reader = new FileReader(filePath)) {
            Type listType = new TypeToken<List<MatkulData>>() {}.getType();
            matkulList = new Gson().fromJson(reader, listType);
        } catch (IOException e) {
            e.printStackTrace();
        }
        for(MatkulData m : matkulList){
            System.out.println(m.code + " - " + m.name);
        }
        return matkulList;
    }

    /**
     * Loads properties from a properties file and returns a Properties object.
     *
     * @param filePath The file path to the properties file.
     * @return A Properties object containing the properties loaded from the file.
     */
    private static Properties loadPropertiesFromFile(String filePath) {
        Properties properties = new Properties();
        try (FileInputStream fileInputStream = new FileInputStream(filePath)) {
            properties.load(fileInputStream);
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the error appropriately or throw an exception if needed
        }
        return properties;
    }


    public static void main(String[] args) throws InterruptedException {
        int i;
        System.setProperty("webdriver.firefox.whitelistedIps", "0.0.0.0");
        // Instantiate a SafariDriver class.
        WebDriver driver = new FirefoxDriver();
        // Launch Website

        // Define the file path to the credentials.properties file
        String propertiesFilePath = "src/credentials.properties";

        // Load the properties from the credentials.properties file
        Properties properties = loadPropertiesFromFile(propertiesFilePath);

        // Get the username and password from the properties
        String username = properties.getProperty("username");
        String password = properties.getProperty("password");

        for(i = 0; i<1; i++) {
            try{
                driver.navigate().to("https://academic.ui.ac.id");
                Thread.sleep(50);
                driver.findElement(By.name("u")).sendKeys(username);
                Thread.sleep(50);
                driver.findElement(By.name("p")).sendKeys(password);
                Thread.sleep(50);
                driver.findElement(By.name("p")).sendKeys(Keys.RETURN);
            } catch (Exception e) {
                i = 0;
                System.out.println("Login error");
                System.out.println(e);
                driver.navigate().to("https://academic.ui.ac.id");

            }
        }
        Thread.sleep(500);
//        dummyChecker(driver);
        /*=====================================ISI MK =================================================*/
        Thread.sleep(500);
        driver.navigate().to("https://academic.ui.ac.id/main/CoursePlan/CoursePlanEdit");
        String jsonFilePath = "src/matkul_data.json";
        List<MatkulData> matkulList = readMatkulDataFromFile(jsonFilePath);

        while(true) {
            try{
                driver.navigate().to("https://academic.ui.ac.id/main/CoursePlan/CoursePlanEdit");
                Actions mouse = new Actions(driver);
                WebElement IrsDropdown = driver.findElement(By.xpath("//*[text()='IRS']"));
                mouse.moveToElement(IrsDropdown).perform();
                WebElement IrsMenu = driver.findElement(By.xpath("//*[text()='Isi/Ubah IRS']"));
                mouse.moveToElement(IrsMenu).click().perform();
                System.out.println("masuk edit");
                for (MatkulData m : matkulList) {
                    Thread.sleep(500);
                    driver.findElement(By.cssSelector("input[value='" + m.code + "']")).click();
                    System.out.println("Nama matkul: " + m.name);
                }
                Thread.sleep(500);
                driver.findElement(By.name("submit")).click();
                Thread.sleep(3000);
                List<WebElement> successMsg = driver.findElements(By.xpath("//h3[text()='IRS berhasil tersimpan!']"));
                if (!successMsg.isEmpty()) {
                    System.out.println("IRS berhasil tersimpan!");
                    break; // Stop the loop since the IRS is successfully saved
                } else {
                    System.out.println("IRS belum tersimpan. Melanjutkan pengisian IRS kembali.");
                    // ... Continue filling the form again ...
                }

            } catch (Exception e) {
                List<WebElement> successMsg = driver.findElements(By.xpath("//h3[text()='IRS berhasil tersimpan!']"));
                System.out.println(successMsg);
                if (!successMsg.isEmpty()) {
                    System.out.println("IRS berhasil tersimpan!");
                    driver.navigate().to("https://academic.ui.ac.id/main/CoursePlan/CoursePlanViewSummary");
                    break; // Stop the loop since the IRS is successfully saved
                } else {
                    System.out.println("IRS belum tersimpan. Melanjutkan pengisian IRS kembali.");
                    System.out.println(e);

                    driver.navigate().to("https://academic.ui.ac.id/main/CoursePlan/CoursePlanEdit");
                }

            }
        }

    }
}