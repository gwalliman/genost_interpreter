package robotinterpreter.variables.vars;

import robotinterpreter.Code;
import robotinterpreter.RobotInterpreter;
import robotinterpreter.variables.BODY;
import robotinterpreter.variables.ID;
import robotinterpreter.variables.Variable;

public class VAR extends Variable 
{
	private String id;
	private VARDECL var;
	
	public VAR(BODY b, Code c, String callCode)
	{
		body = b;
		code = callCode;
		lineNum = c.currentLineNum();
		
		String[] tokens = Code.tokenize(code);
		id = ID.validate(tokens[0], c);
	}
	
	public String id()
	{
		return id;
	}

	public void print() 
	{
		if(id != null)
		{
			System.out.print("var " + id);
		}
		else System.out.print("Empty VARCALL");
	}

	//Ensure that var exists
	public void validate() 
	{
		System.out.println("Validating VAR");

		var = RobotInterpreter.findVar(id);
		if(var == null)
		{
			RobotInterpreter.halt("VAR", lineNum, code, "Var " + id + " is not defined.");
		}
	}

	@Override
	public void execute() {
		// TODO Auto-generated method stub
		
	}
}
