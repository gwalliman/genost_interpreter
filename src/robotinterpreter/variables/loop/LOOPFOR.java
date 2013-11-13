package robotinterpreter.variables.loop;

import robotinterpreter.Code;
import robotinterpreter.RobotInterpreter;
import robotinterpreter.variables.BODY;
import robotinterpreter.variables.Variable;

public class LOOPFOR extends Variable
{
	private int iterations;
	private BODY codeBody;
	
	public LOOPFOR(BODY b, Code c)
	{
		body = b;
		code = c.currentLine();
		lineNum = c.currentLineNum();
		
		String[] tokens = Code.tokenize(code);
		
		if(tokens[1].matches("-?[0-9]+"))
		{
			iterations = Integer.parseInt(tokens[1]);
		}
		else RobotInterpreter.halt("LOOPFOR", lineNum, code, "LOOPFOR iterations integer is missing or of invalid format.");
		
		c.nextLine();
		codeBody = new BODY(body, c);
	}
	
	public void print() 
	{
		System.out.println("loopfor " + iterations + " times");
		codeBody.print();
	}

	//Ensure iterations is not negative (this may be taken care of via parsing)
	//Validate body
	public void validate() 
	{
		System.out.println("Validating LOOPFOR");

		if(iterations < -1)
		{
			RobotInterpreter.halt("LOOPFOR", lineNum, code, "LOOPFOR iterations integer cannot be less than -1");
		}
		codeBody.validate();
	}

	@Override
	public void execute() {
		// TODO Auto-generated method stub
		
	}
}
