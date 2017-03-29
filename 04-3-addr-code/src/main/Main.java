package main;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import lang.*;

import java.util.*;

public class Main {

	public static void main(String[] args) throws Exception {
		Scanner s = new Scanner(System.in);
		s.useDelimiter("\\n");
		ThreeAddrVisitor vis = new ThreeAddrVisitor();
		while(true) {
			try {
				ANTLRInputStream input = new ANTLRInputStream(s.next());
				GrammarLexer lexer = new GrammarLexer(input);
				CommonTokenStream tokens = new CommonTokenStream(lexer);
				GrammarParser parser = new GrammarParser(tokens);
				ParseTree tree = parser.s();
				vis.visit(tree);
				int i = 0;
				for (Command cmd : vis.commands) {
					System.out.println(i + ": " + cmd.toString(vis.commands));
					i++;
				}
			} catch (NoSuchElementException e) {
				break;
			}
		}
		s.close();
	}

}
