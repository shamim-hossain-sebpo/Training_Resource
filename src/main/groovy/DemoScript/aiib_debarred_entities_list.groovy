package shamim.io


import com.rdc.importer.scrapian.ScrapianContext
import com.rdc.importer.scrapian.model.StringSource
import com.rdc.importer.scrapian.util.ModuleLoader
import com.rdc.scrape.ScrapeAddress
import com.rdc.scrape.ScrapeEntity
import com.rdc.scrape.ScrapeEvent
import org.openqa.selenium.By
import org.openqa.selenium.JavascriptExecutor
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.Select
import org.openqa.selenium.support.ui.WebDriverWait
import javax.xml.ws.WebEndpoint
import java.util.concurrent.TimeUnit

/**
 * =================================== PLEASE READ CAREFULLY BEFORE DEBUGGING =================================== *
 *                                                                                                                *
 * This script requires Selenium Web Driver and has captcha issue. Hence we approached with a semi automatic      *
 * procedure. We invoke the target website and accept the T&C by clicking the checkbox automatically.             *
 * When it is done, another page is loaded. This new page has search boxes and Google's reCaptcha verification.   *
 * The reCaptcha in the page is meant to solve manually. The developer should solve the reCaptcha within a        *
 * certain time period. Initially it is set to 70*1000 milliseconds. This value can be modified by the developer  *
 * from the field variable ${CAPTCHA_SOLVE_TIMEOUT}. Within this time period, the dev should solve the captcha    *
 * and wait. When the wait period is over, the script will start working automatically and start scraping data    *
 * from the website. For now, the script is using Chromium driver GUI mode.                                       *
 *                                                                                                                *
 * The script can be run in two modes. The script.initParsing() method can be used to run the script to capture   *
 * data and save them to a local file name given in the field ${SAVE_FILE_NAME} as well as creating entities.     *
 * In turns, all the data will be saved into a JSON file and can be used for debugging purpose by using the       *
 * method script.debugParsing(). It is recommended to to use only one method at a time. And one successful run    *
 * of the script.initParsing() method is required to get script.debugParsing() to work as we require the JSON     *
 * file for debugging.                                                                                            *
 *                                                                                                                *
 * ============================================================================================================== *
 */

context.setup([connectionTimeout: 25000, socketTimeout: 25000, retryCount: 10, multithread: true, userAgent: "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.61 Safari/537.36"])
context.session.encoding = "UTF-8"; //change it according to web page's encoding
context.session.escape = true;

AiibDebarredEntitiesListAlt script = new AiibDebarredEntitiesListAlt(context)

script.initParsing()

class  AiibDebarredEntitiesListAlt {
    final ScrapianContext context
//    final def moduleFactory = ModuleLoader.getFactory("6f3c2f4bbd013bb2a37e5a24530c559eb7736c72")
    def moduleLoaderVersion = context.scriptParams.moduleLoaderVersion
    final def moduleFactory = ModuleLoader.getFactory(moduleLoaderVersion)
    static def root = "https://sorb.chs.state.ma.us"
    static def url = "https://www.aiib.org/en/about-aiib/who-we-are/debarment-list/index.html"
    final addressParser
    final entityType
    final CAPTCHA_SOLVE_TIMEOUT = 70000
    def entityList = []

    // ========================================= CHROME DRIVER INITIALIZATION =========================================
    class ChromeDriverEngine {
        String CHROME_DRIVER_PATH = "/usr/bin/chromedriver"
        ChromeOptions options
        WebDriver driver

        ChromeDriverEngine() {
            System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_PATH)

            options = new ChromeOptions()
            options.addArguments(
//                "--headless",
                "--disable-gpu",
                "--ignore-certificate-errors",
                "--window-size=1366,768",
                "--silent"
            )

