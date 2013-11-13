package robotinterpreter.variables.vars;

import robotinterpreter.Code;
import robotinterpreter.variables.ID;
import robotinterpreter.variables.Variable;

public class VAR extends Variable 
{
	private String id;
	
	public VAR(Code c, String callCode)
	{
		code = callCode;
		lineNum = c.currentLineNum();
		
		String[] tokens = Code.tokenize(code);
		id = ID.validate(tokens[0], c);
	}
	
	public String id()
	{
		return id;
	}

	public void print() 
	{
		if(id != null)
		{
			System.out.print("var " + id);
		}
		else System.out.print("Empty VARCALL");
	}

	//Ensure that var exists
	public void validate() 
	{
		
	}

	@Override
	public void execute() {
		// TODO Auto-generated method stub
		
	}
}
