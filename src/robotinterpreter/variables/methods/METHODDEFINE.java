package robotinterpreter.variables.methods;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import robotinterpreter.Code;
import robotinterpreter.RobotInterpreter;
import robotinterpreter.terminals.Terminals;
import robotinterpreter.variables.BODY;
import robotinterpreter.variables.ID;
import robotinterpreter.variables.STMTLIST;
import robotinterpreter.variables.Variable;
import robotinterpreter.variables.methods.external.ExtMethod;
import robotinterpreter.variables.vars.VARDECL;

/**
 * A METHODDEFINE, as its name suggests, defines a method, which may be executed elsewhere in the code.
 * The METHODDEFINE consists of three items: 
 * 1. A code body to execute
 * 2. A list of parameter types which are passed in when executing the method
 * 3. A return value type which should be returned by the code body execution.
 * 
 * We use the METHODDEFINE for two types of methods: internal and external.
 * An internal method is defined entirely within the provided code. We must parse the code to determine the three items listed above.
 * An external method is one that is preprogrammed - its parameter types and return types are stored in packaged code, and must be read in at runtime.
 * Note that an external method has no BODY - its own execution is handled by an execution function that it defines in its packaged code.
 * We create a METHODDEFINE for our external methods so that we have one unified way of calling methods, which is using the METHODDEFINE.
 * 
 * Note that METHODDEFINEs create a new scope for their code bodies, so any variables declared within the code body will be accessible only from within that body.
 * 
 * @author Garret Walliman (gwallima@asu.edu)
 *
 */
public class METHODDEFINE extends Variable {
	
	//TYPE is the datatype the method returns
	private String type;
	//METHODTYPE is "internal" or "external", as defined above
	private String methodType;
	//ID is the name of the method
	private String id;
	private BODY codeBody;
	//The parameters (types and internal id) for the method
	private DEFPARAMLIST params;
	//The number of parameters
	private int numParams = 0;
	
	private static final String INTERNAL = "internal";
	private static final String EXTERNAL = "external";

	/**
	 * This constructor is called for internal methods.
	 * It gets all the metrics regarding where this definition is in the code.
	 * It then parses the three main items:
	 * 1. Return type
	 * 2. Parameters
	 * 3. Code body
	 * 
	 * @param b	the parent body
	 * @param c	the Code file
	 */
	public METHODDEFINE(BODY b, Code c)
	{
		body = b;
		lineNum = c.currentLineNum();
		code = c.currentLine();
		
		//This is an internal method
		methodType = INTERNAL;
			
		String[] tokens = Code.tokenize(c.currentLine());
		
		//Get the method's return type, which should always be the first token
		type = tokens[1];
		if(!Terminals.dataTypes.contains(type)) RobotInterpreter.halt("METHODDEFINE", lineNum, code, "Invalid METHODDEFINE data type. Must be void, int, string, or bool");
			
		//Get the ID, which should always be the third token.
		id = ID.validate(tokens[2], c);
		if(ExtMethod.extMethods.contains(id))
		{
			RobotInterpreter.halt("METHODDEFINE", lineNum, code, "Cannot create method of the name " + id + ", this is a reserved method");
		}
		
		//Fourth token should always be OPENPAREN
		if(tokens[3] != Terminals.OPENPAREN)
		{
			RobotInterpreter.halt("METHODDEFINE", lineNum, code, "ID must be followed by (");
		}
		
		//Last token should always be CLOSEPAREN
		if(tokens[tokens.length - 1] != Terminals.CLOSEPAREN)
		{
			RobotInterpreter.halt("METHODDEFINE", lineNum, code, "METHODDEFINE header must end with )");
		}
		
		//Parsing DEFPARAMLIST
		//If the fifth token is not a CLOSEPAREN, we have parameters, so we construct a DEFPARAMLIST
		if(tokens[4] != Terminals.CLOSEPAREN)
		{
			params = new DEFPARAMLIST(body, c, Code.implode(tokens, " ", 4, tokens.length - 2), 0);
		}
		//If the fifth token is a CLOSEPAREN, we must ensure that it is the last token.
		else if(tokens.length != 5)
		{
			RobotInterpreter.halt("METHODDEFINE", lineNum, code, "Syntax error in METHODDEFINE: characters after CLOSEPAREN");
		}
		
		//Parsing BODY
		c.nextLine();
		codeBody = new BODY(body, c);
		
		//Link the codebody to this method
		codeBody.method = this;
		
		//If we have parameters, create VARDECLs for them and add them to the variable table of the code body.
		if(params != null)
		{
			DEFPARAMLIST p;
			for(int x = 0; (p = getParam(x)) != null; x++)
			{
				VARDECL v = new VARDECL(p.id(), p.type());
				codeBody.varTable.add(v);
			}
		}
		
		//Add this method to the global method table.
		RobotInterpreter.methodTable.add(this);
	}
	
