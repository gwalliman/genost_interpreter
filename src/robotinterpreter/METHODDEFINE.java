package robotinterpreter;

public class METHODDEFINE extends Variable {
	
	private String type;
	private String id;
	private BODY body;
	private DEFPARAMLIST params;
	
	public METHODDEFINE(Code c)
	{
		lineNum = c.currentLineNum();
		code = c.currentLine();
		
		String[] tokens = Code.tokenize(c.currentLine());
		
		type = tokens[1];
		if(!Terminals.dataTypes.contains(type)) RobotInterpreter.halt("METHODDEFINE", lineNum, code, "Invalid METHODDEFINE data type. Must be int, string, or bool");
			
		id = ID.validate(tokens[2], c);
		
		if(tokens[3] != Terminals.OPENPAREN)
		{
			RobotInterpreter.halt("METHODDEFINE", lineNum, code, "ID must be followed by (");
		}
		
		if(tokens[tokens.length - 1] != Terminals.CLOSEPAREN)
		{
			RobotInterpreter.halt("METHODDEFINE", lineNum, code, "METHODDEFINE header must end with )");
		}
		
		if(tokens[4] != Terminals.CLOSEPAREN)
		{
			params = new DEFPARAMLIST(c, Code.implode(tokens, " ", 4, tokens.length - 2));
		}
		
		c.nextLine();
		body = new BODY(c);
	}
	
	public void print() 
	{
		System.out.print("methoddefine " + type + " " + id + "(");
		if(params != null)
			params.print();
		System.out.println(")");
		body.print();
	}

}
