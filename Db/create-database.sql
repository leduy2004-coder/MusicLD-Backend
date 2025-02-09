-- Kiểm tra nếu database chưa tồn tại thì tạo
IF NOT EXISTS (SELECT name FROM sys.databases WHERE name = 'MusicLD')
BEGIN
    CREATE DATABASE MusicLD;
END
GO

-- Sử dụng database MusicLD
USE MusicLD;
GO

-- Đợi Spring Boot tạo bảng trước khi tiếp tục
WAITFOR DELAY '00:00:10'; -- Đợi 10 giây (có thể chỉnh)

-- Kiểm tra bảng có tồn tại không trước khi tạo PROCEDURE & FUNCTION
IF OBJECT_ID('music', 'U') IS NULL OR
   OBJECT_ID('users', 'U') IS NULL OR
   OBJECT_ID('payment', 'U') IS NULL OR
   OBJECT_ID('followers', 'U') IS NULL
BEGIN
    PRINT 'Bảng chưa được tạo, dừng việc tạo PROCEDURE & FUNCTION';
    RETURN;
END
GO

-- Xóa procedure nếu đã tồn tại
IF OBJECT_ID('dbo.spStatisticsMusicByYear', 'P') IS NOT NULL
DROP PROCEDURE dbo.spStatisticsMusicByYear;
GO

-- Tạo procedure
CREATE PROCEDURE dbo.spStatisticsMusicByYear
    @year INT
AS
BEGIN
    DECLARE @Statistic TABLE (
        months INT,
        countMusic INT
    );

    DECLARE @i INT = 1;
    WHILE (@i <= 12)
BEGIN
INSERT INTO @Statistic (months, countMusic)
SELECT @i, COUNT(*)
FROM music
WHERE YEAR(created_date) = @year AND MONTH(created_date) = @i;

SET @i = @i + 1;
END;

SELECT * FROM @Statistic;
END;
GO

-- Xóa function nếu đã tồn tại
IF OBJECT_ID('dbo.fnStatisticsByYear', 'IF') IS NOT NULL
DROP FUNCTION dbo.fnStatisticsByYear;
GO

-- Tạo function
CREATE FUNCTION dbo.fnStatisticsByYear
(
    @year INT
)
    RETURNS TABLE
    AS
RETURN
(
    SELECT
        (SELECT COUNT(DISTINCT u.id)
         FROM users u
         WHERE YEAR(u.created_date) = @year) AS totalAccount,

        (SELECT COUNT(DISTINCT m.id)
         FROM music m
         WHERE YEAR(m.created_date) = @year) AS totalMusic,

        (SELECT ISNULL(SUM(p.amount), 0)
         FROM payment p
         WHERE YEAR(p.created_date) = @year) AS totalAmount
);
GO

-- Xóa function nếu đã tồn tại
IF OBJECT_ID('dbo.fnTopUserByYear', 'IF') IS NOT NULL
DROP FUNCTION dbo.fnTopUserByYear;
GO

-- Tạo function
CREATE FUNCTION dbo.fnTopUserByYear
(
    @year INT
)
    RETURNS TABLE
    AS
RETURN
(
    SELECT TOP 5
        u.id AS userId,
        u.nickname,
        COUNT(m.id) AS totalMusic,
        ISNULL(SUM(p.amount), 0) AS totalAmount,
        COUNT(f.id) AS totalFollower
    FROM users u
             LEFT JOIN music m ON m.user_id = u.id AND YEAR(m.created_date) = @year
             LEFT JOIN payment p ON p.user_id = u.id AND YEAR(p.created_date) = @year
             LEFT JOIN followers f ON f.receiver_id = u.id AND YEAR(f.created_date) = @year
    GROUP BY u.id, u.nickname
    ORDER BY totalMusic DESC
);
GO
