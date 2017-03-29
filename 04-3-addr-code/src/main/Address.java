package main;

import java.util.List;

public class Address {
	public Command temp = null;
	public Integer param = null;
	public Double val = null;
	public String func = null;
	Address (Command temp) {
		this.temp = temp;
	}
	Address (int param) {
		this.param = param;
	}
	Address (Double val) {
		this.val = val;
	}
	Address (String func) {
		this.func = func;
	}
	String toString (List<Command> list) {
		if (this.temp != null) {
			return ("_t" + list.indexOf(this.temp)); 
		} else if (this.param != null) {
			return ("%" + this.param);
		} else if (this.val != null) {
			return (this.val.toString());
		} else if (this.func != null) {
			return (this.func);
		} else {
			return "???";
		}
	}
}
