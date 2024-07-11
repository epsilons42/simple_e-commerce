package com.example.alsess.viewmodel


import android.content.Context
import android.text.SpannableString
import android.text.Spanned
import android.text.TextUtils
import android.text.style.ClickableSpan
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.alsess.R
import com.example.alsess.util.AppUtils
import com.example.alsess.util.DialogUtils
import com.example.alsess.view.activity.MainActivity
import com.google.firebase.auth.FirebaseAuth
import java.util.*

class SignUpViewModel : ViewModel() {
    private lateinit var firebaseAuth: FirebaseAuth
    private val appUtils = AppUtils()
    private val dialogUtils = DialogUtils()
    val errorMessageMLD = MutableLiveData<String>()
    val charSequenceMLD = MutableLiveData<CharSequence>()

    fun firebaseAuthentication(
        context: Context,
        email: String,
        password: String,
    ) {
        firebaseAuth = FirebaseAuth.getInstance()
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    appUtils.intentAndClear(context, MainActivity())
                }
            }.addOnFailureListener { exception ->
                errorMessageMLD.value = exception.localizedMessage
            }
    }

    fun userAgreement(context: Context) {
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                widget.cancelPendingInputEvents()
                dialogUtils.userAgreementBottomSheet(context)
            }
        }

        if (Locale.getDefault().language == "tr") {
            charSequenceMLD.value = languageTr(context, clickableSpan)
        } else {
            charSequenceMLD.value = languageEng(context,clickableSpan)
        }
    }

    private fun languageTr(context: Context, clickableSpan: ClickableSpan): CharSequence {
        val spannableString = SpannableString(context.getString(R.string.user_agreement))
        spannableString.setSpan(
            clickableSpan,
            0,
            spannableString.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        val charSequence: CharSequence = TextUtils.expandTemplate(
            "^1ni okudum ve onaylıyorum", spannableString
        )
        return charSequence
    }

    private fun languageEng(context: Context, clickableSpan: ClickableSpan): CharSequence {
        val spannableString = SpannableString(context.getString(R.string.user_agreement))
        spannableString.setSpan(
            clickableSpan,
            0,
            spannableString.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        val charSequence: CharSequence = TextUtils.expandTemplate(
            "I have read and agree to the ^1", spannableString
        )
        return charSequence
    }
}