package no.glv.android.stdntworkflow;

import no.glv.android.stdntworkflow.core.BaseActivity;
import no.glv.android.stdntworkflow.intrfc.Student;
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
public class StudentListAdapter extends ArrayAdapter<Student> {

	/**  */
	private static final String TAG = StudentListAdapter.class.getSimpleName();

	/** */
	private Student[] beans;

	private Context context;

	private BaseActivity baseActivity;

	/**
	 * 
	 * @param context
	 * @param objects
	 */
	public StudentListAdapter( Context context, Student[] objects ) {
		super( context, R.layout.row_student_list, objects );

		this.beans = objects;
		this.context = context;
	}

	void setBaseActivity( BaseActivity baseActivity ) {
		this.baseActivity = baseActivity;
	}

	/**
	 * 
	 */
	@Override
	public View getView( int position, View convertView, ViewGroup parent ) {
		View myView = convertView;

		/*
		 * if (myView == null) { myView = createView( context, parent, position
		 * ); }
		 */
		myView = createView( context, parent, position );
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
	private View createView( Context context, ViewGroup parent, int position ) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
		View myView = inflater.inflate( R.layout.row_student_list, parent, false );
		ViewHolder holder = new ViewHolder();
		Student student = beans[position];

		ImageView imgInfoView = (ImageView) myView.findViewById( R.id.info );
		imgInfoView.setTag( student );
		imgInfoView.setOnClickListener( new View.OnClickListener() {

			@Override
			public void onClick( View v ) {
				Log.d( TAG, "" + v.getTag() );

				Intent intent = new Intent( getContext(), StudentInfoActivity.class );

				Student std = (Student) v.getTag();
				baseActivity.putIdentExtra( std, intent );

				getContext().startActivity( intent );
			}
		} );

		ImageView imgTaskView = (ImageView) myView.findViewById( R.id.task );
		imgTaskView.setTag( student );
		imgInfoView.setOnClickListener( new View.OnClickListener() {

			@Override
			public void onClick( View v ) {
				Log.d( TAG, "" + v.getTag() );

				Intent intent = new Intent( getContext(), StudentInfoActivity.class );
				Student std = (Student) v.getTag();
				baseActivity.putIdentExtra( std, intent );
				getContext().startActivity( intent );
			}
		} );

		TextView textView = (TextView) myView.findViewById( R.id.TV_stdlist_name );
		textView.setTag( student );

		holder.textView = textView;
		holder.imgInfoView = imgInfoView;
		holder.imgTaskView = imgTaskView;
		holder.id = position;

		myView.setTag( holder );

		return myView;
	}

	/**
	 * 
	 * @param id
	 * @return
	 */
	public Student getBean( int id ) {
		return beans[id];
	}

	static class ViewHolder {
		int id;

		TextView textView;
		ImageView imgInfoView;
		ImageView imgTaskView;

	}

}
