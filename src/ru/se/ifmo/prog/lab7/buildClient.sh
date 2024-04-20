javac -d ../../../../../../outputs/ cores/*.java exceptions/*.java commands/*.java classes/*.java client/**/*.java client/*.java
cd ../../../../../../outputs/
jar cfe client.jar ru.se.ifmo.prog.lab7.client.Main ru/se/ifmo/prog/lab7/commands/*.class ru/se/ifmo/prog/lab7/cores/*.class ru/se/ifmo/prog/lab7/classes/*.class ru/se/ifmo/prog/lab7/exceptions/*.class ru/se/ifmo/prog/lab7/client/**/*.class ru/se/ifmo/prog/lab7/client/*.class
java -cp postgresql-42.7.3.jar:client.jar ru.se.ifmo.prog.lab7.client.Main

