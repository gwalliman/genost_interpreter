package robotinterpreter.variables;

import robotinterpreter.Code;
import robotinterpreter.Interpreter;
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
	private Interpreter interpreter;
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
	public CALL(Interpreter in, BODY b, Code c, String s)
	{
		interpreter = in;
		body = b;
		lineNum = c.currentLineNum();
		code = s;
		
		String[] tokens = c.tokenize(code);
		
		callType = tokens[0];
		
		//Switch statement to determine what type the call is and parse it accordingly.
		//If the call type is not found in the two included arrays, something has gone wrong.
		if(Terminals.callTypes.contains(callType) || (Terminals.dataTypes.contains(callType) && !callType.equals(Terminals.VOID)))
		{
			if (callType.equals(Terminals.VAR))
				call = new VAR(interpreter, body, c, c.implode(tokens, " ", 1));
			else if (callType.equals(Terminals.METHOD))
				call = new METHOD(interpreter, body, c, c.implode(tokens, " ", 0));
			else if (callType.equals(Terminals.INT))
				call = new INTEGER(interpreter, body, c, c.implode(tokens, " ", 1));
			else if (callType.equals(Terminals.STRING))
			{
				code = code.trim();
				call = new STRING(interpreter, body, c, code.substring(6, code.length()).trim());
			}
			else if (callType.equals(Terminals.BOOL))
				call = new BOOLEAN(interpreter, body, c, c.implode(tokens, " ", 1));
		}
		else interpreter.error("CALL", lineNum, code, "Invalid type for variable / method / data literal call");
	}
	
	/**
	 * Gets the datatype returned by the call.
	 * 
	 * @return	a string containing the datatype
	 */
	public String type()
	{
		if (callType.equals(Terminals.VAR))
			return interpreter.findVar(body, ((VAR)call).id()).type();
		else if (callType.equals(Terminals.METHOD))
			return interpreter.findMethod(((METHOD)call).id()).type();
		else if (callType.equals(Terminals.INT))
			return Terminals.INT;
		else if (callType.equals(Terminals.STRING))
			return Terminals.STRING;
		else if (callType.equals(Terminals.BOOL))
			return Terminals.BOOL;
		else
		{
			interpreter.error("CALL", lineNum, code, "Invalid call type");
			return null;
		}
	}

	/**
	 * Prints the call by determining what type it is, and printing the corresponding variable.
	 * 
	 */
	public void print() 
	{
		if (callType.equals(Terminals.VAR))
			((VAR)call).print();
		else if (callType.equals(Terminals.METHOD))
			((METHOD)call).print();
		else if (callType.equals(Terminals.INT))
			((INTEGER)call).print();
		else if (callType.equals(Terminals.STRING))
			((STRING)call).print();
		else if (callType.equals(Terminals.BOOL))
			((BOOLEAN)call).print();
	}

	/**
	 * Simply validates whatever type of call we have, using that variable's own validate function.
	 */
	public void validate() 
	{
		interpreter.writeln("validate",  "Validating CALL");
		if (callType.equals(Terminals.VAR))
			((VAR)call).validate();
		else if (callType.equals(Terminals.METHOD))
			((METHOD)call).validate();
		else if (callType.equals(Terminals.INT))
			((INTEGER)call).validate();
		else if (callType.equals(Terminals.STRING))
			((STRING)call).validate();
		else if (callType.equals(Terminals.BOOL))
			((BOOLEAN)call).validate();
	}


	/**
	 * Simply executes whatever type of call we have, using that variable's own execute function.
	 * 
	 * @param args	should always be null in this context.
	 * @return	an Object containing the value returned by the call. CALLs should ALWAYS return some value!
	 */
	public Object execute(Object args[]) 
	{
		if (callType.equals(Terminals.VAR))
			return ((VAR)call).execute(null);
		else if (callType.equals(Terminals.METHOD))
			return ((METHOD)call).execute(null);
		else if (callType.equals(Terminals.INT))
			return ((INTEGER)call).execute(null);
		else if (callType.equals(Terminals.STRING))
			return ((STRING)call).execute(null);
		else if (callType.equals(Terminals.BOOL))
			return ((BOOLEAN)call).execute(null);
		else
			return null;	
	}
	
}
