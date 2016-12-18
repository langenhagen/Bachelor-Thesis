package util;

import java.io.*;

/**
 * Class Logger for saving arbitrary information into a file. Works just fine for debugging.
 * Logger can store sets of data in different formats, so that these data can, for example,
 * later be viewed in a spreadsheed programm.
 * 
 * @author langenhagen
 * @version 20110102
 */
public class Logger{

	private FileOutputStream wrtStream;		// Stream-Object
	private String sep;						// Log-Entry separator
	private File file;						// The file-Object
	
	/**
	 * Constructor #1
	 * @param path
	 * The path and filename of the Log-File
	 * 
	 */
	public Logger( String path, String separator){
	
		file = new File(path);
		sep = separator;
		
		try{
			wrtStream = new FileOutputStream(file);
		}catch (FileNotFoundException e){
			System.err.println("Cannot instantiate Logger-Object correctly: FileNotFoundException");
			e.printStackTrace();
		}
		
	}

	/**
	 * Writes information into the log file & appends the standard Separator-String.
	 * Caution: This Method does not close the write-Stream used!
	 * @param info
	 * The Information to write as a String
	 */
	public void log(String info){
		
		log(info, sep);
	}
	
	/**
	 * Writes information into the log file & appends a individual Separator-String.
	 * Caution: This Method does not close the write-Stream used!
	 * @param info
	 * The Information to write as a String
	 * @param sep
	 * A separator used in this logging action as a String
	 */
	public void log(String info, String sep){
		
		try{
			for(int i=0; i<info.length(); i++)
				wrtStream.write(info.charAt(i));
			
			for(int i=0; i<sep.length(); i++)
				wrtStream.write(sep.charAt(i));
		
			wrtStream.flush();
		
		}catch(IOException e){
			System.err.println("Error while logging information to\n" + file.getPath() + ": IOException.");
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Closes the FileOutputstream.
	 */
	public void closeStream(){
		try{
			wrtStream.close();
		}catch (IOException e){
			System.err.println("Closing of the WriteStream for Logger didn't work: IOException");
			e.printStackTrace();
		}
	}
}