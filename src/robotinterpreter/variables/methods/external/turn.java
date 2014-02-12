package robotinterpreter.variables.methods.external;

import robotinterpreter.Interpreter;
import robotinterpreter.RobotListener;
import robotinterpreter.terminals.Terminals;

public class turn extends ExtMethod 
{
	private Interpreter interpreter;
	
	public turn(Interpreter in)
	{
		interpreter = in;
		id = "turn";
		type = Terminals.VOID;
		paramTypes = new String[1];
		paramTypes[0] = Terminals.STRING;
	}
	
	public Object execute(Object[] args) 
	{
		for(RobotListener l : interpreter.getRobotInterpreter().getRobotListeners())
		{
			if(args[0].equals("l"))
			{
				l.turnLeft();
			}
			else if(args[0].equals("r"))
			{
				l.turnRight();
			}
		}
		return null;
	}
}