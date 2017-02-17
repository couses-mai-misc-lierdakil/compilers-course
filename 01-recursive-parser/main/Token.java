package main;
public class Token {
	public enum Type {
		Add, Sub, Mul, Div, Exp, LP, RP, Num
	}
	Type name;
	Integer value;
	public Token(Type name, int value) {
		this.name = name;
		this.value = value;
	}
	public Token(Type name) {
		this.name = name;
		this.value = null;
	}
	public void print() {
		System.out.print(name);
		System.out.print(",");
		System.out.println(value);
	}
}
