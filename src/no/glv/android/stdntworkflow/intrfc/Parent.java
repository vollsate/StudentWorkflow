package no.glv.android.stdntworkflow.intrfc;

public interface Parent {
	
	public static final int PRIMARY = 1;
	public static final int SECUNDARY = 2;

	public String getFirstName();
	
	public String getLastName();
	
	public String getMail();
	
	public int getType();
	
	public String getID();
	
	public Phone[] getPhoneNumbers();
	
	public void setFirstName( String name );
	
	public void setLastName( String name );
	
	public void setMail( String mail );
	
	public void addPhone( Phone phone);
	
}
