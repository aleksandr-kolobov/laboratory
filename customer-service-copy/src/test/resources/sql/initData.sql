DELETE
FROM customer;
DELETE
FROM country_dictionary;
DELETE
FROM family_status_dictionary;
DELETE
FROM status_dictionary;
DELETE
FROM user_profile;

INSERT INTO country_dictionary (country_code_iso, country_name)
VALUES ('RU', 'Russia'),
       ('BY', 'Belarus'),
       ('AM', 'Armenia');

INSERT INTO family_status_dictionary (family_status, family_status_name)
VALUES (1, 'single'),
       (2, 'married');

INSERT INTO status_dictionary (status_type, status_name)
VALUES (1, 'OK'),
       (2, 'Not yet OK'),
       (3, 'Not OK'),
       (4, 'Big not OK');

INSERT INTO customer(customer_id, first_name, last_name, middle_name, phone_number, email, gender, birth_date,
                     family_status_code, child_count, registration_date_bank, secret_question, secret_answer,
                     citizenship, status_type, sms_notification)
VALUES ('7ccbd32e-cdd7-490c-9446-dd716d236fc5', 'Aleksey', 'Sidorov', 'Aleksandrovich', '79234251422',
        'forward2020@inbox.ru', 'M', '1995-01-01', 1, 0, '2021-01-01', 'вопрос', 'ответ', 'RU', 1, true),
       ('94f4ec77-5eb9-4210-84d6-fee7123b8859', 'Andrey', 'Petrov', 'Palych', '79234251515', 'forward2022@inbox.ru',
        'M', '2001-01-01', 2, 2, '1998-12-12', 'вопрос', 'ответ', 'RU', 1, true),
       ('c795e535-3f5e-42a0-9a16-6b3fcfff3d0b', 'Artem', 'Ivanov', 'Ilich', '79234251313', 'forward2013@inbox.ru', 'M',
        '2001-01-01', 1, 0, '2021-01-01', 'вопрос', 'ответ', 'AM', 2, false);

INSERT INTO user_profile(customer_id, hash_password, registration_date_app)
VALUES ('7ccbd32e-cdd7-490c-9446-dd716d236fc5', '$2a$12$0kse7JdZFAdQYNUVELumhOXFTDB2bqiYpRKRK52Qf/FbswzHBHbC.', '2022-01-01'),
       ('94f4ec77-5eb9-4210-84d6-fee7123b8859', '$2a$12$0kse7JdZFAdQYNUVELumhOXFTDB2bqiYpRKRK52Qf/FbswzHBHbC.', '2022-01-01'),
       ('c795e535-3f5e-42a0-9a16-6b3fcfff3d0b', '$2a$12$0kse7JdZFAdQYNUVELumhOXFTDB2bqiYpRKRK52Qf/FbswzHBHbC.', '2022-01-01');
