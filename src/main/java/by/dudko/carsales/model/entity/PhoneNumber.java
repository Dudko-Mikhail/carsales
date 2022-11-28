package by.dudko.carsales.model.entity;

import lombok.*;

import javax.persistence.*;

@Data
@EqualsAndHashCode(of = "number")
@ToString(exclude = "ad")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ad_phone_numbers")
public class PhoneNumber implements BaseEntity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ad_id", referencedColumnName = "id")
    private CarAd ad;
    private String number;
}
