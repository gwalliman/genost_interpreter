package robotinterpreter.variables.methods.external;

public class print extends ExtMethod 
{
	public print()
	{
		id = "print";
		type = "void";
		paramTypes = new String[1];
		paramTypes[0] = "string";
	}
	
	public Object execute(Object[] args) 
	{
		System.out.println((String)args[0]);
		return null;
	}
}
