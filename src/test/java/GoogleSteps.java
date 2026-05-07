import io.cucumber.java.en.*;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class GoogleSteps {

    WebDriver driver;
    WebDriverWait wait;
    JavascriptExecutor js;

    @Given("I open the Google Sign Up page")
    public void openGoogle() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();

        // Настройки для обхода защиты от ботов
        options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
        options.setExperimentalOption("useAutomationExtension", false);
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36");

        driver = new ChromeDriver(options);
        driver.manage().window().maximize();

        js = (JavascriptExecutor) driver;
        js.executeScript("Object.defineProperty(navigator, 'webdriver', {get: () => undefined})");

        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        driver.get("https://accounts.google.com/signup");
    }

    @When("I enter name {string} and surname {string}")
    public void enterNames(String name, String surname) {
        WebElement firstName = wait.until(ExpectedConditions.elementToBeClickable(By.name("firstName")));
        firstName.sendKeys(name);
        driver.findElement(By.name("lastName")).sendKeys(surname);
    }

    @And("I click Next on name page")
    public void clickNextName() {
        clickNextButton();
    }

    @And("I enter birthday {string} {string} {string} and gender {string}")
    public void enterBasicInfo(String day, String month, String year, String gender) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("day"))).sendKeys(day);

        // Выбор месяца
        driver.findElement(By.id("month")).click();
        WebElement monthOption = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='" + month + "']")));
        js.executeScript("arguments[0].click();", monthOption);

        driver.findElement(By.id("year")).sendKeys(year);

        // Выбор пола
        driver.findElement(By.id("gender")).click();
        WebElement genderOption = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='" + gender + "']")));
        js.executeScript("arguments[0].click();", genderOption);
    }

    @And("I click Next on basic info page")
    public void clickNextBasic() {
        clickNextButton();
    }

    @And("I click create own email option")
    public void clickCreateOwnEmail() {
        // Решение проблемы ElementClickInterceptedException:
        // Используем JS клик, чтобы проигнорировать перекрывающие слои анимации
        WebElement link = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//button[contains(.,'Нет адреса электронной почты')]")
        ));
        js.executeScript("arguments[0].click();", link);
    }

    @And("I enter gmail username {string}")
    public void enterGmailUsername(String username) {
        // После клика ждем поле ввода. У Google это либо "Username", либо "identifier"
        WebElement emailField = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//input[@name='Username' or @name='identifier']")
        ));

        String uniqueUsername = username + System.currentTimeMillis();
        emailField.clear();
        emailField.sendKeys(uniqueUsername);
    }

    @And("I click Next on username page")
    public void clickNextUsername() {
        clickNextButton();
    }

    @And("I enter password {string} and confirm it")
    public void enterPassword(String pwd) {
        // Ждем появления хотя бы одного поля пароля
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@type='password']")));

        // Находим все поля пароля на странице
        java.util.List<WebElement> passwordFields = driver.findElements(By.xpath("//input[@type='password']"));

        if (passwordFields.size() >= 2) {
            // Заполняем первое поле
            passwordFields.get(0).clear();
            passwordFields.get(0).sendKeys(pwd);

            // Заполняем второе поле
            passwordFields.get(1).clear();
            passwordFields.get(1).sendKeys(pwd);
        } else {
            // Если Google решил показать только одно поле (бывает в некоторых версиях)
            passwordFields.get(0).clear();
            passwordFields.get(0).sendKeys(pwd);

            // Пробуем найти поле подтверждения по альтернативному пути, если оно не попало в список
            try {
                WebElement confirm = driver.findElement(By.xpath("//input[@name='ConfirmPasswd' or @name='confirmPasswd' or @type='password']"));
                confirm.clear();
                confirm.sendKeys(pwd);
            } catch (Exception e) {
                System.out.println("Second password field not found or not required");
            }
        }
    }

    @And("I click Next to finish")
    public void finalNext() {
        clickNextButton();
        System.out.println("Registration process reached password/final step.");
        // driver.quit(); // Раскомментируй, если хочешь, чтобы браузер закрывался сам
    }

    // Универсальный метод для всех кнопок "Далее"
    private void clickNextButton() {
        WebElement nextBtn = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//span[contains(text(),'Далее') or contains(text(),'Next')]")
        ));
        // Для стабильности используем обычный клик, но если будет падать — можно заменить на JS
        try {
            nextBtn.click();
        } catch (ElementClickInterceptedException e) {
            js.executeScript("arguments[0].click();", nextBtn);
        }
    }
}