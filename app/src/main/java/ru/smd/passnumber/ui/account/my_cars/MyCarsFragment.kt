package ru.smd.passnumber.ui.account.my_cars

import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.alert_view.view.*
import kotlinx.android.synthetic.main.bottom_menu.*
import ru.smd.passnumber.R
import ru.smd.passnumber.data.entities.PassData
import ru.smd.passnumber.data.tools.PreferencesHelper
import ru.smd.passnumber.databinding.FragmentMyCarsBinding
import ru.smd.passnumber.ui.account.my_cars.adapters.MyCarsSwipeAdapter
import ru.smd.passnumber.ui.account.my_cars.data_car.DataCarFragment
import ru.smd.passnumber.ui.activities.main.MainActivity
import ru.smd.passnumber.ui.help_registration.HelpRegistrationFragment
import javax.inject.Inject

@AndroidEntryPoint
class MyCarsFragment : Fragment(), MyCarsContract.View, MyCarsSwipeAdapter.OnClickListner {

    @Inject
    lateinit var presenter: MyCarsContract.Presenter

    @Inject
    lateinit var prefs: PreferencesHelper

    lateinit var binding: FragmentMyCarsBinding

    lateinit var adapter: MyCarsSwipeAdapter

    override fun onStart() {
        super.onStart()
        presenter.onStart(this)
    }

    override fun onStop() {
        super.onStop()
        presenter.onStop()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.run {
            adapter = MyCarsSwipeAdapter(this@MyCarsFragment)
            glass.setOnClickListener { }//TODO поиск
            microphone.setOnClickListener { } //TODO микрофон
            recycleMyCars.adapter = adapter
            txtMyCarsHavent.text =
                Html.fromHtml(requireContext().getString(R.string.you_havent_cars))
            btnBackMyCars.setOnClickListener { presenter.onClickBack() }
            btnAddMyCarsPlus.setOnClickListener { presenter.onClickAdd() }
            btnAddMyCars.setOnClickListener {
                if (contAddMyCars.visibility == View.VISIBLE) {
                    presenter.addCar(
                        edtRegNumberMyCars.text.toString(),
                        edtLabelModelMyCars.text.toString(),
                        edtNameDriverMyCars.text.toString()
                    )
                } else
                    presenter.onClickAdd()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentMyCarsBinding.inflate(inflater).run {
        binding = this
        root
    }

    override fun showErrorMessage(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun showErrorInternet() {
        Toast.makeText(requireContext(), R.string.hasntInternet, Toast.LENGTH_SHORT).show()
    }

    override fun toBack() {
        if (binding.contAddMyCars.visibility == View.VISIBLE) {
            parentFragmentManager.beginTransaction().replace(R.id.mainContainer, MyCarsFragment())
                .commit()
        } else requireActivity().onBackPressed()
    }

    override fun exit() {
        activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()
    }

    override fun showAddCarBlock() {
        binding.contImgMyCars.visibility = View.GONE
        binding.btnAddMyCars.visibility = View.VISIBLE
        binding.contAddMyCars.visibility = View.VISIBLE
        binding.btnAddMyCarsPlus.visibility = View.GONE
        binding.tvTitleMyCars.setText(getString(R.string.add_cars))
        binding.contListCars.visibility = View.GONE
    }

    override fun showCarList(cars: List<PassData>, firstAddedCar: Boolean) {
        binding.contImgMyCars.visibility = View.GONE
        binding.btnAddMyCars.visibility = View.GONE
        binding.contAddMyCars.visibility = View.GONE
        binding.btnAddMyCarsPlus.visibility = View.VISIBLE
        binding.tvTitleMyCars.setText(getString(R.string.cars))
        binding.contListCars.visibility = View.VISIBLE
        adapter.isFirstAdded = firstAddedCar
        adapter.setData(cars)
        if (firstAddedCar) {
            binding.alertUsingSwipe.isGone = false
            binding.alertUsingSwipe.btnAlert.setOnClickListener {
                binding.alertUsingSwipe.isGone = true
                prefs.setFirstAddedCar()
                adapter.isFirstAdded = false
                adapter.notifyDataSetChanged()
            }
        } else
            binding.alertUsingSwipe.isGone = true
    }

    override fun showEmptyList() {
        binding.alertUsingSwipe.isGone = true
        binding.contImgMyCars.visibility = View.VISIBLE
        binding.btnAddMyCars.visibility = View.VISIBLE
        binding.contAddMyCars.visibility = View.GONE
        binding.btnAddMyCarsPlus.visibility = View.VISIBLE
        binding.tvTitleMyCars.setText(getString(R.string.cars))
        binding.contListCars.visibility = View.GONE
    }

    override fun enableEdtRegNum() {
        binding.edtRegNumberMyCars.isEnabled = true
    }

    override fun onClickEdit(regNumber: String, driverName: String, mark: String) {
        showAddCarBlock()
        binding.run {
            edtRegNumberMyCars.setText(regNumber)
            edtRegNumberMyCars.isEnabled = false
            edtLabelModelMyCars.setText(mark)
            edtNameDriverMyCars.setText(driverName)
        }
    }

    override fun onClickDelete(regNumber: Int) {
        presenter.deleteCar(regNumber)
    }

    override fun onClickCard(regNumber: String) {
        parentFragmentManager.beginTransaction()
            .replace(R.id.mainContainer, DataCarFragment.create(regNumber)).addToBackStack(null)
            .commit()
    }

    override fun onClickHelp() {
        val main = requireActivity() as MainActivity
        main.bottomSelected(main.btnBottom3)
        parentFragmentManager.popBackStack()
        parentFragmentManager.beginTransaction()
            .replace(R.id.mainContainer, HelpRegistrationFragment()).commit()
    }

}