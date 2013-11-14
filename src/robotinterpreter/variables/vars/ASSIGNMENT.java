package robotinterpreter.variables.vars;

import robotinterpreter.Code;
import robotinterpreter.RobotInterpreter;
import robotinterpreter.terminals.Terminals;
import robotinterpreter.variables.BODY;
import robotinterpreter.variables.CALL;
import robotinterpreter.variables.ID;
import robotinterpreter.variables.Variable;

public class ASSIGNMENT extends Variable 
{
	private String id;
	private CALL call;
	private VARDECL lhs;
	
	public ASSIGNMENT(BODY b, Code c)
	{
		body = b;
		code = c.currentLine();
		lineNum = c.currentLineNum();
		
		String[] tokens = Code.tokenize(code);
		
		id = ID.validate(tokens[1], c);
		
		if(tokens[2] != Terminals.EQUALS)
		{
			RobotInterpreter.halt("ASSIGNMENT", lineNum, code, "Assignment statement requires an =");
		}
		
		if(tokens[tokens.length - 1] != Terminals.SEMICOLON)
		{
			RobotInterpreter.halt("ASSIGNMENT", lineNum, code, "Missing semicolon");
		}
		
		String rhs = code.split(Terminals.EQUALS)[1];
		call = new CALL(body, c, rhs.substring(0, rhs.length() - 1));
	}

	
	public void print() 
	{
		RobotInterpreter.write("parse", "assign " + id + " = ");
		call.print();
	}

	//IN THIS ORDER
	//Validate call
	//Ensure that lhs exists
	//Ensure that lhs and rhs are of same type
	public void validate() 
	{
		RobotInterpreter.writeln("validate", "Validating ASSIGNMENT");
		call.validate();
		lhs = RobotInterpreter.findVar(body, id);
		if(lhs == null)
		{
			RobotInterpreter.halt("ASSIGNMENT", lineNum, code, "Variable " + id + " is not defined.");
		}
		
		String lhsType = lhs.type();
		String rhsType = call.getType();
		
		if(!lhsType.equals(rhsType))
		{
			RobotInterpreter.halt("ASSIGNMENT", lineNum, code, "LHS and RHS of an assignment must be of same type, but LHS is " + lhsType + " and RHS is " + rhsType);
		}
	}


	@Override
	public Object execute(Object args[]) 
	{
		Object val = call.execute(null);
		
		//Note that we don't have to worry about types,
		//This was taken care of in Validation
		RobotInterpreter.setVar(lhs.id(), val);
		
		return null;
	}
}
