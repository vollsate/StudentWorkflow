package no.glv.android.stdntworkflow.test;

import no.glv.android.stdntworkflow.core.StudentBean;

public class StudentBeanData {

	public StudentBeanData() {
		// TODO Auto-generated constructor stub
	}
	
	
	public static StudentBean[] CreateTestData() {
		StudentBean[] beans = new StudentBean[15];
		
		for ( int i = 0; i < beans.length; i++ ) {
			if ( i%2 == 0 )
				beans[i] = CreateOne();
			else
				beans[i] = CreateTwo();
		}
		
		return beans;
	}
	
	
	
	private static StudentBean CreateOne() {
		return new StudentBean("Glenn", "Vollsæter", "1974");
	}

	private static StudentBean CreateTwo() {
		return new StudentBean("Victoria", "Gaarder", "1982");
	}

}
