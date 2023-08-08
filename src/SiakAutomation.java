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
public class SiakAutomation {

    /**
     * Method to simulate the filling of IRS forms with dummy data.
     * @deprecated This method is deprecated and is only used for testing purposes.
     * @param driver The WebDriver instance used to interact with the web application.
     */
    private static boolean dummyChecker(WebDriver driver) {
        driver.navigate().to("file:///Users/rijal/Documents/SIAK%20Automation/Simulasi%20IRS/Pengisian%20IRS.htm");
        String[][] matkul = {
                {"673415-3", "IOT 2"},
                {"672866-4", "Kemjar 1"},
                {"672882-3", "Probstok 2"},
        };

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
                    return true; // IRS process successful
                } else {
                    System.out.println("IRS belum tersimpan. Melanjutkan pengisian IRS kembali.");
                    // ... Continue filling the form again ...
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }

        return false; // IRS process not successful
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

    /**
     * Login to the SIAK NG web application.
     * @param driver The WebDriver instance used to interact with the web application.
     * @param username The username to login with.
     * @param password The password to login with.
     */
    public static boolean login(WebDriver driver, String username, String password) {
        try {
            driver.navigate().to("https://academic.ui.ac.id");
            Thread.sleep(50);
            driver.findElement(By.name("u")).sendKeys(username);
            Thread.sleep(50);
            driver.findElement(By.name("p")).sendKeys(password);
            Thread.sleep(50);
            driver.findElement(By.name("p")).sendKeys(Keys.RETURN);
            Thread.sleep(3000); // Adjust the wait time as needed
            WebElement titleElement = driver.findElement(By.tagName("title"));
            String pageTitle = titleElement.getAttribute("innerHTML");

            if (pageTitle.equals("Selamat Datang - SIAK NG")) {
                System.out.println("Login successful");
                return true;
            }

        } catch (Exception e) {
            System.out.println("Login error");
            System.out.println(e);
        }
        return false;
    }

    /** Performs the IRS process.
     * @param driver The WebDriver instance used to interact with the web application.
     * @param retry The number of times to retry the IRS process if it fails.
     * @return True if the IRS process is successful, false otherwise.
     */
    public static boolean performIRSProcess(WebDriver driver, int retry, List<MatkulData> matkulList) {
        try {
            driver.navigate().to("https://academic.ui.ac.id/main/CoursePlan/CoursePlanEdit");
            Actions mouse = new Actions(driver);
            WebElement IrsDropdown = driver.findElement(By.xpath("//*[text()='IRS']"));
            mouse.moveToElement(IrsDropdown).perform();
            WebElement IrsMenu = driver.findElement(By.xpath("//*[text()='Isi/Ubah IRS']"));
            mouse.moveToElement(IrsMenu).click().perform();
            System.out.println("masuk edit");

            // Rest of the code for IRS process ...
            for (MatkulData m : matkulList) {

                Thread.sleep(50);
                driver.findElement(By.cssSelector("input[value='" + m.code + "']")).click();
                System.out.println("Nama matkul: " + m.name);
            }
            Thread.sleep(250);
            driver.findElement(By.name("submit")).click();
            Thread.sleep(500);

            List<WebElement> successMsg = driver.findElements(By.xpath("//h3[text()='IRS berhasil tersimpan!']"));
            if (!successMsg.isEmpty()) {
                System.out.println("IRS berhasil tersimpan!");
                System.out.println("Jumlah retry: " + retry);
                return true; // IRS process successful
            } else {
                System.out.println("IRS belum tersimpan. Melanjutkan pengisian IRS kembali.");
                retry++;
                // ... Continue filling the form again ...
            }
        } catch (Exception e) {
            List<WebElement> successMsg = driver.findElements(By.xpath("//h3[text()='IRS berhasil tersimpan!']"));
            System.out.println(successMsg);
            if (!successMsg.isEmpty()) {
                System.out.println("IRS berhasil tersimpan!");
                System.out.println("Jumlah retry: " + retry);
                driver.navigate().to("https://academic.ui.ac.id/main/CoursePlan/CoursePlanViewSummary");
                return true; // IRS process successful
            } else {
                System.out.println("IRS belum tersimpan. Melanjutkan pengisian IRS kembali.");
                System.out.println(e);
                retry++;
            }
        }
        return false; // IRS process not successful
    }



    /** Main method to run the program.
     *
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        int retry = 0;
        System.setProperty("webdriver.firefox.whitelistedIps", "0.0.0.0");
        // Instantiate a FirefoxDriver class.
        WebDriver driver = new FirefoxDriver();
        // Launch Website

        // Define the file path to the credentials.properties file
        String propertiesFilePath = "src/credentials.properties";

        // Load the properties from the credentials.properties file
        Properties properties = loadPropertiesFromFile(propertiesFilePath);

        // Get the username and password from the properties
        String username = properties.getProperty("username");
        String password = properties.getProperty("password");

        String jsonFilePath = "src/matkul_data.json";
        List<MatkulData> matkulList = readMatkulDataFromFile(jsonFilePath);


        /*=====================================LOGIN=================================================*/
        while (true) {
            if (login(driver, username, password)) {
                break; // Exit the loop if login is successful
            }
        }
        Thread.sleep(500);

        /*=====================================ISI MK=================================================*/
        driver.navigate().to("https://academic.ui.ac.id/main/CoursePlan/CoursePlanEdit");

        while (true) {
            System.out.println("Retry ke-" + retry);
            if (performIRSProcess(driver, retry, matkulList)) {
                break; // Exit the loop if the IRS process is successful
            }

        }


    }
}