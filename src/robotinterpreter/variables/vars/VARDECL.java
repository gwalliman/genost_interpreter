package robotinterpreter.variables.vars;

import java.util.Collections;

import robotinterpreter.Code;
import robotinterpreter.RobotInterpreter;
import robotinterpreter.terminals.Terminals;
import robotinterpreter.variables.BODY;
import robotinterpreter.variables.ID;
import robotinterpreter.variables.Variable;

public class VARDECL extends Variable {
	
	private String id;
	private String type;
	
	public VARDECL(BODY b, Code c)
	{
		body = b;
		code = c.currentLine();
		lineNum = c.currentLineNum();
		
		String[] tokens = Code.tokenize(code);

		type = tokens[1];
		if(!Terminals.dataTypes.contains(type)) RobotInterpreter.halt("VARDECL", lineNum, code, "Invalid VARDECL data type. Must be int, string, or bool");
		if(type.equals(Terminals.VOID)) RobotInterpreter.halt("VARDECL", lineNum, code, "Invalid VARDECL data type. Must be int, string, or bool");	
		
		id = ID.validate(tokens[2], c);
		
		if(tokens[tokens.length - 1] != Terminals.SEMICOLON)
		{
			RobotInterpreter.halt("VARDECL", lineNum, code, "Missing semicolon");
		}
		
		body.varTable.add(this);
	}
	
	public VARDECL(String i, String t) 
	{
		id = i;
		type = t;
	}

	public String id()
	{
		return id;
	}
	
	public String type()
	{
		return type;
	}

	public void print() 
	{
		if(id != null && type != null)
		{
			RobotInterpreter.write("parse", "vardecl " + type + " " + id);
		}
		else RobotInterpreter.write("parse", "Empty VARDECL");
	}
	
	//Ensure that var doesn't exist twice
	public void validate() 
	{
		RobotInterpreter.writeln("validate", "Validating VARDECL");

		if(Collections.frequency(body.varTable, RobotInterpreter.findVar(body, id)) > 1)
		{
			RobotInterpreter.halt("VARDECL", lineNum, code, "Var " + id + " cannot be declared more than once!");
		}			
	}

	public Object execute(Object args[]) 
	{
		return null;
	}

}
