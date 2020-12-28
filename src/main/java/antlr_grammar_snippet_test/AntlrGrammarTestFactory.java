package antlr_grammar_snippet_test;

import com.laamella.snippets_test_junit5.ActualGenerator;
import com.laamella.snippets_test_junit5.BasePath;
import com.laamella.snippets_test_junit5.SnippetTestFactory;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.jupiter.api.DynamicTest;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;
import static org.antlr.v4.runtime.atn.PredictionMode.LL_EXACT_AMBIG_DETECTION;

/**
 * Works together with JUnit5's @{@link org.junit.jupiter.api.TestFactory} to generate testcases
 * for all the Antlr grammar test case snippets in a directory.
 *
 * @param <L> the lexer you want to test
 * @param <P> the parser you want to test.
 */
public class AntlrGrammarTestFactory<L extends Lexer, P extends Parser> {
    private final Function<CharStream, L> lexerFactory;
    private final Function<CommonTokenStream, P> parserFactory;
    private final ActualGenerator<GrammarTestCase<L, P>>[] actualGenerators;
    private final BasePath basePath;
    private final Predicate<Path> testcaseFilenameFilter;

    /**
     * @param lexerFactory           creates a new lexer for your grammar
     * @param parserFactory          creates a new parser for your grammar
     * @param basePath               sets the base path. Base path + testCasesDirectory = where the test case snippets are located. Subdirectories are included.
     * @param testcaseFilenameFilter when only snippets are in the indicated directory, "path -> true"  is enough. Otherwise use something like "path -> path.toString().endsWith(".java")"
     * @param actualGenerators       the printers that create an "actual" to test against. {@link ParseTreePrettyPrinter} and {@link ErrorsPrinter} are recommended.
     */
    @SafeVarargs
    public AntlrGrammarTestFactory(
            Function<CharStream, L> lexerFactory,
            Function<CommonTokenStream, P> parserFactory,
            BasePath basePath,
            Predicate<Path> testcaseFilenameFilter,
            ActualGenerator<GrammarTestCase<L, P>>... actualGenerators) {
        this.lexerFactory = requireNonNull(lexerFactory);
        this.parserFactory = requireNonNull(parserFactory);
        this.basePath = requireNonNull(basePath);
        this.testcaseFilenameFilter = requireNonNull(testcaseFilenameFilter);
        this.actualGenerators = requireNonNull(actualGenerators);
    }

    public Stream<DynamicTest> stream(String testCasesDirectory, Function<P, ParseTree> mainRuleInvoker) throws IOException {
        return stream(testCasesDirectory, mainRuleInvoker, false);
    }

    public Stream<DynamicTest> regenerate(String testCasesDirectory, Function<P, ParseTree> mainRuleInvoker) throws IOException {
        return stream(testCasesDirectory, mainRuleInvoker, true);
    }

    /**
     * @param testCasesDirectory where to look for snippet files (basepath +  testCasesDirectory)
     * @param mainRuleInvoker    should invoke the correct grammar rule for the test case.
     * @return a stream of testcases to return from a JUnit5 @{@link org.junit.jupiter.api.TestFactory}
     */
    public Stream<DynamicTest> stream(String testCasesDirectory, Function<P, ParseTree> mainRuleInvoker, boolean regenerate) throws IOException {
        requireNonNull(testCasesDirectory);
        requireNonNull(mainRuleInvoker);
        return new SnippetTestFactory<>(
                basePath,
                testcaseFilenameFilter,
                testCaseText -> preprocess(testCaseText, mainRuleInvoker),
                "/*",
                "*/\n",
                "\n/* expected:\n",
                "\n---\n",
                "*/",
                actualGenerators)
                .stream(testCasesDirectory, regenerate);
    }

    private GrammarTestCase<L, P> preprocess(String testCaseText, Function<P, ParseTree> mainRuleInvoker) {
        List<String> errors = new ArrayList<>();
        L lexer = lexerFactory.apply(CharStreams.fromString(testCaseText));
        collectErrorAndWarningMessagesInList(lexer, errors);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        tokens.fill();
        List<Token> tokenList = tokens.getTokens();
        P parser = parserFactory.apply(tokens);
        collectErrorAndWarningMessagesInList(parser, errors);
        parser.getInterpreter().setPredictionMode(LL_EXACT_AMBIG_DETECTION);
        ParseTree tree = mainRuleInvoker.apply(parser);
        return new GrammarTestCase<>(lexer, parser, tokenList, tree, errors);
    }

    private void collectErrorAndWarningMessagesInList(Recognizer<?, ?> recognizer, List<String> errors) {
        recognizer.removeErrorListeners();
        recognizer.addErrorListener(new DiagnosticErrorListener(true));
        recognizer.addErrorListener(new BaseErrorListener() {
            @Override
            public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
                errors.add("line: " + line + ", " +
                        "offset: " + charPositionInLine + ", " +
                        (offendingSymbol != null ? "symbol:" + offendingSymbol + " " : "") +
                        msg);
            }
        });
    }
}
