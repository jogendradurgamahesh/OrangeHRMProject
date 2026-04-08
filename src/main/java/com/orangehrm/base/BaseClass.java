package com.orangehrm.base;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.ITestContext;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Parameters;
import org.testng.asserts.SoftAssert;

import com.orangehrm.actiondriver.ActionDriver;
import com.orangehrm.utilities.ExtentManager;
import com.orangehrm.utilities.LoggerManager;

public class BaseClass {

	protected static Properties prop;
	
//	protected static WebDriver driver;
//	private static ActionDriver actionDriver;
	
	private static ThreadLocal<WebDriver>driver=new ThreadLocal<>();
	private static ThreadLocal<ActionDriver>actionDriver=new ThreadLocal<>();
	public static final Logger logger=LoggerManager.getLogger(BaseClass.class);

	protected ThreadLocal<SoftAssert>softAssert=ThreadLocal.withInitial(SoftAssert::new);
	
	
	public SoftAssert getSoftAssert() {
		return softAssert.get(); 
	}
	
	

	@BeforeSuite
	//load the configuration file
	public  void loadconfig() throws IOException {

		prop=new Properties();
		FileInputStream fis=new FileInputStream(System.getProperty("user.dir")+ "/src/main/resources/config.properties");
		prop.load(fis);
		logger.info("config.prop is loaded"); 
		
		//Start the extent Report
		//ExtentManager.getReporter(); //implemented on testListener  
	}
	

	
	@BeforeMethod
	//public synchronized void setup(Method method, ITestContext context) throws IOException {
	@Parameters("browser")
	public synchronized void setup(String browser) throws IOException {
		System.out.println("Setting up webdriver for: "+this.getClass().getSimpleName());
		launchBrowser(browser);
		configureBrowser();
		staticWait(2);
		
		logger.info("Webdriver initialized and Browser is maxmized ");
		logger.trace("Trace msg");
		logger.error("Error msg");
		logger.debug("Debug msg");
		logger.fatal("Fatal a msg");
		logger.warn("Warn msg");
		
/*		//inititlaize actionDriver only once-->Singleton pattern
		if(actionDriver==null) {
			actionDriver= new ActionDriver(driver);
//			System.out.println("ActionDriver instance is created");
			logger.info("ActionDriver instance is created. "+Thread.currentThread().getId());
		}
	}   */
		
		 // attach driver to current test thread
	    //context.setAttribute("driver", getDriver());
		
		//initialize actiondriver for the current thread
		actionDriver.set(new ActionDriver(getDriver()));
		logger.info("ActionDriver initlialized for thread: " + Thread.currentThread().getId());
	}
		

	//initialize the webdriver based on browser defined in config properties
	private synchronized void launchBrowser(String browser) {

		//String browser=prop.getProperty("browser");
		
		//boolean seleniumGrid = Boolean.parseBoolean(prop.getProperty("seleniumGrid"));
		boolean seleniumGrid = Boolean.parseBoolean(
			    System.getProperty("seleniumGrid", prop.getProperty("seleniumGrid"))
			);
		String gridURL = prop.getProperty("gridURL");
		
		if (seleniumGrid) {
		    try {
		        if (browser.equalsIgnoreCase("chrome")) {
		            ChromeOptions options = new ChromeOptions();
		            //options.addArguments("--headless", "--disable-gpu", "--window-size=1920,1080");
		            options.addArguments("--start-maximized", "--disable-gpu", "--window-size=1920,1080");
		            driver.set(new RemoteWebDriver(new URL(gridURL), options));
		        } else if (browser.equalsIgnoreCase("firefox")) {
		            FirefoxOptions options = new FirefoxOptions();
		            options.addArguments("-headless");
		            driver.set(new RemoteWebDriver(new URL(gridURL), options));
		        } else if (browser.equalsIgnoreCase("edge")) {
		            EdgeOptions options = new EdgeOptions();
		         //   options.addArguments("--headless=new", "--disable-gpu","--no-sandbox","--disable-dev-shm-usage");
		            options.addArguments(
		            	    "--no-sandbox",
		            	    "--disable-dev-shm-usage",
		            	    "--disable-gpu",
		            	    "--window-size=1920,1080"
		            	);
		            driver.set(new RemoteWebDriver(new URL(gridURL), options));
		        } else {
		            throw new IllegalArgumentException("Browser Not Supported: " + browser);
		        }
		        logger.info("RemoteWebDriver instance created for Grid in headless mode");
		    } catch (MalformedURLException e) {
		        throw new RuntimeException("Invalid Grid URL", e);
		    }
		}
		
		else {

		if(browser.equalsIgnoreCase("chrome")) {
			
			// Create ChromeOptions
			ChromeOptions options = new ChromeOptions();
			options.addArguments("--headless"); // Run Chrome in headless mode
			options.addArguments("--disable-gpu"); // Disable GPU for headless mode
			//options.addArguments("--window-size=1920,1080"); // Set window size
			options.addArguments("--disable-notifications"); // Disable browser notifications
			options.addArguments("--no-sandbox"); // Required for some CI environments like Jenkins
			options.addArguments("--disable-dev-shm-usage"); // Resolve issues in resource-limited environments
			
			//driver=new ChromeDriver();
			driver.set(new  ChromeDriver(options)); //new changes as per thread
			ExtentManager.registerDriver(getDriver());//from ExtentManager
			logger.info("chrome driver instance is created");
		}
		else if(browser.equalsIgnoreCase("firefox")){
			
			// Create FirefoxOptions
			FirefoxOptions options = new FirefoxOptions();
			options.addArguments("--headless"); // Run Firefox in headless mode
			options.addArguments("--disable-gpu"); // Disable GPU rendering (useful for headless mode)
			options.addArguments("--width=1920"); // Set browser width
			options.addArguments("--height=1080"); // Set browser height
			options.addArguments("--disable-notifications"); // Disable browser notifications
			options.addArguments("--no-sandbox"); // Needed for CI/CD environments
			options.addArguments("--disable-dev-shm-usage"); // Prevent crashes in low-resource environments
			
			//driver=new FirefoxDriver();
			driver.set(new  FirefoxDriver(options)); //new changes as per thread
			ExtentManager.registerDriver(getDriver());
			logger.info(" Firefox driver instance is created");
		}
		else if(browser.equalsIgnoreCase("edge")) {
			
			EdgeOptions options = new EdgeOptions();
			options.addArguments("--headless"); // Run Edge in headless mode
			options.addArguments("--disable-gpu"); // Disable GPU acceleration
			options.addArguments("--window-size=1920,1080"); // Set window size
			options.addArguments("--disable-notifications"); // Disable pop-up notifications
			options.addArguments("--no-sandbox"); // Needed for CI/CD
			options.addArguments("--disable-dev-shm-usage"); // Prevent resource-limited crashes
			
			//driver=new EdgeDriver();
			driver.set(new  EdgeDriver(options)); //new changes as per thread
			ExtentManager.registerDriver(getDriver());
			logger.info("Edge driver instance is created");
		}
		else {
			throw new IllegalArgumentException("browser not supported "+browser);
		}

	}

	}
	//configure browser setting like implicir wait,maximize the browser and navigate to url
	private void configureBrowser() {
		//implicit wait
		int implicitWait=Integer.parseInt(prop.getProperty("implicitWait"));
//		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWait));
		boolean seleniumGrid = Boolean.parseBoolean(System.getProperty("seleniumGrid", prop.getProperty("seleniumGrid")));
		getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWait)); //we can use getDriver() or driver.get()

		//maximize the window
		//getDriver().manage().window().maximize();  //we can use getDriver() or driver.get()

		
		if (seleniumGrid) {
			getDriver().get(prop.getProperty("url_grid"));
		} else {
			getDriver().get(prop.getProperty("url"));
		}
		
		System.out.println("Running on Grid: " + seleniumGrid);
		System.out.println("Opening URL: " + prop.getProperty("url_grid"));
		
		//navigate to url
