INSERT INTO users (username, password, role)
VALUES
    ('admin', '{noop}admin1234', 'ADMIN'),
    ('user1', '{noop}user1234', 'USER');

INSERT INTO contents (
    title,
    description,
    view_count,
    created_date,
    created_by,
    last_modified_date,
    last_modified_by
) VALUES
      (
          '첫 번째 콘텐츠',
          '첫 번째 콘텐츠 설명입니다.',
          0,
          CURRENT_TIMESTAMP,
          'admin',
          CURRENT_TIMESTAMP,
          'admin'
      ),
      (
          '두 번째 콘텐츠',
          '두 번째 콘텐츠 설명입니다.',
          0,
          CURRENT_TIMESTAMP,
          'user1',
          CURRENT_TIMESTAMP,
          'user1'
      );