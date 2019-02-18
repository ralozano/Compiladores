package lexer;

public class RelOper extends Token {
	String oper;
	
	public RelOper(int grupo, String oper) {
		super(grupo);
		this.oper = oper;
	}
	
	public String toString() {
		return super.toString() + "(" + oper + ")"; 
	}

}
