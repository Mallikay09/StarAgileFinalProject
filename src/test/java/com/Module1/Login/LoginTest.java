package com.Module1.Login;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class LoginTest {
	
	WebDriver driver;
	ExcelUtility excelData;
	ExtentReports extent;
    ExtentTest test;
    
	@BeforeClass
	public void setUp() {
		excelData= new ExcelUtility();
		driver = new ChromeDriver();
		driver.get("https://opensource-demo.orangehrmlive.com/web/index.php/auth/login");
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		driver.manage().window().maximize();
		ExtentSparkReporter sparkReporter  = new ExtentSparkReporter("extentReports/OrangeHRMReport.html");
        extent = new ExtentReports();
        extent.attachReporter(sparkReporter);
	}
	
	@Test(dataProvider = "excelLoginData", dataProviderClass = ExcelUtility.class)
	public void loginTest(String username, String password) {
		test = extent.createTest("Login Test with username: " + username);
		driver.findElement(By.name("username")).sendKeys(username);
		driver.findElement(By.name("password")).sendKeys(password);
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
		driver.findElement(By.xpath("//button[normalize-space(text()='Login')]")).click();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
		
		// Assertion and validation
		  if (username.equals("Admin") && password.equals("admin123")) {
		  Assert.assertEquals(driver.getCurrentUrl(),"https://opensource-demo.orangehrmlive.com/web/index.php/dashboard/index");
		  WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
		  wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h6[text()='Dashboard']")));
		  takeScreenshot("LoginAttempt_" + username);
		  test.pass("Valid credentials - Login successful.");
		  logout();		  
		  }
		  else {
		  WebElement errorMessage =
		  driver.findElement(By.xpath("//p[text()='Invalid credentials']"));
		  Assert.assertTrue(errorMessage.isDisplayed(), "Invalid credentials");
		  driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
		  takeScreenshot("LoginAttempt_" + username);
		  test.fail("Invalid credentials - Login failed as expected."); 
		  }
        }
	
	public void logout() {
		driver.findElement(By.xpath("//span[@class='oxd-userdropdown-tab']")).click();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		driver.findElement(By.xpath("//li//a[text()='Logout']")).click();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }
	        
    public void takeScreenshot(String testName) {
    	File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		String filePath = "Screenshots/" + testName + "_" + timestamp + ".png";
		try {
		            FileUtils.copyFile(screenshot, new File(filePath));
		            test.addScreenCaptureFromPath(filePath);
		    } catch (IOException e) {
		            e.printStackTrace();
		        }
		    }
    
	  @AfterClass
	   public void tearDown() {
	    	excelData.closeWorkbook();
		    if (driver != null) {
		            driver.quit();
		        }
		        extent.flush();
	  }
}
