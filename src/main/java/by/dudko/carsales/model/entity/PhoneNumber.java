package by.dudko.carsales.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PhoneNumber implements BaseEntity<Long> {
    private Long id;
    private Long adId;
    private String number;
}
