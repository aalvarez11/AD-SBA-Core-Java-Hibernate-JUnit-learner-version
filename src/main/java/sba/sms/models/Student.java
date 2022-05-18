package sba.sms.models;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.extern.java.Log;

import java.util.*;

@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Log
public class Student {

    @Id
    @Column(length = 50)
    String email;

    @NonNull
    @Column(length = 50, nullable = false)
    String name;

    @NonNull
    @Column(length = 50, nullable = false)
    String password;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH},
                fetch = FetchType.EAGER)
    @JoinTable(name = "student_courses",
            joinColumns = @JoinColumn(name = "student_email"),
            inverseJoinColumns = @JoinColumn(name = "courses_id"))
    List<Course> courses = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return email.equals(student.email) && name.equals(student.name) && password.equals(student.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, name, password);
    }

    public void addCourse(Course c) {
        //create a set from the student's current list
        Set<Course> cSet = new HashSet<>(this.courses);

        //add the course, Set will make sure duplicates aren't added
        cSet.add(c);
        c.addStudent(this);

        //now convert the set back into a list and update the student's list
        this.courses = new ArrayList<>(cSet);

    }
}
