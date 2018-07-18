/*
MIT License
Copyright (c) 2018 NUM Technology Ltd
Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
documentation files (the "Software"), to deal in the Software without restriction, including without limitation
the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and
to permit persons to whom the Software is furnished to do so, subject to the following conditions:
The above copyright notice and this permission notice shall be included in all copies or substantial portions of
the Software.
THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS
OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/

lexer grammar MODLLexer;
  // These tokens are for default mode (outside of conditionals)
  WS        : [ \t] + -> skip ;
  NULL      : '000' | 'null' | 'NULL' ;
  TRUE      : '01' | 'true' | 'TRUE' ;
  FALSE     : '00' | 'false' | 'FALSE' ;
  NEWLINE   : ( '\n' | '\r' | '\r\n' ) ;
  COLON     : ':' ;
  EQUALS    : '=' ;
  SC        : ';' ;
  LBRAC     : '(' ;
  RBRAC     : ')' ;
  LSBRAC    : '[' ;
  RSBRAC    : ']' ;
  NUMBER    : '-'? INT ('.' [0-9] +)? EXP? ;
    fragment INT : '0' | [1-9] [0-9]* ;
    fragment EXP : [Ee] [+\-]? INT ;

  COMMENT
    // Comments are made using ## anywhere, they are ignored by parser
    : '##' ( INSIDE_COMMENT ) -> skip
    ;
    fragment INSIDE_COMMENT
      // Any character is allowed inside of comments except for new lines (a new line ends the comment)
      : ~[\n\r] *
    ;

  STRING : '# '? ( ESCAPED | UNRESERVED | GRAVED | HASH_PREFIX )+ ( ('#'? ' '+ '#'?) ( ESCAPED | UNRESERVED | GRAVED )+ )* ;
    // These two should be identical except for regex inversion on first
    fragment UNRESERVED
      : ~ ( '#' | '`' | '{' | '}' | '(' | ')' | '[' | ']' | ';' | ' ' | '=' | ':' | '"' | '\b' | '\f' | '\n' | '\r' | '\t' )
    ;
    fragment RESERVED_CHARS
      :   ( '#' | '`' | '{' | '}' | '(' | ')' | '[' | ']' | ';' | ' ' | '=' | ':' | '"' | '\b' | '\f' | '\n' | '\r' | '\t' )
    ;
    fragment ESCAPED
      // Standard JSON escaping, e.g. \t for tab
      : '\\' ([\\/bfnrt] | UNICODE)
      // Standard back slash can be used to escape reserved characters
      | '\\' RESERVED_CHARS
      // Additionally, MODL allows tilde ( ~ ) escapes because backslash is problematic in DNS
      | '~' RESERVED_CHARS
      ;
      fragment UNICODE
        : 'u' HEX HEX HEX HEX
        ;
          fragment HEX
          : [0-9a-fA-F]
          ;

  HASH_PREFIX
    : '#' STRING
    ;

  QUOTED
    // Literal string inside double quotes – any character is allowed inside quotes except for the double quote itself
    : '"' (STRING | INSIDE_QUOTES) '"'
    ;
    fragment INSIDE_QUOTES
      : ~["]*
      ;
  GRAVED
    // String inside graves – any character is allowed inside graves except for the grave itself. It is handled by parser
    : '`' ( INSIDE_GRAVES ) '`'
    ;
    fragment INSIDE_GRAVES
      : ~[`]*
      ;
  // This token changes the mode to 'conditional' mode, see below
  LCBRAC  : '{' -> pushMode(CONDITIONAL);
mode CONDITIONAL;
  // These tokens must be redefined for this mode
  CWS       : [ \t] + -> skip;
  CNULL     : ('000' | 'null' | 'NULL') -> type(NULL);
  CTRUE     : ('01' | 'true' | 'TRUE') -> type(TRUE);
  CFALSE    : ('00' | 'false' | 'FALSE') -> type(FALSE);
  CNEWLINE  : ( '\n' | '\r' | '\r\n' ) -> type(NEWLINE);
  CCOLON    : ':' -> type(COLON);
  CEQUALS   : '=' -> type(EQUALS);
  CSC       : ';' -> type(SC);
  CLBRAC    : '(' -> type(LBRAC);
  CRBRAC    : ')' -> type(RBRAC);
  CLSBRAC   : '[' -> type(LSBRAC);
  CRSBRAC   : ']' -> type(RSBRAC);
  CNUMBER   : '-'? INT ('.' [0-9] +)? EXP? -> type(NUMBER);
  // These tokens are only defined in this mode
  QMARK     : '?';
  FSLASH    : '/';
  GTHAN     : '>';
  LTHAN     : '<';
  ASTERISK  : '*';
  AMP       : '&';
  PIPE      : '|';
  EXCLAM    : '!';
  // This is for nested conditionals
  CLCBRAC  : '{' -> pushMode(CONDITIONAL), type(LCBRAC);
  // A different version of string is defined to protect the reserved characters
  CSTRING
    : (CESCAPED | CUNRESERVED | CGRAVED)+ (' '+ (CESCAPED | CUNRESERVED | CGRAVED)+)* -> type(STRING)
    ;
    fragment CUNRESERVED
    : ~ ('#' | '{' | '`' | '}' | '(' | ')' | ';' | ' ' | '=' | ':' | '"' | '?' | '/' | '>' | '<' | '!' | '|' | '&' | '\b' | '\f' | '\n' | '\r' | '\t' | '[' | ']' )
    ;
    fragment CRESERVED_CHARS
    :   ('#' | '{' | '`' | '}' | '(' | ')' | ';' | ' ' | '=' | ':' | '"' | '?' | '/' | '>' | '<' | '!' | '|' | '&' | '\b' | '\f' | '\n' | '\r' | '\t' | '[' | ']' )
    ;
    fragment CESCAPED
      // Standard JSON escaping, e.g. \t for tab
      : '\\' ([\\/bfnrt] | UNICODE)
      // Back slash escaping can also be used
      | '\\' CRESERVED_CHARS
      // MODL escaping with tilde ~ (backslash problematic in DNS)
      | '~' CRESERVED_CHARS
      ;
  CCOMMENT
    // Comments are made using ## anywhere, they are ignored by parser
    : '##' ( INSIDE_COMMENT ) -> skip
    ;
  CQUOTED
    // Literal string inside double quotes – any char is allowed inside quotes except for the double quote itself
    : '"' (STRING | INSIDE_QUOTES) '"' -> type(QUOTED)
    ;
  CGRAVED
    // String inside graves – any char is allowed inside graves except for the grave itself. It is handled by parser
    : '`' (STRING | INSIDE_GRAVES) '`' -> type(GRAVED)
    ;
  // The right curly bracket takes us out of conditional mode
  RCBRAC  : '}' -> popMode;