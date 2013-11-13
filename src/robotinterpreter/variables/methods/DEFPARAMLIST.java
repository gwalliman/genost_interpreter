package robotinterpreter.variables.methods;

import robotinterpreter.Code;
import robotinterpreter.RobotInterpreter;
import robotinterpreter.terminals.Terminals;
import robotinterpreter.variables.ID;
import robotinterpreter.variables.Variable;
import robotinterpreter.variables.vars.VAR;
import robotinterpreter.variables.vars.VARDECL;

public class DEFPARAMLIST extends Variable
{
	private String paramType;
	private String id;
	private int paramNum;
	private DEFPARAMLIST nextParam;
	
	public DEFPARAMLIST(Code c, String s, int p) 
	{
		paramNum = p;
		lineNum = c.currentLineNum();
		code = c.currentLine();
		String tokens[] = Code.tokenize(s);

		if(tokens.length > 1)
		{
		
			paramType = tokens[0];
			if(!Terminals.dataTypes.contains(paramType)) RobotInterpreter.halt("DEFPARAMLIST", lineNum, code, "Invalid DEFPARAMLIST data type. Must be int, string, or bool");
			
			id = ID.validate(tokens[1], c);
	
			if(tokens.length > 3)
			{
				if(tokens[2] == Terminals.COMMA)
					nextParam = new DEFPARAMLIST(c, Code.implode(tokens, " ", 3, tokens.length - 1), p++);
				else RobotInterpreter.halt("DEFPARAMLIST", lineNum, code, "There must be a comma between each parameter in a DEFPARAMLIST");
			}
		}
		else RobotInterpreter.halt("DEFPARAMLIST", lineNum, code, "Syntax error in DEFPARAMLIST");
	}
	
	public String getType()
	{
		return paramType;
	}
	
	public int getNum() 
	{
		return paramNum;
	}

	public DEFPARAMLIST nextParam() 
	{
		return nextParam;
	}
	
	public void print() 
	{
		System.out.print(paramType + " " + id);
			
		if(nextParam != null)
		{
			System.out.print(", ");
			nextParam.print();
		}
	}

	//Ensure that var doesn't already exist
	//Validate next var
	public void validate() 
	{
		VARDECL v = RobotInterpreter.findVar(id);
		if(v != null)
		{
			RobotInterpreter.halt("DEFPARAMLIST", lineNum, code, "Error, variable " + id + " already declared on line " + v.lineNum());
		}
		
		if(nextParam != null)
		{
			nextParam.validate();
		}
	}

	@Override
	public void execute() {
		// TODO Auto-generated method stub
		
	}
}
