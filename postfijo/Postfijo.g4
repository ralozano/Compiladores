grammar Postfijo;

prog: expr '\n';

expr: expr '+' term {System.out.println('+');}
    | expr '-' term {System.out.println('-');}
    | term;
    
term: term '*' factor {System.out.println('*');}
    | term '/' factor {System.out.println('/');}
    | factor;
    
factor: INT {System.out.println(_ctx.getText());}
      | '('expr')'; 
    
INT: [0-9]+ ;
WS : [ \t\r\n]+ -> skip ; // skip spaces, tabs, newlines

/*digit: '0' {System.out.println('0');}
    | '1' {System.out.println('1');}
    | '2' {System.out.println('2');}
    | '3' {System.out.println('3');}
    | '4' {System.out.println('4');}
    | '5' {System.out.println('5');}
    | '6' {System.out.println('6');}
    | '7' {System.out.println('7');}
    | '8' {System.out.println('8');}
    | '9' {System.out.println('9');};
*/