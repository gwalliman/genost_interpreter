package robotinterpreter.variables;
public abstract class Variable 
{
	public String code;
	public int lineNum;
	
	public abstract void print();
	
	public String code()
	{
		return code;
	}
	
	public int lineNum()
	{
		return lineNum;
	}
}
