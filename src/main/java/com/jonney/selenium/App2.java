package com.jonney.selenium;

import static com.jonney.selenium.util.SeleniumHelpers.captureElement;
import static com.jonney.selenium.util.SeleniumHelpers.waitForElementPresent;
import static com.jonney.selenium.util.SeleniumHelpers.waitForElementVisible;
import static com.jonney.selenium.util.SeleniumHelpers.waitPageRefresh;
import java.io.File;
import java.util.Scanner;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.base.Predicate;
import com.jonney.selenium.util.ImageFilter;
import com.jonney.selenium.util.OCR;

public class App2 {
	
	static int cs_count = 0;
	static int success_count = 0;

	public static void main(String args[]) throws Exception {
		// C:/Program Files (x86)/Google/Chrome/Application/chromedriver.exe
		// C:/Users/wyq/AppData/Local/Google/Chrome/Application/chromedriver.exe
		System.setProperty("webdriver.chrome.driver",
				"C:/Program Files (x86)/Google/Chrome/Application/chromedriver.exe");
		System.setProperty("phantomjs.binary.path", "D:/Programs/phantomjs-2.1.1-windows/bin/phantomjs.exe");
		Scanner input = new Scanner(System.in);
		WebDriver driver = new ChromeDriver();
		PhantomJSDriver pdriver = new PhantomJSDriver();
		driver.get(new File("show.html").getAbsolutePath());
		while (true) {
			int i = 10;
			while (i-- != 0) {
				ImageFilter imageFilter = new ImageFilter(genAndGetYzm2(driver, pdriver));
				imageFilter.filterColor(0xff0000cc, 150);
				File restu = imageFilter.getImageFile();
				showImgJieGuoPic(driver, imageFilter.getImageFile());
				String recognizeText = OCR.recognizeText(restu).trim().replaceAll("\\s", "");
				showCode(driver, recognizeText);
				checkCode(driver, pdriver, recognizeText);
			}
			Thread.sleep(1500);
			// if(input.nextLine().trim().equalsIgnoreCase("exit")){
			// break;
			// driver.quit();
			// }
		}
		// imageFilter.grayFilter();
		// showImg(driver,"转换为黑白灰度图 ",imageFilter.getImageFile());
		//
		// imageFilter.lineGrey();
		// showImg(driver,"线性灰度变换",imageFilter.getImageFile());

		// imageFilter.sharp();
		// showImg(driver,"提升清晰度,进行锐化 ",imageFilter.getImageFile());

		// imageFilter.median();
		// showImg(driver,"中值滤波",imageFilter.getImageFile());
		//
		// imageFilter.changeGrey();
		// showImg(driver,"图像二值化 ",imageFilter.getImageFile());
	}

	private static void checkCode(WebDriver driver, PhantomJSDriver pdriver, String recognizeText) {
		
		WebElement txtSCode = pdriver.findElement(By.id("txtSecretCode"));
		txtSCode.clear();
		txtSCode.sendKeys(recognizeText);

		pdriver.findElement(By.id("Button1")).click();
		boolean isSuccess = true;
		// Alert alert = waitForAlert(driver, 3);
		// if (alert != null) {
		// System.out.println("Alert: " + alert.getText());
		// alert.accept();
		// isSuccess = false;
		// }
		waitPageRefresh((WebDriver)pdriver,5);
		
		if (pdriver.getPageSource().indexOf("验证码不正确")!=-1&&pdriver.getCurrentUrl().endsWith("default2.aspx")) {
			// driver.get("http://10.8.9.49/");
			isSuccess = false;
		}else{
			success_count++;
		}
		showCount(driver);
		((JavascriptExecutor) driver).executeScript(
				"var tr = document.createElement('tr');" + "var td2 = document.querySelector('.result_hook');"
						+ "td2.innerHTML = arguments[0];" + "td2.className='';",
				isSuccess ? "成功" : "失败");
	}
	
	private static void showCount(WebDriver driver){
		
		((JavascriptExecutor) driver).executeScript(
				"document.querySelector('#sb_count').innerHTML = arguments[0];"
				+ "document.querySelector('#success_count').innerHTML = arguments[1];"
				+ "document.querySelector('#fail_count').innerHTML = arguments[2];"
				+ "document.querySelector('#bfb_count').innerHTML = arguments[3];",
				cs_count,
				success_count,
				cs_count-success_count,
				String .format("%.2f%%",100.0*success_count/cs_count)
				);
		
	}

	private static void showCode(WebDriver driver, String recognizeText) {
		((JavascriptExecutor) driver).executeScript(
				"var tr = document.createElement('tr');" + "var td2 = document.querySelector('.code_hook');"
						+ "td2.innerHTML = arguments[0];" + "td2.className='';",
				recognizeText);
	}

