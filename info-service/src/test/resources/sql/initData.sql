DELETE
FROM object;

INSERT INTO object (object_id, bank_transfer, building_number_house, coordinates, credit_repayment, credit_take,
                    currency_exchange, house_number, insurance, location, microdistrict, object_number, object_status,
                    object_type_name, region, replenishment, replenishment_without_card, schedule, segment, street,
                    transfer_between_accounts, withdrawal)
VALUES ('cd5f739d-10cf-4b1f-a03a-4bb7e10f6a5b', true, null, ST_GeomFromText('POINT(27.922945 57.062408)', 4326), true,
        true, false, 11, true, 'Москва', null, 1, true, 'branch ', 'Измайлово', true, true,
        'Без выходных, круглосуточно', 'private', 'Пушкина', false, true),
       ('35b84242-6502-44e0-acc9-3fee041b723d', false, '123A', ST_GeomFromText('POINT(55.753215 37.622504)', 4326),
        true, false, true, '5', false, 'Санкт-Петербург', 'Центральный', 1001, true, 'ATM', 'Центральный',
        false, true, 'Пн-Пт 9:00-18:00', 'corporate', 'Невский проспект', true, false);
