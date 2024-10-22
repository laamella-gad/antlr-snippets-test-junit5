package com.laamella.antlr_grammar_snippet_test;

import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Parser;

@FunctionalInterface
public interface ParserFactory<P extends Parser> {
    P create(CommonTokenStream tokenStream);
}
