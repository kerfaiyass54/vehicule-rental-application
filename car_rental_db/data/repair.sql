-- ============================================
-- INTERNATIONAL REPAIR CENTERS
-- ============================================

INSERT INTO repairs (
    repair_name,
    email,
    password_hash,
    role,
    id_admin,
    id_location
)
SELECT
    repair_name,
    'repair' || id_location || '@carrental.example',
    '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
    'REPAIRER',
    ((id_location - 1) % 10) + 1,
    id_location
FROM (
    VALUES
    -- Germany
    ('Berlin Auto Technik GmbH', 1),
    ('Munich Fahrzeug Service GmbH', 2),
    ('Hamburg Motor Service GmbH', 3),
    ('Frankfurt Auto Werkstatt GmbH', 4),

    -- France
    ('Paris Auto Repair SAS', 5),
    ('Lyon Mecanique SARL', 6),
    ('Marseille Auto Service SAS', 7),
    ('Nice Riviera Garage', 8),

    -- Italy
    ('Roma Auto Service SRL', 9),
    ('Milano Motor Repair SRL', 10),
    ('Torino Fahrzeug Service SRL', 11),
    ('Firenze Auto Technik SRL', 12),

    -- Spain
    ('Madrid Auto Repair SL', 13),
    ('Barcelona Motor Service SL', 14),
    ('Valencia Auto Technik SL', 15),
    ('Sevilla Fahrzeug Service SL', 16),

    -- United Kingdom
    ('London Vehicle Service Ltd', 17),
    ('Manchester Auto Repair Ltd', 18),
    ('Birmingham Motor Service Ltd', 19),
    ('Edinburgh Vehicle Care Ltd', 20),

    -- United States
    ('New York Auto Service LLC', 21),
    ('Los Angeles Vehicle Repair LLC', 22),
    ('Chicago Motor Works LLC', 23),
    ('San Francisco Auto Care LLC', 24),

    -- Canada
    ('Toronto Auto Service Inc.', 25),
    ('Vancouver Vehicle Care Inc.', 26),
    ('Montreal Motor Service Inc.', 27),
    ('Calgary Auto Repair Inc.', 28),

    -- Japan
    ('Tokyo Vehicle Service KK', 29),
    ('Osaka Auto Repair KK', 30),
    ('Kyoto Motor Service KK', 31),
    ('Yokohama Vehicle Care KK', 32),

    -- South Korea
    ('Seoul Auto Service Co.', 33),
    ('Busan Vehicle Repair Co.', 34),
    ('Incheon Motor Service Co.', 35),
    ('Daegu Auto Care Co.', 36),

    -- UAE
    ('Dubai Auto Service LLC', 37),
    ('Abu Dhabi Vehicle Repair LLC', 38),
    ('Sharjah Motor Service LLC', 39),
    ('Ajman Auto Care LLC', 40),

    -- Switzerland
    ('Zurich Fahrzeug Service AG', 41),
    ('Geneva Auto Repair SA', 42),
    ('Basel Motor Service AG', 43),
    ('Lausanne Vehicle Care SA', 44),

    -- Netherlands
    ('Amsterdam Auto Service BV', 45),
    ('Rotterdam Vehicle Repair BV', 46),
    ('Utrecht Motor Service BV', 47),
    ('The Hague Auto Care BV', 48),

    -- Australia
    ('Sydney Vehicle Service Pty Ltd', 49),
    ('Melbourne Auto Repair Pty Ltd', 50),
    ('Brisbane Motor Service Pty Ltd', 51),
    ('Perth Vehicle Care Pty Ltd', 52),

    -- Brazil
    ('Sao Paulo Auto Service Ltda', 53),
    ('Rio Vehicle Repair Ltda', 54),
    ('Brasilia Motor Service Ltda', 55),
    ('Salvador Auto Care Ltda', 56),

    -- Mexico
    ('Mexico City Auto Service SA', 57),
    ('Monterrey Vehicle Repair SA', 58),
    ('Guadalajara Motor Service SA', 59),
    ('Cancun Auto Care SA', 60),

    -- Egypt
    ('Cairo Auto Service LLC', 61),
    ('Alexandria Vehicle Repair LLC', 62),
    ('Giza Motor Service LLC', 63),
    ('Sharm Auto Care LLC', 64),

    -- Turkey
    ('Istanbul Vehicle Service AS', 65),
    ('Ankara Auto Repair AS', 66),
    ('Izmir Motor Service AS', 67),
    ('Antalya Auto Care AS', 68),

    -- Singapore
    ('Singapore Auto Service Pte Ltd', 69),
    ('Singapore East Vehicle Repair Pte Ltd', 70),
    ('Singapore West Motor Service Pte Ltd', 71),
    ('Singapore North Auto Care Pte Ltd', 72),

    -- Austria
    ('Vienna Fahrzeug Service GmbH', 73),
    ('Salzburg Auto Repair GmbH', 74),
    ('Innsbruck Motor Service GmbH', 75),
    ('Graz Vehicle Care GmbH', 76),

    -- Belgium
    ('Brussels Auto Service SRL', 77),
    ('Antwerp Vehicle Repair NV', 78),
    ('Ghent Motor Service NV', 79),
    ('Bruges Auto Care BV', 80)
    ) AS data(
    repair_name,
    id_location
    );