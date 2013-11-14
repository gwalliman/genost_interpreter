package robotinterpreter.variables.methods;

import robotinterpreter.Code;
import robotinterpreter.RobotInterpreter;
import robotinterpreter.terminals.Terminals;
import robotinterpreter.variables.BODY;
import robotinterpreter.variables.ID;
import robotinterpreter.variables.Variable;

public class METHOD extends Variable 
{
	private String id;
	private CALLPARAMLIST params;
	private METHODDEFINE method;
	
	public METHOD(BODY b, Code c, String s)
	{
		body = b;
		
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
	
			params = new CALLPARAMLIST(body, c, callCode[1].substring(0, callCode[1].length() - 1), this, 0);
		}
	}
	
	public String id()
	{
		return id;
	}
	
	public void print() 
	{
		RobotInterpreter.write("method " + id + "(");
		if(params != null)
			params.print();
		RobotInterpreter.write(")");
	}

	//Ensure that method exists
	//Validate callParamList
	public void validate() 
	{
		RobotInterpreter.writeln("Validating METHOD");

		method = RobotInterpreter.findMethod(id);
		if(method == null)
		{
			RobotInterpreter.halt("METHOD", lineNum, code, "Method " + id + " is not defined.");
		}
		if(params != null)
		{
			params.validate();
		}
	}

	@Override
	public void execute() {
		// TODO Auto-generated method stub
		
	}
}
