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