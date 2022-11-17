package shamim.io

import com.rdc.importer.scrapian.ScrapianContext
import com.rdc.importer.scrapian.util.ModuleLoader
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions

import java.util.concurrent.TimeUnit

context.setup([connectionTimeout: 35000, socketTimeout: 45000, retryCount: 1, multithread: true, userAgent: "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.80 Safari/537.36"])
context.session.encoding = "UTF-8" //change it according to web page's encoding
context.session.escape = true

Selenium__Test2 script = new Selenium__Test2(context)
script.initParsing()

class Selenium__Test2 {

    final ScrapianContext context
    final def moduleFactory = ModuleLoader.getFactory("7dd0501f220ad1d1465967a4e02f6ff17d7d9ff9")
    static final def STR_ROOT = "https://state.sor.dps.ms.gov/"
    static final def STR_URL = "https://state.sor.dps.ms.gov"

    final def CHROME_DRIVER_PATH = "/usr/bin/chromedriver"
    ChromeOptions options
    WebDriver driver

    Selenium__Test2(context) {

        this.context = context
//        entityType = moduleFactory.getEntityTypeDetection(context)
//        addressParser = moduleFactory.getGenericAddressParser(context)
//        addressParser.reloadData()


        System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_PATH)

        options = new ChromeOptions()

        // You can comment out or remove any switch you want
        options.addArguments(
//                    "--headless", // Use this switch to use Chromium without any GUI
            "--disable-gpu",
            "--ignore-certificate-errors",
            "--window-size=1366,768", // Default window-size
            "--silent",
//            "--blink-settings=imagesEnabled=false" // Don't load images
        )

        driver = new ChromeDriver(options)
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS)
    }


    def initParsing() {
        getUrlData(STR_URL)
    }

    def getUrlData(def url) {

        //----- Getting Into Main URL --------//
        driver.get(url)
        Thread.sleep(2000)

        //----- Click Agree Button ----------//
        driver.findElement(By.xpath('//*[@id="ContentPlaceHolder1_btnAgree"]')).click()

        //----- Continue button    ---------//
        Thread.sleep(10000)
        driver.findElement(By.xpath('//*[@id="ContentPlaceHolder1_ValidateButton"]')).click()
        Thread.sleep(2000)

        //----- Click Geographical Search ---//
        driver.findElement(By.linkText('Geographical Search')).click()
        Thread.sleep(2000)

        //----- Click County Radio Button ----//
        driver.findElement(By.xpath('//*[@id="ContentPlaceHolder1_rdoCounty"]')).click()
        Thread.sleep(2000)

        //----- Click CountyList Dropdown ----//
        driver.findElement(By.xpath('//*[@id="ContentPlaceHolder1_ddlCounty"]')).click()

        def conuntySize = driver.findElements(By.xpath('//*[@id="ContentPlaceHolder1_ddlCounty"]/option')).size()
        println "CountyListSize : $conuntySize"

        // Select County one by one
        driver.findElement(By.xpath('//*[@id="ContentPlaceHolder1_ddlCounty"]/option[2]')).click()
        Thread.sleep(1000)

        //------ Select Search Button ----------
        driver.findElement(By.xpath('//*[@id="ContentPlaceHolder1_btnSearch"]')).click()

        //----- Total URL ----------//

        def totalUrl = driver.findElements(By.xpath('//*[@id="Form1"]/div[3]/div[2]/table/tbody/tr')).size()
        println "Total URL : $totalUrl"
        int count = 2

        while (count <= totalUrl) {
            Thread.sleep(2000)
            driver.findElement(By.xpath("//*[@id=\"Form1\"]/div[3]/div[2]/table/tbody/tr[" + count + "]/td[2]/a")).click()
            Thread.sleep(3000)
            //------ Getting URL Data -------//
            println "Getting Page Source ------ [$count]"
            def html = driver.getPageSource()
            //println html

            //--- Click To Return Result Search ------
            driver.findElement(By.xpath('//*[@id="ContentPlaceHolder1_lnkResults"]')).click()
            Thread.sleep(3000)
            count++
        }

//        driver.findElement(By.xpath("//*[@id=\"Form1\"]/div[3]/div[2]/table/tbody/tr[2]/td[2]/a")).click()
//        println driver.getPageSource()



        //*[@id="Form1"]/div[3]/div[2]/table/tbody/tr[2]/td[2]/a
        //*[@id="Form1"]/div[3]/div[2]/table/tbody/tr[2]/td[2]
        //*[@id="Form1"]/div[3]/div[2]/table/tbody/tr

    }
}


