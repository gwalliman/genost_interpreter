package robotinterpreter;

public class STMTLIST extends Variable
{
	private BODY body;
	private STMT stmt;
	private STMTLIST nextStmt;
	
	public STMTLIST(BODY b, Code c) 
	{
		body = b;
		while(c.currentLine().trim().length() == 0) 
		{ 
			c.nextLine();
		}
		
		if(c.currentLineNum() != body.getFinishLine())
			stmt = new STMT(c);
		
		c.nextLine();
		if(c.currentLine() != null && c.currentLineNum() != body.getFinishLine())
		{
			nextStmt = new STMTLIST(body, c);
		}
		else nextStmt = null;
	}
	
	public void print() 
	{
		if(stmt != null)
		{
			stmt.print();
			System.out.print(Code.newline);
			
			if(nextStmt != null)
				nextStmt.print();
		}
		else System.out.println("EMPTY STMTLIST");
	}
}
