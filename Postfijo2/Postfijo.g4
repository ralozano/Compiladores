grammar Postfijo;

prog: expr+;

expr: expr '+' term 
    | expr '-' term 
    | ID '=' expr
    | term;
    
term: term '*' factor 
    | term '/' factor
    | factor; 
    
factor: INT  
      | ID
      | '('expr')'; 

ID : LETRA+(DIGITO|LETRA)*;
LETRA: [A-Za-z];
INT: DIGITO+ ;
DIGITO: [0-9];    
WS : [ \t\r\n]+ -> skip ; // skip spaces, tabs, newlines
