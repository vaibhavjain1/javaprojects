cd %~dp0
DEL DATA\DROP_BKP.txt
DEL DATA\RENAME_TAB.txt
DEL DATA\INSERT_TAB.txt
DEL DATA\GRANTALL.txt
DEL DATA\DROP_BKP.txt

cd data
arcmain.exe sessions=4 outlog=..\data\log\ARC_RESTORE.log < ../script/ARC_Restore.in

cd ../
BTEQ <script/RUN_DDL.sql>> data/log/ARC_RESTORE.log 2>>&1
