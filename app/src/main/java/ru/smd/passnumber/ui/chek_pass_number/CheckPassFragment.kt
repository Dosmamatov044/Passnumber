package ru.smd.passnumber.ui.chek_pass_number

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.InputType
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.*
import android.widget.TextView.OnEditorActionListener
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_check_pass.*
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener
import ru.smd.passnumber.R
import ru.smd.passnumber.data.core.Constants
import ru.smd.passnumber.data.core.hideKeyboard
import ru.smd.passnumber.data.core.showKeyBoard
import ru.smd.passnumber.databinding.FragmentCheckPassBinding
import ru.smd.passnumber.ui.activities.main.MainActivity
import ru.smd.passnumber.ui.checking_pass.CheckingPassFragment


/**
 * Created by Siddikov Mukhriddin on 7/21/21
 */
@AndroidEntryPoint
class CheckPassFragment : Fragment(R.layout.fragment_check_pass) {


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
                    if (it) {
                        tvView.visibility = View.VISIBLE
                        scroll.post(Runnable {
                            scroll.smoothScrollBy(0, scroll.bottom)
                        })
                    } else tvView.visibility = View.GONE
                })
            showKeyBoard(etStartNumber)
            etStartNumber.setOnEditorActionListener(object : OnEditorActionListener {
                override fun onEditorAction(
                    v: TextView?,
                    actionId: Int,
                    event: KeyEvent?
                ): Boolean {
                    if (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER || actionId == EditorInfo.IME_ACTION_DONE) {
                        checkCorrectNumber()
                    }
                    return false
                }

            })
            etEndNumber.setOnEditorActionListener(object : OnEditorActionListener {
                override fun onEditorAction(
                    v: TextView?,
                    actionId: Int,
                    event: KeyEvent?
                ): Boolean {
                    if (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER || actionId == EditorInfo.IME_ACTION_DONE) {
                        checkCorrectNumber()
                    }
                    return false
                }

            })
            btnCheckPassNumber.setOnClickListener {
                checkCorrectNumber()
            }


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

    fun checkCorrectNumber() {
        if (isNoEmpty) {
            if (etStartNumber.text.matches(Constants.MASK_REG_NUMBER.toRegex())&&etEndNumber.text.length>=2) {
                val regNumber=etStartNumber.text.toString()+etEndNumber.text.toString()
                hideKeyboard()
                parentFragmentManager.beginTransaction()
                    .replace(R.id.checkPassContainer, CheckingPassFragment().apply {
                        regNumberData=regNumber
                    }).addToBackStack(null).commit()
                etStartNumber.text.clear()
                etEndNumber.text.clear()
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