package robotinterpreter.variables.conditional;

import robotinterpreter.Code;
import robotinterpreter.Interpreter;
import robotinterpreter.terminals.Terminals;
import robotinterpreter.variables.BODY;
import robotinterpreter.variables.CALL;
import robotinterpreter.variables.Variable;

/**
 * A condition is a single comparison between two values of matching types.
 * Possible comparisons are: 
 * - logical equality (==)
 * - logical inequality (!=)
 * - less than (<)
 * - greater than (>)
 * - less than or equal to (<=)
 * - greater than or equal to (>=)
 * 
 * The above comparators can all be used with ints. However, string and boolean comparison can only use logical equality / inequality.
 * 
 * @author Garret Walliman (gwallima@asu.edu)
 *
 */
public class CONDITION extends Variable
{
	private Interpreter interpreter;
	
	//We have two CALLS that provide the values being compared.
	private CALL lhs;
	private CALL rhs;
	private String comparator;
	
	/**
	 * Takes a string of code which should contain the comparison.
	 * Tokenizes the code and finds the token containing the comparator.
	 * Splits the string by the comparator, and sends the two halves to CALLs.
	 * 
	 * @param b	the parent body
	 * @param c	the Code object
	 * @param s	a string of code containing a comparison. It should ONLY contain the comparison, and not any enclosing brackets. Format: "x > y", "a == b"
	 */
	public CONDITION(Interpreter in, BODY b, Code c, String s)
	{
		interpreter = in;
		body = b;
		code = s;
		lineNum = c.currentLineNum();
		
		//This holds the index of the tokenized string which corresponds to the comparator.
		int symbolNum = -1;
		
		String[] tokens = c.tokenize(s);
		for(int x = 0; x < tokens.length; x++)
		{
			//Once we find the comparator, split the string around the comparator and send the two halves to the CALL parser.
			if(Terminals.comparators.contains(tokens[x]))
			{
				symbolNum = x;
				comparator = tokens[x];
				
				lhs = new CALL(interpreter, body, c, c.implode(tokens, " ", 0, symbolNum - 1));
				rhs = new CALL(interpreter, body, c, c.implode(tokens, " ", symbolNum + 1, tokens.length - 1));
			}
		}
		if(symbolNum == -1)
			interpreter.error("CONDITION", lineNum, code, "CONDITION must have a comparator (==, !=, >, <, >=, <=)");
	}
	
	/**
	 * Prints the CONDITION.
	 */
	public void print() 
	{
		interpreter.write("parse", "[");
		lhs.print();
		interpreter.write("parse", " " + comparator + " ");
		rhs.print();
		interpreter.write("parse", "]");
	}

	//lhs and rhs must be of same type
	//Comparator must be valid for comparison type
	//Validate vars and methods
	/**
	 * Validation function for the CONDITION.
	 * We need to ensure:
	 * - That the lhs and the rhs both validate.
	 * - That the lhs and the rhs are of the same type.
	 * - That the comparator is appropriate for the datatype being compared.
	 */
	public void validate() 
	{
		interpreter.writeln("validate", "Validating CONDITION");

		lhs.validate();
		rhs.validate();
		String lhsType = lhs.type();
		String rhsType = rhs.type();
		if(!lhsType.equals(rhsType))
		{
			interpreter.error("CONDITION", lineNum, code,"LHS and RHS must be of the same type in a condition");
		}
		if((lhsType.equals(Terminals.STRING) || lhsType.equals(Terminals.BOOL)) && (comparator != Terminals.EQ && comparator != Terminals.NEQ))
		{
			interpreter.error("CONDITION", lineNum, code,"A comparison between two " + lhsType + "s can only be compared by " + Terminals.EQ + " or " + Terminals.NEQ);
		}
	}

	/**
	 * Execution function - executes the CONDITION and returns whether it is true or false.
	 * First we execute the lhs and the rhs to get their respective values.
	 * We then compare the two values using the provided comparator.
	 * 
	 * @param args	should always be null
	 * @return a boolean value indicating whether the comparison is true or false
	 */
	public Object execute(Object[] args) 
	{
		Object l = lhs.execute(null);
		Object r = rhs.execute(null);
		
		boolean go = false;
		
		if (comparator.equals(Terminals.EQ))
		{
			if(lhs.type().equals(Terminals.STRING))
			{
				go = ((String)l).equals((String)r);
			}
			else if(lhs.type().equals(Terminals.BOOL))
			{
				go = ((Boolean)l) == ((Boolean)r);
			}
			else if(lhs.type().equals(Terminals.INT))
			{
				go = ((Integer)l) == ((Integer)r);
			}
		}
		else if (comparator.equals(Terminals.NEQ))
		{
			if(lhs.type().equals(Terminals.STRING))
			{
				go = !((String)l).equals((String)r);
			}
			else if(lhs.type().equals(Terminals.BOOL))
			{
				go = ((Boolean)l) != ((Boolean)r);
			}
			else if(lhs.type().equals(Terminals.INT))
			{
				go = ((Integer)l) != ((Integer)r);
			}
		}
		//After this point, we can only have ints
		else if (comparator.equals(Terminals.LT))
				go = ((Integer)l) < ((Integer)r);
		else if (comparator.equals(Terminals.GT))
				go = ((Integer)l) > ((Integer)r);
		else if (comparator.equals(Terminals.LTE))
				go = ((Integer)l) <= ((Integer)r);
		else if (comparator.equals(Terminals.GTE))
				go = ((Integer)l) >= ((Integer)r);
		
		return go;
	}
}
