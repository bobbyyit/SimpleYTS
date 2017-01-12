package com.atgtil.simpleyts.support;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;

public class ContentOf {

    public static String resource(String name) {
        return resourceAsStream(name).collect(joining("\n"));
    }

    public static Stream<String> resourceAsStream(String name) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(ContentOf.class.getClassLoader().getResourceAsStream(name)));
            return reader.lines();
        } catch (Exception e) {
            throw new InternalError(e);
        }
    }
}
