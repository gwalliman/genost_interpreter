package robotinterpreter.variables.conditional;

import robotinterpreter.Code;
import robotinterpreter.RobotInterpreter;
import robotinterpreter.terminals.Terminals;
import robotinterpreter.variables.BODY;
import robotinterpreter.variables.Variable;

public class ELSEIF extends Variable
{
	private CONDITIONLIST cl;
	private BODY body;
	private ELSEIF elseif;
	
	public ELSEIF(Code c)
	{
		code = c.currentLine();
		lineNum = c.currentLineNum();
		
		String[] tokens = Code.tokenize(code);
		
		if(tokens[1] != Terminals.OPENPAREN)
		{
			RobotInterpreter.halt("ELSEIF", lineNum, code, "ELSEIF must open with (");
		}
		
		if(tokens[tokens.length - 1] != Terminals.CLOSEPAREN)
		{
			RobotInterpreter.halt("ELSEIF", lineNum, code, "ELSEIF must close with )");
		}
		
		if(tokens.length > 3)
		{
			cl = new CONDITIONLIST(c, code.substring(8, code.length() - 1));
		}
		else RobotInterpreter.halt("ELSEIF", lineNum, code, "ELSEIF must contain a condition list!");

		c.nextLine();
		body = new BODY(c);
		
		c.nextLine();
		
		String[] newTokens = Code.tokenize(c.currentLine());
		Code.printTokens(newTokens);
		if(newTokens[0].equals("elseif"))
		{
			elseif = new ELSEIF(c);
		}
		
	}
	
	public void print() 
	{
		System.out.print("elseif (");
		cl.print();
		System.out.println(")");
		body.print();
		
		if(elseif != null)
		{
			System.out.print(Code.newline);
			elseif.print();
		}
	}

	//Validate the condition list
	//Validate the body
	//Validate the next ELSEIF, if applicable
	public void validate() 
	{
		System.out.println("Validating ELSEIF");

		cl.validate();
		body.validate();
		
		if(elseif != null)
		{
			elseif.validate();
		}
	}

	@Override
	public void execute() {
		// TODO Auto-generated method stub
		
	}
}
