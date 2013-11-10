package robotinterpreter;

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
	
}
