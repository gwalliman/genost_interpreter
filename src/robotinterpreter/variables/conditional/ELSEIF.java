package robotinterpreter.variables.conditional;

import robotinterpreter.Code;
import robotinterpreter.RobotInterpreter;
import robotinterpreter.terminals.Terminals;
import robotinterpreter.variables.BODY;
import robotinterpreter.variables.Variable;

public class ELSEIF extends Variable
{
	private CONDITIONLIST cl;
	private BODY codeBody;
	private ELSEIF elseif;
	
	public ELSEIF(BODY b, Code c)
	{
		body = b;
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
			cl = new CONDITIONLIST(body, c, code.substring(8, code.length() - 1));
		}
		else RobotInterpreter.halt("ELSEIF", lineNum, code, "ELSEIF must contain a condition list!");

		c.nextLine();
		codeBody = new BODY(body, c);
		
		c.nextLine();
		
		String[] newTokens = Code.tokenize(c.currentLine());
		Code.printTokens(newTokens);
		if(newTokens[0].equals("elseif"))
		{
			elseif = new ELSEIF(body, c);
		}
		
	}
	
	public void print() 
	{
		RobotInterpreter.write("elseif (");
		cl.print();
		RobotInterpreter.writeln(")");
		codeBody.print();
		
		if(elseif != null)
		{
			RobotInterpreter.write(Code.newline);
			elseif.print();
		}
	}

	//Validate the condition list
	//Validate the body
	//Validate the next ELSEIF, if applicable
	public void validate() 
	{
		RobotInterpreter.writeln("Validating ELSEIF");

		cl.validate();
		codeBody.validate();
		
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
