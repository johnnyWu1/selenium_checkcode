package com.jonney.selenium.util;

import static com.jonney.selenium.util.SeleniumHelpers.captureElement;

import java.io.File;
import java.util.Date;
import java.util.Scanner;

import org.junit.Test;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.base.Predicate;

public class ImageFilterTest {

	@Test
	public void test() throws Exception {
		
		System.setProperty("webdriver.chrome.driver", "C:/Users/wyq/AppData/Local/Google/Chrome/Application/chromedriver.exe");
    	Scanner input = new Scanner(System.in);
    	WebDriver driver= new ChromeDriver();
    	driver.get(new File("show.html").getAbsolutePath());
    	
		while(true){
			
	    	int i = 10;
	    	while(i--!=0){
				ImageFilter imageFilter = new ImageFilter(genAndGetYzm2(driver));
				imageFilter.filterColor(0xff0000cc,150);
				File restu = imageFilter.getImageFile();
				showImgJieGuoPic(driver,imageFilter.getImageFile());
				String recognizeText = OCR.recognizeText(restu).trim().replaceAll("\\s", "");
				showCode(driver,recognizeText);
	    	}
	    	Thread.sleep(1500);
//	    	if(input.nextLine().trim().equalsIgnoreCase("exit")){
//	    		break;
//	    		driver.quit();
//	    	}
		}
//		imageFilter.grayFilter();
//		showImg(driver,"转换为黑白灰度图 ",imageFilter.getImageFile());
//		
//		imageFilter.lineGrey();
//		showImg(driver,"线性灰度变换",imageFilter.getImageFile());

//		imageFilter.sharp();
//		showImg(driver,"提升清晰度,进行锐化 ",imageFilter.getImageFile());
		
//		imageFilter.median();
//		showImg(driver,"中值滤波",imageFilter.getImageFile());
//		
//		imageFilter.changeGrey();
//		showImg(driver,"图像二值化 ",imageFilter.getImageFile());

		
		
		
		
		
		
	}

	private void showCode(WebDriver driver, String recognizeText) {
		((JavascriptExecutor)driver).executeScript(
				"var tr = document.createElement('tr');"
				+ "var td2 = document.querySelector('.code_hook');"
				+ "td2.innerHTML = arguments[0];"
				+ "td2.className='';",
				recognizeText
				);  
	}

	private void showImgJieGuoPic(WebDriver driver, File restu) {
		((JavascriptExecutor)driver).executeScript("var tr = document.createElement('tr');"
				+ "var td2 = document.querySelector('.jieguoPic_hook');"
				+ "var img = new Image();"
				+ "img.src=arguments[0];"
				+ "td2.innerHTML='';"
				+ "td2.append(img);"
				+ "td2.className='';",
				"file:///"+restu.getAbsolutePath().replaceAll("\\\\", "/")
				);  
	}

//	private void showImg(WebDriver driver,File yuantu,File jieguotu, String text) {
//		JavascriptExecutor v8 = ((JavascriptExecutor)driver);
//		v8.executeScript("var tr = document.createElement('tr');"
//				+ "var td1 = document.createElement('td');"
//				+ "td1.innerHTML='<img src=\"'+arguments[0]+' \"/>';"
//				+ "var td2 = document.createElement('td');"
//				+ "td2.innerHTML='<img src=\"'+arguments[1]+' \"/>';"
//				+ "var td3 = document.createElement('td');"
//				+ "td3.innerHTML=arguments[2];"
//				+ "tr.append(td1);tr.append(td2);tr.append(td3);"
//				+ "document.querySelector('#content').appendChild(tr);",
//				"file:///"+yuantu.getAbsolutePath().replaceAll("\\\\", "/"),
//				"file:///"+jieguotu.getAbsolutePath().replaceAll("\\\\", "/"),
//				text
//				);
//	}
//	
	
	
//	private static File getYzm(WebDriver driver) throws Exception {
//		File yzmImg = null;
//		String yuanlai = driver.getWindowHandle();
//		JavascriptExecutor oJavaScriptExecutor = (JavascriptExecutor)driver;  
//        oJavaScriptExecutor.executeScript("window.open('http://10.8.9.49/CheckCode.aspx');");  
//        Set<String> windowHandles = driver.getWindowHandles();
//        for (String string : windowHandles) {
//			if(!string.equals(yuanlai)){
//				driver.switchTo().window(string);
//				new WebDriverWait(driver, 3).until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.tagName("img")));
//				yzmImg = captureElement(driver.findElement(By.tagName("img")));
//				oJavaScriptExecutor.executeScript("window.close();");  
//				break;
//			}
//		}
//		driver.switchTo().window(yuanlai);
//		return yzmImg;
//	}
	
	private static File genAndGetYzm2(final WebDriver driver) throws Exception {
		File yzmImg = null;
		final Object img = ((JavascriptExecutor)driver).executeScript(
				"var content =  document.querySelector('#content');"
				+ "var tr;"
				+ "tr = content.childElementCount>5?content.removeChild(content.querySelector('tr')):document.createElement('tr');"
				+ "tr.innerHTML = '';"
				+ "var td1 = document.createElement('td');"
				+ "var img = new Image();"
				+ "img.src=arguments[0];"
				+ "td1.append(img);"
				+ "var td2 = document.createElement('td');"
				+ "td2.className='jieguoPic_hook';"
				+ "td2.innerHTML='处理中...';"
				+ "var td3 = document.createElement('td');"
				+ "td3.className='code_hook';"
				+ "td3.innerHTML='处理中...';"
				+ "tr.append(td1);tr.append(td2);tr.append(td3);"
				+ "content.appendChild(tr);"
				+ "return img;",
				"http://10.8.9.49/CheckCode.aspx?"+new Date().getTime()
				);  
		
		if(img instanceof WebElement){
			new WebDriverWait(driver, 3).until(new Predicate<WebDriver>() {
				@Override
				public boolean apply(WebDriver input) {
					return (Boolean) ((JavascriptExecutor)driver).executeScript("return arguments[0].complete && typeof arguments[0].naturalWidth != \"undefined\" && arguments[0].naturalWidth > 0", (WebElement)img);
				}
			});
			yzmImg = captureElement((WebElement)img);
		}
		return yzmImg;
	}

}
