package robotinterpreter.variables.methods.external;

/**
 * This abstract class should be extended by any external method class.
 * It contains all the values that must be populated by the class to function in the program. These values are:
 * 1. The method ID
 * 2. The method return type (void, int, string, bool)
 * 3. An array containing parameter types
 * 
 * Additionally, the external method must implement the execute() function!
 * 
 * Finally, to register an external method as usable, its id must be added to the extMethodsArray array below.
 * 
 * @author Garret Walliman (gwallima@asu.edu)
 *
 */
public abstract class ExtMethod
{
	protected String id;
	protected String type;
	protected String[] paramTypes;
	
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
