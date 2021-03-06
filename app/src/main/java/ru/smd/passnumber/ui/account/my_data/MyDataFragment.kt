package ru.smd.passnumber.ui.account.my_data

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import ru.smd.passnumber.R
import ru.smd.passnumber.data.core.Constants
import ru.smd.passnumber.databinding.FragmentMyDataBinding
import ru.smd.passnumber.ui.account.AccountFragment
import ru.tinkoff.decoro.MaskImpl
import ru.tinkoff.decoro.watchers.FormatWatcher
import ru.tinkoff.decoro.watchers.MaskFormatWatcher
import javax.inject.Inject

@AndroidEntryPoint
class MyDataFragment:Fragment(),MyDataContract.View {

    @Inject
    lateinit var presenter: MyDataContract.Presenter

    lateinit var binding:FragmentMyDataBinding

    override fun onStart() {
        super.onStart()
        presenter.onStart(this)
    }

    override fun onStop() {
        super.onStop()
        presenter.onStop()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    )= FragmentMyDataBinding.inflate(inflater).run {
        binding = this
        root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.run {
            val mask = MaskImpl.createTerminated(Constants.PHONE_RUS)
            val watcher: FormatWatcher = MaskFormatWatcher(mask)
            watcher.installOn(edtMyDataPhone)
            btnBackMyData.setOnClickListener {
                presenter.onClickBack()
            }
            btnMyData.setOnClickListener {
                if (edtMyDataFio.text.isEmpty()||edtMyDataCompany.text.isEmpty()||edtMyDataEmail.text.isEmpty()||edtMyDataPhone.text.isEmpty()){
                    showErrorMessage(getString(R.string.dont_may_is_empty))
                }else
                presenter.onClickSave(edtMyDataFio.text.toString(),edtMyDataPhone.text.toString(),edtMyDataEmail.text.toString(),edtMyDataCompany.text.toString(),requireContext())
            }
        }
    }


    override fun showErrorMessage(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun showErrorInternet() {
        Toast.makeText(requireContext(), R.string.hasntInternet, Toast.LENGTH_SHORT).show()
    }

    override fun toBack() {
        requireActivity().onBackPressed()
    }

    override fun exit() {
        activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()
    }

    override fun showMyData(fio: String?, phone: String?, email: String?, company: String?) {
        binding.run {
            if (fio!=null)edtMyDataFio.setHint(fio)
            if (phone!=null){
                edtMyDataPhone.setHint(phone)
                edtMyDataPhone.isEnabled=false
            }
            if (email!=null)edtMyDataEmail.setHint(email)
            if (company!=null)edtMyDataCompany.setHint(company)
        }
    }

    override fun showAcountFragment() {
        parentFragmentManager.beginTransaction().replace(R.id.mainContainer, AccountFragment())
            .commit()
    }
}