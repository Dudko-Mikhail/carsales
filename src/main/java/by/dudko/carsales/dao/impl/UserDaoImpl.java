package by.dudko.carsales.dao.impl;

import by.dudko.carsales.dao.UserDao;
import by.dudko.carsales.exception.DaoException;
import by.dudko.carsales.model.entity.User;
import by.dudko.carsales.util.SessionFactoryHolder;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.SessionFactory;

import java.util.List;
import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserDaoImpl implements UserDao {
    private static final UserDao instance = new UserDaoImpl();
    private final SessionFactory sessionFactory = SessionFactoryHolder.getSessionFactory();

    public static UserDao getInstance() {
        return instance;
    }

    @Override
    public List<User> findAll() throws DaoException {
        try (var session = sessionFactory.openSession()) {
            return session.createQuery("FROM User", User.class).getResultList();
        }
    }

    @Override
    public Optional<User> findById(Long id) throws DaoException {
        try (var session = sessionFactory.openSession()) {
            return Optional.ofNullable(session.find(User.class, id));
        }
    }

    @Override
    public void insert(User user) throws DaoException {
        try (var session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(user);
            session.getTransaction().commit();
        }
    }

    @Override
    public boolean deleteById(Long id) throws DaoException {
        try (var session = sessionFactory.openSession()) {
            session.beginTransaction();
            return Optional.ofNullable(findById(id))
                    .map(number -> {
                        session.delete(number);
                        session.getTransaction().commit();
                        return true;
                    }).orElse(false);
        }
    }
}
