package robotinterpreter.variables.methods.external;

import robotinterpreter.Interpreter;
import robotinterpreter.RobotListener;
import robotinterpreter.terminals.Terminals;

public class getSonars extends ExtMethod 
{
	private Interpreter interpreter;
	
	public getSonars(Interpreter in)
	{
		interpreter = in;
		id = "getSonars";
		type = Terminals.INT;
		paramTypes = new String[1];
		paramTypes[0] = Terminals.INT;
	}
	
	public Object execute(Object[] args) 
	{
		int data = 0;
		for(RobotListener l : interpreter.getRobotInterpreter().getRobotListeners())
		{
			data = l.getSonarData((Integer)args[0]);
		}
		return data;
	}
}