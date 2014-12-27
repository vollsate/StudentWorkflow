/**
 * 
 */
package no.glv.android.stdntworkflow.core;

import no.glv.android.stdntworkflow.StudentListHandler;
import android.app.Activity;	

/**
 * @author GleVoll
 *
 */
public class BaseActivity extends Activity {

	/**
	 * 
	 */
	public BaseActivity() {
		// TODO Auto-generated constructor stub
	}
	
	public Student getStudent(int id) {
		return StudentListHandler.GetInstance().getBean( id );
	}

}
