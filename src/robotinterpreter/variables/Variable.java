package robotinterpreter.variables;
public abstract class Variable 
{
	public String code;
	public int lineNum;
	
	public abstract void print();
	public abstract void validate();
	public abstract void execute();
	
	public String code()
	{
		return code;
	}
	
	public int lineNum()
	{
		return lineNum;
	}
}
