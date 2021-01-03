package com.laamella.antlr_grammar_snippet_test;

import com.laamella.calcy.CalcyLexer;
import com.laamella.calcy.CalcyParser;
import com.laamella.snippets_test_junit5.BasePath;
import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.io.IOException;
import java.util.function.Function;
import java.util.stream.Stream;

public class CalcyTest {

    @TestFactory
    Stream<DynamicTest> calcy() throws IOException {
        return javaTestFactory(CalcyParser::calc)
                .stream();
    }

    private AntlrGrammarTestFactory<CalcyLexer, CalcyParser> javaTestFactory(Function<CalcyParser, ParseTree> mainRuleInvoker) {
        return new AntlrGrammarTestFactory<>(
                CalcyLexer::new,
                CalcyParser::new,
                BasePath.fromMavenModuleRoot(CalcyTest.class)
                        .inSrcTestResources()
                        .inSubDirectory("snippets/calcy"),
                path -> path.toString().endsWith(".calcy"),
                mainRuleInvoker,
                new ErrorsPrinter<>(),
                new ParseTreeLispPrinter<>(),
                new ParseTreePrettyPrinter<>("  "),
                new TokensPrinter<>(false));
    }
}
