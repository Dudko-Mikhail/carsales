package by.dudko.carsales.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.ColumnTransformer;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;

@Data
@EqualsAndHashCode(exclude = {"id", "phoneNumbers", "owner"})
@ToString(exclude = {"phoneNumbers", "owner"})
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "car_ads")
public class CarAd implements BaseEntity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User owner;
    private int year;
    private String brand;
    private String model;
    private Integer engineVolume;

    @ColumnTransformer(write = "cast(? as carstate)")
    @Enumerated(value = EnumType.STRING)
    private CarState carState;
    private Integer mileage;
    private Integer power;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "ad")
    @Cascade(value = CascadeType.ALL)
    private List<PhoneNumber> phoneNumbers;
}
