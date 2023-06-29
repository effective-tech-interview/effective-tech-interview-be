ALTER TABLE page_question CHANGE COLUMN feedback positive_feedback VARCHAR(2000) NULL;
ALTER TABLE page_question ADD COLUMN improvement_feedback VARCHAR(2000) NULL;
