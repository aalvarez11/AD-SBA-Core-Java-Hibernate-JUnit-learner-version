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
class StudentServiceTest {

    static StudentService studentService;

    @BeforeAll
    static void beforeAll() {
        studentService = new StudentService();
        CommandLine.addData();
    }

    @Test
    @Order(1)
    void getAllStudents() {
        List<Student> expected = new ArrayList<>(Arrays.asList(
                new Student("reema@gmail.com", "reema brown", "password"),
                new Student("annette@gmail.com", "annette allen", "password"),
                new Student("anthony@gmail.com", "anthony gallegos", "password"),
                new Student("ariadna@gmail.com", "ariadna ramirez", "password"),
                new Student("bolaji@gmail.com", "bolaji saibu", "password")
        ));

        assertThat(studentService.getAllStudents()).hasSameElementsAs(expected);
    }

    @Test
    @Order(2)
    void createStudent() {
        studentService.createStudent(new Student("aalvarez@gmail.com", "alejandro alvarez", "password"));

        assertThat(studentService.getAllStudents()).isNotEmpty().hasSize(6);
    }

    @Test
    @Order(3)
    void getStudentByEmail() {
        assertThat(studentService.getStudentByEmail("annette@gmail.com")).extracting(Student::getName).isEqualTo("annette allen");
        assertThat(studentService.getStudentByEmail("reema@gmail.com")).extracting(Student::getName).isEqualTo("reema brown");
        assertThat(studentService.getStudentByEmail("bolaji@gmail.com")).extracting(Student::getPassword).isEqualTo("password");
    }

    @Test
    @Order(4)
    void validateStudent() {
        assertThat(studentService.validateStudent("anthony@gmail.com", "password")).isTrue();
        assertThat(studentService.validateStudent("ariadna@gmail.com", "PASSw0rd")).isFalse();
    }
}