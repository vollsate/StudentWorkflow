package no.glv.android.stdntworkflow.core;

import java.util.Collections;
import java.util.List;

import no.glv.android.stdntworkflow.intrfc.Student;

public class SettingsManager {
	
	private boolean showFullname = true;

	public SettingsManager() {
		// TODO Auto-generated constructor stub
	}
	
	public boolean isShowFullname() {
		return showFullname;
	}
	
	public void setShowFullname( boolean show ) {
		this.showFullname = show;
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
