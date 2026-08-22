INSERT INTO subscriptions (
    date_start,
    price,
    reduction,
    subscription_type,
    id_client,
    id_supplier
)
SELECT

    -- Different starting dates
    CURRENT_TIMESTAMP
        - ((series * 3) || ' days')::interval
        AS date_start,

    -- Subscription price
    CASE
        WHEN series % 4 = 0 THEN 99.00
        WHEN series % 4 = 1 THEN 199.00
        WHEN series % 4 = 2 THEN 39.00
        ELSE 399.00
        END AS price,

    -- International promotional reductions
    CASE
        WHEN series % 4 = 0 THEN 5
        WHEN series % 4 = 1 THEN 15
        WHEN series % 4 = 2 THEN 10
        ELSE 25
        END AS reduction,

    -- Subscription type
    CASE
        WHEN series % 4 = 0 THEN 'BASIC'
        WHEN series % 4 = 1 THEN 'PREMIUM'
        WHEN series % 4 = 2 THEN 'MONTHLY'
        ELSE 'ANNUAL'
        END AS subscription_type,

    -- 100 international clients
    ((series - 1) % 100) + 1 AS id_client,

    -- 80 international suppliers
    ((series - 1) % 80) + 1 AS id_supplier

FROM generate_series(1, 1000) AS series;