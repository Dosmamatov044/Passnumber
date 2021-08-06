package ru.smd.passnumber.ui.account.my_cars.data_car.docs.adapters

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import ru.smd.passnumber.R
import ru.smd.passnumber.data.entities.Docs
import ru.smd.passnumber.data.entities.DocsWrapper
import ru.smd.passnumber.data.entities.TypeItem
import ru.smd.passnumber.databinding.ItemAddDocsBinding
import ru.smd.passnumber.databinding.ItemDocsBinding


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
            docsWrapper= DocsWrapper(TypeItem.Loaded, it)
            items.add(docsWrapper)
        }
        items.add(DocsWrapper(TypeItem.Add, null))
        notifyDataSetChanged()
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = items.get(position)
        when (getItemViewType(position)) {
            TypeItem.Add.type -> {
                (holder as ViewHolder.AddViewHolder).run {
                    binding.run {

                    }
                }
            }
            else -> {
                (holder as ViewHolder.LoadedViewHolder).run {
                    binding.run {

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
                            Picasso.get().load(data.docs.thumb).resize(135, 112).into(mainImg)
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

}