import java.util.*;

/*
 *  S-> SS+ | SS* | a
 *  
 *  S -> SSB | a
 *  B -> + | *
 *  
 *  S -> aR
 *  R -> SBR | epsilon
 *  B -> + | *
 *  
 */

public class Parser {
    Stack<String> pila = new Stack<>();	
	String[] texto = {"a", "a", "+", "a","*","$"};
	int siguiente = 0;
	String lookahead;
	
	void I() {
		S();
		match("$");
		System.out.println(pila.pop());
		
	}
	
	void S() {
		System.out.println("Entre a S");
		match("a");
		{pila.push("a");} // accion semantica
		R();
		System.out.println("Sali de S");
	}
	
	void R() {
		System.out.println("Entre a R");
		switch (lookahead) {
			case "a" :
				S();
				B();
				R();
			default :
		}
		System.out.println("Sali de R");
	}
	
	void B() {
		System.out.println("Entre a B");
		switch (lookahead) {
		case "+" :
			match("+");
		{
			String b = pila.pop();
			String a = pila.pop();
			String c = "( " + a + " + " + b + " )"; 
			pila.push(c);
		}   // accion semantica
			break;
		case "*" :
			match("*");
		{

			String b = pila.pop();
			String a = pila.pop();
			String c = "( " + a + " * " + b + " )"; 
			pila.push(c);
		}  // accion semantica
			break;
		default:
			System.out.println("Syntax error");
			System.exit(1);
		}
		System.out.println("Sali de B");
	}
	
	void match(String t) {
		if (lookahead.equals("$"))
			if (lookahead.equals(t))
			     return;
			else {
				System.out.println("Syntax error");
				System.exit(1);
			}
		if (lookahead.equals(t)) 		 
				lookahead = texto[siguiente++];
		else {
			System.out.println("Syntax error");
			System.exit(1);
		}
	}
}
