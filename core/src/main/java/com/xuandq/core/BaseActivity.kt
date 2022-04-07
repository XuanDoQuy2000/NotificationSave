package com.xuandq.core

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

abstract class BaseActivity<B : ViewDataBinding> : AppCompatActivity() {
    protected abstract val layoutId: Int
    lateinit var binding: B
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, layoutId)
        initData()
        initView()
        initEvent()
        onObserve()
    }

    protected abstract fun initData()
    protected abstract fun initView()
    protected abstract fun initEvent()
    protected abstract fun onObserve()

    protected val launcher = registerForActivityResult(StartActivityForResult()) {
        onActivityResult(it)
    }

    fun startActivityForResult(intent: Intent) {
        launcher.launch(intent)
    }

    open fun onActivityResult(result: ActivityResult) {}

    protected val singlePermissionLauncher =
        registerForActivityResult(RequestPermission()) { isGranted: Boolean ->
            onSinglePermissionResult(isGranted)
        }

    protected open fun onSinglePermissionResult(isGranted: Boolean) {}

    protected fun requestSinglePermission(permission: String) {
        singlePermissionLauncher.launch(permission)
    }

    protected val mulitplePermissionLauncher =
        registerForActivityResult(RequestMultiplePermissions()) {
            onMultiplePermissionResult(it)
        }

    protected fun requestMultiplePermission(permissions: Array<String>) {
        mulitplePermissionLauncher.launch(permissions)
    }

    protected open fun onMultiplePermissionResult(resultMap: Map<String, Boolean>) {}

    fun allPermissionGranted(vararg permissions: String) =
        permissions.all {
            ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
        }

    protected fun openDetailAppSetting() {
        val intent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("package", packageName, null)
        )
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    protected fun shouldShowPermissionRelation(vararg permissions: String) : Boolean{
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            permissions.any {
                shouldShowRequestPermissionRationale(it)
            }
        }else{
            false
        }
    }

    protected open fun setLightStatusBar(light: Boolean) {
        if (Build.VERSION.SDK_INT >= 23) {
            var newUiOptions = this.window.decorView.systemUiVisibility
            newUiOptions = if (light) newUiOptions or 8192 else newUiOptions and -8193
            this.window.decorView.systemUiVisibility = newUiOptions
        }
    }

    protected fun hideKeyboard() {
        val imm: InputMethodManager =
            this.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        //Find the currently focused view, so we can grab the correct window token from it.
        var view: View? = this.currentFocus
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = View(this)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
        view.clearFocus()
    }

}