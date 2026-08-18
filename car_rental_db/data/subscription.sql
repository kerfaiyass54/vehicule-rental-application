INSERT INTO subscriptions (
    date_start,
    price,
    reduction,
    subscription_type,
    id_client,
    id_supplier
)
SELECT
            CURRENT_TIMESTAMP - ((series * 3) || ' days')::interval,

    CASE
        WHEN series % 4 = 0 THEN 99
        WHEN series % 4 = 1 THEN 199
        WHEN series % 4 = 2 THEN 39
        ELSE 399
        END,

            CASE
                WHEN series % 4 = 0 THEN 5
                WHEN series % 4 = 1 THEN 15
                WHEN series % 4 = 2 THEN 10
                ELSE 25
                END,

            CASE
                WHEN series % 4 = 0 THEN 'BASIC'
                WHEN series % 4 = 1 THEN 'PREMIUM'
                WHEN series % 4 = 2 THEN 'MONTHLY'
                ELSE 'ANNUAL'
                END,

            ((series - 1) % 100) + 1,
    ((series - 1) % 40) + 1

FROM generate_series(1, 1000) AS series;