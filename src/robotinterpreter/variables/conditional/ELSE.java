package robotinterpreter.variables.conditional;

import robotinterpreter.Code;
import robotinterpreter.RobotInterpreter;
import robotinterpreter.variables.BODY;
import robotinterpreter.variables.Variable;

public class ELSE extends Variable
{
	private BODY codeBody;
	
	public ELSE(BODY b, Code c)
	{
		body = b;
		code = c.currentLine();
		lineNum = c.currentLineNum();

		c.nextLine();
		codeBody = new BODY(body, c);
	}
	
	public void print() 
	{
		RobotInterpreter.writeln("parse", "else");
		codeBody.print();
	}

	//Validate the body.
	public void validate() 
	{
		RobotInterpreter.writeln("validate", "Validating ELSE");

		codeBody.validate();
	}

	@Override
	public void execute() {
		// TODO Auto-generated method stub
		
	}
}
