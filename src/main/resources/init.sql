DROP TABLE IF EXISTS schedule_tasks CASCADE;
DROP TABLE IF EXISTS tasks CASCADE;
DROP TABLE IF EXISTS schedules CASCADE;
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS all_audit CASCADE;
DROP SEQUENCE IF EXISTS counter CASCADE;

CREATE TABLE users(
	user_id SERIAL UNIQUE PRIMARY KEY,
	user_name varchar(16) NOT NULL,
	user_email varchar(254) NOT NULL,
	user_password text NOT NULL,
	user_role varchar(10) NOT NULL,
	CONSTRAINT user_name_not_empty CHECK (user_name <> ''),
	CONSTRAINT user_email_not_empty CHECK (user_email <> ''),
	CONSTRAINT user_password_not_empty CHECK (user_password <> ''),
	CONSTRAINT user_role_not_empty CHECK (user_role <> '')
);

CREATE TABLE schedules(
	schedule_id SERIAL UNIQUE PRIMARY KEY,
	user_id integer,
	schedule_title varchar(40) NOT NULL,
	schedule_duration integer NOT NULL,
	schedule_visibility boolean,
	FOREIGN KEY(user_id) REFERENCES users(user_id),
	CONSTRAINT schedule_duration_max CHECK (schedule_duration > 0 AND schedule_duration <= 7)
);

CREATE TABLE tasks(
	task_id SERIAL UNIQUE PRIMARY KEY,
	user_id integer,
	task_title text NOT NULL,
	task_content text NOT NULL,
	task_start integer NOT NULL,
	task_end integer NOT NULL,
	FOREIGN KEY(user_id) REFERENCES users(user_id),
	CONSTRAINT task_start_max CHECK (task_start >= 0 AND task_start <= 24),
	CONSTRAINT task_end_max CHECK (task_end >= 0 AND task_end <= 24)
);

CREATE TABLE schedule_tasks(
	task_id integer,
	schedule_id integer,
	FOREIGN KEY(task_id) REFERENCES tasks(task_id),
	FOREIGN KEY(schedule_id) REFERENCES schedules(schedule_id)
);

CREATE TABLE all_audit(
	event_counter integer UNIQUE PRIMARY KEY,
	event_name varchar(100),
	table_name varchar(100),
	user_id varchar(40),
	event_date timestamp
);

CREATE SEQUENCE counter AS integer INCREMENT BY 1 START 1; 

CREATE OR REPLACE FUNCTION process_audit() RETURNS TRIGGER AS '
    BEGIN
        IF (TG_OP = ''DELETE'') THEN
            INSERT INTO all_audit
                VALUES(nextval(''counter''),''DELETE'', TG_TABLE_NAME, OLD.user_id, now());
        ELSIF (TG_OP = ''UPDATE'') THEN
            INSERT INTO all_audit
                VALUES(nextval(''counter''), ''UPDATE'', TG_TABLE_NAME, OLD.user_id, now());
        ELSIF (TG_OP = ''INSERT'') THEN
            INSERT INTO all_audit
                VALUES(nextval(''counter''), ''INSERT'', TG_TABLE_NAME, NEW.user_id, now());
        END IF;
        RETURN NEW;
    END;
' LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION check_task_id() RETURNS TRIGGER AS '
    BEGIN
        IF (TG_OP = ''INSERT'') THEN
            DECLARE
                id integer;
            BEGIN
                FOR id IN SELECT task_id FROM schedule_tasks WHERE schedule_id = NEW.schedule_id LOOP
                IF id = NEW.task_id THEN
                RAISE EXCEPTION ''Task already exists in schedule!'';
                END IF;
                END LOOP;
			END;
        END IF;
        RETURN NEW;
    END;
' LANGUAGE plpgsql;

CREATE TRIGGER tasks_audit_ins
    AFTER INSERT ON tasks
    FOR EACH ROW EXECUTE Procedure process_audit();
CREATE TRIGGER tasks_audit_upd
    AFTER UPDATE ON tasks
    FOR EACH ROW EXECUTE procedure process_audit();
CREATE TRIGGER tasks_audit_del
    AFTER DELETE ON tasks
    FOR EACH ROW EXECUTE procedure process_audit();

CREATE TRIGGER schedules_audit_ins
    AFTER INSERT ON schedules
    FOR EACH ROW EXECUTE Procedure process_audit();
CREATE TRIGGER schedules_audit_upd
    AFTER UPDATE ON schedules
    FOR EACH ROW EXECUTE procedure process_audit();
CREATE TRIGGER schedules_audit_del
    AFTER DELETE ON schedules
    FOR EACH ROW EXECUTE procedure process_audit();

CREATE TRIGGER users_audit_ins
    AFTER INSERT ON users
    FOR EACH ROW EXECUTE Procedure process_audit();
CREATE TRIGGER users_audit_upd
    AFTER UPDATE ON users
    FOR EACH ROW EXECUTE procedure process_audit();
CREATE TRIGGER users_audit_del
    AFTER DELETE ON users
    FOR EACH ROW EXECUTE procedure process_audit();

CREATE TRIGGER schedule_task_check
    BEFORE INSERT ON schedule_tasks
    FOR EACH ROW EXECUTE procedure check_task_id();

   INSERT INTO users VALUES(1, 'a', 'a', '1000:52a2e5376fe9155814775f1e3231a526:191ade9da2dcbabfc870ba70263b7af6865b40d8e179d19e8ea504d257810c6e78a316d77f5bd8716a7fa54f39b1f082c773ca80b45526dd59c933522e341216', 'ADMIN');
   INSERT INTO users VALUES(2, 'r', 'r', '1000:12b64240b3c5da1f64daa0d26dbd7bfb:e314534adbb83fa0d605557a1d7394f6936b10efcfc89cae85260e69ad452241cbdd6d043ae51ecc92e8776b4aa369fa6afb028cac5254f9cc7a4e8eae0722c2', 'REGULAR');
   -- INSERT INTO users VALUES(2, 'a', 'b', 'c', 'd');
   --INSERT INTO tasks VALUES(1, 1, 'a', 'b', 1, 1);
   --INSERT INTO schedules VALUES(1, 1, 'title', 1, true);
   -- UPDATE users SET user_name = 'z';
   -- DELETE FROM users WHERE user_id = 2;
   --INSERT INTO schedule_tasks VALUES(1, 1);
   --INSERT INTO schedule_tasks VALUES(1, 1);
