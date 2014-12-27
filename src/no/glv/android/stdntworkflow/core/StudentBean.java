package no.glv.android.stdntworkflow.core;

import java.util.Date;

public class StudentBean implements Student {
	
	private String fName;
	private String lName;
	private String bYear;
	private Date birth;
	
	String parent1Name;
	String parent1Phone;
	String parent1Mail;

	String parent2Name;
	String parent2Phone;
	String parent2Mail;
	
	String adress;
	String postalCode;
	
	public StudentBean()  {
	}

	public StudentBean(String firstName, String lastName, String birthYear)  {
		fName = firstName;
		lName = lastName;
		bYear = birthYear;
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

}
