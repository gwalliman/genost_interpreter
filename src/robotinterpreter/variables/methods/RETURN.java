package robotinterpreter.variables.methods;

import robotinterpreter.Code;
import robotinterpreter.RobotInterpreter;
import robotinterpreter.terminals.Terminals;
import robotinterpreter.variables.BODY;
import robotinterpreter.variables.CALL;
import robotinterpreter.variables.Variable;

/**
 * A return statement will stop execution of a body and return a certain value, defined by a CALL.
 * Note that the RETURN class itself just returns the value. It is up to the BODY/STMTLIST/STMT execution to actually stop execution once a RETURN is executed and returns.
 * 
 * @author Garret Walliman (gwallima@asu.edu)
 *
 */
public class RETURN extends Variable
{
	private String type;
	private CALL call;
	
	/**
	 * Constructor; gets the call.
	 * 
	 * @param b	the parent body
	 * @param c	the Code file
	 */
	public RETURN(BODY b, Code c)
	{
		body = b;
		code = c.currentLine();
		lineNum = c.currentLineNum();
		
		//Parsing CALL
		call = new CALL(body, c, code.substring(7, code.length() - 1));
		
		//Check to ensure that the semicolon is present.
		if(!code.substring(code.length() - 1, code.length()).equals(Terminals.SEMICOLON))
		{
			RobotInterpreter.halt("RETURN", lineNum, code, "Missing semicolon!");
		}
	}

	/**
	 * Simple print function.
	 */
	public void print() 
	{
		RobotInterpreter.write("parse", "return ");
		call.print();
	}
	
	/**
	 * Ensure that the CALL type is the same as the parent method's type.
	 * We also ensure here that the RETURN statement is actually within a method body.
	 */
	public void validate() 
	{
		RobotInterpreter.write("validate", "Validating RETURN");
		
		//Get the type
		//We must wait until validation to determine type, since, if the call is a variable, the vartables may not have been fully populated in parsing.
		type = call.type();
		
		//Validate the CALL
		call.validate();
		
		//Ensure that the RETURN stmt appears in a method body.
		if(body.method == null) RobotInterpreter.halt("RETURN", lineNum, code, "RETURN statement may only appear in a method!");
		
		//Ensure that the RETURN type is proper.
		if(!body.method.type().equals(type)) RobotInterpreter.halt("RETURN", lineNum, code, "Method " + body.method.id() + " returns type " + body.method.type() + ", but RETURN statement is of type " + type);
	}

	/**
	 * Execute the CALL and return the value.
	 * 
	 * @param args	should always be null
	 * @return	returns the CALL's return value.
	 */
	public Object execute(Object[] args) 
	{
		return call.execute(null);
	}
}
