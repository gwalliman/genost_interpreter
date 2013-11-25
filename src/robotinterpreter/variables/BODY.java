package robotinterpreter.variables;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import robotinterpreter.Code;
import robotinterpreter.RobotInterpreter;
import robotinterpreter.terminals.Terminals;
import robotinterpreter.variables.methods.METHODDEFINE;
import robotinterpreter.variables.vars.VARDECL;

/**
 * Every BODY variable contains a series of statements and a scope for variables.
 * 
 * BODY variables are nestable: bodies may appear within other bodies, in which case the former is the parent body of the latter.
 * In a nested (child) body, newly declared variables will override variables declared in a parent body, if the variables have the same ID.
 * However, no two variables may have the same name within a single body.
 * 
 * Note that all BODYs should be linked to their parent BODY by passing the parent BODY in via the constructor.
 * 
 * @author Garret Walliman (gwallima@asu.edu)
 *
 */
public class BODY extends Variable
{
	//The list of statements associated with the body.
	private STMTLIST stmtList;
	//The code lines on which the body starts and ends.
	private int startLine;
	private int finishLine;
	
	//If the BODY is the body of a method, we link to the methoddefine stmt here.
	public METHODDEFINE method;
	
	//This table contains the list of variables specifically defined within this BODY's scope
	public ArrayList<VARDECL> varTable;

	/** 
	 * Links the body to its parent body, creates a var table,
	 * finds the bounds of the body and creates the stmtList within the body's bounds.
	 * 
	 * @param b	the parent body, or null if this is the main body.
	 * @param c	the Code object
	 */
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
	
	/**
	 * Goes through the code trying to find a matching closeBrace for the body.
	 * Accounts for the existence of subbodies within the body we are searching.
	 * 
	 * @param c	the Code object
	 */
	public void findCloseBrace(Code c)
	{
		//We know we have one open paren.
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
	
	/**
	 * Getter for the stmtlist variable.
	 * 
	 * @return the body's stmtlist
	 */
	public STMTLIST getStmtList()
	{
		return stmtList;
	}
	
	/**
	 * Getter for the startLine (the line which contains the body's open brace)
	 * 
	 * @return the starting line for the body
	 */
	public int getStartLine()
	{
		return startLine;
	}
	
	/**
	 * Getter for the finishLine (the line which contains the body's close brace)
	 * 
	 * @return the finishing line for the body
	 */
	public int getFinishLine()
	{
		return finishLine;
	}
	
	/**
	 * Prints the body and its stmts.
	 * Also prints a line indicating the body's open and close line numbers.
	 */
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

	/**
	 * Validation function for the body.
	 * Validates the stmtlist, if it exists.
	 */
	public void validate() 
	{
		RobotInterpreter.writeln("validate", "Validating BODY");
		if(stmtList != null)
		{
			stmtList.validate();
		}
	}

	/**
	 * Execution function for the body.
	 * We first add a layer to the varStack and populate it with entries for each vardecl in this body's scope.
	 * We then execute the stmtList, if there is one.
	 * After the stmtList has returned, we remove the top layer of the varStack, as we no longer care about those variables' values.
	 * Finally, we return whatever value the stmtList returned.
	 * 
	 * @param args	If this is a method's codebody, then the method params will be passed in via args. Entries for these args will be made in the varStack.
	 */
	public Object execute(Object[] args) 
	{
		//Create map for holding variable values.
		//This includes any variables declared in the body scope and any method parameters, if this is a method body.
		Map<String, Object> varMap = new HashMap<String, Object>();
		
		//Add entries for all vars in the varTable
		//We set default values here.
		for(VARDECL v : varTable)
		{
			if(v.type().equals(Terminals.INT))
				varMap.put(v.id(), 0);
			else if(v.type().equals(Terminals.STRING))
				varMap.put(v.id(), "");
			else if(v.type().equals(Terminals.BOOL))
				varMap.put(v.id(), false);
		}
		RobotInterpreter.varStack.add(varMap);
		
		//If this is a method codebody and it has parameters, set the value for the param vars in the varstack to those params.
		if(method != null && args != null)
		{
			for(int x = 0; x < args.length; x++)
			{
				String id = method.getParam(x).id();
				RobotInterpreter.setVar(id, args[x]);
			}
		}
		
		//Execute the body statements.
		Object retVal = null;
		if(stmtList != null)
		{
			retVal = stmtList.execute(args);
		}
		
		//Remove this map from the top of the stack
		RobotInterpreter.varStack.remove(RobotInterpreter.varStack.size() - 1);
		
		return retVal;
	}
}
