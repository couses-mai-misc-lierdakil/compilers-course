grammar Lang;

@header {
package parser;
}

Num : [0-9]+;
WS : [ \t\r\n] -> skip;
Add: '+';
Sub: '-';
Mul: '*';
Div: '/';
Exp: '^';
LP : '(';
RP : ')';
	
expr: addsub;

//addsub :
//	addsub ('+'|'-') muldiv
//  | muldiv
//	;
addsub:
	muldiv addsub2
	;
addsub2:
	('+'|'-') muldiv addsub2
	|
	;
muldiv:
	exp (('*'|'/') exp)*
	;
exp :
	term ('^' exp)?;
term:
	Num | '(' addsub ')' | unary
	;
unary:
	('+'|'-') term;