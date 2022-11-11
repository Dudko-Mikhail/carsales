ALTER TABLE users RENAME COLUMN telephone TO phone_number;

ALTER TABLE ad_telephones RENAME TO ad_phone_numbers;

ALTER TABLE ad_phone_numbers RENAME COLUMN telephone TO number;