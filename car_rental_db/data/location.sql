INSERT INTO locations (
    location_name,
    country,
    position,
    id_admin
) VALUES
      -- Germany
      ('Berlin Center', 'Germany', '52.5200,13.4050', 1),
      ('Munich Center', 'Germany', '48.1351,11.5820', 1),
      ('Hamburg Center', 'Germany', '53.5511,9.9937', 9),
      ('Frankfurt Center', 'Germany', '50.1109,8.6821', 9),

      -- France
      ('Paris Center', 'France', '48.8566,2.3522', 2),
      ('Lyon Center', 'France', '45.7640,4.8357', 2),
      ('Marseille Center', 'France', '43.2965,5.3698', 7),
      ('Nice Center', 'France', '43.7102,7.2620', 7),

      -- Italy
      ('Rome Center', 'Italy', '41.9028,12.4964', 3),
      ('Milan Center', 'Italy', '45.4642,9.1900', 3),
      ('Turin Center', 'Italy', '45.0703,7.6869', 3),
      ('Florence Center', 'Italy', '43.7696,11.2558', 3),

      -- Spain
      ('Madrid Center', 'Spain', '40.4168,-3.7038', 8),
      ('Barcelona Center', 'Spain', '41.3874,2.1686', 8),
      ('Valencia Center', 'Spain', '39.4699,-0.3763', 8),
      ('Seville Center', 'Spain', '37.3891,-5.9845', 8),

      -- United Kingdom
      ('London Center', 'United Kingdom', '51.5074,-0.1278', 4),
      ('Manchester Center', 'United Kingdom', '53.4808,-2.2426', 4),
      ('Birmingham Center', 'United Kingdom', '52.4862,-1.8904', 4),
      ('Edinburgh Center', 'United Kingdom', '55.9533,-3.1883', 4),

      -- United States
      ('New York Center', 'United States', '40.7128,-74.0060', 4),
      ('Los Angeles Center', 'United States', '34.0522,-118.2437', 4),
      ('Chicago Center', 'United States', '41.8781,-87.6298', 4),
      ('San Francisco Center', 'United States', '37.7749,-122.4194', 4),

      -- Canada
      ('Toronto Center', 'Canada', '43.6532,-79.3832', 4),
      ('Vancouver Center', 'Canada', '49.2827,-123.1207', 4),
      ('Montreal Center', 'Canada', '45.5017,-73.5673', 4),
      ('Calgary Center', 'Canada', '51.0447,-114.0719', 4),

      -- Japan
      ('Tokyo Center', 'Japan', '35.6762,139.6503', 5),
      ('Osaka Center', 'Japan', '34.6937,135.5023', 5),
      ('Kyoto Center', 'Japan', '35.0116,135.7681', 10),
      ('Yokohama Center', 'Japan', '35.4437,139.6380', 10),

      -- South Korea
      ('Seoul Center', 'South Korea', '37.5665,126.9780', 5),
      ('Busan Center', 'South Korea', '35.1796,129.0756', 5),
      ('Incheon Center', 'South Korea', '37.4563,126.7052', 10),
      ('Daegu Center', 'South Korea', '35.8714,128.6014', 10),

      -- United Arab Emirates
      ('Dubai Center', 'United Arab Emirates', '25.2048,55.2708', 6),
      ('Abu Dhabi Center', 'United Arab Emirates', '24.4539,54.3773', 6),
      ('Sharjah Center', 'United Arab Emirates', '25.3463,55.4209', 6),
      ('Ajman Center', 'United Arab Emirates', '25.4052,55.5136', 6),

      -- Switzerland
      ('Zurich Center', 'Switzerland', '47.3769,8.5417', 9),
      ('Geneva Center', 'Switzerland', '46.2044,6.1432', 9),
      ('Basel Center', 'Switzerland', '47.5596,7.5886', 9),
      ('Lausanne Center', 'Switzerland', '46.5197,6.6323', 9),

      -- Netherlands
      ('Amsterdam Center', 'Netherlands', '52.3676,4.9041', 2),
      ('Rotterdam Center', 'Netherlands', '51.9244,4.4777', 2),
      ('Utrecht Center', 'Netherlands', '52.0907,5.1214', 2),
      ('The Hague Center', 'Netherlands', '52.0705,4.3007', 2),

      -- Australia
      ('Sydney Center', 'Australia', '-33.8688,151.2093', 4),
      ('Melbourne Center', 'Australia', '-37.8136,144.9631', 4),
      ('Brisbane Center', 'Australia', '-27.4698,153.0251', 4),
      ('Perth Center', 'Australia', '-31.9505,115.8605', 4),

      -- Brazil
      ('Sao Paulo Center', 'Brazil', '-23.5505,-46.6333', 8),
      ('Rio de Janeiro Center', 'Brazil', '-22.9068,-43.1729', 8),
      ('Brasilia Center', 'Brazil', '-15.7975,-47.8919', 8),
      ('Salvador Center', 'Brazil', '-12.9777,-38.5016', 8),

      -- Mexico
      ('Mexico City Center', 'Mexico', '19.4326,-99.1332', 8),
      ('Monterrey Center', 'Mexico', '25.6866,-100.3161', 8),
      ('Guadalajara Center', 'Mexico', '20.6597,-103.3496', 8),
      ('Cancun Center', 'Mexico', '21.1619,-86.8515', 8),

      -- Egypt
      ('Cairo Center', 'Egypt', '30.0444,31.2357', 6),
      ('Alexandria Center', 'Egypt', '31.2001,29.9187', 6),
      ('Giza Center', 'Egypt', '30.0131,31.2089', 6),
      ('Sharm El Sheikh Center', 'Egypt', '27.9158,34.3299', 6),

      -- Turkey
      ('Istanbul Center', 'Turkey', '41.0082,28.9784', 3),
      ('Ankara Center', 'Turkey', '39.9334,32.8597', 3),
      ('Izmir Center', 'Turkey', '38.4237,27.1428', 3),
      ('Antalya Center', 'Turkey', '36.8969,30.7133', 3),

      -- Singapore
      ('Singapore Central', 'Singapore', '1.3521,103.8198', 5),
      ('Singapore East', 'Singapore', '1.3571,103.9882', 5),
      ('Singapore West', 'Singapore', '1.3521,103.7000', 10),
      ('Singapore North', 'Singapore', '1.4382,103.7890', 10),

      -- Austria
      ('Vienna Center', 'Austria', '48.2082,16.3738', 9),
      ('Salzburg Center', 'Austria', '47.8095,13.0550', 9),
      ('Innsbruck Center', 'Austria', '47.2692,11.4041', 9),
      ('Graz Center', 'Austria', '47.0707,15.4395', 9),

      -- Belgium
      ('Brussels Center', 'Belgium', '50.8503,4.3517', 7),
      ('Antwerp Center', 'Belgium', '51.2194,4.4025', 7),
      ('Ghent Center', 'Belgium', '51.0543,3.7174', 7),
      ('Bruges Center', 'Belgium', '51.2093,3.2247', 7);