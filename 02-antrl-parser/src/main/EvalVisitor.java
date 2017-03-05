package main;

import java.util.HashMap;

import lang.GrammarBaseVisitor;
import lang.GrammarParser.*;
import lang.GrammarLexer;

public class EvalVisitor extends GrammarBaseVisitor<Double> {
	HashMap<String, Double> variables = new HashMap<String, Double>();
	
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
//		System.out.println("Found UnaryOp:" + ctx.getText());
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
//		System.out.println("Found ExpOp:" + ctx.getText());
		double a = visit(ctx.expr(0));
		double b = visit(ctx.expr(1));
		if(ctx.op.getType() == GrammarLexer.Exp) {
			return Math.pow(a, b);
		}
		return null;
	}
	
	@Override
	public Double visitMulDivOp(MulDivOpContext ctx) {
//		System.out.println("Found MulDivOp:" + ctx.getText());
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
//		System.out.println("Found AddSubOp:" + ctx.getText());
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
//		System.out.println("Found Parens:" + ctx.getText());
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
}
