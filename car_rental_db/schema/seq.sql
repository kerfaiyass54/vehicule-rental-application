-- ============================================
-- RESET IDENTITY SEQUENCES
-- ============================================

SELECT setval(
               pg_get_serial_sequence('admins', 'id_admin'),
               COALESCE(MAX(id_admin), 1),
               COUNT(*) > 0
       )
FROM admins;


SELECT setval(
               pg_get_serial_sequence('locations', 'id_location'),
               COALESCE(MAX(id_location), 1),
               COUNT(*) > 0
       )
FROM locations;


SELECT setval(
               pg_get_serial_sequence('suppliers', 'id_supplier'),
               COALESCE(MAX(id_supplier), 1),
               COUNT(*) > 0
       )
FROM suppliers;


SELECT setval(
               pg_get_serial_sequence('addresses', 'id_address'),
               COALESCE(MAX(id_address), 1),
               COUNT(*) > 0
       )
FROM addresses;


SELECT setval(
               pg_get_serial_sequence('clients', 'id_client'),
               COALESCE(MAX(id_client), 1),
               COUNT(*) > 0
       )
FROM clients;


SELECT setval(
               pg_get_serial_sequence('repairers', 'id_repairer'),
               COALESCE(MAX(id_repairer), 1),
               COUNT(*) > 0
       )
FROM repairers;


SELECT setval(
               pg_get_serial_sequence('vehicles', 'id_vehicle'),
               COALESCE(MAX(id_vehicle), 1),
               COUNT(*) > 0
       )
FROM vehicles;


SELECT setval(
               pg_get_serial_sequence('buyings', 'id_buying'),
               COALESCE(MAX(id_buying), 1),
               COUNT(*) > 0
       )
FROM buyings;


SELECT setval(
               pg_get_serial_sequence('tickets', 'id_ticket'),
               COALESCE(MAX(id_ticket), 1),
               COUNT(*) > 0
       )
FROM tickets;


SELECT setval(
               pg_get_serial_sequence('demands', 'id_demand'),
               COALESCE(MAX(id_demand), 1),
               COUNT(*) > 0
       )
FROM demands;


SELECT setval(
               pg_get_serial_sequence('repair_infos', 'id_repair_info'),
               COALESCE(MAX(id_repair_info), 1),
               COUNT(*) > 0
       )
FROM repair_infos;


SELECT setval(
               pg_get_serial_sequence('subscriptions', 'id_subscription'),
               COALESCE(MAX(id_subscription), 1),
               COUNT(*) > 0
       )
FROM subscriptions;