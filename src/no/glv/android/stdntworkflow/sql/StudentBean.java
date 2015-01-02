package no.glv.android.stdntworkflow.sql;

import java.text.SimpleDateFormat;
import java.util.Date;

import no.glv.android.stdntworkflow.intrfc.BaseValues;
import no.glv.android.stdntworkflow.intrfc.Student;
import android.util.Log;

public class StudentBean implements Student {

	private static final SimpleDateFormat sdf = new SimpleDateFormat( BaseValues.DATE_PATTERN );

	public String mIdent;
	public int mID;

	public 	String fName;
	public String lName;
	public String bYear;
	public Date birth;

	public String parent1Name;
	public String parent1Phone;
	public 	String parent1Mail;

	public String parent2Name;
	public String parent2Phone;
	public String parent2Mail;

	public String adress;
	public String postalCode;
	public String grade;

	private String studentClass;

	private StudentBean() {
	}

	public StudentBean( String stdClass ) {
		this();
		this.studentClass = stdClass;
	}

	@Override
	public String getFirstName() {
		return fName;
	}
	
	public void setFirstName(String nn ) {
		this.fName = nn;
	}

	public void setLastName(String nn ) {
		this.lName = nn;
	}

	public void setIdent(String nn ) {
		this.mIdent = nn;
	}

	@Override
	public String getBirthYear() {
		return bYear;
	}

	@Override
	public String getLastname() {
		return lName;
	}

	@Override
	public String getParent1Name() {
		return parent1Name;
	}

	@Override
	public String getParent1Phone() {
		return parent1Phone;
	}

	@Override
	public String getParent1Mail() {
		return parent1Mail;
	}

	@Override
	public String getParent2Name() {
		return parent2Name;
	}

	@Override
	public String getParent2Phone() {
		return parent2Phone;
	}

	@Override
	public String getParent2Mail() {
		return parent2Mail;
	}

	@Override
	public String getBirth() {
		return sdf.format( birth );

		// return java.text.DateFormat.getDateInstance().format( birth );
		// return birth.toString(); //DateFormat.format( "", birth ).toString():

	}

	@Override
	public String getAdress() {
		return adress;
	}

	@Override
	public String getPostalCode() {
		return postalCode;
	}

	public String getGrade() {
		return grade;
	}

	public String birhtToString() {
		return sdf.format( birth );
	}

	public void setFullName( String fullName ) {
		String[] name = fullName.split( "," );

		lName = name[0].trim();
		fName = name[1].trim();
	}

	@Override
	public String getStudentClass() {
		return studentClass;
	}

	@Override
	public String getIdent() {
		return mIdent;
	}
	
	public String toString() {
		return "Student IDENT: " + mIdent;
	}

	@Override
	public int getID() {
		return mID;
	}

	@Override
	public void setParent1Name( String val ) {
		parent1Name = val;
	}

	@Override
	public void setParent1Phone( String val ) {
		parent1Phone = val;
	}

	@Override
	public void setParent1Mail( String val ) {
		parent1Mail = val;
	}

	@Override
	public void setParent2Name( String val ) {
		parent2Name = val;
	}

	@Override
	public void setParent2Phone( String val ) {
		parent2Phone = val;
	}

	@Override
	public void setParent2Mail( String val ) {
		parent2Mail = val;
	}

	@Override
	public void setBirth( String val ) {
		try {
			birth = sdf.parse( val );
		}
		catch ( Exception e ) {
			Log.e( getClass().getSimpleName(), "Error parsing date: " + val, e );
		}
	}

	@Override
	public void setAdress( String val ) {
		adress = val;
	}

	@Override
	public void setPostalCode( String val ) {
		postalCode = val;
	}

	@Override
	public void setGrade( String val ) {
		grade = val;
	}

	@Override
	public void setStudentClass( String val ) {
		studentClass = val;
	}

	@Override
	public void setID( int val ) {
		mID = val;
	}

}
