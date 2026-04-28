-- Run this in MySQL Workbench or command line if database doesn't auto-create.
-- Spring Boot JPA (ddl-auto=update) will create/update tables automatically on startup.

CREATE DATABASE IF NOT EXISTS heritage_db;
USE heritage_db;

-- ── Core tables ──────────────────────────────────────────────────────────────

CREATE TABLE IF NOT EXISTS users (
    id       BIGINT AUTO_INCREMENT PRIMARY KEY,
    name     VARCHAR(255) NOT NULL,
    email    VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role     VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS monuments (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    city        VARCHAR(255),
    state       VARCHAR(255),
    era         VARCHAR(255),
    year        VARCHAR(100),
    category    VARCHAR(100),
    description VARCHAR(1000),
    image       VARCHAR(500),
    thumbnail   VARCHAR(500),
    facts       VARCHAR(500),
    tour_points VARCHAR(500)
);

-- ── Activity tables (all FK to both users and monuments) ─────────────────────

-- User ──(1:N)──> PageVisit
CREATE TABLE IF NOT EXISTS page_visits (
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id      BIGINT       NOT NULL,
    page         VARCHAR(100) NOT NULL,
    visit_count  INT          DEFAULT 1,
    last_visited DATETIME,
    CONSTRAINT fk_pv_user    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT uq_pv_user_page UNIQUE (user_id, page)
);

-- User ──(1:N)──> TourRegistration <──(N:1)── Monument
CREATE TABLE IF NOT EXISTS tour_registrations (
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id       BIGINT NOT NULL,
    monument_id   BIGINT NOT NULL,
    registered_at DATETIME,
    CONSTRAINT fk_tr_user     FOREIGN KEY (user_id)     REFERENCES users(id)     ON DELETE CASCADE,
    CONSTRAINT fk_tr_monument FOREIGN KEY (monument_id) REFERENCES monuments(id) ON DELETE CASCADE,
    CONSTRAINT uq_tr_user_monument UNIQUE (user_id, monument_id)
);

-- User ──(1:N)──> MonumentVisit <──(N:1)── Monument
CREATE TABLE IF NOT EXISTS monument_visits (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id     BIGINT NOT NULL,
    monument_id BIGINT NOT NULL,
    visited_at  DATETIME,
    CONSTRAINT fk_mv_user     FOREIGN KEY (user_id)     REFERENCES users(id)     ON DELETE CASCADE,
    CONSTRAINT fk_mv_monument FOREIGN KEY (monument_id) REFERENCES monuments(id) ON DELETE CASCADE,
    CONSTRAINT uq_mv_user_monument UNIQUE (user_id, monument_id)
);

-- User ──(1:N)──> Discussion <──(N:1)── Monument
CREATE TABLE IF NOT EXISTS discussions (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id     BIGINT        NOT NULL,
    monument_id BIGINT        NOT NULL,
    text        VARCHAR(2000) NOT NULL,
    timestamp   DATETIME,
    CONSTRAINT fk_disc_user     FOREIGN KEY (user_id)     REFERENCES users(id)     ON DELETE CASCADE,
    CONSTRAINT fk_disc_monument FOREIGN KEY (monument_id) REFERENCES monuments(id) ON DELETE CASCADE
);
