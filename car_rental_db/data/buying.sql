-- ============================================
-- INTERNATIONAL BUYINGS / RENTALS
-- ============================================

INSERT INTO buyings (
    date_buy,
    period_buy,
    buy_status,
    renew,
    id_client,
    id_vehicle,
    id_supplier
)
SELECT
            CURRENT_TIMESTAMP - ((series * 2) || ' days')::INTERVAL
        AS date_buy,

    -- Rental period: 1 to 30 days
    1 + (series % 30)
        AS period_buy,

            -- Rental status
            CASE
                WHEN series % 5 = 0
            THEN 'FINISHED'
                ELSE 'BEING_USED'
                END
                AS buy_status,

            -- Renewal every 7th rental
            CASE
                WHEN series % 7 = 0
            THEN TRUE
                ELSE FALSE
                END
                AS renew,

            -- 100 international clients
            ((series - 1) % 100) + 1
    AS id_client,

            -- 200 international vehicles
    ((series - 1) % 200) + 1
    AS id_vehicle,

            -- Supplier is taken from the selected vehicle
    v.id_supplier

FROM generate_series(1, 500) AS series

    JOIN vehicles v
ON v.id_vehicle = ((series - 1) % 200) + 1;