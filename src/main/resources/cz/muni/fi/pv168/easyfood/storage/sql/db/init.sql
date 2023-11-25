CREATE TABLE IF NOT EXISTS Category
(
    id    BIGINT PRIMARY KEY AUTO_INCREMENT,
    guid  VARCHAR(255) NOT NULL,
    name  VARCHAR(255) NOT NULL,
    color VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS Unit
(
    id              BIGINT PRIMARY KEY AUTO_INCREMENT,
    guid            VARCHAR(255) NOT NULL,
    baseUnitOrdinal INT          NOT NULL,
    name            VARCHAR(255) NOT NULL,
    abbreviation    VARCHAR(255) NOT NULL,
    conversion      DOUBLE       NOT NULL
);

CREATE TABLE IF NOT EXISTS Ingredient
(
    id       BIGINT PRIMARY KEY AUTO_INCREMENT,
    guid     VARCHAR(255) NOT NULL,
    unitId   BIGINT,
    name     VARCHAR(255) NOT NULL,
    calories DOUBLE       NOT NULL,
    FOREIGN KEY (unitId) REFERENCES Unit (id)
);

CREATE TABLE IF NOT EXISTS Recipe
(
    id              BIGINT PRIMARY KEY AUTO_INCREMENT,
    guid            VARCHAR(255) NOT NULL,
    name            VARCHAR(255) NOT NULL,
    description     TEXT         NOT NULL,
    preparationTime INT          NOT NULL,
    portions        INT          NOT NULL,
    categoryId      BIGINT,
    FOREIGN KEY (categoryId) REFERENCES Category (id)
);

CREATE TABLE IF NOT EXISTS RecipeIngredientWithAmount
(
    id           BIGINT PRIMARY KEY AUTO_INCREMENT,
    guid         VARCHAR(255) NOT NULL,
    recipeId     VARCHAR(255) NOT NULL,
    ingredientId BIGINT,
    amount       DOUBLE       NOT NULL,
    FOREIGN KEY (ingredientId) REFERENCES Ingredient (id)
);