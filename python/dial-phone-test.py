import unittest
from appium import webdriver

class DialPhoneTestCase(unittest.TestCase):

    def setUp(self):
        desired_caps = {}
        desired_caps['deviceConnectUsername'] = 'DEVICECONNECT USERNAME'
        desired_caps['deviceConnectApiKey'] = 'DEVICECONNECT API TOKEN'
        desired_caps['deviceConnectSkipInstall'] = 1
        desired_caps['automationName'] = 'XCUITest'
        desired_caps['udid'] = 'ENTER UDID HERE'
        desired_caps['platformName'] = 'iOS'
        desired_caps['bundleId'] = 'com.apple.mobilephone'
        desired_caps['automationName'] = 'XCUITest'

        self.driver = webdriver.Remote('http://DEVICE.CONNECT.IP.ADDRESS/Appium', desired_caps)
        self.driver.implicitly_wait(15)

    def tearDown(self):
        self.driver.quit()

    def test_dial_phone(self):
        self.driver.find_element_by_xpath("//XCUIElementTypeButton[@label='Keypad']").click()

        for c in "5555555555":
            self.driver.find_element_by_xpath("//XCUIElementTypeButton[@label='" + c + "']").click()

        self.driver.find_element_by_xpath("//XCUIElementTypeButton[@label='Call']").click()

        self.driver.find_element_by_id("End call").click()

suite = unittest.TestLoader().loadTestsFromTestCase(DialPhoneTestCase)
unittest.TextTestRunner(verbosity=2).run(suite)
