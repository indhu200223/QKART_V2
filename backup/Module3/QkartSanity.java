/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package QKART_SANITY_LOGIN.Module1;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.BrowserType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;



public class QkartSanity {

    public static String lastGeneratedUserName;


    public static RemoteWebDriver createDriver() throws MalformedURLException {
        // Launch Browser using Zalenium
        final DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setBrowserName(BrowserType.CHROME);
        RemoteWebDriver driver =
                new RemoteWebDriver(new URL("http://localhost:8082/wd/hub"), capabilities);

        return driver;
    }

    public static void logStatus(String type, String message, String status) {

        System.out.println(String.format("%s |  %s  |  %s | %s",
                String.valueOf(java.time.LocalDateTime.now()), type, message, status));
    }

    public static void takeScreenshot(WebDriver driver, String screenshotType, String description) {
        // TODO: CRIO_TASK_MODULE_SYNCHRONISATION - Implement method using below steps
        /*
         * 1. Check if the folder "/screenshots" exists, create if it doesn't 2. Generate a unique
         * string using the timestamp 3. Capture screenshot 4. Save the screenshot inside the
         * "/screenshots" folder using the following naming convention:
         * screenshot_<Timestamp>_<ScreenshotType>_<Description>.png eg:
         * screenshot_2022-03-05T06:59:46.015489_StartTestcase_Testcase01.png
         */

        try {
            File theDir = new File("/screenshots");
            if (!theDir.exists()) {
                theDir.mkdirs();
            }

            String timestamp = String.valueOf(java.time.LocalDateTime.now());
            String fileName = String.format("screenshot_%s_%s_%s.png", timestamp, screenshotType,
                    description);

            TakesScreenshot scrShot = ((TakesScreenshot) driver); /// takescreenshot interface
                                                                  /// acsted with webdriver
            File SrcFile = scrShot.getScreenshotAs(OutputType.FILE);

            File DestFile = new File("screenshots/" + fileName);
            FileUtils.copyFile(SrcFile, DestFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /*
     * Testcase01: Verify the functionality of Login button on the Home page
     */
    public static Boolean TestCase01(RemoteWebDriver driver) throws InterruptedException {
        Boolean status;
        takeScreenshot(driver, "Start TestCase", "TestCase01");

        logStatus("Start TestCase", "Test Case 1: Verify User Registration", "DONE");


        // Visit the Registration page and register a new user
        Register registration = new Register(driver);
        registration.navigateToRegisterPage();
        status = registration.registerUser("testUser", "abc@123", true);
        if (!status) {
            logStatus("TestCase 1", "Test Case Pass. User Registration Pass", "FAIL");
            logStatus("End TestCase", "Test Case 1: Verify user Registration : ",
                    status ? "PASS" : "FAIL");

            // Return False as the test case Fails
            return false;
        } else {
            logStatus("TestCase 1", "Test Case Pass. User Registration Pass", "PASS");
        }

        // Save the last generated username
        lastGeneratedUserName = registration.lastGeneratedUsername;

        // Visit the login page and login with the previuosly registered user
        Login login = new Login(driver);
        login.navigateToLoginPage();
        status = login.PerformLogin(lastGeneratedUserName, "abc@123");
        logStatus("Test Step", "User Perform Login: ", status ? "PASS" : "FAIL");
        if (!status) {
            logStatus("End TestCase", "Test Case 1: Verify user Registration : ",
                    status ? "PASS" : "FAIL");

            takeScreenshot(driver, "Failed", "TestCase01");
            return false;
        }

        // Visit the home page and log out the logged in user
        Home home = new Home(driver);
        status = home.PerformLogout();
        logStatus("End TestCase", "Test Case 1: Verify user Registration : ",
                status ? "PASS" : "FAIL");

        takeScreenshot(driver, "End TestCase", "TestCase01");

        return status;
    }

    /*
     * Verify that an existing user is not allowed to re-register on QKart
     */
    public static Boolean TestCase02(RemoteWebDriver driver) throws InterruptedException {
        Boolean status;
        takeScreenshot(driver, "Start TestCase", "TestCase02");

        logStatus("Start Testcase",
                "Test Case 2: Verify User Registration with an existing username ", "DONE");

        // Visit the Registration page and register a new user
        Register registration = new Register(driver);
        registration.navigateToRegisterPage();
        status = registration.registerUser("testUser", "abc@123", true);
        logStatus("Test Step", "User Registration : ", status ? "PASS" : "FAIL");
        if (!status) {
            logStatus("End TestCase", "Test Case 2: Verify user Registration : ",
                    status ? "PASS" : "FAIL");
            takeScreenshot(driver, "Failed", "TestCase02");
            return false;

        }

        // Save the last generated username
        lastGeneratedUserName = registration.lastGeneratedUsername;

        // Visit the Registration page and try to register using the previously
        // registered user's credentials
        registration.navigateToRegisterPage();
        status = registration.registerUser(lastGeneratedUserName, "abc@123", false);

        // If status is true, then registration succeeded, else registration has
        // failed. In this case registration failure means Success
        logStatus("End TestCase", "Test Case 2: Verify user Registration : ",
                status ? "FAIL" : "PASS");

        takeScreenshot(driver, "End TestCase", "TestCase02");

        return !status;
    }

    /*
     * Verify the functinality of the search text box
     */
    public static Boolean TestCase03(RemoteWebDriver driver) throws InterruptedException {

        takeScreenshot(driver, "Start TestCase", "TestCase03");

        logStatus("TestCase 3", "Start test case : Verify functionality of search box ", "DONE");

        boolean status;

        // Visit the home page
        Home homePage = new Home(driver);
        homePage.navigateToHome();

        // SLEEP_STMT_01 : Wait for Page to Load
        // Thread.sleep(5000); //(removing this sleep statmenet because driver.get() method is
        // implemented inside navigateToHome())

        // Search for the "yonex" product
        status = homePage.searchForProduct("yonex");
        if (!status) {
            logStatus("TestCase 3", "Test Case Failure. Unable to search for given product",
                    "FAIL");
            takeScreenshot(driver, "Failed", "TestCase03");
            return false;
        }

        // Fetch the search results
        List<WebElement> searchResults = homePage.getSearchResults();

        // Verify the search results are available
        if (searchResults.size() == 0) {
            logStatus("TestCase 3",
                    "Test Case Failure. There were no results for the given search string", "FAIL");
            return false;
        }

        for (WebElement webElement : searchResults) {
            // Create a SearchResult object from the parent element
            SearchResult resultelement = new SearchResult(webElement);

            // Verify that all results contain the searched text
            String elementText = resultelement.getTitleofResult();
            if (!elementText.toUpperCase().contains("YONEX")) {
                logStatus("TestCase 3",
                        "Test Case Failure. Test Results contains un-expected values: "
                                + elementText,
                        "FAIL");
                return false;
            }
        }

        logStatus("Step Success", "Successfully validated the search results ", "PASS");
        // SLEEP_STMT_02
        Thread.sleep(2000);

        // Search for product
        status = homePage.searchForProduct("Gesundheit");
        if (!status) {
            logStatus("TestCase 3", "Test Case Failure. Unable to search for given product",
                    "FAIL");
            return false;
        }

        // Verify no search results are found
        searchResults = homePage.getSearchResults();
        if (searchResults.size() == 0) {
            if (homePage.isNoResultFound()) {
                logStatus("Step Success",
                        "Successfully validated that no products found message is displayed",
                        "PASS");
            }
            logStatus("TestCase 3",
                    "Test Case PASS. Verified that no search results were found for the given text",
                    "PASS");
        } else {
            logStatus("TestCase 3",
                    "Test Case Fail. Expected: no results , actual: Results were available",
                    "FAIL");

            return false;
        }

        return true;
    }

    /*
     * Verify the presence of size chart and check if the size chart content is as expected
     */
    public static Boolean TestCase04(RemoteWebDriver driver) throws InterruptedException {
        logStatus("TestCase 4", "Start test case : Verify the presence of size Chart", "DONE");
        boolean status = false;

        // Visit home page
        Home homePage = new Home(driver);
        homePage.navigateToHome();

        // SLEEP_STMT_03 : Wait for page to load
        Thread.sleep(5000);

        // Search for product and get card content element of search results
        status = homePage.searchForProduct("Running Shoes");
        List<WebElement> searchResults = homePage.getSearchResults();

        // Create expected values
        List<String> expectedTableHeaders = Arrays.asList("Size", "UK/INDIA", "EU", "HEEL TO TOE");
        List<List<String>> expectedTableBody = Arrays.asList(Arrays.asList("6", "6", "40", "9.8"),
                Arrays.asList("7", "7", "41", "10.2"), Arrays.asList("8", "8", "42", "10.6"),
                Arrays.asList("9", "9", "43", "11"), Arrays.asList("10", "10", "44", "11.5"),
                Arrays.asList("11", "11", "45", "12.2"), Arrays.asList("12", "12", "46", "12.6"));

        // Verify size chart presence and content matching for each search result
        for (WebElement webElement : searchResults) {
            SearchResult result = new SearchResult(webElement);

            // Verify if the size chart exists for the search result
            if (result.verifySizeChartExists()) {
                logStatus("Step Success", "Successfully validated presence of Size Chart Link",
                        "PASS");

                // Verify if size dropdown exists
                status = result.verifyExistenceofSizeDropdown(driver);
                logStatus("Step Success", "Validated presence of drop down",
                        status ? "PASS" : "FAIL");

                // Open the size chart
                if (result.openSizechart()) {
                    // Verify if the size chart contents matches the expected values
                    if (result.validateSizeChartContents(expectedTableHeaders, expectedTableBody,
                            driver)) {
                        logStatus("Step Success",
                                "Successfully validated contents of Size Chart Link", "PASS");
                    } else {
                        logStatus("Step Failure",
                                "Failure while validating contents of Size Chart Link", "FAIL");
                    }

                    // Close the size chart modal
                    status = result.closeSizeChart(driver);

                } else {
                    logStatus("TestCase 4", "Test Case Fail. Failure to open Size Chart", "FAIL");
                    return false;
                }

            } else {
                logStatus("TestCase 4", "Test Case Fail. Size Chart Link does not exist", "FAIL");
                return false;
            }
        }
        logStatus("TestCase 4", "Test Case PASS. Validated Size Chart Details", "PASS");
        return status;
    }

    /*
     * Verify the complete flow of checking out and placing order for products is working correctly
     */
    public static Boolean TestCase05(RemoteWebDriver driver) throws InterruptedException {
        Boolean status;
        logStatus("Start TestCase", "Test Case 5: Verify Happy Flow of buying products", "DONE");

        // Go to the Register page
        Register registration = new Register(driver);
        registration.navigateToRegisterPage();

        // Register a new user
        status = registration.registerUser("testUser", "abc@123", true);
        if (!status) {
            logStatus("TestCase 5", "Test Case Failure. Happy Flow Test Failed", "FAIL");
        }

        // Save the username of the newly registered user
        lastGeneratedUserName = registration.lastGeneratedUsername;

        // Go to the login page
        Login login = new Login(driver);
        login.navigateToLoginPage();

        // Login with the newly registered user's credentials
        status = login.PerformLogin(lastGeneratedUserName, "abc@123");
        if (!status) {
            logStatus("Step Failure", "User Perform Login Failed", status ? "PASS" : "FAIL");
            logStatus("End TestCase", "Test Case 5: Happy Flow Test Failed : ",
                    status ? "PASS" : "FAIL");
        }

        // Go to the home page
        Home homePage = new Home(driver);
        homePage.navigateToHome();

        // Find required products by searching and add them to the user's cart
        status = homePage.searchForProduct("Yonex");
        homePage.addProductToCart("YONEX Smash Badminton Racquet");
        status = homePage.searchForProduct("Tan");
        homePage.addProductToCart("Tan Leatherette Weekender Duffle");

        // Click on the checkout button
        homePage.clickCheckout();

        // Add a new address on the Checkout page and select it
        Checkout checkoutPage = new Checkout(driver);
        checkoutPage.addNewAddress("Addr line 1 addr Line 2 addr line 3");
        checkoutPage.selectAddress("Addr line 1 addr Line 2 addr line 3");

        // Place the order
        checkoutPage.placeOrder();
        // SLEEP_STMT_04: Wait for place order to succeed and navigate to Thanks page
        // Thread.sleep(3000);
        WebDriverWait wait = new WebDriverWait(driver, 3);
        wait.until(ExpectedConditions.urlContains("/thanks"));

        // Check if placing order redirected to the Thansk page
        status = driver.getCurrentUrl().endsWith("/thanks");

        // Go to the home page
        homePage.navigateToHome();
        Thread.sleep(3000);

        // Log out the user
        homePage.PerformLogout();

        logStatus("End TestCase", "Test Case 5: Happy Flow Test Completed : ",
                status ? "PASS" : "FAIL");
        return status;
    }

    /*
     * Verify the quantity of items in cart can be updated
     */
    public static Boolean TestCase06(RemoteWebDriver driver) throws InterruptedException {
        Boolean status;
        logStatus("Start TestCase", "Test Case 6: Verify that cart can be edited", "DONE");
        Home homePage = new Home(driver);
        Register registration = new Register(driver);
        Login login = new Login(driver);

        // TODO: CRIO_TASK_MODULE_TEST_AUTOMATION - TEST CASE 06: MILESTONE 5

        // TODO: Register a new user

        registration.navigateToRegisterPage();
        status = registration.registerUser(lastGeneratedUserName, "abc@123", true);

        // If status is true, then registration succeeded, else registration has
        // failed. In this case registration failure means Success
        if (!status)
            logStatus("TestCase 6", "Test Case Failure. Happy Flow Test Failed ", "FAIL");

        lastGeneratedUserName = registration.lastGeneratedUsername;

        // TODO: Login using the newly registed user

        login.navigateToLoginPage();

        status = login.PerformLogin(lastGeneratedUserName, "abc@123");
        if (!status) {
            logStatus("Step Failure", "User Perform Login Failed", status ? "PASS" : "FAIL");
            logStatus("End TestCase", "Test Case 5: Happy Flow Test Failed : ",
                    status ? "PASS" : "FAIL");
        }


        // TODO: Add "Xtend Smart Watch" to cart

        status = homePage.searchForProduct("Xtend");
        homePage.addProductToCart("Xtend Smart Watch");



        // TODO: Add "Yarine Floor Lamp" to cart

        status = homePage.searchForProduct("Yarine");
        homePage.addProductToCart("Yarine Floor Lamp");

        // update watch quantity to 2
        homePage.changeProductQuantityinCart("Xtend Smart Watch", 2);

        // update table lamp quantity to 0
        homePage.changeProductQuantityinCart("Yarine Floor Lamp", 0);

        // update watch quantity again to 1
        homePage.changeProductQuantityinCart("Xtend Smart Watch", 1);

        homePage.clickCheckout();

        Checkout checkoutPage = new Checkout(driver);
        checkoutPage.addNewAddress("Addr line 1 addr Line 2 addr line 3");
        checkoutPage.selectAddress("Addr line 1 addr Line 2 addr line 3");

        checkoutPage.placeOrder();
        Thread.sleep(3000);

        status = driver.getCurrentUrl().endsWith("/thanks");

        homePage.navigateToHome();
        Thread.sleep(3000);
        homePage.PerformLogout();

        logStatus("End TestCase", "Test Case 6: Verify that cart can be edited: ",
                status ? "PASS" : "FAIL");
        return status;
    }


    public static Boolean TestCase07(RemoteWebDriver driver) throws InterruptedException {
        Boolean status;
        logStatus("Start TestCase",
                "Test Case 7: Verify that insufficient balance error is thrown when the wallet balance is not enough",
                "DONE");

        Register registration = new Register(driver);
        registration.navigateToRegisterPage();
        status = registration.registerUser("testUser", "abc@123", true);
        if (!status) {
            logStatus("Step Failure", "User Perform Registration Failed", status ? "PASS" : "FAIL");
            logStatus("End TestCase",
                    "Test Case 7: Verify that insufficient balance error is thrown when the wallet balance is not enough: ",
                    status ? "PASS" : "FAIL");
            return false;
        }
        lastGeneratedUserName = registration.lastGeneratedUsername;

        Login login = new Login(driver);
        login.navigateToLoginPage();
        status = login.PerformLogin(lastGeneratedUserName, "abc@123");
        if (!status) {
            logStatus("Step Failure", "User Perform Login Failed", status ? "PASS" : "FAIL");
            logStatus("End TestCase",
                    "Test Case 7: Verify that insufficient balance error is thrown when the wallet balance is not enough: ",
                    status ? "PASS" : "FAIL");
            return false;
        }

        Home homePage = new Home(driver);
        homePage.navigateToHome();
        status = homePage.searchForProduct("Stylecon");
        homePage.addProductToCart("Stylecon 9 Seater RHS Sofa Set");
        Thread.sleep(3000);

        homePage.changeProductQuantityinCart("Stylecon 9 Seater RHS Sofa Set", 10);

        homePage.clickCheckout();

        Checkout checkoutPage = new Checkout(driver);
        checkoutPage.addNewAddress("Addr line 1 addr Line 2 addr line 3");
        checkoutPage.selectAddress("Addr line 1 addr Line 2 addr line 3");

        checkoutPage.placeOrder();
        Thread.sleep(3000);

        status = checkoutPage.verifyInsufficientBalanceMessage();

        logStatus("End TestCase",
                "Test Case 7: Verify that insufficient balance error is thrown when the wallet balance is not enough: ",
                status ? "PASS" : "FAIL");

        return status;
    }

    public static Boolean TestCase08(RemoteWebDriver driver) throws InterruptedException {
        Boolean status = false;

        // TODO: CRIO_TASK_MODULE_SYNCHRONISATION -
        logStatus("Start TestCase", "Test Case 8: Add Test Case for Multitab Scenario", "DONE");

        // Go to the Register page
        Register registration = new Register(driver);
        registration.navigateToRegisterPage();

        // Register a new user
        status = registration.registerUser("testUser", "abc@123", true);
        if (!status) {
            logStatus("TestCase 8", "Test Case Failure. Add Test Case for Multitab Scenario failed",
                    "FAIL");
        }

        // Save the username of the newly registered user
        lastGeneratedUserName = registration.lastGeneratedUsername;

        // Go to the login page
        Login login = new Login(driver);
        login.navigateToLoginPage();

        // Login with the newly registered user's credentials
        status = login.PerformLogin(lastGeneratedUserName, "abc@123");
        if (!status) {
            logStatus("Step Failure", "User Perform Login Failed", status ? "PASS" : "FAIL");
            logStatus("End TestCase", "Test Case 5: Add Test Case for Multitab Scenario failed : ",
                    status ? "PASS" : "FAIL");
        }

        // Go to the home page
        Home homePage = new Home(driver);
        homePage.navigateToHome();
        String currentURL = driver.getCurrentUrl();

        // Find required products by searching and add them to the user's cart
        status = homePage.searchForProduct("Yonex");
        homePage.addProductToCart("YONEX Smash Badminton Racquet");


        WebElement privacyPolicy = driver.findElement(By.xpath("//a[text()='Privacy policy']"));
        privacyPolicy.click();

        Set<String> setWindows = driver.getWindowHandles();
        String[] windows = setWindows.toArray(new String[setWindows.size()]);
        driver.switchTo().window(windows[1]);

        // there is not get method for set, thats y we are converting to array (parent window is
        // window[0] and newly opened window will start from window[1])
        Thread.sleep(2000);
        // there is no wait methods except thread.sleep to the window to be opened
        // homePage.navigateToHome();wor
        driver.get(currentURL);

        // verifying card contents , here we have only one product. so we are creating a list of
        // products and adding our product to the verfycardcontent method

        List<String> expectedProductNamesinCart = new ArrayList<>();
        // we are adding our expectedproductnames in cart to our cart
        expectedProductNamesinCart.add("YONEX Smash Badminton Racquet");
        status = homePage.verifyCartContents(expectedProductNamesinCart);

        logStatus("End TestCase", "Test Case 8: Add Test Case for Multitab Scenario : ",
                status ? "PASS" : "FAIL");

        driver.close(); // closing the opened window(newly)
        driver.switchTo().window(windows[0]); // switching back to the parent window
        homePage.PerformLogout();
        // because as a part of our next testcase, we dont require login

        return status;

    }

    public static Boolean TestCase09(RemoteWebDriver driver) throws InterruptedException {
        // TODO: CRIO_TASK_MODULE_SYNCHRONISATION -
        Boolean status = false;

        logStatus("Start TestCase", "Verify Privacy Policy and Terms of Service failed", "DONE");

        Home homePage = new Home(driver);
        homePage.navigateToHome();


        WebElement privacyPolicy = driver.findElement(By.xpath("//a[text()='Privacy policy']"));
        privacyPolicy.click();
        Thread.sleep(2000);

        String currentTabUrl = driver.getCurrentUrl();


        if (!currentTabUrl.equals("https://crio-qkart-frontend-qa.vercel.app/")) {

            logStatus("Step Failure", "Verify current url failed", "FAIL");

            return false;
        }

        Set<String> setWindows = driver.getWindowHandles();
        String[] windows = setWindows.toArray(new String[setWindows.size()]);
        driver.switchTo().window(windows[1]);
        // switching to privacy policy page

        // checking the availability of privacy policy in the newly opened tab
        WebElement privacyPolicyElementdisplay =
                driver.findElement(By.xpath("//h2[text()='Privacy Policy']"));
        if (!privacyPolicyElementdisplay.isDisplayed()) {

            logStatus("Step Failure", "Verify Privacy Policy content is display failed", "FAIL");

            return false;
        }

        driver.switchTo().window(windows[0]);

        WebElement TermsofService = driver.findElement(By.xpath("//a[text()='Terms of Service']"));
        TermsofService.click();
        Thread.sleep(2000);

        currentTabUrl = driver.getCurrentUrl();
        if (!currentTabUrl.equals("https://crio-qkart-frontend-qa.vercel.app/")) {

            logStatus("Step Failure", "Verify current url failed", "FAIL");

            return false;
        }
        setWindows = driver.getWindowHandles();
        windows = setWindows.toArray(new String[setWindows.size()]);
        driver.switchTo().window(windows[2]); // 2nd opened winodw
        // 1st window is privacy policy (depending on the order of opening)


        // WebElement TermsofServiceElementdisplay =
        //         driver.findElement(By.xpath("//h2[text()='Terms of Service']"));
        // if (!TermsofServiceElementdisplay.isDisplayed()) {

        //     logStatus("Step Failure", "Verify Terms of Conditions content is display failed",
        //             "FAIL");

        //     return false;
        // }
        // closing all the windows
        driver.close(); // terms of service(currently opend) window
        driver.switchTo().window(windows[1]);
        driver.close();
        driver.switchTo().window(windows[0]);
        // driver.close();

        // perform logout is not required, because we are alreay in the homepage

        return true;
    }

    public static Boolean TestCase10(RemoteWebDriver driver) throws InterruptedException {
        Boolean status = false;
        // TODO: CRIO_TASK_MODULE_SYNCHRONISATION -


        Home homePage = new Home(driver);
        homePage.navigateToHome();

        WebElement ContactUs = driver.findElement(By.xpath("//p[text()='Contact us']"));
        ContactUs.click();


        WebDriverWait wait = new WebDriverWait(driver, 2);
        wait.until(ExpectedConditions
                .visibilityOfElementLocated(By.xpath("//div[@class='card-block']")));

        WebElement name = driver.findElement(By.xpath("//input[@placeholder='Name']"));
        name.sendKeys("crio user");

        WebElement email = driver.findElement(By.xpath("//input[@placeholder='Email']"));
        email.sendKeys(" criouser@gmail.com");

        WebElement entermessage = driver.findElement(By.xpath("//input[@placeholder='Message']"));
        entermessage.sendKeys("Testing the contact us page");

        WebElement contactusButton =
                driver.findElement(By.xpath("//button[text()=' Contact Now']"));
        contactusButton.click();


        status = wait.until(ExpectedConditions
                .invisibilityOfElementLocated(By.xpath("//div[@class='card-block']")));



        return status; // because by default the status is false (check above),thats y we are
                     // hardcoding true
    }

    public static Boolean TestCase11(RemoteWebDriver driver) throws InterruptedException {
        Boolean status = false;
        // TODO: CRIO_TASK_MODULE_SYNCHRONISATION -
        logStatus("Start TestCase", "Test Case 11: Verifying Check for advertisements", "DONE");

        // Go to the Register page
        Register registration = new Register(driver);
        registration.navigateToRegisterPage();

        // Register a new user
        status = registration.registerUser("testUser", "abc@123", true);
        if (!status) {
            logStatus("TestCase 11", "Test Case Failure. Check for advertisements", "FAIL");
        }

        // Save the username of the newly registered user
        lastGeneratedUserName = registration.lastGeneratedUsername;

        // Go to the login page
        Login login = new Login(driver);
        login.navigateToLoginPage();

        // Login with the newly registered user's credentials
        status = login.PerformLogin(lastGeneratedUserName, "abc@123");
        if (!status) {
            logStatus("Step Failure", "User Perform Login Failed", status ? "PASS" : "FAIL");
            logStatus("End TestCase", "Test Case 11: Check for advertisements : ",
                    status ? "PASS" : "FAIL");
        }

        // Go to the home page
        Home homePage = new Home(driver);
        homePage.navigateToHome();

        // Find required products by searching and add them to the user's cart
        status = homePage.searchForProduct("Yonex");
        homePage.addProductToCart("YONEX Smash Badminton Racquet");

        // Click on the checkout button
        homePage.clickCheckout();

        // Add a new address on the Checkout page and select it
        Checkout checkoutPage = new Checkout(driver);
        checkoutPage.addNewAddress("Addr line 1 addr Line 2 addr line 3");
        checkoutPage.selectAddress("Addr line 1 addr Line 2 addr line 3");

        // Place the order
        checkoutPage.placeOrder();
        // SLEEP_STMT_04: Wait for place order to succeed and navigate to Thanks page
        // Thread.sleep(3000);
        WebDriverWait wait = new WebDriverWait(driver, 3);
        wait.until(ExpectedConditions.urlContains("/thanks"));

        // Check if placing order redirected to the Thansk page
        status = driver.getCurrentUrl().endsWith("/thanks");



        List<WebElement> ads = driver.findElements(By.xpath("//iframe")); // or
                                                                          // (By.TagName("iframe"));

        int actualSize = ads.size();
        if (actualSize != 3) {
            return false;
        }

        WebElement adv1 = driver.findElement(By.xpath("(//iframe)[1]"));
        driver.switchTo().frame(adv1);
        WebElement buyNow1 = driver.findElement(By.xpath("//button[text()='Buy Now']"));
        buyNow1.click();
        wait.until(ExpectedConditions.urlToBe("https://crio-qkart-frontend-qa.vercel.app/checkout"));

        if(!driver.getCurrentUrl().contains("/checkout")){
            return false;
        }
        driver.navigate().back();
        driver.switchTo().parentFrame(); //switching back to parent frame




        WebElement adv2 = driver.findElement(By.xpath("(//iframe)[2]"));
        driver.switchTo().frame(adv2);
        WebElement buyNow2 = driver.findElement(By.xpath("//button[text()='Buy Now']"));
        buyNow2.click();
        wait.until(ExpectedConditions.urlToBe("https://crio-qkart-frontend-qa.vercel.app/checkout"));

        if(!driver.getCurrentUrl().contains("/checkout")){
            return false;
        }
        driver.navigate().back();
        driver.switchTo().parentFrame(); //switching back to parent fram
        

        return true;
    }




    public static void main(String[] args) throws InterruptedException, MalformedURLException {
        int totalTests = 0;
        int passedTests = 0;
        Boolean status;
        // Maximize and Implicit Wait for things to initailize

        RemoteWebDriver driver = createDriver();

        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

        try {
            // Execute Test Case 1
            totalTests += 1;
            status = TestCase01(driver);
            if (status) {
            passedTests += 1;
            }

        // //  // System.out.println("");

        // //  // Execute Test Case 2
            totalTests += 1;
            status = TestCase02(driver);
            if (status) {
            passedTests += 1;
            }

        // // // System.out.println("");

        // //     // // Execute Test Case 3
            totalTests += 1;
            status = TestCase03(driver);
            if (status) {
            passedTests += 1;
            }

        // //     // System.out.println("");

        // //      // Execute Test Case 4
            totalTests += 1;
            status = TestCase04(driver);
            if (status) {
            passedTests += 1;
            }

        // //     // // System.out.println("");

        // //  // Execute Test Case 5
            totalTests += 1;
            status = TestCase05(driver);
            if (status) {
            passedTests += 1;
            }

        // //      // System.out.println("");

        // //     // Execute Test Case 6
            totalTests += 1;
            status = TestCase06(driver);
            if (status) {
            passedTests += 1;
            }

        // //      // System.out.println("");

        // //     /// Execute Test Case 7
            totalTests += 1;
            status = TestCase07(driver);
            if (status) {
            passedTests += 1;
            }

        // //      // System.out.println("");


        //      totalTests += 1;
            status = TestCase07(driver);
            if (status) {
            passedTests += 1;
            }

        //     // System.out.println("");


        //     // Execute Test Case 8
            totalTests += 1;
            status = TestCase08(driver);
            if (status) {
            passedTests += 1;
            }

        //     // System.out.println("");

        //     // // Execute Test Case 9
            totalTests += 1;
            status = TestCase09(driver);
            if (status) {
            passedTests += 1;
            }

        //     // System.out.println("");

        //     // Execute Test Case 10
            totalTests += 1;
            status = TestCase10(driver);
            if (status) {
            passedTests += 1;
            }

        //     // System.out.println("");

        //     // Execute Test Case 11
            totalTests += 1;
            status = TestCase11(driver);
            if (status) {
                passedTests += 1;
             }

            // System.out.println("");
        } catch (Exception e) {
            throw e;
        } finally {
            // quit Chrome Driver
            driver.quit();

            System.out.println(String.format("%s out of %s test cases Passed ",
                    Integer.toString(passedTests), Integer.toString(totalTests)));
        }

    }
}
