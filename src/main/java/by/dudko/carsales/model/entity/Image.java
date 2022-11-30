package by.dudko.carsales.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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

    @Column(name = "ad_id")
    private long adId;
    private String imageName;

    public static Image of(long adId, String imageName) {
        return Image.builder()
                .adId(adId)
                .imageName(imageName)
                .build();
    }

    public String getImagePath() {
        return String.format("%d_%s", id, imageName);
    }

    public String getImageName() {
        int imageNameStartIndex = imageName.indexOf("_") + 1;
        return imageName.substring(imageNameStartIndex);
    }
}
