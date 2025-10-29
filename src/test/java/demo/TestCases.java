package demo;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.*;
import org.testng.Assert;
import org.openqa.selenium.chrome.ChromeDriverService;
// import org.openqa.Builder;

import java.io.File;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import org.openqa.selenium.JavascriptExecutor;
import demo.utils.ExcelDataProvider;
import demo.wrappers.Wrappers;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class TestCases extends ExcelDataProvider {

    ChromeDriver driver;
    Wrappers wrapper;

    @BeforeTest
public void startBrowser() {
    try {
        // Ensure the log directory exists
        File logFile = new File("assesment/assets/chromedriver.log");
        logFile.getParentFile().mkdirs();

        // Create a ChromeDriverService with logging enabled
        ChromeDriverService service = new ChromeDriverService.Builder()
            .withLogFile(logFile)
            .withVerbose(true)
            .build();

        // Add Chrome options
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");

        // Initialize WebDriver using the service
        driver = new ChromeDriver(service, options);
        driver.manage().window().maximize();

        wrapper = new Wrappers(driver);

        System.out.println("ChromeDriver started and logging to: " + logFile.getAbsolutePath());
    } catch (Exception e) {
        e.printStackTrace();
    }
}

    @AfterTest
    public void endTest() {
        driver.quit();
    }

    // -------------------------- Test Case 01 --------------------------
    @Test
    public void testCase01() {
        System.out.println("Starting Test Case 01");

        // Navigate to YouTube homepage
        System.out.println("Navigate to YouTube");
        wrapper.navigateTo("https://www.youtube.com/");
        WebDriverWait wait = new WebDriverWait(driver, java.time.Duration.ofSeconds(10));
        wait.until(ExpectedConditions.titleContains("YouTube"));
        String currenturl = driver.getCurrentUrl();
        if (currenturl.contains("youtube.com")) {
            System.out.println("URL verification passed - Landed on YouTube homepage");
        } else {
            System.out.println("URL verification failed - Current URL: " + driver.getCurrentUrl());
        }

        // Click sidebar menu
        WebElement sidebarmenu = driver.findElement(By.xpath("//button[@aria-label='Guide']"));
        sidebarmenu.click();

        // Click About menu
        WebElement Aboutmenu = driver.findElement(By.xpath("//a[text()='About']"));
        Aboutmenu.click();

        // Print header from About page
        WebElement header = driver.findElement(By.xpath("//h1"));
        String headerText = header.getText();
        System.out.println("About Page Header: " + headerText);
        

        System.out.println("Completed Test Case 01");
    }

    // -------------------------- Test Case 02 --------------------------
    @Test
    public void testCase02() {
        System.out.println("Starting Test Case 02");

        // Navigate to YouTube homepage
        wrapper.navigateTo("https://www.youtube.com/");
        WebDriverWait wait = new WebDriverWait(driver, java.time.Duration.ofSeconds(10));
        wait.until(ExpectedConditions.titleContains("YouTube"));
        String currenturl = driver.getCurrentUrl();
        if (currenturl.contains("youtube.com")) {
            System.out.println("URL verification passed - Landed on YouTube homepage");
        } else {
            System.out.println("URL verification failed - Current URL: " + driver.getCurrentUrl());
        }

        // Click sidebar menu
        WebElement sidebarmenu = driver.findElement(By.xpath("//button[@aria-label='Guide']"));
        sidebarmenu.click();

        // Click Movies menu
        WebElement Moviesmenu = driver.findElement(By.xpath("//a[@title='Movies']"));
        wrapper.scrollToElement(Moviesmenu);
        Moviesmenu.click();
        wrapper.waitForSeconds(5);

        // Verify last movie details
        List<WebElement> movies = driver.findElements(By.xpath("//ytd-grid-movie-renderer[@class='style-scope yt-horizontal-list-renderer']"));
        if (movies.isEmpty()) {
            System.out.println("No movies found.");
            return;
        }
        WebElement lastMovie = movies.get(movies.size() - 1);
        wrapper.scrollToElement(lastMovie);
        String movieText = lastMovie.getText();

        // Check for Mature (A) rating
        if (movieText.contains("A")) {
            System.out.println("Movie marked Mature (A)");
        } else {
            System.out.println("Movie is not restricted to mature (A) audience");
        }

        // Check for category
        boolean hasCategory = movieText.matches("(?i).*(Comedy|Drama|Action|Animation|Romance).*");
        if (hasCategory) {
            System.out.println("Movie category found");
        } else {
            System.out.println("Movie category not found");
        }

//         // Category and audience assertions
// Assert.assertTrue(movieText.matches("(?i).*(Comedy|Drama|Action|Romance|Animation).*"), 
//                   "Movie category missing");
// Assert.assertTrue(movieText.contains("A") || movieText.contains("U/A"), 
//                   "Target audience info missing");

        System.out.println("Completed Test Case 02");
    }

    // -------------------------- Test Case 03 --------------------------
    @Test
    public void testCase03() {
        System.out.println("Starting Test Case 03");

        // Navigate to YouTube homepage
        wrapper.navigateTo("https://www.youtube.com/");
        WebDriverWait wait = new WebDriverWait(driver, java.time.Duration.ofSeconds(10));
        wait.until(ExpectedConditions.titleContains("YouTube"));
        String currenturl = driver.getCurrentUrl();
        if (currenturl.contains("youtube.com")) {
            System.out.println("URL verification passed - Landed on YouTube homepage");
        } else {
            System.out.println("URL verification failed - Current URL: " + driver.getCurrentUrl());
        }

        // Click sidebar menu
        WebElement sidebarmenu = driver.findElement(By.xpath("//button[@aria-label='Guide']"));
        sidebarmenu.click();

        //Click Music menu
        WebElement Musicmenu = driver.findElement(By.xpath("//a[@title='Music']"));
        wrapper.scrollToElement(Musicmenu);
        Musicmenu.click();
        wrapper.waitForSeconds(5);

        // Get Playlist
        List<WebElement> playlists = driver.findElements(By.xpath("//ytd-rich-item-renderer[@class='style-scope ytd-rich-shelf-renderer']"));
        if (playlists.isEmpty()) {
            System.out.println("No Playlists menu found.");
            return;
        }

        //Go to last playlist
        WebElement lastPlaylist = playlists.get(playlists.size() - 1);
        wrapper.scrollToElement(lastPlaylist);

        //Print playlist title
        String playlistName = lastPlaylist.getText().split("\n")[0];
        System.out.println("Playlist on right: " + playlistName);

        //Track Info
        String trackInfo = lastPlaylist.getText();
        int trackCount = 0;

        try {
            trackCount = Integer.parseInt(trackInfo.replaceAll("\\D+", ""));
        } catch (Exception ignored) {}
        if (trackCount <= 50) {
            System.out.println("Playlist track count within limit: " + trackCount);
        } else {
            System.out.println("Playlist exceeds 50 tracks: " + trackCount);
        }

        System.out.println("Completed Test Case 03");
    }

    // -------------------------- Test Case 04 --------------------------
    @Test
    public void testCase04() {
        System.out.println("Starting Test Case 04");

        // Navigate to YouTube homepage
        wrapper.navigateTo("https://www.youtube.com/");
        WebDriverWait wait = new WebDriverWait(driver, java.time.Duration.ofSeconds(10));
        wait.until(ExpectedConditions.titleContains("YouTube"));
        String currenturl = driver.getCurrentUrl();
        if (currenturl.contains("youtube.com")) {
            System.out.println("URL verification passed - Landed on YouTube homepage");
        } else {
            System.out.println("URL verification failed - Current URL: " + driver.getCurrentUrl());
        }

        // Click sidebar menu
        WebElement sidebarmenu = driver.findElement(By.xpath("//button[@aria-label='Guide']"));
        sidebarmenu.click();

        //Click News menu and Navigate to News page
        WebElement newsmenu = driver.findElement(By.xpath("//a[@title='News']"));
        newsmenu.click();
        wait.until(ExpectedConditions.titleContains("News"));

        //Get the latest news list
        List<WebElement> latestnewsCards = driver.findElements(By.xpath("//ytd-rich-item-renderer[@class='style-scope ytd-rich-shelf-renderer']"));
        if (latestnewsCards.isEmpty()) {
            System.out.println("No news cards found.");
            return;
        }

        // Print the first 3 news titles
        for (int i = 0; i < Math.min(3, latestnewsCards.size()); i++) {
            String title = latestnewsCards.get(i).findElement(By.id("video-title")).getText();
            System.out.println("News " + (i + 1) + ": " + title);
        }

        int totalLikes = 0;

        // Loop through first 3 videos to fetch likes
        for (int i = 0; i < Math.min(3, latestnewsCards.size()); i++) {
            WebElement card = latestnewsCards.get(i);

            // Open each video in a new tab to read likes count
    String videoUrl = card.findElement(By.id("video-title-link")).getAttribute("href");

    // Open video in new tab
        ((JavascriptExecutor) driver).executeScript("window.open(arguments[0])", videoUrl);

    // driver.executeScript("window.open(arguments[0])", videoUrl);

    // Switch to new tab
    ArrayList<String> tabs = new ArrayList<>(driver.getWindowHandles());
    driver.switchTo().window(tabs.get(tabs.size() - 1));

    try{
    // Wait for like element to be visible
    WebElement likeButton = wait.until(ExpectedConditions.visibilityOfElementLocated(
        By.xpath("//yt-formatted-string[@id='text' and @class='style-scope ytd-toggle-button-renderer style-text']")
    ));

    // Extract like count
    String likesText = likeButton.getText().trim();
    int likes = 0;

    // Convert text like "1.2K" or "3.4M" into numeric value
    if (likesText != null && !likesText.isEmpty()) {
        likes = wrapper.convertLikesTextToNumber(likesText);
    }

    totalLikes += likes;
    System.out.println("Likes on video " + (i + 1) + ": " + likes);
    } catch (Exception e) {
            System.out.println("Unable to retrieve likes for video " + (i + 1));
        }

        // Close the tab and switch back
        driver.close();
        ArrayList<String> remainingTabs = new ArrayList<>(driver.getWindowHandles());
        driver.switchTo().window(remainingTabs.get(0));
    }
        System.out.println("Total Likes on 3 Posts: " + totalLikes);
        System.out.println("Completed Test Case 04");
    }

    // -------------------------- Test Case 05 --------------------------
    @Test(dataProvider = "excelData", dataProviderClass = ExcelDataProvider.class)
    public void testCase05(String searchItem) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));
        System.out.println("Starting Test Case 05 for search: " + searchItem);
        
        //Navigate to the url
        wrapper.navigateTo("https://www.youtube.com/");

        //Find Search box and enter Movies
        WebElement searchbox = driver.findElement(By.name("search_query"));
        searchbox.sendKeys(searchItem);
        WebElement searching = driver.findElement(By.xpath("//button[@title='Search']"));
        searching.click();
        wait.until(ExpectedConditions.titleContains(searchItem));
        System.out.println(searchItem);

        //Get the list of Movies view count
        double totalViews = 0;
        while (totalViews < 10_00_00_000) { // 10 crore = 100 million
            List<WebElement> views = driver.findElements(By.xpath("//span[@class='inline-metadata-item style-scope ytd-video-meta-block']"));
            if (views.isEmpty()){
                System.out.println("No view count elements found for search: " + searchItem);
                break;
            }
            for (WebElement view : views) {
                String v = view.getText();
                v = v.replace("views", "").trim();

                double val = wrapper.convertToNumber(v);
                totalViews += val;

                if (totalViews >= 10_00_00_000) break;

            }
            ((JavascriptExecutor) driver).executeScript("window.scrollBy(0,1000);");
            wrapper.waitForSeconds(2);
        }

        //Print Movies view reached target
        if (totalViews >= 10_00_00_000) {
            System.out.println("Total views reached target: " + totalViews + "for " + searchItem);
        } else {
            System.out.println("Total views did not reach target: " + totalViews + "for " + searchItem);
        }

        System.out.println("Completed Test Case 05");
    }

}
