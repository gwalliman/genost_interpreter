package robotinterpreter.variables.conditional;

import robotinterpreter.Code;
import robotinterpreter.RobotInterpreter;
import robotinterpreter.terminals.Terminals;
import robotinterpreter.variables.BODY;
import robotinterpreter.variables.Variable;

/**
 * CONDITIONLISTS are used to build advanced logical statement. At the core, a CONDITIONLIST allows various ways of comparing multiple CONDITION using "and" or "or".
 * A basic logical statement (x AND y) should be extensible in two ways: first, by concatenating (x AND y OR z) and second, by nesting (x AND (y OR z)).
 * CONDITIONLIST structures are able to accomplish both of these functionalities.
 * 
 * A CONDITIONLIST contains one Object "con", which can be either a CONDITION or another CONDITIONLIST. It also contains a logical operator and a link to another CONDITIONLIST "nextCon" (separate from the aforementioned CONDITIONLIST/CONDITION object)
 * Using this structure, we can create a simple logical comparison, and then concatinate or nest.
 * 
 * One CONDITION can be represented by setting the con Object to that condition. A logical comparison may be made by setting a logical operator, creating a new CONDITIONLIST and linking it to nextCon.
 * Concatination may be achieved indefinitely by continuing to string CONDITIONLISTs together, like in a linked list.
 * 
 * We may nest at any time by linking "con" to a CONDITIONLIST instead of a CONDITION.
 * 
 * In this way, logical statements of any complexity may be represented and executed.
 * 
 * We are able to entirely remove ambiguity by wrapping every CONDITION and nested CONDITIONLISTS in brackets ([ ])
 * 
 * @author Garret Walliman (gwallima@asu.edu)
 *
 */
public class CONDITIONLIST extends Variable 
{
	//Con an be either a CONDITION or a CONDITIONLIST, if we are nesting
	private Object con;
	private String conType;
	private String logOp;
	
	//If we are concatinating, we link to the next condition here.
	private CONDITIONLIST nextCon;
	
	private final String CONDITIONLIST = "CONDITIONLIST";
	private final String CONDITION = "CONDITION";
	
	/**
	 * Takes a logical statement. 
	 * This statement will consist of either a CONDITION, or some structure built from a stack of CONDITIONLISTS
	 * If it is the former, we pass the CONDITION on to the CONDITION parser.
	 * If it is the latter, we recursively parse down the layers.
	 * 
	 * @param b	the parent body
	 * @param c	the Code object
	 * @param s	a string consisting of a logical statement. Format: "[var a == int 6]", "[var a > int 6] and [string "asdf" != var s]", "[[var a > int 6] and [string "asdf" != var s]] or [bool true == var b]"
	 */
	public CONDITIONLIST(BODY b, Code c, String s)
	{
		body = b;
		code = s;
		lineNum = c.currentLineNum();
		
		String[] tokens = Code.tokenize(code);
		
		//Every CONDITIONLIST will always have a bracket as its first character.
		if(tokens[0] != Terminals.OPENBRACKET)
			RobotInterpreter.error("CONDITIONLIST", lineNum, code, "CONDITIONLIST must begin with [");
		
		//We must now find a matching close bracket. This may be the close bracket to a CONDITION or a CONDITIONLIST.
		int closeBracket = findCloseBracket(tokens);
		if(closeBracket == -1)
			RobotInterpreter.error("CONDITIONLIST", lineNum, code, "Missing ]! CONDITIONLIST must have matching brackets!");

		//Next we figure out whether the code within the two brackets we have identified contains a logical operator.
		boolean hasLogOp = false;
		for(int x = 0; x < closeBracket; x++)
		{
			if(tokens[x].equals(Terminals.AND) || tokens[x].equals(Terminals.OR))
				hasLogOp = true;
		}
		
		//If hasLogOp is true, then c is a CONDITIONLIST. We recursively parse and set this CONDITIONLIST to con.
		//If false, c is a CONDITION. We send it to the CONDITION parser
		//Note that in both cases, we strip away the outer brackets before sending it on.
		if(hasLogOp)
		{
			conType = CONDITIONLIST;
			con = new CONDITIONLIST(body, c, Code.implode(tokens, " ", 1, closeBracket - 1));
		}
		else
		{
			conType = CONDITION;
			con = new CONDITION(body, c, Code.implode(tokens, " ", 1, closeBracket - 1));

		}
		
		//If there is a token following tokens[closeBracket], it must be a logop, meaning that there is a sequential CONDITIONLIST.
		//We set nextCon to this new CONDITIONLIST. Again, we strip away the outer brackets.
		if(tokens.length - 1 > closeBracket)
		{
			if(Terminals.logOps.contains(tokens[closeBracket + 1]))
			{
				logOp = tokens[closeBracket + 1];
				if(tokens.length - 1 > closeBracket + 1)
				{
					nextCon = new CONDITIONLIST(body, c, Code.implode(tokens, " ", closeBracket + 2, tokens.length - 1));
				}
				else RobotInterpreter.error("CONDITIONLIST", lineNum, code, "A CONDITION or CONDITIONLIST must follow an AND or OR");
			}
			else RobotInterpreter.error("CONDITIONLIST", lineNum, code, "Only AND or OR may follow a CONDITIONLIST");

		}
	}
	
