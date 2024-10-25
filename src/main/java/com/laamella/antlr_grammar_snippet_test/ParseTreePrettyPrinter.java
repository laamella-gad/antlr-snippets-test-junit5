package com.laamella.antlr_grammar_snippet_test;

import com.laamella.snippets_test_junit5.snippet.ActualGenerator;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.misc.Utils;
import org.antlr.v4.runtime.tree.Tree;
import org.antlr.v4.runtime.tree.Trees;

/**
 * Pretty print out a whole tree. {@link Trees#getNodeText} is used on the node payloads to get the
 * text for the nodes. (Derived from Trees.toStringTree(....))
 */
public class ParseTreePrettyPrinter<L extends Lexer, P extends Parser> implements ActualGenerator<GrammarTestCase<L, P>> {
    private final String indent;

    public ParseTreePrettyPrinter(String indent) {
        this.indent = indent;
    }

    @Override
    public String generate(String testCaseText, GrammarTestCase<L, P> testCase) {
        return print(testCase.getParseTree(), testCase.getParser());
    }

    private String print(Tree t, P parser) {
        return process(t, parser, 0)
                .replaceAll("(?m)^\\s+$", "")
                .replaceAll("\\r?\\n\\r?\\n", "\n")
                .trim();
    }

    private String process(Tree t, P parser, int level) {
        if (t.getChildCount() == 0) return Utils.escapeWhitespace(Trees.getNodeText(t, parser), false);
        StringBuilder sb = new StringBuilder();
        sb.append(makeIndent(level));
        String s = Utils.escapeWhitespace(Trees.getNodeText(t, parser), false);
        sb.append(s).append(' ');
        for (int i = 0; i < t.getChildCount(); i++) {
            if (i != 0) {
                sb.append(" ");
            }
            sb.append(process(t.getChild(i), parser, level + 1));
        }
        sb.append(makeIndent(level));
        return sb.toString();
    }

    private String makeIndent(int level) {
        StringBuilder sb = new StringBuilder();
        if (level > 0) {
            sb.append("\n");
            while (level > 0) {
                sb.append(indent);
                level--;
            }
        }
        return sb.toString();
    }
}