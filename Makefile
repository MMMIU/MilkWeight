.PHONY = make jar runjar test clean

CLASSPATH = .:junit-platform-console-standalone-1.5.2.jar:json-simple-1.1.1.jar
JAR = /Library/Java/JavaVirtualMachines/jdk-11.0.6.jdk/Contents/Home/bin/jar
JC = /Library/Java/JavaVirtualMachines/jdk-11.0.6.jdk/Contents/Home/bin/javac
JAVA = /Library/Java/JavaVirtualMachines/jdk-11.0.6.jdk/Contents/Home/bin/java
MP =  --module-path /Users/mmmiu/javafx-sdk-11.0.2/lib
AM = --add-modules javafx.controls -Dfile.encoding=UTF-8 
PATH = -p /Users/mmmiu/javafx-sdk-11.0.2/lib/javafx-swt.jar:/Users/mmmiu/javafx-sdk-11.0.2/lib/javafx.base.jar:/Users/mmmiu/javafx-sdk-11.0.2/lib/javafx.controls.jar:/Users/mmmiu/javafx-sdk-11.0.2/lib/javafx.fxml.jar:/Users/mmmiu/javafx-sdk-11.0.2/lib/javafx.graphics.jar:/Users/mmmiu/javafx-sdk-11.0.2/lib/javafx.web.jar:/Users/mmmiu/javafx-sdk-11.0.2/lib/javafx.swing.jar:/Users/mmmiu/javafx-sdk-11.0.2/lib/javafx.media.jar 
CP = -classpath /Users/mmmiu/eclipse-workspace/p5:/Users/mmmiu/javafx-sdk-11.0.2/lib/javafx-swt.jar:/Users/mmmiu/javafx-sdk-11.0.2/lib/javafx.base.jar:/Users/mmmiu/javafx-sdk-11.0.2/lib/javafx.controls.jar:/Users/mmmiu/javafx-sdk-11.0.2/lib/javafx.fxml.jar:/Users/mmmiu/javafx-sdk-11.0.2/lib/javafx.graphics.jar:/Users/mmmiu/javafx-sdk-11.0.2/lib/javafx.web.jar:/Users/mmmiu/javafx-sdk-11.0.2/lib/javafx.swing.jar:/Users/mmmiu/javafx-sdk-11.0.2/lib/javafx.media.jar
MAIN = application.Main

eclipse:
	$(JAVA) $(MP) $(AM) $(CP) $(MAIN)

compile: 
	$(JC) $(CP) -d . application/*.java

run:
	$(JC)  $(CP) -d . application/*.java
	$(JAVA)  $(MP) $(AM) $(CP) application.Main

jar: 
	$(JC) $(CP) -d . application/*.java
	$(JAR) cvmf manifest.txt executable.jar .

runjar:
	$(JAVA) $(MP) $(AM) -jar executable.jar

zip:
	$(JAR) cvf team.zip application/* *

test: 
	javac -cp $(CLASSPATH) *.java
	java -jar junit-platform-console-standalone-1.5.2.jar --class-path $(CLASSPATH) -p ""

clean:
	\rm application/*.class
	\rm executable.jar
