package ru.smd.passnumber.ui.account.my_cars.data_car

import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_data_car.*
import ru.smd.passnumber.R
import ru.smd.passnumber.data.core.hideKeyboard
import ru.smd.passnumber.data.entities.PassData
import ru.smd.passnumber.databinding.FragmentDataCarBinding
import ru.smd.passnumber.databinding.FragmentMyCarsBinding
import ru.smd.passnumber.ui.account.my_cars.data_car.docs.DocsFragment
import ru.smd.passnumber.ui.checking_pass.CheckingPassFragment
import ru.smd.passnumber.ui.help_registration.HelpRegistrationFragment
import javax.inject.Inject

@AndroidEntryPoint
class DataCarFragment : Fragment(), DataCarContract.View {

    @Inject
    lateinit var presenter: DataCarContract.Presenter

    lateinit var binding: FragmentDataCarBinding

    private var regNumber:String?=null

    override fun onStart() {
        super.onStart()
        presenter.onStart(this)
        regNumber?.let { presenter.getData(it) }
    }

    override fun onStop() {
        super.onStop()
        presenter.onStop()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        regNumber = arguments?.getString("reg_number")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentDataCarBinding.inflate(inflater).run {
        binding = this
        root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.run {
            dataCar.isEnabled=false
            passesCar.isEnabled=false
            notificationsCar.isEnabled=false
            docsCar.isEnabled=false
            tvTitle.setText(regNumber)
            passesCar.setOnClickListener { regNumber?.let { it1 -> presenter.getPasses(it1) } }
            recommendation.text= Html.fromHtml(requireContext().getString(R.string.add_docs_car))
            btnBackMyDataCar.setOnClickListener { presenter.onClickBack() }
            dataCar.setOnClickListener { presenter.onClickDataCar() }
            docsCar.setOnClickListener { presenter.onClickDocs() }
        }
    }

    override fun toBack() {
        binding.run {
            if (mainContDataCar.visibility == View.GONE) {
                mainContDataCar.visibility = View.VISIBLE
                contDataCar.visibility = View.GONE
                tvTitle.setText(regNumber)
                regNumber?.let { presenter.getData(it) }
            } else requireActivity().onBackPressed()
        }
    }

    override fun exit() {
        activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()
    }

    override fun showPasses(passData: PassData) {
        parentFragmentManager.beginTransaction()
            .replace(R.id.mainContainer, CheckingPassFragment().apply {
                data.value = passData
            }).addToBackStack(null).commit()
    }

    override fun showDataCar() {
        binding.run {
            mainContDataCar.visibility = View.GONE
            contDataCar.visibility = View.VISIBLE
            tvTitle.setText(getString(R.string.data_car))
            btnSaveDataCar.setText(getString(R.string.save))
            btnSaveDataCar.setOnClickListener {
                presenter.onClickSaveData(
                    edtLabelModelDataCar.text.toString(),
                    edtNameDriverDataCar.text.toString(),
                    edtRegNumberDataCar.text.toString()
                )
            }
        }
    }

    override fun showData(mark: String, driverName: String, regNumber: String) {
        binding.run{
            dataCar.isEnabled=true
            passesCar.isEnabled=true
            notificationsCar.isEnabled=true
            docsCar.isEnabled=true
            edtLabelModelDataCar.setText(mark)
            edtNameDriverDataCar.setText(driverName)
            edtRegNumberDataCar.isEnabled=false
            edtRegNumberDataCar.setText(regNumber)
            txtDataCarDriverName.setText(driverName)
            if (!mark.isEmpty()){
                txtDataCarMark.visibility=View.VISIBLE
            }else txtDataCarMark.visibility=View.GONE
            txtDataCarMark.setText(mark)
        }
    }

    override fun showDocs(idVehicle: Int) {
        parentFragmentManager.beginTransaction()
            .replace(R.id.mainContainer, DocsFragment.create(idVehicle)).addToBackStack(null).commit()
    }

    companion object {
        fun create(regNumber: String) = DataCarFragment().apply {
            arguments = Bundle().apply {
                putString("reg_number", regNumber)
            }
        }
    }
}