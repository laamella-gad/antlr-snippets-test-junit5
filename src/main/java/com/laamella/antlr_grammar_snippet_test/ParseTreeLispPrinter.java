package com.laamella.antlr_grammar_snippet_test;

import com.laamella.snippets_test_junit5.snippet.ActualGenerator;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Parser;

/**
 * Antlr's built-in parse tree printer.
 */
public class ParseTreeLispPrinter<L extends Lexer, P extends Parser> implements ActualGenerator<GrammarTestCase<L, P>> {
    @Override
    public String generate(String testCaseText, GrammarTestCase<L, P> testCase) {
        return testCase.getParseTree().toStringTree(testCase.getParser());
    }
}
