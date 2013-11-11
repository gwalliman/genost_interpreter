package robotinterpreter.terminals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Terminals 
{
	public static String OPENPAREN = "(";
	public static String CLOSEPAREN = ")";
	public static String OPENBRACE = "{";
	public static String CLOSEBRACE = "}";
	public static String OPENBRACKET = "[";
	public static String CLOSEBRACKET = "]";
	public static String QUOTE = "\"";
	public static String SEMICOLON = ";";
	public static String EQUALS = "=";
	public static String COMMA = ",";
	public static String EQ = "==";
	public static String NEQ = "!=";
	public static String LT = "<";
	public static String GT = ">";
	public static String LTE = "<=";
	public static String GTE = ">=";
	
	//NOTE: Order matters in this array! We must ensure that symbols which are contained in other symbols come LAST.
	private static String[] symbolTerminalsArray = { OPENPAREN, CLOSEPAREN, OPENBRACE, CLOSEBRACE, OPENBRACKET, CLOSEBRACKET, QUOTE, SEMICOLON, COMMA, EQ, NEQ,  LTE, GTE, EQUALS, LT, GT,};
	public static ArrayList<String> symbolTerminals = new ArrayList<String>(Arrays.asList(symbolTerminalsArray));
	
	private static String[] dataTypeArray = { "int", "string", "bool"};
	public static ArrayList<String> dataTypes = new ArrayList<String>(Arrays.asList(dataTypeArray));
	
	private static String[] logOpArray = {"and", "or"};
	public static ArrayList<String> logOps = new ArrayList<String>(Arrays.asList(logOpArray));
	
	private static String[] booleanValsArray = {"true", "false"};
	public static ArrayList<String> booleanVals = new ArrayList<String>(Arrays.asList(booleanValsArray));
		
	private static String[] stmtTypesArray = { "assign", "vardecl", "var", "methoddefine", "method", "if", "loopuntil", "loopfor", "waituntil", "waitfor" };
	public static ArrayList<String> stmtTypes = new ArrayList<String>(Arrays.asList(stmtTypesArray));
	
	private static String[] callTypesArray = { "var", "method" };
	public static ArrayList<String> callTypes = new ArrayList<String>(Arrays.asList(callTypesArray));
	
	public static ArrayList<String> reservedWords = new ArrayList<String>();
	
	public static void init()
	{
		reservedWords.addAll(symbolTerminals);
		reservedWords.addAll(dataTypes);
		reservedWords.addAll(logOps);
		reservedWords.addAll(booleanVals);
		reservedWords.addAll(stmtTypes);
	}
}