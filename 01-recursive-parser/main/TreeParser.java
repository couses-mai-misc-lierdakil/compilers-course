package main;
import java.util.*;
import static main.Token.Type.*;

public class TreeParser {
	List<Token> tokens;
	int pos;
	
	public enum NodeType {
		addsub, addsub2, muldiv, exp, term, unary,
		terminal
	}
	
	public abstract class Node {
		NodeType name;
		public void print() {
			print(0);
		}
		public abstract void print(int indent);
		public Node(NodeType name) {
			this.name = name;
		}
		public Node get(int i) {
			if(name == NodeType.terminal)
				throw new RuntimeException("terminal node doesn't have children!");
			return ((Branch)this).children.get(i);
		}
		public int size() {
			if(name == NodeType.terminal)
				throw new RuntimeException("terminal node doesn't have children!");
			return ((Branch)this).children.size();
		}
		public Iterator<Node> iterator() {
			if(name == NodeType.terminal)
				throw new RuntimeException("terminal node doesn't have children!");
			return ((Branch)this).children.iterator();
		}
		public Token token() {
			if(name != NodeType.terminal)
				throw new RuntimeException("non-terminal node doesn't have token!");
			return ((Leaf)this).value;
		}
	}
	
	public class Branch extends Node {
		private List<Node> children;
		public Branch(NodeType name) {
			super(name);
			this.children = new ArrayList<Node>();
		}
		public void add(Node e) {
			children.add(e);
		}
		public void add(Token e) {
			children.add(new Leaf(e));
		}
		@Override
		public void print(int indent) {
			String i = new String(new char[indent]).replace("\0", " ");
			System.out.println(i+name+": {");
			for(Node e : children) {
				e.print(indent+1);
			}
			System.out.println(i+"}");
		}
	}
	
	public class Leaf extends Node {
		private Token value;
		public Leaf(Token val) {
			super(NodeType.terminal);
			this.value=val;
		}
		@Override
		public void print(int indent) {
			String i = new String(new char[indent]).replace("\0", " ");
			System.out.println(i+value.name+": "+value.value);
		}
	}
	
	public TreeParser(List<Token> ts) {
		this.tokens = ts;
		this.pos = 0;
	}
	
	public Node parse() {
		pos = 0;
		return addsub();
	}
	
	Token peek() {
		if(pos < tokens.size())
			return tokens.get(pos);
		else
			return null;
	}
	
	void next() {
		pos++;
	}
	
	Node addsub() {
		Branch n = new Branch(NodeType.addsub);
		n.add(muldiv());
		n.add(addsub2());
		return n;
	}
	
	Node addsub2() {
		Branch n = new Branch(NodeType.addsub2);
		Token t = peek();
		if(t != null && (t.name == Add || t.name == Sub)) {
			n.add(t);
			next();
			n.add(muldiv());
			n.add(addsub2());
		}
		// Note: since it can be empty, we don't fail here.
		return n;
	}
	
	Node muldiv() {
		Branch n = new Branch(NodeType.muldiv);
		n.add(exp());
		Token t;
		while((t = peek()) != null &&
				(t.name == Mul 
				|| t.name == Div)) {
			n.add(t);
			next();
			n.add(exp());
		}
		return n;
	}
	
	Node exp() {
		Branch n = new Branch(NodeType.exp);
		n.add(term());
		Token t;
		if((t = peek()) != null &&
				t.name == Exp) {
			n.add(t);
			next();
			n.add(exp());
		}
		return n;
	}
	
	Node term() {
		Branch n = new Branch(NodeType.term);
		Token t = peek();
		if(t == null)
			throw new RuntimeException("Parser error");
		if(t.name == LP){
			n.add(t);
			next();
			n.add(addsub());
			t = peek();
			if(t != null && t.name == RP) {
				n.add(t);
				next();
				return n;
			} else {
				throw new RuntimeException("Parser error");
			}
		}
		else if(t.name == Num) {
			n.add(t);
			next();
			return n;
		}
		else { //unary?
			return unary();
		}
	}
	
	Node unary() {
		Branch n = new Branch(NodeType.unary);
		Token t = peek();
		if((t!=null) &&
				(t.name == Add ||
				t.name == Sub)){
			n.add(t);
			next();
			n.add(term());
			return n;
		} else
			throw new RuntimeException("Parser error");
	}
}
