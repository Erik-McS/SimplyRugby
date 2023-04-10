package com.application.simplyrugby.Control;

public interface ThirdParty {
    /**
     * Insert a doctor or next of kin in the database
     * @param person Doctor or NoK to insert in database
     */
    abstract void saveContact(ThirdParty person);

    /**
     * Select a doctor or next of kin from the database
     * @return the selected member
     */
    abstract ThirdParty loadContact();
}
