INSERT INTO tickets (
    date_insert,
    status,
    tariff,
    type,
    id_client,
    id_repair,
    id_vehicle,
    description
)
SELECT
            CURRENT_TIMESTAMP - ((series * 3) || ' days')::interval,

    CASE
        WHEN series % 10 = 0 THEN 'REJECTED'
        WHEN series % 7 = 0 THEN 'COMPLETED'
        WHEN series % 4 = 0 THEN 'PENDING'
        ELSE 'ACCEPTED'
        END,

            ROUND((50 + ((series * 37) % 950))::numeric, 2),

            CASE
                WHEN series % 2 = 0 THEN 'MODIFICATION'
                ELSE 'REPARATION'
                END,

            ((series - 1) % 100) + 1,
    ((series - 1) % 50) + 1,
    ((series - 1) % 200) + 1,

    CASE
    WHEN series % 2 = 0
    THEN 'Vehicle modification requested by the client'
    ELSE
    'Vehicle requires technical repair'
END

FROM generate_series(1, 500) AS series;