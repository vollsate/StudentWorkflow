package no.glv.android.stdntworkflow.sql;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import no.glv.android.stdntworkflow.intrfc.BaseValues;
import no.glv.android.stdntworkflow.intrfc.Parent;
import no.glv.android.stdntworkflow.intrfc.Student;
import android.util.Log;

/**
 * 
 * @author GleVoll
 *
 */
public class StudentBean implements Student {

	private static final SimpleDateFormat sdf = new SimpleDateFormat( BaseValues.DATE_PATTERN, Locale.getDefault() );
	
	public String mIdent;

	public String fName;
	public String lName;
	public String bYear;
	public Date birth;

	public String adress;
	public String postalCode;
	public String grade;
	
	public String mPhone;

	private String studentClass;

	private List<Parent> parents;

	private StudentBean() {
		parents = new ArrayList<Parent>(2);
	}

	public StudentBean( String stdClass ) {
		this();
		this.studentClass = stdClass;
	}

	@Override
	public String getFirstName() {
		return fName;
	}

	public void setFirstName( String nn ) {
		this.fName = nn;
	}

	public void setLastName( String nn ) {
		this.lName = nn;
	}

	public void setIdent( String nn ) {
		this.mIdent = nn;
	}

	@Override
	public String getBirthYear() {
		return bYear;
	}

	@Override
	public String getLastName() {
		return lName;
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
	public List<Parent> getParents() {
		return parents;
	}

	@Override
	public void addParent( Parent parent ) {
		if ( parent == null ) return;
		
		parents.add( parent );
	}

	@Override
	public void addParents( List<Parent> parents ) {
		this.parents.addAll( parents );
	}

	@Override
	public String getPhone() {
		return mPhone;
	}

	@Override
	public void setPhone( String val ) {
		this.mPhone = val;
	}
}
