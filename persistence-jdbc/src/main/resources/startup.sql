CREATE TABLE PETS (ID INT NOT NULL ,
                   NAME varchar(40),
                   TYPENAME varchar(40),
                   CAGE_FK INT);

CREATE TABLE CAGES (ID INT NOT NULL,
                   DESCRIPTION varchar(40),
                   CAPACITY int,
                   REMAINING int);

                   
INSERT INTO PETS (ID, NAME, TYPENAME, CAGE_FK) VALUES (1, 'Sisi','cat', 1) ;
INSERT INTO PETS (ID, NAME, TYPENAME, CAGE_FK) VALUES (2, 'Rex','dog', 1) ;
INSERT INTO PETS (ID, NAME, TYPENAME, CAGE_FK) VALUES (3, 'Tapi','dog', 1) ;

INSERT INTO CAGES(ID, DESCRIPTION, CAPACITY, REMAINING) VALUES (1, 'Big housefor dogs and cats', 10, 7);

