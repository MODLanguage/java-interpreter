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
  : (NEWLINE* modl_structure ( NEWLINE* SC? NEWLINE* modl_structure NEWLINE*)* )? NEWLINE* SC? NEWLINE* EOF;

modl_structure
  : modl_map
  | modl_array
  | modl_top_level_conditional
  | modl_pair
  ;

modl_map
  // ( key = value; key = value )
  : LBRAC NEWLINE*
        ( modl_map_item (SC? NEWLINE* modl_map_item )* NEWLINE* )?
    RBRAC
  ;

modl_array
  // [ item; item ]
  : LSBRAC NEWLINE*
        ( ( modl_array_item | modl_nb_array ) ((SC+ | NEWLINE+) ( modl_array_item | modl_nb_array ) )* NEWLINE* )?
    RSBRAC
  ;

modl_nb_array
  // non-bracketed array with missing elements
  // numbers=1:2:3:::4:5:6
  : modl_array_item (NEWLINE* COLON+ NEWLINE* modl_array_item )+
  ;

modl_pair
  // A pair can be a traditional name-value pair split by an equals sign (standard pair),
  // e.g. name=John
  //
  // For efficiency, it's also possible to assign a map to a pair without an equals sign,
  // since the left bracket separates the key from the value  – this is called a map pair.
  // e.g. person(name=John) – this is equivalent to person=(name=John)
  //a
  // It's also possible to do the same with an array pair
  // e.g. numbers[1;2;3] – equivalent to numbers=[1;2;3]

  : ( STRING | QUOTED) NEWLINE* EQUALS NEWLINE* modl_value_item                // key = value        (standard pair)
  | STRING modl_map                                                            // key( key = value ) (map pair)
  | STRING modl_array                                                          // key[ item; item ]  (array pair)
  ;

modl_value_item
  : ( modl_value | modl_value_conditional )
  ;

// Four conditional rules are set because the grammar validates the conditional return depending on
// the context in which the conditional appears. A conditional at the top level can only return a
// MODL structure, a conditional within a map can only return a pair, a conditional within an array
// can only return a value, a conditional assigned to a key MUST return a value. Each can return another
// conditional of the same type.
modl_top_level_conditional
  // Conditionals at the top level do not require else
  // { country=gb? return=this /country=us? return=that }
  : LCBRAC NEWLINE*
        modl_condition_test QMARK NEWLINE*
        modl_top_level_conditional_return NEWLINE*
        (FSLASH NEWLINE* modl_condition_test? QMARK NEWLINE*
        modl_top_level_conditional_return )* NEWLINE*
    RCBRAC
  ;
  modl_top_level_conditional_return
    : modl_structure ( ( SC | NEWLINE | SC NEWLINE ) modl_structure )* SC? NEWLINE*
    ;

modl_map_conditional
  // Conditionals within maps do not require else
  // e.g. { country=gb? return=this /country=us? return=that }
  : LCBRAC NEWLINE*
        modl_condition_test QMARK NEWLINE* modl_map_conditional_return NEWLINE*
        (FSLASH NEWLINE* modl_condition_test? QMARK NEWLINE*
        modl_map_conditional_return )* NEWLINE*
    RCBRAC
  ;
  modl_map_conditional_return
    : modl_map_item ( ( SC | NEWLINE* | SC NEWLINE* ) modl_map_item )* SC? NEWLINE*
    ;
    modl_map_item
      : modl_pair | modl_map_conditional
      ;

modl_array_conditional
  // Conditionals within arrays do not require else
  // e.g. { country=gb? this /country=us? that }
  : LCBRAC NEWLINE*
        modl_condition_test QMARK NEWLINE* modl_array_conditional_return NEWLINE*
        (FSLASH NEWLINE* modl_condition_test? QMARK NEWLINE*
        modl_array_conditional_return )* NEWLINE*
    RCBRAC
  ;
  modl_array_conditional_return
    : modl_array_item ( ( SC | NEWLINE+ | SC NEWLINE* ) modl_array_item )* NEWLINE*
    ;
    modl_array_item
      : modl_array_value_item | modl_array_conditional
      ;

modl_value_conditional
  // Conditionals within values DO require else
  // e.g. { country=gb? this /country=us? that /? other }
  : LCBRAC NEWLINE* modl_condition_test QMARK (NEWLINE* modl_value_conditional_return NEWLINE*
        (FSLASH NEWLINE* modl_condition_test QMARK NEWLINE* modl_value_conditional_return )* NEWLINE*
        (FSLASH NEWLINE* QMARK NEWLINE* modl_value_conditional_return) NEWLINE*)?
    RCBRAC
  ;
  modl_value_conditional_return
    : modl_value_item ( NEWLINE* COLON modl_value_item ) *
    ;

modl_condition_test
  // country=gb|language=en?
  : EXCLAM? ( modl_condition | modl_condition_group ) (( AMP | PIPE ) EXCLAM? ( modl_condition | modl_condition_group ) )*
  ;

modl_operator
  // operator within conditionals
  : EQUALS | GTHAN | GTHAN EQUALS | LTHAN | LTHAN EQUALS | EXCLAM EQUALS
  ;

modl_condition
  // e.g. country=gb
  : NEWLINE* STRING? modl_operator? modl_value (PIPE modl_value )* NEWLINE*
  ;

modl_condition_group
  // { country=ca & language=fr }
  : LCBRAC modl_condition_test (( AMP | PIPE ) modl_condition_test)* RCBRAC
  ;

modl_value
  : modl_map
  | modl_array
  | modl_nb_array
  | modl_pair
  | QUOTED
  | NUMBER
  | STRING
  | TRUE
  | FALSE
  | NULL
;

modl_array_value_item
  : modl_map
  | modl_pair
  | modl_array
  | QUOTED
  | NUMBER
  | STRING
  | TRUE
  | FALSE
  | NULL
;