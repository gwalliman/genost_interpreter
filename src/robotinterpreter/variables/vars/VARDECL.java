package robotinterpreter.variables.vars;

import java.util.Collections;

import robotinterpreter.Code;
import robotinterpreter.RobotInterpreter;
import robotinterpreter.terminals.Terminals;
import robotinterpreter.variables.ID;
import robotinterpreter.variables.Variable;

public class VARDECL extends Variable {
	
	private String id;
	private String type;
	
	public VARDECL(Code c)
	{
		code = c.currentLine();
		lineNum = c.currentLineNum();
		
		String[] tokens = Code.tokenize(code);

		type = tokens[1];
		if(!Terminals.dataTypes.contains(type)) RobotInterpreter.halt("VARDECL", lineNum, code, "Invalid VARDECL data type. Must be int, string, or bool");
			
		id = ID.validate(tokens[2], c);
		
		if(tokens[tokens.length - 1] != Terminals.SEMICOLON)
		{
			RobotInterpreter.halt("VARDECL", lineNum, code, "Missing semicolon");
		}
		
		RobotInterpreter.varTable.add(this);
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
			System.out.print("vardecl " + type + " " + id);
		}
		else System.out.print("Empty VARDECL");
	}
	
	//Ensure that var doesn't exist twice
	public void validate() 
	{
		if(Collections.frequency(RobotInterpreter.varTable, RobotInterpreter.findVar(id)) > 1)
		{
			RobotInterpreter.halt("VARDECL", lineNum, code, "Var " + id + " cannot be declared more than once!");
		}			
	}

	@Override
	public void execute() {
		// TODO Auto-generated method stub
		
	}

}
