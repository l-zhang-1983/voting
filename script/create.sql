CREATE TABLE config_province (
     province_code INTEGER NOT NULL PRIMARY KEY,
     province_name TEXT(32) NOT NULL
);

CREATE TABLE config_candidate_info (
   candidate_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
   province_code INTEGER NOT NULL,
   candidate_name    TEXT(16) NOT NULL,
   candidate_type    INTEGER DEFAULT (0) NOT NULL,
   is_previous_staff INTEGER DEFAULT (0) NOT NULL
);

CREATE TABLE ballot_info (
     ballot_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
     serial_no INTEGER NOT NULL,
     supervisor_count INTEGER DEFAULT (0) NOT NULL,
     director_count INTEGER DEFAULT (0) NOT NULL,
     supervisor_exceeded INTEGER DEFAULT (0) NOT NULL,
     director_exceed INTEGER DEFAULT (0),
     created_at TEXT(32) DEFAULT ('202501010000000000') NOT NULL,
     updated_at TEXT(32) DEFAULT ('202501010000000000') NOT NULL
);

CREATE TABLE ballot_details (
    detail_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    ballot_id INTEGER NOT NULL,
    candidate_id INTEGER NOT NULL,
    candidate_type INTEGER NOT NULL,
    created_at TEXT(32) DEFAULT ('20250101000000000') NOT NULL,
    updated_at TEXT(32) DEFAULT ('20250101000000000') NOT NULL
);
