CREATE TABLE replay (
    replay_id SERIAL CONSTRAINT replay_pk PRIMARY KEY,
    map_name TEXT NOT NULL
);

CREATE TABLE dungeon_run (
    dungeon_run_id SERIAL CONSTRAINT dungeon_run_pk PRIMARY KEY,
    replay_id BIGINT REFERENCES replay(replay_id) ON DELETE CASCADE
);

CREATE TABLE replay_player (
    replay_player_id SERIAL CONSTRAINT replay_player_pk PRIMARY KEY,
    dungeon_run_id BIGINT REFERENCES dungeon_run(dungeon_run_id) ON DELETE CASCADE,
    name TEXT NOT NULL
);

CREATE TABLE replay_action (
    replay_action_id SERIAL CONSTRAINT replay_action_pk PRIMARY KEY,
    replay_player_id BIGINT REFERENCES replay_player(replay_player_id) ON DELETE CASCADE,
    action TEXT NOT NULL,
    pos_x NUMERIC NOT NULL,
    pos_y NUMERIC NOT NULL,
    pos_z NUMERIC NOT NULL
);
