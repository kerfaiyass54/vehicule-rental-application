-- =========================================================
-- INDEXES FOR SUPPLIER MODULE PERFORMANCE OPTIMIZATION
-- PostgreSQL 17
-- =========================================================

-- =========================================================
-- 1. SUPPLIER (ENTRY POINT)
-- =========================================================

-- Fast lookup by email (used in ALL APIs)
CREATE UNIQUE INDEX IF NOT EXISTS idx_supplier_email
    ON public.supplier (email_supp);


-- =========================================================
-- 2. CATEGORY TABLE
-- =========================================================

-- Join supplier -> category
CREATE INDEX IF NOT EXISTS idx_category_supplier
    ON public.category (id_supp_cat);

-- Optional composite for filtering/counting by category type
CREATE INDEX IF NOT EXISTS idx_category_supplier_name
    ON public.category (id_supp_cat, name_category);


-- =========================================================
-- 3. VEHICULE TABLE
-- =========================================================

-- Join supplier -> vehicule
CREATE INDEX IF NOT EXISTS idx_vehicule_supplier
    ON public.vehicule (id_supp);

-- Join category -> vehicule
CREATE INDEX IF NOT EXISTS idx_vehicule_category
    ON public.vehicule (id_category);

-- Composite index for filtering and aggregation
CREATE INDEX IF NOT EXISTS idx_vehicule_supplier_status
    ON public.vehicule (id_supp, vehicule_status);

-- Covering index for fast COUNT(*)
CREATE INDEX IF NOT EXISTS idx_vehicule_supplier_covering
    ON public.vehicule (id_supp)
    INCLUDE (idvehicule);

-- Partial index for frequently accessed AVAILABLE vehicles
CREATE INDEX IF NOT EXISTS idx_vehicule_available
    ON public.vehicule (id_supp)
    WHERE vehicule_status = 'AVAILABLE';


-- =========================================================
-- 4. ADDRESS TABLE
-- =========================================================

-- Join supplier -> address
CREATE INDEX IF NOT EXISTS idx_address_supplier
    ON public.adress (idsupp);

-- Join address -> location
CREATE INDEX IF NOT EXISTS idx_address_location
    ON public.adress (idloc);

-- Composite index for supplier + location joins
CREATE INDEX IF NOT EXISTS idx_address_supplier_location
    ON public.adress (idsupp, idloc);

-- Pagination optimization (critical for list APIs)
CREATE INDEX IF NOT EXISTS idx_address_supplier_id
    ON public.adress (idsupp, idadress);

-- Reverse join optimization (location -> supplier queries)
CREATE INDEX IF NOT EXISTS idx_address_loc_supplier
    ON public.adress (idloc, idsupp);


-- =========================================================
-- 5. LOCATION TABLE
-- =========================================================

-- Primary lookup optimization
CREATE INDEX IF NOT EXISTS idx_location_id
    ON public.location (idloc);

-- Country-based queries (countries API)
CREATE INDEX IF NOT EXISTS idx_location_country_name
    ON public.location (country, name);



-- =========================================================
-- 6. ANALYZE (UPDATE QUERY PLANNER STATS)
-- =========================================================

ANALYZE public.supplier;
ANALYZE public.category;
ANALYZE public.vehicule;
ANALYZE public.adress;
ANALYZE public.location;


-- =========================================================
-- END OF FILE
-- =========================================================