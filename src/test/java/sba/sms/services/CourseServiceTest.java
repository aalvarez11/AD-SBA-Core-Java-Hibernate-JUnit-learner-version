package sba.sms.services;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.*;
import sba.sms.models.Course;
import sba.sms.models.Student;
import sba.sms.utils.CommandLine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@FieldDefaults(level = AccessLevel.PRIVATE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CourseServiceTest {

    static CourseService courseService;

    @BeforeAll
    static void beforeAll() {
        courseService = new CourseService();
        CommandLine.addData();
    }

    @Test
    @Order(1)
    void getAllCourses() {
        List<Course> expected = new ArrayList<>(Arrays.asList(
            new Course(1, "Java", "Phillip Witkin"),
            new Course(2, "Frontend", "Kasper Kain"),
            new Course(3, "JPA", "Jafer Alhaboubi"),
            new Course(4, "Spring Framework", "Phillip Witkin"),
            new Course(5, "SQL", "Phillip Witkin")
        ));

        assertThat(courseService.getAllCourses()).hasSameElementsAs(expected);
    }

    @Test
    @Order(2)
    void CreateCourse() {
        courseService.createCourse(new Course("Test", "Admin"));

        assertThat(courseService.getAllCourses()).isNotEmpty().hasSize(6);
    }

    @Test
    @Order(3)
    void GetCourseById() {
        assertThat(courseService.getCourseById(6)).isEqualTo(new Course(6, "Test", "Admin"));
        assertThat(courseService.getCourseById(3)).isEqualTo(new Course(3, "JPA", "Jafer Alhaboubi"));
        assertThat(courseService.getCourseById(4)).extracting(Course::getInstructor).isEqualTo("Phillip Witkin");
        assertThat(courseService.getCourseById(4)).extracting(Course::getName).isEqualTo("Spring Framework");
    }
}
