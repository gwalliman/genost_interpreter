package robotinterpreter.variables.methods.external;

import robotinterpreter.Interpreter;
import robotinterpreter.RobotListener;
import robotinterpreter.terminals.Terminals;

public class turnToBearing extends ExtMethod 
{
	private Interpreter interpreter;
	
	public turnToBearing(Interpreter in)
	{
		interpreter = in;
		id = "turnToBearing";
		type = Terminals.VOID;
		paramTypes = new String[1];
		paramTypes[0] = Terminals.INT;
	}
	
	public Object execute(Object[] args) 
	{
		for(RobotListener l : interpreter.getRobotInterpreter().getRobotListeners())
		{
			l.turnToBearing((Integer) args[0]);
		}
		return null;
	}
}