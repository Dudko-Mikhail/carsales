package by.dudko.carsales.repository;

import by.dudko.carsales.model.entity.CarAd;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarAdRepository extends JpaRepository<CarAd, Long> {
    List<CarAd> findAllByOwnerId(long userId);
}
