javac -d ../../../../../../outputs/ **/**/*.java **/*.java
cd ../../../../../../outputs/
jar cfe server.jar ru.se.ifmo.prog.lab7.server.Main ru/se/ifmo/prog/lab7/commands/*.class ru/se/ifmo/prog/lab7/cores/*.class ru/se/ifmo/prog/lab7/classes/*.class ru/se/ifmo/prog/lab7/exceptions/*.class ru/se/ifmo/prog/lab7/server/**/*.class ru/se/ifmo/prog/lab7/server/*.class
java -cp postgresql-42.7.3.jar:server.jar ru.se.ifmo.prog.lab7.server.Main
