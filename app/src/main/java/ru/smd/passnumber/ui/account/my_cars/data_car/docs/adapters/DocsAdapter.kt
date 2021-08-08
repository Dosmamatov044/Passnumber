package ru.smd.passnumber.ui.account.my_cars.data_car.docs.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import ru.smd.passnumber.R
import ru.smd.passnumber.data.entities.Docs
import ru.smd.passnumber.data.entities.DocsWrapper
import ru.smd.passnumber.data.entities.Type
import ru.smd.passnumber.data.entities.TypeItem
import ru.smd.passnumber.databinding.ItemAddDocsBinding
import ru.smd.passnumber.databinding.ItemDocsBinding


class DocsAdapter(val onClick: OnClickListner) : RecyclerView.Adapter<DocsAdapter.ViewHolder>() {
    //для выбора чем открыввать документ
    enum class TypeOpen(val type: Int) {
        Photo(0), PDF(1), DOC(2),
    }
    var items = mutableListOf<DocsWrapper>()

    var type=0

    interface OnClickListner {

        fun onClickAdd(type:Int)
        fun onClickDelete()
        fun onClickOpen(url:String?, type: TypeOpen)


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
                    txtDocs.setOnClickListener { onClick.onClickAdd(type) }
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
                        var typeOpen:TypeOpen
                        if (data.docs?.thumb?.contains(".jpg", ignoreCase = true)==true
                            ||data.docs?.thumb?.contains(".png",ignoreCase = true)==true
                            ||data.docs?.thumb?.contains(".jpeg",ignoreCase = true)==true) {
                            txtDocs.visibility = View.GONE
                            mainImg.visibility=View.VISIBLE
                            Picasso.get().load(data.docs.thumb).into(mainImg)
                            typeOpen = TypeOpen.Photo
                        } else {
                            txtDocs.visibility = View.VISIBLE
                            mainImg.visibility=View.GONE
                            txtDocs.setText(itemView.context.getString(R.string.open))
                            typeOpen = TypeOpen.DOC
                            if (data.docs?.file?.contains(".PDF", ignoreCase = true) == true)
                                typeOpen = TypeOpen.PDF
                        }
                        //логика открытия файла через интент
                        mainContDocs.setOnClickListener { onClick.onClickOpen(data.docs?.file, typeOpen) }

                    }
                }
            }
        }
    }

}