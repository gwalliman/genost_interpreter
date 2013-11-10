package robotinterpreter;
public class STMT extends Variable 
{
	private Object stmt;
	private String stmtType;
	
	public STMT(Code c)
	{
		lineNum = c.currentLineNum();
		code = c.currentLine();
		
		String[] type = code.split(" ", 2);
		if(Terminals.stmtTypes.contains(type[0]))
		{
			stmtType = type[0];
			switch(stmtType)
			{
				case "vardecl":
					stmt = new VARDECL(c);
					break;
				case "assign":
					stmt = new ASSIGNMENT(c);
					break;
				case "methoddefine":
					stmt = new METHODDEFINE(c);
					break;
				case "method":
					String lastchar = c.currentLine().substring(c.currentLine().length() - 1);
					if(!lastchar.equals(Terminals.SEMICOLON))
						RobotInterpreter.halt("METHOD", lineNum, code, "Missing Semicolon");
					stmt = new METHOD(c, c.currentLine().substring(0, c.currentLine().length() - 1));

					break;
			}
		}
		else RobotInterpreter.halt("STMT", lineNum, code, "STMT type is not valid.");
	}
	
	public void print() 
	{
		switch(stmtType)
		{
			case "vardecl":
				((VARDECL)stmt).print();
				break;
			case "assign":
				((ASSIGNMENT)stmt).print();
				break;
			case "methoddefine":
				((METHODDEFINE)stmt).print();
				break;
			case "method":
				((METHOD)stmt).print();
				break;
		}
	}
}
