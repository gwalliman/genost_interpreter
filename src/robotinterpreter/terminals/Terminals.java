package robotinterpreter.terminals;

import java.util.ArrayList;
import java.util.Arrays;

public class Terminals 
{
	//Symbols
	public static final String OPENPAREN = "(";
	public static final String CLOSEPAREN = ")";
	public static final String OPENBRACE = "{";
	public static final String CLOSEBRACE = "}";
	public static final String OPENBRACKET = "[";
	public static final String CLOSEBRACKET = "]";
	public static final String QUOTE = "\"";
	public static final String SEMICOLON = ";";
	public static final String EQUALS = "=";
	public static final String COMMA = ",";
	public static final String EQ = "==";
	public static final String NEQ = "!=";
	public static final String LT = "<";
	public static final String GT = ">";
	public static final String LTE = "<=";
	public static final String GTE = ">=";
	
	//Symbol Arrays
	private static final String[] symbolTerminalsArray = { OPENPAREN, CLOSEPAREN, OPENBRACE, CLOSEBRACE, OPENBRACKET, CLOSEBRACKET, QUOTE, SEMICOLON, COMMA, EQ, NEQ,  LTE, GTE, EQUALS, LT, GT,};
	public static final ArrayList<String> symbolTerminals = new ArrayList<String>(Arrays.asList(symbolTerminalsArray));
	//NOTE: Order matters in this array! We must ensure that symbols which are contained in other symbols come LAST.
	public static final String[] comparatorArray = { EQ, NEQ, LT, GT, LTE, GTE };
	public static final ArrayList<String> comparators = new ArrayList<String>(Arrays.asList(comparatorArray));
		
	//Words
	public static final String VOID = "void";
	public static final String INT = "int";
	public static final String STRING = "string";
	public static final String BOOL = "bool";
	public static final String TRUE = "true";
	public static final String FALSE = "false";
	public static final String AND = "and";
	public static final String OR = "or";
	public static final String VAR = "var";
	public static final String METHOD = "method";
	public static final String ASSIGN = "assign";
	public static final String VARDECL = "vardecl";
	public static final String METHODDEFINE = "methoddefine";
	public static final String RETURN = "return";
	public static final String IF = "if";
	public static final String ELSEIF = "elseif";
	public static final String ELSE = "else";
	public static final String LOOPUNTIL = "loopuntil";
	public static final String LOOPFOR = "loopfor";
	public static final String WAITUNTIL = "waituntil";
	public static final String WAITFOR = "waitfor";
	
	//Word Arrays
	private static final String[] dataTypeArray = { VOID, INT, STRING, BOOL};
	public static final ArrayList<String> dataTypes = new ArrayList<String>(Arrays.asList(dataTypeArray));
	
	private static final String[] logOpArray = {AND, OR};
	public static final ArrayList<String> logOps = new ArrayList<String>(Arrays.asList(logOpArray));
	
	private static final String[] booleanValsArray = {TRUE, FALSE};
	public static final ArrayList<String> booleanVals = new ArrayList<String>(Arrays.asList(booleanValsArray));
		
	private static final String[] stmtTypesArray = { ASSIGN, VARDECL, METHODDEFINE, METHOD, RETURN, IF, ELSEIF, ELSE, LOOPUNTIL, LOOPFOR, WAITUNTIL, WAITFOR };
	public static final ArrayList<String> stmtTypes = new ArrayList<String>(Arrays.asList(stmtTypesArray));
	
	private static final String[] callTypesArray = { VAR, METHOD };
	public static final ArrayList<String> callTypes = new ArrayList<String>(Arrays.asList(callTypesArray));
	
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