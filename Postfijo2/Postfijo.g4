grammar Postfijo;

prog: expr '\n';

expr: expr '+' term 
    | expr '-' term 
    | term;
    
term: term '*' factor 
    | term '/' factor 
    | factor;
    
factor: INT 
      | '('expr')'; 
    
INT: [0-9]+ ;
WS : [ \t\r\n]+ -> skip ; // skip spaces, tabs, newlines
