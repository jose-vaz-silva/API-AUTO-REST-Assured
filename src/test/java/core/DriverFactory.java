package core;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class DriverFactory {
	private DriverFactory() {
	};

	private static WebDriver driver;

	private static ChromeOptions createChromeOptions() {
		ChromeOptions options = new ChromeOptions();
//		options.addArguments("--disable-extensions");
		options.addArguments("--disable-notifications");
		options.addArguments("--disable-infobars");
		options.addArguments("--start-maximized");
		options.addArguments("--no-sandbox");
		options.addArguments("disable-gpu");
		options.addArguments("--ignore-ssl-errors=yes");
		options.addArguments("--ignore-certificate-errors");
		options.addArguments("window-size=1920x1080");
		String runHeadless = System.getProperty("runHeadless");
		if (runHeadless == null) {
			System.setProperty("runHeadless", "false");
		} else {
			if (runHeadless.equalsIgnoreCase("true")) {
				options.addArguments("--headless");
			}
		}

		HashMap<String, Object> chromePrefs = new HashMap<>();
		if (System.getProperty("remote") == null) {
			chromePrefs.put("profile.default_content_settings.popups", 0);
			chromePrefs.put("download.default_directory", System.getProperty("user.dir") + File.separator + "data");
			chromePrefs.put("plugins.always_open_pdf_externally", false);
		}
		chromePrefs.put("credentials_enable_service", false);
		chromePrefs.put("profile.password_manager_enabled", false);
		options.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
		options.setExperimentalOption("prefs", chromePrefs);
		return options;
	}

	private static void setOperationalSystemAndSetWebDriver(String driverName) {
		String OS = System.getProperty("os.name").toLowerCase();
		String driverPath = System.getProperty("driverPath");
		if (driverPath != null) {
			System.setProperty("webdriver.chrome.driver", driverPath);
		} else {
			if (OS.contains("windows")) {
				driverPath = System.getProperty("user.dir") + File.separator + "driver" + File.separator + driverName
						+ ".exe";
				System.setProperty("webdriver.chrome.driver", driverPath);
			} else {
				driverPath = System.getProperty("user.dir") + File.separator + "driver" + File.separator + driverName;
				System.setProperty("webdriver.chrome.driver", driverPath);
			}
		}
	}

	public static WebDriver getDriver() {
		if (driver == null || driver.toString().toLowerCase().contains("null")) {
			setOperationalSystemAndSetWebDriver("chromedriver90");
			ChromeOptions options = createChromeOptions();
			driver = new ChromeDriver(options);
			driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
			driver.manage().window().maximize();
		}
		return driver;
	}

	public static void killDriver() {
		if (driver != null) {
			driver.quit();
			driver = null;
		}
	}

}