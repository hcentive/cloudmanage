-- AWS Metadata

CREATE TABLE IF NOT EXISTS INSTANCE_MASTER (
	ID INT NOT NULL AUTO_INCREMENT PRIMARY KEY, 
	AWS_EC2_ID varchar(40) UNIQUE,
	NAME varchar(40),
	PROJECT varchar(40),
	COST_CENTER varchar(40),
	STACK varchar(40),
	OWNER varchar(60),
	PRIVATE_IP varchar(40),
	PUBLIC_IP varchar(40),
	INSTANCE_TYPE varchar(40),
	LAUNCH_TIME timestamp
);

CREATE TABLE IF NOT EXISTS AWS_BILL_INFO (
	META_INFO_ID INT NOT NULL AUTO_INCREMENT PRIMARY KEY, 
	INSTANCE_ID INT,
	SNAPSHOT_AT timestamp,
	BILL_TOTAL decimal(12,6),
	UNIQUE KEY AWS_BILL_KEY (INSTANCE_ID,SNAPSHOT_AT),
	CONSTRAINT INST_BILL_FK FOREIGN KEY (INSTANCE_ID) REFERENCES INSTANCE_MASTER (ID)
);

CREATE TABLE IF NOT EXISTS AWS_CPU_INFO (
	META_INFO_ID INT NOT NULL AUTO_INCREMENT PRIMARY KEY, 
	INSTANCE_ID INT,
	AVG_CPU_HOURLY varchar(10000),
	SNAPSHOT_AT timestamp,
	UNIQUE KEY AWS_CPU_KEY (INSTANCE_ID,SNAPSHOT_AT),
	CONSTRAINT INST_CPU_FK FOREIGN KEY (INSTANCE_ID) REFERENCES INSTANCE_MASTER (ID)
);

