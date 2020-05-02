package SumUp;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Properties;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

public class utils {
	
	Properties prop = new Properties();
	
	public static String getGlobalValue(String key) throws IOException {
		Properties prop = new Properties();
		FileInputStream fis = new FileInputStream(
				System.getProperty("user.dir") + "\\src\\main\\java\\Resources\\Environment.properties");
		prop.load(fis);
		return prop.getProperty(key);
	}
	
	public static RequestSpecification Reqspec() throws IOException
	{
		PrintStream log =new PrintStream(new FileOutputStream("logging.txt"));
		RequestSpecification req=new RequestSpecBuilder().setBaseUri(getGlobalValue("BaseURI"))
				.addFilter(RequestLoggingFilter.logRequestTo(log))
				 .addFilter(ResponseLoggingFilter.logResponseTo(log)).
				setContentType(ContentType.JSON).build();
		return req;
	    
		}
	
	
	public static ResponseSpecification Resspec()
	{
		ResponseSpecification validresspec=new ResponseSpecBuilder().expectStatusCode(200).expectContentType(ContentType.JSON).build();
	    	return validresspec;
	    
		}
	
	public static ResponseSpecification invalidResspec()
	{
		ResponseSpecification invalidresspec=new ResponseSpecBuilder().expectStatusCode(400).expectContentType(ContentType.JSON).build();
 	return invalidresspec;
	    
		}
	
	public static ResponseSpecification internalservererror()
	{
		ResponseSpecification invalidresspec=new ResponseSpecBuilder().expectStatusCode(500).expectContentType(ContentType.JSON).build();
 	return invalidresspec;
	    
		}

}
