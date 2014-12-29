package no.glv.android.stdntworkflow.core;

import java.text.DateFormat;
import java.util.Date;

public class StudentBean implements Student {
	
	String fName;
	String lName;
	String bYear;
	Date birth;
	
	String parent1Name;
	String parent1Phone;
	String parent1Mail;

	String parent2Name;
	String parent2Phone;
	String parent2Mail;
	
	String adress;
	String postalCode;
	String grade;
	
	private String studentClass;
	
	private StudentBean()  {
	}

	public StudentBean(String stdClass)  {
		this();
		this.studentClass = stdClass;
	}

	@Override
	public String getFirstName() {
		return fName;
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
		return java.text.DateFormat.getDateInstance().format( birth );
		//return birth.toString(); //DateFormat.format( "", birth ).toString():
		
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
	
	String birhtToString() {
		return DateFormat.getDateInstance().format( birth );
	}
	
	void setFullName(String fullName) {
		String[] name = fullName.split( "," );
		
		lName = name[0].trim();
		fName = name[1].trim();
	}

	@Override
	public String getStudentClass() {
		return studentClass;
	}

}
