package com.example.cafein

import Pay
import android.app.FragmentManager
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.cafein.mypages.NoticeFragment
import com.example.cafein.mypages.OrderListFragment
import com.example.cafein.mypages.PointFragment
import com.example.cafein.mypages.SettingFragment
import com.example.cafein.navigation.*
import com.example.cafein.store.Store
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.bottom_navigation
import kotlinx.android.synthetic.main.main_home_page.*
import kotlinx.coroutines.internal.artificialFrame
import org.json.JSONObject
import retrofit2.http.Url


class MainHomePage : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {
    var homeFragment = HomeFragment()
    var regionSelectFragment = RegionSelectFragment()
    var myPageFragment = MyPageFragment()

    lateinit var getId: String
    lateinit var getEmail: String
    lateinit var getPassword: String
    var getPoint : Int = 0
    lateinit var getStoreId: String
    lateinit var getStoreUrl : String

    companion object {
        var orderTotalList = arrayListOf<String>()
        var menuJsonObject = JSONObject()
    }
    fun getOrderTotalList() = orderTotalList

    fun setOrderTotalList(otl: ArrayList<String>){
        orderTotalList = otl
    }
    fun setMenuJson(menuJson: JSONObject){
        menuJsonObject = menuJson
    }

    override fun onNavigationItemSelected(p0: MenuItem): Boolean {
        when (p0.itemId) {
            R.id.action_home -> {
                if(supportFragmentManager.backStackEntryCount != 0) {
                    supportFragmentManager.popBackStack(null,
                        FragmentManager.POP_BACK_STACK_INCLUSIVE
                    )
                }
                supportFragmentManager.beginTransaction().replace(R.id.main_content,homeFragment).commit()
                return true
            }
            R.id.action_region -> {
                if(supportFragmentManager.backStackEntryCount != 0) {
                    supportFragmentManager.popBackStack(null,
                        FragmentManager.POP_BACK_STACK_INCLUSIVE
                    )
                }
                supportFragmentManager.beginTransaction().replace(R.id.main_content,regionSelectFragment).commit()
                return true
            }
            R.id.action_mypage -> {
                val bundle = Bundle(4)
                bundle.putString("id", getId)
                bundle.putString("email", getEmail)
                bundle.putString("password", getPassword)
                bundle.putInt("point", getPoint)
                if(supportFragmentManager.backStackEntryCount != 0) {
                    supportFragmentManager.popBackStack(null,
                        FragmentManager.POP_BACK_STACK_INCLUSIVE
                    )
                }
                supportFragmentManager.beginTransaction().replace(R.id.main_content,myPageFragment).commit()
                myPageFragment.arguments = bundle
                return true
            }

        }
        return false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_home_page)

        getId = intent.getStringExtra("id")
        getEmail = intent.getStringExtra("email")
        getPassword = intent.getStringExtra("password")
        getPoint = intent.getIntExtra("point", 0)
        for (i in 0..7)
            orderTotalList.add("")

        floatingActionButton.setOnClickListener {
            changeFragmentSendOrderList()
        }

        if (intent.hasExtra("status")) {
            var status = intent.getExtras()!!.get("status")
            when (status) {
                0 -> supportFragmentManager.beginTransaction()
                    .replace(R.id.main_content, homeFragment).commit()
                1 -> supportFragmentManager.beginTransaction()
                    .replace(R.id.main_content, regionSelectFragment).commit()
                2 -> supportFragmentManager.beginTransaction()
                    .replace(R.id.main_content, myPageFragment).commit()
            }
        }

