package com.laamella.antlr_grammar_snippet_test;

import com.laamella.snippets_test_junit5.ActualGenerator;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Parser;

/**
 * Prints a list of error messages encountered during parsing.
 */
public class ErrorsPrinter<L extends Lexer, P extends Parser> implements ActualGenerator<GrammarTestCase<L, P>> {
    @Override
    public String generate(String testCaseText, GrammarTestCase<L, P> testCase) {
        if (testCase.getErrors().isEmpty()) {
            return "No errors.";
        }
        return String.join("\n", testCase.getErrors());
    }
}
