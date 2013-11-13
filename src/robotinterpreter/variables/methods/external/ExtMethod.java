package robotinterpreter.variables.methods.external;

import java.util.ArrayList;

import robotinterpreter.Code;
import robotinterpreter.RobotInterpreter;
import robotinterpreter.terminals.Terminals;
import robotinterpreter.variables.ID;
import robotinterpreter.variables.Variable;

public abstract class ExtMethod
{
	private String id;
	private String type;
	private String[] paramTypes;
	
	public abstract void execute(Object[] args);
}
