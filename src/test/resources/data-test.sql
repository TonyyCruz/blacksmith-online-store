-- password: P4ssw0rd#

INSERT INTO USERS (id, username, password, role, birth_date)
VALUES
    (
    'c0c4a69a-9dda-4b50-ab59-d896ce0a5c6e',
    'admin',
    '$2a$10$N3n2nhZ2ck3RN0mVDcGkTeyl/ibdZwB5CKswrjjSI25lo.8obnkyu',
    'ADMIN',
    '1988-01-10'
    ), (
      '7b87f809-d142-4dfa-8802-87644d774dd5',
      'user',
      '$2a$10$N3n2nhZ2ck3RN0mVDcGkTeyl/ibdZwB5CKswrjjSI25lo.8obnkyu',
      'CUSTOMER',
      '1995-05-15'
    ), (
        '7b87f809-d142-4dfa-8802-87644d774dd4',
        'user2',
        '$2a$10$N3n2nhZ2ck3RN0mVDcGkTeyl/ibdZwB5CKswrjjSI25lo.8obnkyu',
        'CUSTOMER',
        '1990-02-05'
    );

INSERT INTO BLACKSMITHS (name, description, total_ratings_sum, rating_count, rating_average, version)
VALUES
    (
        'Gimli o Marteleiro',
        'Expert blacksmith specializing in medieval weaponry.',
        45,
        10,
        4.5,
        0
    ), (
        'Tyrion o Perdido do Martelo',
        'Custom knives and blades forged with precision.',
        30,
        8,
        3.75,
        0
    );

INSERT INTO ITEMS (material, base_damage, base_defense, name, base_price, final_price,
                   has_discount, description, weight, stock, type, rarity, sold, total_ratings_sum,
                   rating_count, rating_average,created_at, updated_at, blacksmith_id, active,
                   blacksmith_name_snapshot, blacksmith_id_snapshot, version)
