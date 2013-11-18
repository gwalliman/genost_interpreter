package robotinterpreter.variables;

import robotinterpreter.Code;
import robotinterpreter.RobotInterpreter;
import robotinterpreter.terminals.Terminals;
import robotinterpreter.variables.conditional.IF;
import robotinterpreter.variables.loop.LOOPFOR;
import robotinterpreter.variables.loop.LOOPUNTIL;
import robotinterpreter.variables.methods.METHOD;
import robotinterpreter.variables.methods.METHODDEFINE;
import robotinterpreter.variables.methods.RETURN;
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
				case Terminals.VARDECL:
					stmt = new VARDECL(body, c);
					break;
				case Terminals.ASSIGN:
					stmt = new ASSIGNMENT(body, c);
					break;
				case Terminals.METHODDEFINE:
					stmt = new METHODDEFINE(body, c);
					break;
				case Terminals.METHOD:
					String lastchar = c.currentLine().substring(c.currentLine().length() - 1);
					if(!lastchar.equals(Terminals.SEMICOLON))
						RobotInterpreter.halt("METHOD", lineNum, code, "Missing Semicolon");
					stmt = new METHOD(body, c, c.currentLine().substring(0, c.currentLine().length() - 1));
					break;
				case Terminals.RETURN:
					stmt = new RETURN(body, c);
					break;
				case Terminals.IF:
					stmt = new IF(body, c);
					break;
				case Terminals.ELSEIF:
					RobotInterpreter.halt("STMT", lineNum, code, "ELSEIF must follow IF");
					break;
				case Terminals.ELSE:
					RobotInterpreter.halt("STMT", lineNum, code, "ELSE must follow IF or ELSEIF");
					break;
				case Terminals.LOOPUNTIL:
					stmt = new LOOPUNTIL(body, c);
					break;
				case Terminals.LOOPFOR:
					stmt = new LOOPFOR(body, c);
					break;
				case Terminals.WAITUNTIL:
					stmt = new WAITUNTIL(body, c);
					break;
				case Terminals.WAITFOR:
					stmt = new WAITFOR(body, c);
					break;
			}
		}
		else RobotInterpreter.halt("STMT", lineNum, code, "STMT type is not valid.");
	}
	
	public String type()
	{
		return stmtType;
	}
	
	public void print() 
	{
		switch(stmtType)
		{
			case Terminals.VARDECL:
				((VARDECL)stmt).print();
				break;
			case Terminals.ASSIGN:
				((ASSIGNMENT)stmt).print();
				break;
			case Terminals.METHODDEFINE:
				((METHODDEFINE)stmt).print();
				break;
			case Terminals.METHOD:
				((METHOD)stmt).print();
				break;
			case Terminals.RETURN:
				((RETURN)stmt).print();
				break;
			case Terminals.IF:
				((IF)stmt).print();
				break;
			case Terminals.LOOPUNTIL:
				((LOOPUNTIL)stmt).print();
				break;
			case Terminals.LOOPFOR:
				((LOOPFOR)stmt).print();
				break;
			case Terminals.WAITUNTIL:
				((WAITUNTIL)stmt).print();
				break;
			case Terminals.WAITFOR:
				((WAITFOR)stmt).print();
				break;
		}
	}

	//Validate whatever the statement is
	public void validate() 
	{
		RobotInterpreter.writeln("validate", "Validating STMT");
		switch(stmtType)
		{
			case Terminals.VARDECL:
				((VARDECL)stmt).validate();
				break;
			case Terminals.ASSIGN:
				((ASSIGNMENT)stmt).validate();
				break;
			case Terminals.METHODDEFINE:
				((METHODDEFINE)stmt).validate();
				break;
			case Terminals.METHOD:
				((METHOD)stmt).validate();
				break;
			case Terminals.RETURN:
				((RETURN)stmt).validate();
				break;
			case Terminals.IF:
				((IF)stmt).validate();
				break;
			case Terminals.LOOPUNTIL:
				((LOOPUNTIL)stmt).validate();
				break;
			case Terminals.LOOPFOR:
				((LOOPFOR)stmt).validate();
				break;
			case Terminals.WAITUNTIL:
				((WAITUNTIL)stmt).validate();
				break;
			case Terminals.WAITFOR:
				((WAITFOR)stmt).validate();
				break;
		}	
	}

	@Override
	public Object execute(Object args[]) 
	{
		switch(stmtType)
		{
			case Terminals.VARDECL:
				return ((VARDECL)stmt).execute(null);
			case Terminals.ASSIGN:
				return ((ASSIGNMENT)stmt).execute(null);
			case Terminals.METHODDEFINE:
				//We don't execute the METHODDEFINE, as this will execute the actual method!
				return null;
			case Terminals.METHOD:
				return ((METHOD)stmt).execute(args);
			case Terminals.RETURN:
				return ((RETURN)stmt).execute(null);
			case Terminals.IF:
				return ((IF)stmt).execute(null);
			case Terminals.LOOPUNTIL:
				return ((LOOPUNTIL)stmt).execute(null);
			case Terminals.LOOPFOR:
				return ((LOOPFOR)stmt).execute(null);
			case Terminals.WAITUNTIL:
				return ((WAITUNTIL)stmt).execute(null);
			case Terminals.WAITFOR:
				return ((WAITFOR)stmt).execute(null);
			default:
				return null;
		}
	}
}
