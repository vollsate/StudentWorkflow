package no.glv.android.stdntworkflow.core;

import java.util.Collections;
import java.util.List;

import no.glv.android.stdntworkflow.intrfc.Student;

public class SettingsManager {
	
	public static final int NEWTASK_USEIDENT = 0;
	public static final int NEWTASK_USEFIRSTNAME = 1;
	public static final int NEWTASK_USEFULLNAME = 2;
	
	private boolean showFullname = true;
	
	private int mShowCount = 5;
	
	private int newTaskUse = NEWTASK_USEIDENT;

	public SettingsManager() {
		// TODO Auto-generated constructor stub
	}
	
	public boolean isShowFullname() {
		return showFullname;
	}
	
	public void setShowFullname( boolean show ) {
		this.showFullname = show;
	}
	
	public int getShowCount() {
		return mShowCount;
	}
	
	public String getNewTaskText( Student std ) {
		switch ( newTaskUse ) {
		case NEWTASK_USEIDENT:
			return std.getIdent();

		case NEWTASK_USEFIRSTNAME:
			return std.getFirstName();

		case NEWTASK_USEFULLNAME:
			return std.getLastName() + ", " + std.getFirstName();

		default:
			return std.getIdent();
		}
	}
	
	/**
	 * 
	 * @param className
	 */
	public void sortByLastNameAsc( String className ) {
		List<Student> list = DataHandler.GetInstance().getStudentClass( className ).getStudents();
		Collections.sort( list, new DataComparator.StudentComparator( DataComparator.SORT_LASTNAME_ASC ) );
	}
	
	/**
	 * 
	 * @param className
	 */
	public void sortByFirstNameAsc( String className ) {
		List<Student> list = DataHandler.GetInstance().getStudentClass( className ).getStudents();
		Collections.sort( list, new DataComparator.StudentComparator( DataComparator.SORT_FIRSTNAME_ASC ) );
	}
	
}
