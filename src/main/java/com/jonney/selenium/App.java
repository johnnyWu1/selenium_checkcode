package com.jonney.selenium;

import static com.jonney.selenium.util.SeleniumHelpers.captureElement;
import static com.jonney.selenium.util.SeleniumHelpers.waitForAlert;
import static com.jonney.selenium.util.SeleniumHelpers.waitForElementPresent;
import static com.jonney.selenium.util.SeleniumHelpers.waitForElementVisible;
import static com.jonney.selenium.util.SeleniumHelpers.waitPageRefresh;

import java.io.File;
import java.util.List;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

import com.jonney.selenium.util.ImageFilter;
import com.jonney.selenium.util.OCR;

/**
 * Hello world!
 *
 */
public class App {
	public static void main(String[] args) throws Exception {
		System.setProperty("webdriver.chrome.driver",
				"C:/Users/wyq/AppData/Local/Google/Chrome/Application/chromedriver.exe");
		// System.setProperty("webdriver.firefox.profile",
		// "C:/Users/wyq/AppData/Local/Google/Chrome/Application/chromedriver.exe");
		// D:\Programs\phantom\bin
		WebDriver driver = new ChromeDriver();
		System.setProperty("phantomjs.binary.path",
				"D:/Programs/phantom/bin/phantomjs.exe");
//		PhantomJSDriver driver = new PhantomJSDriver();
////		driver.
//		Object o = driver.executePhantomJS("phantom.abcde=require('webpage').create();return phantom.abcde;");//'http://www.baidu.com'
//		System.out.println(o);
//		Thread.sleep(9000);
//		System.out.println(driver.executePhantomJS("return phantom.page.content;"));
//		o = driver.executePhantomJS("console.log('on:'+(phantom.page.onConsoleMessage=function(msg){phantom.console.log(msg);}));return phantom.page;");
//		
//		
//		System.out.println(o);
//		driver.get("file:///"+new File("show.html").getAbsolutePath());
//		o = driver.executePhantomJS("console.log('on:'+(phantom.page.onConsoleMessage));return phantom.page;");
//		System.out.println(o);
////		System.out.println(driver.getPageSource());
//		Thread.sleep(4000);
//		System.exit(0);
		OCR ocr = new OCR();

		String url = "http://10.8.9.49";
		String xh = "201340922108";
		String pass = "wu950429";

		// int n = 10;
		// while (n-- > 0) {

		driver.get(url);
		boolean isSuccess;
		do {
			waitForElementVisible(driver, By.cssSelector("#txtUserName"), 3);

			WebElement txtXh = driver.findElement(By.id("txtUserName"));
			txtXh.clear();
			txtXh.sendKeys(xh);
			WebElement txtPass = driver.findElement(By.id("TextBox2"));
			txtPass.clear();
			txtPass.sendKeys(pass);
			File yzmImg = captureElement(driver.findElement(By.id("icode")));
			String recognizeText = ocr.recognizeText(new ImageFilter(yzmImg).filterColor(0xff0000cc,150).getImage()).trim()
					.replaceAll("\\s", "");
			WebElement txtSCode = driver.findElement(By.id("txtSecretCode"));
			txtSCode.clear();
			txtSCode.sendKeys(recognizeText);
			System.out.println(recognizeText);
			driver.findElement(By.id("Button1")).click();
			isSuccess = true;
			Alert alert = waitForAlert(driver, 3);
			if (alert != null) {
				System.out.println("Alert: " + alert.getText());
				alert.accept();
				isSuccess = false;
			}
			if (isSuccess) {
				if (!waitForElementPresent(driver,
						By.cssSelector("li.top:nth-child(1) > a:nth-child(1) > span:nth-child(1)"), 3)) {
					driver.get("http://10.8.9.49/");
					isSuccess = false;
				}
			}
		} while (!isSuccess);
		// }

		Actions action = new Actions(driver);
		// li.top:nth-child(5) > ul:nth-child(2) > li:nth-child(8) >
		// a:nth-child(1)
		WebElement nav = driver.findElement(By.cssSelector("li.top:nth-child(5) > a:nth-child(1)"));
		action.moveToElement(nav, 2, 2).build().perform();

		nav = driver.findElement(By.xpath("//*[@id='headDiv']/ul/li[5]//a[text()='成绩查询']"));

		action.moveToElement(nav, 2, 2).click().build().perform();

		// driver.findElement(By.cssSelector("li.top:nth-child(5) >
		// ul:nth-child(2) > li:nth-child(8) > a:nth-child(1)")).click();
		waitForElementPresent(driver, By.cssSelector("#iframeautoheight"), 3);
		driver = driver.switchTo().frame("iframeautoheight");
		waitPageRefresh(driver, 3);

		if (!waitForElementPresent(driver, By.cssSelector("#ddlXN"), 20)) {
			System.out.println(driver.getPageSource());
			System.exit(0);
		}
		Select xn = new Select(driver.findElement(By.cssSelector("#ddlXN")));

		List<WebElement> options = xn.getOptions();

		for (int i = 1; i < options.size(); i++) {
			xn = new Select(driver.findElement(By.cssSelector("#ddlXN")));
			xn.selectByIndex(i);
			options = xn.getOptions();
			System.out.println(options.get(i).getText());
			driver.findElement(By.xpath("//*[@id='btn_xn']")).click();
			waitPageRefresh(driver, 20);
			Thread.sleep(1000);

			List<WebElement> trs = driver.findElements(By.xpath("//*[@id='Datagrid1']/tbody/tr"));
			System.out.println("size:" + trs.size());
			for (WebElement webElement : trs) {
				// *[@id="Datagrid1"]/tbody/tr[2]/td[4]

				String keName = webElement.findElement(By.xpath("td[4]")).getText();
				String score = webElement.findElement(By.xpath("td[13]")).getText();
				System.out.println(String.format("%s:\t%s", keName, score));
			}

		}

		driver.quit();
		// Object r = ((JavascriptExecutor)driver).executeScript("return
		// arguments", "bb","dd");
		// System.out.println(r);
	}

}
