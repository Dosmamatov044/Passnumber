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

    lateinit var adapterSts: DocsAdapter
    lateinit var adapterPts: DocsAdapter
    lateinit var adapterDk: DocsAdapter
    lateinit var adapterDriverCard: DocsAdapter
    lateinit var adapterPassport: DocsAdapter
    lateinit var adapterCardCompany: DocsAdapter
    lateinit var adapterContractCredit: DocsAdapter
    lateinit var adapterContractCarriage: DocsAdapter
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
        adapterSts= DocsAdapter(this)
        adapterPts= DocsAdapter(this)
        adapterDk= DocsAdapter(this)
        adapterDriverCard= DocsAdapter(this)
        adapterPassport= DocsAdapter(this)
        adapterCardCompany= DocsAdapter(this)
        adapterContractCredit= DocsAdapter(this)
        adapterContractCarriage= DocsAdapter(this)
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
        }
    }


    override fun toBack() {
        requireActivity().onBackPressed()
    }

    override fun showDocsSts(docs: List<Docs>) {
        binding.recycleDocsSts.adapter=adapterSts
        adapterSts.setData(docs)
    }

    override fun showDocsPts(docs: List<Docs>) {
       binding.recycleDocsPts.adapter=adapterPts
        adapterPts.setData(docs)
    }

    override fun showDocsDk(docs: List<Docs>) {
        binding.recycleDocsDk.adapter=adapterDk
        adapterDk.setData(docs)
    }

    override fun showDocsDriverCard(docs: List<Docs>) {
        binding.recycleDocsDriverCard.adapter=adapterDriverCard
        adapterDriverCard.setData(docs)
    }

    override fun showDocsPassport(docs: List<Docs>) {
        binding.recycleDocsPassport.adapter=adapterPassport
        adapterPassport.setData(docs)
    }

    override fun showDocsCardCompany(docs: List<Docs>) {
        binding.recycleDocsCardCompany.adapter=adapterCardCompany
        adapterCardCompany.setData(docs)
    }

    override fun showDocsContractCredit(docs: List<Docs>) {
        binding.recycleDocsContractCredit.adapter=adapterContractCredit
        adapterContractCredit.setData(docs)
    }

    override fun showDocsContractCarriage(docs: List<Docs>) {
        binding.recycleDocsContractCarriage.adapter=adapterContractCarriage
        adapterContractCarriage.setData(docs)
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