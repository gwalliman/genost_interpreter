package robotinterpreter.variables.methods;

import robotinterpreter.Code;
import robotinterpreter.RobotInterpreter;
import robotinterpreter.terminals.Terminals;
import robotinterpreter.variables.ID;
import robotinterpreter.variables.Variable;

public class METHOD extends Variable 
{
	private String id;
	private CALLPARAMLIST params;
	
	public METHOD(Code c, String s)
	{
		//Expects method ID OPENPAREN CALLPARAMLLIST CLOSEPAREN
		code = s;
		lineNum = c.currentLineNum();
		
		String[] tokens = Code.tokenize(code);
		
		id = ID.validate(tokens[1], c);
		
		if(tokens[2] != Terminals.OPENPAREN)
		{
			RobotInterpreter.halt("METHOD", lineNum, code, "ID must be followed by (");
		}
		
		if(tokens[tokens.length - 1] != Terminals.CLOSEPAREN)
		{
			RobotInterpreter.halt("METHOD", lineNum, code, "METHOD header must end with )");
		}
		
		if(tokens[3] != Terminals.CLOSEPAREN)
		{
			//Split by the method open paren. Note that we limit the split to 2 because there may be more openparens in the paramlist.
			String[] callCode = code.split("\\(", 2);
	
			params = new CALLPARAMLIST(c, callCode[1].substring(0, callCode[1].length() - 1));
		}
	}

	
	public void print() 
	{
		System.out.print("method " + id + "(");
		if(params != null)
			params.print();
		System.out.print(")");
	}
}
