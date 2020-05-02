package SumUp;

import java.util.Properties;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import Resources.Getexceldata;

import static io.restassured.RestAssured.*;
import io.restassured.specification.*;
import io.restassured.builder.*;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import junit.framework.Assert;

public class getusers extends utils {

	private static Logger log = LogManager.getLogger(getusers.class.getName());
	Properties prop = new Properties();

	@BeforeTest
	public void getproperty() throws IOException {
		FileInputStream fis = new FileInputStream(
				System.getProperty("user.dir") + "\\src\\main\\java\\Resources\\Environment.properties");
		prop.load(fis);
	}

	// Test Cases//

	// **Test Case 1: Valid Locale **//

	@Step("RequestType:Post,RequestName:User details with locale:{0}")
	@Test(dataProvider = "getlocale", priority = 1, description = "User Generation with Uppercase locale")
	@Severity(SeverityLevel.CRITICAL)
	@Description("Test Case Description:The response shows the user details for UpperCase Locale")
	public void validlocale(String locale) throws IOException {

		RequestSpecification users = given().spec(utils.Reqspec()).body(payload.locale(locale));
		Response response = users.when().post(resources.generateusers()).then().spec(utils.Resspec()).extract()
				.response();
		log.debug("Test Case 1_Caps locale= " + locale + ":" + response.asString());
        JsonPath js = new JsonPath(response.asString());
		Assert.assertTrue(js.getString("message").equals("user generated"));
		Assert.assertTrue(js.getString("user.country").equals(locale));
		Assert.assertTrue(js.getString("user.address.country").equals(locale));
		Assert.assertEquals(js.getInt("user.size()"), 3);
		Assert.assertEquals(js.getInt("user.personal_profile.size()"), 5);
		Assert.assertEquals(js.getInt("user.address.size()"), 10);
		Assert.assertEquals(js.getString("user.address.first_name"), js.getString("user.personal_profile.first_name"));
		Assert.assertEquals(js.getString("user.address.last_name"), js.getString("user.personal_profile.last_name"));
	}

    //**Test Case 2:  Case Sensitive Valid Locale **//

	@Step("RequestType:Post,RequestName:User details with locale:{0}")
	@Test(dataProvider = "getlocale", priority = 2, description = "User Generation with Lowercase locale")
	@Severity(SeverityLevel.NORMAL)
	@Description("Test Case Description:The response shows the user details for LowerCase Locale")
	public void casesensitivelocale(String locale) throws IOException {

		RequestSpecification users = given().spec(utils.Reqspec()).body(payload.locale(locale.toLowerCase()));
		Response response = users.when().post(resources.generateusers()).then().spec(utils.Resspec()).extract()
				.response();
		log.debug("Test Case 2_Small locale= " + locale + ":" + response.asString());
		JsonPath js = new JsonPath(response.asString());
		Assert.assertTrue(js.getString("message").equals("user generated"));
		Assert.assertTrue(js.getString("user.country").equals(locale.toUpperCase()));
		Assert.assertTrue(js.getString("user.address.country").equals(locale.toUpperCase()));
		Assert.assertEquals(js.getInt("user.size()"), 3);
		Assert.assertEquals(js.getInt("user.personal_profile.size()"), 5);
		Assert.assertEquals(js.getInt("user.address.size()"), 10);
		Assert.assertEquals(js.getString("user.address.first_name"), js.getString("user.personal_profile.first_name"));
		Assert.assertEquals(js.getString("user.address.last_name"), js.getString("user.personal_profile.last_name"));
	}

     //**Test Case 3:  Blank Locale**//

	@Step("RequestType:Post,RequestName:User details with locale ' ' ")

	@Test(priority = 3, description = "User Generation with blank locale")

	@Severity(SeverityLevel.NORMAL)

	@Description("Test Case Description:The response shows 400 for null input")
	public void blanklocale() throws IOException {

		RequestSpecification users = given().spec(utils.Reqspec()).body(payload.locale(""));
		Response response = users.when().post(resources.generateusers()).then().spec(utils.invalidResspec()).extract()
				.response();
		log.debug("Test Case 3_blank locale=:" + response.asString());
		JsonPath js = new JsonPath(response.asString());
		Assert.assertFalse(js.getString("message").equals("user generated"));

	}

	// **Test Case 4: Locale with space**//

	@Step("RequestType:Post,RequestName:User details with locale:{0}")

	@Test(dataProvider = "localespace", priority = 4, description = "User Generation with space in locale characters")

	@Severity(SeverityLevel.NORMAL)

