package com.application.simplyrugby.Control;

/**
 * Interface for the club members.
 * @author Erik McSeveney
 */
public interface Member {

    /**
     * Insert a member in the database
     * @param member Member to insert
     */
    abstract void saveMember(Member member);

    /**
     * Select a member from the database
     * @return the selected member
     */
    abstract Member loadMember();


}
