CREATE TABLE IF NOT EXISTS users (
                                     id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                     email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255),
    nickname VARCHAR(255) UNIQUE,
    -- Other columns may be added based on the actual requirements
    );
CREATE TABLE IF NOT EXISTS category (
                                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                        name VARCHAR(255) UNIQUE,
    parent_id BIGINT,
    FOREIGN KEY (parent_id) REFERENCES category (id)
    );
CREATE TABLE IF NOT EXISTS article (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         title VARCHAR(255),
                         content MEDIUMTEXT,
                         author_id BIGINT,
                         category_id BIGINT,
                         created_at DATETIME,
                         updated_at DATETIME,
                         FOREIGN KEY (author_id) REFERENCES User(id),
                         FOREIGN KEY (category_id) REFERENCES Category(id)
);


--
-- CREATE PROCEDURE INSERT_USER(IN userEmail VARCHAR(255), IN userPassword VARCHAR(255), IN userNickname VARCHAR(255))
-- BEGIN
--     DECLARE hashedPassword VARCHAR(255);
--     SET hashedPassword = (SELECT BCryptHash(userPassword, BCryptGenSalt()));
--
-- INSERT INTO users (email, password, nickname) VALUES (userEmail, hashedPassword, userNickname);
-- END;
--
-- -- Insert data for articles using user IDs as references
--
-- CALL INSERT_USER 'Author1@gmail.com', 'author1pw', 'Author1';
-- CALL INSERT_USER 'Author2@gmail.com', 'author2pw', 'Author2';
-- CALL INSERT_USER 'Author3@gmail.com', 'author3pw', 'Author3';
-- INSERT INTO category (name, parent_id) VALUES
--     ('Category1', NULL);
-- INSERT INTO category (name, parent_id) VALUES
--     ('Category2', (SELECT id FROM category WHERE name = 'Category1'));
--
--
-- INSERT INTO article (title, content, author_id, category_id) VALUES
--     ('The sun-kissed beach',
--      'The sun-kissed beach was a veritable haven. Soft, white sand stretched lazily from one end of the coastline to the other, inviting visitors to take off their shoes and dip their toes in the cool water.',
--      (SELECT id FROM users WHERE email = 'Author1@gmail.com'),
--      (SELECT id FROM category WHERE name = 'Category1'));
--
-- INSERT INTO article (title, content, author_id, category_id) VALUES
--     ('The older man',
--      'The older man shuffled slowly down the dirt road, a faded baseball cap pulled low over his eyes to shield them from the sun’s rays. He wore overalls and a flannel shirt, his hands calloused from years of hard work in the fields.',
--      (SELECT id FROM users WHERE email = 'Author2@gmail.com'),
--      (SELECT id FROM category WHERE name = 'Category2'));
--
-- INSERT INTO article (title, content, author_id, category_id) VALUES
--     ('The bustling city',
--      'The bustling city street was a melting pot of cultures, languages, and flavors. Merchants called out from their booths as eager shoppers stopped to examine their wares. Everywhere I looked, people were walking, talking, laughing- the occasional honk of a car horn punctuating the air.',
--      (SELECT id FROM users WHERE email = 'Author3@gmail.com'),
--      (SELECT id FROM category WHERE name = 'Category2'));
--
-- INSERT INTO article (title, content, author_id, category_id) VALUES
--     ('The lush meadow',
--      'The lush meadow was blanketed in soft green grass, punctuated with wildflowers in all rainbow colours. The sun shone brightly in the sky, warming everything beneath it with its gentle rays. In the distance, a stream babbled peacefully as birds chirped their songs from the trees that lined it.',
--      (SELECT id FROM users WHERE email = 'Author2@gmail.com'),
--      (SELECT id FROM category WHERE name = 'Category2'));
--
-- INSERT INTO article (title, content, author_id, category_id) VALUES
--     ('The decrepit old mansion',
--      'The decrepit old mansion stood atop a hill like a silent sentinel watching over the valley below. Tall windows ' ||
--      'stared blankly from the walls, their glass panes long since shattered. The grounds were overgrown with weeds and wildflowers, a testament to the fact that no one had set foot here in many years.',
--     (SELECT id FROM users WHERE email = 'Author3@gmail.com'),
--     (SELECT id FROM category WHERE name = 'Category2'));
-- INSERT INTO article (title, content, author_id, category_id) VALUES
--     ('The forest',
--      'The forest was alive with activity; small creatures skittered through the undergrowth while
--      bright-coloured birds flitted from branch to branch overhead. A cool breeze caressed my skin and rustled through the leaves of nearby trees as I walked along the path, breathing deeply of the damp woodland air.',
--      (SELECT id FROM users WHERE email = 'Author1@gmail.com'),
--      (SELECT id FROM category WHERE name = 'Category1'));
-- INSERT INTO article (title, content, author_id, category_id) VALUES
--     ('The ancient',
--      'The ancient ruins surrounded a vast desert, their sand-covered stones looking out over miles of wind-swept ' ||
--      'dunes. I walked through the crumbling archway and into the courtyard, taking in the eerie silence that pervaded the entire site.',
--      (SELECT id FROM users WHERE email = 'Author1@gmail.com'),
--      (SELECT id FROM category WHERE name = 'Category2'));
-- INSERT INTO article (title, content, author_id, category_id) VALUES
--     ('The silver moon',
--      'The silver moon shone brightly against the night sky, its reflection glittering on the lake’s still surface ' ||
--      'below. Fireflies sparkled around me like stars fallen from the heavens, their lights twinkling in tandem with those of distant galaxies.',
--     (SELECT id FROM users WHERE email = 'Author3@gmail.com'),
--     (SELECT id FROM category WHERE name = 'Category1'));
-- INSERT INTO article (title, content, author_id, category_id) VALUES
--     ('The beach',
--      'The beach was a tranquil paradise, soft white sand stretching out towards an endless blue horizon. The waves ' ||
--      'crashed gently against the shore, their foamy spray cooling my skin under the hot afternoon sun',
--     (SELECT id FROM users WHERE email = 'Author2@gmail.com'),
--     (SELECT id FROM category WHERE name = 'Category1'));
-- INSERT INTO article (title, content, author_id, category_id) VALUES
--     ('The snow-capped mountain',
--      'The snow-capped mountain peak rose majestically above me, its rocky sides glinting in the bright sunlight. I ' ||
--      'could feel the chill of the air around me and see my breath misting in front of me as I trudged up the steep path.',
--     (SELECT id FROM users WHERE email = 'Author3@gmail.com'),
--     (SELECT id FROM category WHERE name = 'Category1'));
-- INSERT INTO article (title, content, author_id, category_id) VALUES
--     ('The sun was setting',
--      'The sun was setting, painting the sky in vibrant shades of orange, pink and purple. The clouds were streaked ' ||
--      'with golden light, completing the brilliant spectacle that was taking place all around me.',
--     (SELECT id FROM users WHERE email = 'Author1@gmail.com'),
--     (SELECT id FROM category WHERE name = 'Category1'));
