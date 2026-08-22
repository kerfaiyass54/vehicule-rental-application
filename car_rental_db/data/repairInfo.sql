INSERT INTO repair_infos (
    date_start,
    repair_status,
    id_repair,
    id_vehicle
)
SELECT
    CASE
        WHEN series % 6 = 0 THEN NULL
        ELSE
                    CURRENT_TIMESTAMP
                - ((series * 2) || ' days')::interval
END AS date_start,

    CASE
        WHEN series % 10 = 0 THEN 'CANCELLED'
        WHEN series % 6 = 0 THEN 'PENDING_START'
        WHEN series % 3 = 0 THEN 'PENDING_FINISH'
        ELSE 'FINISHED'
END AS repair_status,

    -- 80 international repair centers
    ((series - 1) % 80) + 1 AS id_repair,

    -- 200 international vehicles
    ((series - 1) % 200) + 1 AS id_vehicle

FROM generate_series(1, 400) AS series;