package no.glv.android.stdntworkflow.core;

import java.util.Collections;
import java.util.List;

import no.glv.android.stdntworkflow.intrfc.Student;
import no.glv.android.stdntworkflow.intrfc.Task;
import android.util.Log;

public class SettingsManager {

    private static final String TAG = SettingsManager.class.getSimpleName();

    public static final int NEWTASK_USEIDENT = 0;
    public static final int NEWTASK_USEFIRSTNAME = 1;
    public static final int NEWTASK_USEFULLNAME = 2;

    private boolean showFullname = true;

    private int mShowCount = 5;

    private int newTaskUse = NEWTASK_USEIDENT;

    public SettingsManager() {
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

    /**
     * Get the text to display when a new user is to be added to a {@link Task}.
     * 
     * @param std
     * @return
     */
    public String getStdInfoWhenNewTask( Student std ) {
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
	Log.d( TAG, "Sorted list: " + list.toString() );
    }

    /**
     * 
     * @param className
     */
    public void sortByFirstNameAsc( String className ) {
	List<Student> list = DataHandler.GetInstance().getStudentClass( className ).getStudents();
	Collections.sort( list, new DataComparator.StudentComparator( DataComparator.SORT_FIRSTNAME_ASC ) );
	Log.d( TAG, "Sorted list: " + list.toString() );
    }
    
    /**
     * 
     * @return
     */
    public int getStudentSortType() {
	return DataComparator.SORT_FIRSTNAME_ASC;
    }
    
    /**
     * 
     * @return
     */
    public int getStudentClassSortType() {
	return DataComparator.SORT_IDENT_ASC;
    }

}
