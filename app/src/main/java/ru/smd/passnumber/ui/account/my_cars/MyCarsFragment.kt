package ru.smd.passnumber.ui.account.my_cars

import android.os.Binder
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import ru.smd.passnumber.R
import ru.smd.passnumber.databinding.FragmentMyCarsBinding
import ru.smd.passnumber.databinding.FragmentMyDataBinding
import javax.inject.Inject

@AndroidEntryPoint
class MyCarsFragment : Fragment(), MyCarsContract.View {

    @Inject
    lateinit var presenter: MyCarsContract.Presenter

    lateinit var binding: FragmentMyCarsBinding

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

    override fun showCarList() {
        binding.contImgMyCars.visibility = View.GONE
        binding.btnAddMyCars.visibility = View.GONE
        binding.contAddMyCars.visibility = View.GONE
        binding.btnAddMyCarsPlus.visibility = View.VISIBLE
        binding.tvTitleMyCars.setText(getString(R.string.cars))
        binding.contListCars.visibility = View.VISIBLE
    }

    override fun showEmptyList() {
        binding.contImgMyCars.visibility = View.VISIBLE
        binding.btnAddMyCars.visibility = View.VISIBLE
        binding.contAddMyCars.visibility = View.GONE
        binding.btnAddMyCarsPlus.visibility = View.VISIBLE
        binding.tvTitleMyCars.setText(getString(R.string.cars))
        binding.contListCars.visibility = View.GONE
    }


}