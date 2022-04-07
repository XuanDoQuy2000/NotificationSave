package com.xuandq.core

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment


abstract class BaseFragment<B : ViewDataBinding, VM : BaseViewModel> : Fragment() {
    protected abstract val layoutId: Int
    protected abstract val viewModel : VM
    lateinit var binding: B
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initData()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initEvent()
        observe()
    }

    protected abstract fun initData()
    protected abstract fun initView()
    protected abstract fun initEvent()
    protected abstract fun onObserve()

    private fun observe(){
        viewModel.commonError.observe(viewLifecycleOwner){
            //showDialog
        }
        onObserve()
    }

    fun startActivityForResult(intent: Intent) {
        launcher.launch(intent)
    }

    protected val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        onActivityResult(it)
    }

    protected open fun onActivityResult(result: ActivityResult) {}

    protected val singlePermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            onSinglePermissionResult(isGranted)
        }

    protected open fun onSinglePermissionResult(isGranted: Boolean) {}

    protected fun requestSinglePermission(permission: String) {
        singlePermissionLauncher.launch(permission)
    }

    protected val mulitplePermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            onMultiplePermissionResult(it)
        }

    protected fun requestMultiplePermission(permissions: Array<String>) {
        mulitplePermissionLauncher.launch(permissions)
    }

    protected open fun onMultiplePermissionResult(resultMap: Map<String, Boolean>) {}

    fun allPermissionGranted(vararg permissions: String) =
        permissions.all {
            ContextCompat.checkSelfPermission(
                requireContext(),
                it
            ) == PackageManager.PERMISSION_GRANTED
        }

    protected fun openDetailAppSetting() {
        context?.let {
            val intent = Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.fromParts("package", it.packageName, null)
            )
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }

    protected fun shouldShowPermissionRelation(vararg permissions: String): Boolean {
        return permissions.any {
            shouldShowRequestPermissionRationale(it)
        }
    }

}