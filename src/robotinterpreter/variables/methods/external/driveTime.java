package robotinterpreter.variables.methods.external;

import java.util.Timer;
import java.util.TimerTask;

import robotinterpreter.RobotListener;
import robotinterpreter.RobotInterpreter;
import robotinterpreter.terminals.Terminals;

public class driveTime extends ExtMethod 
{
	boolean done = false;
	
	public driveTime()
	{
		id = "driveTime";
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
			if(args[0].equals("f"))
			{
				l.driveForward();
			}
			else if(args[0].equals("b"))
			{
				l.driveBackwards();
			}
		}
		
		timer.schedule(new TimerTask() {
			  public void run() {
			    done = true;
			  }
			}, (int)args[1]*1000);
		
		while(!done) { }
		
		for(RobotListener l : RobotInterpreter.getRobotListeners())
		{
			l.stop();
		}
		
		return null;
	}
}