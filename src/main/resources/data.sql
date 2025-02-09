
INSERT INTO role (code, name) VALUES
                                  ('ADMIN', 'admin'),
                                  ('USER', 'user');

INSERT INTO users (created_by, gender, modified_by, status, created_date, dateofbirth, modified_date, auth_type, nickname, password, username)
VALUES
    (1, 1, NULL, 1, GETDATE(), '1990-01-01', GETDATE(), 'LOCAL', N'Lê Duy', '$2a$10$K1CLXKAzm2h4oLXBnKp81ej80qVnameK8cSgZmlfcydFFOs7GgYkK', 'admin'),
    (2, 0, NULL, 1, GETDATE(), '1992-02-02', GETDATE(), 'LOCAL', N'Lê Phước', '$2a$10$8fgiC4HADVoJfbrLE.0Xa.1B0dqu2Ybk39V99c1oivzWmCb6yLQkO', 'a'),
    (3, 1, NULL, 0, GETDATE(), '1993-03-03', GETDATE(), 'LOCAL', 'User3', '$2a$10$HlEAiu8qQrgXUKyqzzA./eMZMrxEyI60O6BORsqbmc6TDOJwdQvPu', 'b'),
    (4, 0, NULL, 1, GETDATE(), '1994-04-04', GETDATE(), 'LOCAL', 'User4', '$2a$10$J48GTAYrx5eUz2C8dORniuDW3EdSHEWbX0BwHVyrvaH3CDV8Jc.2S', 'c'),
    (5, 1, NULL, 0, GETDATE(), '1995-05-05', GETDATE(), 'LOCAL', 'User5', '$2a$10$i7LRM.VdOtQ411wj./E1iuI57Rseyc89cYJ4Ferfb1XUQlp1cd6ty', 'd'),
    (6, 0, NULL, 1, GETDATE(), '1996-06-06', GETDATE(), 'LOCAL', 'User6', '$2a$10$RxsIC9ceqmaDNAZThJWWKuPjOU.iYZAvEPPZwTELmC0040Y07ojj6', 'e'),
    (7, 1, NULL, 0, GETDATE(), '1997-07-07', GETDATE(), 'LOCAL', 'User7', '$2a$10$fpWrKGwEpjJLdR6nr93z7eyQnscz4XYprsqr9vi58xIvGp4EyB8CS', 'f'),
    (8, 0, NULL, 1, GETDATE(), '1998-08-08', GETDATE(), 'LOCAL', 'User8', 'password8', 'user8'),
    (9, 1, NULL, 1, GETDATE(), '1999-09-09', GETDATE(), 'LOCAL', 'User9', 'password9', 'user9'),
    (10, 0, NULL, 0, GETDATE(), '2000-10-10', GETDATE(), 'LOCAL', 'User10', 'password10', 'user10');

INSERT INTO user_role (role_id, user_id)
VALUES
    (1, 1),
    (2, 2),
    (2, 3),
    (2, 4),
    (2, 5),
    (2, 6),
    (2, 7),
    (2, 8),
    (2, 9),
    (2, 10);

