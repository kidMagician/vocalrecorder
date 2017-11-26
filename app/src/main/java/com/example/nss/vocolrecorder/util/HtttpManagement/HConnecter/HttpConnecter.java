package com.example.nss.vocolrecorder.util.HtttpManagement.HConnecter;

import android.content.ContentValues;

/**
 * Created by NSS on 8/30/2017.
 */

public interface HttpConnecter {

    Object RequestPost(String urlStr, ContentValues params);
}
