package com.jonney.selenium.util;

import java.awt.image.BufferedImage;
import java.io.File;

import net.sourceforge.tess4j.Tesseract;

public class OCR {
	static Tesseract instance = null;
	static{
		instance = new Tesseract(); 
		instance.setDatapath(new File(OCR.class.getClassLoader().getResource("tessdata").getFile()).getAbsolutePath());
//		instance.setDatapath(new File("tessdata").getAbsolutePath());
		instance.setLanguage("eng");
//		instance.setOcrEngineMode();
		instance.setPageSegMode(7);
		instance.setTessVariable("tessedit_char_whitelist", "0123456789QWERTYUIOPASDFGHJKLZXCVBNMqwertyuiopasdfghjklzxcvbnm");
	}
	public static String recognizeText(BufferedImage image) throws Exception {
		return instance.doOCR(image);
	}
	
	public static String recognizeText(File image) throws Exception {
		return instance.doOCR(image);
	}
	
	public static void main(String[] args) throws Exception {
		System.out.println(recognizeText(new File("abc.png")));
	}

}
