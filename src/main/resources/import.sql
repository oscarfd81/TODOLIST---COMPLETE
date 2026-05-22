-- Usuarios (contraseñas con BCrypt para producción, aquí {noop} para demo)
INSERT IGNORE INTO user_entity (id, email, username, password, is_admin, is_gestor, is_user) VALUES (1, 'admin@gmail.com', 'admin', '{noop}1234', true, false, false);
INSERT IGNORE INTO user_entity (id, email, username, password, is_admin, is_gestor, is_user) VALUES (2, 'gestor@gmail.com', 'gestor', '{noop}1234', false, true, false);
INSERT IGNORE INTO user_entity (id, email, username, password, is_admin, is_gestor, is_user) VALUES (3, 'user@gmail.com', 'user', '{noop}1234', false, false, true);

-- Categorías
INSERT IGNORE INTO cat (id, name) VALUES (1, 'Trabajo');
INSERT IGNORE INTO cat (id, name) VALUES (2, 'Estudio');
INSERT IGNORE INTO cat (id, name) VALUES (3, 'Personal');
INSERT IGNORE INTO cat (id, name) VALUES (4, 'Gimnasio');
INSERT IGNORE INTO cat (id, name) VALUES (5, 'Hobbie');
INSERT IGNORE INTO cat (id, name) VALUES (6, 'Amigos');
INSERT IGNORE INTO cat (id, name) VALUES (7, 'Familia');

-- Tags
INSERT IGNORE INTO tagd (id, name) VALUES (1, 'Baloncesto');
INSERT IGNORE INTO tagd (id, name) VALUES (2, 'Rugby');
INSERT IGNORE INTO tagd (id, name) VALUES (3, 'Cumpleaños');
INSERT IGNORE INTO tagd (id, name) VALUES (4, 'Quedada');
INSERT IGNORE INTO tagd (id, name) VALUES (5, 'Examen');
INSERT IGNORE INTO tagd (id, name) VALUES (6, 'Tarea');
INSERT IGNORE INTO tagd (id, name) VALUES (7, 'Ver pelicula');

-- Tareas de ejemplo
INSERT IGNORE INTO task (id, author_id, created_at, deadline, title, description, status, priority, important) VALUES (1, 3, CURRENT_TIMESTAMP, '2026-04-22 08:00:00', 'Ir al gimnasio', 'Rutina de pierna y 20 minutos de cardio', 'EN_PROCESO', 'MEDIA', true);
INSERT IGNORE INTO task (id, author_id, created_at, deadline, title, description, status, priority, important) VALUES (2, 3, CURRENT_TIMESTAMP, '2026-04-22 10:30:00', 'Estudiar Java Spring', 'Ver el módulo de persistencia de datos y JPA', 'PENDIENTE', 'ALTA', true);
INSERT IGNORE INTO task (id, author_id, created_at, deadline, title, description, status, priority, important) VALUES (3, 3, CURRENT_TIMESTAMP, '2026-04-22 14:00:00', 'Comprar comida', 'Ir al súper por pechuga de pollo, arroz y verduras', 'NO_HECHO', 'BAJA', false);
INSERT IGNORE INTO task (id, author_id, created_at, deadline, title, description, status, priority, important) VALUES (4, 3, CURRENT_TIMESTAMP, '2026-04-23 16:00:00', 'Diseño Web UI/UX', 'Practicar con Figma el rediseño del dashboard personal', 'EN_PROCESO', 'MEDIA', true);
INSERT IGNORE INTO task (id, author_id, created_at, deadline, title, description, status, priority, important) VALUES (5, 3, CURRENT_TIMESTAMP, '2026-04-23 21:00:00', 'Noche de cine', 'Ver la película que recomendaron en el foro', 'HECHO', 'BAJA', false);
INSERT IGNORE INTO task (id, author_id, created_at, deadline, title, description, status, priority, important) VALUES (6, 3, CURRENT_TIMESTAMP, '2026-04-24 09:00:00', 'Repasar SQL', 'Practicar JOINs y subconsultas en la base de datos de prueba', 'PENDIENTE', 'ALTA', false);
INSERT IGNORE INTO task (id, author_id, created_at, deadline, title, description, status, priority, important) VALUES (7, 3, CURRENT_TIMESTAMP, '2026-04-24 18:00:00', 'Llamar a soporte', 'Resolver el problema con la conexión a internet de casa', 'NO_HECHO', 'MEDIA', true);
INSERT IGNORE INTO task (id, author_id, created_at, deadline, title, description, status, priority, important) VALUES (8, 3, CURRENT_TIMESTAMP, '2026-04-25 11:00:00', 'Limpieza general', 'Organizar el escritorio y limpiar el equipo', 'HECHO', 'BAJA', false);
INSERT IGNORE INTO task (id, author_id, created_at, deadline, title, description, status, priority, important) VALUES (9, 3, CURRENT_TIMESTAMP, '2026-04-25 19:00:00', 'Leer libro técnico', 'Leer 2 capítulos del libro de Clean Code', 'PENDIENTE', 'MEDIA', true);
INSERT IGNORE INTO task (id, author_id, created_at, deadline, title, description, status, priority, important) VALUES (10, 3, CURRENT_TIMESTAMP, '2026-04-26 23:59:00', 'Planificar semana', 'Organizar las tareas y objetivos de la próxima semana', 'EN_PROCESO', 'ALTA', false);
