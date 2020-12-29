package com.laamella.antlr_grammar_snippet_test;

import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.List;

/**
 * Information about the parsing process for one snippet test case,
 * that can be used by {@link com.laamella.snippets_test_junit5.ActualGenerator}s to produce their output.
 */
public class GrammarTestCase<L extends Lexer, P extends Parser> {
    private final L lexer;
    private final P parser;
    private final List<Token> tokens;
    private final ParseTree parseTree;
    private final List<String> errors;

    public GrammarTestCase(L lexer, P parser, List<Token> tokens, ParseTree parseTree, List<String> errors) {
        this.lexer = lexer;
        this.parser = parser;
        this.tokens = tokens;
        this.parseTree = parseTree;
        this.errors = errors;
    }

    public L getLexer() {
        return lexer;
    }

    public P getParser() {
        return parser;
    }

    public List<Token> getTokens() {
        return tokens;
    }

    public ParseTree getParseTree() {
        return parseTree;
    }

    public List<String> getErrors() {
        return errors;
    }
}
