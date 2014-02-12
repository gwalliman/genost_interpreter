package robotinterpreter.variables.methods;

import java.util.ArrayList;

import robotinterpreter.Code;
import robotinterpreter.Interpreter;
import robotinterpreter.terminals.Terminals;
import robotinterpreter.variables.BODY;
import robotinterpreter.variables.Variable;

/**
 * A DEFPARAMLIST is a linklist to hold method parameters.
 * These are the parameter DEFINITIONS, not the parameters in a method call.
 * 
 * Each DEFPARAMLIST contains:
 * 1. The type of the parameter (int, string, or bool)
 * 2. The id of the parameter
 * 3. The number of the parameter
 * 4. A link to the next param
 * 
 * @author Garret Walliman (gwallima@asu.edu)
 *
 */
public class DEFPARAMLIST extends Variable
{
	private Interpreter interpreter;
	
	private String paramType;
	private String id;
	private int paramNum;
	private DEFPARAMLIST nextParam;

	/**
	 * Constructor for internal method param lists.
	 * We recursively go through a string of parameters, getting the type and ID of each one.
	 * 
	 * @param b	the parent body
	 * @param c	the Code file
	 * @param s	a string consisting solely of the parameters, separated by commas. Format: "int x", "int x, string z, bool a"
	 * @param p	the param number. The first time the constructor is called, this should be 0.
	 */
	public DEFPARAMLIST(Interpreter in, BODY b, Code c, String s, int p) 
	{
		interpreter = in;
		body = b;
		paramNum = p;
		lineNum = c.currentLineNum();
		code = c.currentLine();
		String tokens[] = c.tokenize(s);

		//We should always have at least two tokens (a type and an id)
		if(tokens.length > 1)
		{
		
			//Parsing TYPE
			//Get the datatype, ensure that it is valid and not void.
			paramType = tokens[0];
			if(!Terminals.dataTypes.contains(paramType) || paramType.equals(Terminals.VOID)) interpreter.error("DEFPARAMLIST", lineNum, code, "Invalid DEFPARAMLIST data type. Must be int, string, or bool");
			
			//Parsing ID
			//Get the id
			id = c.validate(tokens[1], c);
	
			//If we have at least four tokens, then we know we have another parameter
			if(tokens.length > 3)
			{
				//The third token must be a comma.
				if(tokens[2].equals(Terminals.COMMA))
				{
					//We recursively call this constructor after removing the string containing the parameter we have just defined from the code.
					nextParam = new DEFPARAMLIST(interpreter, body, c, c.implode(tokens, " ", 3, tokens.length - 1), ++p);
				}
				else interpreter.error("DEFPARAMLIST", lineNum, code, "There must be a comma between each parameter in a DEFPARAMLIST");
			}
		}
		else interpreter.error("DEFPARAMLIST", lineNum, code, "Syntax error in DEFPARAMLIST");
	}
	
	/**
	 * A special constructor for external methods parameter definitions.
	 * 
	 * We take an ArrayList of type String. This ArrayList should contain entries for the parameter types, in order.
	 * For external methods, we don't care what the parameters are actually named, so we just set a default param id.
	 * The type is what matters for validation when we call this method.
	 * 
	 * @param params	an ArrayList of type String containing, in order, entries for the parameter types. This is retrieved from the ExtMethod's class.
	 * @param p	the param number. The first time the constructor is called, this should be 0.
	 */
	public DEFPARAMLIST(Interpreter in, ArrayList<String> params, int p)
	{
		interpreter = in;
		
		//Create some dummy id; it will never actually be used due to how we call the external method's id.
		id = "param" + p;
		paramNum = p;
		
		//Get the paramType from the ArrayList
		paramType = params.remove(0);
		
		//If we still have params, go on to the next one.
		if(params.size() > 0)
		{
			nextParam = new DEFPARAMLIST(interpreter, params, ++p);
		}
	}
	
	/**
	 * @return	the parameter id
	 */
	public String id()
	{
		return id;
	}
	
	/**
	 * @return	the parameter datatype
	 */
	public String type()
	{
		return paramType;
	}
	
	/**
	 * It's not funny, Liam!
	 * 
	 * @return	the parameter number
	 */
	public int getNum() 
	{
		return paramNum;
	}

	/**
	 * @return	the next parameter in the linked list.
	 */
	public DEFPARAMLIST nextParam() 
	{
		return nextParam;
	}
	
	/**
	 * Prints the parameter and, if we have another, calls that param's print function.
	 * 
	 */
	public void print() 
	{
		interpreter.write("parse", paramNum + " " + paramType + " " + id);
			
		if(nextParam != null)
		{
			interpreter.write("parse", ", ");
			nextParam.print();
		}
	}

	/**
	 * We actually don't have anything to validate at this time.
	 * 
	 */
	public void validate() 
	{
		interpreter.writeln("validate", "Validating DEFPARAMLIST");

		if(nextParam != null)
		{
			nextParam.validate();
		}
	}

	/**
	 * We never "execute" a DEFPARAMLIST, so this method should never be called.
	 * 
	 * @param args	should always be null
	 * @return	always returns null
	 */
	public Object execute(Object[] args) 
	{
		return null;
	}
}
