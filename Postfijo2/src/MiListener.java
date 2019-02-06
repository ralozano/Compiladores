import java.util.*;


public class MiListener extends PostfijoBaseListener {
	Stack<Float> pila = new Stack<>();
	Map<String,Float> variables = new HashMap<>();
	@Override public void exitProg(PostfijoParser.ProgContext ctx) {
		if (pila.size() != 0)
			System.out.println("resultado " + pila.pop());
	}

	@Override public void exitFactor(PostfijoParser.FactorContext ctx) { 
		if(ctx.ID() != null) {
			//System.out.println("va " + ctx.ID());
			Float a = variables.get(ctx.ID().getText());
			if(a != null) {
				pila.push(a);
			}
			else {
				System.err.println(ctx.ID().getText() + " no esta definida");
				System.exit(1);
			}
		}
		else if (ctx.getChildCount() == 1) {
			Float i = new Float(ctx.getText());
			pila.push(i);
		}
		//System.out.println(i);
	}

	@Override public void exitTerm(PostfijoParser.TermContext ctx) { 
		//System.out.println("hijos " +  ctx.getChildCount());
		if (ctx.getChildCount() == 3) {
			Float b = pila.pop();
			Float a = pila.pop();
			if(ctx.getChild(1).getText().contentEquals("*")) {
				Float c = a * b;
				pila.push(c);
			}
			else {
				Float c = a / b;
				pila.push(c);
			}
		}
	}
	@Override public void exitExpr(PostfijoParser.ExprContext ctx) { 
		//System.out.println("hijos " +  ctx.getChildCount());
		if (ctx.getChildCount() == 3) {
			System.out.println("tiene tres hijos");
			if(ctx.getChild(1).getText().equals("=")) {
				Float a = pila.pop();
				variables.put(ctx.getChild(0).getText(),a);
				pila.push(a);
			}
			else {
				Float b = pila.pop();
				Float a = pila.pop();
				if(ctx.getChild(1).getText().equals("+")) {
					Float c = a + b;
					pila.push(c);
				}
				else {
					Float c = a - b;
					pila.push(c);
				}
			}
		}
	}

}
