package robotinterpreter.variables.wait;

import robotinterpreter.Code;
import robotinterpreter.RobotInterpreter;
import robotinterpreter.variables.BODY;
import robotinterpreter.variables.Variable;

public class WAITFOR extends Variable
{
	private int iterations;
	private BODY body;
	
	public WAITFOR(Code c)
	{
		code = c.currentLine();
		lineNum = c.currentLineNum();
		
		String[] tokens = Code.tokenize(code);
		
		if(tokens[1].matches("[0-9]+"))
		{
			iterations = Integer.parseInt(tokens[1]);
		}
		else RobotInterpreter.halt("WAITFOR", lineNum, code, "WAITFOR integer is missing or of invalid format.");
		
		c.nextLine();
		body = new BODY(c);
	}
	
	public void print() 
	{
		System.out.println("waitfor " + iterations + " seconds");
		body.print();
	}
}
