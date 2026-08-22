-- ============================================
-- INTERNATIONAL SUPPLIERS
-- ============================================

INSERT INTO suppliers (
    supplier_name,
    email,
    nationality,
    password_hash,
    role,
    experience,
    id_admin
)
SELECT
    supplier_name,
    email,
    nationality,
    '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
    'SUPPLIER',
    3 + (id_location % 15),
    ((id_location - 1) % 10) + 1
FROM (
    VALUES
    -- Germany
    ('Berlin Mobility GmbH', 'contact@berlinmobility.de', 'German', 1),
    ('Bavaria Auto Rental GmbH', 'info@bavariaautorental.de', 'German', 2),
    ('Hamburg Drive Solutions GmbH', 'service@hamburgdrive.de', 'German', 3),
    ('Frankfurt Fleet Services GmbH', 'contact@frankfurtfleet.de', 'German', 4),

    -- France
    ('Paris Auto Services SAS', 'contact@parisautoservices.fr', 'French', 5),
    ('Lyon Mobility SARL', 'info@lyonmobility.fr', 'French', 6),
    ('Marseille Drive SAS', 'contact@marseilledrive.fr', 'French', 7),
    ('Nice Riviera Mobility', 'info@niceriviera.fr', 'French', 8),

    -- Italy
    ('Roma Mobility SRL', 'info@romamobility.it', 'Italian', 9),
    ('Milano Auto SRL', 'contact@milanoauto.it', 'Italian', 10),
    ('Torino Drive SRL', 'info@torinodrive.it', 'Italian', 11),
    ('Firenze Mobility SRL', 'contact@firenzemobility.it', 'Italian', 12),

    -- Spain
    ('Madrid Car Solutions SL', 'contact@madridcars.es', 'Spanish', 13),
    ('Barcelona Mobility SL', 'info@barcelonamobility.es', 'Spanish', 14),
    ('Valencia Auto Rental SL', 'contact@valenciaauto.es', 'Spanish', 15),
    ('Sevilla Drive SL', 'info@sevilladrive.es', 'Spanish', 16),

    -- United Kingdom
    ('London Drive Ltd', 'contact@londondrive.co.uk', 'British', 17),
    ('Manchester Mobility Ltd', 'info@manchestermobility.co.uk', 'British', 18),
    ('Birmingham Auto Hire Ltd', 'contact@birminghamauto.co.uk', 'British', 19),
    ('Edinburgh Fleet Ltd', 'info@edinburghfleet.co.uk', 'British', 20),

    -- United States
    ('New York Auto Group LLC', 'contact@nyautogroup.com', 'American', 21),
    ('Los Angeles Mobility LLC', 'info@lamobility.com', 'American', 22),
    ('Chicago Fleet Services LLC', 'contact@chicagofleet.com', 'American', 23),
    ('San Francisco Drive LLC', 'info@sfdrive.com', 'American', 24),

    -- Canada
    ('Toronto Mobility Inc.', 'contact@torontomobility.ca', 'Canadian', 25),
    ('Vancouver Auto Services Inc.', 'info@vancouverauto.ca', 'Canadian', 26),
    ('Montreal Drive Inc.', 'contact@montrealdrive.ca', 'Canadian', 27),
    ('Calgary Fleet Inc.', 'info@calgaryfleet.ca', 'Canadian', 28),

    -- Japan
    ('Tokyo Mobility KK', 'contact@tokyomobility.jp', 'Japanese', 29),
    ('Osaka Auto Rental KK', 'info@osakaautorental.jp', 'Japanese', 30),
    ('Kyoto Drive KK', 'contact@kyotodrive.jp', 'Japanese', 31),
    ('Yokohama Fleet KK', 'info@yokohamafleet.jp', 'Japanese', 32),

    -- South Korea
    ('Seoul Mobility Co.', 'contact@seoulmobility.kr', 'South Korean', 33),
    ('Busan Auto Co.', 'info@busanauto.kr', 'South Korean', 34),
    ('Incheon Drive Co.', 'contact@incheondrive.kr', 'South Korean', 35),
    ('Daegu Fleet Co.', 'info@daegufleet.kr', 'South Korean', 36),

    -- United Arab Emirates
    ('Dubai Auto Mobility LLC', 'contact@dubaimobility.ae', 'Emirati', 37),
    ('Abu Dhabi Fleet LLC', 'info@abudhabifleet.ae', 'Emirati', 38),
    ('Sharjah Drive LLC', 'contact@sharjahdrive.ae', 'Emirati', 39),
    ('Ajman Auto Services LLC', 'info@ajmanauto.ae', 'Emirati', 40),

    -- Switzerland
    ('Zurich Mobility AG', 'contact@zurichmobility.ch', 'Swiss', 41),
    ('Geneva Auto Services SA', 'info@genevaauto.ch', 'Swiss', 42),
    ('Basel Drive AG', 'contact@baseldrive.ch', 'Swiss', 43),
    ('Lausanne Fleet SA', 'info@lausannefleet.ch', 'Swiss', 44),

    -- Netherlands
    ('Amsterdam Mobility BV', 'contact@amsterdammobility.nl', 'Dutch', 45),
    ('Rotterdam Auto BV', 'info@rotterdamauto.nl', 'Dutch', 46),
    ('Utrecht Drive BV', 'contact@utrechtdrive.nl', 'Dutch', 47),
    ('The Hague Fleet BV', 'info@thehaguefleet.nl', 'Dutch', 48),

    -- Australia
    ('Sydney Mobility Pty Ltd', 'contact@sydneymobility.au', 'Australian', 49),
    ('Melbourne Auto Pty Ltd', 'info@melbourneauto.au', 'Australian', 50),
    ('Brisbane Drive Pty Ltd', 'contact@brisbanedrive.au', 'Australian', 51),
    ('Perth Fleet Pty Ltd', 'info@perthfleet.au', 'Australian', 52),

    -- Brazil
    ('Sao Paulo Mobilidade Ltda', 'contact@saopaulomobility.br', 'Brazilian', 53),
    ('Rio Drive Ltda', 'info@riodrive.br', 'Brazilian', 54),
    ('Brasilia Auto Ltda', 'contact@brasiliaauto.br', 'Brazilian', 55),
    ('Salvador Fleet Ltda', 'info@salvadorfleet.br', 'Brazilian', 56),

    -- Mexico
    ('Mexico City Mobility SA', 'contact@mexicocitymobility.mx', 'Mexican', 57),
    ('Monterrey Auto SA', 'info@monterreyauto.mx', 'Mexican', 58),
    ('Guadalajara Drive SA', 'contact@guadalajaradrive.mx', 'Mexican', 59),
    ('Cancun Fleet SA', 'info@cancunfleet.mx', 'Mexican', 60),

    -- Egypt
    ('Cairo Mobility LLC', 'contact@cairomobility.eg', 'Egyptian', 61),
    ('Alexandria Auto LLC', 'info@alexandriaauto.eg', 'Egyptian', 62),
    ('Giza Drive LLC', 'contact@gizadrive.eg', 'Egyptian', 63),
    ('Sharm Fleet LLC', 'info@sharmfleet.eg', 'Egyptian', 64),

    -- Turkey
    ('Istanbul Mobility AS', 'contact@istanbulmobility.tr', 'Turkish', 65),
    ('Ankara Auto AS', 'info@ankaraauto.tr', 'Turkish', 66),
    ('Izmir Drive AS', 'contact@izmirdrive.tr', 'Turkish', 67),
    ('Antalya Fleet AS', 'info@antalyafleet.tr', 'Turkish', 68),

    -- Singapore
    ('Singapore Mobility Pte Ltd', 'contact@singaporemobility.sg', 'Singaporean', 69),
    ('Singapore East Auto Pte Ltd', 'info@sgeastauto.sg', 'Singaporean', 70),
    ('Singapore West Drive Pte Ltd', 'contact@sgwestdrive.sg', 'Singaporean', 71),
    ('Singapore North Fleet Pte Ltd', 'info@sgnorthfleet.sg', 'Singaporean', 72),

    -- Austria
    ('Vienna Mobility GmbH', 'contact@viennamobility.at', 'Austrian', 73),
    ('Salzburg Auto GmbH', 'info@salzburgauto.at', 'Austrian', 74),
    ('Innsbruck Drive GmbH', 'contact@innsbruckdrive.at', 'Austrian', 75),
    ('Graz Fleet GmbH', 'info@grazfleet.at', 'Austrian', 76),

    -- Belgium
    ('Brussels Mobility SRL', 'contact@brusselsmobility.be', 'Belgian', 77),
    ('Antwerp Auto NV', 'info@antwerpauto.be', 'Belgian', 78),
    ('Ghent Drive NV', 'contact@ghentdrive.be', 'Belgian', 79),
    ('Bruges Fleet BV', 'info@brugesfleet.be', 'Belgian', 80)
    ) AS data(
    supplier_name,
    email,
    nationality,
    id_location
    );