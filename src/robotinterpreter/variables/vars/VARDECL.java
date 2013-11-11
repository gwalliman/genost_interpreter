package robotinterpreter.variables.vars;

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
	}

	public void print() 
	{
		if(id != null && type != null)
		{
			System.out.print("vardecl " + type + " " + id);
		}
		else System.out.print("Empty VARDECL");
	}

}
