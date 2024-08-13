package com.umesh.androidassignment

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding


open class BaseAdapter<T>: RecyclerView.Adapter<BaseViewHolder<T>>(){
    var listOfItems:MutableList<T>? = mutableListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    open var expressionViewHolderBinding: ((T, ViewBinding) -> Unit)? = null
    open var expressionOnCreateViewHolder:((ViewGroup)->ViewBinding)? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<T> {
        return expressionOnCreateViewHolder?.let {
            it(parent)
        }?.let {
            BaseViewHolder(it, expressionViewHolderBinding!!)
        }!!
    }

    override fun onBindViewHolder(holder: BaseViewHolder<T>, position: Int) {
        holder.bind(listOfItems!![position])
    }

    override fun getItemCount(): Int {
        return listOfItems!!.size
    }

    fun addItem(newItem: T) {
        listOfItems?.add(newItem)
        notifyItemInserted(listOfItems?.size?.minus(1) ?: 0)
    }

}


class BaseViewHolder<T> internal constructor(private val binding:ViewBinding,
                                             private val expression:(T,ViewBinding)->Unit)
    :RecyclerView.ViewHolder(binding.root){
    fun bind(item:T){
        expression(item,binding)
    }
}