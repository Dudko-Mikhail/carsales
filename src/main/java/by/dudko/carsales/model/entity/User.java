package by.dudko.carsales.model.entity;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@ToString(exclude = {"userAds"})
@EqualsAndHashCode(exclude = {"userAds"})
@NoArgsConstructor
@AllArgsConstructor
public class User implements BaseEntity<Long> {
    private Long id;
    private String email;
    private String phoneNumber;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<CarAd> userAds;
}
