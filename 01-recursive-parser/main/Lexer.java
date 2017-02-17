package main;
import java.util.*;
import static main.Token.Type.*;

public class Lexer {
	public List<Token> lex(String s) {
		List<Token> tokens = new ArrayList<Token>();
		String buf = "";
		for(int i=0; i< s.length(); ++i) {
			char c = s.charAt(i);
			if(c >= '0' && c <= '9') {
				buf+=c;
			} else {
				if(buf.length()>0) {
					tokens.add(new Token(Num, Integer.parseInt(buf)));
					buf = "";
				}
				
				switch(c) {
				case ' ':
				case '\t':
				case '\r':
				case '\n':
					continue;
				case '+':
					tokens.add(new Token(Add));
					break;
				case '-':
					tokens.add(new Token(Sub));
					break;
				case '*':
					tokens.add(new Token(Mul));
					break;
				case '/':
					tokens.add(new Token(Div));
					break;
				case '^':
					tokens.add(new Token(Exp));
					break;
				case '(':
					tokens.add(new Token(LP));
					break;
				case ')':
					tokens.add(new Token(RP));
					break;
				default:
					throw new RuntimeException("Invalid character "+c);
				}
			}
		}
		if(buf.length()>0)
			tokens.add(new Token(Num, Integer.parseInt(buf)));
		return tokens;
	}
}
