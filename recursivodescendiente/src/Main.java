
public class Main {

	public static void main(String[] args) {
		Parser parser = new Parser();
		parser.lookahead = parser.texto[parser.siguiente++];
		parser.I();
	}

}
