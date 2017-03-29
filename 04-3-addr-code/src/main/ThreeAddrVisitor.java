package main;

import java.util.*;

import org.antlr.v4.runtime.tree.TerminalNode;

import lang.GrammarBaseVisitor;
import lang.GrammarParser.*;
import lang.GrammarLexer;

public class ThreeAddrVisitor extends GrammarBaseVisitor<Command> {
	class FunctionDef {
		int numArgs;
		List<Command> body = new ArrayList<Command>();
		public FunctionDef(int numArgs, List<Command> body) {
			this.numArgs = numArgs;
			this.body = body;
		}
	}
	
	public List<Command> commands = new ArrayList<Command>();
	HashMap<String, Command> variables = new HashMap<String, Command>();
	HashMap<String, FunctionDef> functions = new HashMap<String, FunctionDef>();
	
	@Override
	public Command visitS(SContext ctx) {
		for (StmtContext i : ctx.stmt()) {
			visit(i);
		}
		return null;
	}
	
	@Override
	public Command visitAssignment(AssignmentContext ctx) {
		String key = ctx.Var().getText();
		variables.put(key, visit(ctx.expr()));
		return null;
	}
	
	@Override
	public Command visitJustExpr(JustExprContext ctx) {
		Command value = visit(ctx.expr());
		return value;
	}
	
	@Override
	public Command visitUnaryOp(UnaryOpContext ctx) {
		Command a = visit(ctx.expr());
		if(ctx.op.getType() == GrammarLexer.Plus) {
			return a;
		} else if (ctx.op.getType() == GrammarLexer.Minus) {
			Command command = new Command(Command.Op.UnaryMinus, new Address(a));
			this.commands.add(command);
			return command;
		}
		return null;
	}
	
	@Override
	public Command visitExpOp(ExpOpContext ctx) {
		Command a = visit(ctx.expr(0));
		Command b = visit(ctx.expr(1));
		if(ctx.op.getType() == GrammarLexer.Exp) {
			Command command = new Command(Command.Op.Exp, new Address(a),
					new Address(b));
			this.commands.add(command);
			return command;
		}
		return null;
	}
	
	@Override
	public Command visitMulDivOp(MulDivOpContext ctx) {
		Command a = visit(ctx.expr(0));
		Command b = visit(ctx.expr(1));
		if(ctx.op.getType() == GrammarLexer.Mult) {
			Command command = new Command(Command.Op.Mult, new Address(a),
					new Address(b));
			this.commands.add(command);
			return command;
		} else if (ctx.op.getType() == GrammarLexer.Div) {
			Command command = new Command(Command.Op.Div, new Address(a),
					new Address(b));
			this.commands.add(command);
			return command;
		}
		return null;
	}
	
	@Override
	public Command visitAddSub(AddSubContext ctx) {
		Command a = visit(ctx.expr(0));
		Command b = visit(ctx.expr(1));
		if(ctx.op.getType() == GrammarLexer.Plus) {
			Command command = new Command(Command.Op.Add, new Address(a),
					new Address(b));
			this.commands.add(command);
			return command;
		} else if (ctx.op.getType() == GrammarLexer.Minus) {
			Command command = new Command(Command.Op.Sub, new Address(a),
					new Address(b));
			this.commands.add(command);
			return command;
		}
		return null;
	}
	
	@Override
	public Command visitParens(ParensContext ctx) {
		return visit(ctx.expr());
	}
	
	@Override
	public Command visitLitNum(LitNumContext ctx) {
		double value = Double.parseDouble(ctx.Num().getText());
		this.commands.add(new Command(Command.Op.Assign, new Address(value)));
		return null;
	}
	
	@Override
	public Command visitVarUse(VarUseContext ctx) {
		String key = ctx.Var().getText();
		if(variables.containsKey(key))
			return variables.get(key);
		else
			return null;
	}
	
	@Override
	public Command visitFunDef(FunDefContext ctx) {
		String functionName = ctx.Var().getText();
		ExprContext functionBody = ctx.expr();
		
		ThreeAddrVisitor visitor = new ThreeAddrVisitor();
		visitor.variables = new HashMap<String,Command>(this.variables);
		
		int i = 0;
		for (TerminalNode var : ctx.arglist().Var()) {
			visitor.variables.put(var.getText(), new Command(
					Command.Op.Assign, new Address(i)
					));
			i++;
		}
		
		visitor.visit(functionBody);
		
		List<Command> parsedBody = visitor.commands;
		
		functions.put(functionName, new FunctionDef(i, parsedBody));
		
		return null;
	}
	
	@Override
	public Command visitFunCall(FunCallContext ctx) {
		// TODO Auto-generated method stub
		String functionName = ctx.Var().getText();
		if(! functions.containsKey(functionName)) {
			return null;
		}
		List<ExprContext> argList = ctx.exprlist().expr();
		FunctionDef def = functions.get(functionName);
		if(argList.size() != def.varList.size()) {
			return null;
		}
		List<List<Command>> argValList = new ArrayList<List<Command>>();
		for (ExprContext expr : argList) {
			argValList.add(visit(expr));
		}
		HashMap<String, List<Command>> oldvars = new HashMap<String, List<Command>>(variables);
		for(int i=0; i<argList.size(); ++i) {
			String varName = def.varList.get(i);
			List<Command> varVal = argValList.get(i);
			variables.put(varName, varVal);
		}
		List<Command> funcValue = visit(def.body);
		variables = oldvars;
 		return funcValue;
	}
}
