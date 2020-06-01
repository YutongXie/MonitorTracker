
create table MONITOR_TRACKER_JOB_CONFIG(
JOB_ID INT,
JOB_NAME VARCHAR(50),
JOB_DESC VARCHAR(100),
INPUT_PROCESSOR VARCHAR(200),
BUSINESS_PROCESSOR VARCHAR(200),
OUTPUT_PROCESSOR VARCHAR(200),
ALERT_PROCESSOR VARCHAR(200),
STATUS VARCHAR(1),
CREATE_TIME datetime,
LAST_UPDATE_TIME DATETIME
);
create table MONITOR_TRACKER_JOB_DETAIL_CONFIG(
JOB_ID INT,
PROCESSOR_NAME VARCHAR(50),
TYPE VARCHAR(10),
VALUE1 VARCHAR(2000),
VALUE2 VARCHAR(2000),
VALUE3 VARCHAR(2000),
VALUE4 VARCHAR(2000),
VALUE5 VARCHAR(2000)
)