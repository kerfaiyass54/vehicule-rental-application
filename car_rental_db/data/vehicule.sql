INSERT INTO vehicules (
    vehicule_name,
    vehicule_brand,
    vehicule_color,
    maximum_speed,
    price,
    transmission,
    vehicule_status,
    id_supplier
)
SELECT
    vehicule_names[((series - 1) % 20) + 1] ||
    ' ' ||
    series AS vehicule_name,

    vehicule_brands[((series - 1) % 20) + 1] AS vehicule_brand,

    vehicule_colors[((series - 1) % 10) + 1] AS vehicule_color,

    160 + ((series * 7) % 141) AS maximum_speed,

    ROUND((25000 + ((series * 1375) % 85000))::numeric, 2) AS price,

    CASE
    WHEN series % 3 = 0 THEN 'MANUAL'
    ELSE 'AUTOMATIC'
END AS transmission,

    CASE
        WHEN series % 10 = 0 THEN 'REPAIR'
        WHEN series % 4 = 0 THEN 'RENTED'
        ELSE 'AVAILABLE'
END AS vehicule_status,

    ((series - 1) % 40) + 1 AS id_supplier

FROM generate_series(1, 200) AS series,

LATERAL (
    SELECT ARRAY[
        'Corolla',
        'Camry',
        'Civic',
        'Accord',
        'Golf',
        'Passat',
        'A4',
        'A6',
        '3 Series',
        '5 Series',
        'C-Class',
        'E-Class',
        'Clio',
        'Megane',
        'Peugeot 308',
        'Peugeot 508',
        'Leon',
        'Ibiza',
        'Model 3',
        'Model Y'
    ] AS vehicule_names
) names,

LATERAL (
    SELECT ARRAY[
        'Toyota',
        'Toyota',
        'Honda',
        'Honda',
        'Volkswagen',
        'Volkswagen',
        'Audi',
        'Audi',
        'BMW',
        'BMW',
        'Mercedes-Benz',
        'Mercedes-Benz',
        'Renault',
        'Renault',
        'Peugeot',
        'Peugeot',
        'Seat',
        'Seat',
        'Tesla',
        'Tesla'
    ] AS vehicule_brands
) brands,

LATERAL (
    SELECT ARRAY[
        'Black',
        'White',
        'Silver',
        'Grey',
        'Blue',
        'Red',
        'Green',
        'Brown',
        'Orange',
        'Yellow'
    ] AS vehicule_colors
) colors;