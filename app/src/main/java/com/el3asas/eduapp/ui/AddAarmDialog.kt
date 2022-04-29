package com.el3asas.eduapp.ui

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.*
import com.el3asas.eduapp.R
import com.el3asas.eduapp.models.PrayItem
import java.util.*

class AddAarmDialog(context: Context) : Dialog(context), View.OnClickListener {
    val prayItem = PrayItem()
    private var inSecondView = false
    private lateinit var btn: Button
    private lateinit var h: NumberPicker
    private lateinit var m: NumberPicker
    private lateinit var am_pm: NumberPicker
    private lateinit var picker: LinearLayout
    var editText: EditText? = null
    override fun onCreate(savedInstanceState: Bundle) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.create_new_pray)
        editText = findViewById(R.id.value)
        btn = findViewById(R.id.next)
        h = findViewById(R.id.hours)
        m = findViewById(R.id.minutes)
        am_pm = findViewById(R.id.am_pm)
        h.setMaxValue(12)
        h.setMinValue(1)
        m.setMaxValue(59)
        m.setMinValue(0)
        am_pm.setMinValue(0)
        am_pm.setMaxValue(1)
        picker = findViewById(R.id.piker)
        am_pm.setFormatter(NumberPicker.Formatter { value: Int ->
            when (value) {
                0 -> return@Formatter "AM"
                1 -> return@Formatter "BM"
            }
            null
        })
        btn.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        if (!inSecondView) {
            if (editText!!.text.toString() == "") {
                Toast.makeText(
                    context,
                    context.resources.getString(R.string.aleartAboutAlarmName),
                    Toast.LENGTH_LONG
                ).show()
                return
            }
            prayItem.name = editText!!.text.toString()
            picker!!.visibility = View.VISIBLE
            editText!!.visibility = View.GONE
            btn!!.setText(R.string.creatAlarm)
            inSecondView = true
        } else {
            val c = Calendar.getInstance()
            c[Calendar.HOUR] = h!!.value
            c[Calendar.MINUTE] = m!!.value
            c[Calendar.AM_PM] = am_pm!!.value
            prayItem.calendar = c
            dismiss()
            Toast.makeText(context, "تم اضافه التنبيه", Toast.LENGTH_SHORT).show()
        }
    }
}