package robotinterpreter.variables.conditional;

import robotinterpreter.Code;
import robotinterpreter.variables.BODY;
import robotinterpreter.variables.Variable;

public class ELSE extends Variable
{
	private BODY body;
	
	public ELSE(Code c)
	{
		code = c.currentLine();
		lineNum = c.currentLineNum();

		c.nextLine();
		body = new BODY(c);
	}
	
	public void print() 
	{
		System.out.println("else");
		body.print();
	}
}
