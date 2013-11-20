package robotinterpreter.variables;

/**
 * abstract class Variable
 * 
 * This class is used by every variable (nonterminal) in the program.
 * It contains variables used in every nonterminal, getter / setter functions for these vars, and abstract functions for the nonterminals to implement.
 * 
 * @author Garret Walliman (gwallima@asu.edu)
 *
 */
public abstract class Variable 
{
	//The parent body of the nonterminal. Note that this will only be different from the main body for nonterminals within METHODDEFINE code bodies.
	protected BODY body;
	//The actual code containing the code making up the nonterminal
	protected String code;
	//The line number on which the code above appears
	protected int lineNum;
	
	/**
	 * These abstract functions must be implemented by every nonterminal
	 */
	
	/**
	 * public abstract void print()
	 * 
	 * Print is used to print out the parsed contents of the nonterminal, in the style of code.
	 */
	public abstract void print();
	
	/**
	 * public abstract void validate()
	 * 
	 * Validate is used to ensure that the parsed code conforms to semantic rules. We list which semantic rules we must conform to within the comments of each nonterminal.
	 * We also perform some linking in this function.
	 */
	public abstract void validate();
	
	/**
	 * public abstract Object execute(Object[] args)
	 * 
	 * This function is used in the execution of the code. Any functionality associated with the nonterminal must be coded here.
	 * Note that we provide both an Object return type and an Object array for arguments. We do not know a priori what return / objects are used in execute, and so we keep it general.
	 * (Note that we only use either of these in rare occasions)
	 * 
	 * @param args	an array of objects, whatever arguments must be passed to the nonterminal implementing the function.
	 * @return	whatever the function returns; null if it returns nothing.
	 */
	public abstract Object execute(Object[] args);
	
	/**
	 * public BODY body()
	 * 
	 * @return	a reference to the parent body of this nonterminal
	 */
	public BODY body()
	{
		return body;
	}
	
	/**
	 * public String code()
	 * 
	 * @return	the code stored for this nonterminal
	 */
	public String code()
	{
		return code;
	}
	
	/**
	 * public int lineNum()
	 * 
	 * @return	the line number this nonterminal appears on
	 */
	public int lineNum()
	{
		return lineNum;
	}
}
