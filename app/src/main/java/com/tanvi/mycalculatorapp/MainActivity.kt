package com.tanvi.mycalculatorapp

import android.app.Activity
import android.app.AlertDialog
import android.os.Bundle
import android.text.method.KeyListener
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.text.NumberFormat

class MainActivity : AppCompatActivity() {
    private var txtAmount: EditText? = null
    private var txtPeople: EditText? = null
    private var txtTipOther: EditText? = null
    private var rdoGroupTips: RadioGroup? = null
    private var btnCalculate: Button? = null
    private var btnReset: Button? = null
    private var txtTipAmount: TextView? = null
    private var txtTotalToPay: TextView? = null
    private var txtTipPerPerson: TextView? = null
    private var radioCheckedId = -1
    private var mLogic: NumberPickerLogic? = null
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
        setListener()
    }

    private val mKeyListener =
        View.OnKeyListener { v, keyCode, event ->
            when (v.id) {
                R.id.txtAmount, R.id.txtPeople -> btnCalculate!!.isEnabled =
                    (txtAmount!!.text.length > 0
                            && txtPeople!!.text.length > 0)
                R.id.txtTipOther -> btnCalculate!!.isEnabled =
                    txtAmount!!.text.length > 0 && txtPeople!!.text.length > 0 && txtTipOther!!.text.length > 0
            }
            false
        }
    private val mClickListener =
        View.OnClickListener { v ->
            if (v.id == R.id.btnCalculate) {
                calculate()
            } else {
                reset()
            }
        }

    private fun initViews(){
        txtAmount = findViewById<View>(R.id.txtAmount) as EditText
        txtAmount!!.requestFocus()
        txtPeople = findViewById<View>(R.id.txtPeople) as EditText
        txtPeople!!.setText(Integer.toString(DEFAULT_NUM_PEOPLE))
        txtTipOther = findViewById<View>(R.id.txtTipOther) as EditText
        rdoGroupTips = findViewById<View>(R.id.RadioGroupTips) as RadioGroup
        btnCalculate = findViewById<View>(R.id.btnCalculate) as Button
        btnCalculate!!.isEnabled = true
        btnReset = findViewById<View>(R.id.btnReset) as Button
        txtTipAmount = findViewById<View>(R.id.txtTipAmount) as TextView
        txtTotalToPay = findViewById<View>(R.id.txtTotalToPay) as TextView
        txtTipPerPerson = findViewById<View>(R.id.txtTipPerPerson) as TextView
        txtTipOther!!.isEnabled = false
    }
    private  fun setListener(){

        rdoGroupTips!!.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId == R.id.radioFifteen
                || checkedId == R.id.radioTwenty
            ) {
                txtTipOther!!.isEnabled = false
                btnCalculate!!.isEnabled = (txtAmount!!.text.length > 0
                        && txtPeople!!.text.length > 0)
            }
            if (checkedId == R.id.radioOther) {
                txtTipOther!!.isEnabled = true
                // txtTipOther!!.requestFocus()
               // btnCalculate!!.isEnabled = txtAmount!!.text.length > 0 && txtPeople!!.text.length > 0 && txtTipOther!!.text.length > 0
            }
            radioCheckedId = checkedId
        }
        txtAmount!!.setOnKeyListener(mKeyListener)
        txtPeople!!.setOnKeyListener(mKeyListener)
        txtTipOther!!.setOnKeyListener(mKeyListener)
        btnCalculate!!.setOnClickListener(mClickListener)
        btnReset!!.setOnClickListener(mClickListener)
        mLogic = NumberPickerLogic(txtPeople!!, 1, Int.MAX_VALUE)
    }
    private fun reset() {
        txtTipAmount!!.text = ""
        txtTotalToPay!!.text = ""
        txtTipPerPerson!!.text = ""
        txtAmount!!.setText("")
        txtPeople!!.setText("")
        txtTipOther!!.setText("")
        rdoGroupTips!!.clearCheck()
       // rdoGroupTips!!.check(R.id.radioFifteen)
        txtAmount!!.requestFocus()
    }

    private fun calculate() {
        val billAmount =
            txtAmount!!.text.toString().toDouble()
        val totalPeople =
            txtPeople!!.text.toString().toDouble()
        var percentage: Double? = null
        var isError = false
        if (billAmount < 1.0) {
            showErrorAlert(
                "Enter a valid Total Amount.",
                txtAmount!!.id
            )
            isError = true
        }
        if (totalPeople < 1.0) {
            showErrorAlert(
                "Enter a valid number of people.",
                txtPeople!!.id
            )
            isError = true
        }
        if (radioCheckedId == -1) {
            radioCheckedId = rdoGroupTips!!.checkedRadioButtonId
        }
        if (radioCheckedId == R.id.radioFifteen) {
            percentage = 15.00
        } else if (radioCheckedId == R.id.radioTwenty) {
            percentage = 20.00
        } else if (radioCheckedId == R.id.radioOther) {
            percentage = txtTipOther!!.text.toString().toDouble()
            if (percentage < 1.0) {
                showErrorAlert(
                    "Enter a valid Tip percentage",
                    txtTipOther!!.id
                )
                isError = true
            }
        }
         if (!isError) {
            val tipAmount = billAmount * percentage!! / 100
            val totalToPay = billAmount + tipAmount
            val perPersonPays = totalToPay / totalPeople
            txtTipAmount!!.text = formatter.format(tipAmount)
            txtTotalToPay!!.text = formatter.format(totalToPay)
            txtTipPerPerson!!.text = formatter.format(perPersonPays)
        }
    }
    private fun showErrorAlert(errorMessage: String, fieldId: Int) {
        AlertDialog.Builder(this)
            .setTitle("Error")
            .setMessage(errorMessage)
            .setNeutralButton(
                "Close"
            ) { dialog, which -> findViewById<View>(fieldId).requestFocus() }.show()
    }

    companion object {
        const val DEFAULT_NUM_PEOPLE = 1
        val formatter = NumberFormat.getCurrencyInstance()
    }
}