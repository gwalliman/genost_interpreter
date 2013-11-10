package robotinterpreter;

public class INTEGER extends Variable
{
	private int value;
	
	public INTEGER(Code c, String s)
	{
		code = s;
		lineNum = c.currentLineNum();
		
		String[] tokens = Code.tokenize(s);
		
		if(tokens.length == 1)
		{

			if(tokens[0].matches("[0-9]+"))
			{
				value = Integer.parseInt(tokens[0]);
			}
			else RobotInterpreter.halt("INTEGER", lineNum, code, "Provided integer is of invalid format");
		}
		else RobotInterpreter.halt("INTEGER", lineNum, code, "Syntax error in integer.");
	}

	public void print() 
	{
		System.out.print("int " + value);
	}
}
