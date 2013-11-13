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
	private BODY codeBody;
	
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
		
		if(tokens[tokens.length - 1] != Terminals.CLOSEPAREN)
		{
			RobotInterpreter.halt("WAITUNTIL", lineNum, code, "WAITUNTIL must close with )");
		}
		
		if(tokens.length > 3)
		{
			cl = new CONDITIONLIST(body, c, code.substring(11, code.length() - 1));
		}
		else RobotInterpreter.halt("WAITUNTIL", lineNum, code, "WAITUNTIL must contain a condition list!");

		c.nextLine();
		codeBody = new BODY(body, c);
	}
	
	public void print() 
	{
		System.out.print("waituntil (");
		cl.print();
		System.out.println(")");
		codeBody.print();
	}

	//Validate condition list
	//Validate body
	public void validate() 
	{
		System.out.println("Validating WAITUNTIL");

		cl.validate();
		codeBody.validate();
	}

	@Override
	public void execute() {
		// TODO Auto-generated method stub
		
	}
}
