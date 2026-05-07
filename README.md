# Google Account Registration Automation

This project automates the Google account sign-up process using **Java**, **Selenium WebDriver**, and **Cucumber** (BDD). It is designed to demonstrate advanced automation techniques, such as handling dynamic elements and bypassing basic automation detections.

## 🚀 Technologies Used
* **Java 25** (OpenJDK)
* **Selenium WebDriver 4.21.0**
* **Cucumber (JUnit 4 Runner)**
* **Maven** (Dependency management)
* **WebDriverManager** (Automatic driver setup)

## 📋 Test Scenario
The automated test follows these steps:
1. Navigates to the Google Sign Up page.
2. Enters First Name and Last Name.
3. Fills in Birthday (Day, Month, Year) and Gender.
4. Selects the **"Create your own Gmail address"** option.
5. Generates and enters a unique Gmail username.
6. Enters and confirms a secure password.
7. Reaches the final step of the registration process.

## 💡 Key Features & Fixes
* **Anti-Bot Bypass**: Configured `ChromeOptions` (e.g., `AutomationControlled` disabled, custom `User-Agent`) to minimize detection.
* **Smart Waits**: Uses `WebDriverWait` (Explicit Waits) to ensure elements are interactable before any action.
* **JavaScript Execution**: Uses `JavascriptExecutor` to click elements that are often intercepted by Google's transition animations.
* **Dynamic Locators**: Handles dynamic field IDs by using flexible XPath and CSS selectors (e.g., finding inputs by name or index).
