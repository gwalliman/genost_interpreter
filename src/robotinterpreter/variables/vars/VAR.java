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
			RobotInterpreter.write("parse", "var " + id);
		}
		else RobotInterpreter.write("parse", "Empty VARCALL");
	}

	//Ensure that var exists
	public void validate() 
	{
		RobotInterpreter.writeln("validate", "Validating VAR");

		var = RobotInterpreter.findVar(body, id);
		if(var == null)
		{
			RobotInterpreter.halt("VAR", lineNum, code, "Var " + id + " is not defined.");
		}
	}

	@Override
	public Object execute(Object args[]) 
	{
		return RobotInterpreter.getVar(id);
	}
}
