-- Fix: update admin password hash to correct BCrypt encoding of 'admin123'
UPDATE users
SET password = '$2a$10$OeAkrYXA9Tq/1mmFp2vULuAnh7TnTtvSyslccnGMVgz5HSSP4ldV.'
WHERE username = 'admin';