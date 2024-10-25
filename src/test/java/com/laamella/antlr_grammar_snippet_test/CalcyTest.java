package com.laamella.antlr_grammar_snippet_test;

import com.laamella.calcy.CalcyLexer;
import com.laamella.calcy.CalcyParser;
import com.laamella.snippets_test_junit5.BasePath;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.io.IOException;
import java.util.stream.Stream;

import static com.laamella.snippets_test_junit5.snippet.TestCaseFilenameFilter.filesWithExtension;

public class CalcyTest {

    @TestFactory
    Stream<DynamicTest> calcy() throws IOException {
        return javaTestFactory(CalcyParser::calc)
                .stream();
    }

    private AntlrGrammarTestFactory<CalcyLexer, CalcyParser> javaTestFactory(MainRule<CalcyParser> mainRule) {
        return new AntlrGrammarTestFactory<>(
                CalcyLexer::new,
                CalcyParser::new,
                "/*", "*/",
                BasePath.fromMavenModuleRoot(CalcyTest.class)
                        .inSrcTestResources()
                        .inSubDirectory("snippets/calcy"),
                filesWithExtension(".calcy"),
                mainRule,
                new ErrorsPrinter<>(),
                new ParseTreeLispPrinter<>(),
                new ParseTreePrettyPrinter<>("  "),
                new TokensPrinter<>(false));
    }
}
