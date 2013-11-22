package robotinterpreter.variables;

import robotinterpreter.Code;
import robotinterpreter.RobotInterpreter;
import robotinterpreter.terminals.Terminals;
import robotinterpreter.variables.literals.BOOLEAN;
import robotinterpreter.variables.literals.INTEGER;
import robotinterpreter.variables.literals.STRING;
import robotinterpreter.variables.methods.METHOD;
import robotinterpreter.variables.vars.VAR;

/**
 * A CALL can be one of a handful of different items, all of which return data.
 * A CALL may be:
 * 1. A variable call, returning the value of the variable
 * 2. A method call, returning a value. (therefore, a VOID method cannot be a CALL)
 * 3. An int, string or boolean literal, returning that literal.
 * 
 * CALLs may appear on the RHS of an assign statement, as method parameters, or as a standalone statement if it is a method call.
 * 
 * @author Garret Walliman (gwallima@asu.edu)
 *
 */
public class CALL extends Variable 
{
	//The call itself. Can be a METHOD, VAR, INTEGER, STRING or BOOL
	private Object call;
	//The type of call
	private String callType;
	
	/**
	 * Determines what type of call we are dealing with, and parses it accordingly.
	 * 
	 * @param b	the parent body
	 * @param c	the Code object
	 * @param callCode	a string containing the call itself. This code should not contain any other tokens or characters except those involved in the call. Format: "int 0", "var x", "method foo(int 0, var y)"
	 */
	public CALL(BODY b, Code c, String s)
	{
		body = b;
		lineNum = c.currentLineNum();
		code = s;
		
		String[] tokens = Code.tokenize(code);
		
		callType = tokens[0];
		
		//Switch statement to determine what type the call is and parse it accordingly.
		//If the call type is not found in the two included arrays, something has gone wrong.
		if(Terminals.callTypes.contains(callType) || (Terminals.dataTypes.contains(callType) && !callType.equals(Terminals.VOID)))
		{
			switch(callType)
			{
				case Terminals.VAR:
					call = new VAR(body, c, Code.implode(tokens, " ", 1));
					break;
				case Terminals.METHOD:
					call = new METHOD(body, c, Code.implode(tokens, " ", 0));
					break;
				case Terminals.INT:
					call = new INTEGER(body, c, Code.implode(tokens, " ", 1));
					break;
				case Terminals.STRING:
					code = code.trim();
					call = new STRING(body, c, code.substring(6, code.length()).trim());
					break;
				case Terminals.BOOL:
					call = new BOOLEAN(body, c, Code.implode(tokens, " ", 1));
					break;
			}
		}
		else RobotInterpreter.halt("CALL", lineNum, code, "Invalid type for variable / method / data literal call");
	}
	
	/**
	 * Gets the datatype returned by the call.
	 * 
	 * @return	a string containing the datatype
	 */
	public String type()
	{
		switch(callType)
		{
			case Terminals.VAR:
				return RobotInterpreter.findVar(body, ((VAR)call).id()).type();
			case Terminals.METHOD:
				return RobotInterpreter.findMethod(((METHOD)call).id()).type();
			case Terminals.INT:
				return Terminals.INT;
			case Terminals.STRING:
				return Terminals.STRING;
			case Terminals.BOOL:
				return Terminals.BOOL;
		}
		RobotInterpreter.halt("CALL", lineNum, code, "Invalid call type");
		return null;
	}

	/**
	 * Prints the call by determining what type it is, and printing the corresponding variable.
	 * 
	 */
	public void print() 
	{
		switch(callType)
		{
			case Terminals.VAR:
				((VAR)call).print();
				break;
			case Terminals.METHOD:
				((METHOD)call).print();
				break;
			case Terminals.INT:
				((INTEGER)call).print();
				break;
			case Terminals.STRING:
				((STRING)call).print();
				break;
			case Terminals.BOOL:
				((BOOLEAN)call).print();
				break;
		}
	}

	/**
	 * Simply validates whatever type of call we have, using that variable's own validate function.
	 */
	public void validate() 
	{
		RobotInterpreter.writeln("validate",  "Validating CALL");
		switch(callType)
		{
			case Terminals.VAR:
				((VAR)call).validate();
				break;
			case Terminals.METHOD:
				((METHOD)call).validate();
				break;
			case Terminals.INT:
				((INTEGER)call).validate();
				break;
			case Terminals.STRING:
				((STRING)call).validate();
				break;
			case Terminals.BOOL:
				((BOOLEAN)call).validate();
				break;
		}
	}


	/**
	 * Simply executes whatever type of call we have, using that variable's own execute function.
	 * 
	 * @param args	should always be null in this context.
	 * @return	an Object containing the value returned by the call. CALLs should ALWAYS return some value!
	 */
	public Object execute(Object args[]) 
	{
		switch(callType)
		{
			case Terminals.VAR:
				return ((VAR)call).execute(null);
			case Terminals.METHOD:
				return ((METHOD)call).execute(null);
			case Terminals.INT:
				return ((INTEGER)call).execute(null);
			case Terminals.STRING:
				return ((STRING)call).execute(null);
			case Terminals.BOOL:
				return ((BOOLEAN)call).execute(null);
			default:
				return null;
		}		
	}
	
}
