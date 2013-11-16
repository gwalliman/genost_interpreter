package robotinterpreter.variables.methods;

import java.util.ArrayList;

import robotinterpreter.Code;
import robotinterpreter.RobotInterpreter;
import robotinterpreter.terminals.Terminals;
import robotinterpreter.variables.BODY;
import robotinterpreter.variables.CALL;
import robotinterpreter.variables.Variable;

public class CALLPARAMLIST extends Variable
{
	private CALL call;
	private CALLPARAMLIST nextParam;
	private METHOD method;
	private int paramNum;
	
	public CALLPARAMLIST(BODY b, Code c, String s, METHOD m, int p) 
	{
		body = b;
		method = m;
		paramNum = p;
		lineNum = c.currentLineNum();
		code = c.currentLine();
		
		String tokens[] = Code.tokenize(s);
		String argument = "";
		String remainder = "";
		
		if(tokens[0].equals("method"))
		{
			if(tokens[2] != Terminals.OPENPAREN)
			{
				RobotInterpreter.halt("CALLPARAMLIST", lineNum, code, "METHOD argument must have open paren following ID!");
			}
			
			int closeParen = 2;
			int counter = 1;
			
			while(counter != 0 && closeParen != tokens.length - 1)
			{
				closeParen++;
				if(tokens[closeParen] == Terminals.OPENPAREN) counter++;
				else if(tokens[closeParen] == Terminals.CLOSEPAREN) counter--;
			}
			
			argument = Code.implode(tokens, " ", 0, closeParen);
			if(tokens.length > closeParen)
			{
				//We do codeParen + 2 to skip over the comma
				remainder = Code.implode(tokens, " ", closeParen + 2, tokens.length - 1);
			}
		}
		else
		{
			String[] t = s.split(Terminals.COMMA, 2);
			argument = t[0];
			if(t.length > 1)
			{
				remainder = t[1];
			}
		}
		
		if(argument.trim().length() > 0)
		{
			call = new CALL(body, c, argument);
		}
		else RobotInterpreter.halt("CALLPARAMLIST", lineNum, code, "Syntax error in CALLPARAMLIST");

		if(!remainder.isEmpty())
		{
			nextParam = new CALLPARAMLIST(body, c, remainder, m, ++p);
		}
	}
	
	public void print() 
	{
		RobotInterpreter.write("parse", paramNum + " ");
		call.print();
			
		if(nextParam != null)
		{
			RobotInterpreter.write("parse", ", ");
			nextParam.print();
		}
	}

	//1. Validate call
	//2. Ensure that call should exist at all (what if param doesn't exist?)
	//3. Ensure that call is of right type.
	//4. Validate next call
	public void validate() 
	{
		RobotInterpreter.writeln("validate", "Validating CALLPARAMLIST");

		//1
		call.validate();
		METHODDEFINE methdef = RobotInterpreter.findMethod(method.id());
		
		//As the method should always be validated before the calllist, it should always be valid, but just in case we check.
		if(methdef != null)
		{	
			//2
			DEFPARAMLIST paramdef = methdef.getParam(paramNum);
			if(paramdef == null)
			{
				RobotInterpreter.halt("CALLPARAMLIST", lineNum, code, "Parameter " + paramNum + " does not exist for method " + methdef.id());
			}
			else
			{
				//3
				String callType = call.type();
				String defType = paramdef.type();
				if(!callType.equals(defType))
				{
					RobotInterpreter.halt("CALLPARAMLIST", lineNum, code, "Parameter " + paramNum + " is of wrong type. Method " + methdef.id() + " parameter " + paramNum + " requires " + defType + ", but was provided " + callType);
				}
			}
		}
		
		//4
		if(nextParam != null)
		{
			nextParam.validate();
		}
	}

	@Override
	public Object execute(Object[] args) 
	{
		if(args == null)
		{
			args = new Object[method.methDef().numParams()];
		}
		args[paramNum] = call.execute(null);
		
		if(nextParam != null)
		{
			return nextParam.execute(args);
		}
		else return args;
	}
}
