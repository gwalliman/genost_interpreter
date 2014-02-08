package robotinterpreter.variables.methods.external;

import robotinterpreter.Interpreter;
import robotinterpreter.RobotListener;
import robotinterpreter.terminals.Terminals;

public class getBearing extends ExtMethod 
{
	private Interpreter interpreter;
	
	public getBearing(Interpreter in)
	{
		interpreter = in;
		id = "getSonars";
		type = Terminals.INT;
		paramTypes = null;
	}
	
	public Object execute(Object[] args) 
	{
		int data = 0;
		for(RobotListener l : interpreter.getRobotInterpreter().getRobotListeners())
		{
			data = l.getBearing();
		}
		return data;
	}
}