package robotinterpreter.variables.loop;

import robotinterpreter.Code;
import robotinterpreter.RobotInterpreter;
import robotinterpreter.variables.BODY;
import robotinterpreter.variables.Variable;

public class LOOPFOR extends Variable
{
	private int iterations;
	private BODY body;
	
	public LOOPFOR(Code c)
	{
		code = c.currentLine();
		lineNum = c.currentLineNum();
		
		String[] tokens = Code.tokenize(code);
		
		if(tokens[1].matches("[0-9]+"))
		{
			iterations = Integer.parseInt(tokens[1]);
		}
		else RobotInterpreter.halt("LOOPFOR", lineNum, code, "LOOPFOR iterations integer is missing or of invalid format.");
		
		c.nextLine();
		body = new BODY(c);
	}
	
	public void print() 
	{
		System.out.println("loopfor " + iterations + " times");
		body.print();
	}

	//Ensure iterations is not negative (this may be taken care of via parsing)
	//Validate body
	public void validate() 
	{
		if(iterations < 0)
		{
			RobotInterpreter.halt("LOOPFOR", lineNum, code, "LOOPFOR iterations integer cannot be negative");
		}
		body.validate();
	}

	@Override
	public void execute() {
		// TODO Auto-generated method stub
		
	}
}
