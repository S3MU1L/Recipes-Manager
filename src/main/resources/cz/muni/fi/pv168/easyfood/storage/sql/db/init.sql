--
-- Department table definition
--
CREATE TABLE IF NOT EXISTS "Department"
(
    `id`        BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    `guid`      VARCHAR     NOT NULL UNIQUE,
    `number`    VARCHAR(10) NOT NULL UNIQUE,
    `name`      VARCHAR(50) NOT NULL,
    `createdAt` TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP
);

--
-- Employee table definition
--
CREATE TABLE IF NOT EXISTS "Employee"
(
    `id`           BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    `guid`         VARCHAR      NOT NULL UNIQUE,
    `firstName`    VARCHAR(150) NOT NULL,
    `lastName`     VARCHAR(150) NOT NULL,
    `birthDate`    DATE         NOT NULL,
    `gender`       ENUM('MALE', 'FEMALE') NOT NULL,
    `departmentId` BIGINT REFERENCES "Department"(`id`),
    `createdAt`    TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);
