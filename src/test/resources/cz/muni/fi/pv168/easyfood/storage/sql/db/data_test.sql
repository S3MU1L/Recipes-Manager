--
-- Testing data - used in the tests
--

-- Testing departments
INSERT INTO "Department" ("guid", "number", "name")
VALUES ('d1', '007', 'IT'),
    ('d2', '666', 'Sales'),
    ('d3', '112', 'HR')
;

-- Testing employees

INSERT INTO "Employee" ("guid",
                      "firstName",
                      "lastName",
                      "birthDate",
                      "gender",
                      "departmentId")
VALUES ('e1', 'Jonas', 'Kahnwald', '1999-12-28', 'MALE', 1),
       ('e2', 'Martha', 'Nielsen', '2000-12-10', 'FEMALE', 1),
       ('e3', 'Hanno', 'Tauber', '1921-07-27', 'MALE', 2),
       ('e4', 'Charlotte', 'Doppler', '2000-02-05', 'FEMALE', 2)
;
