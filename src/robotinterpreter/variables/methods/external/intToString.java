package robotinterpreter.variables.methods.external;

public class intToString extends ExtMethod 
{
	public intToString()
	{
		id = "intToString";
		type = "string";
		paramTypes = new String[1];
		paramTypes[0] = "int";
	}
	
	public Object execute(Object[] args) 
	{
		return Integer.toString(((int)args[0]));
	}
}
