-- Insert data for articles using user IDs as references
ALTER TABLE article ALTER COLUMN content CLOB; -- Data type of content in article is changed from VARCHAR to text
INSERT INTO article (title, content, author)
SELECT
    'The sun-kissed beach',
    'The sun-kissed beach was a veritable ' ||
    'haven. Soft, white sand stretched lazily' ||
    ' from one end of the coastline to the ' ||
    'other, inviting visitors to take off ' ||
    'their shoes and dip their toes in the ' ||
    'cool water.',
    u.id
FROM users u
WHERE u.email = 'Author1@gmail.com';

INSERT INTO article (title, content, author)
SELECT
    'The older Man',
    'The older man shuffled slowly down the dirt road, a faded baseball cap pulled low over his eyes to shield them from the sun’s rays. He wore overalls and a flannel shirt, his hands calloused from years of hard work in the fields.',
    u.id
FROM users u
WHERE u.email = 'Author2@gmail.com';
