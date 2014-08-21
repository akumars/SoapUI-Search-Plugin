package com.au.ft.helpers;

public final class ClassUtility
{
	
	  private ClassUtility(){
		  }
	  
	public static boolean ClassExists(String className)
	{
	    try
	    {
	    	ClassLoader classLoader = ClassLoader.getSystemClassLoader();
	        Class.forName(className,false,classLoader);
	        return true;
	    }
	    catch(ClassNotFoundException ex)
	    {
	        return false;
	    }
	}

}
