package by.dudko.carsales.repository;

import by.dudko.carsales.model.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdImageRepository extends JpaRepository<Image, Long> {
    int countByAdId(long adId);

    List<Image> findAllByAdId(long adId);
}
