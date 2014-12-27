package no.glv.android.stdntworkflow;

import no.glv.android.stdntworkflow.R;
import no.glv.android.stdntworkflow.R.id;
import no.glv.android.stdntworkflow.R.layout;
import no.glv.android.stdntworkflow.core.Student;
import no.glv.android.stdntworkflow.core.StudentBean;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 
 * @author GleVoll
 *
 */
public class StudentListHandler extends ArrayAdapter<StudentBean> {
	
	/**  */
	private static final String TAG = StudentListHandler.class.getSimpleName();
	
	/** */
	private Context context;
	/** */
	private Student[] beans;
	/**  */
	public static final String EXTRA_STUDENTBEAN = "no.glv.android.stdntworkflow.StudentBean";
	
	/**  */
	private static StudentListHandler instance;
	
	
	
	public static final StudentListHandler GetInstance() {
		return instance;
	}
	
	static final void SetInstance(StudentListHandler adapter) {
		instance = adapter;
	}
	
	
	
	/**
	 * 
	 * @param context
	 * @param objects
	 */
	public StudentListHandler( Context context, StudentBean[] objects ) {
		super(context, R.layout.student_list_row, objects );
		
		this.context = context;
		this.beans = objects;
	}
	
	
	/**
	 * 
	 */
	@Override
	public View getView( int position, View convertView, ViewGroup parent ) {
		View myView = convertView;
		
		if (myView == null) {
			myView = createView( context, parent, position );
		}
		
		ViewHolder holder = (ViewHolder) myView.getTag();
		Student bean = beans[position];
		
		holder.textView.setText( bean.getFirstName() );
		
		return myView;
	}
	
	
	/**
	 * 
	 * @param context
	 * @param parent
	 * @return
	 */
	private View createView(Context context, ViewGroup parent, int position) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
		View myView = inflater.inflate( R.layout.student_list_row, parent, false );
		ViewHolder holder = new ViewHolder();
		
		ImageView imgInfoView = (ImageView) myView.findViewById( R.id.info );
		imgInfoView.setTag( Integer.valueOf( position) );
		imgInfoView.setOnClickListener( new View.OnClickListener() {
			
			@Override
			public void onClick( View v ) {
				Log.d( TAG, "" + v.getTag() );
				
				Intent intent = new Intent( getContext(), StudentInfoActivity.class );
				Integer id = (Integer)v.getTag();
				intent.putExtra ( EXTRA_STUDENTBEAN, id.intValue());
				StudentListHandler.this.getContext().startActivity( intent );
			}
		});
		
		ImageView imgTaskView = (ImageView) myView.findViewById( R.id.task );
		imgTaskView.setTag( Integer.valueOf( position) );
		imgInfoView.setOnClickListener( new View.OnClickListener() {
			
			@Override
			public void onClick( View v ) {
				Log.d( TAG, "" + v.getTag() );
				
				Intent intent = new Intent( getContext(), StudentInfoActivity.class );
				Integer id = (Integer)v.getTag();
				intent.putExtra ( EXTRA_STUDENTBEAN, id.intValue());
				StudentListHandler.this.getContext().startActivity( intent );
			}
		});

		
		TextView textView = (TextView) myView.findViewById( R.id.studentlistName );
		
		holder.textView = textView;
		holder.imgInfoView = imgInfoView;
		holder.imgTaskView = imgTaskView;
		holder.id = position;
		
		myView.setTag( holder );		
		
		return myView;
	}
	
	
	public Student getBean( int id ) {
		return beans[ id ];
	}
	
	
	
	
	
	
	
	static class ViewHolder {
		int id;
		
		TextView textView;
		ImageView imgInfoView;
		ImageView imgTaskView;
		
	}
	

}
