-- pool
INSERT INTO pool (id, description, expired_at, version) VALUES (1, 'first election', CURRENT_TIMESTAMP(), 0);

-- pool option
INSERT INTO pool_option (id, pool_id, description, version) VALUES (1, 1, 'first option', 0);
INSERT INTO pool_option (id, pool_id, description, version) VALUES (2, 1, 'second option', 0);
INSERT INTO pool_option (id, pool_id, description, version) VALUES (3, 1, 'third option', 0);
