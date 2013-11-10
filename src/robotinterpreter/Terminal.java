package robotinterpreter;
public abstract class Terminal 
{
	public abstract String name();
	public abstract Boolean matches(String token);
	public abstract void print();
}