	@Description("Test Case Description:The response shows 400 for locale with blank space")
	public void localewithspace(String locale) throws IOException {

		RequestSpecification users = given().spec(utils.Reqspec()).body(payload.locale(locale));
		Response response = users.when().post(resources.generateusers()).then().spec(utils.invalidResspec()).extract()
				.response();
		log.debug("Test Case 4_locale with space=:" + response.asString());
		JsonPath js = new JsonPath(response.asString());
		Assert.assertFalse(js.getString("message").equals("user generated"));

	}

	// **Test Case 5: Invalid locale**//

	@Step("RequestType:Post,RequestName:User details with locale:{0}")

	@Test(dataProvider = "Invalidlocale", priority = 5, description = "User Generation with invalid locale")

	@Severity(SeverityLevel.NORMAL)

	@Description("Test Case Description:The response shows 400 for invalid json")
	public void invalidlocale(String locale) throws IOException {

		RequestSpecification users = given().spec(utils.Reqspec()).body(payload.locale(locale));
		Response response = users.when().post(resources.generateusers()).then().spec(utils.invalidResspec()).extract()
				.response();
		log.debug("Test Case 5_invalid locale=:" + response.asString());
		JsonPath js = new JsonPath(response.asString());
		Assert.assertFalse(js.getString("message").equals("user generated"));

	}

	// **Test Case 6: no payload **//
	@Step("RequestType:Post,RequestName:User details with no payload")
	@Test(priority = 6, description = "User Generation with no payload")
	@Severity(SeverityLevel.NORMAL)
	@Description("Test Case Description:The response shows 500 for no payload")
	public void nopayload() throws IOException {
		RequestSpecification users = given().spec(utils.Reqspec());
		Response response = users.when().post(resources.generateusers()).then().spec(utils.internalservererror())
				.extract().response();
		log.debug("Test Case 6_no payload=:" + response.asString());
		JsonPath js = new JsonPath(response.asString());
		Assert.assertTrue(js.getString("message").equals("user not generated"));
	}

	// **Test Case 7: Response body schema**//
	@Step("RequestType:Post,RequestName:User details")
	@Test(dataProvider = "jsonschemadata", priority = 7, description = "jsonschema validation")
	@Severity(SeverityLevel.NORMAL)
	@Description("Test Case Description:Validate response json schema")
	public void jsonschema(String locale) throws IOException {

		RequestSpecification users = given().spec(utils.Reqspec()).body(payload.locale(locale));
		Response response = users.when().post(resources.generateusers()).then().spec(utils.Resspec()).extract()
				.response();
		JsonPath js = new JsonPath(response.asString());
		Assert.assertTrue(js.getString("user.personal_profile").contains("first_name"));
		Assert.assertTrue(js.getString("user.personal_profile").contains("last_name"));
		Assert.assertTrue(js.getString("user.personal_profile").contains("mobile_phone"));
		Assert.assertTrue(js.getString("user.personal_profile").contains("date_of_birth"));
		Assert.assertTrue(js.getString("user.personal_profile").contains("national_id"));
		Assert.assertTrue(js.getString("user.address").contains("address_line1"));
		Assert.assertTrue(js.getString("user.address").contains("address_line2"));
		Assert.assertTrue(js.getString("user.address").contains("city"));
		Assert.assertTrue(js.getString("user.address").contains("post_code"));
		Assert.assertTrue(js.getString("user.address").contains("region_name"));
		Assert.assertTrue(js.getString("user.address").contains("first_name"));
		Assert.assertTrue(js.getString("user.address").contains("last_name"));
		Assert.assertTrue(js.getString("user.address").contains("company"));

	}

//Data from excel sheet
	@DataProvider
	public Object[][] getlocale() {
		Getexceldata excel = new Getexceldata();
		Object[][] data = excel
				.getExcelData(System.getProperty("user.dir") + "\\src\\main\\java\\Resources\\TestData.xls", "Sheet1");
		return data;

	}

	@DataProvider
	public Object[][] localespace() {
		Getexceldata excel = new Getexceldata();
		Object[][] data = excel
				.getExcelData(System.getProperty("user.dir") + "\\src\\main\\java\\Resources\\TestData.xls", "Sheet2");
		return data;

	}

	@DataProvider
	public Object[][] Invalidlocale() {
		Getexceldata excel = new Getexceldata();
		Object[][] data = excel
				.getExcelData(System.getProperty("user.dir") + "\\src\\main\\java\\Resources\\TestData.xls", "Sheet3");
		return data;

	}

//Data from propertyfile
	@DataProvider
	public Object[][] jsonschemadata() {
		Object[][] data = new Object[1][1];
		data[0][0] = prop.getProperty("locale");
		return data;

	}
}
