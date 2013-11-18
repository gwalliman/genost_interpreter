package robotinterpreter.variables;

import robotinterpreter.Code;
import robotinterpreter.RobotInterpreter;
import robotinterpreter.terminals.Terminals;
import robotinterpreter.variables.constants.BOOLEAN;
import robotinterpreter.variables.constants.INTEGER;
import robotinterpreter.variables.constants.STRING;
import robotinterpreter.variables.methods.METHOD;
import robotinterpreter.variables.vars.VAR;

public class CALL extends Variable 
{
	private Object call;
	private String callType;
	
	public CALL(BODY b, Code c, String callCode)
	{
		body = b;
		lineNum = c.currentLineNum();
		code = callCode;
		
		String[] tokens = Code.tokenize(code);
		
		callType = tokens[0];
		if(Terminals.callTypes.contains(callType) || Terminals.dataTypes.contains(callType))
		{
			switch(callType)
			{
				case Terminals.VAR:
					call = new VAR(body, c, Code.implode(tokens, " ", 1));
					break;
				case Terminals.METHOD:
					call = new METHOD(body, c, Code.implode(tokens, " ", 0));
					break;
				case Terminals.INT:
					call = new INTEGER(body, c, Code.implode(tokens, " ", 1));
					break;
				case Terminals.STRING:
					code = code.trim();
					call = new STRING(body, c, code.substring(6, code.length()).trim());
					break;
				case Terminals.BOOL:
					call = new BOOLEAN(body, c, Code.implode(tokens, " ", 1));
					break;
			}
		}
		else RobotInterpreter.halt("CALL", lineNum, code, "Invalid type for variable / method / data literal call");
	}
	
	public String type()
	{
		switch(callType)
		{
			case Terminals.VAR:
				return RobotInterpreter.findVar(body, ((VAR)call).id()).type();
			case Terminals.METHOD:
				return RobotInterpreter.findMethod(((METHOD)call).id()).type();
			case Terminals.INT:
				return "int";
			case Terminals.STRING:
				return "string";
			case Terminals.BOOL:
				return "bool";
		}
		RobotInterpreter.halt("CALL", lineNum, code, "Invalid call type");
		return null;
	}

	
	public void print() 
	{
		switch(callType)
		{
			case Terminals.VAR:
				((VAR)call).print();
				break;
			case Terminals.METHOD:
				((METHOD)call).print();
				break;
			case Terminals.INT:
				((INTEGER)call).print();
				break;
			case Terminals.STRING:
				((STRING)call).print();
				break;
			case Terminals.BOOL:
				((BOOLEAN)call).print();
				break;
		}
	}

	//Validate whatever we are calling
	public void validate() 
	{
		RobotInterpreter.writeln("validate",  "Validating CALL");
		switch(callType)
		{
			case Terminals.VAR:
				((VAR)call).validate();
				break;
			case Terminals.METHOD:
				((METHOD)call).validate();
				break;
			case Terminals.INT:
				((INTEGER)call).validate();
				break;
			case Terminals.STRING:
				((STRING)call).validate();
				break;
			case Terminals.BOOL:
				((BOOLEAN)call).validate();
				break;
		}
	}


	@Override
	public Object execute(Object args[]) 
	{
		switch(callType)
		{
			case Terminals.VAR:
				return ((VAR)call).execute(null);
			case Terminals.METHOD:
				return ((METHOD)call).execute(null);
			case Terminals.INT:
				return ((INTEGER)call).execute(null);
			case Terminals.STRING:
				return ((STRING)call).execute(null);
			case Terminals.BOOL:
				return ((BOOLEAN)call).execute(null);
			default:
				return null;
		}		
	}
	
}
