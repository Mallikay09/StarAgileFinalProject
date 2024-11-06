package com.Module1.Login;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.annotations.DataProvider;

public class ExcelUtility {
	private XSSFWorkbook wb;
	 private FileInputStream fs;
	 private File f1;
	@DataProvider(name="excelLoginData")
	public Object[][] excelData()
	{
		//Locate the excel file path using file class
		 f1=new File(System.getProperty("user.dir")+"//TestData//Data.xlsx");
		 Object arr[][]=null;
		try {
			 fs = new FileInputStream(f1);
			 //Workbook
			  wb=new XSSFWorkbook(fs);			  
			  String uName1=wb.getSheet("userData").getRow(1).getCell(0).getStringCellValue();
			  System.out.println("User name: "+uName1);			  
			  int rows=wb.getSheet("userData").getPhysicalNumberOfRows();
			  System.out.println("No. of Rows: "+rows);			  
			  int cells=wb.getSheet("userData").getRow(0).getPhysicalNumberOfCells();
			  System.out.println("No. of Columns: "+cells);			  
			  arr=new Object[rows-1][cells];			  
			  for(int i=1;i<rows;i++)//to skip heading i starts with 1
			  {
				  for(int j=0;j<cells;j++)
				  {
					  arr[i-1][j]=wb.getSheet("userData").getRow(i).getCell(j).getStringCellValue();
					  System.out.print(arr[i-1][j]+"   ");
				  }
				  System.out.println();  
			  }    
			   
		} catch (IOException e) {
			e.printStackTrace();
		}
                
		return arr;
	} 
	
	public void closeWorkbook() {
		if (wb != null) {
	        try {
	            wb.close();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    } else {
	        System.out.println("Workbook is already null or was never initialized.");
	    }
    }
}
