package screens;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.iOSFindBy;
import org.openqa.selenium.By;

public class PhoneScreen extends MainScreen {
    @iOSFindBy(xpath = "//XCUIElementTypeButton[@label='Keypad']")
    private MobileElement keypadToolbarButton;

    @iOSFindBy(xpath = "//XCUIElementTypeButton[@label='Call']")
    private MobileElement callButton;

    @iOSFindBy(id = "End call")
    private MobileElement endCallButton;


    public PhoneScreen(AppiumDriver driver) {
        super(driver);
    }

    public void dialNumber(String phoneNumber){
        keypadToolbarButton.click();

        for (int i = 0; i < phoneNumber.length(); i++) {
            driver.findElement(By.xpath("//XCUIElementTypeButton[@label='" + phoneNumber.charAt(i) + "']")).click();
        }

        callButton.click();
    }

    public Boolean isEndCallButtonPresent(){
        if (endCallButton.isDisplayed()) {
            return true;
        }

        return false;
    }

    public void hangUp(){
        endCallButton.click();
    }
}
