package no.glv.android.stdntworkflow.intrfc;

public interface SubjectType {

	public static final int TYPE_TYPE = 1;
	public static final int TYPE_SUBJECT = 2;

	public int getID();

	public String getName();

	public String getDescription();

	public int getType();

	public void setName( String newName );

	public void setDescription( String newDesc );

	public void setType( int type );
}
