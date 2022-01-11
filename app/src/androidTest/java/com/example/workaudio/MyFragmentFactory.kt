package com.example.workaudio

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.example.workaudio.presentation.creation.NameFragment
import javax.inject.Inject

class MyFragmentFactory @Inject constructor(): FragmentFactory() {

    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        return when (className) {

            NameFragment::class.java.name -> {
                NameFragment()
            }

            else -> super.instantiate(classLoader, className)
        }
    }
}