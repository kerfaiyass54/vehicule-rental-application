INSERT INTO demands (
    date_ask,
    type,
    status_confirm,
    estimated_time,
    id_supplier,
    id_ticket,
    id_vehicle
)
SELECT
            CURRENT_TIMESTAMP
        - ((series * 2) || ' days')::interval
        AS date_ask,

    CASE
        WHEN series % 4 = 0 THEN 'URGENT_REPAIR'
        WHEN series % 4 = 1 THEN 'VEHICLE_REPAIR'
        WHEN series % 4 = 2 THEN 'MAINTENANCE'
        ELSE 'VEHICLE_MODIFICATION'
        END AS type,

            CASE
                WHEN series % 8 = 0 THEN 'REFUSED'
                WHEN series % 3 = 0 THEN 'PENDING'
                ELSE 'APPROVED'
                END AS status_confirm,

            CASE
                WHEN series % 4 = 0 THEN 2
                WHEN series % 4 = 1 THEN 5
                WHEN series % 4 = 2 THEN 8
                ELSE 12
                END AS estimated_time,

            -- 80 international suppliers
            ((series - 1) % 80) + 1 AS id_supplier,

            -- 500 tickets
    ((series - 1) % 500) + 1 AS id_ticket,

            -- 200 vehicles
    ((series - 1) % 200) + 1 AS id_vehicle

FROM generate_series(1, 500) AS series;