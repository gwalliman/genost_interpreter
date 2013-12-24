package robotinterpreter.variables.methods;

import robotinterpreter.Code;
import robotinterpreter.Interpreter;
import robotinterpreter.terminals.Terminals;
import robotinterpreter.variables.BODY;
import robotinterpreter.variables.ID;
import robotinterpreter.variables.Variable;

/**
 * The METHOD class is used to call and execute a method.
 * When we parse the METHOD, we get its id and its parameters in the form of a CALLPARAMLIST.
 * When we validate the METHOD, we link the call up to the method definition.
 * When we execute the METHOD, we get the values of the parameters, and send them to the METHODDEFINE for execution.
 * 
 * @author Garret Walliman (gwallima@asu.edu)
 *
 */
public class METHOD extends Variable 
{
	private String id;
	private CALLPARAMLIST params;
	private METHODDEFINE method;
	
	/**
	 * Gets the id of the method and, if we have any params, calls the parser for a CALLPARAMLIST.
	 * 
	 * @param b	the parent body
	 * @param c	the Code file
	 * @param s	the code string containing the method code, of form "method ID OPENPAREN CALLPARAMLLIST CLOSEPAREN". We use a string instead of just reading the line from the Code file, because a METHOD can be called as either a STMT or a CALL.
	 */
	public METHOD(BODY b, Code c, String s)
	{
		body = b;
		
		//Expects method ID OPENPAREN CALLPARAMLLIST CLOSEPAREN
		code = s;
		lineNum = c.currentLineNum();
		
		String[] tokens = Code.tokenize(code);
		
		id = ID.validate(tokens[1], c);
		
		//The third token should always be an OPENPAREN
		if(tokens[2] != Terminals.OPENPAREN)
		{
			Interpreter.error("METHOD", lineNum, code, "ID must be followed by (");
		}
		
		//The last token should always be a CLOSEPAREN
		if(tokens[tokens.length - 1] != Terminals.CLOSEPAREN)
		{
			Interpreter.error("METHOD", lineNum, code, "METHOD header must end with )");
		}
		
		//If the CLOSEPAREN is not the fourth token, then we must have parameters.
		if(tokens[3] != Terminals.CLOSEPAREN)
		{
			//Split by the method open paren. Note that we limit the split to 2 because there may be more openparens in the paramlist.
			String[] callCode = code.split("\\(", 2);
	
			//We send the right half of the split above minus the last character, which is the CLOSEPAREN
			params = new CALLPARAMLIST(body, c, callCode[1].substring(0, callCode[1].length() - 1), this, 0);
		}
		else if(tokens.length != 4)
		{
			Interpreter.error("METHOD", lineNum, code, "Syntax error in METHOD: invalid characters after CLOSEPAREN");
		}
	}
	
	/**
	 * @return	the METHOD id
	 */
	public String id()
	{
		return id;
	}
	
	/**
	 * @return	the METHODDEFINE that this METHOD is calling
	 */
	public METHODDEFINE methDef()
	{
		return method;
	}
	
	/**
	 * Simple print function
	 */
	public void print() 
	{
		Interpreter.write("parse", "method " + id + "(");
		if(params != null)
			params.print();
		Interpreter.write("parse", ")");
	}

	/**
	 * First, ensure that the METHOD we are calling actually exists.
	 * Second, validate the CALLPARAMLIST, if we have one.
	 */
	public void validate() 
	{
		Interpreter.writeln("validate", "Validating METHOD");

		//Look the method up in the method table. If it does not exist, we have a problem.
		method = Interpreter.findMethod(id);
		if(method == null)
		{
			Interpreter.error("METHOD", lineNum, code, "Method " + id + " is not defined.");
		}
		
		//Validate the CALLPARAMLIST if we have one.
		if(params != null)
		{
			params.validate();
		}
	}

	/**
	 * If we have parameters, we must execute the CALLPARAMLIST to get the arguments for the function.
	 * Either way, we call the execute function of the METHODDEFINE this METHOD is referencing.
	 */
	public Object execute(Object[] args) 
	{
		if(params != null)
		{
			return method.execute((Object[]) params.execute(null));
		}
		else 
		{
			return method.execute(null);
		}
	}
}
