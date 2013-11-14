package robotinterpreter;
import java.io.File;
import java.lang.reflect.Constructor;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

import components.FileChooserDemo;
import robotinterpreter.terminals.Terminals;
import robotinterpreter.variables.BODY;
import robotinterpreter.variables.methods.METHODDEFINE;
import robotinterpreter.variables.methods.external.ExtMethod;
import robotinterpreter.variables.vars.VARDECL;

public class RobotInterpreter 
{
	//public static ArrayList<VARDECL> varTable;
	public static ArrayList<METHODDEFINE> methodTable;
	public static ArrayList<Object> extMethodTable;
	public static BODY b;
	
	public static void interpret(File codeFile)
	{
		//varTable = new ArrayList<VARDECL>();
		methodTable = new ArrayList<METHODDEFINE>();
		extMethodTable = new ArrayList<Object>();
		
		//Step 0: Load in external methods.
		for(String id : ExtMethod.extMethods)
		{
			try 
			{
				Class extC = Class.forName("robotinterpreter.variables.methods.external." + id);
				Constructor extCtor = extC.getConstructor();
				Object extInst = extCtor.newInstance();
				extMethodTable.add(extInst);
				methodTable.add(new METHODDEFINE(id));
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			} 
		}
		
		Code c = new Code(codeFile);
		//Step 1: Parse.
		//Go over each line of code and populate with information immediately available
		b = new BODY(null, c);
		b.print();
		RobotInterpreter.writeln("message", Code.newline + "=================");
		RobotInterpreter.writeln("message", "Code fully parsed!" + Code.newline + "=================");
		
		//Step 2: Link and Validate
		//Link up certain items: varcalls to variable array, method calls to actual method code, if/loop eventualities, etc.
		//Check type matching, etc.
		b.validate();
		RobotInterpreter.writeln("message", "=================");
		RobotInterpreter.writeln("message", "Code fully validated!" + Code.newline + "=================");

		//Step 3: Execute
	}
	
	public static VARDECL findVar(BODY b, String id)
	{
		for(VARDECL var : b.varTable)
		{
			if(var.id().equals(id))
				return var;
		}
		if(b.body() != null)
		{
			return findVar(b.body(), id);
		}
		else return null;
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
	
	public static void printAllVars()
	{
		printVars(b, "Global");
		for(METHODDEFINE m : methodTable)
		{
			if(m.methodType() != "external")
			{
				printVars(m.codeBody(), m.id());
			}
		}
	}
	
	public static void printVars(BODY b, String s)
	{
		RobotInterpreter.writeln("debug", "===================");
		RobotInterpreter.writeln("debug", "Printing Variable Table for " + s);
		RobotInterpreter.writeln("debug", "===================");

		for(VARDECL var : b.varTable)
		{
			var.print();
			RobotInterpreter.write("debug", Code.newline);
		}
		RobotInterpreter.writeln("debug", "===================");
	}
	
	public static void printMethods()
	{
		RobotInterpreter.writeln("debug", "===================");
		RobotInterpreter.writeln("debug", "Printing Method Table");
		RobotInterpreter.writeln("debug", "===================");
		
		for(METHODDEFINE method : methodTable)
		{
			method.print();
			RobotInterpreter.write("debug", Code.newline);
		}
		RobotInterpreter.writeln("debug", "===================");
	}
	
	private static boolean showMessage(String t)
	{
		switch(t)
		{
			//Display Parser messages
			case "parse":
				return true;
			//Display Validate messages
			case "validate":
				return false;
			//Display Debugging messages
			case "debug":
				return true;
			//Display all other messages
			default:
				return true;
		}
	}
	
	public static void write(String type, String s)
	{
		if(showMessage(type))
		{
			FileChooserDemo.writeLog(s);
			System.out.print(s);
		}
	}
	
	public static void writeln(String type, String s)
	{
		if(showMessage(type))
		{
			FileChooserDemo.writeLog(s + Code.newline);
			System.out.println(s);
		}
	}
	
	public static void halt(String var, int lineNum, String c, String error)
	{
		String fu = var.toUpperCase() + " ERROR Near Line " + lineNum + ": " + c + Code.newline + error;
		JOptionPane.showMessageDialog(null, fu, var + " ERROR", JOptionPane.ERROR_MESSAGE);
		System.exit(1);
	}
	
	public static void main(String args[])
	{
		Terminals.init();
		UIManager.put("swing.boldMetal", Boolean.FALSE);
        components.FileChooserDemo.createAndShowGUI();
	}
}
