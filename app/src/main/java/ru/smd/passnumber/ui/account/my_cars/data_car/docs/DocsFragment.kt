package ru.smd.passnumber.ui.account.my_cars.data_car.docs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import ru.smd.passnumber.data.entities.Docs
import ru.smd.passnumber.databinding.FragmentDocsBinding
import ru.smd.passnumber.ui.account.my_cars.data_car.DataCarFragment
import ru.smd.passnumber.ui.account.my_cars.data_car.docs.adapters.DocsAdapter
import javax.inject.Inject

@AndroidEntryPoint
class DocsFragment:Fragment(),DocsContract.View,DocsAdapter.OnClickListner {

    @Inject
    lateinit var presenter:DocsContract.Presenter

    lateinit var binding: FragmentDocsBinding

    lateinit var adapter: DocsAdapter
    var idVehicle:Int = 0
    override fun onStart() {
        super.onStart()
        presenter.onStart(this)
        presenter.getDocs(idVehicle)
    }

    override fun onStop() {
        super.onStop()
        presenter.onStop()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter= DocsAdapter(this)
        idVehicle= arguments?.getInt("vehicle_id")!!
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    )=FragmentDocsBinding.inflate(inflater).run {
        binding = this
        root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.run {
            btnBackDocs.setOnClickListener { presenter.onClickBack() }
           recycleDocs.adapter=adapter
        }
    }


    override fun toBack() {
        requireActivity().onBackPressed()
    }

    override fun showDocs(docs: List<Docs>) {
        adapter.setData(docs)
    }

    override fun onClickAdd() {
     //   TODO("Not yet implemented")
    }

    companion object {
        fun create(id: Int) = DocsFragment().apply {
            arguments = Bundle().apply {
                putInt("vehicle_id", id)
            }
        }
    }

}