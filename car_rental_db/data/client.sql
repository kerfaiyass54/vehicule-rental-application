INSERT INTO clients (
    client_name,
    email,
    password_hash,
    role,
    budget,
    age,
    nationality,
    id_admin,
    id_location
) VALUES

      -- Germany
      ('Lukas Schneider', 'lukas.schneider@example.com', '$2a$10$client001', 'CLIENT', 2800.00, 34, 'German', 1, 1),
      ('Anna Weber', 'anna.weber@example.com', '$2a$10$client002', 'CLIENT', 1900.00, 29, 'German', 9, 2),
      ('Felix Wagner', 'felix.wagner@example.com', '$2a$10$client003', 'CLIENT', 3500.00, 41, 'German', 1, 3),
      ('Sophie Becker', 'sophie.becker@example.com', '$2a$10$client004', 'CLIENT', 2400.00, 31, 'German', 9, 4),
      ('Max Hoffmann', 'max.hoffmann@example.com', '$2a$10$client005', 'CLIENT', 1700.00, 27, 'German', 1, 1),

      -- France
      ('Jean Dupont', 'jean.dupont@example.com', '$2a$10$client006', 'CLIENT', 2600.00, 36, 'French', 2, 5),
      ('Marie Martin', 'marie.martin@example.com', '$2a$10$client007', 'CLIENT', 1800.00, 28, 'French', 7, 6),
      ('Lucas Bernard', 'lucas.bernard@example.com', '$2a$10$client008', 'CLIENT', 3200.00, 40, 'French', 2, 7),
      ('Claire Richard', 'claire.richard@example.com', '$2a$10$client009', 'CLIENT', 2200.00, 33, 'French', 7, 8),
      ('Antoine Petit', 'antoine.petit@example.com', '$2a$10$client010', 'CLIENT', 1500.00, 25, 'French', 2, 5),

      -- Italy
      ('Marco Rossi', 'marco.rossi@example.com', '$2a$10$client011', 'CLIENT', 2900.00, 35, 'Italian', 3, 9),
      ('Giulia Romano', 'giulia.romano@example.com', '$2a$10$client012', 'CLIENT', 2100.00, 30, 'Italian', 3, 10),
      ('Luca Ferrari', 'luca.ferrari@example.com', '$2a$10$client013', 'CLIENT', 3800.00, 42, 'Italian', 3, 11),
      ('Francesca Esposito', 'francesca.esposito@example.com', '$2a$10$client014', 'CLIENT', 1700.00, 27, 'Italian', 3, 12),
      ('Matteo Bianchi', 'matteo.bianchi@example.com', '$2a$10$client015', 'CLIENT', 2500.00, 34, 'Italian', 3, 9),

      -- Spain
      ('Carlos Garcia', 'carlos.garcia@example.com', '$2a$10$client016', 'CLIENT', 2300.00, 33, 'Spanish', 8, 13),
      ('Maria Lopez', 'maria.lopez@example.com', '$2a$10$client017', 'CLIENT', 1800.00, 28, 'Spanish', 8, 14),
      ('Javier Martinez', 'javier.martinez@example.com', '$2a$10$client018', 'CLIENT', 3100.00, 39, 'Spanish', 8, 15),
      ('Lucia Sanchez', 'lucia.sanchez@example.com', '$2a$10$client019', 'CLIENT', 2000.00, 30, 'Spanish', 8, 16),
      ('Diego Fernandez', 'diego.fernandez@example.com', '$2a$10$client020', 'CLIENT', 2700.00, 37, 'Spanish', 8, 13),

      -- United Kingdom
      ('James Wilson', 'james.wilson@example.com', '$2a$10$client021', 'CLIENT', 3500.00, 40, 'British', 4, 17),
      ('Emily Smith', 'emily.smith@example.com', '$2a$10$client022', 'CLIENT', 2200.00, 29, 'British', 4, 18),
      ('Oliver Brown', 'oliver.brown@example.com', '$2a$10$client023', 'CLIENT', 2800.00, 35, 'British', 4, 19),
      ('Charlotte Taylor', 'charlotte.taylor@example.com', '$2a$10$client024', 'CLIENT', 1900.00, 27, 'British', 4, 20),
      ('Harry Johnson', 'harry.johnson@example.com', '$2a$10$client025', 'CLIENT', 4100.00, 43, 'British', 4, 17),

      -- United States
      ('Michael Johnson', 'michael.johnson@example.com', '$2a$10$client026', 'CLIENT', 4200.00, 38, 'American', 4, 21),
      ('Olivia Davis', 'olivia.davis@example.com', '$2a$10$client027', 'CLIENT', 3000.00, 30, 'American', 4, 22),
      ('William Miller', 'william.miller@example.com', '$2a$10$client028', 'CLIENT', 5000.00, 45, 'American', 4, 23),
      ('Sophia Anderson', 'sophia.anderson@example.com', '$2a$10$client029', 'CLIENT', 2500.00, 28, 'American', 4, 24),
      ('Daniel Thompson', 'daniel.thompson@example.com', '$2a$10$client030', 'CLIENT', 3600.00, 36, 'American', 4, 21),

      -- Canada
      ('Ethan Wilson', 'ethan.wilson@example.com', '$2a$10$client031', 'CLIENT', 3200.00, 34, 'Canadian', 4, 25),
      ('Emma Brown', 'emma.brown@example.com', '$2a$10$client032', 'CLIENT', 2100.00, 27, 'Canadian', 4, 26),
      ('Noah Martin', 'noah.martin@example.com', '$2a$10$client033', 'CLIENT', 3900.00, 40, 'Canadian', 4, 27),
      ('Ava Taylor', 'ava.taylor@example.com', '$2a$10$client034', 'CLIENT', 2400.00, 31, 'Canadian', 4, 28),
      ('Liam Anderson', 'liam.anderson@example.com', '$2a$10$client035', 'CLIENT', 2800.00, 35, 'Canadian', 4, 25),

      -- Japan
      ('Kenji Tanaka', 'kenji.tanaka.client@example.com', '$2a$10$client036', 'CLIENT', 3300.00, 37, 'Japanese', 5, 29),
      ('Yuki Sato', 'yuki.sato@example.com', '$2a$10$client037', 'CLIENT', 2200.00, 29, 'Japanese', 10, 30),
      ('Hiroshi Suzuki', 'hiroshi.suzuki@example.com', '$2a$10$client038', 'CLIENT', 4100.00, 43, 'Japanese', 5, 31),
      ('Aiko Nakamura', 'aiko.nakamura@example.com', '$2a$10$client039', 'CLIENT', 1800.00, 26, 'Japanese', 10, 32),
      ('Daichi Watanabe', 'daichi.watanabe@example.com', '$2a$10$client040', 'CLIENT', 2900.00, 35, 'Japanese', 5, 29),

      -- South Korea
      ('Min-jun Kim', 'minjun.kim@example.com', '$2a$10$client041', 'CLIENT', 2700.00, 32, 'South Korean', 5, 33),
      ('Seo-yeon Park', 'seoyeon.park@example.com', '$2a$10$client042', 'CLIENT', 2100.00, 28, 'South Korean', 5, 34),
      ('Ji-ho Lee', 'jiho.lee@example.com', '$2a$10$client043', 'CLIENT', 3600.00, 39, 'South Korean', 10, 35),
      ('Ha-eun Choi', 'haeun.choi@example.com', '$2a$10$client044', 'CLIENT', 1900.00, 25, 'South Korean', 10, 36),
      ('Joon-ho Kang', 'joonho.kang@example.com', '$2a$10$client045', 'CLIENT', 4000.00, 43, 'South Korean', 5, 33),

      -- United Arab Emirates
      ('Ahmed Al Mansouri', 'ahmed.almansouri@example.com', '$2a$10$client046', 'CLIENT', 5500.00, 40, 'Emirati', 6, 37),
      ('Fatima Al Zahra', 'fatima.alzahra@example.com', '$2a$10$client047', 'CLIENT', 4200.00, 32, 'Emirati', 6, 38),
      ('Khalid Mohammed', 'khalid.mohammed@example.com', '$2a$10$client048', 'CLIENT', 6000.00, 45, 'Emirati', 6, 39),
      ('Maryam Hassan', 'maryam.hassan@example.com', '$2a$10$client049', 'CLIENT', 3800.00, 29, 'Emirati', 6, 40),
      ('Omar Khalifa', 'omar.khalifa@example.com', '$2a$10$client050', 'CLIENT', 4700.00, 37, 'Emirati', 6, 37),

      -- Switzerland
      ('Hans Meier', 'hans.meier@example.com', '$2a$10$client051', 'CLIENT', 4200.00, 46, 'Swiss', 9, 41),
      ('Anna Keller', 'anna.keller@example.com', '$2a$10$client052', 'CLIENT', 3100.00, 38, 'Swiss', 9, 42),
      ('Peter Fischer', 'peter.fischer@example.com', '$2a$10$client053', 'CLIENT', 2300.00, 33, 'Swiss', 9, 43),
      ('Nina Weber', 'nina.weber@example.com', '$2a$10$client054', 'CLIENT', 1900.00, 29, 'Swiss', 9, 44),
      ('Lukas Baumann', 'lukas.baumann@example.com', '$2a$10$client055', 'CLIENT', 3600.00, 41, 'Swiss', 9, 41),

      -- Netherlands
      ('Jan de Vries', 'jan.devries@example.com', '$2a$10$client056', 'CLIENT', 3000.00, 37, 'Dutch', 2, 45),
      ('Sophie Jansen', 'sophie.jansen@example.com', '$2a$10$client057', 'CLIENT', 2100.00, 28, 'Dutch', 2, 46),
      ('Pieter Bakker', 'pieter.bakker@example.com', '$2a$10$client058', 'CLIENT', 3500.00, 43, 'Dutch', 2, 47),
      ('Emma Visser', 'emma.visser@example.com', '$2a$10$client059', 'CLIENT', 2400.00, 30, 'Dutch', 2, 48),
      ('Daan Smit', 'daan.smit@example.com', '$2a$10$client060', 'CLIENT', 1700.00, 24, 'Dutch', 2, 45),

      -- Australia
      ('Jack Williams', 'jack.williams@example.com', '$2a$10$client061', 'CLIENT', 3700.00, 35, 'Australian', 4, 49),
      ('Isla Jones', 'isla.jones@example.com', '$2a$10$client062', 'CLIENT', 2300.00, 28, 'Australian', 4, 50),
      ('Noah Brown', 'noah.brown@example.com', '$2a$10$client063', 'CLIENT', 4200.00, 41, 'Australian', 4, 51),
      ('Mia Wilson', 'mia.wilson@example.com', '$2a$10$client064', 'CLIENT', 1900.00, 26, 'Australian', 4, 52),
      ('Oliver Smith', 'oliver.smith@example.com', '$2a$10$client065', 'CLIENT', 3100.00, 34, 'Australian', 4, 49),

      -- Brazil
      ('Lucas Silva', 'lucas.silva@example.com', '$2a$10$client066', 'CLIENT', 2800.00, 33, 'Brazilian', 8, 53),
      ('Mariana Santos', 'mariana.santos@example.com', '$2a$10$client067', 'CLIENT', 1900.00, 27, 'Brazilian', 8, 54),
      ('Gabriel Oliveira', 'gabriel.oliveira@example.com', '$2a$10$client068', 'CLIENT', 3400.00, 39, 'Brazilian', 8, 55),
      ('Beatriz Costa', 'beatriz.costa@example.com', '$2a$10$client069', 'CLIENT', 2200.00, 31, 'Brazilian', 8, 56),
      ('Rafael Pereira', 'rafael.pereira@example.com', '$2a$10$client070', 'CLIENT', 3000.00, 36, 'Brazilian', 8, 53),

      -- Mexico
      ('Carlos Hernandez', 'carlos.hernandez@example.com', '$2a$10$client071', 'CLIENT', 2600.00, 34, 'Mexican', 8, 57),
      ('Sofia Martinez', 'sofia.martinez@example.com', '$2a$10$client072', 'CLIENT', 2000.00, 28, 'Mexican', 8, 58),
      ('Diego Rodriguez', 'diego.rodriguez@example.com', '$2a$10$client073', 'CLIENT', 3200.00, 40, 'Mexican', 8, 59),
      ('Valeria Lopez', 'valeria.lopez@example.com', '$2a$10$client074', 'CLIENT', 1800.00, 26, 'Mexican', 8, 60),
      ('Miguel Garcia', 'miguel.garcia@example.com', '$2a$10$client075', 'CLIENT', 2900.00, 37, 'Mexican', 8, 57),

      -- Egypt
      ('Omar Hassan', 'omar.hassan.client@example.com', '$2a$10$client076', 'CLIENT', 2200.00, 33, 'Egyptian', 6, 61),
      ('Sara Ahmed', 'sara.ahmed@example.com', '$2a$10$client077', 'CLIENT', 1600.00, 26, 'Egyptian', 6, 62),
      ('Mohamed Ali', 'mohamed.ali@example.com', '$2a$10$client078', 'CLIENT', 3000.00, 38, 'Egyptian', 6, 63),
      ('Fatma Mahmoud', 'fatma.mahmoud@example.com', '$2a$10$client079', 'CLIENT', 1900.00, 30, 'Egyptian', 6, 64),
      ('Karim Mostafa', 'karim.mostafa@example.com', '$2a$10$client080', 'CLIENT', 2700.00, 35, 'Egyptian', 6, 61),

      -- Turkey
      ('Mehmet Yilmaz', 'mehmet.yilmaz@example.com', '$2a$10$client081', 'CLIENT', 2500.00, 36, 'Turkish', 3, 65),
      ('Elif Kaya', 'elif.kaya@example.com', '$2a$10$client082', 'CLIENT', 1800.00, 27, 'Turkish', 3, 66),
      ('Ahmet Demir', 'ahmet.demir@example.com', '$2a$10$client083', 'CLIENT', 3300.00, 41, 'Turkish', 3, 67),
      ('Zeynep Aydin', 'zeynep.aydin@example.com', '$2a$10$client084', 'CLIENT', 2100.00, 29, 'Turkish', 3, 68),
      ('Emre Kaya', 'emre.kaya@example.com', '$2a$10$client085', 'CLIENT', 2900.00, 34, 'Turkish', 3, 65),

      -- Singapore
      ('Ethan Lim', 'ethan.lim@example.com', '$2a$10$client086', 'CLIENT', 3400.00, 35, 'Singaporean', 5, 69),
      ('Chloe Tan', 'chloe.tan@example.com', '$2a$10$client087', 'CLIENT', 2400.00, 28, 'Singaporean', 5, 70),
      ('Ryan Wong', 'ryan.wong@example.com', '$2a$10$client088', 'CLIENT', 3900.00, 39, 'Singaporean', 10, 71),
      ('Rachel Lee', 'rachel.lee@example.com', '$2a$10$client089', 'CLIENT', 2200.00, 30, 'Singaporean', 10, 72),
      ('Daniel Koh', 'daniel.koh@example.com', '$2a$10$client090', 'CLIENT', 3100.00, 37, 'Singaporean', 5, 69),

      -- Austria
      ('Markus Gruber', 'markus.gruber@example.com', '$2a$10$client091', 'CLIENT', 3000.00, 36, 'Austrian', 9, 73),
      ('Anna Hofer', 'anna.hofer@example.com', '$2a$10$client092', 'CLIENT', 1900.00, 27, 'Austrian', 9, 74),
      ('Felix Steiner', 'felix.steiner@example.com', '$2a$10$client093', 'CLIENT', 3500.00, 39, 'Austrian', 9, 75),
      ('Laura Leitner', 'laura.leitner@example.com', '$2a$10$client094', 'CLIENT', 2200.00, 31, 'Austrian', 9, 76),
      ('David Moser', 'david.moser@example.com', '$2a$10$client095', 'CLIENT', 3800.00, 44, 'Austrian', 9, 73),

      -- Belgium
      ('Pierre Lambert', 'pierre.lambert@example.com', '$2a$10$client096', 'CLIENT', 2900.00, 35, 'Belgian', 7, 77),
      ('Julie Dubois', 'julie.dubois@example.com', '$2a$10$client097', 'CLIENT', 2000.00, 32, 'Belgian', 7, 78),
      ('Thomas Peeters', 'thomas.peeters@example.com', '$2a$10$client098', 'CLIENT', 3800.00, 40, 'Belgian', 7, 79),
      ('Laura Janssens', 'laura.janssens@example.com', '$2a$10$client099', 'CLIENT', 1700.00, 26, 'Belgian', 7, 80),
      ('Nicolas Maes', 'nicolas.maes@example.com', '$2a$10$client100', 'CLIENT', 2600.00, 34, 'Belgian', 7, 77);