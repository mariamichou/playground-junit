package com.example.playground.colours;

import com.example.playground.helper.Utils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.*;

class ColourTest {

    @ParameterizedTest
    @CsvFileSource(resources = "/numbers.csv", numLinesToSkip = 1)
    void testsForSum(int a, int b, int expectedResult) {
        assertEquals(expectedResult, Math.addExact(a, b));
    }

    @ParameterizedTest
    @MethodSource("populateIntegers")
    void testsForSum_2(int a, int b, int expectedResult) {
        assertEquals(expectedResult, Math.addExact(a, b));
    }

    private static Stream<Arguments> populateIntegers() {
        return Stream.of(
                Arguments.of(0, 0, 0),
                Arguments.of(0, 1, 1),
                Arguments.of(1, 2, 3));
    }

    @ParameterizedTest
    @DisplayName("name should return a supported rainbow colour")
    @CsvSource(value = {"blue:BLUE", "BluE:BLUE", "green:GREEN"}, delimiter = ':')
    void testsFromName(String name, Colour expectedColour) {
        assertEquals(expectedColour, Colour.fromName(name));
    }

    @ParameterizedTest
    @DisplayName("name should return null if not supported rainbow colour")
    @ValueSource(strings = {"purple:", "\"\"", "null"})
    void testsFromNameResultsNull(String name) {
        assertNull(Colour.fromName(name));
    }

    @ParameterizedTest
    @DisplayName("order should match a supported rainbow colour")
    @CsvSource(value = {"1:ORANGE", "2:YELLOW"}, delimiter = ':')
    void testsFromOrder(Integer order, Colour expectedColour) {
        assertEquals(Colour.fromOrder(order), expectedColour);
    }

    @ParameterizedTest
    @DisplayName("unknown order should return null if not supported rainbow colour")
    @ValueSource(ints = {-1, 7})
    void testsFromUnknownOrderResultsNull(Integer order) {
        assertNull(Colour.fromOrder(order));
    }

    @Test
    void testsGetRainbowColours() {
        assertIterableEquals(Colour.getRainbowColours(), asList(Colour.values()));
    }

    @Test
    @DisplayName("get all rainbow colours, sorted by order ASC")
    void testsGetRainbowColoursSorted() {
        List<Colour> colours = Utils.toSorted(Colour.getRainbowColours(), Colour::getOrder);

        assertIterableEquals(asList(Colour.RED, Colour.ORANGE, Colour.YELLOW, Colour.GREEN, Colour.BLUE, Colour.INDIGO, Colour.VIOLET), colours);
    }

    @ParameterizedTest
    @DisplayName("populate colour order map, ordering existing colours by order, then ordering the rest by name ASC and incrementing index")
    @MethodSource("populateColourOrderMap")
    void populateColourOrderMap(List<String> colours, Map<String, Integer> expectedColourMap) {
        assertTrue(Utils.areEqual(Colour.populateColourOrder(colours), expectedColourMap, e -> e.getValue().equals(expectedColourMap.get(e.getKey()))));
    }

    private static Stream<Arguments> populateColourOrderMap() {
        return Stream.of(
                Arguments.of(
                        asList("blue", "green", "purple", "pink", "black"),
                        new TreeMap<>(Stream.of(new Object[][]{
                                {"green", 3},
                                {"blue", 4},
                                {"black", 7},
                                {"pink", 8},
                                {"purple", 9}
                        }).collect(Collectors.toMap(getKeyMapper(), getValueMapper())))),

                Arguments.of(
                        asList("purple", "pink", "black"),
                        new TreeMap<>(Stream.of(new Object[][]{
                                {"black", 7},
                                {"pink", 8},
                                {"purple", 9}
                        }).collect(Collectors.toMap(getKeyMapper(), getValueMapper())))),

                Arguments.of(
                        asList("pink", "black"),
                        new TreeMap<>(Stream.of(new Object[][]{
                                {"black", 7},
                                {"pink", 8}
                        }).collect(Collectors.toMap(getKeyMapper(), getValueMapper())))),

                Arguments.of(
                        singletonList("black"),
                        new TreeMap<>(Stream.of(new Object[][]{
                                {"black", 7}
                        }).collect(Collectors.toMap(getKeyMapper(), getValueMapper()))))
        );
    }

    private static Function<Object[], String> getKeyMapper() {
        return data -> (String) data[0];
    }

    private static Function<Object[], Integer> getValueMapper() {
        return data -> (Integer) data[1];
    }
}
