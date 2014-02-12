package robotinterpreter.variables.methods.external;

import robotinterpreter.Interpreter;
import robotinterpreter.RobotListener;
import robotinterpreter.terminals.Terminals;

public class stop extends ExtMethod 
{
	private Interpreter interpreter;
	
	public stop(Interpreter in)
	{
		interpreter = in;
		id = "stop";
		type = Terminals.VOID;
		paramTypes = null;
	}
	
	public Object execute(Object[] args) 
	{
		for(RobotListener l : interpreter.getRobotInterpreter().getRobotListeners())
		{
			l.stop();
		}
		return null;
	}
}