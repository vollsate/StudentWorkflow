package no.glv.android.stdntworkflow.intrfc;

public interface Phone {

	public static final int MOBIL = 1;
	
	public static final int HOME = 2;
	
	public static final int WORK = 3;
	
	public long getNumber();
	
	public int getType();
	
	public String getID();
	
	public void setNumber( long num );

	public void setID( String id );
	
}
