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
	1.6. Assignment
	1.7. Conditions
	1.8. If
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
Variable Scoping: 	Dynamic

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

Bodies have variable scopes, although this is only pertinent for the main body, and for method bodies. Variables declared within a body belong to that body's scope and are accessible by any child bodies within that body. 
A parent body cannot, however, access variables declared within a child body.

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

1.5.1. Internal Methods
-----------------

1.5.2. External Methods
-----------------

1.6. Assignment
-----------------

1.7. Conditions
-----------------

1.8. If
-----------------

1.9. Loop
-----------------

1.9.1. Loop For
-----------------

1.9.2. Loop Until
-----------------

1.10. Wait
-----------------

1.10.1. Wait For
-----------------

1.10.2. Wait Until
-----------------

