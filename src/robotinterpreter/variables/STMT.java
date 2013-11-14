package robotinterpreter.variables;

import robotinterpreter.Code;
import robotinterpreter.RobotInterpreter;
import robotinterpreter.terminals.Terminals;
import robotinterpreter.variables.conditional.IF;
import robotinterpreter.variables.loop.LOOPFOR;
import robotinterpreter.variables.loop.LOOPUNTIL;
import robotinterpreter.variables.methods.METHOD;
import robotinterpreter.variables.methods.METHODDEFINE;
import robotinterpreter.variables.vars.ASSIGNMENT;
import robotinterpreter.variables.vars.VARDECL;
import robotinterpreter.variables.wait.WAITFOR;
import robotinterpreter.variables.wait.WAITUNTIL;

public class STMT extends Variable 
{
	private Object stmt;
	private String stmtType;
	
	public STMT(BODY b, Code c)
	{
		body = b;
		lineNum = c.currentLineNum();
		code = c.currentLine();
		
		String[] type = code.split(" ", 2);
		if(Terminals.stmtTypes.contains(type[0]))
		{
			stmtType = type[0];
			switch(stmtType)
			{
				case "vardecl":
					stmt = new VARDECL(body, c);
					break;
				case "assign":
					stmt = new ASSIGNMENT(body, c);
					break;
				case "methoddefine":
					stmt = new METHODDEFINE(body, c);
					break;
				case "method":
					String lastchar = c.currentLine().substring(c.currentLine().length() - 1);
					if(!lastchar.equals(Terminals.SEMICOLON))
						RobotInterpreter.halt("METHOD", lineNum, code, "Missing Semicolon");
					stmt = new METHOD(body, c, c.currentLine().substring(0, c.currentLine().length() - 1));
					break;
				case "if":
					stmt = new IF(body, c);
					break;
				case "elseif":
					RobotInterpreter.halt("STMT", lineNum, code, "ELSEIF must follow IF");
					break;
				case "else":
					RobotInterpreter.halt("STMT", lineNum, code, "ELSE must follow IF or ELSEIF");
					break;
				case "loopuntil":
					stmt = new LOOPUNTIL(body, c);
					break;
				case "loopfor":
					stmt = new LOOPFOR(body, c);
					break;
				case "waituntil":
					stmt = new WAITUNTIL(body, c);
					break;
				case "waitfor":
					stmt = new WAITFOR(body, c);
					break;
			}
		}
		else RobotInterpreter.halt("STMT", lineNum, code, "STMT type is not valid.");
	}
	
	public void print() 
	{
		switch(stmtType)
		{
			case "vardecl":
				((VARDECL)stmt).print();
				break;
			case "assign":
				((ASSIGNMENT)stmt).print();
				break;
			case "methoddefine":
				((METHODDEFINE)stmt).print();
				break;
			case "method":
				((METHOD)stmt).print();
				break;
			case "if":
				((IF)stmt).print();
				break;
			case "loopuntil":
				((LOOPUNTIL)stmt).print();
				break;
			case "loopfor":
				((LOOPFOR)stmt).print();
				break;
			case "waituntil":
				((WAITUNTIL)stmt).print();
				break;
			case "waitfor":
				((WAITFOR)stmt).print();
				break;
		}
	}

	//Validate whatever the statement is
	public void validate() 
	{
		RobotInterpreter.writeln("Validating STMT");
		switch(stmtType)
		{
			case "vardecl":
				((VARDECL)stmt).validate();
				break;
			case "assign":
				((ASSIGNMENT)stmt).validate();
				break;
			case "methoddefine":
				((METHODDEFINE)stmt).validate();
				break;
			case "method":
				((METHOD)stmt).validate();
				break;
			case "if":
				((IF)stmt).validate();
				break;
			case "loopuntil":
				((LOOPUNTIL)stmt).validate();
				break;
			case "loopfor":
				((LOOPFOR)stmt).validate();
				break;
			case "waituntil":
				((WAITUNTIL)stmt).validate();
				break;
			case "waitfor":
				((WAITFOR)stmt).validate();
				break;
		}	
	}

	@Override
	public void execute() {
		// TODO Auto-generated method stub
		
	}
}
