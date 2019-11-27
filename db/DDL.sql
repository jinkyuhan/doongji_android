/*
DROP TABLE DOONG_JI.belongs_tos;
DROP TABLE DOONG_JI.message_boxes;
DROP TABLE DOONG_JI.members;
DROP TABLE DOONG_JI.groups;
DROP TABLE DOONG_JI.locations;
*/


CREATE TABLE DOONG_JI.members(
Mem_id INT(6) NOT NULL AUTO_INCREMENT,
User_id VARCHAR(15) NOT NULL,
User_pw VARCHAR(15) NOT NULL,
User_name VARCHAR(20) NOT NULL,
CONSTRAINT MEM_PK PRIMARY KEY(Mem_id),
CONSTRAINT MEM_UK UNIQUE(User_id)
);

CREATE TABLE DOONG_JI.locations(
Loc_id INT(6) NOT NULL AUTO_INCREMENT,
X_pos VARCHAR(30) NOT NULL,
Y_pos VARCHAR(30) NOT NULL,
CONSTRAINT LOC_PK PRIMARY KEY(Loc_id),
CONSTRAINT LOC_UK UNIQUE(X_pos,Y_pos)
);

CREATE TABLE DOONG_JI.groups(
Grp_id INT(6) NOT NULL AUTO_INCREMENT,
Grp_name VARCHAR(20) NOT NULL,
Grp_loc INT(6) NOT NULL,
Grp_radius INT(5) NULL DEFAULT 500,
CONSTRAINT GRP_PK PRIMARY KEY(Grp_id),
CONSTRAINT GRP_LOC_FK FOREIGN KEY(Grp_loc) REFERENCES DOONG_JI.locations(Loc_id)
);

CREATE TABLE DOONG_JI.belongs_tos(
Mem_id INT(6) NOT NULL,
Grp_id INT(6) NOT NULL,
CONSTRAINT belongs_tos_members FOREIGN KEY(Mem_id) REFERENCES DOONG_JI.members(Mem_id),
CONSTRAINT belongs_tos_groups FOREIGN KEY(Grp_id) REFERENCES DOONG_JI.groups(Grp_id),
CONSTRAINT belongs_tos_PK PRIMARY KEY(Mem_id,Grp_id)
);

CREATE TABLE DOONG_JI.message_boxes(
Msg_id INT(6) NOT NULL AUTO_INCREMENT,
Msg_body VARCHAR(100),
Msg_sender INT(6) NOT NULL,
Msg_receiver INT(6) NOT NULL,
CONSTRAINT MSG_BOX_PK PRIMARY KEY(Msg_id),
CONSTRAINT MSG_BOX_SEND_FK FOREIGN KEY(Msg_sender) REFERENCES DOONG_JI.members(Mem_id),
CONSTRAINT MSG_BOX_RECV_FK FOREIGN KEY(Msg_receiver) REFERENCES DOONG_JI.members(Mem_id)
);
commit;


/*
DROP TABLE DOONG_JI.LIVES_IN;
CREATE TABLE DOONG_JI.LIVES_IN(
Mem_id INT(6) NOT NULL,
Loc_id INT(6) NOT NULL,
CONSTRAINT LIVES_IN_MEM FOREIGN KEY(Mem_id) REFERENCES DOONG_JI.members(Mem_id),
CONSTRAINT LIVES_IN_LOC FOREIGN KEY(Loc_id) REFERENCES DOONG_JI.locations(Loc_id),
CONSTRAINT LIVES_IN_PK PRIMARY KEY(Mem_id,Loc_id)
);
*/