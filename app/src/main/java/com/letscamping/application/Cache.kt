package com.letscamping.application

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Resources
import com.letscamping.model.LoginInfo

class Cache {

    private final var mContext:Context = TODO()
    private var mResources:Resources
        get() { TODO() }
        set(value) {}
    private var mPreferences:SharedPreferences
        get() { TODO() }
        set(value) {}

    private val login: LoginInfo? = null
    /*private val team: TeamInfo? = null
    private val team2: TeamInfo2? = null

    private val web: WebInfo? = null

    private val convlist: ConvList? = null*/

    constructor(context:Context){
        mContext = context
        mResources = mContext.resources
        mPreferences = mContext.getSharedPreferences("LetsCamping",Context.MODE_PRIVATE)
    }

}