package SumUp;

import java.util.Properties;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import org.apache.log4j.BasicConfigurator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
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
import pojo.Getusers;
import pojo.locale;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class Pojoclass_Validation extends utils{

	
	private static Logger log=LogManager.getLogger(Pojoclass_Validation.class.getName());
    Properties prop = new Properties();
	locale l=new locale();
    
	@BeforeTest
	public void getproperty() throws IOException
	{
		FileInputStream fis = new FileInputStream(System.getProperty("user.dir") + "\\src\\main\\java\\Resources\\Environment.properties");
		prop.load(fis);
	}
	
 //Test Cases//
 
 
	@Step("Implementation of pojo class to validate response")
	@Test(dataProvider = "pojovalidation",priority = 1, description = "POJO Classes")
	@Severity(SeverityLevel.NORMAL)
	@Description("Test Case Description:Validate response json")
	public void jsonschema(String locale) throws IOException {
		l.setLocale(locale);
		RequestSpecification users = given().spec(utils.Reqspec()).body(l);
		Getusers response = users.when().post(resources.generateusers()).then().spec(utils.Resspec()).extract()
				.as(Getusers.class);
		log.debug("User_firstname=:" + response.getUser().getPersonal_profile().getFirst_name());
		log.debug("User_Last_name=:" + response.getUser().getPersonal_profile().getLast_name());
		log.debug("User_Mobile_phone=:" + response.getUser().getPersonal_profile().getMobile_phone());
		log.debug("User_Date_of_birth=:" + response.getUser().getPersonal_profile().getDate_of_birth());
		log.debug("User_National_id=:" + response.getUser().getPersonal_profile().getNational_id());
		log.debug("User_Country=:" + response.getUser().getAddress().getCountry());
		log.debug("User_Address_line1=:" + response.getUser().getAddress().getAddress_line1());
		log.debug("User_Address_line2=:" + response.getUser().getAddress().getAddress_line2());
		log.debug("User_City=:" + response.getUser().getAddress().getCity());
		log.debug("User_Post_code=:" + response.getUser().getAddress().getPost_code());
		log.debug("User_Region_name=:" + response.getUser().getAddress().getRegion_name());
		log.debug("User_Landline=:" + response.getUser().getAddress().getLandline());
		log.debug("User_firstname=:" + response.getUser().getAddress().getFirst_name());
		log.debug("User_Lastname=:" + response.getUser().getAddress().getLast_name());
		log.debug("User_Company=:" + response.getUser().getAddress().getCompany());
		Assert.assertTrue(response.getUser().getCountry().contentEquals(response.getUser().getAddress().getCountry()));
		Assert.assertTrue(response.getUser().getPersonal_profile().getFirst_name().contentEquals(response.getUser().getAddress().getFirst_name()));
		Assert.assertTrue(response.getUser().getPersonal_profile().getLast_name().contentEquals(response.getUser().getAddress().getLast_name()));
		Assert.assertTrue(response.getMessage().contentEquals("user generated"));
		Assert.assertTrue(response.getUser().getCountry().contentEquals(locale));
	
	}
	
	@DataProvider
	public Object[][] pojovalidation()
	{
		Object[][] data=new Object[1][1];
		data[0][0]=prop.getProperty("locale");
		return data;
		
	}
}
