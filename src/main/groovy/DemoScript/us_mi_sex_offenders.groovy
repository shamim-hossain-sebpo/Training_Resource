package shamim.io



import com.rdc.importer.scrapian.ScrapianContext
import com.rdc.importer.scrapian.util.ModuleLoader
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait

import java.util.concurrent.TimeUnit

context.setup([connectionTimeout: 25000, socketTimeout: 25000, retryCount: 10, multithread: true, userAgent: "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.61 Safari/537.36"])
context.session.encoding = "UTF-8"; //change it according to web page's encoding
context.session.escape = true;

Us_sc_sex_offenders script = new Us_sc_sex_offenders(context)

script.initParsing()
// script.loadLocalDataForDebug()

class Us_sc_sex_offenders {
    final ScrapianContext context
    final def moduleFactory = ModuleLoader.getFactory("6f3c2f4bbd013bb2a37e5a24530c559eb7736c72")
    static def root = "https://state.sor.dps.ms.gov/"
    static def url = "https://state.sor.dps.ms.gov"
    final addressParser
    final entityType
    final CAPTCHA_SOLVE_TIMEOUT = 10000
    def entityList = []

    def urlList = []
    Set<String> urlSet = new HashSet<String>()

    // ========================================= CHROME DRIVER INITIALIZATION =========================================
    class ChromeDriverEngine {
        String CHROME_DRIVER_PATH = "/usr/bin/chromedriver"
        ChromeOptions options
        WebDriver driver

        ChromeDriverEngine() {
            System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_PATH)

