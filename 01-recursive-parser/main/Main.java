package main;
import java.util.*;

public class Main {
	public static void main(String[] args) throws Exception {
		Scanner sc = new Scanner(System.in);
		sc.useDelimiter("\n");
		String s;
		while((s = sc.next()) != null) {
			try {
				Lexer lexer = new Lexer();
				List<Token> tokens = lexer.lex(s);
//				for(Token t : tokens) {
//					t.print();
//				}
				TreeParser parser = new TreeParser(tokens);
				TreeParser.Node res=parser.parse();
//				res.print();
				Visitor vis = new Visitor();
				System.out.println(vis.visit(res));
			} catch (Exception e) {
				System.out.println(e);
				System.out.println(e.getMessage());
			}
		}
		sc.close();
	}

}
