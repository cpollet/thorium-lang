package org.thoriumlang.test.spec;

import lombok.Data;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Iterator;
import java.util.stream.Collectors;

/**
 * Created by cpollet on 03.06.17.
 */
public class Specs implements Iterable<Specs.Spec> {
    private final String filename;
    private String content;

    public Specs(String filename) {
        this.filename = filename;
    }

    @Override
    public Iterator<Spec> iterator() {
        if (content == null) {
            load();
        }

        int endOfFirstLine = content.indexOf("\n");
        final String root = content.substring(0, endOfFirstLine);

        String[] pairs = content.substring(endOfFirstLine).trim().split("\n\n");

        return Arrays.stream(pairs)
                .map(p -> p.split("\n"))
                .map(a -> new Spec(root, a[0], a[1]))
                .collect(Collectors.toList())
                .iterator();
    }

    private void load() {
        if (content == null) {
            InputStream inputStream = getClass().getResourceAsStream(filename);
            InputStreamReader reader = new InputStreamReader(inputStream);
            content = String.join("\n", new BufferedReader(reader).lines().collect(Collectors.toList()));
        }
    }

    @Data
    public static class Spec {
        private final String root;
        private final String code;
        private final String tree;
    }

}
