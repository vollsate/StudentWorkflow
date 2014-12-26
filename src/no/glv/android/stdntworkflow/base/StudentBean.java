package no.glv.android.stdntworkflow.base;

public class StudentBean implements Student {
	
	private String fName;
	private String lName;
	private String bYear;

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

}
