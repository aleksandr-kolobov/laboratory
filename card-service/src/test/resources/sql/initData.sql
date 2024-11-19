DELETE
FROM card;
DELETE
FROM card_product;
DELETE
FROM bank_limit;
DELETE
FROM user_limit;
DELETE
FROM condition;

INSERT INTO bank_limit (limit_id, withdraw_day, withdraw_month, transaction_day, transaction_month, pay_day, pay_month,
                        over_withdraw_day, over_withdraw_month, over_transaction_day, over_transaction_month,
                        over_pay_day, over_pay_month)
VALUES (1, 100.00, 500.00, 200.00, 1000.00, 50.00, 200.00, 150.00, 750.00, 100.00, 500.00, 25.00, 100.00),
       (2, 200.00, 1000.00, 300.00, 1500.00, 100.00, 400.00, 300.00, 1500.00, 200.00, 1000.00, 50.00, 200.00);

INSERT INTO condition (condition_id, condition_withdraw, condition_partner_withdraw, condition_world_withdraw,
                       condition_transaction, condition_pay)
VALUES (1, 100.00, 200.00, 300.00, 50.00, 150.00),
       (2, 200.00, 300.00, 400.00, 100.00, 200.00);

INSERT INTO card_product (card_product_id, limit_id, condition_id, image, name_product, fee_use, payment_system,
                          is_virtual, type_card, level)
VALUES ('CP1', 1, 1, 'image.png', 'Базовая', 10.00, 'МИР', false, 'DEBIT', 'CLASSIC'),
       ('CP2', 2, 2, 'image2.png', 'Дополнительная', 15.00, 'VISA', true, 'CREDIT', 'GOLD');

INSERT INTO user_limit (user_limit_id, withdraw_limit_day, withdraw_limit_month, transaction_limit_day,
                        transaction_limit_month, pay_limit_day, pay_limit_month)
VALUES ('1b27dcd5-5905-40c4-a337-c7c4b05b71d1', 100.00, 2000.00, 500.00, 10000.00, 200.00, 3000.00),
       ('f05362d5-1385-4c38-a2f4-7324cf1a73aa', 150.00, 3000.00, 600.00, 12000.00, 250.00, 3500.00);

INSERT INTO card (card_id, customer_id, account_number, user_limit, card_product_id, card_number, balance,
                  cvv_encrypted, pin_code_hash, status_name, created_at, expiration_at, closed_at, blocked_at,
                  block_comment)
VALUES ('cdaeb5ef-f132-4042-98c3-364020463e6a', '64b4e2af-ab31-4085-9704-06be803c31f1', 'ACC12345',
        '1b27dcd5-5905-40c4-a337-c7c4b05b71d1', 'CP1', '1234567890123456', 1000.00, 'encrypted_cvv1', 'hashed_pin1',
        'ACTIVE', '2024-03-18T12:00:00Z', '2025-03-18T12:00:00Z', NULL, NULL, NULL),
       ('2d7376bc-ae73-4010-9dec-cd094cdc24c1', '4b7cf0ff-f9e0-44b3-a02f-6fff9e7a8719', 'ACC54321',
        'f05362d5-1385-4c38-a2f4-7324cf1a73aa', 'CP2',
        '0987654321098765', 500.00, 'encrypted_cvv2', 'hashed_pin2',
        'BLOCKED', '2024-03-18T12:00:00Z', '2025-03-18T12:00:00Z', NULL, NULL, NULL);
