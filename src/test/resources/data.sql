insert into users (id, email, phone_number, created_at, updated_at)
values (1, 'ivan@mail.ru', '375254781236', current_timestamp, current_timestamp),
       (2, 'test_user@gmail.com', '375257986842', current_timestamp, current_timestamp),
       (3, 'antonenko@gmail.com', '375442236574', current_timestamp, current_timestamp),
       (4, 'sidorov@mail.ru', '375297221475', current_timestamp, current_timestamp),
       (5, 'peretz@mail.ru', '375297413289', current_timestamp, current_timestamp);
SELECT SETVAL('users_id_seq', (SELECT MAX(id) FROM users));

insert into car_ads (id, year, brand, model, engine_volume, car_state, mileage, power, user_id, created_at, updated_at)
values (1, 2022, 'BMW', 'X6', 83, 'NEW', 1000, 600, 1, current_timestamp, current_timestamp),
       (2, 2020, 'BMW', 'X6', 83, 'USED', 1000, 600, 2, current_timestamp, current_timestamp),
       (3, 2020, 'Volkswagen', 'Passat VIII', 66, 'NEW', 200, 150, 2, current_timestamp, current_timestamp);

SELECT SETVAL('car_ads_id_seq', (SELECT MAX(id) FROM car_ads));


insert into ad_phone_numbers(ad_id, number)
values (1, '375254781236'), (1, '375297221462'),
       (2, '375257986842'),
       (3, '375257986842');

insert into ad_images(id, ad_id, image_name)
values (1, 1, 'first.png'), (2, 1, 'second.jpg'),
       (3, 2, 'test.jpg');
SELECT SETVAL('ad_images_id_seq', (SELECT MAX(id) FROM ad_images));

