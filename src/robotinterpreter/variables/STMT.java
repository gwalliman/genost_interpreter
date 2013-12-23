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

/**
 * A STMT is a single unit of functional code. One STMT performs one function.
 * STMT types are:
 * 1. Variable declarations (VARDECLs)
 * 2. Assigning some value to a variable (ASSIGNs)
 * 3. Method definitions, including definition of return type, parameters, and the code body (METHODDEFINEs)
 * 4. Method calls, including the parameters (METHODs)
 * 5. Return statements, which exit a method call (RETURNs)
 * 6. Conditional statements, including the condition, the body, and any ELSEIFs or ELSEs associated with it (IFs)
 * 7. Sections of code which loop until a certain condition is met (LOOPUNTILs)
 * 8. Sections of code which loop for a certain number of times (LOOPFORs)
 * 9. Statements which halt execution until a certain condition is met (WAITUNTILs)
 * 10. Statements which halt execution for a certain amount of time (WAITFORs)
 * 
 * @author Garret
 *
 */
public class STMT extends Variable 
{
	//The STMT itself.
	private Object stmt;
	//The type of statement
	private String stmtType;
	
	/**
	 * Determines what type of statement we have, and calls that corresponding type's parse function on the statement code.
	 * 
	 * @param b	the parent body
	 * @param c	the Code object
	 */
	public STMT(BODY b, Code c)
	{
		body = b;
		lineNum = c.currentLineNum();
		code = c.currentLine();
		
		//All valid statements will have, as their first token, the statement type, separated from the rest of the code by a space.
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
				//A METHOD is the only STMT which can be both a standalone STMT and a CALL.
				//Therefore the METHOD code expects there to be no ending semicolon, and so we process that semicolon here.
				case Terminals.METHOD:
					//Ensure that the semicolon exists. If it does, remove it and call the METHOD parser.
					String lastchar = c.currentLine().substring(c.currentLine().length() - 1);
					if(!lastchar.equals(Terminals.SEMICOLON))
						RobotInterpreter.error("METHOD", lineNum, code, "Missing Semicolon");
					stmt = new METHOD(body, c, c.currentLine().substring(0, c.currentLine().length() - 1));
					break;
				case Terminals.RETURN:
					stmt = new RETURN(body, c);
					break;
				case Terminals.IF:
					stmt = new IF(body, c);
					break;
				//ELSEIFs and ELSEs should be handled in the IF code.
				//If we find a separate statement beginning with ELSEIF or ELSE, we must halt.
				case Terminals.ELSEIF:
					RobotInterpreter.error("STMT", lineNum, code, "ELSEIF must follow IF");
					break;
				case Terminals.ELSE:
					RobotInterpreter.error("STMT", lineNum, code, "ELSE must follow IF or ELSEIF");
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
		else RobotInterpreter.error("STMT", lineNum, code, "STMT type is not valid.");
	}
	
	/**
	 * Simple getter function, returns the statement type.
	 * 
	 * @return	the statement type
	 */
	public String type()
	{
		return stmtType;
	}
	
	/**
	 * @return	the stmt object itself.
	 */
	public Object getStmt()
	{
		return stmt;
	}
	
	/**
	 * Printing function, determines what type of stmt this is ands calls that type's print function.
	 */
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

	/**
	 * Simple validation function, determines what kind of statement we have, and calls that type's validation function.
	 */
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

	/**
	 * Determines what type of statement we have, and calls that statement's execute function.
	 * Note that we do not execute METHODDEFINE, as this will execute the METHODDEFINE's code body, which should not happen until that method is actually called!
	 * 
	 * @param args	any arguments passed to the statement. Only a METHOD stmt will have any valid arguments; otherwise, this should be null
	 * @return	no STMT should ever return any values, so this should always be null (though ultimately what is returned is handled by the statement types themselves)
	 */
	public Object execute(Object args[]) 
	{
		if(Thread.interrupted())
		{
			RobotInterpreter.halt();
			return null;
		}
		else
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
}
