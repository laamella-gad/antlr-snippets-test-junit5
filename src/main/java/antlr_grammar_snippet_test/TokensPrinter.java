package antlr_grammar_snippet_test;

import com.laamella.snippets_test_junit5.ActualGenerator;
import org.antlr.v4.runtime.CommonToken;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Parser;

import static java.util.stream.Collectors.joining;
import static org.antlr.v4.runtime.Token.DEFAULT_CHANNEL;

public class TokensPrinter<L extends Lexer, P extends Parser> implements ActualGenerator<GrammarTestCase<L, P>> {
    private final boolean printHiddenTokens;

    public TokensPrinter(boolean printHiddenTokens) {
        this.printHiddenTokens = printHiddenTokens;
    }

    @Override
    public String generate(String testCaseText, GrammarTestCase<L, P> testCase) {
        L lexer = testCase.getLexer();
        return testCase.getTokens().stream()
                .filter(token -> printHiddenTokens || token.getChannel() == DEFAULT_CHANNEL)
                .map(token -> ((CommonToken)token).toString(lexer).trim() + "\n")
                .collect(joining());
    }
}
