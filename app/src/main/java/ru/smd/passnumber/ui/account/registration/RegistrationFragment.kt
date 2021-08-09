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
import kotlinx.android.synthetic.main.fragment_registration.*
import ru.smd.passnumber.R
import ru.smd.passnumber.data.core.Constants
import ru.smd.passnumber.databinding.FragmentRegistrationBinding
import ru.smd.passnumber.ui.account.AccountFragment
import ru.smd.passnumber.utils.openLink
import ru.tinkoff.decoro.MaskImpl
import ru.tinkoff.decoro.watchers.FormatWatcher
import ru.tinkoff.decoro.watchers.MaskFormatWatcher
import javax.inject.Inject
@AndroidEntryPoint
class RegistrationFragment : Fragment(), RegistrationContract.View {

    @Inject
    lateinit var presenter: RegistrationContract.Presenter

    lateinit var binding: FragmentRegistrationBinding

    var userNumberFromCheckPass=""
    var userNameFromCheckPass=""
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


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.run {
            txtAgreement.text= Html.fromHtml(getString(R.string.agreement))
            txtAgreement.setOnClickListener {
                openLink("https://pass.su/terms")
            }
            txtSendSms.text=Html.fromHtml(getString(R.string.send_sms))
            btnRegistrationEnter.setOnClickListener {
                handleClickBtnRegistration()
            }
            edtRegistrationCode.addTextChangedListener{
                if (edtRegistrationCode.length()==4){
                    btnRegistrationEnter.isEnabled=true
                }else btnRegistrationEnter.isEnabled=false
            }
            val mask = MaskImpl.createTerminated(Constants.PHONE_RUS)
            val watcher: FormatWatcher = MaskFormatWatcher(mask)
            watcher.installOn(edtRegistrationPhone)
            edtRegistrationPhone.isFocusable=true
            edtRegistrationPhone.addTextChangedListener {
                if (edtRegistrationPhone.text.length == 16 && registrationSwitch.isChecked) {
                    btnRegistrationEnter.isEnabled=true
                }else {
                    btnRegistrationEnter.isEnabled=false
                }
            }
            registrationSwitch.setOnClickListener {
                if (edtRegistrationPhone.text.length == 16 && registrationSwitch.isChecked) {
                    btnRegistrationEnter.isEnabled=true
                }else {
                    btnRegistrationEnter.isEnabled=false
                }
            }

        }
        if (userNumberFromCheckPass.isNotEmpty()){
            binding.edtRegistrationPhone.setText(userNumberFromCheckPass)
            binding.handleClickBtnRegistration()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        userNumberFromCheckPass=""
        userNameFromCheckPass=""
    }

    @SuppressLint("HardwareIds")
    private fun FragmentRegistrationBinding.handleClickBtnRegistration() {
        if (block2RegistrationCont.visibility == View.GONE) {
            block1RegistrationCont.visibility = View.GONE
            block2RegistrationCont.visibility = View.VISIBLE
            btnRegistrationEnter.isEnabled = false
            presenter.startTimer()
            presenter.sendSms(edtRegistrationPhone.text.toString())
        } else {
            presenter.onClickEnter(
                Settings.Secure.getString(
                    requireActivity().getContentResolver(),
                    Settings.Secure.ANDROID_ID
                ), edtRegistrationCode.text.toString(), edtRegistrationPhone.text.toString(),userNameFromCheckPass
            )
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

    override fun showTimer(time: Long) {
       binding.txtTimer.setText(getString(R.string.retry_send_code,time.toString()))
    }

    override fun activateButtons() {
        binding.run {
            txtTimer.visibility=View.GONE
            btnDidntGetSms.visibility=View.VISIBLE
            btnRetrySendCode.visibility=View.VISIBLE
            btnRetrySendCode.setOnClickListener {
                txtTimer.visibility=View.VISIBLE
                btnDidntGetSms.visibility=View.GONE
                btnRetrySendCode.visibility=View.GONE
                presenter.sendSms("")
                presenter.startTimer()
            }
        }
    }

    override fun exit() {
        activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()
    }


}