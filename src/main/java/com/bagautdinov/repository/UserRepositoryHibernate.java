package com.bagautdinov.repository;//package com.bagautdinov.repository;
//
//import com.bagautdinov.model.User;
//import org.hibernate.Session;
//import org.hibernate.SessionFactory;
//import java.util.List;
//import org.springframework.stereotype.Component;
//
//@Component
//public class UserRepositoryHibernate {
//
//    private final SessionFactory sessionFactory;
//
//    public UserRepositoryHibernate(SessionFactory sessionFactory) {
//        this.sessionFactory = sessionFactory;
//    }
//
//    private Session currentSession() {
//        return sessionFactory.getCurrentSession();
//    }
//
//    public List<User> findAll() {
//        return currentSession()
//                .createQuery("from User", User.class)
//                .getResultList();
//    }
//
//    public User findById(long id) {
//        return currentSession().get(User.class, id);
//    }
//
//    public void save(User user) {
//        currentSession().persist(user);
//    }
//}