INSERT INTO music (created_by, modified_by, duration, status, user_id, created_date, modified_date, access, lyrics, public_id, title, url)
VALUES
    (1, NULL, 180, 1, 1, GETDATE(), GETDATE(), 'PUBLIC', N'<p><strong>Verse 1:</strong> Em là ánh sáng trong đời anh</p><p>Dù cho mây mù, vẫn sáng ngời</p><p>Từng nụ cười, em trao cho anh</p><p>Đã xóa đi bao đêm dài vô vọng</p><p><strong>Chorus:</strong> Ánh sáng từ em, soi đường cho anh</p><p>Đưa anh qua những ngày tháng u tối</p><p>Ánh sáng từ em, dịu dàng mỗi đêm</p><p>Em là hy vọng, là niềm tin trong anh</p><p><strong>Verse 2:</strong> Khi em gần bên, anh quên đi sợ hãi</p><p>Vì tình yêu này sẽ mãi không phai</p><p>Em là ngôi sao giữa bầu trời mênh mông</p><p>Dẫn lối anh tìm về nơi yêu thương</p>', 'yd19keinsz6177mr2sg2', N'Ánh Sáng Từ Em', 'https://res.cloudinary.com/diube3uvk/video/upload/v1735137800/yd19keinsz6177mr2sg2.mp3'),
    (2, NULL, 200, 1, 2, GETDATE(), GETDATE(), 'PUBLIC', N'<p><strong>Verse 1:</strong> Mỗi khi anh nhìn vào đôi mắt em</p><p>Tình yêu ấy như ngọn lửa không bao giờ tắt</p><p>Dù xa cách, anh vẫn thấy em trong giấc mơ</p><p>Mà mỗi đêm, anh tự hỏi liệu có còn yêu?</p><p><strong>Chorus:</strong> Dù chúng ta xa nhau, tình này vẫn không phai</p><p>Em ở nơi đâu, anh vẫn tìm thấy em trong trái tim này</p><p>Dù cho thời gian, dù cho mọi thứ thay đổi</p><p>Tình yêu của chúng ta vẫn mãi mãi, không bao giờ vơi</p><p><strong>Verse 2:</strong> Những kỷ niệm chúng ta đã xây dựng</p><p>Như những ngôi sao trên bầu trời đêm tối</p><p>Anh sẽ giữ mãi trong lòng những khoảnh khắc ấy</p><p>Dù cách xa, nhưng trái tim này vẫn không nguôi</p>', 'z3lqxnxxqjo45z9tkivy', N'Dù Chúng Ta Xa Nhau', 'https://res.cloudinary.com/diube3uvk/video/upload/v1735137800/z3lqxnxxqjo45z9tkivy.mp3'),
    (3, NULL, 150, 1, 8, GETDATE(), GETDATE(), 'PRIVATE', N'<p><strong>Verse 1:</strong> Mưa rơi trong đêm vắng, lòng anh như tê tái</p><p>Những kỷ niệm buồn cứ quay về, mãi không dứt</p><p>Dù cho đường phố này có vắng bóng em</p><p>Anh vẫn bước đi, vẫn tìm thấy em trong mưa</p><p><strong>Chorus:</strong> Hòa mình vào cơn mưa, để quên đi nỗi đau</p><p>Nước mắt hòa lẫn cùng mưa, cuốn trôi bao ưu phiền</p><p>Hòa mình vào cơn mưa, anh sẽ quên em thôi</p><p>Vì trong cơn mưa này, anh tìm thấy chính mình</p><p><strong>Verse 2:</strong> Bước đi trong mưa, không còn sợ hãi</p><p>Vì em đã đi rồi, anh không còn chờ đợi</p><p>Mưa xóa đi vết thương trong lòng anh</p><p>Anh sẽ tìm lại chính mình trong những giọt mưa</p>', 'h1vdvrvmrvgvw2jdvn7m', N'Hòa Mình Vào Cơn Mưa', 'https://res.cloudinary.com/diube3uvk/video/upload/v1735137805/h1vdvrvmrvgvw2jdvn7m.mp3'),
    (4, NULL, 210, 1, 4, GETDATE(), GETDATE(), 'PUBLIC', N'<p><strong>Verse 1:</strong> Mỗi buổi sáng, khi anh thức dậy</p><p>Em là người đầu tiên anh nghĩ đến</p><p>Dù thời gian trôi qua, tình yêu vẫn không thay đổi</p><p>Bởi vì em là tất cả, là lý do anh sống</p><p><strong>Chorus:</strong> Vì em là tất cả, là nguồn sống trong anh</p><p>Mỗi nhịp đập tim này chỉ dành cho em</p><p>Vì em là tất cả, là điều duy nhất anh cần</p><p>Anh sẽ mãi yêu em, đến suốt đời này</p><p><strong>Verse 2:</strong> Khi em mỉm cười, thế giới như ngừng quay</p><p>Em làm trái tim anh thổn thức không ngừng</p><p>Bên em, anh không còn sợ hãi</p><p>Vì có em, anh luôn mạnh mẽ vượt qua</p>', 'ynbhs68b14gngrywkosg', N'Vì Em Là Tất Cả', 'https://res.cloudinary.com/diube3uvk/video/upload/v1735137804/ynbhs68b14gngrywkosg.mp3'),
    (5, NULL, 180, 1, 9, GETDATE(), GETDATE(), 'PRIVATE', N'<p><strong>Verse 1:</strong> Anh đứng trong cơn mưa, nhìn theo bóng em xa dần</p><p>Những lời hứa như cơn gió, bay đi theo thời gian</p><p>Anh tìm lại những ký ức, nhưng chúng chỉ còn là nỗi đau</p><p>Vì anh không thể tìm thấy em trong thế giới này</p><p><strong>Chorus:</strong> Tìm lại em, tìm lại tình yêu đã mất</p><p>Tìm lại những tháng ngày ngọt ngào đã qua</p><p>Nhưng giờ đây, em đã đi xa</p><p>Anh chỉ còn lại những ký ức mờ nhạt</p><p><strong>Verse 2:</strong> Mỗi ngày qua đi, anh lại càng cảm thấy trống vắng</p><p>Nhưng em vẫn ở đâu đó trong trái tim này</p><p>Dù em không ở đây, nhưng tình yêu không bao giờ phai</p><p>Anh sẽ mãi tìm lại em trong từng giấc mơ</p>', 'ynk7iefo8o2hxeg4mx8u', N'Tìm Lại Em', 'https://res.cloudinary.com/diube3uvk/video/upload/v1735137811/ynk7iefo8o2hxeg4mx8u.mp3');


