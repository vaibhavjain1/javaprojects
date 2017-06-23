				DB Restore Version

This utility is created only for terakora employees So that users can archive and restore old DB and save installation time. Users are not allowed to share it outside.
This utility is in developing mode. So please use it carefully. 

Please forward any suggestion/Bug Report/improvements on Vaibhav.Jain@Teradata.com

1.	Requirements for using this software
	a.	Arcmain is installed on your system. To test type arcmain in cmd.
	b.	Bteq is installed on your system.
	c.	Java 6 is installed on your system & Java environment variables are properly set.

This utility will create one backup folder contains backup file & other sql files. The directory user selects to use it as backup directory must be empty.

Backup directory contains one Database back info. Properties file. Which will give fair idea to user about which db is archived, when it is archived, DB IP etc. 

This utility will archive & restore all the DB Present (along with children db) in DB Topology. No need to specify Children db separately

Lock Release will release the locks acquired due to abnormal termination of archiving or restoration of db.

DB Delete will delete all objects present in database along with its children database objects. Use Fast delete option carefully because it will lock other users from accessing database.

WARNING: 
	1.	Please Close all DB Connections & server for the DB you are going to operate on. 
	2.	Never close window during archiving, restoration & Deletion operation. It may harm your db.
	3.	Never temper the backup files & other files created by software.
	4.	Do not start two operations simultaneously.
	5. 	You may see error code 8 in restoration log due to some backup tables not present. That's ok. Known issue
	6.	Return Code 4 & 8 is for warnings, but the normal flow of the execution will continue. 

Following issues fixed in version1.0
	a.	Primary key issue for temporal tables
	b.	Index issues while restoring.
	c.	Db Locking during deletion.

Following issues fixed in version1.6
	a. 	Create db info file with all details.
	b.	Added fast delete option for db deletion.
	c.	Added popup boxes whereever required.

Follwoing issues fixed in version1.8
	a.	Added checks in archive & restore. Whether operation went fine or not.
	b.	Removed Materialized tables problem in db deletion.
	c.	Faster Db Deletion

Naming convention we should following for back up folder

DB_Version = specifies MDM version
_MT = If it’s multitable
_CAPP = If it’s Custom_app along with ISG done
_DATA = If test data has been added in instance
_ISG = If ISG is done.
_Linux = If it is linux environment
_UISG = If ISG is done from Upgraded version

Let say I’m archiving 3101 with custom app+ISG in Linux. Then folder Name will be – “DB_3101_CAPP_ISG_LINUX”
