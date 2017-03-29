package main;

public class Address {
	public Command temp;
	public Integer param;
	public Double val;
	Address (Command temp) {
		this.temp = temp;
		this.param = null;
		this.val = null;
	}
	Address (int param) {
		this.temp = null;
		this.param = param;
		this.val = null;
	}
	Address (Double val) {
		this.temp = null;
		this.param = null;
		this.val = val;
	}
}
