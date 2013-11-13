package robotinterpreter;
import java.io.File;






import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import javax.swing.UIManager;

import robotinterpreter.terminals.Terminals;
import robotinterpreter.variables.BODY;
import robotinterpreter.variables.methods.METHODDEFINE;
import robotinterpreter.variables.methods.external.ExtMethod;
import robotinterpreter.variables.vars.VARDECL;

public class RobotInterpreter 
{
	public static ArrayList<VARDECL> varTable;
	public static ArrayList<METHODDEFINE> methodTable;
	public static ArrayList<Object> extMethodTable;
	public static void interpret(File codeFile)
	{
		varTable = new ArrayList<VARDECL>();
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
			catch (ClassNotFoundException e) 
			{
				e.printStackTrace();
			} 
			catch (NoSuchMethodException e) 
			{
				e.printStackTrace();
			} catch (SecurityException e) 
			{
				e.printStackTrace();
			} 
			catch (InstantiationException e) 
			{
				e.printStackTrace();
			} 
			catch (IllegalAccessException e) 
			{
				e.printStackTrace();
			} 
			catch (IllegalArgumentException e) 
			{
				e.printStackTrace();
			} 
			catch (InvocationTargetException e) 
			{
				e.printStackTrace();
			}
		}
		
		Code c = new Code(codeFile);
		//Step 1: Parse.
		//Go over each line of code and populate with information immediately available
		BODY b = new BODY(c);
		b.print();
		System.out.println(Code.newline + "=================");
		System.out.println("Code fully parsed!" + Code.newline + "=================");
		
		//Step 2: Link and Validate
		//Link up certain items: varcalls to variable array, method calls to actual method code, if/loop eventualities, etc.
		//Check type matching, etc.
		b.validate();
		System.out.println("=================");
		System.out.println("Code fully validated!" + Code.newline + "=================");

		//Step 3: Execute
		
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
	
	public static void printVars()
	{
		System.out.println("===================");
		System.out.println("Printing Variable Table");
		System.out.println("===================");

		for(VARDECL var : varTable)
		{
			var.print();
			System.out.print(Code.newline);
		}
		System.out.println("===================");
	}
	
	public static void printMethods()
	{
		System.out.println("===================");
		System.out.println("Printing Method Table");
		System.out.println("===================");
		
		for(METHODDEFINE method : methodTable)
		{
			method.print();
			System.out.print(Code.newline);
		}
		System.out.println("===================");
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
