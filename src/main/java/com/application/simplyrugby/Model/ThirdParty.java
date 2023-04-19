package com.application.simplyrugby.Model;

/**
 * Interface to describe a Next of  Kin or a Doctor
 */
public interface ThirdParty {
    /**
     * Insert a doctor or next of kin in the database
     *
     */
    abstract boolean saveContact();

    /**
     * Select a doctor or next of kin from the database
     * @return the selected member
     */
    abstract ThirdParty loadContact();
    /**
     * Select a doctor or next of kin from the database
     * @param index The primary key to look for.
     * @return the selected member
     */
    abstract ThirdParty loadContact(int index);
}
