package no.glv.android.stdntworkflow.test;

import no.glv.android.stdntworkflow.base.*;;

public class StudentBeanData {

	public StudentBeanData() {
		// TODO Auto-generated constructor stub
	}
	
	
	public static StudentBean[] CreateTestData() {
		StudentBean[] beans = new StudentBean[15];
		
		for ( int i = 0; i < beans.length; i++ ) {
			beans[i] = CreateOne();
		}
		
		return beans;
	}
	
	
	
	private static StudentBean CreateOne() {
		return new StudentBean("Glenn", "Vollsæter", "1974");
	}

}
