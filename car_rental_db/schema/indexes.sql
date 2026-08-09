-- ============================================
-- VEHICLE RENTAL DATABASE INDEXES
-- ============================================


-- ============================================
-- LOCATIONS
-- ============================================

CREATE INDEX IF NOT EXISTS idx_locations_admin
    ON locations(id_admin);


-- ============================================
-- SUPPLIERS
-- ============================================

CREATE INDEX IF NOT EXISTS idx_suppliers_admin
    ON suppliers(id_admin);


-- ============================================
-- ADDRESSES
-- ============================================

CREATE INDEX IF NOT EXISTS idx_addresses_supplier
    ON addresses(id_supplier);

CREATE INDEX IF NOT EXISTS idx_addresses_location
    ON addresses(id_location);

CREATE INDEX IF NOT EXISTS idx_addresses_status
    ON addresses(address_status);


-- ============================================
-- CLIENTS
-- ============================================

CREATE INDEX IF NOT EXISTS idx_clients_admin
    ON clients(id_admin);

CREATE INDEX IF NOT EXISTS idx_clients_location
    ON clients(id_location);


-- ============================================
-- REPAIRERS
-- ============================================

CREATE INDEX IF NOT EXISTS idx_repairers_admin
    ON repairers(id_admin);

CREATE INDEX IF NOT EXISTS idx_repairers_location
    ON repairers(id_location);


-- ============================================
-- VEHICLES
-- ============================================

CREATE INDEX IF NOT EXISTS idx_vehicles_supplier
    ON vehicles(id_supplier);

CREATE INDEX IF NOT EXISTS idx_vehicles_brand
    ON vehicles(brand);

CREATE INDEX IF NOT EXISTS idx_vehicles_status
    ON vehicles(vehicle_status);

CREATE INDEX IF NOT EXISTS idx_vehicles_transmission
    ON vehicles(transmission);

CREATE INDEX IF NOT EXISTS idx_vehicles_price
    ON vehicles(price);


-- ============================================
-- BUYINGS
-- ============================================

CREATE INDEX IF NOT EXISTS idx_buyings_client
    ON buyings(id_client);

CREATE INDEX IF NOT EXISTS idx_buyings_vehicle
    ON buyings(id_vehicle);

CREATE INDEX IF NOT EXISTS idx_buyings_status
    ON buyings(buying_status);

CREATE INDEX IF NOT EXISTS idx_buyings_date
    ON buyings(buying_date);


-- ============================================
-- TICKETS
-- ============================================

CREATE INDEX IF NOT EXISTS idx_tickets_client
    ON tickets(id_client);

CREATE INDEX IF NOT EXISTS idx_tickets_repairer
    ON tickets(id_repairer);

CREATE INDEX IF NOT EXISTS idx_tickets_vehicle
    ON tickets(id_vehicle);

CREATE INDEX IF NOT EXISTS idx_tickets_status
    ON tickets(ticket_status);

CREATE INDEX IF NOT EXISTS idx_tickets_type
    ON tickets(ticket_type);

CREATE INDEX IF NOT EXISTS idx_tickets_created_at
    ON tickets(created_at);


-- ============================================
-- DEMANDS
-- ============================================

CREATE INDEX IF NOT EXISTS idx_demands_supplier
    ON demands(id_supplier);

CREATE INDEX IF NOT EXISTS idx_demands_ticket
    ON demands(id_ticket);

CREATE INDEX IF NOT EXISTS idx_demands_vehicle
    ON demands(id_vehicle);

CREATE INDEX IF NOT EXISTS idx_demands_status
    ON demands(confirmation_status);

CREATE INDEX IF NOT EXISTS idx_demands_created_at
    ON demands(created_at);


-- ============================================
-- REPAIR INFOS
-- ============================================

CREATE INDEX IF NOT EXISTS idx_repair_infos_repairer
    ON repair_infos(id_repairer);

CREATE INDEX IF NOT EXISTS idx_repair_infos_vehicle
    ON repair_infos(id_vehicle);

CREATE INDEX IF NOT EXISTS idx_repair_infos_status
    ON repair_infos(repair_status);

CREATE INDEX IF NOT EXISTS idx_repair_infos_start_date
    ON repair_infos(start_date);


-- ============================================
-- SUBSCRIPTIONS
-- ============================================

CREATE INDEX IF NOT EXISTS idx_subscriptions_client
    ON subscriptions(id_client);

CREATE INDEX IF NOT EXISTS idx_subscriptions_supplier
    ON subscriptions(id_supplier);

CREATE INDEX IF NOT EXISTS idx_subscriptions_type
    ON subscriptions(subscription_type);

CREATE INDEX IF NOT EXISTS idx_subscriptions_start_date
    ON subscriptions(start_date);