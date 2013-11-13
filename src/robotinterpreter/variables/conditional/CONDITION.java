package robotinterpreter.variables.conditional;

import robotinterpreter.Code;
import robotinterpreter.RobotInterpreter;
import robotinterpreter.terminals.Terminals;
import robotinterpreter.variables.CALL;
import robotinterpreter.variables.Variable;

public class CONDITION extends Variable
{
	private CALL lhs;
	private CALL rhs;
	private String comparator;
	
	public CONDITION(Code c, String s)
	{
		code = s;
		lineNum = c.currentLineNum();
		
		int symbolNum = -1;
		
		String[] tokens = Code.tokenize(s);
		for(int x = 0; x < tokens.length; x++)
		{
			if(Terminals.comparators.contains(tokens[x]))
			{
				symbolNum = x;
				comparator = tokens[x];
				
				lhs = new CALL(c, Code.implode(tokens, " ", 0, symbolNum - 1));
				rhs = new CALL(c, Code.implode(tokens, " ", symbolNum + 1, tokens.length - 1));
			}
		}
		if(symbolNum == -1)
			RobotInterpreter.halt("CONDITION", lineNum, code, "CONDITION must have a comparator (==, !=, >, <, >=, <=)");
	}
	
	public void print() 
	{
		System.out.print("[");
		lhs.print();
		System.out.print(" " + comparator + " ");
		rhs.print();
		System.out.print("]");
	}

	//lhs and rhs must be of same type
	//lhs and rhs must both exist (if vars or methods)
	//comparator must be valid for comparison type
	//Validate vars and methods
	public void validate() 
	{
		
	}

	@Override
	public void execute() {
		// TODO Auto-generated method stub
		
	}
}
