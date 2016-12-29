package com.jonney.selenium.util;

import java.awt.image.BufferedImage;
import java.io.File;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.lf5.util.StreamUtils;

public class OCR2 {
	static private final String LANG_OPTION = "-l";  //英文字母小写l，并非数字1  
//	static private final String EOL = System.getProperty("line.separator");  
	static private String tessPath = "D://Programs//Tesseract-OCR";  
    //private String tessPath = new File("tesseract").getAbsolutePath();  
    static{
//    	System.out.println(System.getProperty("TESSDATA_PREFIX"));
//    	System.out.println(System.getenv("TESSDATA_PREFIX"));
//    	System.out.println(System.setProperty("TESSDATA_PREFIX", "D:\\Programs\\Tesseract-OCR\\"));
    }
    public static String recognizeText(File imageFile)throws Exception{  
        File outputFile = new File(imageFile.getParentFile(),"output");  
        ProcessBuilder pb = new ProcessBuilder();  
        pb.directory(imageFile.getParentFile());  
        pb.environment().put("TESSDATA_PREFIX", new File(OCR2.class.getClassLoader().getResource("tessdata").getFile()).getParent());
        pb.environment().put("PATH", tessPath);
        pb.command("tesseract",imageFile.getAbsolutePath(),outputFile.getAbsolutePath(),LANG_OPTION,"eng","-psm","7","digits");  
        pb.redirectErrorStream(true);  
        Process process = pb.start();  
        //tesseract.exe 1.jpg 1 -l chi_sim  
        int w = process.waitFor();  
        String msg;
        if(w==0){  
        	msg = FileUtils.readFileToString(new File(outputFile.getAbsolutePath()+".txt"), "utf-8");
        	new File(outputFile.getAbsolutePath()+".txt").delete();  
        }else{  
            switch(w){  
                case 1:  
                    msg = "Errors accessing files.There may be spaces in your image's filename.";  
                    break;  
                case 29:  
                    msg = "Cannot recongnize the image or its selected region.";  
                    break;  
                case 31:  
                    msg = "Unsupported image format.";  
                    break;  
                default:  
                    msg = "Errors occurred.";  
            }  
        }  
        return msg;  
    }  
    
	public static void main(String[] args) throws Exception {
		
		System.out.println("code: "+recognizeText(new File("abc.png")));
	}

	public String recognizeText(BufferedImage image) throws Exception {
		return recognizeText(ImageIOHelper.createPngImage(image));
	}

}
