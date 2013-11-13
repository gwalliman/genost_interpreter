package robotinterpreter;
import java.io.File;






import java.util.ArrayList;

import javax.swing.UIManager;

import robotinterpreter.terminals.Terminals;
import robotinterpreter.variables.BODY;
import robotinterpreter.variables.methods.METHODDEFINE;
import robotinterpreter.variables.vars.VARDECL;

public class RobotInterpreter 
{
	public static ArrayList<VARDECL> varTable;
	public static ArrayList<METHODDEFINE> methodTable;
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
	
	public static VARDECL findVar(String id)
	{
		for(VARDECL var : varTable)
		{
			if(var.id().equals(id))
				return var;
		}
		return null;
	}
	
	public static METHODDEFINE findMethod(String id)
	{
		for(METHODDEFINE method : methodTable)
		{
			if(method.id().equals(id))
				return method;
		}
		return null;
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
