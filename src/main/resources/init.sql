drop table IF EXISTS schedule_tasks CASCADE;
drop table IF EXISTS tasks CASCADE;
drop table IF EXISTS schedules CASCADE;
drop table IF EXISTS users CASCADE;
drop table IF EXISTS all_audit CASCADE;
drop sequence IF EXISTS counter CASCADE;

create TABLE users(
	user_id SERIAL UNIQUE PRIMARY KEY,
	user_name varchar(100) NOT NULL,
	user_email varchar(254) NOT NULL,
	user_password text NOT NULL,
	user_role varchar(10) NOT NULL,
	CONSTRAINT user_name_not_empty CHECK (user_name <> ''),
	CONSTRAINT user_email_not_empty CHECK (user_email <> ''),
	CONSTRAINT user_password_not_empty CHECK (user_password <> ''),
	CONSTRAINT user_role_not_empty CHECK (user_role <> '')
);

create TABLE schedules(
	schedule_id SERIAL UNIQUE PRIMARY KEY,
	user_id integer,
	schedule_title varchar(20) NOT NULL,
	schedule_duration integer NOT NULL,
	schedule_visibility boolean DEFAULT false,
	FOREIGN KEY(user_id) REFERENCES users(user_id),
	CONSTRAINT schedule_duration_max CHECK (schedule_duration > 0 AND schedule_duration <= 7)
);

create TABLE tasks(
	task_id SERIAL UNIQUE PRIMARY KEY,
	user_id integer,
	task_title varchar(20) NOT NULL,
	task_content varchar(20) NOT NULL,
	task_start integer NOT NULL,
	task_end integer NOT NULL,
	FOREIGN KEY(user_id) REFERENCES users(user_id),
	CONSTRAINT task_start_max CHECK (task_start >= 0 AND task_start <= 24),
	CONSTRAINT task_end_max CHECK (task_end >= 0 AND task_end <= 24)
);

create TABLE schedule_tasks(
	task_id integer,
	schedule_id integer,
	column_number integer,
	FOREIGN KEY(task_id) REFERENCES tasks(task_id),
	FOREIGN KEY(schedule_id) REFERENCES schedules(schedule_id)
);

create TABLE all_audit(
	event_counter integer UNIQUE PRIMARY KEY,
	event_name varchar(100),
	table_name varchar(100),
	user_id integer,
	event_date timestamp
);

create sequence counter AS integer INCREMENT BY 1 START 1;

create or replace function process_audit() RETURNS trigger AS '
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

create or replace function check_task_id() RETURNS trigger AS '
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

create trigger tasks_audit_ins
    after insert on tasks
    for each row EXECUTE procedure process_audit();
create trigger tasks_audit_upd
    after update on tasks
    for each row EXECUTE procedure process_audit();
create trigger tasks_audit_del
    after delete on tasks
    for each row EXECUTE procedure process_audit();

create trigger schedules_audit_ins
    after insert on schedules
    for each row EXECUTE procedure process_audit();
create trigger schedules_audit_upd
    after update on schedules
    for each row EXECUTE procedure process_audit();
create trigger schedules_audit_del
    after delete on schedules
    for each row EXECUTE procedure process_audit();

create trigger users_audit_ins
    after insert on users
    for each row EXECUTE procedure process_audit();
create trigger users_audit_upd
    after update on users
    for each row EXECUTE procedure process_audit();
create trigger users_audit_del
    after delete on users
    for each row EXECUTE procedure process_audit();

create trigger schedule_task_check
    before insert on schedule_tasks
    for each row EXECUTE procedure check_task_id();

create or replace function check_schedule_coloumn() RETURNS trigger AS '
    BEGIN
        IF (TG_OP = ''INSERT'') THEN
            DECLARE
                rows integer;
            BEGIN
				SELECT schedule_duration INTO rows FROM schedules WHERE schedule_id = NEW.schedule_id;
                IF rows  < NEW.column_number THEN
                	RAISE EXCEPTION ''Task could not be added to schedule, because the row number is invalid '';
                END IF;
			END;
        END IF;
        RETURN NEW;
    END;
' LANGUAGE plpgsql;

create trigger check_schedule_coloumns
    before insert on schedule_tasks
    for each row EXECUTE procedure check_schedule_coloumn();

create or replace function check_schedule_coloumn() RETURNS trigger AS '
    BEGIN
        IF (TG_OP = ''INSERT'') THEN
            DECLARE
                task_start_var integer;
				task_end_var integer;
				task_start integer;
				task_end integer;
				col_num integer;
            BEGIN
				SELECT tasks.task_start, tasks.task_end INTO task_start, task_end FROM schedule_tasks JOIN tasks ON tasks.task_id = NEW.task_id;
                FOR col_num, task_start_var, task_end_var IN SELECT column_number, tasks.task_start, tasks.task_end FROM schedule_tasks JOIN tasks ON schedule_tasks.task_id = tasks.task_id WHERE schedule_id = NEW.schedule_id LOOP
	                IF task_start_var = task_start OR task_end_var = task_end AND col_num = NEW.column_number THEN
	                	RAISE EXCEPTION ''There is already a task in the choosen timeframe and day'';
	                END IF;

	                IF task_start_var < task_start AND task_start < task_end_var AND col_num = NEW.column_number THEN
	                	RAISE EXCEPTION ''There is already a task in the choosen timeframe and day'';
	                END IF;

					IF task_end_var > task_end AND task_end > task_start_var AND col_num = NEW.column_number THEN
	                	RAISE EXCEPTION ''There is already a task in the choosen timeframe and day'';
	                END IF;

                END LOOP;
			END;
        END IF;
        RETURN NEW;
    END;
