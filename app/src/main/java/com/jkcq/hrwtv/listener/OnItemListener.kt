package com.exercise.ems.listener


interface OnItemClickListener<T> {
    fun onItem(t: T, position: Int)
}