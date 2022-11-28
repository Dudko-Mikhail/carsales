package by.dudko.carsales.dao.impl;

import by.dudko.carsales.dao.CarAdDao;
import by.dudko.carsales.model.entity.CarAd;
import by.dudko.carsales.util.SessionFactoryHolder;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.SessionFactory;

import java.util.List;
import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CarAdDaoImpl implements CarAdDao {
    private static final CarAdDao instance = new CarAdDaoImpl();
    private final SessionFactory sessionFactory = SessionFactoryHolder.getSessionFactory();

    public static CarAdDao getInstance() {
        return instance;
    }

    @Override
    public List<CarAd> findAll() {
        try (var session = sessionFactory.openSession()) {
            return session.createQuery("FROM CarAd", CarAd.class).getResultList();
        }
    }

    @Override
    public Optional<CarAd> findById(Long id) {
        try (var session = sessionFactory.openSession()) {
            return Optional.ofNullable(session.find(CarAd.class, id));
        }
    }

    @Override
    public void insert(CarAd ad) {
        try (var session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(ad);
            session.getTransaction().commit();
        }
    }

    @Override
    public boolean deleteById(Long id) {
        try (var session = sessionFactory.openSession()) {
            session.beginTransaction();
            return Optional.ofNullable(findById(id))
                    .map(ad -> {
                        session.remove(id);
                        session.getTransaction().commit();
                        return true;
                    }).orElse(false);
        }
    }
}
