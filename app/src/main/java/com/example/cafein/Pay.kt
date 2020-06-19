import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.cafein.*
import kotlinx.android.synthetic.main.pay_layout.*
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*
import kotlin.collections.ArrayList

class Pay : Fragment() {
    companion object {
        var clientId = ""
        var clientEmail = ""
        var storeId: String = ""
        var menuId: String = ""
        var menuName: String = ""
        var tall: String = ""
        var tallPrice: String = ""
        var ventie: String = ""
        var ventiePrice: String = ""
        var grande: String = ""
        var grandePrice: String = ""
        var ice: String = ""
        var hot: String = ""
        var temp: String = ""
        var sizes: String = ""
        var prices: String = ""
        var menuUrl: String = ""
        var item: String = ""
        var choiceTemp = "ICE"
        var buyJson = JSONObject()
    }

    fun getBuyJson() = buyJson

    fun setVars() {
        val argument = arguments
        clientId = argument!!.getString("clientId")
        clientEmail = argument!!.getString("clientEmail")
        storeId = argument!!.getString("storeId")
        menuId = argument!!.getString("menuId")
        menuName = argument!!.getString("menuName")
        sizes = argument!!.getString("size")
        prices = argument!!.getString("price")
        menuUrl = argument!!.getString("menuUrl")
        temp = argument!!.getString("temp")
        item = argument!!.getString("item")
        var tempList = temp.split(",")
        var menuSizeList = sizes.split(",")
        var menuPriceList = prices.split(",")
        var sizeSize = menuSizeList.size

        when (tempList.size) {
            0 -> {
                check_ice.visibility = View.GONE
                check_hot.visibility = View.GONE
            }
            1 -> {
                if (tempList[0] == "ice")
                    ice = "ice"
                else
                    hot = "hot"
            }
            2 -> {
                ice = "ice"
                hot = "hot"
            }
        }

        when (sizeSize) {
            1 -> {
                tall = menuSizeList[0]
                tallPrice = menuPriceList[0]
            }
            2 -> {
                tall = menuSizeList[0]
                tallPrice = menuPriceList[0]
                grande = menuSizeList[1]
                grandePrice = menuPriceList[1]
            }
            3 -> {
                tall = menuSizeList[0]
                tallPrice = menuPriceList[0]
                grande = menuSizeList[1]
                grandePrice = menuPriceList[1]
                ventie = menuSizeList[2]
                ventiePrice = menuPriceList[2]
            }
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.pay_layout, container, false)
        val menuImage = view.findViewById<ImageView>(R.id.menu_image)
        val check_tall = view.findViewById<TextView>(R.id.check_tall)
        val check_venti = view.findViewById<TextView>(R.id.check_venti)
        val check_grande = view.findViewById<TextView>(R.id.check_grande)
        val total = view.findViewById<Button>(R.id.total)
        val shopping = view.findViewById<Button>(R.id.shopping)
        val menu_amout = view.findViewById<TextView>(R.id.menu_amount)
        var choiceTotalPrice = 0
        var amount = 1
        var orderSize = "T"

        setVars()
        var choicePrice = tallPrice.toInt()

        view.findViewById<TextView>(R.id.price).text = tallPrice
        menu_amout.text = amount.toString()
        Glide.with(this).load(menuUrl).into(menuImage)
        view.findViewById<TextView>(R.id.menu_name).text = menuName

        if (ice != "" && hot != "") {
            view.findViewById<TextView>(R.id.icehot).visibility = View.VISIBLE
            view.findViewById<RadioButton>(R.id.etc).visibility = View.GONE

            val check_ice = view.findViewById<RadioButton>(R.id.check_ice)
            check_ice.visibility = View.VISIBLE
            check_ice.setOnClickListener {
                choiceTemp = "ICE"
            }
            val check_hot = view.findViewById<RadioButton>(R.id.check_hot)
            check_hot.visibility = View.VISIBLE
            check_hot.setOnClickListener {
                choiceTemp = "HOT"
            }

        }
        if (sizes != "etc") {
            view.findViewById<TextView>(R.id.size).visibility = View.VISIBLE
            view.findViewById<RadioGroup>(R.id.check_size).visibility = View.VISIBLE
            view.findViewById<View>(R.id.viewLine).visibility = View.VISIBLE
            view.findViewById<TextView>(R.id.icehot).visibility = View.VISIBLE

            if (tall != "") {
                check_tall.text = "TALL" + ":   " + tallPrice
                check_tall.visibility = View.VISIBLE
                check_tall.setOnClickListener {
                    choicePrice = tallPrice.toInt()
                    choiceTotalPrice = choicePrice * amount
                    view.findViewById<TextView>(R.id.price).text = choiceTotalPrice.toString()
                    orderSize = "T"
                }
            }
            if (ventie != "") {
                check_venti.text = "VENTI" + ":   " + ventiePrice
                check_venti.visibility = View.VISIBLE
                check_venti.setOnClickListener {
                    choicePrice = ventiePrice.toInt()
                    choiceTotalPrice = choicePrice * amount
                    view.findViewById<TextView>(R.id.price).text = choiceTotalPrice.toString()
                    orderSize = "V"

                }
            }
            if (grande != "") {
                check_grande.text = "GRANDE" + ":   " + grandePrice
                check_grande.visibility = View.VISIBLE
                check_grande.setOnClickListener {
                    choicePrice = grandePrice.toInt()
                    choiceTotalPrice = choicePrice * amount
                    view.findViewById<TextView>(R.id.price).text = choiceTotalPrice.toString()
                    orderSize = "G"

                }
            }
        } else {
            view.findViewById<TextView>(R.id.icehot).visibility = View.GONE
            view.findViewById<TextView>(R.id.size).visibility = View.GONE
            view.findViewById<View>(R.id.viewLine).visibility = View.GONE
            view.findViewById<RadioButton>(R.id.etc).visibility = View.VISIBLE
            view.findViewById<RadioButton>(R.id.etc).isChecked = true
            view.findViewById<RadioButton>(R.id.etc).text = "PRICE" + ":   " + prices
            view.findViewById<RadioGroup>(R.id.check_temp).visibility = View.GONE
            choiceTemp = ""
            orderSize = ""
        }
        val minus = view.findViewById<Button>(R.id.minus)
        minus.setOnClickListener {

            if (amount != 0) {
                amount -= 1
                menu_amout.text = amount.toString()
                choiceTotalPrice = choicePrice * amount
                view.findViewById<TextView>(R.id.price).text = choiceTotalPrice.toString()

            }
        }
        val plus = view.findViewById<Button>(R.id.plus)
        plus.setOnClickListener {

            amount += 1
            menu_amout.text = amount.toString()
            choiceTotalPrice = choicePrice * amount
            view.findViewById<TextView>(R.id.price).text = choiceTotalPrice.toString()

        }
        // clientId, storeId, orderList, orderDate, status, email, point(total), storeName

        var orderTotalList = MainHomePage.orderTotalList


        Log.e("orderTotalListSize", orderTotalList.size.toString())
        shopping.setOnClickListener {
            if(orderTotalList[1] == "") {
                orderTotalList[0] = clientId
                Log.e("clientID", clientId)
                orderTotalList[1] = storeId
                orderTotalList[2] = choiceTemp + " " + menuName + orderSize + "*" + amount.toString()
                orderTotalList[3] = SimpleDateFormat("yyyy.MM.dd HH:mm:ss").format(Calendar.getInstance().time)
                orderTotalList[4] = "0"
                orderTotalList[5] = clientEmail
                orderTotalList[6] = view.findViewById<TextView>(R.id.price).text.toString()
                orderTotalList[7] = MenuList().getStoreName()

                MainHomePage().setOrderTotalList(orderTotalList)
                (activity as MainHomePage).changeFragmentToMenuList()
                //Log.e("orderTotalList index 2", MainHomePage().orderTotalList[2])


            }
            //같은 가게에서 주문할 경우
            else if ((storeId == orderTotalList[1])) {
                orderTotalList[2] += "+" + choiceTemp + " " + menuName + orderSize + "*" + amount.toString()
                orderTotalList[6] = (orderTotalList[6].toInt() + view.findViewById<TextView>(R.id.price).text.toString().toInt()).toString()
                var toast = Toast.makeText(context, "장바구니에 메뉴가 담겼습니다.", Toast.LENGTH_SHORT)
                toast.setGravity(Gravity.BOTTOM, 0, 700)
                toast.show()
                MainHomePage().setOrderTotalList(orderTotalList)
                (activity as MainHomePage).changeFragmentToMenuList()
            }
            //다른 가게에서 주문할 경우
            else if (storeId != orderTotalList[1]) {
                /*var toast = Toast.makeText(requireContext(), "같은 가게의 메뉴만 장바구니에 넣을 수 있습니다.", Toast.LENGTH_SHORT)
                toast.setGravity(Gravity.BOTTOM, 0, 700)
                toast.show()*/
                val builder = AlertDialog.Builder(requireContext())
                builder.setMessage("장바구니에는 한 가게의 메뉴만 담을 수 있습니다.\n담으시겠습니까??")
                builder.setCancelable(true)
                builder.setNegativeButton("취소") { dialogInterface, i ->
                    dialogInterface.cancel()
                    (activity as MainHomePage).changeFragmentToMenuList()
                }
                builder.setPositiveButton("넣기") { dialogInterface, i ->
                    orderTotalList[0] = clientId
                    Log.e("clientID", clientId)
                    orderTotalList[1] = storeId
                    orderTotalList[2] = choiceTemp + menuName + orderSize + "*" + amount.toString()
                    orderTotalList[3] = ""
                    orderTotalList[4] = "0"
                    orderTotalList[5] = clientEmail
                    orderTotalList[6] = view.findViewById<TextView>(R.id.price).text.toString()
                    orderTotalList[7] = MenuList().getStoreName()
                    var toast = Toast.makeText(context, "장바구니에 메뉴가 담겼습니다.", Toast.LENGTH_SHORT)
                    toast.setGravity(Gravity.BOTTOM, 0, 700)
                    toast.show()
                    MainHomePage().setOrderTotalList(orderTotalList)
                    (activity as MainHomePage).changeFragmentToMenuList()
                }
                val alertDialog = builder.create()
                alertDialog.show()
            }



        }

        total.setOnClickListener {
            orderTotalList[0] = clientId
            Log.e("clientID", clientId)
            orderTotalList[1] = storeId
            orderTotalList[2] = choiceTemp + menuName + orderSize + "*" + amount.toString()
            orderTotalList[3] = ""
            orderTotalList[4] = "0"
            orderTotalList[5] = clientEmail
            orderTotalList[6] = view.findViewById<TextView>(R.id.price).text.toString()
            orderTotalList[7] = MenuList().getStoreName()

            MainHomePage().setOrderTotalList(orderTotalList)

            var intent = Intent(context, Last::class.java)
            intent.putStringArrayListExtra("orderTotalList", orderTotalList)
            startActivity(intent)
        }

        return view
    }


}