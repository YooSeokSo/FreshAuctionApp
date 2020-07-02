package com.example.freshauctionapp.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.example.freshauctionapp.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val controller = findNavController(R.id.navigation_host)
        /*Navigation을 세팅합니다. */
        NavigationUI.setupActionBarWithNavController(
            this,
            controller,
            /* Login 화면과 검색 화면에서 뒤로가기를 없앱니다.*/
            AppBarConfiguration.Builder(R.id.splashFragment, R.id.loginFragment, R.id.searchFragment, R.id.userFragment).build()
        )

        NavigationUI.setupWithNavController(
            bottom_navigation, controller
        )

        controller.addOnDestinationChangedListener { _, destination, _ ->
            /* Search나 Profile 화면이라면?*/
            if (arrayListOf(R.id.searchFragment, R.id.userFragment).contains(destination.id)) {
                bottom_navigation.visibility = View.VISIBLE
            } else {
                bottom_navigation.visibility = View.GONE
            }
        }
    }

    //    /* AppBar(툴바)에서 뒤로가기 버튼(<-) 눌렀을 때,
    //      뒤로 이동하려면 onSupportNavigateUp를 오버라이드하여 구현  */
    override fun onSupportNavigateUp() = findNavController(R.id.navigation_host).navigateUp()
}
