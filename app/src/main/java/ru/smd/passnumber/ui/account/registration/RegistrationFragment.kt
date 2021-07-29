package ru.smd.passnumber.ui.account.registration


import android.annotation.SuppressLint
import android.os.Bundle
import android.provider.Settings
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import ru.smd.passnumber.R
import ru.smd.passnumber.data.core.Constants
import ru.smd.passnumber.databinding.FragmentRegistrationBinding
import ru.smd.passnumber.ui.account.AccountFragment
import ru.tinkoff.decoro.MaskImpl
import ru.tinkoff.decoro.watchers.FormatWatcher
import ru.tinkoff.decoro.watchers.MaskFormatWatcher
import javax.inject.Inject
@AndroidEntryPoint
class RegistrationFragment : Fragment(), RegistrationContract.View {

    @Inject
    lateinit var presenter: RegistrationContract.Presenter

    lateinit var binding: FragmentRegistrationBinding

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
    ) = FragmentRegistrationBinding.inflate(inflater).run {
        binding = this
        root
    }


    @SuppressLint("HardwareIds")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.run {
            txtAgreement.text= Html.fromHtml(getString(R.string.agreement))
            btnRegistrationEnter.setOnClickListener {
                if (block2RegistrationCont.visibility==View.GONE){
                    block1RegistrationCont.visibility=View.GONE
                    block2RegistrationCont.visibility=View.VISIBLE
                }else{
                    presenter.onClickEnter(
                        Settings.Secure.getString(requireActivity().getContentResolver(),
                        Settings.Secure.ANDROID_ID),edtRegistrationCode.text.toString(),edtRegistrationPhone.text.toString())
                }
            }
            val mask = MaskImpl.createTerminated(Constants.PHONE_RUS)
            val watcher: FormatWatcher = MaskFormatWatcher(mask)
            watcher.installOn(edtRegistrationPhone)
            edtRegistrationPhone.isFocusable=true
            edtRegistrationPhone.addTextChangedListener {
                if (edtRegistrationPhone.text.length == 16 && registrationSwitch.isChecked) {
                    btnRegistrationEnter.setBackgroundResource(R.drawable.ic_rectangle_round_4_blue)
                    btnRegistrationEnter.setTextColor(resources.getColor(R.color.blue))
                    btnRegistrationEnter.isEnabled=true
                }else {
                    btnRegistrationEnter.setBackgroundResource(R.drawable.ic_rectangle_round_4_gray)
                    btnRegistrationEnter.setTextColor(resources.getColor(R.color.gray))
                    btnRegistrationEnter.isEnabled=false
                }
            }
            registrationSwitch.setOnClickListener {
                if (edtRegistrationPhone.text.length == 16 && registrationSwitch.isChecked) {
                    btnRegistrationEnter.setBackgroundResource(R.drawable.ic_rectangle_round_4_blue)
                    btnRegistrationEnter.setTextColor(resources.getColor(R.color.blue))
                    btnRegistrationEnter.isEnabled=true
                }else {
                    btnRegistrationEnter.setBackgroundResource(R.drawable.ic_rectangle_round_4_gray)
                    btnRegistrationEnter.setTextColor(resources.getColor(R.color.gray))
                    btnRegistrationEnter.isEnabled=false
                }
            }

        }
    }

    override fun showErrorMessage(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun showErrorInternet() {
        Toast.makeText(requireContext(), R.string.hasntInternet, Toast.LENGTH_SHORT).show()
    }

    override fun showAccountFragment() {
        parentFragmentManager.beginTransaction().replace(R.id.mainContainer, AccountFragment())
            .commit()
    }

    override fun exit() {
        activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()
    }


}