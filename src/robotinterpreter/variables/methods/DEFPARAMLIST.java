package robotinterpreter.variables.methods;

import java.util.ArrayList;

import robotinterpreter.Code;
import robotinterpreter.RobotInterpreter;
import robotinterpreter.terminals.Terminals;
import robotinterpreter.variables.BODY;
import robotinterpreter.variables.ID;
import robotinterpreter.variables.Variable;

public class DEFPARAMLIST extends Variable
{
	private String paramType;
	private String id;
	private int paramNum;
	private DEFPARAMLIST nextParam;

	//Define params for INTERNAL method
	public DEFPARAMLIST(BODY b, Code c, String s, int p) 
	{
		body = b;
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
					nextParam = new DEFPARAMLIST(body, c, Code.implode(tokens, " ", 3, tokens.length - 1), ++p);
				else RobotInterpreter.halt("DEFPARAMLIST", lineNum, code, "There must be a comma between each parameter in a DEFPARAMLIST");
			}
		}
		else RobotInterpreter.halt("DEFPARAMLIST", lineNum, code, "Syntax error in DEFPARAMLIST");
	}
	
	//Define params for EXTERNAL method
	public DEFPARAMLIST(ArrayList<String> params, int p)
	{
		id = "param" + p;
		paramNum = p;
		paramType = params.remove(0);
		
		if(params.size() > 0)
		{
			nextParam = new DEFPARAMLIST(params, ++p);
		}
	}
	
	public String id()
	{
		return id;
	}
	
	public String type()
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
		RobotInterpreter.write("parse", paramNum + " " + paramType + " " + id);
			
		if(nextParam != null)
		{
			RobotInterpreter.write("parse", ", ");
			nextParam.print();
		}
	}

	//Nothing at this time to validate
	//Validate next var
	public void validate() 
	{
		RobotInterpreter.writeln("validate", "Validating DEFPARAMLIST");

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