            driver = new ChromeDriver(options)
            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS)
        }

        ChromeDriverEngine(ArrayList<String> args) {
            System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_PATH)

            options = new ChromeOptions()
            options.addArguments(args)
            driver = new ChromeDriver(options)
        }

        void get(String URL, int sleepTime) {
            System.out.println("[SELENIUM] Invoking: [" + URL + "]")
            driver.get(URL)

            if (sleepTime != 0) {
                wait(sleepTime)
            }
        }

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

        List<WebElement> findAllByXpath(String xpath) {
            By by = new By.ByXPath(xpath)
            return driver.findElements(by)
        }

        WebElement findById(String id) {
            By by = new By.ById(id)
            return driver.findElement(by)
        }

        void wait(int time) {
            try {
                Thread.sleep(time)
            } catch (InterruptedException e) {
                e.printStackTrace()
            }
        }

        void shutdown() {
            driver.close()
            driver.quit()
        }

        void goBack() {
            driver.navigate().back()
        }

        String getSource() {
            return driver.getPageSource()
        }

        void waitForElementToBeClickable(String xpath) {
            WebDriverWait wait = new WebDriverWait(driver, 10)
            wait.until(ExpectedConditions.elementToBeClickable(new By.ByXPath(xpath)))
        }
    }
    // ========================================= CHROME DRIVER INITIALIZATION =========================================


    AiibDebarredEntitiesListAlt(context) {
        this.context = context
        entityType = moduleFactory.getEntityTypeDetection(context)
        addressParser = moduleFactory.getGenericAddressParser(context)
        addressParser.reloadData()
    }

    def initParsing() {
        ChromeDriverEngine driverEngine = new ChromeDriverEngine()
        driverEngine.get(url, 5)
        getData(driverEngine)
        driverEngine.shutdown()
    }

    def getData(ChromeDriverEngine engine) {

        WebElement menu = engine.findByXpath("/html/body/div[2]/section[1]/div/div[3]/div/div/div/div/div/div/ul/li[3]/a")
        menu.click()

        WebElement levelSelector = engine.findByXpath("/html/body/div[2]/section[1]/div/div[3]/div/div/div/div/div/div/ul/li[3]/ul/li[4]")
        levelSelector.click()
        engine.wait(1000)

        def rowSize = engine.findAllByXpath("//*[@id=\"itemContainer\"]/ul")
        def debarredName = engine.findAllByXpath("//*[@class=\"data1\"]")
        def debarredAddress = engine.findAllByXpath("//*[@class=\"data2\"]")
        def debarredEventDes = engine.findAllByXpath("//*[@class=\"data7\"]")
        def eventStartDate = engine.findAllByXpath("//*[@class=\"data5\"]")
        def eventEndDate = engine.findAllByXpath("//*[@class=\"data6\"]")

        for (int i = 0; i < rowSize.size(); i++) {
            def aliasList = []
            def dName = debarredName[i].getText()
            dName = sanitizeEntityName(dName)
            (dName, aliasList) = separateAlias(dName)
            def dAddress = debarredAddress[i].getText()
            def dEventDes = "This entity appears on the Asian Infrastructure Investment Bank published list of debarred entities. Prohibited Practice: " + debarredEventDes[i].getText()
            def dStartDate = eventStartDate[i].getText()
            def dEndDate = eventEndDate[i].getText()

            if (dAddress.contains("Beijing Xin Chuang Si Te Technology Co., Ltd.")) {
                dAddress = dAddress.replaceAll(/Beijing Xin Chuang Si Te Technology Co., Ltd. \(Xin Trust\)/, "")
                createEntity(dName, aliasList, dAddress, dEventDes, dStartDate, dEndDate)
                dName = "Beijing Xin Chuang Si Te Technology Co., Ltd. (Xin Trust)"
                dName = sanitizeEntityName(dName)
                (dName, aliasList) = separateAlias(dName)
            }
            createEntity(dName, aliasList, dAddress, dEventDes, dStartDate, dEndDate)
        }
    }


    def createEntity(def name, def aliasList, def address, def eventDescription, def eventStartDate, def eventEndDate) {
        def entity = null

        def entityName = name
        def entityType = detectEntity(name)

        entity = context.findEntity("name": entityName, "type": entityType)
        if (!entity) {
            entity = context.getSession().newEntity()
            entity.setName(entityName)
            entity.setType(entityType)
        }

        if (aliasList) {
            aliasList.each {
                if (!it.isEmpty()) {
                    entity.addAlias(it)
                }
            }
        }

        if (address) {
            ScrapeAddress scrapeAddress = new ScrapeAddress()
            def addrMap = addressParser.parseAddress([text: address, force_country: true])
            scrapeAddress = addressParser.buildAddress(addrMap, [street_sanitizer: street_sanitizer])
            entity.addAddress(scrapeAddress)
        }

        ScrapeEvent event = new ScrapeEvent()
        event.setDescription(eventDescription)

        if (eventStartDate) {
            eventStartDate = context.parseDate(new StringSource(eventStartDate), ["dd.MM.yyyy", "dd.M.yyyy", "d.MM.yyyy", "MMM d, yyyy", "dd/MM/yyyy", "dd/MM/yy", "dd-MMM-yy", "dd-MM-yyyy", "MMM-yy", "MMMM yyyy", "MM/yy"] as String[])
            event.setDate(eventStartDate)
        }
        if (eventEndDate) {
            eventEndDate = context.parseDate(new StringSource(eventEndDate), ["dd.MM.yyyy", "dd.M.yyyy", "d.MM.yyyy", "MMM d, yyyy", "dd/MM/yyyy", "dd/MM/yy", "dd-MMM-yy", "dd-MM-yyyy", "MMM-yy", "MMMM yyyy", "MM/yy"] as String[])
            event.setEndDate(eventEndDate)
        }
        entity.addEvent(event)
    }

    def separateAlias(def name) {
        def aliasList = []
        if (name.toString().contains("(")) {
            def aliasMatcher = name =~ /(?ism)\((.*?)\)/
            while (aliasMatcher.find()) {
                def totalAlias = aliasMatcher.group(0)
                def alias = aliasMatcher.group(1)
                aliasList.add(alias)
                name = name.toString().replaceAll(/\($totalAlias\)/, "").trim()
            }
        }
        return [name, aliasList]
    }

    def sanitizeEntityName(def entityName) {
        entityName = entityName.toString().trim()
        entityName = entityName.replaceAll(/Co.,/, "Co.")

        return entityName
    }

    def detectEntity(def name) {
        def type
        if (name =~ /^\S+$/) {
            type = "O"
        } else {
            type = entityType.detectEntityType(name)
            if (type.equals("P")) {
                if (name =~ /(?i)(?:Phamacy|Druggists|Homecare|Neuromedical|Clinic|Medicine|Hearing\s+Aid|Medi\-Screens|Softech|India|Trustee)/) {
                    type = "O"
                }
            } else if (name =~ /(?i)(?:Christie L|William House|Ben Parker|Margaret Leigh|Philip L|Ashley Nicole|Honey|Logic|Tangella Jackson|Kathy)/) {
                type = "P"
            }
        }
        return type
    }

    def street_sanitizer = { street ->
        street = street.toString().replaceAll(/(?s)\s+/, " ").trim()
        return street
    }

    def invoke(url, cache = false, tidy = false, clean = false) {
        return context.invoke([url: url, tidy: tidy, cache: cache, clean: clean])
    }

    def invokeBinary(url, type = null, paramsMap = null, cache = true, clean = true, miscData = [:]) {
        //Default type is GET
        Map dataMap = [url: url, type: type, params: paramsMap, clean: clean, cache: cache]
        dataMap.putAll(miscData)
        return context.invokeBinary(dataMap)
    }
}

