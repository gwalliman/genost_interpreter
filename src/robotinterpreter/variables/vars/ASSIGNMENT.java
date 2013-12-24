package robotinterpreter.variables.vars;

import robotinterpreter.Code;
import robotinterpreter.Interpreter;
import robotinterpreter.terminals.Terminals;
import robotinterpreter.variables.BODY;
import robotinterpreter.variables.CALL;
import robotinterpreter.variables.ID;
import robotinterpreter.variables.Variable;

/**
 * An ASSIGNMENT assigns the value of a CALL to a variable.
 * The ASSIGNMENT holds the id of the variable being assigned, the CALL, and a link to the VARDECL of the variable being assigned.
 * 
 * @author Garret Walliman (gwallima@asu.edu)
 *
 */
public class ASSIGNMENT extends Variable 
{
	private String id;
	private CALL call;
	private VARDECL lhs;
	
	/**
	 * Gets the id of the lhs variable and, finds the equals sign, and sends everything on the right of the equals sign to the CALL parser.
	 * 
	 * @param b	the parent body
	 * @param c	the Code file
	 */
	public ASSIGNMENT(BODY b, Code c)
	{
		body = b;
		code = c.currentLine();
		lineNum = c.currentLineNum();
		
		String[] tokens = Code.tokenize(code);
		
		id = ID.validate(tokens[1], c);
		
		//Equals should always be the second token.
		if(tokens[2] != Terminals.EQUALS)
		{
			Interpreter.error("ASSIGNMENT", lineNum, code, "Assignment statement requires an =");
		}
		
		if(tokens[tokens.length - 1] != Terminals.SEMICOLON)
		{
			Interpreter.error("ASSIGNMENT", lineNum, code, "Missing semicolon");
		}
		
		//Parsing CALL
		String rhs = code.split(Terminals.EQUALS)[1];
		call = new CALL(body, c, rhs.substring(0, rhs.length() - 1));
	}

	/**
	 * Simple print function
	 */
	public void print() 
	{
		Interpreter.write("parse", "assign " + id + " = ");
		call.print();
	}

	/**
	 * IN THIS ORDER
	 * 1. Validate call
	 * 2. Ensure that lhs exists
	 * 3. Ensure that lhs and rhs are of same type
	 */
	public void validate() 
	{
		Interpreter.writeln("validate", "Validating ASSIGNMENT");
		
		//1. Validating CALL
		call.validate();
		
		//2. Ensure that lhs exists.
		lhs = Interpreter.findVar(body, id);
		if(lhs == null)
		{
			Interpreter.error("ASSIGNMENT", lineNum, code, "Variable " + id + " is not defined.");
		}
		
		//3. Ensure that lhs and rhs are of same type
		String lhsType = lhs.type();
		String rhsType = call.type();
		
		if(!lhsType.equals(rhsType))
		{
			Interpreter.error("ASSIGNMENT", lineNum, code, "LHS and RHS of an assignment must be of same type, but LHS is " + lhsType + " and RHS is " + rhsType);
		}
	}

	/**
	 * We get the value from the call and set the lhs variable in the varStack to that value.
	 * 
	 * @param args	should always be null
	 * @return	always returns null
	 */
	public Object execute(Object args[]) 
	{
		Object val = call.execute(null);
		
		//Note that we don't have to worry about types,
		//This was taken care of in Validation
		Interpreter.setVar(lhs.id(), val);
		
		return null;
	}
}
