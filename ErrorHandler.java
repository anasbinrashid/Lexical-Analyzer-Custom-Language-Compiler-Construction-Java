package Assignment1;

import java.util.*;

public class ErrorHandler 
{
	private List<String> listoferrors;
	
	ErrorHandler()
	{
		listoferrors = new ArrayList<>();
	}
	
    public void reporterror(int line, String message) 
    {
    	String error = "Error at line " + line + ": " + message;
    	listoferrors.add(error);
    }
    
    public void printallerrors()
    {
    	if(listoferrors.isEmpty())
    	{
    		System.out.println("No Errors Found");
    		
    		return;
    	}
    	
        System.out.println("\nErrors Found:\n");
        
        for (String error : listoferrors) 
        {
            System.out.println(error);
        }

    }
}
