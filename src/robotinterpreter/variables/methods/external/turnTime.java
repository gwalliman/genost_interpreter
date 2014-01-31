package robotinterpreter.variables.methods.external;

import java.util.Timer;
import java.util.TimerTask;

import robotinterpreter.RobotListener;
import robotinterpreter.RobotInterpreter;
import robotinterpreter.terminals.Terminals;

public class turnTime extends ExtMethod 
{
	boolean done = false;
	
	public turnTime()
	{
		id = "turnTime";
		type = Terminals.VOID;
		paramTypes = new String[2];
		paramTypes[0] = Terminals.STRING;
		paramTypes[1] = Terminals.INT;
	}
	
	public Object execute(Object[] args) 
	{
		Timer timer = new Timer();
		
		for(RobotListener l : RobotInterpreter.getRobotListeners())
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
		
		for(RobotListener l : RobotInterpreter.getRobotListeners())
		{
			l.stop();
		}
		
		return null;
	}
}