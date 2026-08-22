INSERT INTO addresses (
    address_status,
    number,
    road,
    surface,
    id_supplier,
    id_location
) VALUES

      -- Germany
      ('ASSIGNED', 12, 'Friedrichstrasse', 850.00, 1, 1),
      ('ASSIGNED', 45, 'Leopoldstrasse', 920.00, 2, 2),
      ('ASSIGNED', 78, 'Reeperbahn', 1100.00, 3, 3),
      ('ASSIGNED', 23, 'Kaiserstrasse', 780.00, 4, 4),

      -- France
      ('ASSIGNED', 15, 'Rue de Rivoli', 950.00, 5, 5),
      ('ASSIGNED', 42, 'Rue de la Republique', 820.00, 6, 6),
      ('ASSIGNED', 67, 'La Canebiere', 1050.00, 7, 7),
      ('ASSIGNED', 31, 'Avenue Jean Medecin', 760.00, 8, 8),

      -- Italy
      ('ASSIGNED', 18, 'Via Nazionale', 890.00, 9, 9),
      ('ASSIGNED', 55, 'Corso Buenos Aires', 980.00, 10, 10),
      ('ASSIGNED', 29, 'Via Roma', 720.00, 11, 11),
      ('ASSIGNED', 74, 'Via della Scala', 830.00, 12, 12),

      -- Spain
      ('ASSIGNED', 21, 'Gran Via', 1050.00, 13, 13),
      ('ASSIGNED', 63, 'Passeig de Gracia', 1200.00, 14, 14),
      ('ASSIGNED', 38, 'Calle Colon', 780.00, 15, 15),
      ('ASSIGNED', 91, 'Avenida de la Constitucion', 870.00, 16, 16),

      -- United Kingdom
      ('ASSIGNED', 44, 'Oxford Street', 1150.00, 17, 17),
      ('ASSIGNED', 72, 'Deansgate', 890.00, 18, 18),
      ('ASSIGNED', 19, 'New Street', 970.00, 19, 19),
      ('ASSIGNED', 56, 'Princes Street', 820.00, 20, 20),

      -- United States
      ('ASSIGNED', 101, '5th Avenue', 1500.00, 21, 21),
      ('ASSIGNED', 420, 'Wilshire Boulevard', 1350.00, 22, 22),
      ('ASSIGNED', 215, 'Michigan Avenue', 1100.00, 23, 23),
      ('ASSIGNED', 88, 'Market Street', 1250.00, 24, 24),

      -- Canada
      ('ASSIGNED', 250, 'Yonge Street', 1180.00, 25, 25),
      ('ASSIGNED', 735, 'Robson Street', 1050.00, 26, 26),
      ('ASSIGNED', 310, 'Rue Sainte-Catherine', 940.00, 27, 27),
      ('ASSIGNED', 520, '17th Avenue SW', 880.00, 28, 28),

      -- Japan
      ('ASSIGNED', 1, 'Shibuya Street', 980.00, 29, 29),
      ('ASSIGNED', 3, 'Namba Street', 920.00, 30, 30),
      ('ASSIGNED', 15, 'Shijo Street', 760.00, 31, 31),
      ('ASSIGNED', 25, 'Minato Avenue', 890.00, 32, 32),

      -- South Korea
      ('ASSIGNED', 120, 'Gangnam-daero', 1050.00, 33, 33),
      ('ASSIGNED', 45, 'Haeundae-ro', 820.00, 34, 34),
      ('ASSIGNED', 77, 'Incheon-ro', 780.00, 35, 35),
      ('ASSIGNED', 32, 'Dongdaegu-ro', 850.00, 36, 36),

      -- UAE
      ('ASSIGNED', 12, 'Sheikh Zayed Road', 1450.00, 37, 37),
      ('ASSIGNED', 88, 'Corniche Road', 1320.00, 38, 38),
      ('ASSIGNED', 34, 'Al Wahda Street', 970.00, 39, 39),
      ('ASSIGNED', 56, 'Al Ittihad Street', 890.00, 40, 40),

      -- Switzerland
      ('ASSIGNED', 25, 'Bahnhofstrasse', 1250.00, 41, 41),
      ('ASSIGNED', 17, 'Rue du Rhone', 1180.00, 42, 42),
      ('ASSIGNED', 42, 'Freie Strasse', 930.00, 43, 43),
      ('ASSIGNED', 8, 'Rue de Bourg', 850.00, 44, 44),

      -- Netherlands
      ('ASSIGNED', 120, 'Damrak', 980.00, 45, 45),
      ('ASSIGNED', 75, 'Coolsingel', 910.00, 46, 46),
      ('ASSIGNED', 42, 'Oudegracht', 790.00, 47, 47),
      ('ASSIGNED', 18, 'Grote Marktstraat', 870.00, 48, 48),

      -- Australia
      ('ASSIGNED', 200, 'George Street', 1100.00, 49, 49),
      ('ASSIGNED', 350, 'Collins Street', 1050.00, 50, 50),
      ('ASSIGNED', 75, 'Queen Street', 920.00, 51, 51),
      ('ASSIGNED', 44, 'St Georges Terrace', 980.00, 52, 52),

      -- Brazil
      ('ASSIGNED', 1500, 'Avenida Paulista', 1300.00, 53, 53),
      ('ASSIGNED', 850, 'Avenida Atlantica', 1200.00, 54, 54),
      ('ASSIGNED', 120, 'Eixo Monumental', 1050.00, 55, 55),
      ('ASSIGNED', 300, 'Avenida Sete de Setembro', 890.00, 56, 56),

      -- Mexico
      ('ASSIGNED', 100, 'Paseo de la Reforma', 1150.00, 57, 57),
      ('ASSIGNED', 450, 'Avenida Constitucion', 980.00, 58, 58),
      ('ASSIGNED', 250, 'Avenida Vallarta', 900.00, 59, 59),
      ('ASSIGNED', 75, 'Boulevard Kukulcan', 1250.00, 60, 60),

      -- Egypt
      ('ASSIGNED', 25, 'Tahrir Street', 820.00, 61, 61),
      ('ASSIGNED', 18, 'Corniche Road', 780.00, 62, 62),
      ('ASSIGNED', 40, 'Pyramids Road', 950.00, 63, 63),
      ('ASSIGNED', 12, 'Peace Road', 880.00, 64, 64),

      -- Turkey
      ('ASSIGNED', 145, 'Istiklal Avenue', 1050.00, 65, 65),
      ('ASSIGNED', 80, 'Ataturk Boulevard', 890.00, 66, 66),
      ('ASSIGNED', 42, 'Kordon Street', 820.00, 67, 67),
      ('ASSIGNED', 18, 'Konyaalti Street', 970.00, 68, 68),

      -- Singapore
      ('ASSIGNED', 10, 'Orchard Road', 1200.00, 69, 69),
      ('ASSIGNED', 25, 'Tampines Street', 850.00, 70, 70),
      ('ASSIGNED', 40, 'Jurong Gateway Road', 780.00, 71, 71),
      ('ASSIGNED', 18, 'Yishun Avenue', 820.00, 72, 72),

      -- Austria
      ('ASSIGNED', 15, 'Karntner Strasse', 1050.00, 73, 73),
      ('ASSIGNED', 27, 'Getreidegasse', 820.00, 74, 74),
      ('ASSIGNED', 44, 'Maria-Theresien-Strasse', 780.00, 75, 75),
      ('ASSIGNED', 62, 'Herrengasse', 860.00, 76, 76),

      -- Belgium
      ('ASSIGNED', 50, 'Rue Neuve', 980.00, 77, 77),
      ('ASSIGNED', 30, 'Meir', 920.00, 78, 78),
      ('ASSIGNED', 22, 'Veldstraat', 790.00, 79, 79),
      ('ASSIGNED', 14, 'Steenstraat', 810.00, 80, 80);