package robotinterpreter.variables.methods;

import robotinterpreter.Code;
import robotinterpreter.RobotInterpreter;
import robotinterpreter.terminals.Terminals;
import robotinterpreter.variables.BODY;
import robotinterpreter.variables.CALL;
import robotinterpreter.variables.Variable;

public class RETURN extends Variable
{
	private String type;
	private CALL call;
	
	public RETURN(BODY b, Code c)
	{
		body = b;
		code = c.currentLine();
		lineNum = c.currentLineNum();
		
		call = new CALL(body, c, code.substring(7, code.length() - 1));
		
		if(!code.substring(code.length() - 1, code.length()).equals(Terminals.SEMICOLON))
		{
			RobotInterpreter.halt("RETURN", lineNum, code, "Missing semicolon!");
		}
	}

	public void print() 
	{
		RobotInterpreter.write("parse", "return ");
		call.print();
	}
	
	public void validate() 
	{
		RobotInterpreter.write("validate", "Validating RETURN");
		
		//We must wait until validation to determine type,
		//since the vartables may not have been fully populated in parsing.
		type = call.type();
		
		call.validate();
		if(body.method == null) RobotInterpreter.halt("RETURN", lineNum, code, "RETURN statement may only appear in a method!");
		if(!body.method.type().equals(type)) RobotInterpreter.halt("RETURN", lineNum, code, "Method " + body.method.id() + " returns type " + body.method.type() + ", but RETURN statement is of type " + type);
	}

	public Object execute(Object[] args) 
	{
		return call.execute(null);
	}
}
