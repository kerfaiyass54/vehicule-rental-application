INSERT INTO buyings (
    date_buy,
    period_buy,
    buy_status,
    id_client,
    id_vehicule,
    renew
)
SELECT
            CURRENT_TIMESTAMP
        - ((series * 2) || ' days')::interval
        AS date_buy,

    -- Rental/buying period: 1 to 30 days
    1 + (series % 30) AS period_buy,

            -- Most transactions are currently being used
            CASE
                WHEN series % 5 = 0 THEN 'FINISHED'
                ELSE 'BEING_USED'
                END AS buy_status,

            -- Distribute across all 100 international clients
            ((series - 1) % 100) + 1 AS id_client,

            -- Distribute across all 200 international vehicles
    ((series - 1) % 200) + 1 AS id_vehicule,

            -- Every 7th transaction is renewable
    CASE
    WHEN series % 7 = 0 THEN TRUE
    ELSE FALSE
END AS renew

FROM generate_series(1, 500) AS series;