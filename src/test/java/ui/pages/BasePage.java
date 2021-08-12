package ui.pages;

import static constants.BasePageConstants.CIRCUNFLEJO;
import static constants.BasePageConstants.DOLAR;
import static constants.BasePageConstants.MAX_SECONDS;
import static constants.BasePageConstants.REGEX;
import static core.Hooks.currentStep;

import java.io.File;
import java.io.FilenameFilter;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import constants.TimeOutConstants;
import core.DriverFactory;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.api.java.pt.Dado;
import cucumber.api.java.pt.E;
import cucumber.api.java.pt.Entao;
import cucumber.api.java.pt.Então;
import cucumber.api.java.pt.Quando;

public class BasePage {
	private static WebDriver driver;

	public BasePage() {
		driver = DriverFactory.getDriver();
	}

	public void navigate(String url) throws Exception {
		if (!url.isEmpty())
			DriverFactory.getDriver().navigate().to(url);
		else {
			throw new Exception("Url inválida.");
		}
	}

	public void click(WebElement element) {
		element.click();
	}

	public void quitBrowser() {
		if (driver != null) {
			driver.quit();
			driver = null;
		}
	}

	public void closeBrowser() {
		driver.close();
	}

	public void sendKeys(WebElement element, String value) {
		element.sendKeys(value);
	}

	public void sendKey(WebElement element, Keys key) {
		element.sendKeys(key);
	}

