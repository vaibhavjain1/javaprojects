cd %~dp0
del DATA\SHOWSQL.TXT
del DATA\DDL.TXT
BTEQ <script/GEN_DDL.sql> data/log/GEN_DDL.log 2>&1

cd data
arcmain.exe sessions=4 outlog=..\data\log\ARC_ARCMAIN_EXTRACT.log < ..\script\ARC_Archive.in
