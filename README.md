# BugTracker
Postgraduate studies project, task management application written in Java

SQL EXAMPLES:

Project with creators:
	SELECT P.ID, P.NAME, P.DATE_CREATED, P.description, S.FIRST_NAME, S.LAST_NAME 
	FROM PROJECT P
	LEFT JOIN PERSON S ON P.CREATOR_ID = S.ID;

Issues with their project assignment:
	SELECT I.ID, I.NAME, I.DATE_CREATED, I.PRIORITY, I.STATUS, I.TYPE, P.NAME 
	FROM ISSUE I
	LEFT JOIN PROJECT P ON I.PROJECT_ID = P.ID;

Issues audit log:
	SELECT * FROM ISSUE_AUD;
