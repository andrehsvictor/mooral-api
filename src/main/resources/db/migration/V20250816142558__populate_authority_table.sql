INSERT INTO authority (name, display_name, description) VALUES
-- Roles
('ROLE_ROOT', 'Root', 'Root user with full access'),
('ROLE_ADMIN', 'Admin', 'Administrator with elevated privileges'),
('ROLE_MODERATOR', 'Moderator', 'Moderator with limited administrative privileges'),
('ROLE_USER', 'User', 'Default role for all users'),

-- Permissions
('profile', 'Profile reading', 'Permission to read user profile data'),
('mural.read', 'Mural reading', 'Permission to read user murals'),
('mural.write', 'Mural writing', 'Permission to write user murals'),
('post.read', 'Post reading', 'Permission to read user posts and drafts'),
('post.write', 'Post writing', 'Permission to write user posts'),
('post.delete', 'Post deleting', 'Permission to delete user posts'),
('comment.read', 'Comment reading', 'Permission to read comments on posts'),
('comment.write', 'Comment writing', 'Permission to write comments on posts'),
('comment.delete', 'Comment deleting', 'Permission to delete comments on posts');