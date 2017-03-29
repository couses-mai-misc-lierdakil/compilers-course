package main;

import lang.GrammarBaseVisitor;
import lang.GrammarParser.*;

public class PrintVisitor extends GrammarBaseVisitor<Integer> {
	@Override
	public Integer visitUnaryOp(UnaryOpContext ctx) {
		System.out.println("Found UnaryOp:" + ctx.getText());
		visit(ctx.expr());
		return null;
	}
	
	@Override
	public Integer visitExpOp(ExpOpContext ctx) {
		System.out.println("Found ExpOp:" + ctx.getText());
		visit(ctx.expr(0));
		visit(ctx.expr(1));
		return null;
	}
	
	@Override
	public Integer visitMulDivOp(MulDivOpContext ctx) {
		System.out.println("Found MulDivOp:" + ctx.getText());
		visit(ctx.expr(0));
		visit(ctx.expr(1));
		return null;
	}
	
	@Override
	public Integer visitAddSub(AddSubContext ctx) {
		System.out.println("Found AddSubOp:" + ctx.getText());
		visit(ctx.expr(0));
		visit(ctx.expr(1));
		return null;
	}
	
	@Override
	public Integer visitParens(ParensContext ctx) {
		System.out.println("Found Parens:" + ctx.getText());
		visit(ctx.expr());
		return null;
	}
	
	@Override
	public Integer visitLitNum(LitNumContext ctx) {
		System.out.println("Found LitNum:" + ctx.getText());
		return super.visitLitNum(ctx);
	}
}
