package robotinterpreter.variables.methods;

import robotinterpreter.Code;
import robotinterpreter.RobotInterpreter;
import robotinterpreter.terminals.Terminals;
import robotinterpreter.variables.CALL;
import robotinterpreter.variables.Variable;

public class CALLPARAMLIST extends Variable
{
	private CALL call;
	private CALLPARAMLIST nextParam;
	
	public CALLPARAMLIST(Code c, String s) 
	{
		lineNum = c.currentLineNum();
		code = c.currentLine();

		String tokens[] = s.split(Terminals.COMMA);
		
		if(tokens[0].trim().length() > 0)
		{
			call = new CALL(c, tokens[0]);
		}
		else RobotInterpreter.halt("CALLPARAMLIST", lineNum, code, "Syntax error in CALLPARAMLIST");

		if(tokens.length > 1)
		{
			nextParam = new CALLPARAMLIST(c, Code.implode(tokens, ",", 1, tokens.length - 1));
		}
	}
	
	public void print() 
	{
		call.print();
			
		if(nextParam != null)
		{
			System.out.print(", ");
			nextParam.print();
		}
	}
}
