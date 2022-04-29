package com.el3asas.eduapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import com.el3asas.eduapp.R
import com.el3asas.eduapp.databinding.FragmentMainAzkarrListBinding
import com.el3asas.eduapp.models.MainAzkarItem
import com.el3asas.eduapp.ui.MainAzkarrFragmentDirections.ActionMainAzkarrFragmentToListedAzkarFragment
import com.el3asas.eduapp.ui.adapters.MainAzkarAdapter
import com.el3asas.eduapp.ui.adapters.MainAzkarAdapter.CardListener
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class MainAzkarrFragment : BottomSheetDialogFragment(), CardListener {
    private var binding: FragmentMainAzkarrListBinding? = null
    private lateinit var azkarItems: MutableList<MainAzkarItem>.
    private var action: ActionMainAzkarrFragmentToListedAzkarFragment? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainAzkarrListBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        azkarItems = ArrayList()
        azkarItems.add(MainAzkarItem(R.drawable.ic_shrouq, "اذكار الصباح"))
        azkarItems.add(MainAzkarItem(R.drawable.ic_maghrib, "اذكار المساء"))
        azkarItems.add(MainAzkarItem(R.drawable.ic_pray, "اذكار الصلاه"))
        azkarItems.add(MainAzkarItem(R.drawable.daily_ic, "اذكار يوميه"))
        azkarItems.add(MainAzkarItem(R.drawable.ic_rip, "الجنازه"))
        azkarItems.add(MainAzkarItem(R.drawable.ic_roqia, "الرقيه الشرعيه"))
        val adapter = MainAzkarAdapter(this, azkarItems)
        binding!!.list.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun CardClickListener(position: Int) {
        when (position) {
            0 -> {
                action = MainAzkarrFragmentDirections.actionMainAzkarrFragmentToListedAzkarFragment(
                    arrayOf("أذكار الصباح")
                )
                action!!.categoryTitle = azkarItems!![position].azkarName
                NavHostFragment.findNavController(requireParentFragment()).navigate(action!!)
            }
            1 -> {
                action = MainAzkarrFragmentDirections.actionMainAzkarrFragmentToListedAzkarFragment(
                    arrayOf("أذكار المساء")
                )
                action!!.categoryTitle = azkarItems!![position].azkarName
                NavHostFragment.findNavController(requireParentFragment()).navigate(action!!)
            }
            2 -> {
                action = MainAzkarrFragmentDirections.actionMainAzkarrFragmentToListedAzkarFragment(
                    arrayOf(
                        "أذكار الآذان",
                        "دعاء الذهاب إلى المسجد",
                        "دعاء دخول المسجد",
                        "الذكر قبل الوضوء",
                        "الذكر بعد الفراغ من الوضوء",
                        "الأذكار بعد السلام من الصلاة",
                        "الاستغفار و التوبة",
                        "التسبيح، التحميد، التهليل، التكبير",
                        "الدعاء بعد التشهد الأخير قبل السلام",
                        "الصلاة على النبي بعد التشهد",
                        "دعاء الجلسة بين السجدتين",
                        "دعاء الرفع من الركوع",
                        "دعاء الركوع",
                        "دعاء السجود",
                        "دعاء الخروج من المسجد",
                        "دعاء الوسوسة في الصلاة و القراءة",
                        "دعاء صلاة الاستخارة",
                        "كيف كان النبي يسبح؟",
                        "الدعاء للفرط في الصلاة عليه",
                        "دعاء سجود التلاوة",
                        "دعاء قنوت الوتر"
                    )
                )
                action!!.categoryTitle = azkarItems!![position].azkarName
                NavHostFragment.findNavController(requireParentFragment()).navigate(action!!)
            }
            3 -> {
                action = MainAzkarrFragmentDirections.actionMainAzkarrFragmentToListedAzkarFragment(
                    arrayOf("دعاء الاستفتاح")
                )
                action!!.categoryTitle = azkarItems!![position].azkarName
                NavHostFragment.findNavController(requireParentFragment()).navigate(action!!)
            }
            4 -> {
                action = MainAzkarrFragmentDirections.actionMainAzkarrFragmentToListedAzkarFragment(
                    arrayOf(
                        "تلقين المحتضر",
                        "الدعاء عند إغماض الميت",
                        "الدعاء للميت في الصلاة عليه",
                        "الدعاء بعد دفن الميت",
                        "الدعاء عند إدخال الميت القبر",
                        "دعاء التعزية"
                    )
                )
                action!!.categoryTitle = azkarItems!![position].azkarName
                NavHostFragment.findNavController(requireParentFragment()).navigate(action!!)
            }
            5 -> {
                action = MainAzkarrFragmentDirections.actionMainAzkarrFragmentToListedAzkarFragment(
                    arrayOf(
                        "الرُّقية الشرعية من القرآن الكريم",
                        "الرُّقية الشرعية من السنة النبوية",
                        "ما يعوذ به الأولاد - رقية"
                    )
                )
                action!!.categoryTitle = azkarItems!![position].azkarName
                NavHostFragment.findNavController(requireParentFragment()).navigate(action!!)
            }
        }
    }
}