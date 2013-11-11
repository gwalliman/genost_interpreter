package robotinterpreter;
import java.io.File;






import javax.swing.UIManager;

import robotinterpreter.terminals.Terminals;
import robotinterpreter.variables.BODY;

public class RobotInterpreter 
{
	public static void interpret(File codeFile)
	{
		Code c = new Code(codeFile);
		BODY b = new BODY(c);
		b.print();
		//Step 1: Parse.
		//Go over each line of code and populate with information immediately available
	
		//Step 2: Link
		//Link up certain items: varcalls to variable array, method calls to actual method code, if/loop eventualities, etc.
	
		//Step 3: Validate
		//Check type matching, etc.
		
		//Step 4: Execute
		
		System.exit(0);
	}
	
	public static void halt(String var, int lineNum, String c, String error)
	{
		System.out.println(var.toUpperCase() + " ERROR Near Line " + lineNum + ": " + c);
		System.out.println(error);
		System.exit(1);
	}
	
	public static void main(String args[])
	{
		Terminals.init();
		UIManager.put("swing.boldMetal", Boolean.FALSE);
        components.FileChooserDemo.createAndShowGUI();
	}
}
