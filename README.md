# SiakAutomation

This Java program is a Selenium WebDriver-based bot designed to automate the process of filling the Individual Study Plan (IRS) form on the academic portal of the University of Indonesia (UI). The bot uses FirefoxDriver to interact with the web application.

## Prerequisites

- Java Development Kit (JDK)
- Selenium WebDriver
- Google Gson library

## Setup

1. Clone the repository and navigate to the project directory.

2. Install the required libraries (Selenium WebDriver and Gson) if you haven't already.

3. Create a `credentials.properties` file in the `src` directory. The `credentials.properties` file should contain your UI academic portal username and password in the following format:

    ```properties
    username=your_username
    password=your_password
    ```

4. Create a `matkul_data.json` file in the src directory. The `matkul_data.json` file should contain the list of course codes followed by SKS amount and names that you want to add to the IRS form. The JSON format should be as follows:
    ```json
    [
      {
        "code": "673415-3",
        "name": "IOT 2"
      },
      {
        "code": "672866-4",
        "name": "Kemjar 1"
      },
      {
        "code": "672882-3",
        "name": "Probstok 2"
      }
    ]
    ```
> To get the codes for each matkul, go to [this page in SIAK NG](https://duckduckgo.com) and hover your cursor above the desired matkul, your code should pop up.
> ![img.png](img.png)\
> For the example above, the code is `725776-3`


## Usage
Run the SiakAutomation class. The program will navigate to the UI academic portal, login using the credentials provided in the credentials.properties file, and then proceed to fill the IRS form with the course data provided in the matkul_data.json file. The program will keep retrying until it successfully saves the IRS form with the selected courses.
## Notes
* Make sure you have the Firefox browser installed on your machine, as the bot uses FirefoxDriver to interact with the web application.
* The program uses Selenium WebDriver's FirefoxDriver. If you prefer a different browser, you can replace it with a different driver, such as ChromeDriver or EdgeDriver, by changing the WebDriver driver = new FirefoxDriver(); line to the appropriate driver initialization.
* Before running the bot, ensure that you have a stable internet connection, and the UI academic portal is accessible.
* The bot is designed for educational purposes and should be used responsibly and in accordance with the university's policies and guidelines.
* The bot may require adjustments or modifications if there are changes in the UI academic portal's structure or authentication mechanisms.

## Disclaimer

The provided code is for educational and learning purposes only. Use it responsibly and at your own risk. The developer of this bot is not responsible for any misuse or violations of any institution's policies or terms of service. Be sure to comply with the regulations and terms of the website you are automating.