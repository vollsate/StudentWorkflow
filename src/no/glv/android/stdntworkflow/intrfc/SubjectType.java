package no.glv.android.stdntworkflow.intrfc;

/**
 * Represents a subject or type to better specify the type of {@link Task} a
 * {@link Student} is involved in.
 * 
 * <p>
 * The two types are either <tt>subject</tt> or <tt>type</tt>. The subject may
 * be translated to curriculum, and the type is category: text, parent approval
 * or other types of tasks the student may be linked to.
 * 
 * <p>The system installs a few default subject/ types. Any custom type the user
 * installs, gets a specific flag.
 * 
 * @author glevoll
 *
 */
public interface SubjectType {

	public static final int TYPE_TYPE = 1;
	public static final int TYPE_SUBJECT = 2;
	
	/** Used to flag a custom type. */
	public static final int TYPE_CUSTOM = 4;

	public int getID();

	public String getName();

	public String getDescription();

	public int getType();

	public void setName( String newName );

	public void setDescription( String newDesc );

	public void setType( int type );
}
