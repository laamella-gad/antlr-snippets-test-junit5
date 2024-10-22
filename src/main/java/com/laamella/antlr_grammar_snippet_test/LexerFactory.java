package com.laamella.antlr_grammar_snippet_test;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Lexer;

@FunctionalInterface
public interface LexerFactory<L extends Lexer> {
    L create(CharStream charStream);
}
