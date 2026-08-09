INSERT INTO demands (
    date_ask,
    status_confirm,
    type,
    id_supplier,
    id_ticket,
    id_vehicule,
    estimated_time
)
SELECT
            CURRENT_TIMESTAMP
        - ((series * 2) || ' days')::interval AS date_ask,

    CASE
        WHEN series % 5 = 0 THEN 'APPROVED'
        WHEN series % 7 = 0 THEN 'REFUSED'
        ELSE 'PENDING'
        END AS status_confirm,

            CASE
                WHEN series % 3 = 0 THEN 'REPAIR'
                WHEN series % 3 = 1 THEN 'MAINTENANCE'
                ELSE 'VEHICLE_REQUEST'
                END AS type,

            ((series - 1) % 40) + 1 AS id_supplier,

    ((series - 1) % 500) + 1 AS id_ticket,

    ((series - 1) % 200) + 1 AS id_vehicule,

    1 + (series % 30) AS estimated_time

FROM generate_series(1, 1000) AS series;