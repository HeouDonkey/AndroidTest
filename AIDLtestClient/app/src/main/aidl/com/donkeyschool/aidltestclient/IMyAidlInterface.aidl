// IMyAidlInterface.aidl
package com.donkeyschool.aidltestclient;

// Declare any non-default types here with import statements

interface IMyAidlInterface {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    String getContacts(String name);

    int getPID();
}