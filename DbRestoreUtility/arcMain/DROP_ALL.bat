cd %~dp0
del DATA\DROPSTATEMENT.TXT
BTEQ <script/DROPALL.sql> data/log/DROP_ALL.log 2>&1

