-- password_user: 123456
-- password_admin: loginAdmin

INSERT INTO USERS (id, username, password, role, birth_date)
VALUES
    (
    'c0c4a69a-9dda-4b50-ab59-d896ce0a5c6e',
    'admin',
    '$2a$10$vC1hgddH4UJBxQYv0AUqLOqd5HGPfeD5Pbp3nhTwy9tnOavEudiBK',
    'ADMIN',
    '1988-01-10'
    ), (
      '7b87f809-d142-4dfa-8802-87644d774dd5',
      'user',
      '$2a$10$GaeCNDGHa.u6vNAPS6xUee/3PoWsN.nVxaDHmNK5LMheS7ZDqa6TG',
      'CUSTOMER',
      '1995-05-15'
    );

INSERT INTO BLACKSMITHS (name, description, total_ratings_sum, rating_count, rating_average)
VALUES
    (
        'Gimli o Marteleiro',
        'Expert blacksmith specializing in medieval weaponry.',
        45,
        10,
        4.5
    ), (
        'Tyrion o Perdido',
        'Custom knives and blades forged with precision.',
        30,
        8,
        3.75
    );

INSERT INTO ITEMS (material, base_damage, base_defense, name, base_price, final_price,
                   has_discount, description, weight, stock, type, rarity, sold, total_ratings_sum,
                   rating_count, rating_average,created_at, updated_at, blacksmith_id, active,
                   blacksmith_name_snapshot, blacksmith_id_snapshot)
VALUES
    (
     'Steel', 50, 20, 'Sword of Valor', 100.00,
     90.00, true, 'A legendary sword forged by the finest blacksmiths.',
     5.0, 10, 'SHORT_SWORD', 'Rare', 2, 10, 2, 5,
     '2025-11-15', '2026-02-25', 1, true,
     'Gimli o Marteleiro', 1
    ), (
     'Iron', 30, 10, 'Dagger of Night', 80.00,
     80.00, false,
     'A consistent weapon great fot night hunts.', 7.0,
     15, 'DAGGER', 'Uncommon', 154, 549, 122, 4.5, '2025-11-15', '2026-02-25', 2, false,
     'Tyrion o Perdido', 2
);