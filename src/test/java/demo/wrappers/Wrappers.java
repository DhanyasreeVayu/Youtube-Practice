package demo.wrappers;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;
import java.util.NoSuchElementException;

public class Wrappers {

    WebDriver driver;
    WebDriverWait wait;

    public Wrappers(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    public void navigateTo(String url) {
        driver.get(url);
    }

    public WebElement waitForElement(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public void clickElement(By locator) {
        waitForElement(locator).click();
    }

    public void scrollToElement(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
    }

    public void scrollRight(WebElement container) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollLeft = arguments[0].scrollWidth;", container);
    }

    public void typeText(By locator, String text) {
        WebElement input = waitForElement(locator);
        input.clear();
        input.sendKeys(text);
        input.sendKeys(Keys.ENTER);
    }

    public boolean isElementPresent(By locator) {
        try {
            driver.findElement(locator);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public String getText(By locator) {
        return waitForElement(locator).getText().trim();
    }

    public void waitForSeconds(int sec) {
        try { Thread.sleep(sec * 1000); } catch (InterruptedException ignored) {}
    }

    // Helper method to convert text like "1.2K" or "3.4M" to numeric
        public static int convertLikesTextToNumber(String text) {
    text = text.trim().toUpperCase();
    double num = 0;

    try {
        if (text.endsWith("K")) {
            num = Double.parseDouble(text.replace("K", "")) * 1000;
        } else if (text.endsWith("M")) {
            num = Double.parseDouble(text.replace("M", "")) * 1000000;
        } else if (text.matches("\\d+")) {
            num = Double.parseDouble(text);
        } else {
            // fallback — remove non-digits
            num = Double.parseDouble(text.replaceAll("\\D+", ""));
        }
    } catch (Exception ignored) {}

    return (int) num;
}

public double convertToNumber(String str) {
    if (str == null || str.trim().isEmpty()) {
        return 0;
    }
    str = str.trim().toUpperCase();

    String numberPart = str.replaceAll("[^0-9.]", "");
    if (numberPart.isEmpty()) {
        return 0;
    }

    double value = Double.parseDouble(numberPart);

        if (str.endsWith("K")) {
        value *= 1_000;
    } else if (str.endsWith("M")) {
        value *= 1_000_000;
    } else if (str.endsWith("B")) {
        value *= 1_000_000_000;
    }

     return value;
}
}
