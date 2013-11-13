package robotinterpreter.variables.methods.external;

public class Print extends ExtMethod 
{
	private String id = "println";
	private String type = "void";
	private String[] paramTypes = { "string" };
	
	public void execute(Object[] args) 
	{
		System.out.println((String)args[0]);
	}

}