' LANGUAGE plpgsql;


create trigger check_task_duplicate_inschedules
    before insert on schedule_tasks
    for each row EXECUTE procedure check_schedule_coloumn();

    -- users
    INSERT INTO users(user_name, user_email, user_password, user_role) VALUES('a', 'a', '1000:52a2e5376fe9155814775f1e3231a526:191ade9da2dcbabfc870ba70263b7af6865b40d8e179d19e8ea504d257810c6e78a316d77f5bd8716a7fa54f39b1f082c773ca80b45526dd59c933522e341216', 'ADMIN');
    INSERT INTO users(user_name, user_email, user_password, user_role) VALUES('r', 'r', '1000:12b64240b3c5da1f64daa0d26dbd7bfb:e314534adbb83fa0d605557a1d7394f6936b10efcfc89cae85260e69ad452241cbdd6d043ae51ecc92e8776b4aa369fa6afb028cac5254f9cc7a4e8eae0722c2', 'REGULAR');
    INSERT INTO users(user_name, user_email, user_password, user_role)  VALUES('peti', 'peti', '1000:f074ea92965ce178a8b1f5268f8fcdf4:4c1ee44787c9bf719299beadc07d4f09cc0f473edccafeb23d9938b53d9134e670eddbf74db2948bfdd0b47693e13279b1d42b4d13726bd74f1d08f2424b1dfb', 'REGULAR');
    INSERT INTO users(user_name, user_email, user_password, user_role)  VALUES('andi', 'andi', '1000:cafa85fbe65927fb7cf235fbb5a3787e:5311a5ec8a594736709abe582b9d51b05cf2c02798dbb11618b723236821f227047bafadbdcf6eb9809d6791baf34e3542af0c764d84ebd3aafc5b2bd9cb52f7', 'REGULAR');
    INSERT INTO users(user_name, user_email, user_password, user_role)  VALUES('berta', 'berta', '1000:6092951cee335fec6f8e436ba995476b:c98729fb8912dc85e8d2233b0cb3b3856a8cfa611991716ff8236b90a148b57def3b233ba65698e8b8d719e366659e89e91d9fb9a0138f415eef3a8713dc89e0', 'ADMIN');

   -- schedules
    INSERT INTO schedules(user_id, schedule_title, schedule_duration) VALUES (1, 'schedule1', 3);
    INSERT INTO schedules(user_id, schedule_title, schedule_duration) VALUES (1, 'schedule2', 4);
    INSERT INTO schedules(user_id, schedule_title, schedule_duration) VALUES (2, 'schedule3', 4);
    INSERT INTO schedules(user_id, schedule_title, schedule_duration) VALUES (3, 'schedule4', 5);
    INSERT INTO schedules(user_id, schedule_title, schedule_duration) VALUES (4, 'schedule5', 6);
    INSERT INTO schedules(user_id, schedule_title, schedule_duration) VALUES (5, 'schedule6', 2);

    -- tasks
    INSERT INTO tasks(user_id, task_title, task_content, task_start, task_end) VALUES (1, 'cleaning', 'cleaning the kitchen', 3, 5);
    INSERT INTO tasks(user_id, task_title, task_content, task_start, task_end) VALUES (1, 'jogging', 'morning jogging', 6, 7);
    INSERT INTO tasks(user_id, task_title, task_content, task_start, task_end) VALUES (2, 'cleaning', 'cleaning the car', 8, 9);
    INSERT INTO tasks(user_id, task_title, task_content, task_start, task_end) VALUES (3, 'watering plants', 'watering plants', 3, 4);
    INSERT INTO tasks(user_id, task_title, task_content, task_start, task_end) VALUES (3, 'going out', 'going to concert', 16, 21);
    INSERT INTO tasks(user_id, task_title, task_content, task_start, task_end) VALUES (4, 'going out', 'shopping', 9, 10);
    INSERT INTO tasks(user_id, task_title, task_content, task_start, task_end) VALUES (5, 'jogging', 'jogging to the park', 6, 8);
    INSERT INTO tasks(user_id, task_title, task_content, task_start, task_end) VALUES (5, 'cooking', 'cooking dinner', 11, 12);

	INSERT INTO tasks(user_id, task_title, task_content, task_start, task_end) VALUES (1, 'watching movie', 'watching avengers', 1, 4);
    INSERT INTO tasks(user_id, task_title, task_content, task_start, task_end) VALUES (1, 'walking the dog', 'walking the dog', 16, 18);
    INSERT INTO tasks(user_id, task_title, task_content, task_start, task_end) VALUES (1, 'cinema', 'going to the cinema', 19, 23);
    INSERT INTO tasks(user_id, task_title, task_content, task_start, task_end) VALUES (1, 'gym', 'going to the gym', 14, 16);
    INSERT INTO tasks(user_id, task_title, task_content, task_start, task_end) VALUES (1, 'studying', 'studying stuff', 12, 14);
    INSERT INTO tasks(user_id, task_title, task_content, task_start, task_end) VALUES (1, 'dentist', 'going to the dentist', 10, 12);
    INSERT INTO tasks(user_id, task_title, task_content, task_start, task_end) VALUES (1, 'cooking', 'cooking lunch', 11, 12);


    -- schedule_tasks
    INSERT INTO schedule_tasks(schedule_id, task_id, column_number) VALUES
        (1, 1, 1),
        (2, 2, 3),
        (3, 3, 2),
        (4, 4, 4),
        (4, 5, 3),
        (6, 7, 1),
        (6, 8, 2);
