package com.pluralsight.courseinfo.cli;
// STRUCTURE OF PACKAGE
// company/Org i.e Group ID -> com.pluralsight
// Application/Project i.e Artifact ID -> courseinfo
// components -> cli

import com.pluralsight.courseinfo.cli.service.CourseRetrievalService;
import com.pluralsight.courseinfo.cli.service.CourseStorageService;
import com.pluralsight.courseinfo.cli.service.PluralsightCourse;
import com.pluralsight.courseinfo.repository.CourseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.function.Predicate;

import static java.util.function.Predicate.not;

public class CourseRetriever {
    // setting up a logger to print the Logs / statements
    private static final Logger LOG = LoggerFactory.getLogger(CourseRetriever.class);


    public static void main(String args[]){
        LOG.info("CourseRetriever Starting");

        if(args.length == 0){
//            System.out.println("Please provide an author name as 1st argument");
            LOG.warn("Please provide an author name as 1st argument");
            return;
        }
        try{
            retrieveCourses(args[0]);
//            PluralsightCourse course = new PluralsightCourse("teb","titel","egjn","edhbv",true);
//            LOG.info("course : {}", course);
        }
        catch (Exception e){
//            System.out.println("Error: " + e.getMessage());
//            e.printStackTrace();
            // error log can print the error msg and throwable aswell
            LOG.error("Error: " + e.getMessage(), e);
        }
    }
    private static void retrieveCourses(String authorId){
        //System.out.println("Retrieving Courses for author " + authorId);
        //LOG info also has placeholder method instead of concatinating strings we can put curly braces
        LOG.info("Retrieving Courses for author {}" ,authorId);

        CourseRetrievalService courseRetrievalService = new CourseRetrievalService();
        CourseRepository courseRepository = CourseRepository.openCourseRepository("./courses.db");
        CourseStorageService courseStorageService = new CourseStorageService(courseRepository);

        List<PluralsightCourse> coursesTOStore = courseRetrievalService.getCoursesFor(authorId)
                .stream()
                .filter(not(PluralsightCourse::isRetired)) // .filter(course -> !course.isRetired())
                .toList();

        LOG.info("Retrieveing the  following {} courses {}", coursesTOStore.size(), coursesTOStore);
        courseStorageService.storePluralsightCourses(coursesTOStore);
        LOG.info("Courses store successfully");
    }
}
