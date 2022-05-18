package sba.sms.services;


import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import sba.sms.dao.CourseI;
import sba.sms.models.Course;
import sba.sms.models.Student;
import sba.sms.utils.HibernateUtil;

import java.util.List;

public class CourseService implements CourseI {

    @Override
    public void createCourse(Course course) {
        //start a session and prepare a transaction
        Session s = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;

        try {
            //move a student instance to managed state and commit (save) to the db
            tx = s.beginTransaction();
            s.persist(course);
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
    public Course getCourseById(int courseId) {
        //start a session
        Session s = HibernateUtil.getSessionFactory().openSession();

        try {
            //try finding the course in the db based on id (courseId)
            Course c = s.get(Course.class, courseId);
            if (c == null) {
                //course wasn't found
                throw new HibernateException("Did not find course");
            } else {
                //return the course instance
                return c;
            }
        } catch (HibernateException e) {
            //show the stack trace for the exception thrown
            e.printStackTrace();
        } finally {
            //close the session
            s.close();
        }
        //if the try fails, a default/null Course is returned
        return new Course();
    }

    @Override
    public List<Course> getAllCourses() {
        //start a session, prepare a transaction and make a list for storing db data
        Session s = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        List<Course> courseList = null;

        try {
            tx = s.beginTransaction();
            //use Native query
            Query q = s.createNamedQuery("from Course", Course.class);
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
