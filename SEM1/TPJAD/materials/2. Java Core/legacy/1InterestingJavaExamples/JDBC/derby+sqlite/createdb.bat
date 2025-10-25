call setcp
del dbexemplu.db
%SQLITE_HOME%\sqlite3 dbexemplu.db <Screate.sql
rd /s /q dbexemplu
java -jar %DERBY_HOME%\lib\derbyrun.jar ij Dcreate.sql
