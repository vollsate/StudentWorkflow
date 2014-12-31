package no.glv.android.stdntworkflow.core;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import no.glv.android.stdntworkflow.R.id;

public class TaskImpl implements Task {
	
	private String mName;
	private String mDesc;
	private Date mExpirationDate;
	private int mType;
	
	private HashMap<String, StudentTask> studentsMap;

	private HashMap<String, StudentTask> studentsMapPending;
	
	private List<String> mClasses;

	TaskImpl() {
		mClasses = new ArrayList<String>();
		studentsMap = new HashMap<String, StudentTask>();
		studentsMapPending = new HashMap<String, StudentTask>();
	}

	@Override
	public String getName() {
		return mName;
	}

	@Override
	public Date getDate() {
		return mExpirationDate;
	}

	@Override
	public int getType() {
		return mType;
	}
	
	@Override
	public void setType( int type ) {
		this.mType = type;
	}

	@Override
	public List<String> getStudentsHandedIn() {
		List<String> list = new ArrayList<String>();
		Iterator<String> it = studentsMap.keySet().iterator();
		
		while (it.hasNext()) {
			list.add( it.next() );
		}
		
		return list;
	}

	@Override
	public int getStudentsHandedInCount() {
		return studentsMap.size();
	}

	@Override
	public List<String> getClasses() {
		List<String> list = new ArrayList<String>( mClasses.size() );
		
		for ( int i = 0; i < mClasses.size(); i++ ) {
			list.add( mClasses.get( i ) );
		}
		
		return list;
	}

	@Override
	public void addClass( StudentClass stdClass ) {
		mClasses.add( stdClass.getName() );
		
		Iterator<Student> it = stdClass.iterator();
		while (it.hasNext() ) {
			Student std = it.next();
			
			StudentTaskImpl tImpl = new StudentTaskImpl();
			tImpl.ident = std.getIdent();
			tImpl.task = mName;
			
			studentsMapPending.put( stdClass.getName(), tImpl );
		}
		
	}

	@Override
	public void setName( String name ) {
		mName = name;
	}

	@Override
	public void removeClass( String name ) {
		if ( ! mClasses.contains( name ) )
			return;
		
		mClasses.remove( name );
		StudentClass stdClass = StudentClassHandler.GetInstance().getStudentClass( name );
		List<Student> list = stdClass.getStudents();
		Iterator<Student> it = list.iterator();
		while (it.hasNext()) {
			Student std = it.next();
//			studentsMap.remove( std.getIdent() );
			studentsMapPending.remove( std.getIdent() );
		}
		
	}

	@Override
	public boolean removeStudent( String ident ){
		if (studentsMapPending.containsKey( ident ) ) {
			studentsMapPending.remove( ident );
			return true;
		}
		
		return false;
	}

	@Override
	public void addClass( String name ) {
		StudentClass stdClass = StudentClassHandler.GetInstance().getStudentClass( name );
		addClass( stdClass );
	}



	static class StudentTaskImpl implements StudentTask {
		
		String ident;
		String task;
		Date handInDate;
		

		@Override
		public String getIdent() {
			return ident;
		}

		@Override
		public String getTask() {
			return task;
		}

		@Override
		public Date getHandInDate() {
			return handInDate;
		}
		
		public static StudentTask CreateFromStudent( String taskName, Student std ) {
			StudentTaskImpl impl = new StudentTaskImpl();
			
			impl.ident = std.getIdent();
			impl.task = taskName;
			
			return impl;
		}
		
	}



	@Override
	public boolean addStudent( String ident ) {
		if (ident == null ) return false;
		if (studentsMap.containsKey( ident ) ) return true;
		
		Student std = StudentClassHandler.GetInstance().getStudentById( ident );
		
		if (std == null ) return false; 
		
		studentsMapPending.put( ident, StudentTaskImpl.CreateFromStudent( mName, std ) );

		return true;
	}

	@Override
	public boolean hasClass( String className ) {
		return mClasses.contains( className );
	}

	boolean hasStudent( List<StudentTask> list, String ident ) {
		Iterator<StudentTask> it = list.iterator();
		while (it.hasNext()) {
			if ( it.next().getIdent().equals( ident ) )
				return true;
		}
		
		return false;
	}

	@Override
	public boolean hasStudent( String ident ) {
		if ( studentsMap.containsKey( ident ) ) return true;
		if ( studentsMapPending.containsKey( ident) ) return true;

		return false;
	}

	@Override
	public List<String> getStudents() {
		List<String> stds = new ArrayList<String>();
		
		stds.addAll( studentsMap.keySet() );
		stds.addAll( studentsMapPending.keySet() );
		
		return stds;
	}

	@Override
	public String getDesciption() {
		return mDesc;
	}

	@Override
	public void setDescription( String desc ) {
		mDesc = desc;
	}

	@Override
	public List<String> getStudentsPending() {
		List<String> stds = new ArrayList<String>();

		stds.addAll( studentsMapPending.keySet() );
		return stds;
	}

	@Override
	public void setDate( Date date ) {
		this.mExpirationDate = date;
	}

}
