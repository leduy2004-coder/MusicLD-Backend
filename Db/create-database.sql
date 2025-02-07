IF NOT EXISTS (SELECT name FROM sys.databases WHERE name = 'MusicLD')
BEGIN
    CREATE DATABASE MusicLD;
END



CREATE PROCEDURE spStatisticsMusicByYear
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