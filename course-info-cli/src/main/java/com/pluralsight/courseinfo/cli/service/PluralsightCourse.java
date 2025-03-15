package com.pluralsight.courseinfo.cli.service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.Duration;
import java.time.LocalTime;

// if the json deserialize data doesnt exactly match it will give error so to avoid it we add this annotation to match the sufficient feilds that we mentiona nd rest of data ignored that has returned
@JsonIgnoreProperties(ignoreUnknown = true)
public record PluralsightCourse(String id, String title, String duration,String contentUrl, boolean isRetired) {
    // duration = "00:05:37"
    public long durationInMinutes(){
        return Duration.between(LocalTime.MIN, LocalTime.parse(duration())).toMinutes();
    }
}
