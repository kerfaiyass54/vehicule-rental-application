INSERT INTO repairs (
    email,
    name,
    password_hash,
    role,
    id_admin,
    id_location,
    speciality
) VALUES
      ('repairer01@example.com', 'Karim Ben Salah', '$2a$10$repair001', 'REPAIRER', 1, 1, 'Engine'),
      ('repairer02@example.com', 'Mehdi Trabelsi', '$2a$10$repair002', 'REPAIRER', 2, 2, 'Transmission'),
      ('repairer03@example.com', 'Omar Gharbi', '$2a$10$repair003', 'REPAIRER', 3, 3, 'Brakes'),
      ('repairer04@example.com', 'Sami Jaziri', '$2a$10$repair004', 'REPAIRER', 1, 4, 'Electrical'),
      ('repairer05@example.com', 'Nabil Ayari', '$2a$10$repair005', 'REPAIRER', 2, 5, 'Bodywork'),

      ('repairer06@example.com', 'Hans Becker', '$2a$10$repair006', 'REPAIRER', 3, 6, 'Engine'),
      ('repairer07@example.com', 'Felix Schneider', '$2a$10$repair007', 'REPAIRER', 1, 7, 'Transmission'),
      ('repairer08@example.com', 'Lukas Weber', '$2a$10$repair008', 'REPAIRER', 2, 8, 'Electrical'),
      ('repairer09@example.com', 'Max Wagner', '$2a$10$repair009', 'REPAIRER', 3, 9, 'Brakes'),
      ('repairer10@example.com', 'Paul Hoffmann', '$2a$10$repair010', 'REPAIRER', 1, 10, 'Bodywork'),

      ('repairer11@example.com', 'Jean Martin', '$2a$10$repair011', 'REPAIRER', 2, 11, 'Engine'),
      ('repairer12@example.com', 'Pierre Bernard', '$2a$10$repair012', 'REPAIRER', 3, 12, 'Transmission'),
      ('repairer13@example.com', 'Antoine Dubois', '$2a$10$repair013', 'REPAIRER', 1, 13, 'Brakes'),
      ('repairer14@example.com', 'Lucas Moreau', '$2a$10$repair014', 'REPAIRER', 2, 14, 'Electrical'),
      ('repairer15@example.com', 'Hugo Laurent', '$2a$10$repair015', 'REPAIRER', 3, 15, 'Bodywork'),

      ('repairer16@example.com', 'Carlos Garcia', '$2a$10$repair016', 'REPAIRER', 1, 16, 'Engine'),
      ('repairer17@example.com', 'Javier Lopez', '$2a$10$repair017', 'REPAIRER', 2, 17, 'Transmission'),
      ('repairer18@example.com', 'Diego Martinez', '$2a$10$repair018', 'REPAIRER', 3, 18, 'Electrical'),
      ('repairer19@example.com', 'Pablo Fernandez', '$2a$10$repair019', 'REPAIRER', 1, 19, 'Brakes'),
      ('repairer20@example.com', 'Alejandro Ruiz', '$2a$10$repair020', 'REPAIRER', 2, 20, 'Bodywork'),

      ('repairer21@example.com', 'Marco Rossi', '$2a$10$repair021', 'REPAIRER', 3, 1, 'Engine'),
      ('repairer22@example.com', 'Luca Ferrari', '$2a$10$repair022', 'REPAIRER', 1, 2, 'Transmission'),
      ('repairer23@example.com', 'Matteo Bianchi', '$2a$10$repair023', 'REPAIRER', 2, 3, 'Brakes'),
      ('repairer24@example.com', 'Andrea Romano', '$2a$10$repair024', 'REPAIRER', 3, 4, 'Electrical'),
      ('repairer25@example.com', 'Davide Conti', '$2a$10$repair025', 'REPAIRER', 1, 5, 'Bodywork'),

      ('repairer26@example.com', 'Jan de Vries', '$2a$10$repair026', 'REPAIRER', 2, 6, 'Engine'),
      ('repairer27@example.com', 'Pieter Jansen', '$2a$10$repair027', 'REPAIRER', 3, 7, 'Transmission'),
      ('repairer28@example.com', 'Daan Bakker', '$2a$10$repair028', 'REPAIRER', 1, 8, 'Electrical'),
      ('repairer29@example.com', 'Thomas Smit', '$2a$10$repair029', 'REPAIRER', 2, 9, 'Brakes'),
      ('repairer30@example.com', 'Willem Visser', '$2a$10$repair030', 'REPAIRER', 3, 10, 'Bodywork'),

      ('repairer31@example.com', 'James Wilson', '$2a$10$repair031', 'REPAIRER', 1, 11, 'Engine'),
      ('repairer32@example.com', 'Oliver Brown', '$2a$10$repair032', 'REPAIRER', 2, 12, 'Transmission'),
      ('repairer33@example.com', 'William Taylor', '$2a$10$repair033', 'REPAIRER', 3, 13, 'Brakes'),
      ('repairer34@example.com', 'George Smith', '$2a$10$repair034', 'REPAIRER', 1, 14, 'Electrical'),
      ('repairer35@example.com', 'Harry Johnson', '$2a$10$repair035', 'REPAIRER', 2, 15, 'Bodywork'),

      ('repairer36@example.com', 'Kenji Tanaka', '$2a$10$repair036', 'REPAIRER', 3, 16, 'Engine'),
      ('repairer37@example.com', 'Hiroshi Suzuki', '$2a$10$repair037', 'REPAIRER', 1, 17, 'Transmission'),
      ('repairer38@example.com', 'Daichi Watanabe', '$2a$10$repair038', 'REPAIRER', 2, 18, 'Electrical'),
      ('repairer39@example.com', 'Yuki Nakamura', '$2a$10$repair039', 'REPAIRER', 3, 19, 'Brakes'),
      ('repairer40@example.com', 'Haruto Sato', '$2a$10$repair040', 'REPAIRER', 1, 20, 'Bodywork'),

      ('repairer41@example.com', 'Min-jun Kim', '$2a$10$repair041', 'REPAIRER', 2, 1, 'Engine'),
      ('repairer42@example.com', 'Ji-ho Lee', '$2a$10$repair042', 'REPAIRER', 3, 2, 'Transmission'),
      ('repairer43@example.com', 'Joon-ho Park', '$2a$10$repair043', 'REPAIRER', 1, 3, 'Brakes'),
      ('repairer44@example.com', 'Seo-jun Choi', '$2a$10$repair044', 'REPAIRER', 2, 4, 'Electrical'),
      ('repairer45@example.com', 'Hyun-woo Kang', '$2a$10$repair045', 'REPAIRER', 3, 5, 'Bodywork'),

      ('repairer46@example.com', 'Ahmed Hassan', '$2a$10$repair046', 'REPAIRER', 1, 6, 'Engine'),
      ('repairer47@example.com', 'Mohamed Ali', '$2a$10$repair047', 'REPAIRER', 2, 7, 'Transmission'),
      ('repairer48@example.com', 'Omar Mahmoud', '$2a$10$repair048', 'REPAIRER', 3, 8, 'Electrical'),
      ('repairer49@example.com', 'Karim Mostafa', '$2a$10$repair049', 'REPAIRER', 1, 9, 'Brakes'),
      ('repairer50@example.com', 'Youssef Khaled', '$2a$10$repair050', 'REPAIRER', 2, 10, 'Bodywork');