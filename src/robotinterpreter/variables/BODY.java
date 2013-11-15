package robotinterpreter.variables;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import robotinterpreter.Code;
import robotinterpreter.RobotInterpreter;
import robotinterpreter.terminals.Terminals;
import robotinterpreter.variables.methods.METHODDEFINE;
import robotinterpreter.variables.vars.VARDECL;

public class BODY extends Variable
{
	private STMTLIST stmtList;
	private int startLine;
	private int finishLine;
	
	public METHODDEFINE method;
	public ArrayList<VARDECL> varTable;

	public BODY(BODY b, Code c)
	{
		body = b;
		varTable = new ArrayList<VARDECL>();
		
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
	
	public STMTLIST getStmtList()
	{
		return stmtList;
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
			RobotInterpreter.writeln("parse", "BODY: Start Line " + startLine + ", Finish Line " + finishLine);
			RobotInterpreter.writeln("parse", "{");
			if(stmtList != null)
				stmtList.print();
			else
				RobotInterpreter.write("parse", "EMPTY BODY");
			RobotInterpreter.write("parse", "}");
		}
		else RobotInterpreter.write("parse", "EMPTY BODY");
	}

	//Validate stmtlist
	public void validate() 
	{
		RobotInterpreter.writeln("validate", "Validating BODY");
		if(stmtList != null)
		{
			stmtList.validate();
		}
	}

	public Object execute(Object[] args) 
	{
		//Create map for holding variables
		Map<String, Object> varMap = new HashMap<String, Object>();
		for(VARDECL v : varTable)
		{
			varMap.put(v.id(), "");
		}
		RobotInterpreter.varStack.add(varMap);
		
		Object retVal = null;
		if(stmtList != null)
		{
			retVal = stmtList.execute(null);
		}
		
		//Remove this map from the top of the stack
		RobotInterpreter.varStack.remove(RobotInterpreter.varStack.size() - 1);
		
		return retVal;
	}
}
