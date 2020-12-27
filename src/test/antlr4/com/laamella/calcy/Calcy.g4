grammar Calcy;

calc: expression EOF;

expression:
      '(' expression ')'                                  # embracedExpr
    | '-' expression                                    # negationExpr
    |  expression op=('*' | '/') expression        # mulDivExpr
    |  expression op=('+' | '-') expression        # addSubExpr
    | DECIMAL_NUMBER_LITERAL                        # decimalLiteralExpr
    | HEXADECIMAL_NUMBER_LITERAL                # hexadecimalLiteralExpr
    ;

DECIMAL_NUMBER_LITERAL: [0-9]+;
HEXADECIMAL_NUMBER_LITERAL: '$' [0-9a-fA-F]+;
CR: [\r\n ] -> skip;
