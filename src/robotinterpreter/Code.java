package robotinterpreter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import robotinterpreter.terminals.Terminals;

public class Code 
{
	private String code = "";
	private int lineNum = 0;
	private List<String> codeLines;
	
	public static final String newline = "\n";
	
	public Code(File codeFile)
	{
		try 
	    {
			FileReader fr = new FileReader(codeFile);
			BufferedReader br = new BufferedReader(fr);
			String line = "";
			codeLines = new ArrayList<String>();
		
			while((line = br.readLine()) != null)
			{
				codeLines.add(line.trim());
				code += line + newline;
			}
			
			br.close();
			fr.close();
		} 
		catch (FileNotFoundException e1) 
		{
			e1.printStackTrace();
		} 
		catch (IOException e1) 
		{
			e1.printStackTrace();
		}
	}
	
	public String prevLine()
	{
		if(lineNum != 0)
		{
			lineNum--;
			if(codeLines.get(lineNum).trim().length() == 0) return prevLine();
			else return codeLines.get(lineNum); 
		}
		else return null;
	}
	
	public int currentLineNum()
	{
		return lineNum;
	}
	
	public String currentLine()
	{
		return codeLines.get(lineNum); 
	}
	
	public String nextLine()
	{
		if(lineNum != getMax())
		{
			lineNum++;
			if(codeLines.get(lineNum).trim().length() == 0) return nextLine();
			else return codeLines.get(lineNum); 
		}
		else return null;
	}
	
	public String lastLine()
	{
		return codeLines.get(codeLines.size()); 
	}
	
	public String getLine(int num)
	{
		return codeLines.get(num);
	}
	
	public String setCurrentLine(int num)
	{
		lineNum = num;
		return currentLine();
	}
	
	public int getMax()
	{
		return codeLines.size() - 1;
	}
	
	public String getCode()
	{
		return code;
	}
	
	public static boolean checkAlphaNumeric(String s)
	{
		return s.matches("[A-Za-z0-9]+");
	}
	
	public static String[] tokenize(String s)
	{
		ArrayList<String> tokens = new ArrayList<String>();
		String[] pass = s.split(" ");
		for(int x = 0; x < pass.length; x++)
		{
			String token = pass[x].trim();
			if(token.length() > 0)
			{
				if(checkAlphaNumeric(token))
				{
					tokens.add(token);
				}
				else
				{
					//If this token is the beginning of a string
					if(token.contains(Terminals.QUOTE) && token.substring(0, 1).equals(Terminals.QUOTE))
					{
						String r = implode(pass, " ", x, pass.length - 1);
						r = exciseString(r, tokens);
						
						String[] remainingTokens = tokenize(r);
						for(String t : remainingTokens)
						{
							tokens.add(t);
						}
						
						return tokens.toArray(new String[tokens.size()]);
					}
					
					for(String symbol : Terminals.symbolTerminals)
					{
						if(token.contains(symbol))
						{
							String subtokens[] = token.split(Pattern.quote(symbol), 2);
							tokens.addAll(Arrays.asList(tokenize(subtokens[0])));
							tokens.add(symbol);
							tokens.addAll(Arrays.asList(tokenize(subtokens[1])));
							break;
						}
					}
				}
			}
		}
		
		return tokens.toArray(new String[tokens.size()]);
	}
	
	//Find the string, add it to tokens, return the remainder
	private static String exciseString(String s, ArrayList<String> tokens)
	{
		//Get rid of the first quote (guaranteed to be there)
		s = s.substring(1, s.length());
		
		String[] h = s.split("\"", 2);
		if(h.length == 1)
		{
			RobotInterpreter.halt("STRING", -1, s, "Unenclosed string!");
		}
		
		tokens.add(Terminals.QUOTE + h[0] + Terminals.QUOTE);
		return h[1];
		
		/*//We remove the quote from the beginning of the current token.
		pass1[x] = pass1[x].substring(1, pass1[x].length());
		
		//We now loop through the current token, character by character, looking for the close quote.
		for(int y = 0; y < pass1[x].length(); y++)
		{
			//Pull the current character.
			String i = pass1[x].substring(y, y+1);
			
			//If it is a quote, it must be a close quote.
			if(i == Terminals.QUOTE)
			{
				//Add the close quote to the string, add the string to the tokens array.
				string += i;
				tokens.add(string);
				
				//If that character was the last character in the string, 
				if(y + 1 == pass1[x].length())
				{
					x++;
				}
				else
				{
					pass1[x] = pass1[x].substring(y + 1, pass1[x].length());
				}
				
				token = pass1[x];
				continue;
			}
			else
			{
				string += i;
				if(y + 1 == pass1[x].length())
				{
					x++;
					y = 0;
				}
			}
		}*/
	}
	
	public static String implode(String[] s, String sep)
	{
		StringBuilder sb = new StringBuilder();
		for(int x = 0; x < s.length; x++)
		{
			sb.append(s[x]);
			if(x < s.length - 1)
				sb.append(sep);
		}
		return sb.toString();
	}
	
	public static String implode(String[] s, String sep, int start)
	{
		StringBuilder sb = new StringBuilder();
		for(int x = start; x < s.length; x++)
		{
			sb.append(s[x]);
			if(x < s.length - 1)
				sb.append(sep);
		}
		return sb.toString();
	}
	
	public static String implode(String[] s, String sep, int start, int end)
	{
		StringBuilder sb = new StringBuilder();
		for(int x = start; x <= end; x++)
		{
			sb.append(s[x]);
			if(x < end)
				sb.append(sep);
		}
		return sb.toString();
	}
	
	public static void printTokens(String[] tokens)
	{
		for(int x = 0; x < tokens.length; x++)
			System.out.println(tokens[x]);
		System.out.println("===");
	}
}