INSERT INTO avatar (created_by, modified_by, music_id, status, user_id, created_date, modified_date, url, name, public_id, type)
VALUES
    (1, NULL, null, 1, 1, GETDATE(), GETDATE(), 'https://res.cloudinary.com/diube3uvk/image/upload/v1735131021/jci2q9x4kxnrudjqpucq.jpg', 'Avatar1', 'jci2q9x4kxnrudjqpucq', 'USER'),
    (2, NULL, null, 1, 2, GETDATE(), GETDATE(), 'https://res.cloudinary.com/diube3uvk/image/upload/v1735131013/vriksc5eyy341bquw3wi.jpg', 'Avatar2', 'vriksc5eyy341bquw3wi', 'USER'),
    (3, NULL, null, 0, 3, GETDATE(), GETDATE(), 'https://res.cloudinary.com/diube3uvk/image/upload/v1735130998/k186y1xpyxhza1uqufm0.jpg', 'Avatar3', 'k186y1xpyxhza1uqufm0', 'USER'),
    (4, NULL, null, 1, 4, GETDATE(), GETDATE(), 'https://res.cloudinary.com/diube3uvk/image/upload/v1735130969/rawtk230z8igslcisykn.jpg', 'Avatar4', 'rawtk230z8igslcisykn', 'USER'),
    (5, NULL, null, 0, 5, GETDATE(), GETDATE(), 'https://res.cloudinary.com/diube3uvk/image/upload/v1735130950/ptq8vnakncpg0lgiuc10.jpg', 'Avatar5', 'ptq8vnakncpg0lgiuc10', 'USER'),
    (6, NULL, null, 1, 6, GETDATE(), GETDATE(), 'https://res.cloudinary.com/diube3uvk/image/upload/v1735130936/jfqo7mxfiawnbbodgtdk.jpg', 'Avatar6', 'jfqo7mxfiawnbbodgtdk', 'USER'),
    (7, NULL, null, 1, 7, GETDATE(), GETDATE(), 'https://res.cloudinary.com/diube3uvk/image/upload/v1735130920/eomoduzren3opc3dxm66.jpg', 'Avatar7', 'eomoduzren3opc3dxm66', 'USER'),
    (8, NULL, null, 0, 8, GETDATE(), GETDATE(), 'https://res.cloudinary.com/diube3uvk/image/upload/v1735130902/hmnikpnsfhf1t4lkixjq.jpg', 'Avatar8', 'hmnikpnsfhf1t4lkixjq', 'USER'),
    (9, NULL, null, 1, 9, GETDATE(), GETDATE(), 'https://res.cloudinary.com/diube3uvk/image/upload/v1735130821/gnupbfpzi4w2t5xf8scd.jpg', 'Avatar9', 'gnupbfpzi4w2t5xf8scd', 'USER'),
    (10, NULL, null, 1, 10, GETDATE(), GETDATE(), 'https://res.cloudinary.com/diube3uvk/image/upload/v1735130797/txvuurrbswkojizpehoa.jpg', 'Avatar10', 'txvuurrbswkojizpehoa', 'USER'),
    (1, NULL, 1, 1, null, GETDATE(), GETDATE(), 'https://res.cloudinary.com/diube3uvk/image/upload/v1735130778/ve535t3bcodrd61qvnww.jpg', 'Music2', 've535t3bcodrd61qvnww', 'MUSIC'),
    (2, NULL, 2, 1, null, GETDATE(), GETDATE(), 'https://res.cloudinary.com/diube3uvk/image/upload/v1735131708/wykfb32crqydyztqnt8q.jpg', 'Music3', 'wykfb32crqydyztqnt8q', 'MUSIC'),
    (3, NULL, 3, 1, null, GETDATE(), GETDATE(), 'https://res.cloudinary.com/diube3uvk/image/upload/v1735131750/gaojwb2ajmzcipnpmcaj.jpg', 'Music4', 'gaojwb2ajmzcipnpmcaj', 'MUSIC'),
    (4, NULL, 4, 1, null, GETDATE(), GETDATE(), 'https://res.cloudinary.com/diube3uvk/image/upload/v1735131787/mluwwd2kvuniuafhpsh2.jpg', 'Music5', 'mluwwd2kvuniuafhpsh2', 'MUSIC'),
    (5, NULL, 5, 1, null, GETDATE(), GETDATE(), 'https://res.cloudinary.com/diube3uvk/image/upload/v1735131823/kftv9lpppshtyumpjgat.jpg', 'Music6', 'kftv9lpppshtyumpjgat', 'MUSIC');


