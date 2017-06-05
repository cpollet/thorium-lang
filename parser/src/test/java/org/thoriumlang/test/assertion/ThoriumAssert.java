package org.thoriumlang.test.assertion;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.thoriumlang.antlr.ThoriumLexer;
import org.thoriumlang.antlr.ThoriumParser;

/**
 * Created by cpollet on 03.06.17.
 */
public class ThoriumAssert {
    public static ParseTree assertThat(String code) {
        ANTLRInputStream input = new ANTLRInputStream(code);
        ThoriumLexer lexer = new ThoriumLexer(input);
        lexer.removeErrorListeners();

        CommonTokenStream tokens = new CommonTokenStream(lexer);

        ThoriumParser parser = new ThoriumParser(tokens);

        return new ParseTree(parser, code);
    }
}
