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

    1 + (series % 30) AS period_buy,

            CASE
                WHEN series % 5 = 0 THEN 'FINISHED'
                ELSE 'BEING_USED'
                END AS buy_status,

            ((series - 1) % 100) + 1 AS id_client,

    ((series - 1) % 200) + 1 AS id_vehicule,

    CASE
    WHEN series % 7 = 0 THEN TRUE
    ELSE FALSE
END AS renew

FROM generate_series(1, 500) AS series;