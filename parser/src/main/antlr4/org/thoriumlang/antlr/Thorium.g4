/*
 * Copyright 2015 Christophe Pollet
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this inputStream except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/** A thorium grammar for ANTLR v4
 *
 *  You can test with
 *
 *  $ antlr4 Thorium.g4
 *  $ javac *.java
 *  $ grun Thorium compilationUnit *.th
 */
grammar Thorium;

TYPE    : 'type';
DEFINE  : 'define';
EXTENDS : 'extends';
ALIAS   : 'alias';

// starting point for parsing a thorium inputStream
compilationUnit
    : (typeDeclaration | classDeclaration)* EOF
    ;

typeDeclaration
    : TYPE UCFirstIdentifier ('<' typeList '>')? ALIAS anyType
    | TYPE valueType (EXTENDS typeList)? ':' methodDeclarationList
    ;

/**
 * Type
 */
anyType
    : valueType
    | functionType
    ;
valueType
    : '(' valueType ')'
    | UCFirstIdentifier ('<' typeList '>')?
    ;
functionType
    : '(' functionType ')'
    | '(' functionType ')' '->' valueType
    | '(' functionType ')' '->' '(' functionType ')'
    | valueType '->' valueType
    | valueType '->' '(' functionType ')'
    | '(' typeList? ')' '->' valueType
    | '(' typeList? ')' '->' '(' functionType ')'
    ;
typeList
    : anyType (',' anyType)*
    ;

/**
 * Methods declaration
 */
methodDeclarationList
    : methodDeclaration (',' methodDeclaration)*
    ;
methodDeclaration
    : LCFirstIdentifier '(' typeList? ')' '->' valueType
    | LCFirstIdentifier '(' typeList? ')' '->' '(' functionType ')'
    ;


classDeclaration
    : DEFINE UCFirstIdentifier ('<' typeList '>')? ':' typeList classDefaultConstructor? '{' methodImplementationList? '}'
    ;
classDefaultConstructor
    : '(' methodParameterList? ')'
    ;

methodParameterList
    : methodParameter (',' methodParameter)*
    ;
methodParameter
    : anyType LCFirstIdentifier
    ;

methodImplementationList
    : methodImplementation+
    ;
methodImplementation
    : LCFirstIdentifier '(' methodParameterList? ')' '->' methodBody
    ;
methodBody
    : '...'
    | '{' '}'
    ;

//typeDisjunction
//    : UCFirstIdentifier ('|' UCFirstIdentifier)*
//    ;
//typeConjunction
//    : UCFirstIdentifier ('&' UCFirstIdentifier)*
//    ;

fragment
Identifier
    : [a-zA-Z0-9_]+
    ;

UCFirstIdentifier
    : [A-Z] Identifier*
    ;

LCFirstIdentifier
    : [a-z] Identifier*
    ;

WS
    : [ \t\r\n\u000C]+ -> skip
    ;

LINE_COMMENT
    : '//' ~[\r\n]* -> skip
    ;