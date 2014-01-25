package robotinterpreter;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import robotinterpreter.terminals.Terminals;

/**
 * This class holds the textual code being interpreted,
 * and contains various functions which may be called to
 * get certain parts of said code.
 * 
 * On the whole, this class is used as a special kind of reader, allowing us to
 * get each line of the code and move back and fourth between them.
 * 
 * @author Garret Walliman (gwallima@asu.edu)
 *
 */
public class Code 
{
	//Used to hold the entire code file, after being read in.
	private String code = "";
	//Used to keep track of what line we are currently on.
	private int lineNum = 0;
	//Holds each physical line of code.
	private List<String> codeLines;
	
	public static final String newline = "\n";
	
	/**
	 * Receives the file object, reads it in
	 * and populates the code and codeLine variables.
	 * 
	 * @param codeFile	a File object containing code
	 * 
	 */
	public Code(String c)
	{
		try 
	    {
			BufferedReader br = new BufferedReader(new StringReader(c));
			String line = "";
			codeLines = new ArrayList<String>();
		
			//Note that we add in empty lines.
			//This is so that the length of codeLines will correctly
			//correspond with the length of the actual code document.
			while((line = br.readLine()) != null)
			{
				codeLines.add(line.trim());
				code += line + newline;
			}
			
			br.close();
			
			if(codeLines.size() == 0)
			{
				code = "";
				codeLines.add("");
			}
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
	
	/**
	 * Moves the internal pointer back one line.
	 * Skips over blank lines.
	 * If we are on the first line of code, returns null.
	 * 
	 * @return	the line of code now pointed to, or null if we were on first line
	 */
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
	
	/**
	 * @return	the current line number
	 */
	public int currentLineNum()
	{
		return lineNum;
	}
	
	/**
	 * @return	the text of the current line
	 */
	public String currentLine()
	{
		return codeLines.get(lineNum); 
	}
	
	/**
	 * Moves the internal pointer forward one line
	 * Skips over blank lines.
	 * If we are on the last line, returns null.
	 * 
	 * @return	the line of code now pointed to, or null if we were on last line
	 */
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
	
	/**
	 * Useful for ensuring that the main body is enclosed in braces
	 * 
	 * @return	the final line of code.
	 */
	public String lastLine()
	{
		return codeLines.get(codeLines.size() - 1); 
	}
	
	/**
	 * Returns the line of code specified by the provided line number
	 * 
	 * @param num	the line number we want returned
	 * @return	the line of code specified by the provided line number
	 */
	public String getLine(int num)
	{
		return codeLines.get(num);
	}
	
	/**
	 * Moves the internal pointer to the provided line number
	 * Checks to ensure that the provided line number is valid. If not valid, returns null
	 * 
	 * @param num	the line number to move the internal pointer to. Should be between 0 and the maximum line number
	 * @return	the line of code which we moved the pointer to, or null if the provided line number is invalid
	 */
	public String setCurrentLine(int num)
	{
		if(num >= 0 && num <= codeLines.size())
		{
			lineNum = num;
			return currentLine();
		}
		else return null;
	}
	
	/**
	 * @return	the maximum line number for the provided code.
	 */
	public int getMax()
	{
		return codeLines.size() - 1;
	}
	
	/**
	 * @return	a String containing the code from the codeFile
	 */
	public String getCode()
	{
		return code;
	}
	
	/**
	 * Used to check whether a string contains only numbers and letters
	 * 
	 * @param s	the String to be checked
	 * @return	true if the string is alphanumeric, false otherwise
	 */
	public static boolean checkAlphaNumeric(String s)
	{
		return s.matches("[A-Za-z0-9]+");
	}
	
	/**
	 * Takes a string and splits it up into tokens as follows.
	 * 		1. Each symbol (see the Terminals class for the list of symbols) will be considered one token
	 * 		2. Every set of alphanumeric characters, separated from other tokens by spaces, will be one token
	 * 			The separating spaces will not be enclosed in the token
	 * 		3. A String (set of alphanumeric characters or symbols enclosed by two quote symbols) will be one token.
	 * 			Note that the String token will include the enclosing quotes
	 * 		4. A negative integer will be one token.
	 * Example: assign x = method y(string "Tokenize Test");
	 * This will be split up into the following tokens:
	 * |assign|x|=|method|y|(|string|"Tokenize Test|)|;|
	 * 
	 * @param s	the string being tokenized
	 * @return an array of Strings, containing the tokens, in the order they appear in the provided string
	 */
	public static String[] tokenize(String s)
	{
		ArrayList<String> tokens = new ArrayList<String>();
		
		//We first split up the string by spaces.
		String[] pass = s.split(" ");
		
		//Go over each individual item separated by spaces in the original string.
		//This will get all tokens of type 2 without further processing,
		//but if we find a symbol, it might be type 1, 3 or 4, so more processing is required.
		for(int x = 0; x < pass.length; x++)
		{
			//Trim the string and ensure it's not empty space. If it is, move on to the next one.
			String token = pass[x].trim();
			if(token.length() > 0)
			{
				//If the string is entirely alphanumeric, add it to the array and move on to the next one.
				if(checkAlphaNumeric(token))
				{
					tokens.add(token);
				}
				//If the string has a symbol, it could be of type 1, 3 or 4
				else
				{
					//If this token has a quote as its first character, we will consider it, 
					//and all tokens following it (until we find another quote), part of a type 3 String token
					if(token.contains(Terminals.QUOTE) && token.substring(0, 1).equals(Terminals.QUOTE))
					{
						//Put the remaining tokens back together.
						String r = implode(pass, " ", x, pass.length - 1);
						r = exciseString(r, tokens);

						//We now have the String as a token in the tokens array,
						//and r is the code line that remains. We must tokenize r
						//and add it to the tokens array.
						String[] remainingTokens = tokenize(r);
						for(String t : remainingTokens)
						{
							tokens.add(t);
						}
						
						return tokens.toArray(new String[tokens.size()]);
					}
					//If this token has a dash as its first character, it might be a negative number.
					//We treat the dash and all numbers following as part of one token, and continue on after the first non-numeric character.
					else if(token.contains(Terminals.DASH) && token.substring(0, 1).equals(Terminals.DASH))
					{
						int start = 1;
						int end = 2;
						String negInt = Terminals.DASH;
						//Loop until we encounter a character that is not a number.
						while(end <= token.length() && token.substring(start, end).matches("\\d"))
						{
							negInt += token.substring(start, end);
							start++;
							end++;
						}
						//Add the negative integer
						tokens.add(negInt);
						
						//Get the rest of the string (if there is one)
						String remainder = token.substring(start, token.length());
						
						//Put the remaining tokens back together.
						String r = remainder + implode(pass, " ", x + 1, pass.length - 1);

						//We now have the negative integer as a token in the tokens array,
						//and r is the code line that remains. We must tokenize r
						//and add it to the tokens array.
						String[] remainingTokens = tokenize(r);
						for(String t : remainingTokens)
						{
							tokens.add(t);
						}
						
						return tokens.toArray(new String[tokens.size()]);
					}
					//If we don't meet the criteria above, then we assume type 1
					//We know at this point that we have at least one symbol somewhere in the string.
					else
					{
						//Go through each symbol to find which one we have
						for(String symbol : Terminals.symbolTerminals)
						{
							//Once we have found a symbol in the string:
							if(token.contains(symbol))
							{
								//Split the string around that one symbol (resulting in two halves)
								String subtokens[] = token.split(Pattern.quote(symbol), 2);
								//Tokenize the left half ands add it to the tokens array
								tokens.addAll(Arrays.asList(tokenize(subtokens[0])));
								//Add the symbol to the array
								tokens.add(symbol);
								//Tokenize the right half and add it to the array
								tokens.addAll(Arrays.asList(tokenize(subtokens[1])));
								break;
							}
						}
					}
				}
			}
		}
		
		return tokens.toArray(new String[tokens.size()]);
	}
	
	/**
	 * This function is used to "excise" a quoted string from a line of code.
	 * When we get the code line (param s) we assume that the first character is the opening quote of the string.
	 * We attempt to find the end of the string and add the entire string as one large token to the provided tokens array.
	 * We then return what is left of the provided line of code after removing the string token
	 * 
	 * @param s	a string of code where the first character is the opening quote to a string
	 * @param tokens an ArrayList for Strings
	 * @return	the remaining text in the provided line of code after excising the string
	 */
	private static String exciseString(String s, ArrayList<String> tokens)
	{
		//Get rid of the first quote (assumed to be there)
		s = s.substring(1, s.length());
		
		//Split the provided line of code by the first found quote.l
		String[] h = s.split("\"", 2);
		
		//If there is no found quote, then the string is not enclosed.
		if(h.length == 1)
		{
			Interpreter.error("STRING", -1, s, "Unenclosed string!");
		}
		
		//We add the left half of the split (the string) to the tokens array.
		tokens.add(Terminals.QUOTE + h[0] + Terminals.QUOTE);
		
		//We return the right half of the split (the remainder)
		return h[1];
	}
	
	/**
	 * Performs the same function as PHP's implode.
	 * Takes each element of the provided String array s and attaches them together in order
	 * as one large string, separated by the provided separator, sep
	 * 
	 * @param s an array of strings to be attached together
	 * @param sep a string which will be placed between each of the strings in s
	 * @return the resultant string after implosion
	 */
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
	
	/**
	 * Takes each element of the provided String array s and attaches them together in order
	 * as one large string, separated by the provided separator, sep
	 * 
	 * This variant allows us to choose which index in the string array we will begin at. All elements
	 * before this will be ignored. Every element after this will be incorporated into the string.
	 * 
	 * @param s an array of strings to be attached together
	 * @param sep a string which will be placed between each of the strings in s
	 * @param start the index at which we will begin imploding.
	 * @return the resultant string after implosion
	 */
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
	
	/**
	 * Takes each element of the provided String array s and attaches them together in order
	 * as one large string, separated by the provided separator, sep
	 * 
	 * This variant allows us to choose the indices which will start and end the string.
	 * All indices before start will be ignored, as will all indices after end.
	 * 
	 * @param s an array of strings to be attached together
	 * @param sep a string which will be placed between each of the strings in s
	 * @param start the index at which we will begin imploding.
	 * @param end the last index which will be imploded
	 * @return the resultant string after implosion
	 */
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
	
	/**
	 * A simple print function which will print out the tokens in the provided tokens array.
	 * 
	 * @param tokens a tokens array to print
	 */
	public static void printTokens(String[] tokens)
	{
		for(int x = 0; x < tokens.length; x++)
			Interpreter.writeln("debug", tokens[x]);
		Interpreter.writeln("debug", "===");
	}
}
