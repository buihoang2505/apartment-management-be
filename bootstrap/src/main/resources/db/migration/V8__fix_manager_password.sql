-- Fix manager password to BCrypt hash of 'manager123'
UPDATE users
SET password = '$2a$10$/FZi2VqSMYZJaESPLJIVO.uSt27lFAz4KlhIqks0SDxyaxd0kpmyu'
WHERE username = 'manager';
