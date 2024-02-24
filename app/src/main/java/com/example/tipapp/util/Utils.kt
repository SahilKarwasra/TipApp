package com.example.tipapp.util

fun calculateTotalTip(totalBill: Double, tipPercentage: Int): Double {
    return if (totalBill > 1 && totalBill.toString().isNotEmpty())
        (totalBill * tipPercentage) / 100
    else 0.0

}

fun calculateTotalBillAmount(
    totalBill: Double,
    splitBy: Int,
    tipPercentage: Int
): Double {
    val Bill = calculateTotalTip(totalBill = totalBill, tipPercentage = tipPercentage) + totalBill
    return (Bill/splitBy)
}
