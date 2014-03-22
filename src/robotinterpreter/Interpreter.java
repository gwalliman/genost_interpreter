package robotinterpreter;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import robotinterpreter.terminals.Terminals;
import robotinterpreter.variables.BODY;
import robotinterpreter.variables.STMT;
import robotinterpreter.variables.STMTLIST;
import robotinterpreter.variables.conditional.ELSE;
import robotinterpreter.variables.conditional.ELSEIF;
import robotinterpreter.variables.conditional.IF;
import robotinterpreter.variables.loop.LOOPFOR;
import robotinterpreter.variables.loop.LOOPUNTIL;
import robotinterpreter.variables.methods.METHODDEFINE;
import robotinterpreter.variables.vars.VARDECL;

/**
 * This class is the "main" class for the Robot Interpreter program.
 * It contains functions for managing master tables of defined methods and the values of variables in execution.
 * It also contains functions pertaining to data being output to a log.
 * Finally, it contains the main method which executes each stage of interpretation
 * 
 * @author Garret Walliman (gwallima@asu.edu)
 *
 */
public class Interpreter 
{
	private RobotInterpreter robotInterpreter;
	/*
	 * The varStack is a stack containing lists of active variables.
	 * Whenever we enter a body (either the main program body or a method body) a new layer is added to the stack
	 * containing entries for each variable defined in that body. This stack layer is then removed after exiting the body.
	 */
	private ArrayList<Map<String, Object>> varStack;
	
	/*
	 * The methodTable contains an entry for each method defined in the program.
	 * It has entries for both methods defined within the code, as well as for a set of external methods, which are read in at program start.
	 */
	private ArrayList<METHODDEFINE> methodTable;
	
	/*
	 * The extMethodTable is used to hold all external methods, which are read in at program start.
	 */
	private ArrayList<Object> extMethodTable;
	
	//BODY b is the "head" data structure for the program tree. It is the main program body which contains everything else.
	public BODY b;
	
	public boolean isReady = true;
	
	private String[] extMethodsArray = { "print", "intToString", "add", "subtract", "multiply", "divide", "drive", "turn", "stop", "getSonars", "getBearing", "driveDistance", "driveTime", "turnAngle", "turnTime", "turnToBearing" };
	
	public ArrayList<String> extMethods = new ArrayList<String>(Arrays.asList(extMethodsArray));

	/**
	 * This function is the "master" function, orchestrating virtually all interpretation functionality.
	 * 
	 * The function first reads in all externally defined methods.
	 * 
	 * It then receives the file containing the program code and goes through three steps:
	 * 1. Parsing: the program is parsed lexically and translated into various linked data structures.
	 * 		During lexical parsing, basic syntax error checking is performed.
	 * 2. Validation and Linking: the program is scanned for advanced syntax errors (i.e. type mismatch).
	 * 		Certain links are made within the program (e.g. a variable call is linked to the declaration of said variable, giving the call access to the variable's type)
	 * 3. Execution: the program is run. Steps 1 and 2 should take care of virtually all errors, so upon reaching execution, the program should run without errors.
	 * 		Exceptions may occur in certain rare cases (for example, divide by zero errors, which must be handled at the execution stage)
	 * 
	 * @param code the file which contains the program code
	 */
	public Interpreter(RobotInterpreter rob, String code)
	{
		robotInterpreter = rob;
		
		//Initialize the stacks / tables
		varStack = new ArrayList<Map<String, Object>>();
		methodTable = new ArrayList<METHODDEFINE>();
		extMethodTable = new ArrayList<Object>();
		
		//Step 0: Load in external methods.
		for(String id : extMethods)
		{
			//Every external method is stored in its own separate class in the robotinterpreter.variables.methods.external package.
			//The names of these methods (which is the same as the class they are defined in) are stored in an array in the ExtMethod class.
			//We go through this array to find each external method class, create a new instance of it, and add it to the two method tables
			try 
			{
				Class<?> extC = Class.forName("robotinterpreter.variables.methods.external." + id);
				Constructor<?> extCtor = extC.getConstructor(this.getClass());
				Object extInst = extCtor.newInstance(this);
				extMethodTable.add(extInst);
				methodTable.add(new METHODDEFINE(this, id));
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			} 
		}
		
		//Take the code file and get it ready for parsing.
		Code c = new Code(this, code);
		
		//Step 1: Parse.
		//Go over each line of code and populate with information immediately available
		b = new BODY(this, null, c);
	
		if(isReady)
		{
			b.print();
			writeln("parse", Code.newline + "=================");
			writeln("parse", "Code fully parsed!" + Code.newline + "=================");
		}
		else
		{
			writeln("parse", Code.newline + "=================");
			writeln("parse", "ERROR: Code failed to parse." + Code.newline + "=================");
			halt();
			return;
		}
		
		//Step 2: Link and Validate
		//Link up certain items: varcalls to variable array, method calls to actual method code, if/loop eventualities, etc.
		//Check type matching, etc.
		b.validate();
		
		//Validate external methods
		for(String id : extMethods)
		{
			findMethod(id).validate();
		}
		
		writeln("validate", "=================");
		if(isReady)
		{
			writeln("validate", "Code fully validated!" + Code.newline + "=================");
		}
		else
		{
			writeln("validate", "ERROR: Code failed to validate." + Code.newline + "=================");
			halt();
			return;
		}
		
		isReady = true;
	}
	
