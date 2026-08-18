INSERT INTO repair_infos (
    date_start,
    repair_status,
    id_repair,
    id_vehicle
)
SELECT
            CURRENT_TIMESTAMP - ((series * 4) || ' days')::interval,

    CASE
        WHEN series % 10 = 0 THEN 'CANCELLED'
        WHEN series % 4 = 0 THEN 'FINISHED'
        WHEN series % 3 = 0 THEN 'PENDING_FINISH'
        ELSE 'PENDING_START'
        END,

            ((series - 1) % 50) + 1,
    ((series - 1) % 200) + 1

FROM generate_series(1, 500) AS series;