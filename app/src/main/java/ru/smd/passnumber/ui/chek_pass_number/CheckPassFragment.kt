package ru.smd.passnumber.ui.chek_pass_number

import android.accessibilityservice.AccessibilityService
import android.annotation.SuppressLint
import android.os.Bundle
import android.text.InputFilter
import android.text.InputType
import android.text.method.DigitsKeyListener
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.ScrollView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.text.isDigitsOnly
import androidx.core.text.set
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_check_pass.*
import kotlinx.android.synthetic.main.fragment_check_pass.view.*
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener
import ru.smd.passnumber.R
import ru.smd.passnumber.data.core.Constants
import ru.smd.passnumber.data.core.hideKeyboard
import ru.smd.passnumber.data.core.showKeyBoard
import ru.smd.passnumber.data.entities.PassData
import ru.smd.passnumber.data.entities.PassesData
import ru.smd.passnumber.databinding.FragmentCheckPassBinding
import ru.smd.passnumber.databinding.FragmentRegistrationBinding
import ru.smd.passnumber.ui.activities.main.MainActivity
import ru.smd.passnumber.ui.checking_pass.CheckingPassFragment
import java.util.*


/**
 * Created by Siddikov Mukhriddin on 7/21/21
 */
@AndroidEntryPoint
class CheckPassFragment : Fragment(R.layout.fragment_check_pass) {

    private val viewModel: CheckPassViewModel by viewModels()

    lateinit var binding: FragmentCheckPassBinding

    var isNoEmpty = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentCheckPassBinding.inflate(inflater).run {
        binding = this
        root
    }


    @SuppressLint("FragmentLiveDataObserve")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.run {
            val main = requireActivity() as MainActivity
            requireActivity().window.statusBarColor =
                ContextCompat.getColor(requireContext(), R.color.colorPrimaryDark)
            main.hideBottomMenu()
            KeyboardVisibilityEvent.setEventListener(requireActivity(),
                KeyboardVisibilityEventListener {
                    if (it){
                        tvView.visibility=View.VISIBLE
                       scroll.post(Runnable {
                          scroll.smoothScrollBy(0,scroll.bottom)
                       })
                    }else  tvView.visibility=View.GONE
                })
            showKeyBoard(etStartNumber)
            btnCheckPassNumber.setOnClickListener {
                if (isNoEmpty) {
                    if (etStartNumber.text.matches(Constants.MASK_REG_NUMBER.toRegex())) {
                        viewModel.composite()
                        viewModel.checkPassData(etStartNumber.text.toString() + etEndNumber.text.toString())
                    } else {
                        Toast.makeText(
                            requireContext(),
                            R.string.fail_reg_number,
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                } else Toast.makeText(requireContext(), R.string.enter_number, Toast.LENGTH_SHORT)
                    .show()
            }
            viewModel.data.observe(this@CheckPassFragment, passData)
            etStartNumber.doOnTextChanged { text, start, before, count ->
                when (etStartNumber.selectionStart) {
                    0 -> {
                        etStartNumber.inputType = InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS
                    }
                    1 -> {
                        etStartNumber.inputType = InputType.TYPE_CLASS_NUMBER
                    }
                    2 -> {
                        etStartNumber.inputType = InputType.TYPE_CLASS_NUMBER
                    }
                    3 -> {
                        etStartNumber.inputType = InputType.TYPE_CLASS_NUMBER
                    }
                    4 -> {
                        etStartNumber.inputType = InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS
                    }
                    5 -> {
                        etStartNumber.inputType = InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS
                    }
                }
                if (etStartNumber.length() >= 6)
                    etEndNumber.requestFocus()
                checkNumberField()
            }
            etEndNumber.doOnTextChanged { text, start, before, count ->
                if (etEndNumber.length() == 0) {
                    etStartNumber.requestFocus()
                } else
                    checkNumberField()
            }

        }
    }

    fun checkNumberField() {
        if (etStartNumber.length() == 6 && etEndNumber.length() >= 2) {
            btnCheckPassNumber.isClickable = true
            btnCheckPassNumber.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.colorBlue
                )
            )
            btnCheckPassNumber.setBackgroundResource(R.drawable.ic_rectangle_round_4_blue)
            isNoEmpty = true
        } else {
            isNoEmpty = false
            btnCheckPassNumber.isClickable = true
            btnCheckPassNumber.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.color_gray_text
                )
            )
            btnCheckPassNumber.setBackgroundResource(R.drawable.ic_rectangle_round_4_gray)
        }

    }


    override fun onStop() {
        super.onStop()
        viewModel.composite()
        viewModel.dispose()

    }

    private val passData = Observer<PassData> {
        etStartNumber.text.clear()
        etEndNumber.text.clear()
        if (!viewModel.data.value?.regNumber.isNullOrEmpty()) {
            hideKeyboard()
            parentFragmentManager.beginTransaction()
                .replace(R.id.checkPassContainer, CheckingPassFragment().apply {
                    data.value = it
                }).addToBackStack(null).commit()
        }

    }

}

//    var oldPosition = etStartNumber.length()


//   if (etStartNumber.length() > oldPosition) {
//                when (etStartNumber.length()) {
//                    1 -> {
//                        if (!etStartNumber.text.first().isLetter())
//                            etStartNumber.setText(etStartNumber.text.dropLast(1).toString())
//                    }
//                    2 -> {
//                        if (!etStartNumber.text.toString()[1].isDigit())
//                            etStartNumber.setText(etStartNumber.text.dropLast(1).toString())
//                    }
//                    3 -> {
//                        if (!etStartNumber.text.toString()[2].isDigit())
//                            etStartNumber.setText(etStartNumber.text.dropLast(1).toString())
//                    }
//                    4 -> {
//                        if (!etStartNumber.text.toString()[3].isDigit())
//                            etStartNumber.setText(etStartNumber.text.dropLast(1).toString())
//                    }
//                    5 -> {
//                        if (!etStartNumber.text.toString()[4].isLetter())
//                            etStartNumber.setText(etStartNumber.text.dropLast(1).toString())
//                    }
//                    6 -> {
//                        if (!etStartNumber.text.toString()[5].isLetter())
//                            etStartNumber.setText(etStartNumber.text.dropLast(1).toString())
//                    }
//                }
//                etStartNumber.setSelection(etStartNumber.text.toString().length)
//            }
//            oldPosition = etStartNumber.length()