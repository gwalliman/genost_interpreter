Robot Interpreter
Author: Garret Walliman (gwallima@asu.edu)
Developed for Master's Thesis at Arizona State University, 2013-2014

======================================================================

0. Introduction
	0.1. Language Metrics
1. Code Overview
	1.1. Body
	1.2. Statements
	1.3. Literals
	1.4. Variables
		1.4.1. Variable Declarations
		1.4.2. Variable Calls
	1.5. Methods
		1.5.1. Internal Methods
		1.5.2. External Methods
		1.5.3. Calling Methods
	1.6. Assignment
	1.7. Conditions
	1.8. If Statements
		1.8.1. If
		1.8.2. Elseif
		1.8.3. Else
	1.9. Loop
		1.9.1. Loop For
		1.9.2. Loop Until
	1.10. Wait
		1.10.1. Wait For
		1.10.2. Wait Until
2. Future Development

======================================================================

0. Introduction
-----------------

This program is an interpreter: it takes properly written code, parses it, validates it, and executes it.
The interpreter was written as part of Garret Walliman's master's thesis at ASU. The interpreter is intended to run on a robot, and the code the interpreter receives is assumed to be commands to control the robot in some capacity.
The language that this interpreter uses is a custom language written specifically for this project. The actual code text is planned to be automatically generated from a graphical programming IDE.
Because we are automatically generating the language, it was developed to be as unambiguous as possible. This mean that it is, in many ways, stricter and more verbose than a standard programming language.

We intend that the programming IDE which generates the code file will always produce syntactically correct code. Despite this, we perform extensive error checking in parsing and validation and provide feedback in case an error is found.

0.1. Language Metrics:
-----------------
Language Paradigm:	Procedural
Type Expression: 	Explicit
Type Checking: 		Static
Language Types:		integer (int), string (string), boolean (bool)	//See section 1.3 for definitions of these types
Variable Scoping: 	Semi-dynamic: child bodies declared within a parent body may access the parent body's variables. However, two bodies declared separately (i.e. on the same level) cannot access each others variables if one calls the other during runtime.

======================================================================

1. Code Overview
-----------------

The code this language interprets is intended to be automatically generated. 
The following section is a reference both for those that develop the automatic code generation, and for anyone who, for whatever reason, wants to actually hand-write code for the interpreter.


1.1. Body
-----------------
All written code must exist within the "main body". This includes all statements, variable declarations, and method declarations.
There may be sub-bodies or child bodies within the main body. Methods, If Statements and Loops have these child bodies.
Every body must be enclosed by braces ({, }). Both the opening and close braces must be on their own dedicated code lines.

This means that the first line of every program must be the opening brace ({) of the main body, and the last line of every program must be the close brace (})

Bodies have variable scopes. Variables declared within a body belong to that body's scope and are accessible by any child bodies within that body. A parent body cannot, however, access variables declared within a child body.
Effectively, this gives us a semi-dynamic scope. A child body within a parent body has access to both its own local variables and the parent body's variables. This continues for as deep as the child bodies are defined.
However, a body cannot access the variables of a body that is not its ancestor, even if that non-ancestor body has called it (so the scoping is not truly dynamic)

Note that methods can be declared anywhere, including inside other method bodies. However, all methods are accessible from everywhere in the program.


1.2. Statements
-----------------
Each statement must have its own dedicated line (or lines, if the statement has a body) with at least one newline between each statement. 
For statements without bodies (Assignment, Method calls, Variable declaration, Waits), these statements must have exactly one line to themselves, and at least one linebreak between subsequent statements.
NOTE: Statements without bodies must always end with a semicolon!

For statements with bodies (Method declarations, Ifs, Loops) the header part of these statements must also have exactly one line to themselves. The line immediately after must be the body's open brace. Refer to the Body section above on how the body must be formatted.

IMPORTANT: There may be empty lines between statements, but two or more statements CANNOT occupy the same line!


