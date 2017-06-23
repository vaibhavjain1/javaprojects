echo cleaning log folder...
cd %~dp0
del /F /S /Q data
echo cleanup completed

IF Not EXIST .\data\log (
MD data\log

) 