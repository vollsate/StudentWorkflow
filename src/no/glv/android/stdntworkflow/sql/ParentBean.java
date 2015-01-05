package no.glv.android.stdntworkflow.sql;

import java.util.ArrayList;
import java.util.List;

import no.glv.android.stdntworkflow.intrfc.Parent;
import no.glv.android.stdntworkflow.intrfc.Phone;

public class ParentBean implements Parent {
	
	private String fName;
	private String lName;
	
	private String id;
	private String mail;
	
	private List<Phone> phones;
	private int type;

	public ParentBean(String id) {
		this.id = id;
	}

	@Override
	public String getFirstName() {
		return fName;
	}

	@Override
	public String getLastName() {
		return lName;
	}

	@Override
	public String getMail() {
		return mail;
	}

	@Override
	public int getType() {
		return type;
	}

	@Override
	public String getID() {
		return id;
	}

	@Override
	public Phone[] getPhoneNumbers() {
		return (Phone[]) phones.toArray();
	}

	@Override
	public void setFirstName( String name ) {
		fName = name;
	}

	@Override
	public void setLastName( String name ) {
		lName = name;
	}

	@Override
	public void setMail( String mail ) {
		this.mail = mail;
	}

	@Override
	public void addPhone( Phone phone ) {
		if ( phones == null ) phones = new ArrayList<Phone>(2);
		
		phones.add( phone );
	}

}
