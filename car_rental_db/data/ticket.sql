INSERT INTO tickets (
    date_insert,
    status,
    tarif,
    type,
    id_client,
    id_repair,
    id_vehicule,
    description
)
SELECT
            CURRENT_TIMESTAMP
        - ((series * 3) || ' days')::interval AS date_insert,

    CASE
        WHEN series % 4 = 0 THEN 'REJECTED'
        ELSE 'ACCEPTED'
        END AS status,

            ROUND(
                    (50 + ((series * 37) % 950))::numeric,
        2
    ) AS tarif,

            CASE
                WHEN series % 3 = 0 THEN 'MODIFICATION'
                WHEN series % 3 = 1 THEN 'REPAIR'
                ELSE 'IMPROVEMENT'
                END AS type,

            ((series - 1) % 100) + 1 AS id_client,

    ((series - 1) % 50) + 1 AS id_repair,

    ((series - 1) % 200) + 1 AS id_vehicule,

    CASE
    WHEN series % 3 = 0
    THEN 'Vehicle modification requested by the client'
    WHEN series % 3 = 1
    THEN 'Vehicle requires technical repair'
    ELSE
    'Vehicle improvement requested by the client'
END AS description

FROM generate_series(1, 500) AS series;