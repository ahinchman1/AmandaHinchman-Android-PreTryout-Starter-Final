/*
 * Copyright (c) 2020 Razeware LLC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * Notwithstanding the foregoing, you may not use, copy, modify, merge, publish,
 * distribute, sublicense, create a derivative work, and/or sell copies of the
 * Software in any work that is designed, intended, or marketed for pedagogical or
 * instructional purposes related to programming, coding, application development,
 * or information technology.  Permission for such use, copying, modification,
 * merger, publication, distribution, sublicensing, creation of derivative works,
 * or sale is expressly withheld.
 *
 * This project and source code may use libraries or frameworks that are
 * released under various Open-Source licenses. Use of those libraries and
 * frameworks are governed by their own individual licenses.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.raywenderlich.android.tipcalculator

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.text.NumberFormat
import java.util.*
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity() {

  var percent: Int by Delegates.observable(initialValue = 15) { _, _, newValue ->
    if (newValue == 0) {
      tip_amount.text = getString(R.string.zero_percent)
      tip_edit_percent.hint = getString(R.string.zero_percent)
    } else {
      tip_edit_percent.hint = "$newValue%"
      recalculateWithUpdatedTip(newValue)
    }
  }

  var totalBill: Double by Delegates.observable(initialValue = 0.00) { _, _, newValue ->
    total_amount.text = billFormat.format(newValue)
  }

  private val locale = Locale.getDefault()
  private val currency = Currency.getInstance(locale)
  private val billFormat = NumberFormat.getCurrencyInstance(locale)

  override fun onCreate(savedInstanceState: Bundle?) {
    // Switch to AppTheme for displaying the activity
    setTheme(R.style.AppTheme)

    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main) // sets the view content and inflates

    bill_edit_text.addTextChangedListener(object : TextWatcher {
      var current = ""

      override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) { }

      override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        val stringText = s.toString()

        when {
          stringText.isEmpty() -> total_amount.text = getString(R.string.default_bill_amount)
          stringText != current -> {
            bill_edit_text.removeTextChangedListener(this)

            val currentBill = stringText.clean(currency).toDouble() / 100

            recalculateWithUpdatedBill(currentBill)
            current = billFormat.format(currentBill)

            bill_edit_text.apply {
              setText(current)
              setSelection(current.length)
            }

            bill_edit_text.addTextChangedListener(this)
          }
        }
      }

      override fun afterTextChanged(s: Editable) { }
    })

    tip_edit_percent.addTextChangedListener(TipPercentTextWatcher(percent))

    decrement.setOnClickListener { if (percent > 0) --percent }
    increment.setOnClickListener { if (percent < 100) ++percent }
  }

  private fun recalculateWithUpdatedTip(percent: Int) {
    val tip = calculateTip(totalBill, percent)
    val bill = bill_edit_text.text
    val currentBill = if (bill.isEmpty()) 0.00 else bill.substring(1).toDouble()
    totalBill = tip + currentBill

    tip_amount.text = billFormat.format(tip)
  }

  private fun recalculateWithUpdatedBill(bill: Double) {
    val tip= calculateTip(bill, percent)
    totalBill = tip + bill

    tip_amount.text = billFormat.format(tip)
  }
}