INSERT INTO comment (created_by, modified_by, music_id, parent_comment_id, user_id, created_date, modified_date, content)
VALUES
    (1, NULL, 1, NULL, 1, GETDATE(), GETDATE(), 'Great music!'),
    (2, NULL, 2, NULL, 2, GETDATE(), GETDATE(), 'Love this song!'),
    (3, NULL, 3, NULL, 3, GETDATE(), GETDATE(), 'Very energetic track!'),
    (4, NULL, 4, NULL, 4, GETDATE(), GETDATE(), 'Amazing lyrics!'),
    (5, NULL, 5, NULL, 5, GETDATE(), GETDATE(), 'So emotional!'),
    (6, NULL, 1, NULL, 6, GETDATE(), GETDATE(), 'Perfect for relaxing!'),
    (7, NULL, 2, NULL, 7, GETDATE(), GETDATE(), 'Would love to hear more like this!'),
    (8, NULL, 3, NULL, 8, GETDATE(), GETDATE(), 'The melody is beautiful.'),
    (9, NULL, 4, NULL, 9, GETDATE(), GETDATE(), 'Great rhythm and beat!'),
    (10, NULL,5, NULL, 10, GETDATE(), GETDATE(), 'Impressive performance!');


INSERT INTO followers (created_by, modified_by, receiver_id, sender_id, created_date, modified_date, status)
VALUES
    (1, NULL, 1, 2, GETDATE(), GETDATE(), 'ACCEPTED'),
    (2, NULL, 2, 1, GETDATE(), GETDATE(), 'ACCEPTED'),
    (3, NULL, 3, 4, GETDATE(), GETDATE(), 'PENDING'),
    (4, NULL, 4, 5, GETDATE(), GETDATE(), 'ACCEPTED'),
    (5, NULL, 5, 4, GETDATE(), GETDATE(), 'ACCEPTED'),
    (7, NULL, 7, 8, GETDATE(), GETDATE(), 'PENDING'),
    (8, NULL, 10, 9, GETDATE(), GETDATE(), 'ACCEPTED'),
    (9, NULL, 9, 10, GETDATE(), GETDATE(), 'ACCEPTED'),
    (10, NULL, 10, 8, GETDATE(), GETDATE(), 'PENDING');

