package main;

import java.util.*;

import org.antlr.v4.runtime.tree.TerminalNode;

import lang.GrammarBaseVisitor;
import lang.GrammarParser.*;
import lang.GrammarLexer;

public class EvalVisitor extends GrammarBaseVisitor<Double> {
	class FunctionDef {
		List<String> varList;
		ExprContext body;
		public FunctionDef(List<String> vars, ExprContext body) {
			this.varList = vars;
			this.body = body;
		}
	}
	
	HashMap<String, Double> variables = new HashMap<String, Double>();
	HashMap<String, FunctionDef> functions = new HashMap<String, FunctionDef>();
	
	@Override
	public Double visitS(SContext ctx) {
		Double value=null;
		for (StmtContext i : ctx.stmt()) {
			value = visit(i);
		}
		return value;
	}
	
	@Override
	public Double visitAssignment(AssignmentContext ctx) {
		String key = ctx.Var().getText();
		Double value = visit(ctx.expr());
		variables.put(key, value);
		return null;
	}
	
	@Override
	public Double visitJustExpr(JustExprContext ctx) {
		Double value = visit(ctx.expr());
		System.out.println(value);
		return value;
	}
	
	@Override
	public Double visitUnaryOp(UnaryOpContext ctx) {
		double a = visit(ctx.expr());
		if(ctx.op.getType() == GrammarLexer.Plus) {
			return a;
		} else if (ctx.op.getType() == GrammarLexer.Minus) {
			return -a;
		}
		return null;
	}
	
	@Override
	public Double visitExpOp(ExpOpContext ctx) {
		double a = visit(ctx.expr(0));
		double b = visit(ctx.expr(1));
		if(ctx.op.getType() == GrammarLexer.Exp) {
			return Math.pow(a, b);
		}
		return null;
	}
	
	@Override
	public Double visitMulDivOp(MulDivOpContext ctx) {
		double a = visit(ctx.expr(0));
		double b = visit(ctx.expr(1));
		if(ctx.op.getType() == GrammarLexer.Mult) {
			return a*b;
		} else if (ctx.op.getType() == GrammarLexer.Div) {
			return a/b;
		}
		return null;
	}
	
	@Override
	public Double visitAddSub(AddSubContext ctx) {
		double a = visit(ctx.expr(0));
		double b = visit(ctx.expr(1));
		if(ctx.op.getType() == GrammarLexer.Plus) {
			return a+b;
		} else if (ctx.op.getType() == GrammarLexer.Minus) {
			return a-b;
		}
		return null;
	}
	
	@Override
	public Double visitParens(ParensContext ctx) {
		return visit(ctx.expr());
	}
	
	@Override
	public Double visitLitNum(LitNumContext ctx) {
		double value = Double.parseDouble(ctx.Num().getText());
		return value;
	}
	
	@Override
	public Double visitVarUse(VarUseContext ctx) {
		String key = ctx.Var().getText();
		if(variables.containsKey(key))
			return variables.get(key);
		else
			return null;
	}
	
	@Override
	public Double visitFunDef(FunDefContext ctx) {
		String functionName = ctx.Var().getText();
		ExprContext functionBody = ctx.expr();
		List<String> varList = new ArrayList<String>();
		for (TerminalNode var : ctx.arglist().Var()) {
			varList.add(var.getText());
		}
		
		functions.put(functionName, new FunctionDef(varList, functionBody));
		
		return null;
	}
	
	@Override
	public Double visitFunCall(FunCallContext ctx) {
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
		List<Double> argValList = new ArrayList<Double>();
		for (ExprContext expr : argList) {
			argValList.add(visit(expr));
		}
		HashMap<String, Double> oldvars = new HashMap<String, Double>(variables);
		for(int i=0; i<argList.size(); ++i) {
			String varName = def.varList.get(i);
			Double varVal = argValList.get(i);
			variables.put(varName, varVal);
		}
		Double funcValue = visit(def.body);
		variables = oldvars;
 		return funcValue;
	}
}
