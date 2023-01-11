package by.dudko.carsales.web.controller;

import by.dudko.carsales.model.dto.PageResponse;
import by.dudko.carsales.model.dto.carad.CarAdCreateDto;
import by.dudko.carsales.model.dto.carad.CarAdEditDto;
import by.dudko.carsales.model.dto.carad.CarAdReadDto;
import by.dudko.carsales.model.dto.user.UserReadDto;
import by.dudko.carsales.model.entity.CarAd;
import by.dudko.carsales.model.entity.Image;
import by.dudko.carsales.service.AdService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/ads")
@RequiredArgsConstructor
public class CarAdRestController {
    private final AdService adService;

    @GetMapping
    public PageResponse<?> findAll(@RequestParam(defaultValue = "5") int size,
                                   @RequestParam(defaultValue = "0") int page,
                                   @RequestParam(defaultValue = "false", name = "full") boolean isFull) {
        Sort createdAtDescSort = Sort.by(Sort.Direction.DESC, CarAd.Fields.createdAt);
        Pageable pageable = PageRequest.of(page, size, createdAtDescSort);
        Page<?> pageObject = isFull ? adService.findAllWithFullData(pageable) : adService.findAll(pageable);
        return PageResponse.of(pageObject);
    }

    @GetMapping("/{id}")
    public Object findById(@PathVariable long id,
                           @RequestParam(defaultValue = "false", name = "full") boolean isFull) {
        Optional<?> ad = isFull ? adService.findByIdWithFullData(id) : adService.findById(id);
        return ad.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @GetMapping("images/{id}")
    public byte[] findImageById(@PathVariable long id) {
        return adService.findAdImageById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/{id}/owner")
    public UserReadDto findOwner(@PathVariable long id) {
        return adService.findAdOwner(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @ResponseStatus(code = HttpStatus.CREATED)
    @PostMapping
    @SneakyThrows
    public CarAdReadDto createAd(@RequestBody @Validated CarAdCreateDto carAdDto) {
        return adService.saveAd(carAdDto);
    }

    @ResponseStatus(code = HttpStatus.CREATED)
    @PostMapping(value = "/{id}/images")
    public List<Image> uploadImage(@PathVariable long id, @RequestParam List<MultipartFile> images) {
        return adService.uploadImages(id, images)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PatchMapping("/{id}")
    public CarAdReadDto updateAd(@PathVariable long id, @RequestBody @Validated CarAdEditDto carAdDto) {
        return adService.updateAd(id, carAdDto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteAd(@PathVariable long id) {
        if (!adService.deleteById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    @DeleteMapping("/images/{imageId}")
    public void deleteAdImage(@PathVariable long imageId) {
        if (!adService.deleteImageById(imageId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
}
