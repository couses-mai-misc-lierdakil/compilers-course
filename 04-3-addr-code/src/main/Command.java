package main;

import java.util.List;

public class Command {
	enum Op {
		Exp,
		Mult,
		Div,
		Add,
		Sub,
		Assign,
		Param,
		Call
	}
	public Op cmd;
	public Address a1;
	public Address a2;
	Command (List<Command> list, Op cmd, Address a1, Address a2) {
		this.cmd = cmd;
		this.a1 = a1;
		this.a2 = a2;
		list.add(this);
	}
	Command (List<Command> list, Op cmd, Address a1) {
		this.cmd = cmd;
		this.a1 = a1;
		this.a2 = null;
		list.add(this);
	}
	String toString (List<Command> list) {
		if (this.a2 != null)
			return (this.cmd.name() + " " + this.a1.toString(list) + " " + this.a2.toString(list));
		else
			return (this.cmd.name() + " " + this.a1.toString(list));
	}
}
