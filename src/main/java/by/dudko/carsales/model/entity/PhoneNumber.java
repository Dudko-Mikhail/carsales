package by.dudko.carsales.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Telephone implements BaseEntity<Long> {
    private Long id;
    private Long addId;
    private String number;
}
