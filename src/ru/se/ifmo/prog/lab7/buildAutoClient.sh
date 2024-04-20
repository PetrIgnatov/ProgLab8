javac -d ../../../../../../outputs/ **/**/*.java **/*.java
cd ../../../../../../outputs/
jar cfe autoclient.jar ru.se.ifmo.prog.lab7.autoclient.Main ru/se/ifmo/prog/lab7/commands/*.class ru/se/ifmo/prog/lab7/cores/*.class ru/se/ifmo/prog/lab7/classes/*.class ru/se/ifmo/prog/lab7/exceptions/*.class ru/se/ifmo/prog/lab7/autoclient/**/*.class ru/se/ifmo/prog/lab7/autoclient/*.class
#java -cp postgresql-42.7.3.jar:client.jar ru.se.ifmo.prog.lab7.client.Main auto 172.17.0.2 6789
java -cp postgresql-42.7.3.jar:autoclient.jar ru.se.ifmo.prog.lab7.autoclient.Main auto 192.168.31.253 6789
