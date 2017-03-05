grammar Grammar;
@header {
	package lang;
}

WS : [ \t\n\r]+ -> skip;
/* 123.432E+10 */
Num : [0-9]+( '.' [0-9]+)?([eE][+-]?[0-9]+)?;
Plus : '+';
Minus : '-';
Mult : '*';
Div : '/';
Exp : '^';
LParen : '(';
RParen : ')';
Assign : '=';
Var : [A-Za-z]+;
Semicolon : ';';

s : stmt (';' stmt)* EOF;

stmt :
    Var '=' expr #Assignment
  | expr #JustExpr
  ;

expr :
	op=('+'|'-') expr #UnaryOp
  | <assoc=right> expr op='^' expr #ExpOp 
  | expr op=('*'|'/') expr #MulDivOp
  | expr op=('+'|'-') expr #AddSub
  | '(' expr ')' #Parens
  | Num #LitNum
  | Var #VarUse
  ;