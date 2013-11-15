package robotinterpreter.variables.wait;

import robotinterpreter.Code;
import robotinterpreter.RobotInterpreter;
import robotinterpreter.terminals.Terminals;
import robotinterpreter.variables.BODY;
import robotinterpreter.variables.Variable;
import robotinterpreter.variables.conditional.CONDITIONLIST;

public class WAITUNTIL extends Variable
{
	private CONDITIONLIST cl;
	
	public WAITUNTIL(BODY b, Code c)
	{
		body = b;
		code = c.currentLine();
		lineNum = c.currentLineNum();
		
		String[] tokens = Code.tokenize(code);
		
		if(tokens[1] != Terminals.OPENPAREN)
		{
			RobotInterpreter.halt("WAITUNTIL", lineNum, code, "WAITUNTIL must open with (");
		}
		
		if(tokens[tokens.length - 2] != Terminals.CLOSEPAREN)
		{
			RobotInterpreter.halt("WAITUNTIL", lineNum, code, "WAITUNTIL must close with )");
		}
		
		if(tokens.length > 3)
		{
			cl = new CONDITIONLIST(body, c, code.substring(11, code.length() - 2));
		}
		else RobotInterpreter.halt("WAITUNTIL", lineNum, code, "WAITUNTIL must contain a condition list!");

		if(tokens[tokens.length - 1] != Terminals.SEMICOLON)
		{
			RobotInterpreter.halt("WAITUNTIL", lineNum, code, "Missing semicolon");
		}		
	}
	
	public void print() 
	{
		RobotInterpreter.write("parse", "waituntil (");
		cl.print();
		RobotInterpreter.writeln("parse", ")");
	}

	//Validate condition list
	//Validate body
	public void validate() 
	{
		RobotInterpreter.writeln("validate", "Validating WAITUNTIL");

		cl.validate();
	}

	public Object execute(Object[] args) 
	{
		boolean go = false;
		
		while(!go)
		{
			go = (boolean) cl.execute(null);
			if(go)
			{
				return null;
			}
			else
			{
				try 
				{
					//We currently set polling interval to 1/4 of a second
					Thread.sleep(250);
				} 
				catch (InterruptedException e) 
				{
					e.printStackTrace();
				}
			}
		}
		
		//We will never reach here, but Eclipse is making me add this.
		return null;
	}
}
