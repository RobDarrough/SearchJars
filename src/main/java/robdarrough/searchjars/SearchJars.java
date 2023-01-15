package robdarrough.searchjars;

import java.io.*;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.*;

/**
 * SearchJars searches for files contained in JAR, ZIP, WAR and EAR files.
 * 
 * Very useful for resolving compilation issues involving a missing class
 * file or a conflict between class files. The compilers messages will
 * only name the class files. This utility identifies the corresponding
 * jar files. It is also good for finding corrupted archives.  
 */
public class SearchJars {

	public static void main(String[] args) {
	    
		try {
	        // there should be two args, 
			// 1. the starting folder or file 
			// 2. the search pattern.
	        if(args.length != 2) {
	            summary();
	            System.exit(1);
	        }
	        
	        File startFileFolder = new File(args[0]);
	        String searchFor = args[1];
	
			SearchJars app = new SearchJars();
			
			// stop the search if the path given on the cmd line was bad.
	        app.checkPaths(startFileFolder);
	        
	        app.search(startFileFolder, searchFor);
        
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return;
	}
	
	// command explanation for command line interface.
	private static void summary() {
	
		System.out.println("SearchJars searches for files contained in JAR, ZIP, WAR and EAR files.    ");
		System.out.println("");
		System.out.println("SearchJars PATH PATTERN");
		System.out.println("");
		System.out.println("      PATH: Archive file or directory to be searched. If a directory is    ");
		System.out.println("            specified then all subdirectories are searched.                ");
		System.out.println("");
		System.out.println("   PATTERN: Regular expression to be searched for. DUMP will display the   ");
		System.out.println("            entire contents of the file(s). A dot will be translated into  ");
		System.out.println("            a forward slash so that package names can be searched for.     ");
		System.out.println("");
		
		return;
	}
		
    // identifies archive files by their extension.
    private FileFilter archiveFilesFilter = new FileFilter() {
        @Override
        public boolean accept(File f) {
            return f.getName().toUpperCase().endsWith(".JAR")
                    || f.getName().toUpperCase().endsWith(".ZIP")
                    || f.getName().toUpperCase().endsWith(".WAR")
                    || f.getName().toUpperCase().endsWith(".EAR");
        }
    };
    	
	/**
	 * confirm that the path provided is good.
	 */
	void checkPaths(File startFileFolder) {

		// the starting folder or file has to exist.
		if(!startFileFolder.exists()) {
			System.out.println("ERROR: specified path does not exist.\n");
			summary();
			System.exit(2);
		} 

		// the starting folder or file has to be readable.
		if(!startFileFolder.canRead()) {
			System.out.println("ERROR: specified path is not readable.\n");
			summary();
			System.exit(3);
		}
		
        // if a file is named, then it has to be an archive file.
        if(!startFileFolder.isDirectory() && !archiveFilesFilter.accept(startFileFolder)) {
            System.out.println("ERROR: specified file is not an archive file.\n");
            summary();
            System.exit(4);
        }

		return;
	}
	
   /**
	* priming method for the recursive search.
	**/	
	void search(File startFileFolder, String searchFor) {
		
		// the pattern "dump" means print all zip file entries.
		if(searchFor.equalsIgnoreCase("dump")) 
			searchFor = null;

		if(startFileFolder.isDirectory()) 
			checkFolder(startFileFolder, searchFor);
		else 
			checkZipFile(startFileFolder, searchFor);

		return;
	}

	// scans the folder and all its sub folders for matching zip file 
	// entries. prints them on the console. uses recursion.
	void checkFolder(File folder, String regex) {
	
	    // list all archive files.
		File[] files = folder.listFiles(archiveFilesFilter);
		
		// search all archive files.
		for(File file : files)
		    checkZipFile(file, regex);

		// list all sub folders
        File[] folders = folder.listFiles(new FileFilter() {
            @Override
            public boolean accept(File f) {
                return f.isDirectory();
            }
        });
        
		// use recursion for sub folders.
		for(File subfolder : folders) {
			if(subfolder.isDirectory())
				checkFolder(subfolder, regex);
		}
		
		return;
	}

	// seek matching entries in this zip file.
	void checkZipFile(File file, String searchFor) {
		
		try(ZipFile zipFile = new ZipFile(file)) {
		    
    		Enumeration<? extends ZipEntry> entries = zipFile.entries();
    		
    		while(entries.hasMoreElements()) {
    			ZipEntry zipEntry = entries.nextElement();
    			if(searchFor == null)
    				print(file, zipEntry.getName());
    			else if(contain(searchFor, zipEntry.getName()))
    				print(file, zipEntry.getName());
    		}
    		
    		// catch the exception so that the search will not end.
		} catch(Exception exc) {
		    System.out.println("Corrupted File: " + file.getAbsolutePath());
		}
		
		return;
	}
	
	boolean contain(String searchFor, String candidate) {
		
		if(searchFor.equals("") && candidate.equals(""))
			return true;
		
		// if the search key is larger than the candidate then
		// the candidate cannot contain the search key.
		if(searchFor.length() > candidate.length())
			return false;
		
		char[] search = searchFor.toCharArray();
		char[] cand = candidate.toCharArray();
		int c = 0, s = 0;
		
		for(; c < cand.length; c++, s++) {
			
			// if there are less remaining characters in the candidate than
			// there are remaining characters in the search key then the 
			// candidate cannot contain the search key.
			if(cand.length - c < search.length - s)
				return false;
			
			// if the search key has matched character for character any
			// region of the candidate then the search key has been found in
			// the candidate.
			if(s == search.length)
				return true;
			
			// if at any point the search string does not match the candidate 
			// then restart the search one character after where they originally 
			// matched.
			if(!compare(cand[c], search[s])) {
				c = c - s;
				s = -1;	
			}
		}
		
		return (s == search.length);
	}
	
	boolean compare(char c, char s) {
		// dot in package name matches forward slash in path.
		return c == s || (c == '/' && s == '.');
	}
	
	private Set<File> previousFiles = new HashSet<File>();
	
	void print(File file, String zipEntryName) {
		
	    // the first time a entry from a jar is printed, also print the file name.
		if(!previousFiles.contains(file)) {
			previousFiles.add(file);
			System.out.println(file.getAbsolutePath());
		}

		System.out.println("\t" + zipEntryName);
		
		return;
	}
}
