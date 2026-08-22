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
            CURRENT_TIMESTAMP
        - ((series * 3) || ' days')::interval
        AS date_insert,

    CASE
        WHEN series % 10 = 0 THEN 'REJECTED'
        WHEN series % 7 = 0 THEN 'COMPLETED'
        WHEN series % 4 = 0 THEN 'PENDING'
        ELSE 'ACCEPTED'
        END AS status,

            ROUND(
                    (50 + ((series * 37) % 950))::numeric,
        2
    )::real AS tariff,

    CASE
        WHEN series % 2 = 0 THEN 'MODIFICATION'
        ELSE 'REPARATION'
        END AS type,

            -- 100 international clients
            ((series - 1) % 100) + 1 AS id_client,

            -- 80 international repair centers
    ((series - 1) % 80) + 1 AS id_repair,

            -- 200 international vehicles
    ((series - 1) % 200) + 1 AS id_vehicle,

    CASE
    WHEN series % 2 = 0 THEN
    'Vehicle modification requested by the client'
    WHEN series % 3 = 0 THEN
    'Engine inspection and mechanical repair required'
    WHEN series % 5 = 0 THEN
    'Brake system inspection requested'
    WHEN series % 7 = 0 THEN
    'Electrical system diagnosis required'
    ELSE
    'Vehicle requires technical repair'
END AS description

FROM generate_series(1, 500) AS series;