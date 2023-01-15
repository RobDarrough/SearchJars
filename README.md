# SearchJars

A Java application that searches for files contained in JAR, ZIP, WAR and EAR 
files. Very useful for resolving compilation issues involving a missing class 
file or a conflict between class files. The compilers messages will only name 
the class files. This utility identifies the corresponding jar files. It is 
also good for finding corrupted archives.

# Usage

From the command line ...

SearchJars PATH PATTERN

PATH: Archive file or directory to be searched. If a directory is specified 
	then all subdirectories are searched.

PATTERN: Regular expression to be searched for. DUMP will display the
		entire contents of the file(s). A dot will be translated into
		a forward slash so that package names can be searched for. 

# Install (Windows)

Java must be installed on the system and listed on the system path.

Maven must be installed on the system and listed on the system path.

1. download the project from Github.

2. From a command line change to the project root and run "mvn clean install"
   If the system responds "bad command or filename" then either Maven is not installed or it is not listed on the system path.
   If there are other errors try seeking help on Reddit.
   
3. Add a folder to your path, say c:\scripts and add it to your system path.

4. create a batch file named SearchJars.bat in c:\scripts containing this line ...

		java -jar D:\bin\jars\SearchJars.jar %1 %2

5. From the project root copy target\SearchJars.jar to c:\scripts

6. For a test, close your command line and open a new one. Go to your JDK folder and type the following command ...

		SearchJars . java.lang.Object
		
It should respond ...

		C:\PF\jdk-18.0.2.1>java -jar D:\bin\jars\SearchJars.jar . java.lang.Object
		C:\PF\jdk-18.0.2.1\.\lib\src.zip
			java.base/java/lang/Object.java