	/**
	 * Goes through the tokenList and finds the closing bracket that matches an opening bracket.
	 * Handles having inner bracket pairs.
	 * 
	 * @param tokens	the token list, minus the opening bracket
	 * @return	the index of the token list where the closing bracket is located, or -1 if no matching closing bracket is found.
	 */
	private int findCloseBracket(String[] tokens)
	{
		int openBrackets = 0;
		int tokenNum = 0;
		for(String token : tokens)
		{
			if(token.equals(Terminals.OPENBRACKET))
				openBrackets++;
			else if(token.equals(Terminals.CLOSEBRACKET))
				openBrackets--;
			
			if(openBrackets == 0)
			{
				return tokenNum;
			}
			else tokenNum++;
		}
		return -1;
	}
	
	/**
	 * Print function. Determines how to print con (as a CONDITION or a CONDITIONLIST).
	 * Next prints the logop and nextCon, if we have one.
	 */
	public void print() 
	{
		if(conType.equals(CONDITIONLIST))
		{		
			RobotInterpreter.write("parse", "[");
			((CONDITIONLIST)con).print();
			RobotInterpreter.write("parse", "]");
		}
		else if(conType.equals(CONDITION))
		{
			((CONDITION)con).print();

		}
		
		if(logOp != null)
		{
			RobotInterpreter.write("parse", " " + logOp + " ");
			nextCon.print();
		}
	}

	/**
	 * Validates the con by determining what type it is and calling the validation function for that one.
	 * Next, validates the nextCon, if there is one.
	 */
	public void validate() 
	{ 
		RobotInterpreter.writeln("validate", "Validating CONDITIONLIST");

		if(conType.equals(CONDITIONLIST))
		{		
			((CONDITIONLIST)con).validate();
		}
		else if(conType.equals(CONDITION))
		{
			((CONDITION)con).validate();
		}
		
		if(nextCon != null)
		{
			nextCon.validate();
		}
	}

	/**
	 * Execute con by calling its proper execute function.
	 * Both functions will return a boolean value.
	 * 
	 * If we have a nextCon, execute that and receive its own boolean value.
	 * Finally, compare the two values using the logOp and return the result.
	 * 
	 */
	public Object execute(Object args[]) 
	{
		boolean go = false;
		if(conType.equals(CONDITIONLIST))
		{		
			go = (boolean) ((CONDITIONLIST)con).execute(null);
		}
		else if(conType.equals(CONDITION))
		{
			go = (boolean) ((CONDITION)con).execute(null);
		}
		
		if(nextCon != null)
		{
			//Not sure about this variable name, it may be harmful.
			boolean go2 = false;
			go2 = (boolean) ((CONDITIONLIST)nextCon).execute(null);
			
			if(logOp.equals(Terminals.AND))
				return go && go2;
			else if(logOp.equals(Terminals.OR))
				return go || go2;
		}
		return go;
	}
}
