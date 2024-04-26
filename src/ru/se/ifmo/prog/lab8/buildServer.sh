javac -d ../../../../../../outputs/ cores/*.java exceptions/*.java commands/*.java classes/*.java server/**/*.java server/*.java
cd ../../../../../../outputs/
jar cfe server.jar ru.se.ifmo.prog.lab7.server.Main ru/se/ifmo/prog/lab8/commands/*.class ru/se/ifmo/prog/lab8/cores/*.class ru/se/ifmo/prog/lab8/classes/*.class ru/se/ifmo/prog/lab8/exceptions/*.class ru/se/ifmo/prog/lab8/server/**/*.class ru/se/ifmo/prog/lab8/server/*.class
java -cp postgresql-42.7.3.jar:server.jar ru.se.ifmo.prog.lab8.server.Main