VALUES
    (
        'STEEL', 50, 20, 'Sword of Valor', 100.00,
        90.00, true, 'A LEGENDARY sword forged by the finest blacksmiths.',
        5.0, 10, 'SHORT_SWORD', 'RARE', 2, 10, 2, 5,
        '2025-11-15', '2026-02-25', 1, true,
        'Gimli o Marteleiro', 1, 0
    ), (
        'IRON', 30, 10, 'Dagger of Night', 80.00,
        80.00, false,
        'A consistent weapon great fot night hunts.', 7.0,
        15, 'DAGGER', 'UNCOMMON', 154, 549, 122, 4.5,
        '2025-11-15', '2026-02-25', 2, false,
        'Tyrion o Perdido', 2, 0
    ), (
        'STEEL', 13, 6, 'Student Sword', 18.00,
        15.00, true, 'A basic sword forged for beginners.',
        4.8, 1233, 'SHORT_SWORD', 'RARE', 2, 10, 2, 5,
        '2020-11-15', '2022-02-25', 1, true,
        'Gimli o Marteleiro', 1, 0
    ), (
        'MITHRIL', 70, 30, 'Axe of Light', 150.00,
        135.00, true,
        'A Axe that shines with the brilliance of the sun.', 10.0,
        5, 'DOUBLE_HEADED_AXE', 'EPIC', 1, 25, 5,
        5, '2025-11-15', '2026-02-25', 1, true,
        'Gimli o Marteleiro', 1, 0
    ), (
        'COPPER', 20, 5, 'Axe of Fury', 60.00,
        60.00, false,
        'An axe that channels the fury of battle.', 8.0,
        20, 'HAND_AXE', 'COMMON', 50, 100, 20,
        4, '2025-11-15', '2026-02-25', 2, true,
        'Tyrion o Perdido', 2, 0
    ), (
        'STEEL', 40, 15, 'Bow of the Moon', 120.00,
        108.00, true,
        'A bow that strikes with the grace of the moonlight.', 3.0,
        8, 'BOW', 'RARE', 5, 32, 8, 4.25,
        '2025-11-15', '2026-02-25', 1, true,
        'Gimli o Marteleiro', 1, 0
    ), (
        'ADAMANTIUM', 60, 25, 'Hammer of the Titans', 200.00,
        180.00, true,
        'A hammer that embodies the strength of the titans.', 12.0,
        3, 'WAR_HAMMER', 'LEGENDARY', 0, 50, 10,
        5, '2025-11-15', '2026-02-25', 2, true,
        'Tyrion o Perdido', 2, 0
    ), (
        'ADAMANTIUM', 80, 40, 'Spear of the Gods', 250.00,
        225.00, true,
        'A spear that pierces through the heavens.', 6.0,
        2, 'SPEAR', 'MYTHIC', 0, 100, 20, 5,
        '2025-11-15', '2026-02-25', 1, true,
        'Gimli o Marteleiro', 1, 0
    ), (
        'OBSIDIAN', 55, 25, 'Mace of Shadows', 110.00,
        99.00, true,
        'A mace that strikes with the darkness of shadows.', 9.0,
        7, 'MACE', 'EPIC', 3, 40, 10, 4.5,
        '2025-11-15', '2026-02-25', 2, true,
        'Tyrion o Perdido', 2, 0
    ), (
        'OBSIDIAN', 90, 50, 'Greatsword of the Dragon', 300.00,
        270.00, true,
        'A greatsword forged from the bones of a mighty dragon.', 15.0,
        1, 'BROADSWORD', 'LEGENDARY', 0, 200, 50,
        5, '2025-11-15', '2026-02-25', 1, true,
        'Gimli o Marteleiro', 1, 0
    ), (
        'STEEL', 35, 10, 'Crossbow of the Eagle', 90.00,
        81.00, true,
        'A crossbow that strikes with the precision of an eagle.', 4.0,
        12, 'CROSSBOW', 'UNCOMMON', 4, 28, 7,
        4.25, '2025-11-15', '2026-02-25', 2, false,
        'Tyrion o Perdido', 2, 0
    ) ON CONFLICT DO NOTHING;

INSERT INTO ORDERS (user_id, created_at, updated_at, status, total)
VALUES
    ('7b87f809-d142-4dfa-8802-87644d774dd5', '2025-11-15', '2025-11-15',
     'DELIVERED', 240.00),
    ('7b87f809-d142-4dfa-8802-87644d774dd5', '2025-11-20', '2025-11-20',
     'PENDING', 135.00),
    ('7b87f809-d142-4dfa-8802-87644d774dd4', '2025-11-15', '2025-11-15',
     'DELIVERED', 440.00);

INSERT INTO ORDER_ITEMS (item_id, item_name, base_price_at_purchase, price_applied, quantity,
                         total_price, order_id, rating_value, user_id, blacksmith_id,
                         created_at)
VALUES
    (1, 'Sword of Valor', 100.00, 90.00, 2,
     180.00, 1, NULL, '7b87f809-d142-4dfa-8802-87644d774dd5',
     1, '2025-11-15'),
    (4, 'Axe of Fury', 60.00, 60.00, 1,
     60.00, 1, NULL, '7b87f809-d142-4dfa-8802-87644d774dd5',
     2, '2025-11-15'),
    (3, 'Axe of Light', 150.00, 135.00, 1,
     135.00, 2, NULL, '7b87f809-d142-4dfa-8802-87644d774dd5',
     2, '2025-11-15'),
    (4, 'Sword of Valor', 100.00, 90.00, 2,
     180.00, 3, NULL, '7b87f809-d142-4dfa-8802-87644d774dd4',
     1, '2025-11-20'),
    (5, 'Dagger of Night', 80.00, 80.00, 1,
     80.00, 3, NULL, '7b87f809-d142-4dfa-8802-87644d774dd4',
     2, '2025-11-20');
