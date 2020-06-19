package com.example.cafein.mypages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cafein.R
import com.example.cafein.store.Notice
import com.example.cafein.store.NoticeAdapter

class NoticeFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = LayoutInflater.from(activity).inflate(R.layout.notice_page, container, false)
        val notices = arrayListOf<Notice>()

        notices.add(
            Notice(
                1, "CafeIn App이 출시되었습니다\n",
                "CafeIn에서 커피를 미리 주문하시면 \n매장에서 커피를 바로 받아가실 수 있습니다!\n" +
                        "그리고 CafeIn에서 커피를 주문하시면\n마일리지가 적립됩니다.\n" +
                        "CafeIn 많이 이용해주세요!\n", "2020.06.06"
            )
        )
        notices.add(
            Notice(
                2, "스마트 오더 기능 출시\n",
                "한성대학교 온라인 발표회 행사가 6/19에 열리게 되었습니다.\n" +
                        "학생들이 열심히 준비한 캡스톤디자인 발표회 많은 참여 부탁드립니다.\n", "2020.06.07"
            )
        )
        notices.add(
            Notice(
                3, "앱 점검 공지사항",
                "서시\n" +
                        "\n" +
                        "죽는 날까지 하늘을 우러러\n" +
                        "한점 부끄럼이 없기를,\n" +
                        "잎새에 이는 바람에도\n" +
                        "나는 괴로워했다.\n" +
                        "별을 노래하는 마음으로\n" +
                        "모든 죽어가는 것을 사랑해야지\n" +
                        "그리고 나한테 주어진 길을\n" +
                        "걸어가야겠다.\n" +
                        "\n" +
                        "오늘밤에도 별이 바람에 스치운다.\n", "2020.06.08"
            )
        )

        notices.add(
            Notice(
                4, "신규 회원 이벤트",
                "서시\n" +
                        "\n" +
                        "죽는 날까지 하늘을 우러러\n" +
                        "한점 부끄럼이 없기를,\n" +
                        "잎새에 이는 바람에도\n" +
                        "나는 괴로워했다.\n" +
                        "별을 노래하는 마음으로\n" +
                        "모든 죽어가는 것을 사랑해야지\n" +
                        "그리고 나한테 주어진 길을\n" +
                        "걸어가야겠다.\n" +
                        "\n" +
                        "오늘밤에도 별이 바람에 스치운다.\n", "2020.06.08"
            )
        )

        var noticeRecyclerView = view?.findViewById<RecyclerView>(R.id.notice_recyclerView)
        if (noticeRecyclerView != null) {
            noticeRecyclerView.layoutManager = LinearLayoutManager(this.context)
        }
        if (noticeRecyclerView != null) {
            noticeRecyclerView.adapter = NoticeAdapter(notices)
            { Notice ->
                //onclickEvent
                Toast.makeText(this.context, "$Notice", Toast.LENGTH_LONG).show()

            }
        }
        return view
    }

}