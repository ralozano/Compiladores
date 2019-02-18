package lexer;

import java.io.*; 
import java.util.*;
import java.util.stream.IntStream;

public class Lexer {
	public int line = 1;
	private char peek = ' ';

	private Hashtable words = new Hashtable();

	void reserve(Word t) {
		words.put(t.lexeme, t);
	}

	public Lexer() {
		reserve(new Word(Tag.TRUE, "true"));
		reserve(new Word(Tag.FALSE, "false"));
	}

	private void saltaBlancos() throws IOException {
		for (; peek == ' ' || peek == '\n'; peek = (char) System.in.read()) {
			if (peek == ' ' || peek == '\t')
				continue;
			else if (peek == '\n')
				line = line + 1;
			else
				break;
		}
	}
	public Token scan() throws IOException {
		/* salta espacios en blanco */
		saltaBlancos();
		
		/* para comentario de una sola linea */
		if (peek == '/') {
			System.in.mark(1);
			char peek2 = (char) System.in.read();
			if (peek2 == '/') {
				while (peek != '\n') {
					peek = (char) System.in.read();
				} 
				saltaBlancos();
			}
			else 
				System.in.reset();
		}
		
		/* Para generar un entero */
		if (Character.isDigit(peek)) {
			int v = 0;
			do {
				v = 10 * v + Character.digit(peek, 10);
				peek = (char) System.in.read();
			} while (Character.isDigit(peek));
			return new Num(v);
		}

		/* Para generar un identificador */
		if (Character.isLetter(peek)) {
			StringBuffer b = new StringBuffer();
			do {
				b.append(peek);
				peek = (char) System.in.read();
			} while (Character.isLetterOrDigit(peek));
			String s = b.toString();
			Word w = (Word) words.get(s);
			if (w != null)
				return w;
			w = new Word(Tag.ID, s);
			words.put(s, w);
			return w;
		}
		if (peek == '<' || peek == '>' || peek == '!' || peek == '=') {
			System.in.mark(1);
			char peek2 = (char) System.in.read();
			if (peek2 == '=') {
				RelOper reloper = new RelOper(Tag.RELOPER, ""+ peek + peek2);
				peek = ' ';
				return reloper;
			}
			else {
				System.in.reset();
				if (peek != '=') {
					RelOper reloper = new RelOper(Tag.RELOPER, ""+ peek);
					peek = ' ';
					return reloper;
				}
			}
		}
		
			
			
			
		
			
			
			
			
			
			
			
			
			
		Token t = new Token(peek);
		peek = ' ';
		return t;
	}
}
