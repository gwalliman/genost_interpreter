package robotinterpreter.variables.conditional;

import robotinterpreter.Code;
import robotinterpreter.RobotInterpreter;
import robotinterpreter.terminals.Terminals;
import robotinterpreter.variables.Variable;

public class CONDITIONLIST extends Variable 
{
	//Can be either a CONDITION or a CONDITIONLIST
	private Object con;
	private String conType;
	private String logOp;
	private CONDITIONLIST nextCon;
	
	public CONDITIONLIST(Code c, String s)
	{
		code = s;
		lineNum = c.currentLineNum();
		
		String[] tokens = Code.tokenize(code);
		
		if(tokens[0] != Terminals.OPENBRACKET)
			RobotInterpreter.halt("CONDITIONLIST", lineNum, code, "CONDITIONLIST must begin with [");
		
		int closeBracket = findCloseBracket(tokens);
		if(closeBracket == -1)
			RobotInterpreter.halt("CONDITIONLIST", lineNum, code, "Missing ]! CONDITIONLIST must have matching brackets!");

		boolean hasLogOp = false;
		for(int x = 0; x < closeBracket; x++)
		{
			if(tokens[x].equals(Terminals.AND) || tokens[x].equals(Terminals.OR))
				hasLogOp = true;
		}
		
		//If hasLogOp is true, then c is a CONDITIONLIST.
		//If false, c is a CONDITION
		//If there is a token following tokens[closeBracket], it must be a logop, and there is a sequential CONDITIONLIST
		if(hasLogOp)
		{
			conType = "CONDITIONLIST";
			con = new CONDITIONLIST(c, Code.implode(tokens, " ", 1, closeBracket - 1));
		}
		else
		{
			conType = "CONDITION";
			con = new CONDITION(c, Code.implode(tokens, " ", 1, closeBracket - 1));

		}
		
		
		if(tokens.length - 1 > closeBracket)
		{
			if(Terminals.logOps.contains(tokens[closeBracket + 1]))
			{
				logOp = tokens[closeBracket + 1];
				if(tokens.length - 1 > closeBracket + 1)
				{
					nextCon = new CONDITIONLIST(c, Code.implode(tokens, " ", closeBracket + 2, tokens.length - 1));
				}
				else RobotInterpreter.halt("CONDITIONLIST", lineNum, code, "A CONDITION or CONDITIONLIST must follow an AND or OR");
			}
			else RobotInterpreter.halt("CONDITIONLIST", lineNum, code, "Only AND or OR may follow a CONDITIONLIST");

		}
		
		
	}
	
	public void print() 
	{
		if(conType == "CONDITIONLIST")
		{		
			System.out.print("[");
			((CONDITIONLIST)con).print();
			System.out.print("]");
		}
		else if(conType == "CONDITION")
		{
			((CONDITION)con).print();

		}
		
		if(logOp != null)
		{
			System.out.print(" " + logOp + " ");
			nextCon.print();
		}
	}
	
	private int findCloseBracket(String[] tokens)
	{
		int openBrackets = 0;
		int tokenNum = 0;
		for(String token : tokens)
		{
			if(token == Terminals.OPENBRACKET)
				openBrackets++;
			else if(token == Terminals.CLOSEBRACKET)
				openBrackets--;
			
			if(openBrackets == 0)
			{
				return tokenNum;
			}
			else tokenNum++;
		}
		return -1;
	}
}
