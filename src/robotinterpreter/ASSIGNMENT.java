package robotinterpreter;

public class ASSIGNMENT extends Variable 
{
	private String id;
	private CALL call;
	
	public ASSIGNMENT(Code c)
	{
		code = c.currentLine();
		lineNum = c.currentLineNum();
		
		String[] tokens = Code.tokenize(code);
		
		id = ID.validate(tokens[1], c);
		
		if(tokens[2] != Terminals.EQUALS)
		{
			RobotInterpreter.halt("ASSIGNMENT", lineNum, code, "Assignment statement requires an =");
		}
		
		if(tokens[tokens.length - 1] != Terminals.SEMICOLON)
		{
			RobotInterpreter.halt("ASSIGNMENT", lineNum, code, "Missing semicolon");
		}
		
		String rhs = code.split(Terminals.EQUALS)[1];
		call = new CALL(c, rhs.substring(0, rhs.length() - 1));
	}

	
	public void print() 
	{
		System.out.print("assign " + id + " = ");
		call.print();
	}
}
