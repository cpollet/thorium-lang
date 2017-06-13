package org.thoriumlang;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.thoriumlang.test.assertion.ThoriumAssert;
import org.thoriumlang.test.spec.Specs;

import java.util.Arrays;
import java.util.Collection;

/**
 * Created by cpollet on 03.06.17.
 */
@RunWith(Parameterized.class)
@Slf4j
public class TestParse {
    private final String filename;

    public TestParse(String filename) {
        this.filename = filename;
    }

    @Parameterized.Parameters
    public static Collection<String[]> data() {
        return Arrays.asList(
                new String[]{"/specs/syntax/anyType.txt"},
                new String[]{"/specs/syntax/typeDeclaration.txt"},
                new String[]{"/specs/syntax/methodDeclaration.txt"}
        );
    }

    @Test
    public void testSpec() {
        log.info("Started executing {}", filename);

        Specs specs = new Specs(filename);

        for (Specs.Spec spec : specs) {
            log.info("Parsing {}", spec.getCode());
            ThoriumAssert
                    .assertThat(spec.getCode())
                    .from(spec.getRoot())
                    .producesSyntaxTree(spec.getTree())
                    .hasErrors(spec.getErrors());
        }
    }
}
