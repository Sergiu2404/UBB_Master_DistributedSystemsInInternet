javac -cp .;%ACTIVEMQ_HOME%\activemq-all-5.15.16.jar ExecJmsClie.java
rem AdresaServiciuMesagerie CoadaCerere CoadaRaspuns
java -cp .;%ACTIVEMQ_HOME%\activemq-all-5.15.16.jar ExecJmsClie %1 %2 %3
pause
