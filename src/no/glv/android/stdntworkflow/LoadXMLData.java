package no.glv.android.stdntworkflow;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.LinkedList;
import java.util.List;

import no.glv.android.stdntworkflow.core.DataHandler;
import no.glv.android.stdntworkflow.core.SettingsManager;

import org.xmlpull.v1.XmlPullParser;

import android.os.AsyncTask;
import android.util.Log;
import android.util.Xml;

public class LoadXMLData extends AsyncTask<URL, Void, Void> {
    
    SettingsManager sManager;
    

    public LoadXMLData() {
	sManager = DataHandler.GetInstance().getSettingsManager();
    }
    
    private void readData() {
	try {
	    URL url = new URL( sManager.getXMLDataURL());
	    URLConnection con = url.openConnection();

	    String line;
	    StringBuffer sb = new StringBuffer();
	    BufferedReader reader = new BufferedReader( new InputStreamReader( con.getInputStream() ) );
	    while( ( line = reader.readLine() ) != null ) {
		sb.append( line );
	    }
	    
	    Log.d( "LoadXMLData", sb.toString() );
	}
	catch ( Exception e ) {
	    e.printStackTrace();
	}
    }


    @Override
    protected Void doInBackground( URL... params ) {
	readData();
	
	try {
	    XmlPullParser parser = Xml.newPullParser();
	    parser.setFeature( XmlPullParser.FEATURE_PROCESS_NAMESPACES, false );

	    URL url = new URL( sManager.getXMLDataURL());
	    URLConnection con = url.openConnection();
	    parser.setInput( con.getInputStream(), null );

	    parser.nextTag();
	    parseFeed( parser );
	}
	catch ( Exception e ) {
	    e.printStackTrace();
	    //Toast.makeText( sManager.getApplication(), e.toString(), Toast.LENGTH_LONG ).show();
	}
	
	return null;
    }
    
    

    /**
     * 
     * @param parser
     * @return
     * @throws Exception
     */
    private static List<XMLData> parseFeed( XmlPullParser parser ) throws Exception {
	LinkedList<XMLData> data = new LinkedList<XMLData>();
	
					// no name space
	parser.require( XmlPullParser.START_TAG, null, "swf" );
	while ( parser.next() != XmlPullParser.END_TAG ) {
	    if ( parser.getEventType() != XmlPullParser.START_TAG ) {
		continue;
	    }

	    String name = parser.getName();
	    if ( "class".equals( name ) ) {
		XMLData xml = new XMLData();
		xml.name = null;
		xml.url = null;

		data.add( xml );
	    }
	}

	return data;
    }

    /**
     * 
     * @author glevoll
     *
     */
    public static class XMLData {
	String name;
	String url;
    }

}
