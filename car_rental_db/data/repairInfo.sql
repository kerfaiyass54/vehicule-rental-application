INSERT INTO repair_infos (
    date_start,
    repair_status,
    id_repair,
    id_vehicule
)
SELECT
            CURRENT_TIMESTAMP
        - ((series * 4) || ' days')::interval AS date_start,

    CASE
        WHEN series % 10 = 0 THEN 'CANCELLED'
        WHEN series % 4 = 0 THEN 'FINISHED'
        WHEN series % 3 = 0 THEN 'PENDING_FINISH'
        ELSE 'PENDING_START'
        END AS repair_status,

            ((series - 1) % 50) + 1 AS id_repair,

    ((series - 1) % 200) + 1 AS id_vehicule

FROM generate_series(1, 500) AS series;