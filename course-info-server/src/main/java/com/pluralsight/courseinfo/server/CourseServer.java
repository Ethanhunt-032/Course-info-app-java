package com.pluralsight.courseinfo.server;

import com.pluralsight.courseinfo.repository.CourseRepository;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

import javax.imageio.IIOException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Properties;
import java.util.logging.LogManager;

public class CourseServer {
    static {
        LogManager.getLogManager().reset();
//        install some hooks to JDK logger to redirect to slf4j logger
        SLF4JBridgeHandler.install();
    }

    private static final Logger LOG = LoggerFactory.getLogger(CourseServer.class);
    private static final String BASE_URI = "http://localhost:8080/";

    public static void main(String... args) {
        String databaseFilename =  loadDatabaseFilename();
        LOG.info("Starting HTTP server with datbase {}", databaseFilename);

        CourseRepository courseRepository = CourseRepository.openCourseRepository(databaseFilename);

//        CourseRepository courseRepository = CourseRepository.openCourseRepository("./courses.db");

//        to initialize jersey jaxrs implimentation we need resourceConfig
        ResourceConfig config = new ResourceConfig().register(new CourseResource(courseRepository));

//        Running Jaxrs Api on Jersey implementation
        GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI),config);
    }

    private static String loadDatabaseFilename() {
        // we r opening Server properties as resources
        try (InputStream propertyStream = CourseServer.class.getResourceAsStream("/server.properties"))
        {
            Properties props = new Properties();
    //        load that takes Input Stream
            props.load(propertyStream);
            return props.getProperty("course-info.database");
        } catch (IOException e){
            throw new IllegalStateException("Could not load database filename");
        }
    }
}
