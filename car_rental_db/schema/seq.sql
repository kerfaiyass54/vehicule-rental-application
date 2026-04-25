SELECT setval(
               pg_get_serial_sequence('vehicule', 'idvehicule'),
               (SELECT MAX(idvehicule) FROM vehicule)
       );