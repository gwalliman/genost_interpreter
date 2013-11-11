package robotinterpreter.variables.conditional;

import robotinterpreter.Code;
import robotinterpreter.RobotInterpreter;
import robotinterpreter.terminals.Terminals;
import robotinterpreter.variables.BODY;
import robotinterpreter.variables.Variable;

public class IF extends Variable
{
	private CONDITIONLIST cl;
	private BODY body;
	private ELSEIF elseif;
	private ELSE els;
	
	public IF(Code c)
	{
		code = c.currentLine();
		lineNum = c.currentLineNum();
		
		String[] tokens = Code.tokenize(code);
		
		if(tokens[1] != Terminals.OPENPAREN)
		{
			RobotInterpreter.halt("IF", lineNum, code, "IF must open with (");
		}
		
		if(tokens[tokens.length - 1] != Terminals.CLOSEPAREN)
		{
			RobotInterpreter.halt("IF", lineNum, code, "IF must close with )");
		}
		
		if(tokens.length > 3)
		{
			cl = new CONDITIONLIST(c, code.substring(4, code.length() - 1));
		}
		else RobotInterpreter.halt("IF", lineNum, code, "IF must contain a condition list!");

		c.nextLine();
		body = new BODY(c);
	}
	
	public void print() 
	{
		System.out.print("if (");
		cl.print();
		System.out.println(")");
		body.print();
	}
}
