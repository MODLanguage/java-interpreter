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
  // Valid MODL is zero or more MODL structures separated by semi-colons
  : (( modl_structure?) | (modl_structure (STRUCT_SEP modl_structure )* ) STRUCT_SEP?) EOF;

modl_structure
  : modl_map
  | modl_array
  | modl_top_level_conditional
  | modl_pair
  ;

modl_map
  // ( key = value; key = value )
  : LBRAC
        ( modl_map_item (STRUCT_SEP modl_map_item )* )?
    RBRAC
  ;

modl_array
  // [ item; item ]
  : LSBRAC
        ( ( modl_array_item | modl_nb_array ) (STRUCT_SEP+ ( modl_array_item | modl_nb_array ) STRUCT_SEP* )* )?
    RSBRAC
  ;

modl_nb_array
  // non-bracketed array
  // numbers=1:2:3
  // also possible to have blank items
  // numbers=1:2:3:::4:5:6
  : (modl_array_item COLON+ )+ (modl_array_item)* COLON?
  ;

modl_pair
  // A pair can be a traditional name-value pair split by an equals sign (standard pair),
  // e.g. name=John
  //
  // For efficiency, it's also possible to assign a map to a pair without an equals sign,
  // since the left bracket separates the key from the value  – this is called a map pair.
  // e.g. person(name=John) – this is equivalent to person=(name=John)
  //
  // It's also possible to do the same with an array pair
  // e.g. numbers[1;2;3] – equivalent to numbers=[1;2;3]

  : ( STRING | QUOTED ) EQUALS modl_value_item  // key = value (standard pair)
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
  : LCBRAC
        modl_condition_test QMARK
        modl_top_level_conditional_return
        (FSLASH modl_condition_test? QMARK
        modl_top_level_conditional_return )*
    RCBRAC
  ;
  modl_top_level_conditional_return
    : modl_structure ( STRUCT_SEP modl_structure )*
    ;

modl_map_conditional
  // Conditionals within maps do not require else
  // e.g. { country=gb? return=this /country=us? return=that }
  : LCBRAC
        modl_condition_test QMARK modl_map_conditional_return
        (FSLASH modl_condition_test? QMARK
        modl_map_conditional_return )*
    RCBRAC
  ;
  modl_map_conditional_return
    : (modl_map_item )+
    ;
    modl_map_item
      : modl_pair | modl_map_conditional
      ;

modl_array_conditional
  // Conditionals within arrays do not require else
  // e.g. { country=gb? this /country=us? that }
  : LCBRAC
        modl_condition_test QMARK  modl_array_conditional_return
        (FSLASH  modl_condition_test? QMARK
        modl_array_conditional_return )*
    RCBRAC
  ;
  modl_array_conditional_return
    : (modl_array_item )+
    ;
    modl_array_item
      : modl_array_value_item | modl_array_conditional
      ;

modl_value_conditional
  // Conditionals within values DO require else
  // e.g. { country=gb? this /country=us? that /? other }
  : LCBRAC modl_condition_test QMARK (modl_value_conditional_return
        (FSLASH  modl_condition_test QMARK  modl_value_conditional_return )*
        (FSLASH  QMARK  modl_value_conditional_return))?
    RCBRAC
  ;
  modl_value_conditional_return
    : modl_value_item ( COLON modl_value_item ) *
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
  : STRING? modl_operator? modl_value (FSLASH modl_value )*
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
  | modl_primitive
;

modl_array_value_item
  : modl_map
  | modl_pair
  | modl_array
  | modl_primitive
;

modl_primitive
  : QUOTED
  | NUMBER
  | STRING
  | TRUE
  | FALSE
  | NULL
;

