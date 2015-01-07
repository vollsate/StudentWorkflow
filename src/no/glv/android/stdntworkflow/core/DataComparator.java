package no.glv.android.stdntworkflow.core;

import java.util.Comparator;

import no.glv.android.stdntworkflow.intrfc.Student;
import no.glv.android.stdntworkflow.intrfc.StudentTask;

public class DataComparator {
	
	public static final int SORT_FIRSTNAME_ASC = 0;
	public static final int SORT_LASTNAME_ASC = 1;
	
	public static final int SORT_IDENT_ASC = 2;
	public static final int SORT_IDENT_DSC = 3;

	private DataComparator() {
	}
	
	/**
	 * 
	 * @author GleVoll
	 *
	 */
	public static class StudentComparator implements Comparator<Student> {
		
		private int mode;
		
		public StudentComparator(  ) {
			this( SORT_FIRSTNAME_ASC );
		}

		public StudentComparator( int mode ) {
			this.mode = mode;
		}
		
		@Override
		public int compare( Student lhs, Student rhs ) {
			switch ( mode ) {
			case SORT_FIRSTNAME_ASC:
				return lhs.getFirstName().compareToIgnoreCase( rhs.getFirstName() );

			case SORT_LASTNAME_ASC:
				return lhs.getLastName().compareToIgnoreCase( rhs.getLastName() );

			case SORT_IDENT_ASC:
				return lhs.getIdent().compareToIgnoreCase( rhs.getIdent() );
				
			case SORT_IDENT_DSC:
				return rhs.getIdent().compareToIgnoreCase( lhs.getIdent() );

			}
			
			return 0;
		}
		
	}

	public static class StudentTaskComparator implements Comparator<StudentTask> {
		
		private int mode;
		
		public StudentTaskComparator(  ) {
			this( SORT_IDENT_ASC );
		}

		public StudentTaskComparator( int mode ) {
			this.mode = mode;
		}
		
		@Override
		public int compare( StudentTask lhs, StudentTask rhs ) {
			switch ( mode ) {
			case SORT_IDENT_ASC:
				return lhs.getIdent().compareToIgnoreCase( rhs.getIdent() );
				
			case SORT_IDENT_DSC:
				return rhs.getIdent().compareToIgnoreCase( lhs.getIdent() );

			}
			
			return 0;
		}
		
	}
}
