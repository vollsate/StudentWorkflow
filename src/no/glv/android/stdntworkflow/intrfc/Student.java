package no.glv.android.stdntworkflow.intrfc;

import java.util.Date;
import java.util.List;

/**
 * Represents a student. A student is a member of a class, and involved in
 * one or more {@link Task}s.
 * 
 * <p>
 * 
 * @author glevoll
 *
 */
public interface Student {

	public static final String EXTRA_IDENT = BaseValues.EXTRA_BASEPARAM + "Ident";
	public static final String EXTRA_STUDENTNAME = BaseValues.EXTRA_BASEPARAM + "StudentName";

	public String getFirstName();

	public void setFirstName( String fName );

	public String getBirthYear();

	public String getLastName();

	public void setLastName( String lName );

	public Date getBirth();

	public void setBirth( Date val );

	public String getAdress();

	public void setAdress( String val );

	public String getPostalCode();

	public void setPostalCode( String val );

	public String getPhone();

	public void setPhone( String val );

	public String getGrade();

	public void setGrade( String val );

	public String getStudentClass();

	public void setStudentClass( String val );

	/** @return The unique identity used on It's Learning and Google */
	public String getIdent();

	public void setIdent( String val );

	public List<Parent> getParents();

	public void addParent( Parent parent );

	public void addParents( List<Parent> parents );

}