	/**
	 * This constructor is called for external methods.
	 * We pass in the method's ID, look it up in the extMethodTable and create a METHODDEFINE for it.
	 * 
	 * Because the external methods define their own execution functions, we only have to get:
	 * 1. Return Type
	 * 2. Parameters
	 * 
	 * @param s	the external method's ID. Format: "print", "add"
	 */
	public METHODDEFINE(String s)
	{
		//We put dummy data for the code parameters
		lineNum = -1;
		code = "Externally defined";
		methodType = EXTERNAL;
		id = s;
		codeBody = null;
		
		//Go through the extMethodTable and find the method that matches this id.
		//The extMethodTable is populated before we begin parsing the code.
		//Additionally, we only call this constructor on the external method IDs.
		//So, we will always find the method and don't have to worry about the id not appearing in the table.
		for(Object ext : RobotInterpreter.extMethodTable)
		{
			if(((ExtMethod)ext).id().equals(s))
			{
				//Get the return type
				type = ((ExtMethod)ext).type();
				//Get the parameters and create a DEFPARAMLIST for them.
				ArrayList<String> pt = new ArrayList<String>(Arrays.asList(((ExtMethod)ext).paramTypes()));
				params = new DEFPARAMLIST(pt, 0);
			}
		}
		
	}
	
	/**
	 * @return	the method's id
	 */
	public String id()
	{
		return id;
	}
	
	/**
	 * @return	the method's return datatype
	 */
	public String type()
	{
		return type;
	}
	
	/**
	 * @return	internal or external, depending on the method's type.
	 */
	public String methodType()
	{
		return methodType;
	}
	
	/**
	 * @return	a reference to this method's code body.
	 */
	public BODY codeBody() 
	{
		return codeBody;
	}
	
	/**
	 * @return	the number of params this method has
	 */
	public int numParams()
	{
		return numParams;
	}
	
	/**
	 * Goes through our parameters and returns the param with the number indicated by paramNum, starting from 0.
	 * If paramNum is out of bounds (either less than 0 or greater than the number of params) we return null
	 * 
	 * @param paramNum	the parameter number, starting from 0
	 * @return	a reference to that parameter, or null if the paramNum is out of bounds
	 */
	public DEFPARAMLIST getParam(int paramNum) 
	{
		DEFPARAMLIST param = params;
		
		//Loop until we find the matching parameter, or run out of parameters
		while(param != null && param.getNum() != paramNum)
		{
			param = param.nextParam();
		}
		
		return param;
	}
	
	/**
	 * Simple print function. Prints the method's type, id, params, and body.
	 */
	public void print() 
	{
		RobotInterpreter.write("parse", "methoddefine " + type + " " + id + "(");
		if(params != null)
			params.print();
		RobotInterpreter.writeln("parse", ")");
		codeBody.print();
	}

	//Ensure that method doesn't exist twice
	//Validate body
	//Validate params
	/**
	 * Ensures that the method has not already been defined.
	 * If we have parameters, we validate them. We also use this opportunity to set the number of params we have.
	 * If the method is internal, we validate its body. If it is external, we assume the execution function is correct syntactically and semantically
	 * 
	 */
	public void validate() 
	{
		RobotInterpreter.writeln("validate", "Validating METHODDEFINE");

		//Ensure the method has not already been defined
		if(Collections.frequency(RobotInterpreter.methodTable, RobotInterpreter.findMethod(id)) > 1)
		{
			RobotInterpreter.halt("METHODDEFINE", lineNum, code, "Method " + id + " cannot be defined more than once!");
		}		
		//If we have params, validate them.
		if(params != null)
		{
			params.validate();
			DEFPARAMLIST p = params;
			
			//Set numParams
			while(p != null)
			{
				numParams++;
				p = p.nextParam();
			}
		}
		
		//If the method is internal, validate the body.
		if(methodType.equals(INTERNAL))
		{
			codeBody.validate();
			
			//If this method has a non-void return type, but the body has no return statement, we have a problem.
			if(!type.equals(Terminals.VOID) && !hasReturn())
			{
				RobotInterpreter.halt("METHODDEFINE", lineNum, code, "Method " + id + " does not have a return statement");
			}
		}
	}

	/**
	 * Goes through the body statements and ensures that a return type is present.
	 * Note that a body can have multiple return types; we only need one.
	 * 
	 * NOTE: this currently only checks return statements in the MAIN BODY. If there are items that have subbodies (like loops) we do not look there!
	 * This can lead to situations where a return statement will always be hit, yet this function does not see it.
	 * 
	 * Because this code is procedurally generated, we will handle this by always requiring some sort of return statement at the end of the method code in the main body.
	 * 
	 * @return	true if we find a return statement, false otherwise
	 */
	private boolean hasReturn() 
	{
		STMTLIST s = codeBody.getStmtList();
		while(s != null)
		{
			if(s.getStmt().type().equals(Terminals.RETURN))
				return true;
			else s = s.getNextStmt();
		}
		return false;
		
	}

	/**
	 * This is only called from a METHOD variable's execute function.
	 * 
	 * If this is an internal method, we execute its code body.
	 * If this is an external method, we call its execute function.
	 * 
	 * In both cases, we pass in the args parameter, which will contain the parameter values.
	 * (These values are retrieved in the METHOD's execute function)
	 * 
	 * @param args	the parameters for the function
	 * @return	the function's return value, or null if it doesn't have one
	 * 
	 */
	public Object execute(Object args[]) 
	{
		if(methodType.equals(INTERNAL))
		{
			return codeBody.execute(args);
		}
		else if(methodType.equals(EXTERNAL))
		{
			//Find the method in the table and run its execute function.
			for(Object ext : RobotInterpreter.extMethodTable)
			{
				if(((ExtMethod)ext).id().equals(id))
				{
					return ((ExtMethod)ext).execute(args);
				}
			}
		}
		
		//We should never get here
		return null;
	}
}
