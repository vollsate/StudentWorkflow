package no.glv.android.stdntworkflow;

/**
 * 
 * @author glevoll
 *
 */
public class NavDrawerItem {

	private String title;
	private int icon;
	private String count = "0";

	// boolean to set visiblity of the counter
	private boolean isCounterVisible = false;

	// Used to indicate weather or not it's a header or listItem
	private boolean isHeader;

	/**
	 * Use this constructor to create a header in the NavDrawer.
	 * @param title The name of the header
	 */
	public NavDrawerItem( String title ) {
		this.title = title;
		isHeader = true;;
	}

	/**
	 * A simple list item in the NavDrawer
	 * @param title
	 * @param icon
	 */
	public NavDrawerItem( String title, int icon ) {
		this.title = title;
		this.icon = icon;
	}

	public NavDrawerItem( String title, int icon, boolean isCounterVisible, String count ) {
		this.title = title;
		this.icon = icon;
		this.isCounterVisible = isCounterVisible;
		this.count = count;
	}
	
	public boolean isHeader() {
		return isHeader;
	}

	public String getTitle() {
		return this.title;
	}

	public int getIcon() {
		return this.icon;
	}

	public String getCount() {
		return this.count;
	}

	public boolean getCounterVisibility() {
		return this.isCounterVisible;
	}

	public void setTitle( String title ) {
		this.title = title;
	}

	public void setIcon( int icon ) {
		this.icon = icon;
	}

	public void setCount( String count ) {
		this.count = count;
	}

	public void setCounterVisibility( boolean isCounterVisible ) {
		this.isCounterVisible = isCounterVisible;
	}
}
