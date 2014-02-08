package robotinterpreter.variables.methods;

import robotinterpreter.Code;
import robotinterpreter.Interpreter;
import robotinterpreter.terminals.Terminals;
import robotinterpreter.variables.BODY;
import robotinterpreter.variables.CALL;
import robotinterpreter.variables.Variable;

/**
 * A CALLPARAMLIST is a series of CALLs that are passed into a METHOD as parameters.
 * Each CALL must match the datatype of its respective parameter. There also must be the same number of CALLs as there are params.
 * 
 * Like DEFPARAMLIST, a CALLPARAMLIST is a linked list, where each CALLPARAMLIST links to the next one.
 * 
 * @author Garret Walliman (gwallima@asu.edu)
 */
public class CALLPARAMLIST extends Variable
{
	private Interpreter interpreter;
	
	private CALL call;
	private CALLPARAMLIST nextParam;
	
	//We link each CALLPARAMLIST to the METHOD, for convenience in validation.
	private METHOD method;
	private int paramNum;
	
	/**
	 * The constructor parses one CALL, then recursively calls itself to parse the next one.
	 * In order to parse the CALL, we have to isolate a string containing the CALL code.
	 * Once we have done so, we pass it to the CALL parser, and pass the remaining string to the CALLPARAMLIST constructor.
	 * 
	 * @param b	the parent body
	 * @param c	the Code file
	 * @param s	a string containing the parameter calls, separated by commas. Format: "var x, int 0, string "asdf", bool true, method foo(var x, int 5)"
	 * @param m	a reference to the method this CALLPARAMLIST is linked to.
	 * @param p	an integer indicating what number CALLPARAMLIST this is.
	 */
	public CALLPARAMLIST(Interpreter in, BODY b, Code c, String s, METHOD m, int p) 
	{
		interpreter = in;
		body = b;
		method = m;
		paramNum = p;
		lineNum = c.currentLineNum();
		code = c.currentLine();
		
		String tokens[] = c.tokenize(s);
		
		//We create two strings: "argument" is the current CALL code, and "remainder" is the remaining argument(s)
		String argument = "";
		String remainder = "";
		
		//The easiest thing to do would be to split the code by comma (as this separates each CALL).
		//However, there are two cases where a CALL may have a comma within it:
		//1. The CALL is a method with parameters, in which case the CALL's CALLPARAMLIST will have commas
		//2. The CALL is a string literal, which may have a comma in it.
		//So, we deal with cases 1 and 2 if they arise, and if not, we simply split by comma.
		
		//Case 1
		if(tokens[0].equals(Terminals.METHOD))
		{
			//We need to find the limits of the method call.
			//We do this by finding the close paren to this method's CALLPARAMLIST; the character immediately after this will be the comma, and hence the end of the CALL.
			if(tokens[2] != Terminals.OPENPAREN)
			{
				interpreter.error("CALLPARAMLIST", lineNum, code, "METHOD argument must have open paren following ID!");
			}
			
			//The closeparen index is at least 2 (0 is "method", 1 is open paren)
			int closeParen = 2;
			int counter = 1;
			
			//Go through each token and find the corresponding closeparen.
			//We either exit when we have found it, or when closeParen has gone out of bounds.
			while(counter != 0 && closeParen < tokens.length)
			{
				closeParen++;
				if(tokens[closeParen].equals(Terminals.OPENPAREN)) counter++;
				else if(tokens[closeParen].equals(Terminals.CLOSEPAREN)) counter--;
			}
			
			//If closeParen has not gone out of bounds, we have found our closing paren.
			if(tokens.length > closeParen)
			{
				//The argument is everything from the beginning to the closeparen
				argument = c.implode(tokens, " ", 0, closeParen);

				//The remainder is everything after the comma following the closeparen to the end of the string.
				//We do codeParen + 2 to skip over the comma
				remainder = c.implode(tokens, " ", closeParen + 2, tokens.length - 1);
			}
			else
			{
				interpreter.error("CALLPARAMLIST", lineNum, code, "METHOD argument does not have corresponding closeparen!");
			}
		}
		//Case 2
		else if(tokens[0].equals(Terminals.STRING))
		{
			//The argument is just the second token.
			argument = tokens[0] + " " + tokens[1];
			//The remainder is tokens 3 through end.
			remainder = c.implode(tokens, " ", 2, tokens.length - 1);
		}
		//If Case 1 and 2 both fail, then we have either a var, an int or a boolean, which can be safely split by comma (once)
		else
		{
			String[] t = s.split(Terminals.COMMA, 2);
			argument = t[0];
			if(t.length > 1)
			{
				remainder = t[1];
			}
		}
		
		//Ensure that we actually have an argument; if so, parse the CALL
		if(argument.trim().length() > 0)
		{
			call = new CALL(interpreter, body, c, argument);
		}
		else interpreter.error("CALLPARAMLIST", lineNum, code, "Syntax error in CALLPARAMLIST");

		//If we have a remainder, recursively call CALLPARAMLIST
		if(!remainder.isEmpty())
		{
			nextParam = new CALLPARAMLIST(interpreter, body, c, remainder, m, ++p);
		}
	}
	
	/**
	 * Simple print function
	 */
	public void print() 
	{
		interpreter.write("parse", paramNum + " ");
		call.print();
			
		if(nextParam != null)
		{
			interpreter.write("parse", ", ");
			nextParam.print();
		}
	}


	/**
	 * 1. Validate call
	 * 2. Ensure that call should exist at all (what if param doesn't exist?)
	 * 3. Ensure that call is of right type.
	 * 4. Validate next call
	 */
	public void validate() 
	{
		interpreter.writeln("validate", "Validating CALLPARAMLIST");

		//1: Validate CALL
		call.validate();
		METHODDEFINE methdef = interpreter.findMethod(method.id());
		
		//As the method should always be validated before the calllist, it should always be non-null, but just in case we check.
		if(methdef != null)
		{	
			//2: Ensure that call should exist in the first place
			DEFPARAMLIST paramdef = methdef.getParam(paramNum);
			if(paramdef == null)
			{
				interpreter.error("CALLPARAMLIST", lineNum, code, "Parameter " + paramNum + " does not exist for method " + methdef.id());
			}
			else
			{
				//3: Ensure that CALL is of right type
				String callType = call.type();
				String defType = paramdef.type();
				if(!callType.equals(defType))
				{
					interpreter.error("CALLPARAMLIST", lineNum, code, "Parameter " + paramNum + " is of wrong type. Method " + methdef.id() + " parameter " + paramNum + " requires " + defType + ", but was provided " + callType);
				}
			}
		}
		
		//4: validate next call, if it exists
		if(nextParam != null)
		{
			nextParam.validate();
		}
	}

	/**
	 * We recursively call execute to build an Object array of arguments.
	 * 
	 * @param	When calling, this should be null
	 * @return	an Object array of size numParams containing the parameter values
	 */
	public Object execute(Object[] args) 
	{
		//If args is null, this is our first time calling, so we initialize a new array of the right size.
		if(args == null)
		{
			args = new Object[method.methDef().numParams()];
		}
		
		//Get the CALL value
		args[paramNum] = call.execute(null);
		
		//Get the next CALL value, if it exists.
		if(nextParam != null)
		{
			return nextParam.execute(args);
		}
		else return args;
	}
}
