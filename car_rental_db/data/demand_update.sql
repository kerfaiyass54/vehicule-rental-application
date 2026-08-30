INSERT INTO demands (
    date_ask,
    type,
    status_confirm,
    estimated_time,
    id_supplier,
    id_ticket,
    id_vehicle,
    id_repair,
    id_client
)
SELECT

            CURRENT_TIMESTAMP
        - (
                (ROW_NUMBER() OVER (ORDER BY t.id_ticket) * 2)
                    || ' days'
                )::interval
        AS date_ask,


    -- =========================================================
    -- DEMAND TYPE
    -- =========================================================

    CASE
        WHEN ROW_NUMBER() OVER (ORDER BY t.id_ticket) % 2 = 0
            THEN 'CONFIRMATION'
        ELSE 'UPDATE'
        END
        AS type,


            -- =========================================================
            -- CONFIRM STATUS
            -- =========================================================

            CASE

                WHEN ROW_NUMBER() OVER (ORDER BY t.id_ticket) % 10 = 0
            THEN 'REFUSED'

                WHEN ROW_NUMBER() OVER (ORDER BY t.id_ticket) % 3 = 0
            THEN 'PENDING'

                ELSE 'APPROVED'

                END
                AS status_confirm,


            -- =========================================================
            -- ESTIMATED TIME
            -- =========================================================

            CASE

                WHEN ROW_NUMBER() OVER (ORDER BY t.id_ticket) % 4 = 0
            THEN 2

                WHEN ROW_NUMBER() OVER (ORDER BY t.id_ticket) % 4 = 1
            THEN 5

                WHEN ROW_NUMBER() OVER (ORDER BY t.id_ticket) % 4 = 2
            THEN 8

                ELSE 12

                END
                AS estimated_time,


            -- =========================================================
            -- SUPPLIER
            -- =========================================================

            ((ROW_NUMBER() OVER (ORDER BY t.id_ticket) - 1) % 80) + 1
    AS id_supplier,


    -- =========================================================
    -- RELATIONS FROM TICKET
    -- =========================================================

    t.id_ticket,

    t.id_vehicle,

    t.id_repair,

    t.id_client


FROM (

    /*
     * One ticket per:
     *
     *     client + repair
     *
     * This guarantees:
     *
     *     UNIQUE(id_client, id_repair)
     */

    SELECT DISTINCT ON (
    t.id_client,
    t.id_repair
    )

    t.id_ticket,
    t.id_client,
    t.id_repair,
    t.id_vehicle

    FROM tickets t

    WHERE
    t.id_client IS NOT NULL
    AND t.id_repair IS NOT NULL
    AND t.id_vehicle IS NOT NULL

    ORDER BY
    t.id_client,
    t.id_repair,
    t.id_ticket

    ) t

ORDER BY t.id_ticket

    LIMIT 500;