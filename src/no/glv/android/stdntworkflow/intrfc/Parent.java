package no.glv.android.stdntworkflow.intrfc;

import java.util.List;

public interface Parent {

	public static final int PRIMARY = 1;
	public static final int SECUNDARY = 2;

	public String getFirstName();

	public String getLastName();

	public String getMail();

	public int getType();

	public String getID();
	
	public void setID( String id );

	public String getStudentID();
	
	public void setStudentID( String stdID );
	
	public List<Phone> getPhoneNumbers();

	public void setFirstName( String name );

	public void setLastName( String name );

	public void setMail( String mail );

	public void addPhone( Phone phone );

	public void addPhones( List<Phone> phones );

	public Phone getPhone( int type );

}
