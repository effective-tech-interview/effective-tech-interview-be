ALTER TABLE page ADD COLUMN mid_category_id BIGINT REFERENCES mid_category(id) ON DELETE CASCADE ON UPDATE CASCADE;
