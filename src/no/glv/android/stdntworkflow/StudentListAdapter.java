package no.glv.android.stdntworkflow;

import no.glv.android.stdntworkflow.base.StudentBean;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class StudentListAdapter extends ArrayAdapter<StudentBean> {
	
	private Context context;
	private StudentBean[] beans;
	

	public StudentListAdapter( Context context, StudentBean[] objects ) {
		super(context, R.layout.student_list_row, objects );
		
		this.context = context;
		this.beans = objects;
	}
	
	
	@Override
	public View getView( int position, View convertView, ViewGroup parent ) {
		View myView = convertView;
		
		if (myView == null) {
			myView = createView( context, parent );
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
	private View createView(Context context, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
		View myView = inflater.inflate( R.layout.student_list_row, parent, false );
		ViewHolder holder = new ViewHolder();
		
		ImageView imgInfoView = (ImageView) myView.findViewById( R.id.info );
		ImageView imgTaskView = (ImageView) myView.findViewById( R.id.task );
		TextView textView = (TextView) myView.findViewById( R.id.studentlistName );
		
		holder.textView = textView;
		holder.imgInfoView = imgInfoView;
		holder.imgTaskView = imgTaskView;
		
		myView.setTag( holder );		
		
		return myView;
	}
	
	
	
	
	
	
	
	static class ViewHolder {
		
		TextView textView;
		ImageView imgInfoView;
		ImageView imgTaskView;
		
	}
	

}
