package by.dudko.carsales.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ad_images")
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Exclude
    private long id;

    @ManyToOne
    @JoinColumn(name = "ad_id", referencedColumnName = "id")
    private CarAd ad;
    private String imageName;

    public static Image of(CarAd ad, String imageName) {
        return Image.builder()
                .ad(ad)
                .imageName(imageName)
                .build();
    }

    public String defineImagePath() {
        return String.format("%d_%s", id, imageName);
    }

    public String getImageName() {
        int imageNameStartIndex = imageName.indexOf("_") + 1;
        return imageName.substring(imageNameStartIndex);
    }
}
