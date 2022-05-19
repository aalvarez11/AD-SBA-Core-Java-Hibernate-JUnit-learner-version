package sba.sms.services;

import jakarta.persistence.NamedNativeQuery;
import lombok.extern.java.Log;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import sba.sms.dao.StudentI;
import sba.sms.models.Course;
import sba.sms.models.Student;
import sba.sms.utils.HibernateUtil;

import java.util.List;

@NamedNativeQuery(
        name = "getStudentCourses",
        query = "select c.id, c.name, c.instructor from student s " +
                "join student_courses sc on s.email = sc.student_email " +
                "join course c on c.id = sc.course_id " +
                "where s.email = :email")

@Log
public class StudentService implements StudentI {

    @Override
    public List<Student> getAllStudents() {
        //start a session
        Session s = HibernateUtil.getSessionFactory().openSession();

        try {
            //use a query to get all students from the db, and return the list
            return s.createQuery("from Student", Student.class).list();
        } catch (HibernateException e) {
            //show the stack trace for the exception thrown
            e.printStackTrace();
        } finally {
            //close the session
            s.close();
        }
        //if the try fails, a null list is returned
        return null;
    }

    @Override
    public void createStudent(Student student) {
        //start a session and prepare a transaction
        Session s = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;

        try {
            //move a student instance to managed state and commit (save) to the db
            tx = s.beginTransaction();
            s.persist(student);
            tx.commit();
        } catch (HibernateException e) {
            //rollback if an exception occurs (do not save student to db)
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            //close the session
            s.close();
        }
    }

    @Override
    public Student getStudentByEmail(String email) {
        //start a session
        Session s = HibernateUtil.getSessionFactory().openSession();

        try {
            //try finding the student in the db based on id (email)
            Student st = s.get(Student.class, email);
            if (st == null) {
                //student wasn't found
                throw new HibernateException("Did not find student");
            } else {
                //return the student instance
                return st;
            }
        } catch (HibernateException e) {
            //show the stack trace for the exception thrown
            e.printStackTrace();
        } finally {
            //close the session
            s.close();
        }
        //if the try fails, a default/null Student is returned
        return new Student();
    }

    @Override
    public boolean validateStudent(String email, String password) {
        try {
            //use the previous method to get the student using the passed email
            Student st = getStudentByEmail(email);
            //check that the desired student was returned by the db & that passwords match
            if (st != null && st.getPassword().equals(password)) {
                return true;
            }
        } catch (HibernateException e) {
            //show the stack trace for the exception thrown
            e.printStackTrace();
        }
        //if the try fails/completes past the if, student was not valid
        return false;
    }

    @Override
    public void registerStudentToCourse(String email, int courseId) {
        //start a session and prepare a transaction
        Session s = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;

        try {
            tx = s.beginTransaction();
            //fetch the course and student from the db
            Course c = s.get(Course.class, courseId);
            Student st = s.get(Student.class, email);
            //add the course to the student's courses list
            //this should also add the student to the course's students list
            st.addCourse(c);
            //commit (save) to the db
            s.merge(st);
            tx.commit();
        } catch (HibernateException e) {
            //rollback if an exception occurs (do not save changes to db)
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            //close the session
            s.close();
        }
    }

    @Override
    public List<Course> getStudentCourses(String email) {
        //start a session, prepare a transaction and make a list for storing db data
        Session s = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        List<Course> courseList = null;

        try {
            tx = s.beginTransaction();
            //use Native query
            Query q = s.createNamedQuery("getStudentCourses", Course.class);
            q.setParameter("email", email);
            courseList = q.getResultList();
            tx.commit();
        } catch (HibernateException e) {
            //rollback if an exception occurs (do not save changes to db)
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            //close the session
            s.close();
        }

        return courseList;
    }
}
