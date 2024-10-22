package com.laamella.antlr_grammar_snippet_test;

import com.laamella.snippets_test_junit5.*;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.ArrayList;
import java.util.List;

import static org.antlr.v4.runtime.atn.PredictionMode.LL_EXACT_AMBIG_DETECTION;

/**
 * Works together with JUnit5's @{@link org.junit.jupiter.api.TestFactory} to generate testcases
 * for all the Antlr grammar test case snippets in a directory.
 *
 * @param <L> the lexer you want to test
 * @param <P> the parser you want to test.
 */
public class AntlrGrammarTestFactory<L extends Lexer, P extends Parser> extends SnippetTestFactory<GrammarTestCase<L, P>> {
	/**
	 * @param lexerFactory           creates a new lexer for your grammar
	 * @param parserFactory          creates a new parser for your grammar
	 * @param basePath               sets the base path. Base path + testCasesDirectory = where the test case snippets are located. Subdirectories are included.
	 * @param testCaseFilenameFilter when only snippets are in the indicated directory, "path -> true"  is enough. Otherwise use something like "path -> path.toString().endsWith(".java")"
	 * @param mainRule               the main parse rule to invoke (like FortranParser::forLoop)
	 * @param actualGenerators       the printers that create an "actual" to test against. {@link ParseTreePrettyPrinter} and {@link ErrorsPrinter} are recommended.
	 */
	@SafeVarargs
	public AntlrGrammarTestFactory(
			LexerFactory<L> lexerFactory,
			ParserFactory<P> parserFactory,
			String blockCommentOpen,
			String blockCommentClose,
			BasePath basePath,
			TestCaseFilenameFilter testCaseFilenameFilter,
			MainRule<P> mainRule,
			ActualGenerator<GrammarTestCase<L, P>>... actualGenerators) {
		super(
				new SnippetFileFormat(
						blockCommentOpen,
						blockCommentClose + "\n",
						"\n" + blockCommentOpen + " expected:\n",
						"\n---\n",
						blockCommentClose),
				basePath,
				testCaseFilenameFilter,
				testCaseText -> preprocess(testCaseText, mainRule, lexerFactory, parserFactory),
				actualGenerators);
	}

	private static <L extends Lexer, P extends Parser> GrammarTestCase<L, P> preprocess(
			String testCaseText,
			MainRule<P> mainRule,
			LexerFactory<L> lexerFactory,
			ParserFactory<P> parserFactory) {
		List<String> errors = new ArrayList<>();
		L lexer = lexerFactory.create(CharStreams.fromString(testCaseText));
		collectErrorAndWarningMessagesInList(lexer, errors);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		tokens.fill();
		List<Token> tokenList = tokens.getTokens();
		P parser = parserFactory.create(tokens);
		collectErrorAndWarningMessagesInList(parser, errors);
		parser.getInterpreter().setPredictionMode(LL_EXACT_AMBIG_DETECTION);
		ParseTree tree = mainRule.invoke(parser);
		return new GrammarTestCase<>(lexer, parser, tokenList, tree, errors);
	}

	private static void collectErrorAndWarningMessagesInList(Recognizer<?, ?> recognizer, List<String> errors) {
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
