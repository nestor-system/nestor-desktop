package cy.ac.ouc.cognition.nestor.app;

import cy.ac.ouc.cognition.nestor.lib.parameters.NESTORParameters;


public class NESTORAppParameters extends NESTORParameters {


    // The single instance of the NESTORParameters class
    private static NESTORAppParameters	NESTORAppParametersInstance;
    protected static String				NESTORAppParametersFile = "NESTORApp.Settings.json";


    protected NESTORAppParameters() {

    	super();

    }

    
    
    /********************************
	 * NESTOR App Parameters
	 ********************************/
	private String		fileNLPData;
	private String		fileDocumentJSON;
	private String		fileLogicAnnotation;
	private String		fileTranslationPolicy;
	private String		fileGeneratedExpressions;
	

	
	
    public static synchronized NESTORAppParameters getInstance(String jsonString) {

    	if (NESTORAppParametersInstance == null) {

    		System.out.println("NESTOR App Parameters Instance is null!");

    		NESTORAppParametersInstance = new NESTORAppParameters();

        	// Update configuration from a source file or jsonString
    		NESTORAppParametersInstance.updateValues(jsonString);
    	}
    	 
    	else if (jsonString != null && !jsonString.equals(""))
        	// Update configuration from jsonString
    		NESTORAppParametersInstance.updateValues(jsonString);

    	
    	return NESTORAppParametersInstance;

    }



	public String getParametersFile() {
		return NESTORAppParametersFile;
	}


	protected void initializeDefaults() {
		
		fileNLPData = "NLPData.txt";
		fileDocumentJSON = "NLDocument.json";
		fileLogicAnnotation = "LogicAnnotation.prudens";
		fileTranslationPolicy = "TranslationPolicy.prudens";
		fileGeneratedExpressions = "LogicExpressions.prudens";
		
	}

	
	
	public void printValues() {
		
		System.out.println("");
		System.out.println("NESTOR Application Parameters Values:");
		System.out.println("fileNLPData = [" + fileNLPData + "]");
		System.out.println("fileDocumentJSON = [" + fileDocumentJSON + "]");
		System.out.println("fileLogicAnnotation = [" + fileLogicAnnotation + "]");
		System.out.println("fileTranslationPolicy = [" + fileTranslationPolicy + "]");
		System.out.println("fileGeneratedExpressions = [" + fileGeneratedExpressions + "]");

	}
	
	
	
	/**
	 * @return the fileNLPData
	 */
	public String getFileNLPData() {
		return fileNLPData;
	}



	/**
	 * @param fileNLPData the fileNLPData to set
	 */
	public void setFileNLPData(String fileNLPData) {
		this.fileNLPData = fileNLPData;
	}



	/**
	 * @return the fileDocumentJSON
	 */
	public String getFileDocumentJSON() {
		return fileDocumentJSON;
	}



	/**
	 * @param fileDocumentJSON the fileDocumentJSON to set
	 */
	public void setFileDocumentJSON(String fileDocumentJSON) {
		this.fileDocumentJSON = fileDocumentJSON;
	}



	/**
	 * @return the fileLogicAnnotation
	 */
	public String getFileLogicAnnotation() {
		return fileLogicAnnotation;
	}



	/**
	 * @param fileLogicAnnotation the fileLogicAnnotation to set
	 */
	public void setFileLogicAnnotation(String fileLogicAnnotation) {
		this.fileLogicAnnotation = fileLogicAnnotation;
	}



	/**
	 * @return the fileTranslationPolicy
	 */
	public String getFileTranslationPolicy() {
		return fileTranslationPolicy;
	}



	/**
	 * @param fileTranslationPolicy the fileTranslationPolicy to set
	 */
	public void setFileTranslationPolicy(String fileTranslationPolicy) {
		this.fileTranslationPolicy = fileTranslationPolicy;
	}



	/**
	 * @return the fileGeneratedExpressions
	 */
	public String getFileGeneratedExpressions() {
		return fileGeneratedExpressions;
	}



	/**
	 * @param fileGeneratedExpressions the fileGeneratedExpressions to set
	 */
	public void setFileGeneratedExpressions(String fileGeneratedExpressions) {
		this.fileGeneratedExpressions = fileGeneratedExpressions;
	}


}
