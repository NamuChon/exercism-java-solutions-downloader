package main;

import java.time.*;
import java.util.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.*;
import org.openqa.selenium.support.ui.*;
import io.github.bonigarcia.wdm.*;

public class Main {

    private static final int WAITING_TIME = 60;

    private static WebDriver driver;
    private static WebDriverWait wait;
    private static FileConstructor fileConstructor;

    public static void main(String[] args) {
        WebDriverManager.chromedriver().setup();

        fileConstructor = new FileConstructor();
        UserInput userInput = new UserInput();

        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(WAITING_TIME));
        driver.get("about:blank");
        iteratePages("https://exercism.org/profiles/" + userInput.userName + "/solutions?track_slug=java&page=");

        driver.quit();
    }

    private static void iteratePages(String pageUrl) {
        int i = 1;
        boolean done = false;
        while (!done) {
            String currentUrl = pageUrl + i;
            driver.navigate().to(currentUrl);
            List<String> solutionUrls = waitForElement(By.className("solutions"))
                    .findElements(By.tagName("a"))
                    .stream()
                    .map(element -> element.getAttribute("href"))
                    .toList();
            if (solutionUrls.isEmpty()) {
                done = true;
            } else {
                solutionUrls.forEach(Main::handleSolution);
            }
            i++;
        }
    }

    private static void handleSolution(String url) {
        driver.navigate().to(url);
        WebElement tag = waitForElement(By.cssSelector("div.c-exercise-type-tag, div.c-difficulty-tag"));
        if (tag.getAttribute("class").contains("--tutorial")) return;

        waitForElement(By.className("c-iterations-footer"));

        List<Map<String, String>> iterations = new ArrayList<>();
        boolean done = false;
        List<WebElement> previousButtonTest = driver.findElements(By.cssSelector("button.btn-keyboard-shortcut.previous"));
        WebElement previousButton = null;
        if (previousButtonTest.isEmpty()) {
            iterations.add(getIterationFileCodes());
            done = true;
        } else {
            previousButton = previousButtonTest.getFirst();
        }

        while (!done) {
            final Map<String, String> current = getIterationFileCodes();
            iterations.addFirst(current);
            if (previousButton.isEnabled()) {
                previousButton.click();
                wait.until(_ -> !getIterationFileCodes().equals(current));
            } else {
                done = true;
            }
        }

        String difficulty = tag.getText().trim().replaceAll("\\s", "-").toLowerCase();
        String taskName = url.replaceAll(".+/exercises/(.+)/solutions/.+", "$1");

        fileConstructor.downloadSolution(difficulty, taskName, iterations);
    }

    private static Map<String, String> getIterationFileCodes() {
        By resultZoneSelector = By.className("c-results-zone");
        wait.until(d -> !d.findElement(resultZoneSelector).getAttribute("class").contains("--fetching"));

        String classes = waitForElement(By.className("c-copy-text-to-clipboard"))
                .findElement(By.tagName("span"))
                .getAttribute("data-content");

        List<String> names = waitForElement(By.cssSelector("div.flex.flex-grow.relative"))
                .findElements(By.tagName("button"))
                .stream()
                .map(button -> button.getAttribute("id").replaceAll("tab-src/main/java/(.+?)\\.java\\d+", "$1"))
                .toList();

        List<String> codes = Arrays.asList(classes.split("\n\n\n\n"));

        Map<String, String> result = new HashMap<>();
        for (int i = 0; i < names.size(); i++)
            result.put(names.get(i), codes.get(i));
        return result;
    }

    private static WebElement waitForElement(By by) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(by));
    }
}