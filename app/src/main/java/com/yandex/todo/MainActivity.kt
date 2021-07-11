package com.yandex.todo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}

//: AppCompatActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//        openFragment(HomeFragment())
//    }
//
//    fun openFragment(fragment: Fragment, arguments: Bundle? = null) {
//        fragment.arguments = arguments
//        supportFragmentManager
//            .beginTransaction()
//            .replace(R.id.fragment_container, fragment)
//            .commit()
//    }
//
//    fun openFragmentWithBackStack(fragment: Fragment, arguments: Bundle? = null) {
//        fragment.arguments = arguments
//        supportFragmentManager
//            .beginTransaction()
//            .addToBackStack(null)
//            .replace(R.id.fragment_container, fragment)
//            .commit()
//    }
//}