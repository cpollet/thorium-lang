package org.thoriumlang.test.assertion;

import org.antlr.v4.runtime.RuleContext;
import org.thoriumlang.antlr.ThoriumParser;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static com.google.common.truth.Truth.assertThat;

/**
 * Created by cpollet on 03.06.17.
 */
public class ParseTree {
    private final ThoriumParser parser;
    private final String code;
    private Method method;

    ParseTree(ThoriumParser parser, String code) {
        this.parser = parser;
        this.code = code;
    }

    public void producesSyntaxTree(String expected) {
        try {
            RuleContext ruleContext = (RuleContext) method.invoke(parser);
            String actual = ruleContext.toStringTree(parser);

            assertThat(actual)
                    .named(code)
                    .isEqualTo(expected);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public ParseTree from(String root) {
        try {
            method = parser.getClass().getMethod(root);
            return this;
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException(root + " is not a valid root.");
        }
    }
}
