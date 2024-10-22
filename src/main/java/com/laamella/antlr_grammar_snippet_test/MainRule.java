package com.laamella.antlr_grammar_snippet_test;

import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.tree.ParseTree;

@FunctionalInterface
public interface MainRule<P extends Parser> {
    ParseTree invoke(P parser);
}
