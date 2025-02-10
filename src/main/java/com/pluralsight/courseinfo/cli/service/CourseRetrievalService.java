package com.pluralsight.courseinfo.cli.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class CourseRetrievalService {
    private static final String PS_URI = "https://app.pluralsight.com/profile/data/author/%s/all-content";
//    private static final HttpClient HTTP_CLIENT = HttpClient.newHttpClient();
    // to make it more Robust
    private static final HttpClient HTTP_CLIENT = HttpClient
            .newBuilder()
            .followRedirects(HttpClient.Redirect.ALWAYS)
            .build();

    public String getCoursesFor(String authorId)
    {
        HttpRequest request = HttpRequest
                .newBuilder(URI.create(String.format(PS_URI, authorId)))
                .GET()
                .build();
        try{
            HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
            return switch (response.statusCode()){
                case 200 -> response.body();
                case 404 -> "No courses found for author " + authorId;
                default -> throw new RuntimeException("Couldnot call Pluralsight API with status code: " + response.statusCode());
            };
        }catch (IOException | InterruptedException e){
            throw new RuntimeException("Couldnot call Pluralsight API ", e);
        }
    }

}
