IF NOT EXISTS (SELECT name FROM sys.databases WHERE name = 'MusicLD')
BEGIN
    CREATE DATABASE MusicLD;
END