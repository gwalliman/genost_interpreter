package robotinterpreter.variables;

import robotinterpreter.Code;
import robotinterpreter.Interpreter;
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
	private Interpreter interpreter;
	
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
	public STMT(Interpreter in, BODY b, Code c)
	{
		interpreter = in;
		body = b;
		lineNum = c.currentLineNum();
		code = c.currentLine();
		
		//All valid statements will have, as their first token, the statement type, separated from the rest of the code by a space.
		String[] type = code.split(" ", 2);
		if(Terminals.stmtTypes.contains(type[0]))
		{
			stmtType = type[0];
			if (stmtType.equals(Terminals.VARDECL))
				stmt = new VARDECL(interpreter, body, c);
			else if (stmtType.equals(Terminals.ASSIGN))
				stmt = new ASSIGNMENT(interpreter, body, c);
			else if (stmtType.equals(Terminals.METHODDEFINE))
				stmt = new METHODDEFINE(interpreter, body, c);
			//A METHOD is the only STMT which can be both a standalone STMT and a CALL.
			//Therefore the METHOD code expects there to be no ending semicolon, and so we process that semicolon here.
			else if (stmtType.equals(Terminals.METHOD))
			{
				//Ensure that the semicolon exists. If it does, remove it and call the METHOD parser.
				String lastchar = c.currentLine().substring(c.currentLine().length() - 1);
				if(!lastchar.equals(Terminals.SEMICOLON))
					interpreter.error("METHOD", lineNum, code, "Missing Semicolon");
				stmt = new METHOD(interpreter, body, c, c.currentLine().substring(0, c.currentLine().length() - 1));
			}
			else if (stmtType.equals(Terminals.RETURN))
				stmt = new RETURN(interpreter, body, c);
			else if (stmtType.equals(Terminals.IF))
				stmt = new IF(interpreter, body, c);
			//ELSEIFs and ELSEs should be handled in the IF code.
			//If we find a separate statement beginning with ELSEIF or ELSE, we must halt.
			else if (stmtType.equals(Terminals.ELSEIF))
				interpreter.error("STMT", lineNum, code, "ELSEIF must follow IF");
			else if (stmtType.equals(Terminals.ELSE))
				interpreter.error("STMT", lineNum, code, "ELSE must follow IF or ELSEIF");
			else if (stmtType.equals(Terminals.LOOPUNTIL))
				stmt = new LOOPUNTIL(interpreter, body, c);
			else if (stmtType.equals(Terminals.LOOPFOR))
				stmt = new LOOPFOR(interpreter, body, c);
			else if (stmtType.equals(Terminals.WAITUNTIL))
				stmt = new WAITUNTIL(interpreter, body, c);
			else if (stmtType.equals(Terminals.WAITFOR))
				stmt = new WAITFOR(interpreter, body, c);
		}
		else interpreter.error("STMT", lineNum, code, "STMT type is not valid.");
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
		if (stmtType.equals(Terminals.VARDECL))
				((VARDECL)stmt).print();
		else if (stmtType.equals(Terminals.ASSIGN))
				((ASSIGNMENT)stmt).print();
		else if (stmtType.equals(Terminals.METHODDEFINE))
				((METHODDEFINE)stmt).print();
		else if (stmtType.equals(Terminals.METHOD))
				((METHOD)stmt).print();
		else if (stmtType.equals(Terminals.RETURN))
				((RETURN)stmt).print();
		else if (stmtType.equals(Terminals.IF))
				((IF)stmt).print();
		else if (stmtType.equals(Terminals.LOOPUNTIL))
				((LOOPUNTIL)stmt).print();
		else if (stmtType.equals(Terminals.LOOPFOR))
				((LOOPFOR)stmt).print();
		else if (stmtType.equals(Terminals.WAITUNTIL))
				((WAITUNTIL)stmt).print();
		else if (stmtType.equals(Terminals.WAITFOR))
				((WAITFOR)stmt).print();
	}

	/**
	 * Simple validation function, determines what kind of statement we have, and calls that type's validation function.
	 */
	public void validate() 
	{
		interpreter.writeln("validate", "Validating STMT");
		if (stmtType.equals(Terminals.VARDECL))
			((VARDECL)stmt).validate();
		else if (stmtType.equals(Terminals.ASSIGN))
			((ASSIGNMENT)stmt).validate();
		else if (stmtType.equals(Terminals.METHODDEFINE))
			((METHODDEFINE)stmt).validate();
		else if (stmtType.equals(Terminals.METHOD))
			((METHOD)stmt).validate();
		else if (stmtType.equals(Terminals.RETURN))
			((RETURN)stmt).validate();
		else if (stmtType.equals(Terminals.IF))
			((IF)stmt).validate();
		else if (stmtType.equals(Terminals.LOOPUNTIL))
			((LOOPUNTIL)stmt).validate();
		else if (stmtType.equals(Terminals.LOOPFOR))
			((LOOPFOR)stmt).validate();
		else if (stmtType.equals(Terminals.WAITUNTIL))
			((WAITUNTIL)stmt).validate();
		else if (stmtType.equals(Terminals.WAITFOR))
			((WAITFOR)stmt).validate();
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
			Interpreter.halt();
			return null;
		}
		else
		{
			if (stmtType.equals(Terminals.VARDECL))
				return ((VARDECL)stmt).execute(null);
			else if (stmtType.equals(Terminals.ASSIGN))
				return ((ASSIGNMENT)stmt).execute(null);
			else if (stmtType.equals(Terminals.METHODDEFINE))
				//We don't execute the METHODDEFINE, as this will execute the actual method!
				return null;
			else if (stmtType.equals(Terminals.METHOD))
				return ((METHOD)stmt).execute(args);
			else if (stmtType.equals(Terminals.RETURN))
				return ((RETURN)stmt).execute(null);
			else if (stmtType.equals(Terminals.IF))
				return ((IF)stmt).execute(null);
			else if (stmtType.equals(Terminals.LOOPUNTIL))
				return ((LOOPUNTIL)stmt).execute(null);
			else if (stmtType.equals(Terminals.LOOPFOR))
				return ((LOOPFOR)stmt).execute(null);
			else if (stmtType.equals(Terminals.WAITUNTIL))
				return ((WAITUNTIL)stmt).execute(null);
			else if (stmtType.equals(Terminals.WAITFOR))
				return ((WAITFOR)stmt).execute(null);
			else
				return null;
		}
	}
}
