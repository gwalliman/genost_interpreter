package robotinterpreter.variables.conditional;

import robotinterpreter.Code;
import robotinterpreter.RobotInterpreter;
import robotinterpreter.terminals.Terminals;
import robotinterpreter.variables.BODY;
import robotinterpreter.variables.Variable;

public class IF extends Variable
{
	private CONDITIONLIST cl;
	private BODY codeBody;
	private ELSEIF elseif;
	private ELSE els;
	
	public IF(BODY b, Code c)
	{
		body = b;
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
			cl = new CONDITIONLIST(body, c, code.substring(4, code.length() - 1));
		}
		else RobotInterpreter.halt("IF", lineNum, code, "IF must contain a condition list!");

		c.nextLine();
		codeBody = new BODY(body, c);
		
		c.nextLine();
		
		String[] newTokens = Code.tokenize(c.currentLine());
		if(newTokens[0].equals("elseif"))
		{
			elseif = new ELSEIF(body, c);
		}
		newTokens = Code.tokenize(c.currentLine());
		if(newTokens[0].equals("else"))
		{
			els = new ELSE(body, c);
		}
		else c.prevLine();
	}
	
	public void print() 
	{
		System.out.print("if (");
		cl.print();
		System.out.println(")");
		codeBody.print();
		
		if(elseif != null)
		{
			System.out.print(Code.newline);
			elseif.print();
		}
		
		if(els != null)
		{
			System.out.print(Code.newline);
			els.print();
		}
	}

	//Validiate the condition list
	//Validate the body
	//Validate the elseif, if it exists
	//Validate the else, if it exists.
	public void validate() 
	{
		System.out.println("Validating IF");

		cl.validate();
		codeBody.validate();
		
		if(elseif != null)
		{
			elseif.validate();
		}
		
		if(els != null)
		{
			els.validate();
		}
	}

	@Override
	public void execute() {
		// TODO Auto-generated method stub
		
	}
}
