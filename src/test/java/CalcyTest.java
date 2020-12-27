import antlr_grammar_snippet_test.*;
import com.laamella.calcy.CalcyLexer;
import com.laamella.calcy.CalcyParser;
import com.laamella.snippets_test_junit5.BasePathBuilder;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.io.IOException;
import java.util.stream.Stream;

public class CalcyTest {

    @TestFactory
    Stream<DynamicTest> calcy() throws IOException {
        return javaTestFactory()
                .stream("calcy", CalcyParser::calc);
    }

    private AntlrGrammarTestFactory<CalcyLexer, CalcyParser> javaTestFactory() {
        return new AntlrGrammarTestFactory<>(
                CalcyLexer::new,
                CalcyParser::new,
                BasePathBuilder.fromMavenModuleRoot(CalcyTest.class)
                        .inSrcTestResources()
                        .inSubDirectory("snippets")
                        .build(),
                path -> path.toString().endsWith(".calcy"),
                new ErrorsPrinter<>(),
                new ParseTreeLispPrinter<>(),
                new ParseTreePrettyPrinter<>("  "),
                new TokensPrinter<>(false));
    }
}
