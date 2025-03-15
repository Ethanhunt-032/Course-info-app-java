package com.pluralsight.courseinfo.repository;

import com.pluralsight.courseinfo.domain.Course;
import com.sun.jdi.PrimitiveValue;
import org.h2.jdbcx.JdbcDataSource;

import javax.sql.DataSource;
import javax.swing.plaf.nimbus.State;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

// we shouldn't make it public as it should only be accessed within this package and only through Interface("CourseRepository")
class CourseJdbcRepository implements CourseRepository {
//    Unsupported connection setting "AUTO_SERVICE"
//    private static final String H2_DATABASE_URL = "jdbc:h2:file:%s;AUTO_SERVICE=TRUE;INIT=RUNSCRIPT FROM './db_init.sql'";
//    This eradicate the error caused above
    private static final String H2_DATABASE_URL = "jdbc:h2:file:%s;INIT=RUNSCRIPT FROM './db_init.sql'";
//    private static final String H2_DATABASE_URL = "jdbc:h2:file:%s;AUTO_SERVER=TRUE;INIT=RUNSCRIPT FROM './db_init.sql'";

    private final DataSource dataSource;

    private static final String INSERT_COURSE = """
                                               MERGE INTO Courses (id, name, length, url)
                                               VALUES (?, ?, ?, ?)
                                               """;
    private static final String ADD_NOTES = """
            UPDATE Courses SET notes = ?
            WHERE id = ?
            """;

    public CourseJdbcRepository(String databaseFile){
        JdbcDataSource jdbcDataSource = new JdbcDataSource();
        jdbcDataSource.setURL(H2_DATABASE_URL.formatted(databaseFile));
        this.dataSource = jdbcDataSource;
    }
    @Override
    public void saveCourse(Course course) {
//        connecting to our H2 DB through JDBC and we need to close the resource after try block so we include in try()
        try( Connection connection = dataSource.getConnection()){
//            these are statements which can hold placeholders
            PreparedStatement statement = connection.prepareStatement(INSERT_COURSE);
            statement.setString(1, course.id());
            statement.setString(2, course.name());
            statement.setLong(3,course.length());
            statement.setString(4, course.url());
            statement.execute();
        }
        catch (SQLException e){
            throw new RepositoryException("Failed to save " + course, e);
        }
    }

    @Override
    public List<Course> getAllCourses() {
        try(Connection connection = dataSource.getConnection())
        {
//            these r simple statements
            Statement statement = connection.createStatement();
//            we can invoke executeQuery  which execute SQL queries
//            since its sql query we get o/p in form of ResultSet

            ResultSet resultSet = statement.executeQuery("SELECT * FROM COURSES");
            List<Course> courses = new ArrayList<>();
            while(resultSet.next()){
                courses.add(new Course(resultSet.getString(1),
                        resultSet.getString(2),
                        resultSet.getLong(3),
                        resultSet.getString(4),
                        Optional.ofNullable(resultSet.getString(5))));
            }
            // returned data shouldn't be modified by caller/client
            return Collections.unmodifiableList(courses);
        }
        catch (SQLException e){
            throw new RepositoryException("Failed to retrieve courses", e);
        }
    }

    @Override
    public void addNotes(String id, String notes) {
        try( Connection connection = dataSource.getConnection()){
//            these are statements which can hold placeholders
            PreparedStatement statement = connection.prepareStatement(ADD_NOTES);
            statement.setString(1, notes);
            statement.setString(2, id);
            statement.execute();
        }
        catch (SQLException e){
            throw new RepositoryException("Failed to Add Notes " + id , e);
        }
    }
}
