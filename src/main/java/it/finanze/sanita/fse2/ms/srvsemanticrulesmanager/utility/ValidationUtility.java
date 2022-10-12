package it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.utility;

public class ValidationUtility {
	
	private ValidationUtility() {
		
	}
	
	public static final int DEFAULT_STRING_MIN_SIZE = 0;
	public static final int DEFAULT_STRING_MAX_SIZE = 100;
	public static final int DEFAULT_NUMBER_MIN_SIZE = 0;
	public static final int DEFAULT_NUMBER_MAX_SIZE = 10000;
	public static final int DEFAULT_ARRAY_MIN_SIZE = 0;
	public static final int DEFAULT_ARRAY_MAX_SIZE = 10000;
	public static final int DEFAULT_BINARY_MIN_SIZE = 0;
	public static final int DEFAULT_BINARY_MAX_SIZE = 10000;

	public static boolean isMajorVersion(final String newVersion, final String lastVersion) {
		boolean isMajor = false;
		String[] lastVersionChunks = lastVersion.split("\\.");
		String [] newVersionChunks = newVersion.split("\\.");
	
		if (Integer.valueOf(lastVersionChunks[0]) < Integer.valueOf(newVersionChunks[0])) {
			isMajor = true;
		} else if (Integer.valueOf(lastVersionChunks[0]).equals(Integer.valueOf(newVersionChunks[0]))) {
			isMajor = Integer.valueOf(lastVersionChunks[1]) < Integer.valueOf(newVersionChunks[1]);
		}
		
		return isMajor;
	}	
}
