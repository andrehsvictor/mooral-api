CREATE TABLE IF NOT EXISTS mural (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    position INT NOT NULL DEFAULT 0,
    views_count INT DEFAULT 0,
    posts_count INT DEFAULT 0,
    posts_likes_count INT DEFAULT 0,
    posts_comments_count INT DEFAULT 0,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT mural_user_id_position_uk UNIQUE (user_id, position)
);