            options = new ChromeOptions()
            options.addArguments(
//                    "--headless",
                "--disable-gpu",
                "--ignore-certificate-errors",
                "--window-size=1366,768",
                "--silent",
//                    "--blink-settings=imagesEnabled=false" // Don't load images
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


    Us_sc_sex_offenders(context) {
        this.context = context
        entityType = moduleFactory.getEntityTypeDetection(context)
        addressParser = moduleFactory.getGenericAddressParser(context)
        addressParser.reloadData()
    }


    def initParsing() {
        initDownload()

        getData()
    }

    def getData() {
        readHTMLFromLocal()
    }

    def readHTMLFromLocal() {
        int cntr = -1
        new File("/home/rubaiya/Rubaiya/RDCScrapper/output/Mississippi_Offenders").listFiles().each { file ->
            cntr++
//            if (cntr < 50) {
            if (cntr > 100 && cntr <= 200) {
                BufferedReader bufferedReader = new BufferedReader(new FileReader(file))
                def html = ""
                String line
                while ((line = bufferedReader.readLine()) != null) html = html + "\n" + line
                println("\n***********\n")
                println("Counter: $cntr")
                println("File Name: " + file.name)
//                println(html)

                def firstName, middleName, lastName, aliases, address, dob, gender, race, height, weight, hairColor, eyeColor, imageURL
                def firstNameMatcher, middleNameMatcher, lastNameMatcher, aliasesMatcher, addressMatcher, dobMatcher, genderMatcher, raceMatcher, heightMatcher, weightMatcher, hairColorMatcher, eyeColorMatcher, imageURLMatcher

                firstNameMatcher = html =~ /(?s)First Name:<\/div>\s*?(.+?)&nbsp;/
                middleNameMatcher = html =~ /(?s)Middle Name:<\/div>\s*?(.+?)&nbsp;/
                lastNameMatcher = html =~ /(?s)Last Name:<\/div>\s*?(.+?)&nbsp;/

                aliasesMatcher = html =~ /(?s)Aliases:<\/div>.+?<div.+?>(.+?)<\/div>(?:\s*?<div.+?>(.+?)<\/div>)?/

                addressMatcher = html =~ /(?s)Primary Address:<\/div>.+?"Row">(.+?)<\/div>/
                dobMatcher = html =~ /(?s)Date of Birth:<\/div>(.+?)<\/div>/

                genderMatcher = html =~ /(?s)Gender:<\/div>(.+?)<\/div>/
                raceMatcher = html =~ /(?s)Race:<\/div>(.+?)<\/div>/

                heightMatcher = html =~ /(?s)Height:<\/div>(.+?)<\/div>/
                weightMatcher = html =~ /(?s)Weight:<\/div>(.+?)<\/div>/

                hairColorMatcher = html =~ /(?s)Hair Color:<\/div>(.+?)<\/div>/
                eyeColorMatcher = html =~ /(?s)Eye Color:<\/div>(.+?)<\/div>/

                imageURLMatcher = html =~ /(?s)(?m)id="ContentPlaceHolder1_OffenderDetails1_listImages_ImageIdLit_0" src="(DisplayImage.+?OffenderId.+?ImageId.+?)" style/

                if (firstNameMatcher.find()) firstName = fixExtraSpace(firstNameMatcher.group(1).trim())
                if (middleNameMatcher.find()) middleName = fixExtraSpace(middleNameMatcher.group(1).trim())
                if (lastNameMatcher.find()) lastName = fixExtraSpace(lastNameMatcher.group(1).trim())

                if (addressMatcher.find()) address = fixExtraSpace(addressMatcher.group(1).trim())
                if (dobMatcher.find()) dob = fixExtraSpace(dobMatcher.group(1).trim())

                if (genderMatcher.find()) gender = fixExtraSpace(genderMatcher.group(1).trim())
                if (raceMatcher.find()) race = fixExtraSpace(raceMatcher.group(1).trim())

                if (heightMatcher.find()) height = fixExtraSpace(heightMatcher.group(1).trim())
                if (weightMatcher.find()) weight = fixExtraSpace(weightMatcher.group(1).trim())

                if (hairColorMatcher.find()) hairColor = fixExtraSpace(hairColorMatcher.group(1).trim())
                if (eyeColorMatcher.find()) eyeColor = fixExtraSpace(eyeColorMatcher.group(1).trim())

                if (imageURLMatcher.find()) imageURL = root + fixExtraSpace(imageURLMatcher.group(1).trim().replaceAll("amp;", ''))

//                println("First Name: " + firstName)
//                println("Middle Name: " + middleName)
//                println("Last Name: " + lastName)

                println("Name: $firstName $middleName $lastName")

                def aliasList = []
                if (aliasesMatcher.find()) {
                    aliases = aliasesMatcher.group(0)
                    aliases = aliases.toString().replaceAll(/<b>No records found\.<\/b>/, "")
//                    println(aliases)
                    def aliasMatcher = aliases =~ /(?s)<div.*?>(.+?)<\/div>/
                    while (aliasMatcher.find()) {
                        def alias = fixExtraSpace(aliasMatcher.group(1).trim())
                        aliasList.add(alias)
                        println("Alias: " + alias)
                    }
                }

                println("Address: " + address)
                println("DoB: " + dob)

                println("Gender: " + gender)
                println("Race: " + race)

                println("Height: " + height)
                println("Weight: " + weight)

                println("Hair Color: " + hairColor)
                println("Eye Color: " + eyeColor)

                println("Image URL: " + imageURL)
            }
        }
    }

    def fixExtraSpace(value) {
        value = value.toString().replaceAll(/\s+/, ' ')
    }

    def initDownload() {
        ChromeDriverEngine driverEngine = new ChromeDriverEngine()
        driverEngine.get(url, 5)

        getURLData(driverEngine)

        driverEngine.shutdown()
    }

    def getURLData(ChromeDriverEngine engine) {
        def agreeButton = engine.findByXpath("//*[@id=\"ContentPlaceHolder1_btnAgree\"]")
        agreeButton.click()

        boolean wait = true

        if (wait) {
            engine.wait(CAPTCHA_SOLVE_TIMEOUT)
        }

        def continueButton = engine.findByXpath("//*[@id=\"ContentPlaceHolder1_ValidateButton\"]")
        continueButton.click()

        def geographicalSearch = engine.findByXpath("//*[@id=\"mymenu\"]/li[2]/a")
        geographicalSearch.click()

        engine.wait(1000)

        def countyCheckbox = engine.findByXpath("//*[@id=\"ContentPlaceHolder1_rdoCounty\"]")
        countyCheckbox.click()

        def countyDropdown = engine.findByXpath("//*[@id=\"ContentPlaceHolder1_ddlCounty\"]")
        countyDropdown.click()

        def countyList = engine.findAllByXpath("//*[@id=\"ContentPlaceHolder1_ddlCounty\"]/option")
        int countySize = countyList.size()
        println(countySize)

        def start = 20
        def end = start + 1

        for (int i = start; i < end; i++) {
            println("County Counter: " + i)
            def captchaBox = engine.findByXpath("//*[@id=\"ContentPlaceHolder1_CodeTextBox\"]")
            def captachBoxString = captchaBox.toString()
            if (captachBoxString.contains("//*[@id=\"ContentPlaceHolder1_CodeTextBox\"]")) {
                println("Captcha found")
                engine.wait(CAPTCHA_SOLVE_TIMEOUT)
                //*[@id="ContentPlaceHolder1_ValidateButton"]
                def continueButton1 = engine.findByXpath("//*[@id=\"ContentPlaceHolder1_ValidateButton\"]")
                continueButton1.click()
            } else {
                println("No captcha")
            }


            def county = engine.findByXpath("//*[@id=\"ContentPlaceHolder1_ddlCounty\"]/option[" + i + "]")
            county.click()
            engine.wait(500)

            def searchButton = engine.findByXpath("//*[@id=\"ContentPlaceHolder1_btnSearch\"]")
            searchButton.click()
            engine.wait(500)

            def table = engine.findByXpath("//*[@id=\"Form1\"]/div[3]/div[2]/table")
            Thread.sleep(1000)
            def htmlTable = engine.getSource()

            def urls = engine.findAllByXpath("//*[@id=\"Form1\"]/div[3]/div[2]/table/tbody/tr")
            int urlSize = urls.size()
            println("URL Size: " + urlSize)

            int j = 201

            while (j < urlSize) {
                j++
                println("URL Counter: " + j)

                def url = engine.findByXpath("//*[@id=\"Form1\"]/div[3]/div[2]/table/tbody/tr[" + j + "]/td[2]/a")
                url.click()

                engine.wait(3000)

                def html = engine.getSource()
                def urlMatcher = html =~ /(?m)href="(OffenderDetails.+?;ID=.+?)"/

                if (urlMatcher.find()) {
                    def htmlURL = urlMatcher.group(1)
                    saveFile(htmlURL, html)
                }

                def returnToResults = engine.findByXpath("//*[@id=\"ContentPlaceHolder1_lnkResults\"]")
                returnToResults.click()

                engine.wait(1000)
            }

        }

        engine.wait(3000)
        engine.shutdown()

    }

    def saveFile(def fileName, def html) {

        def folder = new File('/home/rubaiya/Rubaiya/RDCScrapper/output/Mississippi_Offenders')
        if (!folder.exists()) {
            folder.mkdir()
        }

        fileName = fileName + ".html"
        // Enter filename in which you want to download
        File actualFile = new File(folder, fileName)
        BufferedWriter writer = new BufferedWriter(new FileWriter(actualFile))
        // read each line fromstream till end
        writer.write(html)
        println("File successfully saved")
        writer.close();

    }


    def handleData(html) {
        def firstNameCheck = html =~ /(?ism)Last Name:<\/div>(.*?)<\/div>/

        def firstName

        if (firstNameCheck.find()) {
            firstName = firstNameCheck.group(1).trim()
            println("First Name : " + firstName)
        }
    }

    def invokeUrl(url, type = null, paramsMap = null, cache = true, clean = true, miscData = [:]) {
        Map dataMap = [url: url, type: type, params: paramsMap, clean: clean, cache: cache]
        dataMap.putAll(miscData)
        return context.invoke(dataMap)
    }
}

