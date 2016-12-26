package com.jonney.selenium;

import java.io.File;
import java.util.Scanner;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class App2 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.setProperty("webdriver.chrome.driver", "C:/Users/wyq/AppData/Local/Google/Chrome/Application/chromedriver.exe");
    	Scanner input = new Scanner(System.in);
    	WebDriver driver= new ChromeDriver();
    	
    	driver.get("http://10.8.9.49/CheckCode.aspx");
    	
    	System.out.println(driver.getPageSource());
    	
    	input.nextLine();
    	
    	driver.quit();
	}

}
