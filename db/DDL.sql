/*
DROP TABLE belongs_tos;
DROP TABLE groups;
DROP TABLE message_boxes;
DROP TABLE members;
*/


/*
CREATE TABLE members(
Mem_id INT(6) NOT NULL AUTO_INCREMENT,
token VARCHAR(15) NOT NULL,
User_pw VARCHAR(15) NOT NULL,
User_name VARCHAR(20) NOT NULL,
CONSTRAINT MEM_PK PRIMARY KEY(Mem_id),
CONSTRAINT MEM_UK UNIQUE(token)
);

CREATE TABLE locations(
Loc_id INT(6) NOT NULL AUTO_INCREMENT,
X_pos VARCHAR(30) NOT NULL,
Y_pos VARCHAR(30) NOT NULL,
CONSTRAINT LOC_PK PRIMARY KEY(Loc_id),
CONSTRAINT LOC_UK UNIQUE(X_pos,Y_pos)
);

CREATE TABLE groups(
Grp_id INT(6) NOT NULL AUTO_INCREMENT,
Grp_name VARCHAR(20) NOT NULL,
Grp_loc INT(6) NOT NULL,
Grp_radius INT(5) NULL DEFAULT 500,
CONSTRAINT GRP_PK PRIMARY KEY(Grp_id),
CONSTRAINT GRP_LOC_FK FOREIGN KEY(Grp_loc) REFERENCES locations(Loc_id)
);

CREATE TABLE belongs_tos(
Mem_id INT(6) NOT NULL,
Grp_id INT(6) NOT NULL,
CONSTRAINT belongs_tos_members FOREIGN KEY(Mem_id) REFERENCES members(Mem_id),
CONSTRAINT belongs_tos_groups FOREIGN KEY(Grp_id) REFERENCES groups(Grp_id),
CONSTRAINT belongs_tos_PK PRIMARY KEY(Mem_id,Grp_id)
);

CREATE TABLE message_boxes(
Msg_id INT(6) NOT NULL AUTO_INCREMENT,
Msg_body VARCHAR(100),
Msg_sender INT(6) NOT NULL,
Msg_receiver INT(6) NOT NULL,
CONSTRAINT MSG_BOX_PK PRIMARY KEY(Msg_id),
CONSTRAINT MSG_BOX_SEND_FK FOREIGN KEY(Msg_sender) REFERENCES members(Mem_id),
CONSTRAINT MSG_BOX_RECV_FK FOREIGN KEY(Msg_receiver) REFERENCES members(Mem_id)
);
*/



/*
DROP TABLE LIVES_IN;
CREATE TABLE LIVES_IN(
Mem_id INT(6) NOT NULL,
Loc_id INT(6) NOT NULL,
CONSTRAINT LIVES_IN_MEM FOREIGN KEY(Mem_id) REFERENCES members(Mem_id),
CONSTRAINT LIVES_IN_LOC FOREIGN KEY(Loc_id) REFERENCES locations(Loc_id),
CONSTRAINT LIVES_IN_PK PRIMARY KEY(Mem_id,Loc_id)
);
*/

-- belongs_tos trigger update 이후에 updatedAt 값 변화
DELIMITER $$
CREATE TRIGGER belongs_tos_BEFORE_UPDATE BEFORE UPDATE ON belongs_tos FOR EACH ROW
BEGIN
SET NEW.updatedAt=CURRENT_TIMESTAMP;
END
$$
DELIMITER ;

--  belongs_tos trigger insert 이후에 createdAt 값 변화 
DELIMITER $$
CREATE TRIGGER belongs_tos_BEFORE_INSERT BEFORE INSERT ON belongs_tos FOR EACH ROW
BEGIN
SET NEW.createdAt=CURRENT_TIMESTAMP,NEW.updatedAt=CURRENT_TIMESTAMP;
END
$$
DELIMITER ;

-- groups trigger update 이후에 updatedAt 값 변화
DELIMITER $$
CREATE TRIGGER groups_BEFORE_UPDATE BEFORE UPDATE ON groups FOR EACH ROW
BEGIN
SET NEW.updatedAt=CURRENT_TIMESTAMP;
END
$$
DELIMITER ;

--  groups trigger insert 이후에 createdAt 값 변화 
DELIMITER $$
CREATE TRIGGER groups_BEFORE_INSERT BEFORE INSERT ON groups FOR EACH ROW
BEGIN
SET NEW.createdAt=CURRENT_TIMESTAMP,NEW.updatedAt=CURRENT_TIMESTAMP;
END
$$
DELIMITER ;

-- members trigger update 이후에 updatedAt 값 변화 
DELIMITER $$
CREATE TRIGGER members_BEFORE_UPDATE BEFORE UPDATE ON members FOR EACH ROW
BEGIN
SET NEW.updatedAt=CURRENT_TIMESTAMP;
END
$$
DELIMITER ;


--  members trigger insert 이후에 createdAt 값 변화 
DELIMITER $$
CREATE TRIGGER members_BEFORE_INSERT BEFORE INSERT ON members FOR EACH ROW
BEGIN
SET NEW.createdAt=CURRENT_TIMESTAMP,NEW.updatedAt=CURRENT_TIMESTAMP;
END
$$
DELIMITER ;

-- message_boxes trigger update 이후에 updatedAt 값 변화
DELIMITER $$
CREATE TRIGGER message_boxes_BEFORE_UPDATE BEFORE UPDATE ON message_boxes FOR EACH ROW
BEGIN
SET NEW.updatedAt=CURRENT_TIMESTAMP;
END
$$
DELIMITER ;
--  message_boxes trigger insert 이후에 createdAt 값 변화 
DELIMITER $$
CREATE TRIGGER message_boxes_BEFORE_INSERT BEFORE INSERT ON message_boxes FOR EACH ROW
BEGIN
SET NEW.createdAt=CURRENT_TIMESTAMP,NEW.updatedAt=CURRENT_TIMESTAMP;
END
$$
DELIMITER ;

DELIMITER $$
CREATE TRIGGER belongs_tos_AFTER AFTER DELETE ON belongs_tos FOR EACH ROW
BEGIN
DECLARE
count int(3);

SET @count :=(
SELECT COUNT(*)
FROM groups g, belongs_tos b, members m
where b.grp_id=g.grp_id
and b.token=m.token
GROUP BY g.GRP_ID
HAVING g.GRP_ID=OLD.grp_id);
if @count IS NULL
THEN 
DELETE FROM groups
WHERE GRP_ID=OLD.grp_id;
END IF;
END 
$$
DELIMITER ;

drop view Belongs_View;
create view Belongs_View as(
select m.token,m.user_name,g.grp_id, g.grp_name,g.grp_xpos,g.grp_ypos,g.grp_radius
from groups g, belongs_tos b, members m
where b.grp_id=g.grp_id
and b.token=m.token);

create view Message_User_View as 
					select * 
                    from
						(select user_name as sender_name , token as sender_token, msg_body
							from message_boxes, members
							where msg_sender=token) as a
                           NATURAL JOIN
							(select user_name as receiver_name,token as receiver_token, msg_body
								from message_boxes,members
								where msg_receiver=token)as b;

commit;