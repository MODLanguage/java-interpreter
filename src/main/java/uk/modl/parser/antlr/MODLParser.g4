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

parser grammar MODLParser;

options {
  tokenVocab = MODLLexer;
}

modl
  // Valid MODL is zero or more MODL structures separated by semi-colons, newlines or both
  : (NEWLINE* structure ( ( SC | NEWLINE+ | SC NEWLINE+ ) structure )* SC?)? NEWLINE* EOF;

structure
  : map
  | array
  | top_level_conditional
  | pair
  ;

map
  // ( key = value; key = value )
  : LBRAC NEWLINE*
        map_item (SC? NEWLINE* map_item )* NEWLINE*
    RBRAC
  ;

array
  // [ item; item ]
  : LSBRAC NEWLINE*
        array_item (SC? NEWLINE* array_item )* SC? NEWLINE*
    RSBRAC
  ;

pair
  // A pair can be a traditional name-value pair split by an equals sign (standard pair),
  // e.g. name=John
  //
  // For efficiency, it's also possible to assign a map to a pair without an equals sign,
  // since the left bracket separates the key from the value  – this is called a map pair.
  // e.g. person(name=John) – this is equivalent to person=(name=John)
  //
  // It's also possible to do the same with an array pair
  // e.g. numbers[1;2;3] – equivalent to numbers=[1;2;3]
  //
  // Finally, it's possible to have multi-value pairs. This allows multiple values to be
  // passed with one key, the key must have an accompanying class which dictates how these
  // values are assigned to keys. Multi-value pairs can also be nested (see value_item)

  : ( STRING | QUOTED) EQUALS value_item ( NEWLINE* COLON value_item )*   // key = value        (standard pair)
  | STRING map                                                            // key( key = value ) (map pair)
  | STRING array                                                          // key[ item; item ]  (array pair)
  ;

value_item
  // this allows for nested multi-value pair values
  : ( value | LBRAC value_item ( NEWLINE* COLON value_item )* RBRAC | value_conditional )
  ;

// Four conditional rules are set because the grammar validates the conditional return depending on
// the context in which the conditional appears. A conditional at the top level can only return a
// MODL structure, a conditional within a map can only return a pair, a conditional within an array
// can only return a value, a conditional assigned to a key MUST return a value. Each can return another
// conditional of the same type.
top_level_conditional
  // Conditionals at the top level do not require else
  // { country=gb? return=this /country=us? return=that }
  : LCBRAC NEWLINE*
        condition_test QMARK NEWLINE*
        top_level_conditional_return NEWLINE*
        (FSLASH NEWLINE* condition_test? QMARK NEWLINE*
        top_level_conditional_return )* NEWLINE*
    RCBRAC
  ;
  top_level_conditional_return
    : structure ( ( SC | NEWLINE | SC NEWLINE ) structure )* SC? NEWLINE*
    ;

map_conditional
  // Conditionals within maps do not require else
  // e.g. { country=gb? return=this /country=us? return=that }
  : LCBRAC NEWLINE*
        condition_test QMARK NEWLINE* map_conditional_return NEWLINE*
        (FSLASH NEWLINE* condition_test? QMARK NEWLINE*
        map_conditional_return )* NEWLINE*
    RCBRAC
  ;
  map_conditional_return
    : map_item ( ( SC | NEWLINE* | SC NEWLINE* ) map_item )* SC? NEWLINE*
    ;
    map_item
      : pair | map_conditional
      ;

array_conditional
  // Conditionals within arrays do not require else
  // e.g. { country=gb? this /country=us? that }
  : LCBRAC NEWLINE*
        condition_test QMARK NEWLINE* array_conditional_return NEWLINE*
        (FSLASH NEWLINE* condition_test? QMARK NEWLINE*
        array_conditional_return )* NEWLINE*
    RCBRAC
  ;
  array_conditional_return
    : array_item ( ( SC | NEWLINE* | SC NEWLINE* ) array_item )* SC? NEWLINE*
    ;
    array_item
      : value | array_conditional
      ;

value_conditional
  // Conditionals within values DO require else
  // e.g. { country=gb? this /country=us? that /? other }
  : LCBRAC NEWLINE* condition_test QMARK NEWLINE* value_conditional_return NEWLINE*
        (FSLASH NEWLINE* condition_test QMARK NEWLINE* value_conditional_return )* NEWLINE*
        (FSLASH NEWLINE* QMARK NEWLINE* value_conditional_return) NEWLINE*
    RCBRAC
  ;
  value_conditional_return
    : value_item ( NEWLINE* COLON value_item ) *
    ;

condition_test
  // country=gb|us?
  : EXCLAM? ( condition | condition_group ) (( AMP | PIPE ) EXCLAM? ( condition | condition_group ) )*
  ;

operator
  // operator within conditionals
  : EQUALS | GTHAN | GTHAN EQUALS | LTHAN | LTHAN EQUALS | EXCLAM EQUALS
  ;

condition
  // e.g. country=gb
  : NEWLINE* STRING? operator? value (PIPE value )* NEWLINE*
  ;

condition_group
  // { country=ca & language=fr }
  : LCBRAC condition_test (( AMP | PIPE ) condition_test)* RCBRAC
  ;

value
  : map
  | array
  | pair
  | QUOTED
  | NUMBER
  | STRING
  | TRUE
  | FALSE
  | NULL
  ;