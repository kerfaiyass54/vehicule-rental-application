INSERT INTO suppliers (
    supplier_name,
    email,
    phone,
    id_location
) VALUES
      -- Germany
      ('Berlin Mobility GmbH', 'contact@berlinmobility.de', '+49 30 24581001', 1),
      ('Bavaria Auto Rental GmbH', 'info@bavariaautorental.de', '+49 89 45872002', 2),
      ('Hamburg Drive Solutions GmbH', 'service@hamburgdrive.de', '+49 40 67392003', 3),
      ('Frankfurt Fleet Services GmbH', 'contact@frankfurtfleet.de', '+49 69 58214004', 4),

      -- France
      ('Paris Auto Services SAS', 'contact@parisautoservices.fr', '+33 1 45821005', 5),
      ('Lyon Mobility SARL', 'info@lyonmobility.fr', '+33 4 72183006', 6),
      ('Marseille Drive SAS', 'contact@marseilledrive.fr', '+33 4 91562007', 7),
      ('Nice Riviera Mobility', 'info@niceriviera.fr', '+33 4 93184008', 8),

      -- Italy
      ('Roma Mobility SRL', 'info@romamobility.it', '+39 06 45821009', 9),
      ('Milano Auto SRL', 'contact@milanoauto.it', '+39 02 73942010', 10),
      ('Torino Drive SRL', 'info@torinodrive.it', '+39 011 58231011', 11),
      ('Firenze Mobility SRL', 'contact@firenzemobility.it', '+39 055 62841012', 12),

      -- Spain
      ('Madrid Car Solutions SL', 'contact@madridcars.es', '+34 91 4821013', 13),
      ('Barcelona Mobility SL', 'info@barcelonamobility.es', '+34 93 5712014', 14),
      ('Valencia Auto Rental SL', 'contact@valenciaauto.es', '+34 96 3821015', 15),
      ('Sevilla Drive SL', 'info@sevilladrive.es', '+34 95 4216016', 16),

      -- United Kingdom
      ('London Drive Ltd', 'contact@londondrive.co.uk', '+44 20 79461017', 17),
      ('Manchester Mobility Ltd', 'info@manchestermobility.co.uk', '+44 161 5821018', 18),
      ('Birmingham Auto Hire Ltd', 'contact@birminghamauto.co.uk', '+44 121 4521019', 19),
      ('Edinburgh Fleet Ltd', 'info@edinburghfleet.co.uk', '+44 131 6821020', 20),

      -- United States
      ('New York Auto Group LLC', 'contact@nyautogroup.com', '+1 212 555 1021', 21),
      ('Los Angeles Mobility LLC', 'info@lamobility.com', '+1 213 555 1022', 22),
      ('Chicago Fleet Services LLC', 'contact@chicagofleet.com', '+1 312 555 1023', 23),
      ('San Francisco Drive LLC', 'info@sfdrive.com', '+1 415 555 1024', 24),

      -- Canada
      ('Toronto Mobility Inc.', 'contact@torontomobility.ca', '+1 416 555 1025', 25),
      ('Vancouver Auto Services Inc.', 'info@vancouverauto.ca', '+1 604 555 1026', 26),
      ('Montreal Drive Inc.', 'contact@montrealdrive.ca', '+1 514 555 1027', 27),
      ('Calgary Fleet Inc.', 'info@calgaryfleet.ca', '+1 403 555 1028', 28),

      -- Japan
      ('Tokyo Mobility KK', 'contact@tokyomobility.jp', '+81 3 4582 1029', 29),
      ('Osaka Auto Rental KK', 'info@osakaautorental.jp', '+81 6 5821 1030', 30),
      ('Kyoto Drive KK', 'contact@kyotodrive.jp', '+81 75 621 1031', 31),
      ('Yokohama Fleet KK', 'info@yokohamafleet.jp', '+81 45 582 1032', 32),

      -- South Korea
      ('Seoul Mobility Co.', 'contact@seoulmobility.kr', '+82 2 4582 1033', 33),
      ('Busan Auto Co.', 'info@busanauto.kr', '+82 51 5821 1034', 34),
      ('Incheon Drive Co.', 'contact@incheondrive.kr', '+82 32 6210 1035', 35),
      ('Daegu Fleet Co.', 'info@daegufleet.kr', '+82 53 5821 1036', 36),

      -- United Arab Emirates
      ('Dubai Auto Mobility LLC', 'contact@dubaimobility.ae', '+971 4 458 1037', 37),
      ('Abu Dhabi Fleet LLC', 'info@abudhabifleet.ae', '+971 2 582 1038', 38),
      ('Sharjah Drive LLC', 'contact@sharjahdrive.ae', '+971 6 621 1039', 39),
      ('Ajman Auto Services LLC', 'info@ajmanauto.ae', '+971 6 582 1040', 40),

      -- Switzerland
      ('Zurich Mobility AG', 'contact@zurichmobility.ch', '+41 44 458 1041', 41),
      ('Geneva Auto Services SA', 'info@genevaauto.ch', '+41 22 582 1042', 42),
      ('Basel Drive AG', 'contact@baseldrive.ch', '+41 61 621 1043', 43),
      ('Lausanne Fleet SA', 'info@lausannefleet.ch', '+41 21 582 1044', 44),

      -- Netherlands
      ('Amsterdam Mobility BV', 'contact@amsterdammobility.nl', '+31 20 458 1045', 45),
      ('Rotterdam Auto BV', 'info@rotterdamauto.nl', '+31 10 582 1046', 46),
      ('Utrecht Drive BV', 'contact@utrechtdrive.nl', '+31 30 621 1047', 47),
      ('The Hague Fleet BV', 'info@thehaguefleet.nl', '+31 70 582 1048', 48),

      -- Australia
      ('Sydney Mobility Pty Ltd', 'contact@sydneymobility.au', '+61 2 4582 1049', 49),
      ('Melbourne Auto Pty Ltd', 'info@melbourneauto.au', '+61 3 5821 1050', 50),
      ('Brisbane Drive Pty Ltd', 'contact@brisbanedrive.au', '+61 7 6210 1051', 51),
      ('Perth Fleet Pty Ltd', 'info@perthfleet.au', '+61 8 5821 1052', 52),

      -- Brazil
      ('Sao Paulo Mobilidade Ltda', 'contact@saopaulomobility.br', '+55 11 4582 1053', 53),
      ('Rio Drive Ltda', 'info@riodrive.br', '+55 21 5821 1054', 54),
      ('Brasilia Auto Ltda', 'contact@brasiliaauto.br', '+55 61 6210 1055', 55),
      ('Salvador Fleet Ltda', 'info@salvadorfleet.br', '+55 71 5821 1056', 56),

      -- Mexico
      ('Mexico City Mobility SA', 'contact@mexicocitymobility.mx', '+52 55 4582 1057', 57),
      ('Monterrey Auto SA', 'info@monterreyauto.mx', '+52 81 5821 1058', 58),
      ('Guadalajara Drive SA', 'contact@guadalajaradrive.mx', '+52 33 6210 1059', 59),
      ('Cancun Fleet SA', 'info@cancunfleet.mx', '+52 998 582 1060', 60),

      -- Egypt
      ('Cairo Mobility LLC', 'contact@cairomobility.eg', '+20 2 4582 1061', 61),
      ('Alexandria Auto LLC', 'info@alexandriaauto.eg', '+20 3 5821 1062', 62),
      ('Giza Drive LLC', 'contact@gizadrive.eg', '+20 2 6210 1063', 63),
      ('Sharm Fleet LLC', 'info@sharmfleet.eg', '+20 69 5821 1064', 64),

      -- Turkey
      ('Istanbul Mobility AS', 'contact@istanbulmobility.tr', '+90 212 458 1065', 65),
      ('Ankara Auto AS', 'info@ankaraauto.tr', '+90 312 582 1066', 66),
      ('Izmir Drive AS', 'contact@izmirdrive.tr', '+90 232 621 1067', 67),
      ('Antalya Fleet AS', 'info@antalyafleet.tr', '+90 242 582 1068', 68),

      -- Singapore
      ('Singapore Mobility Pte Ltd', 'contact@singaporemobility.sg', '+65 6582 1069', 69),
      ('Singapore East Auto Pte Ltd', 'info@sgeastauto.sg', '+65 6210 1070', 70),
      ('Singapore West Drive Pte Ltd', 'contact@sgwestdrive.sg', '+65 5821 1071', 71),
      ('Singapore North Fleet Pte Ltd', 'info@sgnorthfleet.sg', '+65 4582 1072', 72),

      -- Austria
      ('Vienna Mobility GmbH', 'contact@viennamobility.at', '+43 1 4582 1073', 73),
      ('Salzburg Auto GmbH', 'info@salzburgauto.at', '+43 662 5821 1074', 74),
      ('Innsbruck Drive GmbH', 'contact@innsbruckdrive.at', '+43 512 6210 1075', 75),
      ('Graz Fleet GmbH', 'info@grazfleet.at', '+43 316 5821 1076', 76),

      -- Belgium
      ('Brussels Mobility SRL', 'contact@brusselsmobility.be', '+32 2 4582 1077', 77),
      ('Antwerp Auto NV', 'info@antwerpauto.be', '+32 3 5821 1078', 78),
      ('Ghent Drive NV', 'contact@ghentdrive.be', '+32 9 6210 1079', 79),
      ('Bruges Fleet BV', 'info@brugesfleet.be', '+32 50 5821 1080', 80);