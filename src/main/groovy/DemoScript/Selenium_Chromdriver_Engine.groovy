package shamim.io


import com.rdc.importer.scrapian.ScrapianContext
import com.rdc.importer.scrapian.util.ModuleLoader
import org.apache.commons.io.FileUtils
import org.openqa.selenium.By
import org.openqa.selenium.OutputType
import org.openqa.selenium.TakesScreenshot
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit

context.setup([connectionTimeout: 25000, socketTimeout: 25000, retryCount: 10, multithread: true, userAgent: "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.61 Safari/537.36"])
context.session.encoding = "UTF-8"; //change it according to web page's encoding
context.session.escape = true;

NetflixAnimeNames script = new NetflixAnimeNames(context)
script.initParsing()

class NetflixAnimeNames {
    final ScrapianContext context
    final def moduleFactory = ModuleLoader.getFactory("6f3c2f4bbd013bb2a37e5a24530c559eb7736c72")
    static def root = "www.netflix.com"
    static def url = "https://www.netflix.com/browse/genre/7424"
    final addressParser
    final entityType

    // ========================================= CHROME DRIVER CONTROLLER DEFINITION =========================================
    class ChromeDriverEngine {
        /**
         * Local installation path of the Chrome WebDriver
         */
        String CHROME_DRIVER_PATH = "/usr/bin/chromedriver"
        ChromeOptions options
        WebDriver driver

        ChromeDriverEngine() {
            System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_PATH)

            options = new ChromeOptions()

            // You can comment out or remove any switch you want
            options.addArguments(
//                    "--headless", // Use this switch to use Chromium without any GUI
                "--disable-gpu",
                "--ignore-certificate-errors",
                "--window-size=1366,768", // Default window-size
                "--silent",
                "--blink-settings=imagesEnabled=false" // Don't load images
            )

            driver = new ChromeDriver(options)
            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS)
        }

        /**
         * This method invokes and retrieves a URL
         * @param URL: The URL address
         * @param sleepTime: Wait {ms} after getting the URL
         */
        void get(String URL, int sleepTime = 0) {
            System.out.println("[SELENIUM] Invoking: [" + URL + "]")
            driver.get(URL)

            if (sleepTime != 0) {
                wait(sleepTime)
            }
        }

        /**
         * This method takes a screenshot of the current window and saves
         * it to the 'output/' directory
         * @throws IOException
         */
        void takeScreenshot() throws IOException {
            System.out.println("Taking Screenshot ...")
            File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE)
            String fileName = "screenshot_" + LocalDateTime.now().toString() + ".png"
            FileUtils.copyFile(screenshot, new File(fileName))
        }

        /**
         * This method retrieves a web element by using it's xpath
         * in the current window.
         * It returns null if no element is found with the xpath.
         *
         * @param xpath: The xpath of the element
         * @return: null or WebElement
         */
        WebElement findByXpath(String xpath) {
            By by = new By.ByXPath(xpath)
            WebElement webElement

            try {
                webElement = driver.findElement(by)
            } catch (Exception e) {
                println("WebElement doesn't exist")
                webElement = null
            }

            return webElement == null ? null : webElement
        }

        /**
         * This method retrieves web elements by their xpath
         * in the current window and returns a list of elements.
         *
         * @param xpath: The xpath of the elements
         * @return: List<WebElement>
         */
        List<WebElement> findAllByXpath(String xpath) {
            By by = new By.ByXPath(xpath)
            return driver.findElements(by)
        }

        /**
         * This method retrieves a web element by using its id
         * @param id: String
         * @return WebElement
         */
        WebElement findById(String id) {
            By by = new By.ById(id)
            return driver.findElement(by)
        }

        /**
         * This method pauses the thread for the given amount of time
         * @param time: Sleep time in ms
         */
        void wait(int time) {
            try {
                Thread.sleep(time)
            } catch (InterruptedException e) {
                e.printStackTrace()
            }
        }

        /**
         * This method stops the selenium driver and closes the window(s)
         */
        void shutdown() {
            driver.close()
            driver.quit()
        }

        /**
         * This method navigates to the previous webpage in the current window.
         */
        void goBack() {
            driver.navigate().back()
        }

        /**
         * This page returns the HTML document of the current webpage as String
         * @return: String HTML document
         */
        String getSource() {
            return driver.getPageSource()
        }

        /**
         * This method makes the selenium webdriver to wait for a particular element to be
         * clickable for further proceeding
         * @param xpath: The xpath String of the element
         */
        void waitForElementToBeClickable(String xpath) {
            WebDriverWait wait = new WebDriverWait(driver, 10)
            wait.until(ExpectedConditions.elementToBeClickable(new By.ByXPath(xpath)))
        }
    }
    // ========================================= CHROME DRIVER CONTROLLER DEFINITION =========================================

    NetflixAnimeNames(context) {
        this.context = context
        entityType = moduleFactory.getEntityTypeDetection(context)
        addressParser = moduleFactory.getGenericAddressParser(context)
        addressParser.reloadData()
    }

    def initParsing() {
        ChromeDriverEngine engine = new ChromeDriverEngine()
        engine.get(this.url, 5000)

        getAnimeNamesWithXpath(engine)
        getAnimeThumbnailsWithRegex(engine)

        engine.shutdown()
    }

    def getAnimeNamesWithXpath(ChromeDriverEngine engine) {
        def allElements = engine.findAllByXpath("//span[@class='nm-collections-title-name']")

        println("Anime Names")
        for (int i = 0; i < allElements.size(); i++) {
            println(allElements[i].getText())
        }
    }

    def getAnimeThumbnailsWithRegex(ChromeDriverEngine engine) {
        def html = engine.getSource()

        def imageMatcher = html =~ /(?ism)nm-collections-title-img.*?src="(.*?)"/

        println("Thumbnail URLs")
        while (imageMatcher.find()) {
            println(imageMatcher.group(1))
        }
    }

}

