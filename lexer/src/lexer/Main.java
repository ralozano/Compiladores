package lexer;

import java.io.IOException;

public class Main {

	public static void main(String[] args) throws IOException {
		Lexer lexer = new Lexer();
		
		System.in.mark(1);
		while(System.in.read() != -1) {
		    System.in.reset();
			Token t = lexer.scan();
			System.out.println(t);
			System.in.mark(1);
		} 
	}

}
