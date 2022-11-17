package shamim.io

import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver


// ----- Declearing and Setting ChromDriver Path ---- //

final def CHORME_DRIVER_PATH = "/usr/bin/chromedriver"
System.setProperty("webdriver.chrome.driver", CHORME_DRIVER_PATH)

// ----- Initialize and Launching ChromeDriver -------

WebDriver webDriver = new ChromeDriver()
webDriver.get("https://chaldal.com")
Thread.sleep(2000)

//----- search input on google --------//
webDriver.findElementByName("search_term_string").sendKeys("potato")
Thread.sleep(5000)
webDriver.findElementByXPath("//*[@id=\"page\"]/div/div[3]/div/div[1]/div[1]/div[1]/a").click()
Thread.sleep(2000)
webDriver.navigate().back()
webDriver.findElementByXPath("//*[@id=\"page\"]/div/div[3]/div/div[2]/div/div/div[2]/ul[1]/li[1]/div/a").click()


//------ Navigate to Another Link ---------//
Thread.sleep(2000)
webDriver.navigate().to("https://sebpo.com")
Thread.sleep(5000)
webDriver.navigate().back()
Thread.sleep(2000)
def source = webDriver.getPageSource()
println source


//webDriver.navigate().forward()



// --------- Closing WebDriver -------- //
 webDriver.close()