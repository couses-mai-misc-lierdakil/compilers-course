package main;

import java.util.Iterator;

import main.TreeParser.*;

public class Visitor {
	Double visit(Node n) {
		if(n instanceof Branch)
			return visit((Branch)n);
		else if(n instanceof Leaf)
			return visit((Leaf)n);
		else
			throw new RuntimeException("Not implemented");
	}
	Double visit(Branch b) {
		switch(b.name) {
		case addsub: {
			double op1 = visit(b.get(0));
			Node addsub2 = b.get(1);
			while(addsub2.size() > 0) {
				double op2 = visit(addsub2.get(1));
				switch(addsub2.get(0).token().name) {
				case Add:
					op1+=op2;
					break;
				case Sub:
					op1-=op2;
					break;
				default:
					throw new RuntimeException("Not implemented?");
				}
				addsub2 = (Branch)addsub2.get(2);
			}
			return op1;
		}
		case muldiv: {
			Iterator<Node> it = b.iterator();
			double op1 = visit(it.next());
			while(it.hasNext()) {
				Node op = it.next();
				double op2 = visit(it.next());
				switch(op.token().name) {
				case Mul:
					op1*=op2;
					break;
				case Div:
					op1/=op2;
					break;
				default:
					throw new RuntimeException("Not implemented?");
				}
			}
			return op1;
		}
		case exp: {
			if(b.size()>2)
				return Math.pow(visit(b.get(0)), visit(b.get(2)));
			else
				return visit(b.get(0));
		}
		case term: {
			if(b.get(0).token().name == Token.Type.LP)
				return visit(b.get(1));
			else
				return visit(b.get(0));
		}
		case unary : {
			switch(b.get(0).token().name) {
			case Add:
				return visit(b.get(1));
			case Sub:
				return -visit(b.get(1));
			default:
				throw new RuntimeException("Not implemented");
			}
		}
		default:
			throw new RuntimeException("Not implemented");
		}
	}
	Double visit(Leaf l) {
		switch(l.token().name) {
		case Num:
			return (double)l.token().value;
		default:
			return null;
		}
	}
}
