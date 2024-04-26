export LD_LIBRARY_PATH=/usr/lib/jvm/java-17-openjdk-17.0.7.0.7-1.fc36.x86_64/lib/server:$LD_LIBRARY_PATH
export LD_BIND_NOW=1
javac -d ../../../../../../outputs cores/*.java exceptions/*.java commands/*.java classes/*.java client/back/**/*.java client/back/*.java
cd client/gui
cmake ../../../../../../../../outputs/tmp
cd ../../../../../../../../outputs/tmp
make
cd ../
./main
#g++ -o ../../../../../../outputs/main.o client/gui/**/*.h client/gui/**/*.cpp client/gui/*.cpp -I /usr/lib/jvm/java-17-openjdk-17.0.7.0.7-1.fc36.x86_64/include/ -I /usr/lib/jvm/java-17-openjdk-17.0.7.0.7-1.fc36.x86_64/include/linux/  -L /usr/lib/jvm/java-17-openjdk-17.0.7.0.7-1.fc36.x86_64/lib/server/ -L /usr/lib/jvm/java-17-openjdk-17.0.7.0.7-1.fc36.x86_64/lib/server/ -ljvm
#cd ../../../../../../outputs/
#./main.o
#jar cfe client.jar ru.se.ifmo.prog.lab8.client.back.Main ru/se/ifmo/prog/lab7/commands/*.class ru/se/ifmo/prog/lab7/cores/*.class ru/se/ifmo/prog/lab7/classes/*.class ru/se/ifmo/prog/lab7/exceptions/*.class ru/se/ifmo/prog/lab7/client/**/*.class ru/se/ifmo/prog/lab7/client/*.class
#java -cp postgresql-42.7.3.jar:client.jar ru.se.ifmo.prog.lab7.client.Main

