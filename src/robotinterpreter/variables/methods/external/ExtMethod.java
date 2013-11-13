package robotinterpreter.variables.methods.external;

import java.util.ArrayList;
import java.util.Arrays;


public abstract class ExtMethod
{
	protected String id;
	protected String type;
	protected String[] paramTypes;
	
	private static String[] extMethodsArray = { "print", "intToString", "add", "subtract", "multiply", "divide"  };
	
	public static ArrayList<String> extMethods = new ArrayList<String>(Arrays.asList(extMethodsArray));

	public abstract Object execute(Object[] args);
	
	public String id()
	{
		return id;
	}
	
	public String type()
	{
		return type;
	}
	
	public String[] paramTypes()
	{
		return paramTypes;
	}
}
