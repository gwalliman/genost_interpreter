package robotinterpreter.variables.methods.external;

public class subtract extends ExtMethod 
{
	public subtract()
	{
		id = "subtract";
		type = "int";
		paramTypes = new String[2];
		paramTypes[0] = "int";
		paramTypes[1] = "int";
	}
	
	public Object execute(Object[] args) 
	{
		return ((int)args[0]) - ((int)args[1]);
	}
}
