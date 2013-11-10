package robotinterpreter;
public class SymbolTerminal extends Terminal
{
	private String name;
	private String symbol;
	
	public SymbolTerminal() 
	{ 
		name = null;
		symbol = null;
	}
	
	public SymbolTerminal(String n, String s)
	{
		name = n;
		symbol = s;
	}
	
	public String name() 
	{
		return name;
	}
	
	public Boolean matches(String token) 
	{
		if(token.equals(symbol)) return true;
		else return false;
	}

	public void print() {
		System.out.print(symbol);
	}
}
