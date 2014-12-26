package no.glv.android.stdntworkflow;

import no.glv.android.stdntworkflow.base.StudentBean;
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
public class StudentListAdapter extends ArrayAdapter<StudentBean> {
	
	/**  */
	private static final String TAG = StudentListAdapter.class.getSimpleName();
	
	/** */
	private Context context;
	/** */
	private StudentBean[] beans;
	/**  */
	public static final String EXTRA_STUDENTBEAN = "no.glv.android.stdntworkflow.StudentBean";
	
	/**  */
	private static StudentListAdapter instance;
	
	
	
	static final StudentListAdapter GetInstance() {
		return instance;
	}
	
	static final void SetInstance(StudentListAdapter adapter) {
		instance = adapter;
	}
	
	
	
	/**
	 * 
	 * @param context
	 * @param objects
	 */
	public StudentListAdapter( Context context, StudentBean[] objects ) {
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
		StudentBean bean = beans[position];
		
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
				StudentListAdapter.this.getContext().startActivity( intent );
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
				StudentListAdapter.this.getContext().startActivity( intent );
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
	
	
	public StudentBean getBean( int id ) {
		return beans[ id ];
	}
	
	
	
	
	
	
	
	static class ViewHolder {
		int id;
		
		TextView textView;
		ImageView imgInfoView;
		ImageView imgTaskView;
		
	}
	

}
