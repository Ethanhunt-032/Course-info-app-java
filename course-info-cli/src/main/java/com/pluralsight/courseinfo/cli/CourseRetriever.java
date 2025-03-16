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
    private static final Logger LOG = LoggerFactory.getLogger(CourseRetriever.class);


    public static void main(String args[]){
        LOG.info("CourseRetriever Starting");

        if(args.length == 0){
            LOG.warn("Please provide an author name as 1st argument");
            return;
        }
        try{
            retrieveCourses(args[0]);
        }
        catch (Exception e){
            LOG.error("Error: " + e.getMessage(), e);
        }
    }
    private static void retrieveCourses(String authorId){
        LOG.info("Retrieving Courses for author {}" ,authorId);

        CourseRetrievalService courseRetrievalService = new CourseRetrievalService();
        CourseRepository courseRepository = CourseRepository.openCourseRepository("./courses.db");
        CourseStorageService courseStorageService = new CourseStorageService(courseRepository);

        List<PluralsightCourse> coursesTOStore = courseRetrievalService.getCoursesFor(authorId)
                .stream()
                .filter(not(PluralsightCourse::isRetired))
                .toList();

        LOG.info("Retrieveing the  following {} courses {}", coursesTOStore.size(), coursesTOStore);
        courseStorageService.storePluralsightCourses(coursesTOStore);
        LOG.info("Courses store successfully");
    }
}