	public void execute()
	{
		//Step 3: Execute program
		writeln("message", "=================");
		writeln("message", "Execution output follows:" + Code.newline + "=================" + Code.newline);
		b.execute(null);
		writeln("message", Code.newline + "=================");
		writeln("message", "End of execution" + Code.newline + "=================");
	}
	
	public RobotInterpreter getRobotInterpreter() 
	{
		return robotInterpreter;
	}
	
	public ArrayList<Object> getExtMethodTable()
	{
		return extMethodTable;
	}
	
	public ArrayList<METHODDEFINE> getMethodTable()
	{
		return methodTable;
	}
	
	public ArrayList<Map<String, Object>> getVarStack()
	{
		return varStack;
	}
	
	/**
	 * Used to get the value of a variable stored in the variable stack.
	 * We start at the top of the stack and try to find a variable matching the provided id.
	 * If none is found, then the variable must be defined at a lower level, so we move down one level and try again.
	 * 
	 * This function should only ever be called during execution, so any variable that we search for should be defined (invalid varcalls are taken care of in validation)
	 * 
	 * @param id the name of the variable we are trying to get.
	 * @return the value of the variable as an Object.
	 */
	public Object getVar(String id)
	{
		for(int x = varStack.size() - 1; x >= 0; x--)
		{
			Map<String, Object> m = varStack.get(x);
			if(m.get(id) != null)
				return m.get(id);
		}
		
		//As all variables are confirmed to exist in Validation, we should never be here.
		return null;
	}
	
	/**
	 * Used to set the value of a variable stored in the variable stack.
	 * We start at the top of the stack and try to find a variable matching the provided id.
	 * If none is found, then the variable must be defined at a lower level, so we move down one level and try again.
	 * 
	 * This function should only ever be called during execution, so any variable that we search for should be defined (invalid varcalls are taken care of in validation)
	 * 
	 * @param id the name of the variable we are trying to set.
	 * @param val the value which we will set
	 */
	public void setVar(String id, Object val)
	{
		for(int x = varStack.size() - 1; x >= 0; x--)
		{
			Map<String, Object> m = varStack.get(x);
			if(m.get(id) != null)
			{
				m.put(id, val);
				break;
			}
		}
	}
	
	/**
	 * This function is used to find the declaration of a variable.
	 * These VARDECLs are stored in tables located within code bodies. Each body will contain all variables defined within its scope; method bodies will also contain all parameters to the method.
	 * Every body also contains a link to its parent body.
	 * 
	 * In this function we search the varTable of the provided body for a VARDECL matching the provided id.
	 * If one is not found, we move up to the parent body and try again. This continues until the variable is found.
	 * 
	 * This function should only be called using ids retrieved from code statements. These ids should always be valid; invalid variable names should be detected during validation.
	 * 
	 * Note that if / loop bodies do not have valid varTables, but no statement within these bodies is linked to the if / loop body, but instead whatever parent body has a valid vartable, so
	 * this method should never be called with an if / loop codebody.
	 * 
	 * @param b	the "lowest" codebody where we will begin our search
	 * @param id	the id of the VARDECL we are looking for
	 * @return	the found VARDECL
	 */
	public VARDECL findVar(BODY b, String id)
	{
		for(VARDECL var : b.varTable)
		{
			if(var.id().equals(id))
				return var;
		}
		if(b.body() != null)
		{
			return findVar(b.body(), id);
		}
		else return null;
	}
	
	/**
	 * This method is used to find the method declaration structure in the methodTable.
	 * The methodTable is universal and unscoped (unlike variables) so we simply search the table to find the correct method.
	 * 
	 * This should never be called with an invalid method id; invalid method calls should be detected during validation.
	 * 
	 * @param id	the method id
	 * @return	the corresponding METHODDEFINE
	 */
	public METHODDEFINE findMethod(String id)
	{
		for(METHODDEFINE method : methodTable)
		{
			if(method.id().equals(id))
				return method;
		}
		return null;
	}
	
	/**
	 * Printer function that prints the defined variables.
	 * First prints the vars defined in the main codebody,
	 * and then prints the vars defined in each individual method's codebody.
	 * 
	 */
	public void printAllVars()
	{
		printVars(b, "Global");
		for(METHODDEFINE m : methodTable)
		{
			if(m.methodType() != "external")
			{
				printVars(m.codeBody(), m.id());
			}
		}
	}
	
