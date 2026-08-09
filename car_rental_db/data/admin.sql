INSERT INTO admins (
    admin_name,
    email,
    password_hash,
    role
) VALUES
      (
          'admin',
          'admin@vehiclerental.com',
          '$2a$10$exampleAdminPasswordHash',
          'ADMIN'
      ),
      (
          'manager',
          'manager@vehiclerental.com',
          '$2a$10$exampleManagerPasswordHash',
          'ADMIN'
      ),
      (
          'superadmin',
          'superadmin@vehiclerental.com',
          '$2a$10$exampleSuperAdminPasswordHash',
          'ADMIN'
      );