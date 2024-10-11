package com.zdy.wallpaperinstallapp.ActivityExtensions

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

open class ActivityFragment : AppCompatActivity() {

    protected fun SwitchFragment(placeHolder : Int, fragment: Fragment){

        supportFragmentManager
            .beginTransaction()
            .replace(placeHolder,fragment)
            .commit()

    }

    protected fun AddFragment(placeHolder : Int, fragment: Fragment){
        supportFragmentManager
            .beginTransaction()
            .add(placeHolder,fragment)
            .addToBackStack(fragment.javaClass.simpleName)
            .commit()
    }


    protected fun SwitchFragmentWithResult(placeHolder : Int, fragment: Fragment, onResult : (Int) -> Unit){


        supportFragmentManager
            .beginTransaction()
            .replace(placeHolder,fragment).commit()


    }

}