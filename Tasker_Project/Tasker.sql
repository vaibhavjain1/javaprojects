CREATE DATABASE TASKER_DB; 

GRANT USAGE ON *.* TO taskeradmin@'localhost';

drop user taskeradmin@localhost;

flush privileges;

CREATE USER taskeradmin@'localhost' IDENTIFIED BY 'taskeradmin';

GRANT ALL PRIVILEGES ON *.* TO taskeradmin@'localhost';

FLUSH PRIVILEGES;

CREATE TABLE TASKER_DB.User_Details (
  User_Id INT NOT NULL AUTO_INCREMENT,
  User_Name VARCHAR(50),
  Password VARCHAR(50),
  PRIMARY KEY  (User_Id),
  UNIQUE (User_Name)
);

CREATE TABLE TASKER_DB.Task_Details (
  Task_Id INT NOT NULL AUTO_INCREMENT,
  Task_Name VARCHAR(50),
  Task_Description VARCHAR(200),
  Start_Time TIMESTAMP,
  End_Time TIMESTAMP,
  Task_Status VARCHAR(20),
  PRIMARY KEY  (Task_Id)
);

CREATE TABLE TASKER_DB.User_Task (
  User_Id INT NOT NULL,
  Task_Id INT NOT NULL,
  Comment VARCHAR(200),
  PRIMARY KEY  (User_Id,Task_Id),
  FOREIGN KEY User_Details_fk1(User_Id) REFERENCES User_Details(User_Id),
  FOREIGN KEY Task_Details_fk1(User_Id) REFERENCES Task_Details(Task_Id)
);

INSERT INTO TASKER_DB.User_Details(User_Name,Password) Values('admin','admin');