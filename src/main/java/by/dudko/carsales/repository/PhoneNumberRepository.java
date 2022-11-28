package by.dudko.carsales.repository;

import by.dudko.carsales.model.entity.PhoneNumber;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PhoneNumberRepository extends JpaRepository<PhoneNumber, Long> {
    List<PhoneNumber> findAllByAdId(long adId);
}
