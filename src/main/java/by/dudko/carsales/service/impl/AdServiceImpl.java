package by.dudko.carsales.service.impl;

import by.dudko.carsales.dao.CarAdDao;
import by.dudko.carsales.dao.PhoneNumberDao;
import by.dudko.carsales.dao.impl.CarAdDaoImpl;
import by.dudko.carsales.dao.impl.PhoneNumberDaoImpl;
import by.dudko.carsales.mapper.impl.CarAdCreateMapper;
import by.dudko.carsales.model.dto.carad.CreateCarAdDto;
import by.dudko.carsales.model.entity.CarAd;
import by.dudko.carsales.service.AdService;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

import java.util.List;
import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AdServiceImpl implements AdService {
    private static final AdService instance = new AdServiceImpl();
    private final CarAdDao carAdDao = CarAdDaoImpl.getInstance();
    private final PhoneNumberDao phoneNumberDao = PhoneNumberDaoImpl.getInstance();

    public static AdService getInstance() {
        return instance;
    }

    @Override
    public List<CarAd> findAll() {
        return carAdDao.findAll();
    }

    @Override
    public Optional<CarAd> findById(long adId) {
        return carAdDao.findById(adId);
    }

    @SneakyThrows
    public Optional<CarAd> saveAd(CreateCarAdDto adDto) {
        return Optional.ofNullable(adDto)
                .map(dto -> {
                    CarAd ad = CarAdCreateMapper.getInstance().map(dto);
                    carAdDao.insert(ad);
                    return ad;
                });
    }


    @Override
    public boolean deleteById(long adId) {
        return carAdDao.deleteById(adId);
    }
}
