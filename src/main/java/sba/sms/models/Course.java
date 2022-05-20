package sba.sms.models;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.*;

@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Course {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    int id;

    @NonNull
    @Column(length = 50)
    String name;

    @NonNull
    @Column(length = 50)
    String instructor;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @ManyToMany(mappedBy = "courses",
                cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH},
                fetch = FetchType.EAGER)
    List<Student> students = new ArrayList<>();

    //constructor used in testing
    public Course (int id, String name, String instructor) {
        this.id = id;
        this.name = name;
        this.instructor =instructor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return id == course.id && name.equals(course.name) && instructor.equals(course.instructor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, instructor);
    }

    public void addStudent(Student st) {
        //create a set from the course's current list of students
        Set<Student> studentSet = new HashSet<>(this.students);

        //add the student, Set will make sure duplicates aren't added
        studentSet.add(st);

        //now convert the set back into a list and update the course's student list
        this.students = new ArrayList<>(studentSet);
    }
}
