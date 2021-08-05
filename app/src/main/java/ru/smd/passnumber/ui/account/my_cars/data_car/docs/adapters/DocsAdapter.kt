package ru.smd.passnumber.ui.account.my_cars.data_car.docs.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import ru.smd.passnumber.R
import ru.smd.passnumber.data.entities.*
import ru.smd.passnumber.databinding.*

class DocsAdapter(val onClick: OnClickListner) : RecyclerView.Adapter<DocsAdapter.ViewHolder>() {

    var items = mutableListOf<DocsWrapper>()

    interface OnClickListner {
        fun onClickAdd()
    }

    sealed class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        class AddViewHolder(val binding: ItemAddDocsBinding) : ViewHolder(binding.root)
        class LoadedViewHolder(val binding: ItemDocsBinding) : ViewHolder(binding.root)
    }

    override fun getItemViewType(position: Int) = items.get(position).typeItem.type

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = when (viewType) {
        TypeItem.Add.type -> {
            ViewHolder.AddViewHolder(
                ItemAddDocsBinding.inflate(
                    LayoutInflater.from(
                        parent.context
                    ), parent, false
                )
            )
        }
        else -> {
            ViewHolder.LoadedViewHolder(
                ItemDocsBinding.inflate(
                    LayoutInflater.from(
                        parent.context
                    ), parent, false
                )
            )

        }
    }


    fun setData(data: List<Docs>) {
        items.clear()
        var docsWrapper: DocsWrapper
        data.forEach {
            when (it.type.toInt()) {
                1 -> {
                    docsWrapper = DocsWrapper(TypeDocs.Pts, TypeItem.Loaded, it)
                    items.add(docsWrapper)
                }
                2 -> {
                    docsWrapper = DocsWrapper(TypeDocs.Sts, TypeItem.Loaded, it)
                    items.add(docsWrapper)
                }
                3 -> {
                    docsWrapper = DocsWrapper(TypeDocs.Dk, TypeItem.Loaded, it)
                    items.add(docsWrapper)
                }
                4 -> {
                    docsWrapper = DocsWrapper(TypeDocs.DriverCard, TypeItem.Loaded, it)
                    items.add(docsWrapper)
                }
                5 -> {
                    docsWrapper = DocsWrapper(TypeDocs.Passport, TypeItem.Loaded, it)
                    items.add(docsWrapper)
                }
                6 -> {
                    docsWrapper = DocsWrapper(TypeDocs.CardCompany, TypeItem.Loaded, it)
                    items.add(docsWrapper)
                }
                7 -> {
                    docsWrapper = DocsWrapper(TypeDocs.ContractCredit, TypeItem.Loaded, it)
                    items.add(docsWrapper)
                }
                8 -> {
                    docsWrapper = DocsWrapper(TypeDocs.ContractCarriage, TypeItem.Loaded, it)
                    items.add(docsWrapper)
                }
            }
        }
        items.add(DocsWrapper(TypeDocs.Pts, TypeItem.Add, null))
        items.add(DocsWrapper(TypeDocs.Sts, TypeItem.Add, null))
        items.add(DocsWrapper(TypeDocs.Dk, TypeItem.Add, null))
        items.add(DocsWrapper(TypeDocs.DriverCard, TypeItem.Add, null))
        items.add(DocsWrapper(TypeDocs.Passport, TypeItem.Add, null))
        items.add(DocsWrapper(TypeDocs.CardCompany, TypeItem.Add, null))
        items.add(DocsWrapper(TypeDocs.ContractCredit, TypeItem.Add, null))
        items.add(DocsWrapper(TypeDocs.ContractCarriage, TypeItem.Add, null))
        items.sortBy { it.type }
        notifyDataSetChanged()
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var data = items.get(position)
        when (getItemViewType(position)) {
            TypeItem.Add.type -> {
                (holder as ViewHolder.AddViewHolder).run {
                    binding.run {
                        if (position > 0 && data.type == items.get(position - 1).type) {
                            txtDocsType.visibility = View.INVISIBLE
                        } else{
                            txtDocsType.visibility = View.VISIBLE
                            txtDocsType.setText(data.docs?.id?.toInt()?.let { setNameType(it,itemView.context) })
                        }
                        txtDocs.setOnClickListener { (onClick.onClickAdd()) }
                    }
                }
            }
            else -> {
                (holder as ViewHolder.LoadedViewHolder).run {
                    binding.run {
                        if (position > 0 && data.type == items.get(position - 1).type) {
                            txtDocsType.visibility = View.INVISIBLE
                        } else{
                            txtDocsType.visibility = View.VISIBLE
                            txtDocsType.setText(data.docs?.id?.toInt()?.let { setNameType(it,itemView.context) })
                        }
                        when (data.docs?.status?.toInt()) {
                            1 -> imgAccepted.visibility = View.GONE
                            2 -> {
                                imgAccepted.visibility = View.VISIBLE
                                imgAccepted.setImageDrawable(
                                    ContextCompat.getDrawable(
                                        itemView.context,
                                        R.drawable.ic_accepted
                                    )
                                )
                            }
                            3 -> {
                                imgAccepted.visibility = View.VISIBLE
                                imgAccepted.setImageDrawable(
                                    ContextCompat.getDrawable(
                                        itemView.context,
                                        R.drawable.ic_canceled
                                    )
                                )
                            }
                        }
                        if (data.docs?.thumb != null) {
                            txtDocs.visibility = View.GONE
                            mainImg.visibility=View.VISIBLE
                            Picasso.get().load(data.docs!!.thumb).resize(135,112).into(mainImg)
                        } else {
                            txtDocs.visibility = View.VISIBLE
                            mainImg.visibility=View.GONE
                            txtDocs.setText(data.docs?.file)
                        }

                    }
                }
            }
        }
    }

    fun setNameType(type:Int,contex:Context):String{
        when(type){
            1->return contex.getString(R.string.pts)
            2->return contex.getString(R.string.sts)
            3->return contex.getString(R.string.dk)
            4->return contex.getString(R.string.driver_card)
            5->return contex.getString(R.string.passport)
            6->return contex.getString(R.string.card_company)
            7->return contex.getString(R.string.contract_credit)
            else->return contex.getString(R.string.contrsct_carriage)
        }
    }
}