INSERT INTO heart (created_by, modified_by, music_id, user_id, created_date, modified_date)
VALUES
    (1, NULL, 1, 1, GETDATE(), GETDATE()),
    (2, NULL, 1, 2, GETDATE(), GETDATE()),
    (3, NULL, 3, 3, GETDATE(), GETDATE()),
    (4, NULL, 3, 4, GETDATE(), GETDATE()),
    (5, NULL, 5, 1, GETDATE(), GETDATE()),
    (6, NULL, 2, 6, GETDATE(), GETDATE()),
    (7, NULL, 2, 1, GETDATE(), GETDATE()),
    (8, NULL, 5, 8, GETDATE(), GETDATE()),
    (9, NULL, 4, 9, GETDATE(), GETDATE()),
    (10, NULL, 1, 10, GETDATE(), GETDATE());



INSERT INTO payment (created_by, modified_by, user_id, amount, created_date, modified_date, bank_code, code)
VALUES
    (1, NULL, 1, 50000, GETDATE(), GETDATE(), 'Vietcombank', 'payment_code_1'),
    (2, NULL, 2, 60000, GETDATE(), GETDATE(), 'Techcombank', 'payment_code_2'),
    (3, NULL, 3, 70000, GETDATE(), GETDATE(), 'BIDV', 'payment_code_3'),
    (4, NULL, 4, 80000, GETDATE(), GETDATE(), 'VietinBank', 'payment_code_4'),
    (5, NULL, 5, 90000, GETDATE(), GETDATE(), 'ACB', 'payment_code_5');


--
-- CREATE PROCEDURE spStatisticsMusicByYear
-- @year INT
-- AS
-- BEGIN
--     DECLARE @Statistic TABLE (
--                                  months INT,
--                                  countMusic INT
--                              );
--
--     DECLARE @i INT = 1;
--     WHILE (@i <= 12)
--         BEGIN
--             INSERT INTO @Statistic (months, countMusic)
--             SELECT @i, COUNT(*)
--             FROM music
--             WHERE YEAR(created_date) = @year AND MONTH(created_date) = @i;
--
--             SET @i = @i + 1;
--         END;
--
--     SELECT * FROM @Statistic;
-- END;
--
-- CREATE FUNCTION dbo.fnStatisticsByYear
-- (
--     @year INT
-- )
--     RETURNS TABLE
--         AS
--         RETURN
--         (
--         SELECT
--             (SELECT COUNT(DISTINCT u.id)
--              FROM users u
--              WHERE YEAR(u.created_date) = @year) AS totalAccount,
--
--             (SELECT COUNT(DISTINCT m.id)
--              FROM music m
--              WHERE YEAR(m.created_date) = @year) AS totalMusic,
--
--             (SELECT ISNULL(SUM(p.amount), 0)
--              FROM payment p
--              WHERE YEAR(p.created_date) = @year) AS totalAmount
--         );
--
--
-- CREATE FUNCTION dbo.fnTopUserByYear
-- (
--     @year INT
-- )
--     RETURNS TABLE
--         AS
--         RETURN
--         (
--         SELECT TOP 5
--             u.id AS userId,
--             u.nickname,
--             COUNT(m.id) AS totalMusic,
--             ISNULL(SUM(p.amount), 0) AS totalAmount,
--             COUNT(f.id) AS totalFollower
--         FROM users u
--                  LEFT JOIN music m ON m.user_id = u.id AND YEAR(m.created_date) = @year
--                  LEFT JOIN payment p ON p.user_id = u.id AND YEAR(p.created_date) = @year
--                  LEFT JOIN followers f ON f.receiver_id = u.id AND YEAR(f.created_date) = @year
--         GROUP BY u.id, u.nickname
--         ORDER BY totalMusic DESC
--         );