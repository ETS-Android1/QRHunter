package com.example.qrhunter;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TestScoringSystem {
    @Test
    public void test_empty_string_score_system() {
        float myScore = ScoringSystem.calculateScore("");
        assertEquals(myScore, 0f, 0.00000001);
    }

    @Test
    public void test__score_system() {
        String testString = "abbassd";
        float myScore = ScoringSystem.calculateScore(testString);
        float manualScore = 0;
        for (char el : testString.toCharArray()) {
            manualScore = manualScore + el;
        }
        assertEquals(myScore, manualScore, 0.00000001);
    }

}