//		String url=prop.getProperty("url");
//		try {
//			getDriver().get(url); ////we can use getDriver() or driver.get()
//		} catch (Exception e) {
//		System.out.println("Failed to navigate "+e.getMessage());
//		}

	}


//
//	@BeforeMethod
//	public void setup() throws IOException {
		//		//load the configuration file
		//		prop=new Properties();
		//		FileInputStream fis=new FileInputStream("src\\main\\resources\\config.properties");
		//		prop.load(fis);


		//initialize the webdriver based on browser defined in config properties
		//		String browser=prop.getProperty("browser");
		//
		//		if(browser.equalsIgnoreCase("chrome")) {
		//			driver=new ChromeDriver();
		//		}
		//		else if(browser.equalsIgnoreCase("firefox")){
		//			driver=new FirefoxDriver();
		//		}
		//		else if(browser.equalsIgnoreCase("edge")) {
		//			driver=new EdgeDriver();
		//		}
		//		else {
		//			throw new IllegalArgumentException("browser not supported "+browser);
		//		}


		//		//implicit wait
		//		int implicitWait=Integer.parseInt(prop.getProperty("implicitWait"));
		//		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWait));
		//
		//		//maximize the window
		//		driver.manage().window().maximize();
		//
		//		//navigate to url
		//		String url=prop.getProperty("url");
		//		driver.get(url);



//	}


	@AfterMethod
	public synchronized void tearDown() {
		if(getDriver()!=null) {
			try {
				getDriver().quit();
			} catch (Exception e) {
				System.out.println("Unable to quit driver "+e.getMessage());
			}
		}
		logger.info("Webdriver instance is closed");
//		driver=null;
//		actionDriver=null;
		driver.remove();
		actionDriver.remove();
		
		//ExtentManager.endTest();   //trigger when suite ends implemented in testListener
	}

	
	//getter method for prop
	public static Properties getProp() {
		return prop;
	}
	
	/*	//Driver getter method
	public WebDriver getDriver() {
		return driver;
	}      */
	
	
	//getter method for WebDriver
	public static WebDriver getDriver() {
		if(driver.get()==null) {
			System.out.println("WebDriver not initialized");
			throw new IllegalStateException("WebDriver not initialized");
		}
		return driver.get();
	}
	
	//getter method for ActionDriver
	public static ActionDriver getActionDriver() {
		if(actionDriver.get()==null) {
			System.out.println("ActionDriver not initialized");
			throw new IllegalStateException("ActionDriver not initialized");
		}
		return actionDriver.get();
	}
	
	//Driver setter method
	public void setDriver(ThreadLocal<WebDriver> driver) {
		this.driver=driver;
	}
	
	
	//static wait for pause
	public void staticWait(int sec) {
		LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(sec));
	}
}

