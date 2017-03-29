package main;

public class Command {
	enum Op {
		UnaryMinus,
		Exp,
		Mult,
		Div,
		Add,
		Sub,
		Assign,
		FunDef
	}
	public Op cmd;
	public Address a1;
	public Address a2;
	Command (Op cmd, Address a1, Address a2) {
		this.cmd = cmd;
		this.a1 = a1;
		this.a2 = a2;
	}
	Command (Op cmd, Address a1) {
		this.cmd = cmd;
		this.a1 = a1;
		this.a2 = null;
	}
}
