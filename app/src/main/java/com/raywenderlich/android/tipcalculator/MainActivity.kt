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

class MainActivity : AppCompatActivity() {

  private var percent: Int = 15
  private var totalBill: Double = 0.00

  override fun onCreate(savedInstanceState: Bundle?) {
    // Switch to AppTheme for displaying the activity
    setTheme(R.style.AppTheme) // sets the view content and inflates

    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    billEditText.addTextChangedListener(object : TextWatcher {
      override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) { }

      override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) { }

      override fun afterTextChanged(s: Editable) {
        val currentBill = s.substring(1).toDoubleOrNull()
        currentBill?.let {
          recalculateWithUpdatedBill(it)
        }
      }
    })

    tipPercent.addTextChangedListener(object : TextWatcher {
      override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) { }

      override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) { }

      override fun afterTextChanged(s: Editable) {
        val tip = s.substring(1).toIntOrNull()
        tip?.let {
          recalculateWithUpdatedTip(it)
        }
      }
    })

    decrement.setOnClickListener { minusPercent() }
    increment.setOnClickListener { addPercent() }
  }

  private fun recalculateWithUpdatedTip(tipPercent: Int) {
    percent = tipPercent

    val tip = "${totalBill * (tipPercent/100.0)}%"
    tipAmount.text = tip

    val currentBill = billEditText.text.toString().toDoubleOrNull()
    currentBill?.let {
      val totalBill = "$${percent/100.0 + currentBill}"
      totalAmount.text = totalBill
    }
  }

  private fun recalculateWithUpdatedBill(bill: Double) {
    val tip= bill * (percent/100.0)
    totalBill = tip + bill

    val updatedBill = "$$totalBill"
    totalAmount.text = updatedBill
  }

  private fun addPercent() {
    if (percent < 100) recalculateWithUpdatedTip(++percent)
  }

  private fun minusPercent() {
    if (percent > 0) recalculateWithUpdatedTip(--percent)
  }
}