	public static WebElement getWebElement(By by) {
		WebDriverWait wait = new WebDriverWait(driver, MAX_SECONDS);
		WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(by));
		return element;
	}

	public static List<WebElement> getWebElements(By locator) {
		return driver.findElements(locator);
	}

	public static void waitElement(By locator, int timeOut) throws NoSuchElementException {
		try {
			WebDriverWait wait = new WebDriverWait(driver, timeOut);
			wait.until(ExpectedConditions.presenceOfElementLocated(locator));
		} catch (NoSuchElementException ex) {
			throw new NoSuchElementException("No elements found with " + locator + " locator");
		} catch (TimeoutException ex) {
			throw new TimeoutException("Time (" + timeOut + ") exceeded to find element: " + locator);
		}
	}

	public void waitElementToBeClicable(By locator, int timeOut) throws NoSuchElementException {
		try {
			WebDriverWait wait = new WebDriverWait(driver, MAX_SECONDS);
			wait.until(ExpectedConditions.elementToBeClickable(locator));
		} catch (NoSuchElementException ex) {
			throw new NoSuchElementException("No elements found with " + locator + " locator");
		} catch (TimeoutException ex) {
			throw new TimeoutException("Time (" + MAX_SECONDS + ") exceeded to find element: " + locator);
		}
	}

	public static Boolean waitVisibilityPresenceOfElement(By element, Integer waitTime) throws Exception {
		WebDriverWait wait = new WebDriverWait(driver, waitTime);
		wait.until(ExpectedConditions.presenceOfElementLocated(element));
		wait.until(ExpectedConditions.visibilityOfElementLocated(element));
		return isPresent(element);
	}

	public static Boolean waitPresenceOfElement(By element, Integer waitTime) throws Exception {
		try {
			WebDriverWait wait = new WebDriverWait(driver, waitTime);
			wait.until(ExpectedConditions.presenceOfElementLocated(element));
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public static void waitVisibilityAndPresenceOfElement(By element, Integer waitTime, String exceptionMessage)
			throws Exception {
		try {
			WebDriverWait wait = new WebDriverWait(driver, waitTime);
			wait.until(ExpectedConditions.presenceOfElementLocated(element));
			wait.until(ExpectedConditions.visibilityOfElementLocated(element));
		} catch (Exception e) {
			throw new Exception(exceptionMessage + "\r\n" + e);
		}
	}

	public static boolean waitVisibilityAndPresenceOfElement(By element, Integer waitTime) throws Exception {
		try {
			WebDriverWait wait = new WebDriverWait(driver, waitTime);
			wait.until(ExpectedConditions.presenceOfElementLocated(element));
			wait.until(ExpectedConditions.visibilityOfElementLocated(element));
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public static void waitLoadPageComplete() {
		WebDriverWait wait = new WebDriverWait(driver, MAX_SECONDS);
		wait.until((ExpectedCondition<Boolean>) d -> ((JavascriptExecutor) d)
				.executeScript("return document.readyState").equals("complete"));
	}

	public void clickJs(By by) {
		JavascriptExecutor jse = (JavascriptExecutor) driver;
		jse.executeScript("arguments[0].click();", getWebElement(by));
	}

	public void scrollDynamic(By by) {
		WebElement element = driver.findElement(by);
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
	}

	public void clickActions(By by) {
		WebElement element = getWebElement(by);
		Actions actions = new Actions(driver);
		actions.moveToElement(element).click().perform();
	}

	public void moveMouseHover(By by) throws InterruptedException {
		WebElement target = getWebElement(by);
		Actions actions = new Actions(driver);
		actions.moveToElement(target).build().perform();
		actions.release(target);
	}

	public void moveMouseHoverParameters(int x, int y) {
		Actions actions = new Actions(driver);
		actions.moveByOffset(x, y).build().perform();

	}

	public String getTextElement(By by) {
		WebElement element = getWebElement(by);
		return element.getText();
	}

	public String getOptionSelected(By by) {
		Select select = new Select(getWebElement(by));
		WebElement option = select.getFirstSelectedOption();
		return option.getText();
	}

	public static void scrollToElement(By by) throws InterruptedException {
		boolean failed = false;
		int tryCount = 0;
		do {
			try {
				((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", getWebElement(by));
				failed = false;
			} catch (StaleElementReferenceException e) {
				failed = true;
				tryCount++;
			}
			TimeUnit.MILLISECONDS.sleep(500);
		} while (failed && tryCount < 3);
	}

	public static void scrollToElement(WebElement element) {
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
	}

	public static String getLocalhost() throws UnknownHostException {
		InetAddress inet = InetAddress.getLocalHost();
		String localhost = inet.toString().split("/")[1];
		return localhost;
	}

	/**
	 * get files in directory with filter
	 * 
	 * @param directoryPath
	 * @param filterFile
	 * @return
	 */
	public static File[] getDirectoryFilesWithFilter(String directoryPath, String filterFile) {
		File directory = new File(directoryPath);
		File[] arquivos = directory.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File file, String nome) {
				return nome.matches(filterFile);
			}
		});
		return arquivos;
	}

	/**
	 * get current Url
	 * 
	 * @return
	 */
	public static String getUrlCurrentPage() {
		return driver.getCurrentUrl();
	}

	public void newTab() {
		((JavascriptExecutor) driver).executeScript("window.open('your url','_blank');");
	}

	public void closeTab() {
		((JavascriptExecutor) driver).executeScript("window.close('your url');");
	}

	public void clickJS(By locator) throws Exception {
		Actions actions = new Actions(driver);
		actions.moveToElement(getWebElement(locator)).click().build().perform();
	}

	/**
	 * Switch to second tab, closes the first if boolean true
	 */
	public void switchToNewTab(boolean closeTab) {
		String oldTab = driver.getWindowHandle();
		ArrayList<String> tabs = new ArrayList<String>(driver.getWindowHandles());
		tabs.remove(oldTab);
		if (closeTab) {
			driver.close();
		}
		driver.switchTo().window(tabs.get(0));
	}

	/**
	 * Browse to a page
	 *
	 * @param url URL of the application
	 */
	void navigateToUrl(String url) throws Exception {
		driver.navigate().to(url);
	}

	public void getUrl(String url) throws Exception {
		driver.get(url);
	}

	/**
	 * Switch to second tab, closes the first
	 */
	public void switchTab() {
		String oldTab = driver.getWindowHandle();
		ArrayList<String> tabs = new ArrayList<String>(driver.getWindowHandles());
		tabs.remove(oldTab);
		driver.close();
		driver.switchTo().window(tabs.get(0));
	}

	/**
	 * SendKeys
	 * 
	 * @param element
	 * @param value
	 * @throws ElementNotFoundException
	 */
	public void sendKeysActions(By locator, String value) throws Exception {
		Actions actions = new Actions(driver);
		actions.moveToElement(getWebElement(locator)).click().keyDown(Keys.CONTROL).sendKeys("a").keyUp(Keys.CONTROL)
				.sendKeys(Keys.BACK_SPACE).build().perform();
		actions.moveToElement(getWebElement(locator)).sendKeys(getWebElement(locator), value).build().perform();
	}

	/**
	 * Send keys
	 * 
	 * @param finder
	 * @param value
	 * @throws Exception
	 */
	public void sendEnter(By locator) throws Exception {
		waitElement(locator, MAX_SECONDS);
		if (isPresent(locator)) {
			scrollToElement(locator);
			getWebElement(locator).sendKeys(Keys.ENTER);
		} else {
			throw new Exception("Element not found: " + getWebElement(locator).getText());
		}
	}

	public static boolean isPresent(By locator) {
		Boolean isPresent = getWebElements(locator).size() > 0;
		return isPresent;
	}

	/**
	 * Fill the field with a certain value
	 *
	 * @param finder    Element to action
	 * @param value     Value to be filled
	 * @param sendEnter If true send enter key at the end of filling; If false not
	 *                  send enter key
	 */
	public void sendKeys(By finder, String value, Boolean sendEnter) throws Exception {
		waitElement(finder, MAX_SECONDS);
		WebElement element = getWebElement(finder);
		if (isPresent(finder)) {
			scrollToElement(finder);
			value = value.replace("/", Keys.DIVIDE);
			element.click();
			element.clear();
			element.sendKeys(value);
			if (sendEnter) {
				sendEnter(finder);
			}
		} else {
			throw new Exception("This element was not found: " + finder.toString());
		}
	}

	public void sendKeysSimple(By finder, String value) throws Exception {
		WebElement element = getWebElement(finder);
		waitElement(finder, MAX_SECONDS);
		if (isPresent(finder)) {
			scrollToElement(finder);
			element.clear();
			element.sendKeys(value);
		} else {
			throw new Exception("This element was not found: " + finder.toString());
		}
	}

	/**
	 * Send keys by script
	 * 
	 * @param finder
	 * @param value
	 * @throws ElementNotFoundException
	 * @throws Exception
	 */
	public void sendKeysScript(By finder, String value) throws Exception, Exception {
		waitElement(finder, MAX_SECONDS);
		if (isPresent(finder)) {
			scrollToElement(finder);
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("arguments[0].value='" + value + "';", driver.findElement(finder));
		} else {
			throw new Exception("This element was not found: " + finder.toString());
		}
	}

	void sendKeysLazy(By finder, String value, Boolean sendEnter) throws Exception {
		WebDriverWait wait = new WebDriverWait(driver, MAX_SECONDS);
		WebElement element = wait.until(ExpectedConditions.elementToBeClickable(finder));
		waitElement(finder, MAX_SECONDS);
		if (isPresent(finder)) {
			scrollToElement(finder);
			value = value.replace("/", Keys.DIVIDE);
			element.click();
			element.clear();
			for (Character c : value.toCharArray()) {
				element.sendKeys(c.toString());
				Thread.sleep(50);
			}
			if (sendEnter) {
				element.sendKeys(Keys.ENTER);
			}
		} else {
			throw new Exception("This element was not found: " + finder.toString());
		}
	}

	public void sendKeysSlow(By finder, String value, Boolean sendEnter, int millisecondsIntervalWait)
			throws Exception {
		WebDriverWait wait = new WebDriverWait(driver, MAX_SECONDS);
		WebElement element = wait.until(ExpectedConditions.elementToBeClickable(finder));
		waitElement(finder, MAX_SECONDS);
		if (isPresent(finder)) {
			scrollToElement(finder);
			value = value.replace("/", Keys.DIVIDE);
			element.click();
			element.clear();
			for (Character c : value.toCharArray()) {
				element.sendKeys(c.toString());
				Thread.sleep(millisecondsIntervalWait);
			}
			if (sendEnter) {
				element.sendKeys(Keys.ENTER);
			}
		} else {
			throw new Exception("This element was not found: " + finder.toString());
		}
	}

	/**
	 * Fill the field with a certain value slowly
	 *
	 * @param finder Element to action
	 * @param value  Value to be filled
	 */
	void sendKeysPause(By finder, String value) throws Exception {
		WebElement element = getWebElement(finder);
		waitElement(finder, MAX_SECONDS);
		if (isPresent(finder)) {
			scrollToElement(finder);
			value = value.replace("/", Keys.DIVIDE);
			element.click();
			element.clear();
			for (Character c : value.toCharArray()) {
				element.sendKeys(c.toString());
				Thread.sleep(800);
			}
			if (!element.getAttribute("value").equals(value.replace(Keys.DIVIDE, "/"))) {
				element.click();
				element.clear();
				for (Character c : value.toCharArray()) {
					element.sendKeys(c.toString());
					Thread.sleep(800);
				}
				seleniumWaitForAttribute(finder, "value", value.replace(Keys.DIVIDE, "/"));
			}
		} else {
			throw new Exception("This element was not found: " + finder.toString());
		}
	}

	private void seleniumWaitForAttribute(By finder, String attribute, String expectedValue) throws Exception {
		try {
			WebDriverWait wait = new WebDriverWait(driver, MAX_SECONDS);
			wait.until(ExpectedConditions.attributeToBe(finder, attribute, expectedValue));
		} catch (TimeoutException t) {
			throw new Exception("Expected condition failed: waiting for element found by xpath: " + finder.toString()
					+ " to have value: " + expectedValue + " but the current value is: " + toString());
		}
	}

	public void sendKey(By finder, Keys key) throws Exception {
		WebElement element = getWebElement(finder);
		element.sendKeys(key);
	}

	/**
	 * Click on the object
	 *
	 * @param finder Element to action
	 * @param b
	 */
	public void click(By finder) throws Exception {
		waitVisibilityAndPresenceOfElement(finder, TimeOutConstants.MIN_SECONDS);
		WebDriverWait wait = new WebDriverWait(driver, TimeOutConstants.MIN_SECONDS);
		WebElement element = wait.until(ExpectedConditions.elementToBeClickable(finder));
		scrollToElement(finder);
		int countClicks = 0;
		if (isPresent(finder) && isClickable(finder)) {
			while (countClicks < 5) {
				try {
					element.click();
					break;
				} catch (Exception e) {
					TimeUnit.SECONDS.sleep(1);
					if (countClicks == 5)
						throw new Exception(e);
				}
				countClicks++;
			}
		} else {
			throw new Exception("This element was not found: " + finder.toString());
		}
	}

	public void justClick(By finder) throws Exception {
		getWebElement(finder).click();
	}

	public void clickAlternative(By element) throws Exception {
		Actions actions = new Actions(driver);
		WebElement webElement = getWebElement(element);
		actions.moveToElement(webElement).click().build().perform();
	}

	public void doubleClick(By finder) throws Exception {
		waitElement(finder, MAX_SECONDS);
		if (isPresent(finder)) {
			scrollToElement(finder);
			Actions act = new Actions(driver);
			act.moveToElement(driver.findElement(finder)).doubleClick().build().perform();
		} else {
			throw new Exception("This element was not found: " + finder.toString());
		}
	}

	/**
	 * Verify if the value exists on the combo box options, if exists the option is
	 * selected if not exists, nothing is done.
	 *
	 * @param finder comboBox element
	 * @param value  value that will be selected
	 */
	void verifyAndSelectCombo(By finder, String value) throws Exception {
		waitElement(finder, MAX_SECONDS);
		if (!isPresent(finder)) {
			throw new Exception("This element was not found: " + finder.toString());
		} else if (viableValue(finder, value)) {
			Select select = new Select(getWebElement(finder));
			select.selectByVisibleText(value);
		} else {
			return;
		}
	}

	private Boolean viableValue(By comboBox, String value) {
		try {
			List<String> options = Arrays.asList(comboBoxOptions(comboBox, false));
			return options.contains(value);
		} catch (Exception e) {
			return false;
		}
	}

	private List<String> getComboBoxOptions(By comboBox) throws Exception {
		return Arrays.asList(comboBoxOptions(comboBox, false));
	}

	/**
	 * Return all viable options of a comboBox
	 *
	 * @param comboBox     ComboBox element
	 * @param discardFirst If true, discard the first element of the comboBox
	 */
	private String[] comboBoxOptions(By comboBox, Boolean discardFirst) throws Exception {
		StringBuilder comboBoxOptions = new StringBuilder();
		int checkComboBox = discardFirst ? 1 : 0;
		WebElement mySelectElement = getWebElement(comboBox);
		Select dropdown = new Select(mySelectElement);
		List<WebElement> dropdownElement = dropdown.getOptions();
		for (int j = checkComboBox; j < dropdownElement.size(); j++) {
			comboBoxOptions.append(dropdownElement.get(j).getText()).append('\n');
		}
		return comboBoxOptions.toString().split("\n");
	}

	public List<String> getComboBoxOptions(By comboBox, Boolean discardFirst) throws Exception {
		StringBuilder comboBoxOptions = new StringBuilder();
		int checkComboBox = discardFirst ? 1 : 0;
		WebElement mySelectElement = getWebElement(comboBox);
		Select dropdown = new Select(mySelectElement);
		List<WebElement> dropdownElement = dropdown.getOptions();
		for (int j = checkComboBox; j < dropdownElement.size(); j++) {
			comboBoxOptions.append(dropdownElement.get(j).getText()).append('\n');
		}
		return Arrays.asList(comboBoxOptions.toString().split("\n"));
	}

	void selectCombo(By finder, String value) throws Exception {
		waitVisibilityAndPresenceOfElement(finder, TimeOutConstants.AVERAGE_SECONDS);
		if (!isPresent(finder)) {
			throw new Exception("This element was not found: " + finder.toString());
		} else if (!viableValue(finder, value)) {
			WebDriverWait wait = new WebDriverWait(driver, TimeOutConstants.AVERAGE_SECONDS);
			wait.until(ExpectedConditions.elementToBeSelected(finder));
			if (!viableValue(finder, value)) {
				throw new Exception("Selected value is not viable: " + value);
			}
		}
		Select select = new Select(getWebElement(finder));
		select.selectByVisibleText(value);
	}

	void selectComboContainsValue(By finder, String value) throws Exception {
		waitVisibilityAndPresenceOfElement(finder, TimeOutConstants.AVERAGE_SECONDS);
		if (!isPresent(finder)) {
			throw new Exception("This element was not found: " + finder.toString());
		} else {
			Select select = new Select(getWebElement(finder));
			int index = -1;
			for (String option : getComboBoxOptions(finder)) {
				if (option.contains(value)) {
					index = getComboBoxOptions(finder).indexOf(option);
				}
			}
			if (index != -1) {
				select.selectByIndex(index);
			} else {
				throw new Exception("Selected value is not viable: " + value);
			}

		}
	}

	/**
	 * Select select by index
	 * 
	 * @param finder
	 * @param index
	 * @throws Exception
	 */
	void selectCombo(By finder, int index) throws Exception {
		waitElement(finder, MAX_SECONDS);
		if (!isPresent(finder)) {
			throw new Exception("This element was not found: " + finder.toString());
		}
		Select select = new Select(getWebElement(finder));
		select.selectByIndex(index);
	}

	public void sendKeysValidate(By finder, String value, Boolean sendEnter) throws Exception {
		WebElement element = getWebElement(finder);
		waitElement(finder, MAX_SECONDS);
		if (isPresent(finder)) {
			scrollToElement(finder);
			value = value.replace("/", Keys.DIVIDE);
			element.click();
			element.clear();
			element.sendKeys(value);
			if (!element.getAttribute("value").equals(value.replace(Keys.DIVIDE, "/"))) {

				element.click();
				element.clear();
				element.sendKeys(value);
				seleniumWaitForAttribute(finder, "value", value.replace(Keys.DIVIDE, "/"));
			}
			if (sendEnter) {

				element.sendKeys(Keys.ENTER);
			}
		} else {
			throw new Exception("This element was not found: " + finder.toString());
		}
	}

	public void justSendKeysValidate(By finder, String value, Boolean sendEnter) throws Exception {
		WebElement element = getWebElement(finder);
		if (isPresent(finder)) {
			scrollToElement(finder);
			value = value.replace("/", Keys.DIVIDE);
			element.click();
			element.clear();
			element.sendKeys(value);
			if (!element.getAttribute("value").equals(value.replace(Keys.DIVIDE, "/"))) {

				element.click();
				element.clear();
				element.sendKeys(value);
				seleniumWaitForAttribute(finder, "value", value.replace(Keys.DIVIDE, "/"));
			}
			if (sendEnter) {

				element.sendKeys(Keys.ENTER);
			}
		} else {
			throw new Exception("This element was not found: " + finder.toString());
		}
	}

	/**
	 * Switch to last window opened
	 */
	void switchToNewWindow() throws Exception {
		for (String winHandle : driver.getWindowHandles()) {
			driver.switchTo().window(winHandle);
		}
	}

	/**
	 * Check if the text of an element is equal to a string
	 *
	 * @param element      element that has the value
	 * @param expectedText text to be compared
	 */
	public void checkText(By element, String expectedText) throws Exception {
		waitElement(element, MAX_SECONDS);
		if (isPresent(element)) {
			scrollToElement(element);
			String actualText = (String) getWebElement(element).getText();
			if (!expectedText.equals(actualText)) {
				throw new Exception(
						"The text of the element is " + actualText + " and the expected text is " + expectedText);
			}
		} else {
			throw new Exception("This element was not found: " + element.toString());
		}
	}

	/**
	 * Check if the value of an element is equal to a string
	 *
	 * @param element      element that has the value
	 * @param expectedText text to be compared
	 */
	void checkValue(By element, String expectedText) throws Exception {
		waitElement(element, MAX_SECONDS);
		if (isPresent(element)) {
			scrollToElement(element);
			String actualText = (String) getWebElement(element).getAttribute("value");
			if (!expectedText.equals(actualText)) {
				throw new Exception(
						"The value of the element is " + actualText + " and the expected value is " + expectedText);
			}
		} else {
			throw new Exception("This element was not found: " + element.toString());
		}
	}

	/**
	 * Get a attribute of the element
	 *
	 * @param element   Element Object
	 * @param attribute Attribute Name
	 * @return Value of the attribute of the element.
	 */
	protected String getAttribute(By element, String attribute) throws Exception {
		waitElement(element, MAX_SECONDS);
		if (isPresent(element)) {
			scrollToElement(element);
			String elementAttribute = (String) getWebElement(element).getAttribute(attribute);
			if (!elementAttribute.isEmpty()) {
				return elementAttribute;
			} else {
				throw new Exception("The attribute " + attribute + " of the element is empty");
			}
		} else {
			throw new Exception("This element was not found: " + element.toString());
		}
	}

	/**
	 * Get the text of an element
	 *
	 * @param element Element that has the value
	 * @return The text of the element
	 */
	public String getText(By element) throws Exception {
		waitElement(element, MAX_SECONDS);
		if (isPresent(element)) {
			scrollToElement(element);
			String elementText = (String) getWebElement(element).getText();
			if (!elementText.isEmpty()) {
				return elementText;
			} else {
				throw new Exception("Text of the element is empty");
			}
		} else {
			throw new Exception("This element was not found: " + element.toString());
		}
	}

	public boolean isAttribtuePresent(WebElement element, String attribute) {
		Boolean isPresent = false;
		try {
			String value = element.getAttribute(attribute);
			if (value != null) {
				isPresent = true;
			}
		} catch (Exception e) {
			if (!e.getMessage().toLowerCase().contains("null")) {
				throw e;
			}
		}
		return isPresent;
	}

	public boolean isAttribtuePresent(By finder, String attribute) {
		Boolean isPresent = false;
		WebElement element = getWebElement(finder);
		try {
			String value = element.getAttribute(attribute);
			if (value != null) {
				isPresent = true;
			}
		} catch (Exception e) {
			if (!e.getMessage().toLowerCase().contains("null")) {
				throw e;
			}
		}
		return isPresent;
	}

	/**
	 * Get the value of an element
	 *
	 * @param element Element that has the value
	 * @return The value of the element
	 * @throws EFABasicException        Exception that can be throw if the value of
	 *                                  the element is empty
	 * @throws ElementNotFoundException Exception that can be throw if the element
	 *                                  are not found
	 */
	public String getValue(By element) throws Exception {
		waitElement(element, MAX_SECONDS);
		if (isPresent(element)) {
			scrollToElement(element);
			String elementText = (String) getWebElement(element).getAttribute("value");
			if (!elementText.isEmpty()) {
				return elementText;
			} else {
				throw new Exception("Value of the element is empty");
			}
		} else {
			throw new Exception("This element was not found: " + element.toString());
		}
	}

	/**
	 * Check if an element exists on the page
	 *
	 * @param element     Element to check
	 * @param shouldExist True if the element should exist or false if it shouldn't
	 */
	public void elementExists(By element, Boolean shouldExist) throws Exception {
		waitElement(element, MAX_SECONDS);
		if (shouldExist) {
			if (isPresent(element))
				scrollToElement(element);
			else
				throw new Exception("This element was not found: " + element.toString());
		} else {
			if (isPresent(element))
				throw new Exception("The element exists but it shouldn't");
		}

	}

	/**
	 * Return an attribute from an element using javascript
	 *
	 * @param element   Element that will be used
	 * @param attribute The desired attribute
	 */
	String getAttributeJS(By element, String attribute) throws Exception {
		waitElement(element, MAX_SECONDS);
		if (isPresent(element)) {
			scrollToElement(element);
			return (String) ((JavascriptExecutor) driver).executeScript("return arguments[0]." + attribute, element);
		} else
			throw new Exception("This element was not found: " + element.toString());
	}

	/**
	 * Check if a specific attribute exists
	 *
	 * @param element   Element to be checked
	 * @param attribute The desired attribute
	 */
	Boolean attributeExists(By element, String attribute) throws Exception {
		waitElement(element, MAX_SECONDS);
		if (isPresent(element)) {
			scrollToElement(element);
			String elementText = (String) ((JavascriptExecutor) driver)
					.executeScript("return arguments[0]." + attribute, element);
			return elementText != null;
		} else
			throw new Exception("This element was not found: " + element.toString());
	}

	/**
	 * Reload the current page
	 */
	void refresh() throws Exception {
		driver.navigate().refresh();
	}

	/**
	 * Refresh Page
	 */
	public static void refreshPage() {
		((JavascriptExecutor) driver).executeScript("location.reload();");
	}

	/**
	 * Check if an element is clickable
	 *
	 * @param element Element to be checked
	 */
	boolean isClickable(By element) throws Exception {
		WebElement webElement = getWebElement(element);
		try {
			WebDriverWait wait = new WebDriverWait(driver, 30);
			webElement = wait.until(ExpectedConditions.elementToBeClickable(webElement));
			if (webElement != null) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Move to an element
	 *
	 * @param element Element to move to
	 */
	void moveToElement(By element) throws Exception {
		waitElement(element, MAX_SECONDS);
		WebElement webElement = getWebElement(element);
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(false);", webElement);
	}

	/**
	 * Scroll to the maximum height of the page
	 */
	void scrollToMaximumHeightOfPage() {
		((JavascriptExecutor) driver).executeScript("window.scrollBy(0,document.body.scrollHeight)");
	}

	/**
	 * Get the Absolute xpath of a WebElement
	 *
	 * @param webElement WebElement to retrieve the absolute xpath
	 */
	String getAbsoluteXPathFromWebElement(By webElement) {
		return (String) ((JavascriptExecutor) driver).executeScript("function absoluteXPath(element) {"
				+ "var comp, comps = [];" + "var parent = null;" + "var xpath = '';"
				+ "var getPos = function(element) {" + "var position = 1, curNode;"
				+ "if (element.nodeType == Node.ATTRIBUTE_NODE) {" + "return null;" + "}"
				+ "for (curNode = element.previousSibling; curNode; curNode = curNode.previousSibling) {"
				+ "if (curNode.nodeName == element.nodeName) {" + "++position;" + "}" + "}" + "return position;" + "};"
				+ "if (element instanceof Document) {" + "return '/';" + "}"
				+ "for (; element && !(element instanceof Document); element = element.nodeType == Node.ATTRIBUTE_NODE ? element.ownerElement : element.parentNode) {"
				+ "comp = comps[comps.length] = {};" + "switch (element.nodeType) {" + "case Node.TEXT_NODE:"
				+ "comp.name = 'text()';" + "break;" + "case Node.ATTRIBUTE_NODE:"
				+ "comp.name = '@' + element.nodeName;" + "break;" + "case Node.PROCESSING_INSTRUCTION_NODE:"
				+ "comp.name = 'processing-instruction()';" + "break;" + "case Node.COMMENT_NODE:"
				+ "comp.name = 'comment()';" + "break;" + "case Node.ELEMENT_NODE:" + "comp.name = element.nodeName;"
				+ "break;" + "}" + "comp.position = getPos(element);" + "}"
				+ "for (var i = comps.length - 1; i >= 0; i--) {" + "comp = comps[i];"
				+ "xpath += '/' + comp.name.toLowerCase();" + "if (comp.position !== null) {"
				+ "xpath += '[' + comp.position + ']';" + "}" + "}" + "return xpath;"
				+ "} return absoluteXPath(arguments[0]);", getWebElement(webElement));
	}

	/**
	 * Get the xpath of a WebElement
	 *
	 * @param webElement WebElement to retrieve the xpath
	 */
	String getXpathFromWebElement(By webElement) {
		String[] xpath = getWebElement(webElement).toString().split(" xpath: ");
		return xpath[1].substring(0, xpath[1].length() - 1);
	}

	/**
	 * Increases or decreases chrome zoom Example: 1 = 100%, 1.5 = 150%, etc.
	 *
	 * @param zoomLevel The desired zoom level
	 */
	void changeChromeZoomLevel(Double zoomLevel) {
		((JavascriptExecutor) driver).executeScript("document.body.style.zoom = '" + zoomLevel + "'");
	}

	/**
	 * Replace the ^ and & to Empty String for creating the log message
	 *
	 * @param log The log message
	 */
	private String replaceStartEndRegex(String log) {
		return log.replace(CIRCUNFLEJO, StringUtils.EMPTY).replace(DOLAR, StringUtils.EMPTY);
	}

	/**
	 * Create the log message based on cucumber annotation value, matching the
	 * current method that is being executed with the size of the object[] passed as
	 * parameters
	 *
	 * @param className  The class name
	 * @param methodName The method name
	 * @param objects    Array of objects containning the values to create the log
	 */
	protected String createLog(Class<?> className, String methodName, Object[] objects) throws Exception {
		Predicate<Method> isGetNameEqualsMethodName = e -> e.getName().equals(methodName);
		Predicate<Method> isParameterCountEqualsObjectsLength = e -> e.getParameterCount() == objects.length;
		Method method = Arrays.stream(className.getMethods())
				.filter(isGetNameEqualsMethodName.and(isParameterCountEqualsObjectsLength)).findFirst()
				.orElseThrow(Exception::new);
		String logValue = getAnnotationValue(method);
		logValue = replaceStartEndRegex(logValue);
		for (int i = 0; i < method.getParameterCount(); i++) {
			logValue = logValue.replaceFirst(REGEX, "[" + objects[i] + "]");
		}
		currentStep = logValue;
		return logValue;
	}

	/**
	 * Create the log message based on cucumber annotation value, matching the
	 * current method that is being executed
	 *
	 * @param className  The class name
	 * @param methodName The method name
	 */
	String createLog(Class<?> className, String methodName) throws Exception {
		Predicate<Method> isGetNameEqualsMethodName = e -> e.getName().equals(methodName);
		Method method = Arrays.stream(className.getMethods()).filter(isGetNameEqualsMethodName).findFirst()
				.orElseThrow(Exception::new);
		String logValue = getAnnotationValue(method);
		logValue = replaceStartEndRegex(logValue);
		return logValue;
	}

	/**
	 * Get the annotation value of a method
	 *
	 * @param method The object of the method
	 * @throws Exception
	 */
	private static String getAnnotationValue(Method method) throws Exception {
		if (method.isAnnotationPresent(Given.class)) {
			return method.getAnnotation(Given.class).value();
		} else if (method.isAnnotationPresent(When.class)) {
			return method.getAnnotation(When.class).value();
		} else if (method.isAnnotationPresent(And.class)) {
			return method.getAnnotation(And.class).value();
		} else if (method.isAnnotationPresent(Then.class)) {
			return method.getAnnotation(Then.class).value();
		} else if (method.isAnnotationPresent(Dado.class)) {
			return method.getAnnotation(Dado.class).value();
		} else if (method.isAnnotationPresent(Quando.class)) {
			return method.getAnnotation(Quando.class).value();
		} else if (method.isAnnotationPresent(E.class)) {
			return method.getAnnotation(E.class).value();
		} else if (method.isAnnotationPresent(Então.class)) {
			return method.getAnnotation(Então.class).value();
		} else if (method.isAnnotationPresent(Entao.class)) {
			return method.getAnnotation(Entao.class).value();
		} else
			throw new Exception("Anotação do cucumber inesperada");
	}

	/**
	 * Get the second method that is being executed in runtime and return its name
	 */
	protected String getMethodName() {
		return Thread.currentThread().getStackTrace()[2].getMethodName();
	}

	public static void scrollByHeightPage(int height) {
		JavascriptExecutor Scrool = (JavascriptExecutor) driver;
		Scrool.executeScript("window.scrollBy(0," + height + ")", "");
	}

	public static void validateAndScrollToElement(By element) throws Exception {
		if (waitPresenceOfElement(element, TimeOutConstants.MAX_SECONDS)) {
			if (!isPresent(element))
				throw new Exception("This element was not found: " + element.toString());
			scrollToElement(element);
		} else {
			throw new Exception("this element '" + element + "' was not found on the page in '"
					+ TimeOutConstants.MAX_SECONDS + "' seconds");
		}
	}

	public static void scrollToElementBottom(By element) throws Exception {
		WebElement webElement = getWebElement(element);
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(false);", webElement);
	}

	/**
	 * Return "true" if the element is enabled, return false if not
	 * 
	 * @param element element
	 */
	public static Boolean isEnabled(By element) throws Exception {
		return getWebElement(element).getAttribute("disabled") == null;
	}

	public Boolean isDisabled(By element) throws Exception {
		return isAttribtuePresent(element, "disabled");
	}

	public static Boolean isSelected(By element) throws Exception {
		return getWebElement(element).isSelected();
	}

	public static Boolean isSelectedJquery(By element, String option) {
		WebElement webElement = getWebElement(element);
		String script = "$(arguments[0]).find(option:selected).text();";
		String optionSelected = (String) ((JavascriptExecutor) driver).executeScript(script, webElement);
		return option.equals(optionSelected);
	}

	public static Boolean isChecked(By element) {
		WebElement webElement = getWebElement(element);
		return webElement.isSelected();
	}

	/**
	 * Return true if the element is present and displayed, return false if not
	 * 
	 * @param element
	 * @return
	 * @throws Exception
	 */
	public static Boolean isDisplayed(By element) throws Exception {
		if (isPresent(element)) {
			if ((boolean) getWebElement(element).isDisplayed()) {
				return true;
			}
		}
		return false;
	}

	public void clickWithActionsJS(By element) throws Exception {
		Actions actions = new Actions(driver);
		WebElement webElement = getWebElement(element);
		actions.moveToElement(webElement).click().build().perform();
	}

	/**
	 * Executes the javascript action that performs scrolling for the element with
	 * the element in the middle of the screen
	 * 
	 * @param element Element
	 */
	public static void scrollToElementAlternative(By element) throws Exception {
		WebElement webElement = getWebElement(element);
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", webElement);
	}

	public static void hover(By element) {
		WebDriverWait wait = new WebDriverWait(driver, MAX_SECONDS);
		wait.until(ExpectedConditions.visibilityOfElementLocated(element));
		Actions builder = new Actions(driver);
		builder.moveToElement(getWebElement(element)).perform();
	}

	public void waitInvisibilityOfElement(By locator) {
		WebDriverWait wait = new WebDriverWait(driver, MAX_SECONDS);
		wait.until(ExpectedConditions.invisibilityOf(getWebElement(locator)));
	}

//	public List<String> filterStringByRegex(String string, String regex) {
//		Scanner sc = new Scanner(string);
//
//		List<String> results = sc.findAll(Pattern.compile(regex)).map(mr -> mr.group(0)).filter(Objects::nonNull)
//				.collect(Collectors.toList());
//		sc.close();
//
//		return results;
//	}

	/**
	 * Focus element --> focus happening when clicking
	 * 
	 */
	public static void focusElement(By locator) throws Exception {
		WebElement webElement = getWebElement(locator);
		((JavascriptExecutor) driver).executeScript("arguments[0].focus();", webElement);

		driver.switchTo().activeElement().click();
	}

	/**
	 * Blur element --> happening when something else is focused
	 * 
	 */
	public static void blurElement(By locator) throws Exception {
		WebElement webElement = getWebElement(locator);
		((JavascriptExecutor) driver).executeScript("arguments[0].blur();", webElement);
	}

	public static void sleepBySystemTime(int milliseconds) throws InterruptedException {
		long currentTimeMillis = System.currentTimeMillis();
		while (System.currentTimeMillis() < currentTimeMillis + milliseconds) {
			Thread.sleep(1);
		}
	}

	public static void waitForUrlContains(String expectedString, int time) throws Exception {
		try {
			WebDriverWait wait = new WebDriverWait(driver, time);
			wait.until(ExpectedConditions.urlContains(expectedString));

		} catch (Exception e) {
			throw new MalformedURLException("A url esperada está incorreta!");
		}
	}
}
