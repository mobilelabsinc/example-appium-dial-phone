import appium.AppiumController;
import io.qameta.allure.*;
import org.openqa.selenium.OutputType;
import org.testng.Assert;
import org.testng.annotations.*;


import screens.PhoneScreen;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;


public class TestDialPhone extends AppiumController {
    private final static String DEVICECONNECT_PROPERTIES_FILE = "deviceconnect.properties";
    private final static String DEVICECONNECT_URL = "deviceconnect.url";
    private final static String DEVICECONNECT_USERNAME = "deviceconnect.username";
    private final static String DEVICECONNECT_API_KEY = "deviceconnect.api.key";

    private final static String IOS_IDS = "ios.ids";
    private final static String IOS_BUNDLE_ID = "ios.bundle.id";
    private final static String IOS_PLATFORM_NAME = "IOS";
    private final static String IOS_AUTOMATION_NAME = "XCUITest";

    private final static String ANDROID_IDS = "android.ids";
    private final static String ANDROID_BUNDLE_ID = "android.bundle.id";
    private final static String ANDROID_PLATFORM_NAME = "ANDROID";
    private final static String ANDROID_AUTOMATION_NAME = "UiAutomator2";

    private final static String PHONE_NUMBER = "phone.number";

    protected PhoneScreen phoneScreen;


    @Factory (dataProvider = "deviceList")
    public TestDialPhone(String udid, String platformName,
                         String bundleID, String automationName) throws Exception {
        this.udid = udid;
        this.bundleID = bundleID;
        this.automationName = automationName;
        this.platform = OperatingSystem.valueOf(platformName);

        //Load deviceConnect properties from file used for every test connection
        Properties props = new Properties();
        props.load(new FileReader(new File(DEVICECONNECT_PROPERTIES_FILE)));

        //Load the server connection properties
        server = props.getProperty(DEVICECONNECT_URL);
        username = props.getProperty(DEVICECONNECT_USERNAME);
        apiToken = props.getProperty(DEVICECONNECT_API_KEY);
    }

    @DataProvider(name = "deviceList", parallel=true)
    private static Iterator<Object[]> buildDeviceList() throws IOException {
        List<Object[]> devices = new ArrayList<>();

        //Pull device properties from file
        //Used to run multiple devices in parallel
        Properties props = new Properties();
        props.load(new FileReader(new File(DEVICECONNECT_PROPERTIES_FILE)));

        //Load iOS devices from properties file
        buildDeviceList(devices, props.getProperty(IOS_IDS), props.getProperty(IOS_BUNDLE_ID),
                IOS_PLATFORM_NAME, IOS_AUTOMATION_NAME);

        //Load Android devices from properties file
        buildDeviceList(devices, props.getProperty(ANDROID_IDS), props.getProperty(ANDROID_BUNDLE_ID),
                ANDROID_PLATFORM_NAME, ANDROID_AUTOMATION_NAME);

        return devices.iterator();
    }

    @BeforeClass
    public void setUp() throws Exception {
        startAppium();

        phoneScreen = new PhoneScreen(driver);
    }

    @Test
    @Feature("Login")
    @Story("Valid Login")
    @Description("Verifies that the Search Button appears on the Search Screen after entering the username and password and then clicking the Sign In button on the login screen")
    public void dialTest() throws Exception {
        try {
            Properties props = new Properties();
            props.load(new FileReader(new File(DEVICECONNECT_PROPERTIES_FILE)));
            String phoneNumber = props.getProperty(PHONE_NUMBER);

            phoneScreen.dialNumber(phoneNumber);
            Assert.assertTrue(phoneScreen.isEndCallButtonPresent());
            getScreenshot("Phone Screen");
            phoneScreen.hangUp();
        } catch (Exception ex) {
            //Get screenshot if test fails
            getScreenshot("Failed - Exception");
            throw ex;
        }
    }
    @AfterClass
    public void tearDown() throws Exception {
        stopAppium();
    }

    @Attachment(value = "{attachmentName}", type = "image/png")
    public byte[] getScreenshot(String attachmentName) throws Exception {
        // make screenshot and get is as base64
        return driver.getScreenshotAs(OutputType.BYTES);
    }

    private static void buildDeviceList(List<Object[]> list, String deviceList, String bundleId,
                                        String platformName, String automationName) {
        if (deviceList != null && !deviceList.trim().isEmpty()) {
            for (String device : deviceList.split(",")) {
                list.add(new Object[]{
                        device.trim(), platformName, bundleId, automationName
                });
            }
        }
    }


}