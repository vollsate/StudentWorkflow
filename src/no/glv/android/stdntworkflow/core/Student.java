package no.glv.android.stdntworkflow.core;

public interface Student {
	
	public static final String EXTRA_IDENT =  BaseValues.EXTRA_BASEPARAM +  "Ident";
	public static final String EXTRA_STUDENTNAME =  BaseValues.EXTRA_BASEPARAM +  "StudentName";

	public String getFirstName();
	
	public String getBirthYear();
	
	public String getLastname();
	
	public String getParent1Name();
	public String getParent1Phone();
	public String getParent1Mail();
	
	public String getParent2Name();
	public String getParent2Phone();
	public String getParent2Mail();
	
	public String getBirth();
	
	public String getAdress();
	
	public String getPostalCode();
	
	public String getGrade();
	
	public String getStudentClass();
	
	
	/** @return The unique identity used on It's Learning and Google */
	public String getIdent();

}
