package com.example.workaudio.presentation.utils.dialogs

import androidx.fragment.app.DialogFragment

object DialogFactory {

    fun create(
        flavour: DialogFlavour,
        updateDuration: (Int) -> Unit = {},
        updateWorkout: (String) -> Unit = {},
        ok: () -> Unit = {},
        cancel: () -> Unit = {}
    ): DialogFragment {
        return when (flavour) {
            DialogFlavour.EDIT_NAME -> EditNameDialogFragment(updateWorkout)
            DialogFlavour.EDIT_DURATION -> EditDurationDialogFragment(updateDuration)
            DialogFlavour.STOP_PLAYER -> StopPlayerDialogFragment(ok, cancel)
            else -> EditDurationDialogFragment(updateDuration)
        }
    }

}