1.3. Literals
-----------------
There are three datatypes in the program: integer (int), string (string), and boolean (bool)
These types are the same as their common definition.
- Integer is a whole number, negative, positive or zero. We do not limit the size of the number, although your computer's compiler will.
- String is any length of characters (which includes letters, numbers and symbols) between two quotation marks ("). 
	NOTE: The quotation symbol cannot appear anywhere in a string! It will be treated as the end of the string
- Boolean consists simply of the values "true" or "false" (without the quotations)

We use literals in the program as either the right hand side (rhs) of an Assignment statement, within a Condition, or as a parameter in a Method call.
In either case, the literal should appear as follows: 
	The datatype (int, string, bool) , followed one or more spaces, and finally the data value.

Examples:
	int 0
	string "ASDF"
	bool true


1.4. Variables
-----------------
A Variable (var) is a single-type multi-use variable which may be read and written within its scope.
There are two items related to variables in our language: a Variable Declaration (which is a Statement) and a variable call.


1.4.1. Variable Declarations
-----------------
The Variable Declaration associates a name with a type and makes it available for assignment within its scope.
Upon leaving the scope, the Variable loses its value, but it is not "undeclared" and will be available for assignment again once we re-enter the scope.

A Variable can be declared anywhere in the program and then used anywhere else. Because the language is interpreted, we create the variable tables before we actually execute the program. 
This means that a valid code could have all of its variable declarations at the end! However, by convention, we usually declare all variables at the beginning in our automatically generated code.

The Variable Declaration must appear as follows: 
	The word "vardecl", followed by one or more spaces, the datatype (int, string, bool), followed by one or more spaces, and finally the variable's id, followed by a semicolon.
	
The variable ID must be alphanumeric. However, we do not require any other restrictions (for example, it can be capitalized in any way, and may begin with a number)

Examples of a Variable Declaration
	vardecl int x;
	vardecl string y;
	vardecl bool z;
	
Variable Declarations are Statements. This means that they must always appear on their own line, followed by a semicolon, followed by at least one newline.
	
	
1.4.2. Variable Calls
-----------------
A Variable Call is used to retrieve the value of a variable. Like Literals, they are used either on the RHS of an assignment, within a Condition, or as a parameter in a Method call.
When calling a variable, a valid ID must be used. This means that the variable must be declared somewhere within the scope of the call, though not necessarily on a line prior to the call itself.

The variable call is formatted as follows:
	The word "var", followed by one or more spaces, and finally the variable id.
	
Examples:
	var x
	var y


1.5. Methods
-----------------
A Method consists of three items:
1. A return type (void, int, bool, string)
2. A list of parameters
3. A body of code

Methods, when called, are sent certain parameters upon call, execute a body of code, and return some value of the proper return type.
For a method to be used in the code, it must be defined somewhere.

Methods have variable scopes: each method has access to its own local variables and the variables of the parent scopes (i.e. ancestor bodies).
Methods always have access to the main body's variables, since all methods are declared within the main body. Theoretically, we could declare a method within another method, so that the child method could access the parent's variables.
However, by convention, all methods will be defined separately in the main code body at the beginning of the automatically generated code, and so effectively, each method will only have access to its own local variables and those in the scope of the main code body.

We allow two kinds of methods in this program: internal methods, and external methods. 


1.5.1. Internal Methods
-----------------
Internal methods are methods whose id, parameters, return type and code bodies are defined in the provided code.
These methods are defined at runtime. Because internal methods have inline code bodies, they can only utilize functionality already programmed into the language.

Internal methods are defined using the "methoddefine" statement. This statement works as follows:
The first line of the methoddefine statement provides the method return type, method id, and a list of parameters that the method will execute.
The list of parameters is a set of pairs of two items: the parameter datatype, and the parameter id. Every parameter is followed by a comma, except for the last one.

After the first line and a newline, the code body is defined. The line immediately after the methoddefine line must be the opening brace to the method code.
Method code will continue until a closebrace is provided, again, on its own line.

The format of a methoddefine statement is:
	The word "methoddefine", followed by one or more spaces, an open parentheses, a list of parameter variables, a close parentheses, a newline, and the code body
The format of a parameter list is:
	The datatype of the first parameter, followed by one or more spaces, the id of the first parameter. If there is a second parameter, the id is followed by a comma. The next parameter is then listed.
	
Examples:
	methoddefine int x()
	{
		//Method code
	}
	
	methoddefine string y(int z, bool a)
	{
		//Method code
	}
	
If the internal method has a non-void return type, a RETURN stmt must appear within the method's codebody.
Multiple return types may appear in the code body. However, the first one that we execute will be the last statement we execute within the code body.

Note that every method MUST have a return statement on the main level of its code body. If the only return statement(s) appear in other bodies (loops, ifs) this will throw an error.
We will handle this convention by forcing a return statement to appear at the very end of every method in our automatically generated code.

This RETURN statement is of the following format:
	The word "return", followed by one or more spaces, followed by a variable call, literal, or method call of the same type as the internal method's return type, followed by a semicolon

Examples:
	return int 0;
	return var x;
	return method a(int 7);


1.5.2. External Methods
-----------------
External methods are methods whose id, parameters, return type and execution code are defined in the Java package.
Effectively, these external methods provide all of the actual "stuff" the language can do. At time of writing, we have provided external method code for:
- Printing values to the screen
- Basic arithmetic: addition, subtraction, multiplication, division

Future external methods will define robot drive / sensor code.

To create an External Method:
1. Create a new class in the robotinterpreter.variables.methods.external package. This class must extend ExtMethod
2. The class should be named what we want the method id to be.
3. The class constructor should instantiate the following variables (to literals)
	- The method name (Must be alphanumeric. Should be the same as the class name, this will be the id by which the method will be called)
	- The return type (Must be int, string or bool)
	- An array of strings should be instantiated, with length equal to the number of arguments this function will take.
	- Each entry in the above string array should be initialized to the datatype of the corresponding argument.
4. The class must implement method "execute". This method will contain the functionality of the method.
	- Method arguments will be passed in through the args array. They will be passed in in the order defined in the paramTypes array.
	- If the external method has a non-void return type, it must return a value.
5. Finally, the method's class name / id must be added to the extMethodsArray in the ExtMethods class. If this is not done, the method will not be detected!


1.5.3. Calling Methods
-----------------
When a method is called, its parameters are evaluated and sent to the method's code body, which is executed. If the method is non-void, it returns a value.
A method may be called as either a statement, as a data element in a Condition, or as a parameter to another method call.

When a method is called, its local variables (including its parameters) are instantiated and given default values. The values of these variables will persist until we exit the method.
Note that this means that, if we call another method (method #2) from inside the method (method #1), even though #2 cannot access #1's variables, #1's variables will keep their values while #2 is executed.

A method call is formatted in the following way:
	The word "method", followed by one or more spaces, followed by the method id, followed by an open parentheses, followed by a full parameter call list, followed by a close paren. If this method is being called as a statement, it must be followed by a semicolon.
The format of a parameter list is:
	A variable call, method call or literal of the proper type. If there is a second parameter, the id is followed by a comma. The next parameter is then listed.
	
Examples:
	method a();
	method b(int 0, var y);
	method c(method f(int 0));
	

1.6. Assignment
-----------------
In an Assignment statement, we assign the value of either a literal, a method call, or another variable to a specified variable.
The right hand side (rhs) of the Assignment statement must be of the same datatype as the left hand side (lhs) variable. The only valid datatypes that may be assigned are int, string and bool.

An Assignment statement is the ONLY way in which variables may take on non-default values.

The format of an Assignment statement is the following:
	The word "assign", followed by one or more spaces, followed by the id of a valid variable, followed by one or more spaces, followed by an equals sign, followed by one or more spaces, followed by a variable call, method call, or literal, followed by a semicolon.
	
Examples:
	assign x = int 0;
	assign y = var x;
	assign z = method a(var y);
	

1.7. Conditions
-----------------
A Condition is a logical statement containing at least one comparison between two pieces of data (variable calls, method calls or literals) and possibly one or more logical operations (AND, OR).
Conditions are used within If statements and Loops. See those sections for more details on the specifics of how they are used.
A Condition always returns a boolean value - true or false - indicating what it evaluated to!

The comparison in a Condition requires two calls of the same type and has one comparator. The comparator may be:
- Logical equality (==)
- Logical inequality (!=)
- Greater Than (>)
- Less Than (<)
- Greater Than or Equal To (>=)
- Less Than or Equal To (<=)
NOTE: All of these logical operators may be used if we are comparing int datatypes; but only the first two (==, !=) may be used if we are comparing strings or bools.

Every comparison must be wrapped in brackets ([ ]).
The format of a comparison is as follows:
	An open bracket, followed by a variable call / method call / literal, followed by one or more spaces, followed by a comparator, followed by a variable call / method call / literal, followed by a close bracket.

Examples:
	[var x > int y]
	[method x() == method y(int 0)]
	
A Condition may also have a logical operation. The logical operation requires two comparisons and a logical operator, which may be either "and" or "or".

The most basic logical operation is of the form of "comparison #1 and comparison #2" / "comparison #1 or comparision #2". 

The format of the basic logical operation is the following:
	A comparison, followed by one or more spaces, followed by the word "and" or "or", followed by one or more spaces, followed by another comparison.
Examples:
	[var x > int y] and [method x() == method y(int 0)]
	[int 5 != method y()] or [string "ASDF" == var s]

There are two ways which we may extend this basic logical operation
1. Concatination: we may add another logical comparison to the end. 
	Example: "comparison #1 and comparison #2 or comparison #3"
2. Nesting: The comparison on one side of the logical operator may be replaced with a full logical statement. Whenever we nest, we wrap the nested logical statement in brackets ([ ])
	Example: "[comparison #1 and comparison #2] or comparison #3"

Examples:
	[var x > int y] and [method x() == method y(int 0)] or [string "ASDF" == var s]				//Concationation Example
	[[method x() == method y(int 0)] or [string "ASDF" == var s]] or [string "ASDF" == var s]	//Nesting Example


1.8. If Statements
-----------------
An If statement conditionally executes a body / bodies of code depending on whether an included Condition evaluates as true or false.
Every If statement must include a Condition to evaluate, and a body to execute if that Condition evaluates to true. It may optionally include Elseifs or Elses to modify how the If operates.

If/Elseif/Else bodies, like all bodies, have their own scope and may declare variables within them accessible only to these bodies (or any child bodies within them)

1.8.1. If
-----------------
The If is the basic building block, and is required with every If statement.

The If format is as follows:
	The word "if", followed by one or more spaces, followed by an open parentheses, followed by a Condition, followed by a close parentheses, followed by a newline, followed by a body.	
	NOTE: DO NOT FORGET THE SPACE BETWEEN IF AND THE OPENPAREN
Examples:
	if ([var x > int y])
	{
		//Code
	}
	
	if ([int 5 != method y()] or [string "ASDF" == var s])
	{
		//Code
	}
	

1.8.2. Elseif
-----------------
The optional Elseif provides another Condition to evaluate and another body to possibly execute if the If Condition fails.
An Elseif appears on the immediate next line after an If statement's body closing brace.
There may be multiple Elseifs associated with an If. They are processed in the order they appear. Only one Elseif will have its body executed; once an Elseif Condition evaluates to true, its body will execute, and the rest of the If statement will be ignored.

An Elseif MUST follow an If statement (or another Elseif). It cannot appear under any other circumstances!

The Elseif format is as follows:
	The word "elseif", followed by one or more spaces, followed by an open parentheses, followed by a Condition, followed by a close parentheses, followed by a newline, followed by a body.	
	NOTE: DO NOT FORGET THE SPACE BETWEEN ELSEIF AND THE OPENPAREN
Examples:
	if ([var x > int y])
	{
		//Code
	}
	elseif ([int 5 != method y()] or [string "ASDF" == var s])
	{
		//Code
	}

1.8.3. Else
-----------------
The optional Else appears after an If or after an Elseif. If an Else appears, no more Elseifs may appear; the Else is always the last item in an If statement.
Else will execute only if none of the Conditions in the If / Elseif(s) evaluated to true. As such, it has no condition itself.

The Else format is as follows:
	The word "else", followed by a newline, followed by a body.	
Examples:
	if ([var x > int y])
	{
		//Code
	}
	elseif ([int 5 != method y()] or [string "ASDF" == var s])
	{
		//Code
	}
	else
	{
		//Code
	}


1.9. Loop
-----------------
A Loop, like an If, contains a code body which will be executed under certain conditions.
Loops will repeatedly execute their code bodies until a certain condition is met. This condition differs between the two variants.

Loop bodies, like all bodies, have their own scope and may declare variables within them accessible only to these bodies (or any child bodies within them)

1.9.1. Loop For
-----------------
A Loop For executes its code body for a specified number of times. The number must be an integer and may be either a positive integer, zero, or -1.
The positive number will execute for specifically that many times. If the integer is zero, the loop for will not execute.
Finally, if -1 is provided, the loop for will loop infinitely, until the program is halted.

The format of a Loop For is as follows:
	The word "loopfor", followed by one or more spaces, an integer (as described above), followed by a newline, followed by a body.
		NOTE: DO NOT FORGET THE SPACE BETWEEN LOOPFOR AND THE INTEGER
Examples:
	loopfor 5
	{
		//Code
	}
	
	loopfor -1
	{
		//Code
	}

1.9.2. Loop Until
-----------------
A Loop Until has a Condition associated with it and will execute for as long as that condition remains false.
The Condition is evaluated before we enter the loop body, so if the Condition is true on first evaluation, the body is never executed.

Note that it is possible to enter an infinite loop using a Loop Until. However, if you intend to create an infinite loop, the Loop For would be better for this.

The format of a Loop Until is as follows:
	The word "loopuntil", followed by one or more spaces, followed by an open parentheses, followed by a Condition, followed by a close parentheses, followed by a newline, followed by a body.	
	NOTE: DO NOT FORGET THE SPACE BETWEEN LOOPUNTIL AND THE OPENPAREN
Examples:
	loopuntil ([var x > int y])
	{
		//Code
	}
	
	loopuntil ([int 5 != method y()] or [string "ASDF" == var s])
	{
		//Code
	}

1.10. Wait
-----------------
A Wait statement causes the program to pause (sleep) for a certain amount of time.
There are two variants: one which waits until a certain condition is met, and one which waits for a specified amount of time.

1.10.1. Wait For
-----------------
A Wait For simply waits for a specified amount of time. This amount of time is provided in the form of an integer. The integer must be either positive or zero. NEGATIVE INTEGERS ARE NOT ALLOWED.
The integer in this case represents the number of seconds that we wait. Once that amount of time has passed, we wake up and continue execution.

The format of a Wait For is as follows:
	The word "waitfor", followed by one or more spaces, followed by an integer (as described above). followed by a semicolon.
	NOTE: DO NOT FORGET THE SPACE BETWEEN WAITFOR AND THE INTEGER
Examples:
	waitfor 5;
	waitfor 20;

1.10.2. Wait Until
-----------------
The Wait Until statement causes the program to sleep until a provided Condition evaluates to true.
It is very important to note that the Condition should be something that could actually ever evaluate to true. Because the interpreter is sleeping, no internal values (i.e. variables) will ever change, 
and so the Condition on which we are waiting should involve values external. The most common example for our robot code will be sensor values, which will be retrieved by an external method.

The Wait Until has a polling frequency. It will sleep for a certain amount of time, wake up, and evaluate the condition. The polling frequency is currently set to 250ms.
Once the Condition evaluates to true, the interpreter will wake up and continue execution.

If the Condition evaluates to true on first evaluation, the Wait will never occur and the code will continue as normal.

The format of a Wait Until is as follows:
	The word "waituntil", followed by one or more spaces, followed by an open parentheses, followed by a Condition, followed by a close parentheses, followed by a semicolon.
	NOTE: DO NOT FORGET THE SPACE BETWEEN WAITUNTIL AND THE OPENPAREN
Examples:
	waituntil ([var x > int y]);
	waituntil ([int 5 != method y()] or [string "ASDF" == var s]);

======================================================================

2. Future Development
-----------------