	/**
	 * Prints the defined variables for a single codebody.;
	 * 
	 * @param b	the body which we are printing variables for
	 * @param s	the name of the scope in which we are printing. Is only printed in a debug statement, so can be whatever you want.
	 */
	public void printVars(BODY b, String s)
	{
		writeln("debug", "===================");
		writeln("debug", "Printing Variable Table for " + s);
		writeln("debug", "===================");

		for(VARDECL var : b.varTable)
		{
			var.printVar();
			write("debug", Code.newline);
		}
		
		STMTLIST stmts = b.getStmtList();
		while(stmts != null)
		{
			STMT stmt = stmts.getStmt();
			if(stmt.type().equals(Terminals.IF))
			{
				IF i = ((IF)stmt.getStmt());
				ELSEIF elseif = i.getElseIf();
				ELSE els = i.getElse();
				
				printVars(i.getCodeBody(), "IF Statement (Line " + stmt.lineNum() + ") within scope of " + s);
				
				while(elseif != null)
				{
					printVars(elseif.getCodeBody(), "ELSEIF Statement (Line " + elseif.lineNum() + ") within scope of " + s);
					elseif = elseif.getNextElseIf();
				}
				
				if(els != null)
				{
					printVars(els.getCodeBody(), "ELSE Statement (Line " + els.lineNum() + ") within scope of " + s);
				}
			}
			else if(stmt.type().equals(Terminals.LOOPUNTIL))
			{
				printVars(((LOOPUNTIL)stmt.getStmt()).getCodeBody(), "LOOPUNTIL Statement (Line " + stmt.lineNum() + ") within scope of " + s);

			}
			else if(stmt.type().equals(Terminals.LOOPFOR))
			{
				printVars(((LOOPFOR)stmt.getStmt()).getCodeBody(), "LOOPFOR Statement (Line " + stmt.lineNum() + ") within scope of " + s);
			}
			stmts = stmts.getNextStmt();
		}
		
		writeln("debug", "===================");
		writeln("debug", "End Variable Table for " + s);
		writeln("debug", "===================");

	}
	
	/**
	 * Prints all defined methods using the methods' print function.
	 */
	public void printMethods()
	{
		writeln("debug", "===================");
		writeln("debug", "Printing Method Table");
		writeln("debug", "===================");
		
		for(METHODDEFINE method : methodTable)
		{
			method.print();
			write("debug", Code.newline);
		}
		writeln("debug", "===================");
	}
	
	/**
	 * This function is used to control which printed messages should be printed to the screen.
	 * Modify the values in here to turn on or off different message printings.
	 * 
	 * Garret's todo: hook this method into the UI so these can be turned on / off dynamically.
	 * 
	 * @param t	the type of message we are querying about
	 * @return	true if we can print this type of message, false otherwise
	 */
	private boolean showMessage(String t)
	{
		/*switch(t)		--No switch(string) in 1.6 -- IP
		{
			//Display Parser messages
			case "parse":
				return true;
			//Display Validate messages
			case "validate":
				return false;
			//Display Debugging messages
			case "debug":
				return true;
			//Display all other messages
			default:
				return true;
		}*/
		
		if (t.equals("parse"))
			return true;
		else if (t.equals("validate"))
			return false;
		else if (t.equals("debug"))
			return true;
		else
			return true;
		
	}
	
	/**
	 * This function should be used within the program to write a message to the screen WITHOUT a linebreak.
	 * We wrap standard print calls in our own message to both control whether messages are printed or not,
	 * as well as to make it easy to control where those messages are printed to (i.e. Java console log vs. a JTextField)
	 * 
	 * @param type	the type of message we are printing
	 * @param s	the message itself
	 */
	public void write(String type, String s)
	{
		if(showMessage(type))
		{
			for(RobotListener l : robotInterpreter.getRobotListeners())
			{
				l.print(s);
			}
		}
	}
	
	/**
	 * This function should be used within the program to write a message to the screen WITH a linebreak.
	 * We wrap standard print calls in our own message to both control whether messages are printed or not,
	 * as well as to make it easy to control where those messages are printed to (i.e. Java console log vs. a JTextField)
	 * 
	 * @param type	the type of message we are printing
	 * @param s	the message itself
	 */
	public void writeln(String type, String s)
	{
		if(showMessage(type))
		{
			for(RobotListener l : robotInterpreter.getRobotListeners())
			{
				l.println(s);
			}
		}
	}
	
	/**
	 * This method should be called anytime the parser / validator / execution encounters an error in the code.
	 * The method will print an error message to the screen and stop the program.
	 * 
	 * Garret's todo: Allow us to scan for multiple errors instead of halting at the very first error.
	 * 
	 * @param var	the Variable structure (i.e. BODY, IF, ASSIGN) where the error occurred
	 * @param lineNum	the line number where the error occurred
	 * @param c	the code fragment which contains the error
	 * @param error	a String describing the error
	 */
	public void error(String var, int lineNum, String c, String error)
	{
		String fu = var.toUpperCase() + " ERROR Near Line " + lineNum + ": " + c + Code.newline + error;
		//JOptionPane.showMessageDialog(null, fu, var + " ERROR", JOptionPane.ERROR_MESSAGE);
		for(RobotListener l : robotInterpreter.getRobotListeners())
		{
			l.error(var, fu);
		}
		isReady = false;
	}
	
	public static void halt()
	{
		Thread.currentThread().interrupt();
		return;
	}

	public boolean isReady() 
	{
		return isReady;
	}
}
