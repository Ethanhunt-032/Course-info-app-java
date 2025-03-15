package com.pluralsight.courseinfo.cli.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class PluralsightCourseTest {

    @Test
    void durationInMinutes() {
        PluralsightCourse course =  new PluralsightCourse("teb","titel","00:05:34","edhbv",false);
        assertEquals(5,course.durationInMinutes());
    }

    @ParameterizedTest
    @CsvSource(textBlock = """
            00:00:00,0
            02:52:09.6531,172
            12:00:00,720
            """)
    void durationInMinutes_withDuration(String duration, int expectedMinutes){
        PluralsightCourse course =  new PluralsightCourse("teb","titel",duration,"edhbv",false);
        assertEquals(expectedMinutes,course.durationInMinutes());
    }
    
    // to run test using MAVEN  - Cmd : mvn clean test
}