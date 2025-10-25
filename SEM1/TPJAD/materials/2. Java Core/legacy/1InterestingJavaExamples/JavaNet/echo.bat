@echo off
echo Ii salut pe urmatorii:
:IAR
shift
if "%0" == "" goto :STOP
echo Salut %0
goto :IAR
:STOP

