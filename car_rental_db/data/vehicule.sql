-- ============================================
-- INTERNATIONAL VEHICLES
-- ============================================

INSERT INTO vehicles (
    vehicle_name,
    brand,
    color,
    price,
    max_speed,
    transmission,
    vehicle_status,
    id_supplier
)
SELECT
    vehicle_name || ' ' || model AS vehicle_name,
    brand,
    color,
    price,
    max_speed,

    CASE
        WHEN series % 3 = 0 THEN 'MANUAL'
        ELSE 'AUTOMATIC'
        END AS transmission,

    CASE
        WHEN vehicle_status = 'RENTED' THEN 'TAKEN'
        WHEN vehicle_status = 'REPAIR' THEN 'REPARATION'
        ELSE 'AVAILABLE'
        END AS vehicle_status,

    id_supplier

FROM (
         SELECT
             ROW_NUMBER() OVER () AS series,
             data.*
         FROM (
                  VALUES

                      -- Germany
                      ('BMW 3 Series', 'BMW', '320i', 'Black', 58.00, 235, 'AVAILABLE', 1),
                      ('BMW 5 Series', 'BMW', '530d', 'White', 82.00, 250, 'AVAILABLE', 1),
                      ('Mercedes C-Class', 'Mercedes-Benz', 'C220d', 'Silver', 75.00, 245, 'AVAILABLE', 2),
                      ('Mercedes E-Class', 'Mercedes-Benz', 'E300', 'Black', 95.00, 250, 'RENTED', 2),
                      ('Audi A4', 'Audi', '40 TFSI', 'Grey', 68.00, 240, 'AVAILABLE', 3),
                      ('Audi A6', 'Audi', '45 TFSI', 'Blue', 88.00, 250, 'AVAILABLE', 3),
                      ('Volkswagen Golf', 'Volkswagen', 'Golf 8', 'Red', 45.00, 225, 'AVAILABLE', 4),
                      ('Volkswagen Passat', 'Volkswagen', 'Passat', 'Black', 58.00, 235, 'REPAIR', 4),
                      ('Porsche Macan', 'Porsche', 'Macan', 'White', 145.00, 254, 'AVAILABLE', 1),
                      ('Porsche 911', 'Porsche', 'Carrera', 'Yellow', 280.00, 293, 'AVAILABLE', 2),

                      -- France
                      ('Peugeot 208', 'Peugeot', '208', 'Blue', 42.00, 208, 'AVAILABLE', 5),
                      ('Peugeot 3008', 'Peugeot', '3008', 'Grey', 60.00, 220, 'AVAILABLE', 5),
                      ('Renault Clio', 'Renault', 'Clio', 'Red', 40.00, 200, 'RENTED', 6),
                      ('Renault Megane', 'Renault', 'Megane', 'Black', 52.00, 215, 'AVAILABLE', 6),
                      ('Citroen C3', 'Citroen', 'C3', 'White', 38.00, 195, 'AVAILABLE', 7),
                      ('Citroen C5 Aircross', 'Citroen', 'C5 Aircross', 'Grey', 62.00, 215, 'AVAILABLE', 7),
                      ('DS 7', 'DS', 'DS 7', 'Black', 78.00, 225, 'AVAILABLE', 8),
                      ('Alpine A110', 'Alpine', 'A110', 'Blue', 160.00, 250, 'AVAILABLE', 8),
                      ('Peugeot 508', 'Peugeot', '508', 'Silver', 65.00, 230, 'REPAIR', 5),
                      ('Renault Austral', 'Renault', 'Austral', 'Green', 70.00, 215, 'AVAILABLE', 6),

                      -- Italy
                      ('Fiat 500', 'Fiat', '500', 'White', 38.00, 180, 'AVAILABLE', 9),
                      ('Fiat Tipo', 'Fiat', 'Tipo', 'Grey', 42.00, 190, 'AVAILABLE', 9),
                      ('Alfa Romeo Giulia', 'Alfa Romeo', 'Giulia', 'Red', 78.00, 240, 'AVAILABLE', 10),
                      ('Alfa Romeo Stelvio', 'Alfa Romeo', 'Stelvio', 'Black', 88.00, 230, 'RENTED', 10),
                      ('Ferrari Roma', 'Ferrari', 'Roma', 'Red', 450.00, 320, 'AVAILABLE', 11),
                      ('Ferrari 296 GTB', 'Ferrari', '296 GTB', 'Yellow', 520.00, 330, 'AVAILABLE', 11),
                      ('Lamborghini Huracan', 'Lamborghini', 'Huracan', 'Orange', 550.00, 325, 'AVAILABLE', 12),
                      ('Maserati Ghibli', 'Maserati', 'Ghibli', 'Blue', 180.00, 285, 'AVAILABLE', 12),
                      ('Fiat Panda', 'Fiat', 'Panda', 'Green', 35.00, 175, 'REPAIR', 9),
                      ('Maserati Levante', 'Maserati', 'Levante', 'Black', 165.00, 270, 'AVAILABLE', 12),

                      -- Spain
                      ('Seat Ibiza', 'SEAT', 'Ibiza', 'Red', 39.00, 195, 'AVAILABLE', 13),
                      ('Seat Leon', 'SEAT', 'Leon', 'White', 48.00, 215, 'AVAILABLE', 13),
                      ('Cupra Formentor', 'Cupra', 'Formentor', 'Grey', 72.00, 245, 'AVAILABLE', 14),
                      ('Cupra Leon', 'Cupra', 'Leon', 'Black', 68.00, 245, 'RENTED', 14),
                      ('Mercedes A-Class', 'Mercedes-Benz', 'A200', 'Blue', 62.00, 225, 'AVAILABLE', 15),
                      ('BMW X1', 'BMW', 'X1', 'White', 70.00, 230, 'AVAILABLE', 15),
                      ('Audi Q3', 'Audi', 'Q3', 'Grey', 75.00, 235, 'AVAILABLE', 16),
                      ('Volkswagen T-Roc', 'Volkswagen', 'T-Roc', 'Black', 58.00, 215, 'AVAILABLE', 16),
                      ('Seat Ateca', 'SEAT', 'Ateca', 'Silver', 60.00, 215, 'REPAIR', 13),
                      ('Cupra Born', 'Cupra', 'Born', 'Green', 65.00, 160, 'AVAILABLE', 14),

                      -- United Kingdom
                      ('Mini Cooper', 'Mini', 'Cooper S', 'Red', 55.00, 235, 'AVAILABLE', 17),
                      ('Mini Countryman', 'Mini', 'Countryman', 'Green', 62.00, 220, 'AVAILABLE', 17),
                      ('Range Rover Evoque', 'Land Rover', 'Evoque', 'Black', 105.00, 221, 'AVAILABLE', 18),
                      ('Range Rover Sport', 'Land Rover', 'Sport', 'White', 145.00, 242, 'RENTED', 18),
                      ('Jaguar XE', 'Jaguar', 'XE', 'Blue', 82.00, 250, 'AVAILABLE', 19),
                      ('Jaguar F-Pace', 'Jaguar', 'F-Pace', 'Grey', 110.00, 250, 'AVAILABLE', 19),
                      ('Aston Martin Vantage', 'Aston Martin', 'Vantage', 'Green', 350.00, 314, 'AVAILABLE', 20),
                      ('Bentley Continental GT', 'Bentley', 'Continental GT', 'Black', 450.00, 318, 'AVAILABLE', 20),
                      ('Lotus Emira', 'Lotus', 'Emira', 'Yellow', 210.00, 290, 'REPAIR', 17),
                      ('McLaren Artura', 'McLaren', 'Artura', 'Orange', 390.00, 330, 'AVAILABLE', 20),

                      -- USA
                      ('Ford Mustang', 'Ford', 'Mustang GT', 'Blue', 120.00, 250, 'AVAILABLE', 21),
                      ('Ford Explorer', 'Ford', 'Explorer', 'Black', 90.00, 210, 'AVAILABLE', 21),
                      ('Chevrolet Camaro', 'Chevrolet', 'Camaro SS', 'Red', 125.00, 290, 'AVAILABLE', 22),
                      ('Chevrolet Tahoe', 'Chevrolet', 'Tahoe', 'White', 115.00, 210, 'RENTED', 22),
                      ('Tesla Model 3', 'Tesla', 'Model 3', 'White', 70.00, 201, 'AVAILABLE', 23),
                      ('Tesla Model Y', 'Tesla', 'Model Y', 'Black', 78.00, 217, 'AVAILABLE', 23),
                      ('Jeep Wrangler', 'Jeep', 'Wrangler', 'Green', 95.00, 180, 'AVAILABLE', 24),
                      ('Jeep Grand Cherokee', 'Jeep', 'Grand Cherokee', 'Grey', 110.00, 200, 'AVAILABLE', 24),
                      ('Dodge Challenger', 'Dodge', 'Challenger R/T', 'Orange', 135.00, 290, 'REPAIR', 21),
                      ('Ford Bronco', 'Ford', 'Bronco', 'Yellow', 105.00, 200, 'AVAILABLE', 22),

                      -- Canada
                      ('Tesla Model 3', 'Tesla', 'Model 3', 'Blue', 68.00, 201, 'AVAILABLE', 25),
                      ('Tesla Model X', 'Tesla', 'Model X', 'White', 125.00, 250, 'AVAILABLE', 25),
                      ('BMW X3', 'BMW', 'X3', 'Black', 82.00, 240, 'AVAILABLE', 26),
                      ('BMW X5', 'BMW', 'X5', 'Grey', 110.00, 250, 'RENTED', 26),
                      ('Audi Q5', 'Audi', 'Q5', 'White', 88.00, 240, 'AVAILABLE', 27),
                      ('Audi Q7', 'Audi', 'Q7', 'Black', 105.00, 250, 'AVAILABLE', 27),
                      ('Lexus RX', 'Lexus', 'RX 350', 'Silver', 92.00, 210, 'AVAILABLE', 28),
                      ('Toyota Highlander', 'Toyota', 'Highlander', 'Blue', 80.00, 190, 'AVAILABLE', 28),
                      ('Ford F-150', 'Ford', 'F-150', 'Red', 115.00, 180, 'REPAIR', 25),
                      ('Toyota RAV4', 'Toyota', 'RAV4', 'Green', 68.00, 190, 'AVAILABLE', 26),

                      -- Japan
                      ('Toyota Corolla', 'Toyota', 'Corolla', 'White', 42.00, 180, 'AVAILABLE', 29),
                      ('Toyota Camry', 'Toyota', 'Camry', 'Black', 55.00, 200, 'AVAILABLE', 29),
                      ('Honda Civic', 'Honda', 'Civic', 'Blue', 48.00, 200, 'AVAILABLE', 30),
                      ('Honda Accord', 'Honda', 'Accord', 'Grey', 60.00, 205, 'RENTED', 30),
                      ('Nissan Qashqai', 'Nissan', 'Qashqai', 'White', 58.00, 200, 'AVAILABLE', 31),
                      ('Nissan Z', 'Nissan', 'Z', 'Yellow', 110.00, 250, 'AVAILABLE', 31),
                      ('Mazda CX-5', 'Mazda', 'CX-5', 'Red', 65.00, 195, 'AVAILABLE', 32),
                      ('Mazda MX-5', 'Mazda', 'MX-5', 'Blue', 75.00, 220, 'AVAILABLE', 32),
                      ('Toyota Land Cruiser', 'Toyota', 'Land Cruiser', 'Black', 120.00, 210, 'REPAIR', 29),
                      ('Lexus LC500', 'Lexus', 'LC500', 'Orange', 180.00, 270, 'AVAILABLE', 30),

                      -- South Korea
                      ('Hyundai i30', 'Hyundai', 'i30', 'White', 40.00, 200, 'AVAILABLE', 33),
                      ('Hyundai Tucson', 'Hyundai', 'Tucson', 'Grey', 58.00, 200, 'AVAILABLE', 33),
                      ('Hyundai Santa Fe', 'Hyundai', 'Santa Fe', 'Black', 70.00, 200, 'AVAILABLE', 34),
                      ('Kia Sportage', 'Kia', 'Sportage', 'Blue', 55.00, 195, 'RENTED', 34),
                      ('Kia Sorento', 'Kia', 'Sorento', 'White', 72.00, 200, 'AVAILABLE', 35),
                      ('Genesis G70', 'Genesis', 'G70', 'Black', 90.00, 240, 'AVAILABLE', 35),
                      ('Genesis GV80', 'Genesis', 'GV80', 'Silver', 115.00, 240, 'AVAILABLE', 36),
                      ('Kia EV6', 'Kia', 'EV6', 'Green', 78.00, 260, 'AVAILABLE', 36),
                      ('Hyundai Ioniq 5', 'Hyundai', 'Ioniq 5', 'Grey', 82.00, 185, 'REPAIR', 33),
                      ('Genesis G80', 'Genesis', 'G80', 'Blue', 105.00, 250, 'AVAILABLE', 35),

                      -- UAE
                      ('Toyota Land Cruiser', 'Toyota', 'Land Cruiser', 'White', 130.00, 210, 'AVAILABLE', 37),
                      ('Nissan Patrol', 'Nissan', 'Patrol', 'Black', 125.00, 200, 'AVAILABLE', 37),
                      ('Range Rover Vogue', 'Land Rover', 'Vogue', 'White', 180.00, 250, 'AVAILABLE', 38),
                      ('Mercedes G-Class', 'Mercedes-Benz', 'G500', 'Black', 220.00, 210, 'RENTED', 38),
                      ('Lamborghini Urus', 'Lamborghini', 'Urus', 'Yellow', 350.00, 305, 'AVAILABLE', 39),
                      ('Ferrari SF90', 'Ferrari', 'SF90', 'Red', 600.00, 340, 'AVAILABLE', 39),
                      ('Porsche Cayenne', 'Porsche', 'Cayenne', 'Grey', 150.00, 265, 'AVAILABLE', 40),
                      ('Bentley Bentayga', 'Bentley', 'Bentayga', 'Blue', 260.00, 290, 'AVAILABLE', 40),
                      ('McLaren 720S', 'McLaren', '720S', 'Orange', 480.00, 341, 'REPAIR', 37),
                      ('Aston Martin DBX', 'Aston Martin', 'DBX', 'Green', 230.00, 291, 'AVAILABLE', 38),

                      -- Switzerland
                      ('Volvo XC60', 'Volvo', 'XC60', 'Silver', 72.00, 210, 'AVAILABLE', 41),
                      ('Volvo XC90', 'Volvo', 'XC90', 'Black', 95.00, 215, 'AVAILABLE', 41),
                      ('Mercedes GLC', 'Mercedes-Benz', 'GLC', 'White', 88.00, 235, 'AVAILABLE', 42),
                      ('Audi Q8', 'Audi', 'Q8', 'Grey', 120.00, 250, 'RENTED', 42),
                      ('BMW i4', 'BMW', 'i4', 'Blue', 85.00, 225, 'AVAILABLE', 43),
                      ('BMW iX', 'BMW', 'iX', 'Black', 115.00, 200, 'AVAILABLE', 43),
                      ('Porsche Taycan', 'Porsche', 'Taycan', 'White', 180.00, 260, 'AVAILABLE', 44),
                      ('Porsche Panamera', 'Porsche', 'Panamera', 'Grey', 175.00, 315, 'AVAILABLE', 44),
                      ('Volvo V90', 'Volvo', 'V90', 'Green', 80.00, 180, 'REPAIR', 41),
                      ('Mercedes EQE', 'Mercedes-Benz', 'EQE', 'Silver', 110.00, 210, 'AVAILABLE', 42),

                      -- Netherlands
                      ('Volkswagen ID.4', 'Volkswagen', 'ID.4', 'White', 62.00, 180, 'AVAILABLE', 45),
                      ('Volkswagen ID.7', 'Volkswagen', 'ID.7', 'Black', 75.00, 180, 'AVAILABLE', 45),
                      ('Tesla Model 3', 'Tesla', 'Model 3', 'Red', 68.00, 201, 'AVAILABLE', 46),
                      ('Tesla Model Y', 'Tesla', 'Model Y', 'White', 75.00, 217, 'RENTED', 46),
                      ('Volvo XC40', 'Volvo', 'XC40', 'Blue', 65.00, 180, 'AVAILABLE', 47),
                      ('Volvo EX30', 'Volvo', 'EX30', 'Green', 58.00, 180, 'AVAILABLE', 47),
                      ('BMW iX1', 'BMW', 'iX1', 'Grey', 80.00, 180, 'AVAILABLE', 48),
                      ('Audi Q4 e-tron', 'Audi', 'Q4 e-tron', 'Black', 82.00, 180, 'AVAILABLE', 48),
                      ('Polestar 2', 'Polestar', '2', 'White', 78.00, 205, 'REPAIR', 45),
                      ('Mercedes EQB', 'Mercedes-Benz', 'EQB', 'Silver', 85.00, 160, 'AVAILABLE', 46),

                      -- Australia
                      ('Toyota Hilux', 'Toyota', 'Hilux', 'White', 85.00, 175, 'AVAILABLE', 49),
                      ('Toyota Land Cruiser', 'Toyota', 'Land Cruiser', 'Black', 125.00, 210, 'AVAILABLE', 49),
                      ('Ford Ranger', 'Ford', 'Ranger', 'Blue', 88.00, 180, 'AVAILABLE', 50),
                      ('Ford Everest', 'Ford', 'Everest', 'Grey', 95.00, 180, 'RENTED', 50),
                      ('Mazda CX-60', 'Mazda', 'CX-60', 'Red', 72.00, 200, 'AVAILABLE', 51),
                      ('Mazda CX-5', 'Mazda', 'CX-5', 'White', 65.00, 195, 'AVAILABLE', 51),
                      ('Subaru Outback', 'Subaru', 'Outback', 'Green', 70.00, 190, 'AVAILABLE', 52),
                      ('Subaru Forester', 'Subaru', 'Forester', 'Silver', 62.00, 185, 'AVAILABLE', 52),
                      ('Nissan Navara', 'Nissan', 'Navara', 'Black', 80.00, 175, 'REPAIR', 49),
                      ('Toyota GR Supra', 'Toyota', 'GR Supra', 'Yellow', 130.00, 250, 'AVAILABLE', 50),

                      -- Brazil
                      ('Volkswagen Polo', 'Volkswagen', 'Polo', 'White', 38.00, 190, 'AVAILABLE', 53),
                      ('Volkswagen T-Cross', 'Volkswagen', 'T-Cross', 'Blue', 52.00, 190, 'AVAILABLE', 53),
                      ('Chevrolet Onix', 'Chevrolet', 'Onix', 'Red', 36.00, 185, 'AVAILABLE', 54),
                      ('Chevrolet Tracker', 'Chevrolet', 'Tracker', 'Grey', 55.00, 190, 'RENTED', 54),
                      ('Fiat Pulse', 'Fiat', 'Pulse', 'Black', 45.00, 185, 'AVAILABLE', 55),
                      ('Fiat Toro', 'Fiat', 'Toro', 'White', 62.00, 190, 'AVAILABLE', 55),
                      ('Jeep Compass', 'Jeep', 'Compass', 'Silver', 65.00, 200, 'AVAILABLE', 56),
                      ('Jeep Renegade', 'Jeep', 'Renegade', 'Green', 58.00, 190, 'AVAILABLE', 56),
                      ('Toyota Corolla', 'Toyota', 'Corolla', 'Black', 55.00, 200, 'REPAIR', 53),
                      ('Honda HR-V', 'Honda', 'HR-V', 'Blue', 60.00, 190, 'AVAILABLE', 54),

                      -- Mexico
                      ('Nissan Versa', 'Nissan', 'Versa', 'White', 40.00, 190, 'AVAILABLE', 57),
                      ('Nissan Kicks', 'Nissan', 'Kicks', 'Red', 50.00, 180, 'AVAILABLE', 57),
                      ('Volkswagen Jetta', 'Volkswagen', 'Jetta', 'Black', 55.00, 210, 'AVAILABLE', 58),
                      ('Volkswagen Taos', 'Volkswagen', 'Taos', 'Grey', 60.00, 190, 'RENTED', 58),
                      ('Toyota Corolla', 'Toyota', 'Corolla', 'Silver', 52.00, 200, 'AVAILABLE', 59),
                      ('Toyota RAV4', 'Toyota', 'RAV4', 'White', 70.00, 190, 'AVAILABLE', 59),
                      ('Mazda 3', 'Mazda', 'Mazda 3', 'Blue', 55.00, 210, 'AVAILABLE', 60),
                      ('Mazda CX-30', 'Mazda', 'CX-30', 'Red', 62.00, 195, 'AVAILABLE', 60),
                      ('Honda CR-V', 'Honda', 'CR-V', 'Black', 72.00, 200, 'REPAIR', 57),
                      ('Ford Bronco Sport', 'Ford', 'Bronco Sport', 'Green', 75.00, 200, 'AVAILABLE', 58),

                      -- Egypt
                      ('Toyota Corolla', 'Toyota', 'Corolla', 'White', 35.00, 190, 'AVAILABLE', 61),
                      ('Hyundai Elantra', 'Hyundai', 'Elantra', 'Black', 38.00, 195, 'AVAILABLE', 61),
                      ('Kia Cerato', 'Kia', 'Cerato', 'Grey', 40.00, 190, 'AVAILABLE', 62),
                      ('Kia Sportage', 'Kia', 'Sportage', 'White', 55.00, 195, 'RENTED', 62),
                      ('Nissan Sunny', 'Nissan', 'Sunny', 'Silver', 32.00, 180, 'AVAILABLE', 63),
                      ('Nissan Qashqai', 'Nissan', 'Qashqai', 'Blue', 50.00, 200, 'AVAILABLE', 63),
                      ('Renault Logan', 'Renault', 'Logan', 'Red', 30.00, 175, 'AVAILABLE', 64),
                      ('Renault Duster', 'Renault', 'Duster', 'Black', 48.00, 180, 'AVAILABLE', 64),
                      ('Chevrolet Optra', 'Chevrolet', 'Optra', 'Grey', 34.00, 175, 'REPAIR', 61),
                      ('Hyundai Tucson', 'Hyundai', 'Tucson', 'Green', 58.00, 200, 'AVAILABLE', 62),

                      -- Turkey
                      ('Renault Clio', 'Renault', 'Clio', 'White', 38.00, 190, 'AVAILABLE', 65),
                      ('Renault Megane', 'Renault', 'Megane', 'Blue', 48.00, 210, 'AVAILABLE', 65),
                      ('Fiat Egea', 'Fiat', 'Egea', 'Grey', 36.00, 190, 'AVAILABLE', 66),
                      ('Fiat Tipo', 'Fiat', 'Tipo', 'Black', 40.00, 190, 'RENTED', 66),
                      ('Toyota Corolla', 'Toyota', 'Corolla', 'White', 50.00, 200, 'AVAILABLE', 67),
                      ('Toyota C-HR', 'Toyota', 'C-HR', 'Red', 60.00, 190, 'AVAILABLE', 67),
                      ('Ford Puma', 'Ford', 'Puma', 'Blue', 55.00, 195, 'AVAILABLE', 68),
                      ('Ford Kuga', 'Ford', 'Kuga', 'Grey', 65.00, 200, 'AVAILABLE', 68),
                      ('Peugeot 3008', 'Peugeot', '3008', 'Black', 62.00, 210, 'REPAIR', 65),
                      ('Volkswagen T-Roc', 'Volkswagen', 'T-Roc', 'Green', 58.00, 215, 'AVAILABLE', 66),

                      -- Singapore
                      ('Toyota Corolla Altis', 'Toyota', 'Corolla Altis', 'White', 75.00, 190, 'AVAILABLE', 69),
                      ('Toyota Camry', 'Toyota', 'Camry', 'Black', 90.00, 200, 'AVAILABLE', 69),
                      ('Honda Civic', 'Honda', 'Civic', 'Blue', 82.00, 200, 'AVAILABLE', 70),
                      ('Honda HR-V', 'Honda', 'HR-V', 'Grey', 78.00, 185, 'RENTED', 70),
                      ('Mazda 3', 'Mazda', 'Mazda 3', 'Red', 80.00, 210, 'AVAILABLE', 71),
                      ('Mazda CX-5', 'Mazda', 'CX-5', 'White', 92.00, 195, 'AVAILABLE', 71),
                      ('BMW 3 Series', 'BMW', '320i', 'Black', 120.00, 235, 'AVAILABLE', 72),
                      ('Mercedes A-Class', 'Mercedes-Benz', 'A200', 'Silver', 110.00, 225, 'AVAILABLE', 72),
                      ('Tesla Model 3', 'Tesla', 'Model 3', 'Blue', 100.00, 201, 'REPAIR', 69),
                      ('Volvo XC40', 'Volvo', 'XC40', 'Green', 105.00, 180, 'AVAILABLE', 70),

                      -- Austria
                      ('Skoda Octavia', 'Skoda', 'Octavia', 'White', 48.00, 220, 'AVAILABLE', 73),
                      ('Skoda Superb', 'Skoda', 'Superb', 'Black', 60.00, 230, 'AVAILABLE', 73),
                      ('Volkswagen Golf', 'Volkswagen', 'Golf 8', 'Blue', 50.00, 225, 'AVAILABLE', 74),
                      ('Volkswagen Tiguan', 'Volkswagen', 'Tiguan', 'Grey', 65.00, 215, 'RENTED', 74),
                      ('Audi A3', 'Audi', 'A3', 'Red', 62.00, 230, 'AVAILABLE', 75),
                      ('Audi Q5', 'Audi', 'Q5', 'White', 85.00, 240, 'AVAILABLE', 75),
                      ('BMW 3 Series', 'BMW', '320d', 'Black', 72.00, 235, 'AVAILABLE', 76),
                      ('BMW X3', 'BMW', 'X3', 'Silver', 90.00, 240, 'AVAILABLE', 76),
                      ('Mercedes GLC', 'Mercedes-Benz', 'GLC', 'Green', 95.00, 235, 'REPAIR', 73),
                      ('Porsche Cayenne', 'Porsche', 'Cayenne', 'Blue', 145.00, 265, 'AVAILABLE', 74),

                      -- Belgium
                      ('BMW 1 Series', 'BMW', '120i', 'White', 55.00, 230, 'AVAILABLE', 77),
                      ('BMW X1', 'BMW', 'X1', 'Black', 70.00, 230, 'AVAILABLE', 77),
                      ('Audi A3', 'Audi', 'A3', 'Grey', 58.00, 230, 'AVAILABLE', 78),
                      ('Audi Q3', 'Audi', 'Q3', 'Blue', 75.00, 235, 'RENTED', 78),
                      ('Volvo XC40', 'Volvo', 'XC40', 'Red', 65.00, 180, 'AVAILABLE', 79),
                      ('Volvo XC60', 'Volvo', 'XC60', 'White', 80.00, 210, 'AVAILABLE', 79),
                      ('Mercedes A-Class', 'Mercedes-Benz', 'A200', 'Silver', 62.00, 225, 'AVAILABLE', 80),
                      ('Mercedes GLA', 'Mercedes-Benz', 'GLA', 'Black', 78.00, 220, 'AVAILABLE', 80),
                      ('Volkswagen Golf', 'Volkswagen', 'Golf 8', 'Green', 48.00, 225, 'REPAIR', 77),
                      ('Peugeot 308', 'Peugeot', '308', 'Blue', 45.00, 210, 'AVAILABLE', 78)

              ) AS data(
                        vehicle_name,
                        brand,
                        model,
                        color,
                        price,
                        max_speed,
                        vehicle_status,
                        id_supplier
             )
     ) AS numbered;