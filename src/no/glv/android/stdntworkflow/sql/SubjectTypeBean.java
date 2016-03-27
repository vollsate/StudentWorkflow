package no.glv.android.stdntworkflow.sql;

import no.glv.android.stdntworkflow.intrfc.SubjectType;

public class SubjectTypeBean implements SubjectType {

    /**
     * The row in the database
     */
    int _id;

    private String mName;
    private String mDesc;
    private int mType;

    @Override
    public int getID() {
        return _id;
    }

    @Override
    public boolean isCustomSpecific() {
        if ( _id > mType )
            return true;

        return false;
    }

    @Override
    public boolean isSystemSpecific() {
        if ( _id < TYPE_CUSTOM )
            return true;

        return false;
    }

    @Override
    public String getName() {
        return mName;
    }

    @Override
    public String getDescription() {
        return mDesc;
    }

    @Override
    public int getType() {
        return mType;
    }

    @Override
    public void setName( String newName ) {
        mName = newName;
    }

    @Override
    public void setDescription( String newDesc ) {
        mDesc = newDesc;
    }

    @Override
    public void setType( int type ) {
        mType = type;
    }

}
