package com.jonney.selenium.util;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.WrapsDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SeleniumHelpers {
	public static boolean waitForElementVisible(WebDriver driver, final By by, int timeout) {
		try {
			new WebDriverWait(driver, timeout).until(ExpectedConditions.visibilityOfElementLocated(by));
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public static Alert waitForAlert(WebDriver driver, int timeout) {

		try {
			return new WebDriverWait(driver, timeout).until(ExpectedConditions.alertIsPresent());
		} catch (Exception e) {
			return null;
		}

	}

	// 页面元素截图
	public static File captureElement(WebElement element) throws Exception {
		WrapsDriver wrapsDriver = (WrapsDriver) element;
		// 截图整个页面
		File screen = ((TakesScreenshot) wrapsDriver.getWrappedDriver()).getScreenshotAs(OutputType.FILE);
		BufferedImage img = ImageIO.read(screen);
		// 获得元素的高度和宽度
		int width = element.getSize().getWidth();
		int height = element.getSize().getHeight();
		// 创建一个矩形使用上面的高度，和宽度
		// 得到元素的坐标
		Point p = element.getLocation();
		BufferedImage dest = img.getSubimage(p.getX(), p.getY(), width, height);
		// 存为png格式
		ImageIO.write(dest, "png", screen);
		return screen;
	}

	public static boolean waitForElementPresent(WebDriver driver, final By by, int timeout) {
		try {
			new WebDriverWait(driver, timeout).until(ExpectedConditions.presenceOfElementLocated(by));
			return true;
		} catch (Exception e) {
			return false;
		}

	}

	public static boolean waitPageRefresh(WebDriver driver, int timeout) {
		try {
			new WebDriverWait(driver, timeout).until(ExpectedConditions.refreshed(null));
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	
	
}
