package no.glv.android.stdntworkflow.intrfc;


public interface Student {
	
	public static final String EXTRA_IDENT =  BaseValues.EXTRA_BASEPARAM +  "Ident";
	public static final String EXTRA_STUDENTNAME =  BaseValues.EXTRA_BASEPARAM +  "StudentName";

	public String getFirstName();
	public void setFirstName( String fName );
	
	public String getBirthYear();
	
	public String getLastname();
	public void setLastName( String lName );
	
	public String getParent1Name();
	public void setParent1Name( String val );

	public String getParent1Phone();
	public void setParent1Phone( String val );
	
	public String getParent1Mail();
	public void setParent1Mail( String val );
	
	public String getParent2Name();
	public void setParent2Name( String val );
	public String getParent2Phone();
	public void setParent2Phone( String val );
	public String getParent2Mail();
	public void setParent2Mail( String val );
	
	public String getBirth();
	public void setBirth( String val );
	
	public String getAdress();
	public void setAdress( String val );
	
	public String getPostalCode();
	public void setPostalCode( String val );
	
	public String getGrade();
	public void setGrade( String val );
	
	public String getStudentClass();
	public void setStudentClass( String val );
	
	
	/** @return The unique identity used on It's Learning and Google */
	public String getIdent();
	public void setIdent( String val );
	
	public int getID();
	public void setID( int val );

}
