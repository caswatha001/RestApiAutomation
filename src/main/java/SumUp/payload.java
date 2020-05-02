package SumUp;

public class payload {
	
	public static String locale(String locale)
	{
		
		String location="{\r\n" + 
				"  \"locale\": \""+locale+"\"\r\n" + 
				"  \r\n" + 
				"}";
		
		return location;
		
	}
	
	public static String emptypayload()
	{
		
		String location="";
		
		return location;
		
	}
}