        backArrow.setOnClickListener {
            /*if(supportFragmentManager.backStackEntryCount != 0)
            super.onBackPressed()*/
            onBackPressed()
        }
        bottom_navigation.setOnNavigationItemSelectedListener(this)
        supportFragmentManager.beginTransaction().replace(R.id.main_content, homeFragment).commit()


    }

    fun replaceNoticeFragment() {
        val noticefragment =  NoticeFragment()
        supportFragmentManager.beginTransaction().replace(R.id.main_content, noticefragment).addToBackStack(null).commit()
    }
    fun replaceOrderListFragment() {
        val orderlistfragment =  OrderListFragment()
        val bundle = Bundle(1)
        bundle.putString("id", getId)
        supportFragmentManager.beginTransaction().replace(R.id.main_content, orderlistfragment).addToBackStack(null).commit()
        orderlistfragment.arguments = bundle
    }
    fun replaceSettingFragment() {
        val settingfragment =  SettingFragment()
        val bundle = Bundle(4)
        bundle.putString("id", getId)
        bundle.putString("email", getEmail)
        bundle.putString("password", getPassword)
        bundle.putInt("point", getPoint)
        supportFragmentManager.beginTransaction().replace(R.id.main_content, settingfragment).addToBackStack(null).commit()
        settingfragment.arguments = bundle
    }
    fun replacePointFragment() {
        val pointfragment = PointFragment()
        val bundle = Bundle(4)
        bundle.putString("id", getId)
        bundle.putString("email", getEmail)
        bundle.putString("password", getPassword)
        bundle.putInt("point", getPoint)
        supportFragmentManager.beginTransaction().replace(R.id.main_content, pointfragment).addToBackStack(null).commit()
        pointfragment.arguments = bundle
    }
    fun changeFragmentKeepShopping(){
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_content, homeFragment).addToBackStack(null).commit()
    }

    fun changeFragmentSendOrderList() {
        var shoppingBasket = ShoppingBasket()
        var bundle = Bundle()
        bundle.putStringArrayList("orderTotalList", orderTotalList)
        supportFragmentManager.beginTransaction().replace(R.id.main_content, shoppingBasket).addToBackStack(null).commit()
        shoppingBasket.arguments = bundle

    }

    fun changeFragmentBeverage(menuJson: JSONObject) {
        val pay = Pay()
        val bundle = Bundle()
        menuJsonObject = menuJson
        bundle.putString("clientId", getId)
        bundle.putString("clientEmail", getEmail)
        bundle.putString("storeId", menuJson.getString("storeId").toString())
        bundle.putString("menuId", menuJson.getString("id").toString())
        bundle.putString("menuName", menuJson.getString("name").toString())
        bundle.putString("size", menuJson.getString("size").toString())
        bundle.putString("price", menuJson.getString("price").toString())
        bundle.putString("temp", menuJson.getString("temp").toString())
        bundle.putString("item", menuJson.getString("item").toString())
        bundle.putString("menuUrl", menuJson.getString("menuUrl"))
        supportFragmentManager.beginTransaction().replace(R.id.main_content, pay).addToBackStack(null)
            .commit()
        pay.arguments = bundle
    }

    fun changeFragment(store: Store) {
        val menuList = MenuList()
        val bundle = Bundle()
        bundle.putString("storeId", "${store.id}")
        getStoreId = "${store.id}"
        getStoreUrl = store.storeUrl
        menuList.setStoreUrl(store.storeUrl)
        supportFragmentManager.beginTransaction().replace(R.id.main_content, menuList).addToBackStack(null)
            .commit()
        menuList.arguments = bundle
    }

    fun changeFragmentToMenuList() {
        Log.e("fragmentStoreID", getStoreId)
        var menuList = MenuList()
        var bundle = Bundle()
        menuList.setStoreUrl(getStoreUrl)
        bundle.putString("storeId", getStoreId)
        supportFragmentManager.beginTransaction().replace(R.id.main_content, menuList).addToBackStack(null)
            .commit()
        menuList.arguments = bundle
    }

    fun replaceRegionFragment(city: City) {
        val regionfragment = RegionFragment()
        val bundle = Bundle(1)
        bundle.putInt("number", city.number)
        supportFragmentManager.beginTransaction().replace(R.id.main_content, regionfragment).addToBackStack(null)
            .commit()
        regionfragment.arguments = bundle
    }

    override fun onBackPressed() {
        if(supportFragmentManager.backStackEntryCount != 0){
            //Toast.makeText(this, supportFragmentManager.backStackEntryCount.toString(), Toast.LENGTH_LONG).show()
            supportFragmentManager.popBackStack()
            //super.onBackPressed()
        }

        else {
            val builder = AlertDialog.Builder(this)
            builder.setMessage("CafeIn을 종료하시겠습니까?")
            builder.setCancelable(true)
            builder.setNegativeButton("취소") { dialogInterface, i ->
                dialogInterface.cancel()
            }
            builder.setPositiveButton("종료") { dialogInterface, i ->
                moveTaskToBack(true)
                finish()
                android.os.Process.killProcess(android.os.Process.myPid())

            }
            val alertDialog = builder.create()
            alertDialog.show()
        }


    }

}


