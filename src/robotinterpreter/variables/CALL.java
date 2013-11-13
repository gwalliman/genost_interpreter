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
	
	public CALL(Code c, String callCode)
	{
		lineNum = c.currentLineNum();
		code = callCode;
		
		String[] tokens = Code.tokenize(code);
		
		callType = tokens[0];
		if(Terminals.callTypes.contains(callType) || Terminals.dataTypes.contains(callType))
		{
			switch(callType)
			{
				case "var":
					call = new VAR(c, Code.implode(tokens, " ", 1));
					break;
				case "method":
					call = new 	METHOD(c, Code.implode(tokens, " ", 0));
					break;
				case "int":
					call = new INTEGER(c, Code.implode(tokens, " ", 1));
					break;
				case "string":
					code = code.trim();
					call = new STRING(c, code.substring(6, code.length()).trim());
					break;
				case "bool":
					call = new BOOLEAN(c, Code.implode(tokens, " ", 1));
					break;
			}
		}
		else RobotInterpreter.halt("CALL", lineNum, code, "Invalid type for variable / method / data literal call");
	}
	
	public String getType()
	{
		switch(callType)
		{
			case "var":
				return RobotInterpreter.findVar(((VAR)call).id()).type();
			case "method":
				return RobotInterpreter.findMethod(((METHOD)call).id()).type();
			case "int":
				return "int";
			case "string":
				return "string";
			case "bool":
				return "bool";
		}
		RobotInterpreter.halt("CALL", lineNum, code, "Invalid call type");
		return null;
	}

	
	public void print() 
	{
		switch(callType)
		{
			case "var":
				((VAR)call).print();
				break;
			case "method":
				((METHOD)call).print();
				break;
			case "int":
				((INTEGER)call).print();
				break;
			case "string":
				((STRING)call).print();
				break;
			case "bool":
				((BOOLEAN)call).print();
				break;
		}
	}

	//Validate whatever we are calling
	public void validate() 
	{
		System.out.println("Validating CALL");
		switch(callType)
		{
			case "var":
				((VAR)call).validate();
				break;
			case "method":
				((METHOD)call).validate();
				break;
			case "int":
				((INTEGER)call).validate();
				break;
			case "string":
				((STRING)call).validate();
				break;
			case "bool":
				((BOOLEAN)call).validate();
				break;
		}
	}


	@Override
	public void execute() {
		// TODO Auto-generated method stub
		
	}
	
}
