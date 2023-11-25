CREATE TABLE Category (
                          id BIGINT PRIMARY KEY AUTO_INCREMENT,
                          guid VARCHAR(255) NOT NULL,
                          name VARCHAR(255) NOT NULL,
                          color VARCHAR(255) NOT NULL
);

CREATE TABLE Unit (
                      id BIGINT PRIMARY KEY AUTO_INCREMENT,
                      guid VARCHAR(255) NOT NULL,
                      baseUnitOrdinal INT NOT NULL,
                      name VARCHAR(255) NOT NULL,
                      abbreviation VARCHAR(255) NOT NULL,
                      conversion DOUBLE NOT NULL
);

CREATE TABLE Ingredient (
                            id BIGINT PRIMARY KEY AUTO_INCREMENT,
                            guid VARCHAR(255) NOT NULL,
                            unitId BIGINT,
                            name VARCHAR(255) NOT NULL,
                            calories DOUBLE NOT NULL,
                            FOREIGN KEY (unitId) REFERENCES Unit(id)
);

CREATE TABLE IngredientWithAmount (
                                      id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                      guid VARCHAR(255) NOT NULL,
                                      ingredientId BIGINT,
                                      amount DOUBLE NOT NULL,
                                      FOREIGN KEY (ingredientId) REFERENCES Ingredient(id)
);

CREATE TABLE Recipe (
                        id BIGINT PRIMARY KEY AUTO_INCREMENT,
                        guid VARCHAR(255) NOT NULL,
                        name VARCHAR(255) NOT NULL,
                        description TEXT NOT NULL,
                        preparationTime INT NOT NULL,
                        portions INT NOT NULL,
                        categoryId BIGINT,
                        FOREIGN KEY (categoryId) REFERENCES Category(id)
);

CREATE TABLE RecipeIngredientWithAmount (
                                            recipeId BIGINT,
                                            ingredientWithAmountId BIGINT,
                                            PRIMARY KEY (recipeId, ingredientWithAmountId),
                                            FOREIGN KEY (recipeId) REFERENCES Recipe(id),
                                            FOREIGN KEY (ingredientWithAmountId) REFERENCES IngredientWithAmount(id)
);
