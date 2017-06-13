package org.thoriumlang.test.assertion;

import org.antlr.v4.runtime.ANTLRErrorListener;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.RuleContext;
import org.antlr.v4.runtime.atn.ATNConfigSet;
import org.antlr.v4.runtime.dfa.DFA;
import org.thoriumlang.antlr.ThoriumParser;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import static com.google.common.truth.Truth.assertThat;

/**
 * Created by cpollet on 03.06.17.
 */
public class ParseTree {
    private final ThoriumParser parser;
    private final String code;
    private final List<String> errors;
    private Method method;

    ParseTree(ThoriumParser parser, String code) {
        this.parser = parser;
        this.code = code;
        this.errors = new ArrayList<>();

        parser.addErrorListener(new ANTLRErrorListener() {
            @Override
            public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
                errors.add(msg);
            }

            @Override
            public void reportAmbiguity(Parser recognizer, DFA dfa, int startIndex, int stopIndex, boolean exact, BitSet ambigAlts, ATNConfigSet configs) {
                // nothing
            }

            @Override
            public void reportAttemptingFullContext(Parser recognizer, DFA dfa, int startIndex, int stopIndex, BitSet conflictingAlts, ATNConfigSet configs) {
                // nothing
            }

            @Override
            public void reportContextSensitivity(Parser recognizer, DFA dfa, int startIndex, int stopIndex, int prediction, ATNConfigSet configs) {
                // nothing
            }
        });
    }

    public ParseTree producesSyntaxTree(String expected) {
        try {
            RuleContext ruleContext = (RuleContext) method.invoke(parser);
            String actual = ruleContext.toStringTree(parser);

            assertThat(actual)
                    .named(code)
                    .isEqualTo(expected);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }

        return this;
    }

    public ParseTree from(String root) {
        try {
            method = parser.getClass().getMethod(root);
            return this;
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException(root + " is not a valid root.");
        }
    }

    public void hasErrors(List<String> errors) {
        assertThat(this.errors)
                .containsExactlyElementsIn(errors);
    }
}
