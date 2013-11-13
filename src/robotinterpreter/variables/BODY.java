package robotinterpreter.variables;

import java.util.ArrayList;

import robotinterpreter.Code;
import robotinterpreter.RobotInterpreter;
import robotinterpreter.terminals.Terminals;
import robotinterpreter.variables.vars.VARDECL;

public class BODY extends Variable
{
	private STMTLIST stmtList;
	private int startLine;
	private int finishLine;
	private ArrayList<VARDECL> paramVarTable;

	public BODY(BODY b, Code c)
	{
		if(b == null)
		{
			b = this;
		}
		body = b;
		
		if(c.currentLine().equals(Terminals.OPENBRACE))
		{
			lineNum = startLine = c.currentLineNum();
			findCloseBrace(c);
			
			//Move to first statement.
			c.nextLine();
			if(c.currentLineNum() != finishLine)
				stmtList = new STMTLIST(body, c);

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

	//Validate stmtlist
	public void validate() 
	{
		System.out.println("Validating BODY");
		if(stmtList != null)
		{
			stmtList.validate();
		}
	}

	@Override
	public void execute() {
		// TODO Auto-generated method stub
		
	}
}
