CREATE TABLE ad_images (
    id BIGSERIAL PRIMARY KEY,
    ad_id BIGINT REFERENCES car_ads(id) ON DELETE CASCADE  NOT NULL,
    image_name VARCHAR(255) NOT NULL
)