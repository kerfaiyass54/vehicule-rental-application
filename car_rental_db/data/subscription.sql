INSERT INTO subscriptions (
    date_start,
    price,
    reduction,
    subscription_type,
    id_client,
    id_supplier
)
SELECT
            CURRENT_TIMESTAMP
        - ((series * 3) || ' days')::interval AS date_start,

    CASE
        WHEN series % 4 = 0 THEN 49.99
        WHEN series % 4 = 1 THEN 99.99
        WHEN series % 4 = 2 THEN 199.99
        ELSE 999.99
        END AS price,

            CASE
                WHEN series % 5 = 0 THEN 20
                WHEN series % 3 = 0 THEN 10
                ELSE 0
                END AS reduction,

            CASE
                WHEN series % 4 = 0 THEN 'BASIC'
                WHEN series % 4 = 1 THEN 'PREMIUM'
                WHEN series % 4 = 2 THEN 'MONTHLY'
                ELSE 'ANNUAL'
                END AS subscription_type,

            ((series - 1) % 100) + 1 AS id_client,

    ((series - 1) % 40) + 1 AS id_supplier

FROM generate_series(1, 1000) AS series;