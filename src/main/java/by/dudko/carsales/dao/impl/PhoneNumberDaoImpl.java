package by.dudko.carsales.dao.impl;

import by.dudko.carsales.dao.PhoneNumberDao;
import by.dudko.carsales.model.entity.PhoneNumber;
import by.dudko.carsales.util.SessionFactoryHolder;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.SessionFactory;

import java.util.List;
import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PhoneNumberDaoImpl implements PhoneNumberDao {
    private static final PhoneNumberDao instance = new PhoneNumberDaoImpl();
    private final SessionFactory sessionFactory = SessionFactoryHolder.getSessionFactory();

    public static PhoneNumberDao getInstance() {
        return instance;
    }

    @Override
    public List<PhoneNumber> findAll() {
        try (var session = sessionFactory.openSession()) {
            return session.createQuery("FROM PhoneNumber", PhoneNumber.class).getResultList();
        }
    }

    @Override
    public Optional<PhoneNumber> findById(Long id) {
        try (var session = sessionFactory.openSession()) {
            return Optional.ofNullable(session.find(PhoneNumber.class, id));
        }
    }

    @Override
    public void insert(PhoneNumber number) {
        try (var session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(number);
            session.getTransaction().commit();
        }
    }

    @Override
    public boolean deleteById(Long id) {
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
