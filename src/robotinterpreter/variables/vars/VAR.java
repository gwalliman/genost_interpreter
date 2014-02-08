package robotinterpreter.variables.vars;

import robotinterpreter.Code;
import robotinterpreter.Interpreter;
import robotinterpreter.variables.BODY;
import robotinterpreter.variables.Variable;

/**
 * The VAR class represents a call to a variable. When we call a variable, we simply return its value.
 * All we have here is an id, which can be used to link up the called var to a VARDECL in the parent body varTable.
 * 
 * @author Garret Walliman (gwallima@asu.edu)
 *
 */
public class VAR extends Variable 
{
	private Interpreter interpreter;
	
	private String id;
	private VARDECL var;
	
	/**
	 * Very simple parse function: just get the ID.
	 * 
	 * @param b	the parent body
	 * @param c	the Code file
	 * @param callCode	the code containing the actual variable, of the form "var x"
	 */
	public VAR(Interpreter in, BODY b, Code c, String s)
	{
		interpreter = in;
		body = b;
		code = s;
		lineNum = c.currentLineNum();
		
		String[] tokens = c.tokenize(code);
		id = c.validate(tokens[0], c);
	}
	
	/**
	 * @return	the variable id
	 */
	public String id()
	{
		return id;
	}

	/**
	 * Simple print functions
	 */
	public void print() 
	{
		if(id != null)
		{
			interpreter.write("parse", "var " + id);
		}
		else interpreter.write("parse", "Empty VARCALL");
	}

	/**
	 * Ensure that the var actually exists in the varTable.
	 */
	public void validate() 
	{
		interpreter.writeln("validate", "Validating VAR");

		var = interpreter.findVar(body, id);
		if(var == null)
		{
			interpreter.error("VAR", lineNum, code, "Var " + id + " is not defined.");
		}
	}

	/**
	 * We get the variable's current value from the varTable and return it.
	 * 
	 * @param args	should always be null
	 * @return	the variable's current value
	 */
	public Object execute(Object args[]) 
	{
		return interpreter.getVar(id);
	}
}
