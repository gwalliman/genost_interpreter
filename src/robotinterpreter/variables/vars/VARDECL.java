package robotinterpreter.variables.vars;

import java.util.Collections;

import robotinterpreter.Code;
import robotinterpreter.Interpreter;
import robotinterpreter.terminals.Terminals;
import robotinterpreter.variables.BODY;
import robotinterpreter.variables.Variable;

/**
 * A VARDECL is a declaration of a variable.
 * It consists of an id and a type (int, string, bool)
 * 
 * When we declare a variable, we add it to the variable table of its parent body.
 * 
 * @author Garret Walliman (gwallima@asu.edu)
 *
 */
public class VARDECL extends Variable 
{
	private Interpreter interpreter;
	
	private String id;
	private String type;
	
	/**
	 * This constructor is used when VARDECL is a STMT.
	 * We get the ID and the type, ensure that it is a valid type, and add it to its parent body's vartable.
	 * 
	 * @param b	the parent body
	 * @param c	the Code object
	 */
	public VARDECL(Interpreter in, BODY b, Code c)
	{
		interpreter = in;
		body = b;
		code = c.currentLine();
		lineNum = c.currentLineNum();
		
		String[] tokens = c.tokenize(code);

		type = tokens[1];
		
		//Ensure that the type is int, string or bool.
		if(!Terminals.dataTypes.contains(type)) interpreter.error("VARDECL", lineNum, code, "Invalid VARDECL data type. Must be int, string, or bool");
		if(type.equals(Terminals.VOID)) interpreter.error("VARDECL", lineNum, code, "Invalid VARDECL data type. Must be int, string, or bool");	
		
		//Get the ID
		id = c.validate(tokens[2], c);
		
		if(tokens[tokens.length - 1] != Terminals.SEMICOLON)
		{
			interpreter.error("VARDECL", lineNum, code, "Missing semicolon");
		}
		
		//Add to the varTable of the parent body
		body.varTable.add(this);
	}
	
	/**
	 * This constructor is used when we are creating VARDECLs out of method parameters.
	 * This is called in METHODDEFINE after parsing a DEFPARAMLIST.
	 * 
	 * Creating VARDECLs out of parameters prevents us from declaring new vars within a method body with the same ID as a parameter.
	 * 
	 * @param i	the id
	 * @param t	the type
	 */
	public VARDECL(Interpreter in, String i, String t) 
	{
		interpreter = in;
		id = i;
		type = t;
	}

	/**
	 * @return	the variable id
	 */
	public String id()
	{
		return id;
	}
	
	/**
	 * @return the variable type
	 */
	public String type()
	{
		return type;
	}
	
	/**
	 * This is a special print function we use for printing the variable tables.
	 */
	public void printVar() 
	{
		if(id != null && type != null)
		{
			interpreter.write("debug", type + " " + id);
		}
		else interpreter.write("debug", "Empty VARDECL");		
	}

	/**
	 * Simple print function
	 */
	public void print() 
	{
		if(id != null && type != null)
		{
			interpreter.write("parse", "vardecl " + type + " " + id);
		}
		else interpreter.write("parse", "Empty VARDECL");
	}
	
	/**
	 * Ensure that the var doesn't exist twice in the same varTable (it can exist in different varTables)
	 */
	public void validate() 
	{
		interpreter.writeln("validate", "Validating VARDECL");

		if(Collections.frequency(body.varTable, interpreter.findVar(body, id)) > 1)
		{
			interpreter.error("VARDECL", lineNum, code, "Var " + id + " cannot be declared more than once!");
		}			
	}

	/**
	 * We never "execute" a VARDECL, so this should never be called.
	 * 
	 * @param args	should always be null
	 * @return	always returns null
	 */
	public Object execute(Object args[]) 
	{
		return null;
	}
}
