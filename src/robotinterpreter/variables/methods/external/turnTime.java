package robotinterpreter.variables.methods.external;

import java.util.Timer;
import java.util.TimerTask;

import robotinterpreter.Interpreter;
import robotinterpreter.RobotListener;
import robotinterpreter.terminals.Terminals;

public class turnTime extends ExtMethod 
{
	private Interpreter interpreter;
	
	boolean done = false;
	
	public turnTime(Interpreter in)
	{
		interpreter = in;
		id = "turnTime";
		type = Terminals.VOID;
		paramTypes = new String[2];
		paramTypes[0] = Terminals.STRING;
		paramTypes[1] = Terminals.INT;
	}
	
	public Object execute(Object[] args) 
	{
		Timer timer = new Timer();
		
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
		
		timer.schedule(new TimerTask() {
			  public void run() {
			    done = true;
			  }
			}, (Integer)args[1]*1000);
		
		while(!done) { }
		
		for(RobotListener l : interpreter.getRobotInterpreter().getRobotListeners())
		{
			l.stop();
		}
		
		return null;
	}
}