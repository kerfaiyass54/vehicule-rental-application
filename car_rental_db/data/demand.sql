INSERT INTO demands (
    date_ask,
    status_confirm,
    type,
    id_supplier,
    id_ticket,
    id_vehicle,
    estimated_time
)
SELECT
            CURRENT_TIMESTAMP - ((series * 2) || ' days')::interval,

    CASE
        WHEN series % 5 = 0 THEN 'APPROVED'
        WHEN series % 7 = 0 THEN 'REFUSED'
        ELSE 'PENDING'
        END,

            CASE
                WHEN series % 3 = 0 THEN 'CONFIRMATION'
                WHEN series % 3 = 1 THEN 'CANCELLATION'
                ELSE 'UPDATE'
                END,

            ((series - 1) % 40) + 1,
    ((series - 1) % 500) + 1,
    ((series - 1) % 200) + 1,

    1 + (series % 30)

FROM generate_series(1, 1000) AS series;