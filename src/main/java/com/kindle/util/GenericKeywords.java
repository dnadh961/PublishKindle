package com.kindle.util;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.FileInputStream;
import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class GenericKeywords {

	public WebDriver driver;
	public Properties prop;
	public Properties dataProp;
	public String[] authorList;
	public WebDriverWait wait;

	public GenericKeywords() {
		prop = new Properties();
		dataProp = new Properties();
		try {
			FileInputStream fis = new FileInputStream(
					System.getProperty("user.dir") + "/src/main/resources/Config.properties");
			prop.load(fis);
			FileInputStream fis1 = new FileInputStream(
					System.getProperty("user.dir") + "/src/main/resources/Data.properties");
			dataProp.load(fis1);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void logout(){
		driver.findElement(By.linkText("Sign out")).click();
		WaitHandler.waitForPageLoaded(driver);
		driver.close();
		driver.quit();
	}
	
	public void login(){
		openBrowser();
		navigate("amazon_url");
		click("signin_button_id");
		input("email_field_id", "username");
		input("password_field_id", "password");
		click("signinwith_credentials_button_id");
	}

	public void openBrowser() {
		String browserType = prop.getProperty("browser_type");
		if (browserType.equalsIgnoreCase("mozilla")) {
			System.setProperty("webdriver.gecko.driver", "geckodriver.exe");
			driver = new FirefoxDriver();
			driver.manage().window().maximize();
		} else if (browserType.equalsIgnoreCase("chrome")) {
			System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
			ChromeOptions options = new ChromeOptions();
			options.addArguments("--start-maximized");
			options.addArguments("disable-infobars");
			driver = new ChromeDriver(options);

		} else if (browserType.equalsIgnoreCase("ie")) {
			System.setProperty("webdriver.ie.driver", "IEDriverServer.exe");
			driver = new InternetExplorerDriver();
		}
		wait = new WebDriverWait(driver, 100);
	}

	public void navigate(String name) {
		String url = prop.getProperty(name);
		driver.navigate().to(url);
	}

	public void click(String locatorKey) {
		click(locatorKey, false);
	}
	
	public void click(String locatorKey, boolean scroll) {
		WebElement e = getElement(locatorKey, true);
		if(scroll){
			((JavascriptExecutor)driver).executeScript("arguments[0].scrollIntoView()", e);
		}
		e.click();
		WaitHandler.waitForPageLoaded(driver);
	}
	
	public void clickWithJs(String locatorKey) {
		WebElement e = getElement(locatorKey);
		((JavascriptExecutor)driver).executeScript("arguments[0].click()", e);
		WaitHandler.waitForPageLoaded(driver);
	}

	public void input(String locatorKey, String data) {
		WebElement e = getElement(locatorKey);
		data = dataProp.getProperty(data);
		e.sendKeys(data);
	}

	public void write(String locatorKey, String data) {
		WebElement e = getElement(locatorKey);
		e.sendKeys(data);
	}

	public WebElement getElement(String locatorKey) {
		WebElement e = null;
		System.out.println(prop.getProperty(locatorKey));
		try {
			if (locatorKey.endsWith("_id")){
				WaitHandler.waitForElementVisibility(driver, By.id(prop.getProperty(locatorKey)));
				e = driver.findElement(By.id(prop.getProperty(locatorKey)));
			}
			else if (locatorKey.endsWith("_xpath")){
				WaitHandler.waitForElementVisibility(driver, By.xpath(prop.getProperty(locatorKey)));
				e = driver.findElement(By.xpath(prop.getProperty(locatorKey)));
			}
			else if (locatorKey.endsWith("_name")){
				WaitHandler.waitForElementVisibility(driver, By.name(prop.getProperty(locatorKey)));
				e = driver.findElement(By.name(prop.getProperty(locatorKey)));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println(("Failure in Element Extraction - " + locatorKey));
		}
		return e;
	}
	
	public WebElement getElement(String locatorKey, boolean click) {
		WebElement e = null;
		System.out.println(prop.getProperty(locatorKey));
		try {
			if (locatorKey.endsWith("_id")){
				WaitHandler.waitForElementClickable(driver, By.id(prop.getProperty(locatorKey)));
				e = driver.findElement(By.id(prop.getProperty(locatorKey)));
			}
			else if (locatorKey.endsWith("_xpath")){
				WaitHandler.waitForElementClickable(driver, By.xpath(prop.getProperty(locatorKey)));
				e = driver.findElement(By.xpath(prop.getProperty(locatorKey)));
			}
			else if (locatorKey.endsWith("_name")){
				WaitHandler.waitForElementClickable(driver, By.name(prop.getProperty(locatorKey)));
				e = driver.findElement(By.name(prop.getProperty(locatorKey)));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println(("Failure in Element Extraction - " + locatorKey));
		}
		return e;
	}

	public String[] getAuthors() {
		String[] authorList = dataProp.getProperty("authors").split(",");
		return authorList;
	}

	public String[] getTitles() {
		String[] titleList = dataProp.getProperty("titles").split(",");
		return titleList;
	}
	
	public String[] getDesc() {
		String[] descList = dataProp.getProperty("descriptions").split(",");
		return descList;
	}

	public boolean isElementPresent(String xpath) {
		try {
			driver.findElement(By.xpath(xpath));
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public boolean isElementVisible(String xpath) {
		return driver.findElement(By.xpath(xpath)).isDisplayed();
	}

	public void setCatagory() {
		findElmt(By.xpath("//*[@id='data-categories-button-proto-announce']"), true).click();;
		findElmt(By.xpath("//a[text()='Fiction']"), true).click();
		int counter = 0;
		while (counter <= 5) {
			System.out.println("romance present:?  " + isElementPresent("//a[text()='Romance']") + counter);
			if (!isElementPresent("//a[text()='Romance']")) {
				System.out.println("clicking on fiction again");
				driver.findElement(By.xpath("//a[text()='Fiction']")).click();
				WaitHandler.sleep(4);
				counter++;
			} else if(!isElementVisible("//a[text()='Romance']")){
				driver.findElement(By.xpath("//a[text()='Fiction']")).click();
				WaitHandler.sleep(4);
				counter++;
			}
			else {
				break;
			}
		}
		findElmt(By.xpath("//a[text()='Romance']")).click();
		counter = 0;
		WaitHandler.sleep(4);
		while (counter <= 5) {
			System.out.println("general chk box present:?  "
					+ isElementPresent("//*[@id='checkbox-fiction_romance_general']") + counter);
			if (!isElementPresent("//*[@id='checkbox-fiction_romance_general']")) {
				driver.findElement(By.xpath("//a[text()='Romance']")).click();
				WaitHandler.sleep(4);
				counter++;
			} else if(!isElementVisible("//*[@id='checkbox-fiction_romance_general']")){
				driver.findElement(By.xpath("//a[text()='Romance']")).click();
				WaitHandler.sleep(4);
				counter++;
			}
			else {
				break;
			}
		}

		findElmt(By.xpath("//*[@id='checkbox-fiction_romance_general']")).click();
		WaitHandler.sleep(3);
		findElmt(By.xpath("//span[@id='category-chooser-ok-button']")).click();
		WaitHandler.sleep(5);
		WaitHandler.waitForElementClickable(driver, By.xpath("//*[@id='save-and-continue-announce']"));
	}
	
	private WebElement findElmt(By by){
		WaitHandler.waitForElementPresence(driver, by);
		return driver.findElement(by);
	}
	
	private WebElement findElmt(By by, boolean click){
		WaitHandler.waitForElementClickable(driver, by);
		return driver.findElement(by);
	}

	public void upload(String text, int i) throws AWTException {
		StringSelection stringSelection = null;
		if (text.equalsIgnoreCase("pic")) {
			stringSelection = new StringSelection(
					dataProp.getProperty("imagepath") + i + dataProp.getProperty("imagetype"));
		} else if (text.equalsIgnoreCase("book")) {
			System.out.println("Selecting a book");
			stringSelection = new StringSelection(
					dataProp.getProperty("bookpath") + i + dataProp.getProperty("booktype"));
		}
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
		Robot robot = new Robot();
		WaitHandler.sleep(3);
		robot.keyPress(KeyEvent.VK_CONTROL);
		robot.keyPress(KeyEvent.VK_V);
		robot.keyRelease(KeyEvent.VK_V);
		robot.keyRelease(KeyEvent.VK_CONTROL);
		WaitHandler.sleep(3);
		robot.keyPress(KeyEvent.VK_ENTER);
		robot.keyRelease(KeyEvent.VK_ENTER);
		WaitHandler.sleep(3);
	}

	public void waitforElement(String xpathExpression) throws InterruptedException {
		WaitHandler.waitForElementVisibility(driver, By.xpath(xpathExpression));
		WaitHandler.sleep(3);
	}

	public void waitforElementAbsence(String xpathExpression) throws InterruptedException {
		WaitHandler.waitForElementNotVisible(driver, By.xpath(xpathExpression));
		WaitHandler.sleep(3);
	}

	public int getNoofAuthors() {
		String n = dataProp.getProperty("noofauthors");
		int noofAuthors = Integer.parseInt(n);
		return noofAuthors;
	}

	public int getTotWindowHandles() {
		return driver.getWindowHandles().size();
	}

}
