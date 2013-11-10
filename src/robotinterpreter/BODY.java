package robotinterpreter;

public class BODY extends Variable
{
	private STMTLIST stmtList;
	private String code;
	private int startLine;
	private int finishLine;

	public BODY(Code c)
	{
		if(c.currentLine().equals(Terminals.OPENBRACE))
		{
			lineNum = startLine = c.currentLineNum();
			findCloseBrace(c);
			
			//Move to first statement.
			c.nextLine();
			if(c.currentLineNum() != finishLine)
				stmtList = new STMTLIST(this, c);

		}
		else RobotInterpreter.halt("BODY", c.currentLineNum(), c.currentLine(), "Body must begin with {");
	}
	
	public void findCloseBrace(Code c)
	{
		int numOpens = 1;
		while(numOpens != 0 && c.nextLine() != null)
		{
			if(c.currentLine().equals(Terminals.OPENBRACE)) numOpens++;
			else if(c.currentLine().equals(Terminals.CLOSEBRACE)) numOpens--;
		}
		if(numOpens == 0)
		{
			finishLine = c.currentLineNum();
			c.setCurrentLine(startLine);
		}
		else RobotInterpreter.halt("BODY", c.currentLineNum(), c.currentLine(), "Body must end with }");

	}
	
	public int getStartLine()
	{
		return startLine;
	}
	
	public int getFinishLine()
	{
		return finishLine;
	}
	
	public void print() 
	{
		if(stmtList != null)
		{
			System.out.println("BODY: Start Line " + startLine + ", Finish Line " + finishLine);
			System.out.println("{");
			if(stmtList != null)
				stmtList.print();
			else
				System.out.print("EMPTY BODY");
			System.out.print("}");
		}
		else System.out.print("EMPTY BODY");
	}
}
