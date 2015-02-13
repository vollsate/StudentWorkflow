package no.glv.android.stdntworkflow.intrfc;

/**
 * Represents a phone number. There are three diffent types of numbers: home
 * number, work and mobile.
 * 
 * @author glevoll
 *
 */
public interface Phone {

	public static final int MOBIL = 1;

	public static final int HOME = 2;

	public static final int WORK = 3;

	public long getNumber();

	public int getType();

	public String getStudentID();

	public String getParentID();

	public void setNumber( long num );

	public void setStudentID( String id );

	public void setParentID( String id );

}