	private static void showImgJieGuoPic(WebDriver driver, File restu) {
		((JavascriptExecutor) driver).executeScript("var tr = document.createElement('tr');"
				+ "var td2 = document.querySelector('.jieguoPic_hook');" + "var img = new Image();"
				+ "img.src=arguments[0];" + "td2.innerHTML='';" + "td2.append(img);" + "td2.className='';",
				"file:///" + restu.getAbsolutePath().replaceAll("\\\\", "/"));
	}

	// private void showImg(WebDriver driver,File yuantu,File jieguotu, String
	// text) {
	// JavascriptExecutor v8 = ((JavascriptExecutor)driver);
	// v8.executeScript("var tr = document.createElement('tr');"
	// + "var td1 = document.createElement('td');"
	// + "td1.innerHTML='<img src=\"'+arguments[0]+' \"/>';"
	// + "var td2 = document.createElement('td');"
	// + "td2.innerHTML='<img src=\"'+arguments[1]+' \"/>';"
	// + "var td3 = document.createElement('td');"
	// + "td3.innerHTML=arguments[2];"
	// + "tr.append(td1);tr.append(td2);tr.append(td3);"
	// + "document.querySelector('#content').appendChild(tr);",
	// "file:///"+yuantu.getAbsolutePath().replaceAll("\\\\", "/"),
	// "file:///"+jieguotu.getAbsolutePath().replaceAll("\\\\", "/"),
	// text
	// );
	// }
	//

	// private static File getYzm(WebDriver driver) throws Exception {
	// File yzmImg = null;
	// String yuanlai = driver.getWindowHandle();
	// JavascriptExecutor oJavaScriptExecutor = (JavascriptExecutor)driver;
	// oJavaScriptExecutor.executeScript("window.open('http://10.8.9.49/CheckCode.aspx');");
	// Set<String> windowHandles = driver.getWindowHandles();
	// for (String string : windowHandles) {
	// if(!string.equals(yuanlai)){
	// driver.switchTo().window(string);
	// new WebDriverWait(driver,
	// 3).until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.tagName("img")));
	// yzmImg = captureElement(driver.findElement(By.tagName("img")));
	// oJavaScriptExecutor.executeScript("window.close();");
	// break;
	// }
	// }
	// driver.switchTo().window(yuanlai);
	// return yzmImg;
	// }

	private static File genAndGetYzm2(final WebDriver driver, PhantomJSDriver pdriver) throws Exception {
		cs_count++;
		showCount(driver);
		File yzmImg = null;

		if (pdriver.getTitle().indexOf("请登录") == -1) {
			pdriver.get("http://10.8.9.49");
		}
		waitForElementVisible(pdriver, By.cssSelector("#txtUserName"), 3);

		WebElement txtXh = pdriver.findElement(By.id("txtUserName"));
		txtXh.clear();
		txtXh.sendKeys("201340922s");
		WebElement txtPass = pdriver.findElement(By.id("TextBox2"));
		txtPass.clear();
		txtPass.sendKeys("556d56s8d");
		yzmImg = captureElement(pdriver.findElement(By.id("icode")));

		final Object img = ((JavascriptExecutor) driver)
				.executeScript("var content =  document.querySelector('#content');" + "var tr;"
						+ "tr = content.childElementCount>10?content.removeChild(content.querySelector('tr')):document.createElement('tr');"
						+ "tr.innerHTML = '';" + "var td1 = document.createElement('td');" + "var img = new Image();"
						+ "img.src=arguments[0];" + "td1.append(img);" + "var td2 = document.createElement('td');"
						+ "td2.className='jieguoPic_hook';" + "td2.innerHTML='处理中...';"
						+ "var td3 = document.createElement('td');" + "td3.className='code_hook';"
						+ "td3.innerHTML='处理中...';" + "var td4 = document.createElement('td');"
						+ "td4.className='result_hook';" + "td4.innerHTML='处理中...';"
						+ "tr.append(td1);tr.append(td2);tr.append(td3);tr.append(td4);" + "content.appendChild(tr);"
						+ "return img;", "file:///" + yzmImg.getAbsolutePath());

		if (img instanceof WebElement) {
			new WebDriverWait(driver, 3).until(new Predicate<WebDriver>() {
				public boolean apply(WebDriver input) {
					return (Boolean) ((JavascriptExecutor) driver).executeScript(
							"return arguments[0].complete && typeof arguments[0].naturalWidth != \"undefined\" && arguments[0].naturalWidth > 0",
							(WebElement) img);
				}
			});
		}
		return yzmImg;
	}

}
