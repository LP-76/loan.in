CREATE TABLE IF NOT EXISTS USER (
    `ID` INT(11) NOT NULL AUTO_INCREMENT,
    `FEEDBACK_ID` INT(11) DEFAULT NULL,
    `FIRST_NAME` VARCHAR(50) NOT NULL,
    `LAST_NAME` VARCHAR(50) NOT NULL,
    `EMAIL` VARCHAR(50) NOT NULL,
    `PHONE_NUMBER` VARCHAR(50) DEFAULT NULL,
    `PASSWORD` VARCHAR(50) NOT NULL,
    `CREATED_AT` TIMESTAMP,
    `UPDATED_AT` TIMESTAMP,
    PRIMARY KEY (`ID`)
);

CREATE TABLE IF NOT EXISTS USER_INVENTORY_ITEM (
   `ID` INT(11) NOT NULL AUTO_INCREMENT,
   `USER_ID` INT(11) DEFAULT NULL,
   `BOX_ID` INT(11) DEFAULT NULL,
   `ITEM_DESCRIPTION` VARCHAR(30),
   `STATUS` VARCHAR(50),
   `BOX_WIDTH` FLOAT,
   `BOX_HEIGHT` FLOAT,
   `BOX_LENGTH` FLOAT,
   `FRAGILITY` INT(11),
   `WEIGHT` DOUBLE,
   `IMAGE` BLOB,
   `CREATED_AT` TIMESTAMP,
   `UPDATED_AT` TIMESTAMP,
    `ROOM` VARCHAR(25),
    `ITEM_LIST` VARCHAR(50),
   PRIMARY KEY (`ID`),
   FOREIGN KEY (`USER_ID`) REFERENCES USER(`ID`)
);

CREATE TABLE IF NOT EXISTS USER_FEEDBACK (
   `ID` INT(11) NOT NULL AUTO_INCREMENT,
   `USER_ID` INT(11) DEFAULT NULL,
   `ACCOUNT_CREATION_COMMENT`	VARCHAR(255),
   `ACCOUNT_CREATION_RATING`	INT(11),
   `ITEM_INPUT_COMMENT` VARCHAR(255),
   `ITEM_INPUT_RATING`	 INT(11),
   `LOAD_PLAN_COMMENT`	 VARCHAR(255),
   `LOAD_PLAN_RATING` 	 INT(11),
   `EXPERT_TIPS_COMMENT` VARCHAR(255),
   `EXPERT_TIPS_RATING`  INT(11),
   `OVERALL_EXPERIENCE_COMMENT` VARCHAR(255),
   `OVERALL_EXPERIENCE_RATING` INT(11),
   `CREATED_AT` TIMESTAMP,
   `UPDATED_AT` TIMESTAMP,
   PRIMARY KEY (`ID`),
   FOREIGN KEY (`USER_ID`) REFERENCES USER(`ID`)
);

CREATE TABLE IF NOT EXISTS ROLE (
    `ID` INT (11) AUTO_INCREMENT,
    `DESCRIPTION` VARCHAR(255) NOT NULL,
    PRIMARY KEY (`ID`)
    );

CREATE TABLE IF NOT EXISTS USER_ROLE (
    `ID` INT (11) AUTO_INCREMENT,
    `ROLE_ID` INT (11) DEFAULT NULL,
    `USER_ID` INT(11) DEFAULT NULL,
    `CREATED_AT` TIMESTAMP,
    `UPDATED_AT` TIMESTAMP,
    PRIMARY KEY (`ID`),
    FOREIGN KEY (`ROLE_ID`) REFERENCES ROLE(`ID`),
    FOREIGN KEY (`USER_ID`) REFERENCES USER(`ID`)
    );

CREATE TABLE IF NOT EXISTS BOX_SIZE (
    `ID` INT (11) NOT NULL AUTO_INCREMENT,
    `DESCRIPTION` VARCHAR(255),
    `DIMENSIONS` VARCHAR(255),
    `CREATED_AT` TIMESTAMP,
    `UPDATED_AT` TIMESTAMP,
    PRIMARY KEY (`ID`)
    );

CREATE TABLE IF NOT EXISTS EXPERT_TIP (
    `ID` INT(11) NOT NULL AUTO_INCREMENT,
    `KEYWORD` VARCHAR(50),
    `CONTENT` VARCHAR(255),
    `TITLE` VARCHAR(255),
    `VIDEO` VARCHAR(255),
    `IMAGE` BLOB,
    `COMMENTS` VARCHAR(255),
    `CREATED_AT` TIMESTAMP,
    `UPDATED_AT` TIMESTAMP,
    PRIMARY KEY (`ID`)
    );

CREATE TABLE IF NOT EXISTS EXPERT_TIP_UNMATCHED_KEYWORDS (
    `ID` INT(11) NOT NULL AUTO_INCREMENT,
    `KEYWORD` VARCHAR(50),
    `CREATED_AT` TIMESTAMP,
    `UPDATED_AT` TIMESTAMP,
    PRIMARY KEY (`ID`)
    );

CREATE TABLE IF NOT EXISTS LOAD_PLAN_BOX (
    `ID` INT(11) NOT NULL,
    `X_OFFSET` FLOAT,
    `Y_OFFSET` FLOAT,
    `Z_OFFSET` FLOAT,
    `BOX_STEP` INT,
    `LOAD_NUMBER` INT,
    PRIMARY KEY (`ID`),
    FOREIGN KEY (`ID`) REFERENCES USER_INVENTORY_ITEM(`ID`)
    );

