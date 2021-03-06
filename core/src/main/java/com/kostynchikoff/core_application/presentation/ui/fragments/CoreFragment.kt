package com.kostynchikoff.core_application.presentation.ui.fragments

import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.kostynchikoff.core_application.data.constants.CoreConstant.PERMISSION_DENIED
import com.kostynchikoff.core_application.data.constants.CoreVariables.LOGIN_ACTIVITY
import com.kostynchikoff.core_application.data.network.Status
import com.kostynchikoff.core_application.utils.callback.PermissionHandler
import com.kostynchikoff.core_application.utils.callback.ResultLiveDataHandler
import com.kostynchikoff.core_application.utils.extensions.showActivityAndClearBackStack
import com.kostynchikoff.core_application.utils.wrappers.EventObserver

abstract class CoreFragment(id: Int) : Fragment(id), ResultLiveDataHandler, PermissionHandler {

    /**
     * Для того чтобы отслеживать статусы необходимо подписаться во Fragment-е
     */
    protected val statusObserver = Observer<Status> {
        it?.let {
            when (it) {
                Status.SHOW_LOADING -> showLoader()
                Status.HIDE_LOADING -> hideLoader()
                Status.REDIRECT_LOGIN -> redirectLogin()
                Status.SUCCESS -> success()
                else -> return@let
            }
        }
    }

    protected val errorMessageObserver = EventObserver<String> {
        error(it)
    }

    private fun redirectLogin() = activity?.showActivityAndClearBackStack(LOGIN_ACTIVITY)

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        grantResults.forEach {
            when {
                it != PERMISSION_DENIED -> {
                    confirmPermission()
                    return
                }
                else -> {
                    ignorePermission()
                    return
                }
            }
        }
    }
}
