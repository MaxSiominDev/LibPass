package dev.maxsiomin.libpass.activity

import android.app.Dialog
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.hilt.android.AndroidEntryPoint
import dev.maxsiomin.libpass.APK_LOCATION
import dev.maxsiomin.libpass.BuildConfig
import dev.maxsiomin.libpass.R
import dev.maxsiomin.libpass.extensions.openInBrowser
import dev.maxsiomin.libpass.util.SharedPrefsConfig.DATE_UPDATE_SUGGESTED
import java.time.LocalDate
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), Updater {

    @Inject
    lateinit var analytics: FirebaseAnalytics

    private val mViewModel by viewModels<MainViewModel>()

    private val navController: NavController by lazy {
        findNavController(R.id.fragmentContainer)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mViewModel.checkForUpdates { latestVersionName ->
            suggestUpdating(latestVersionName)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.overflow_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.my_libpasses -> goToMyLibpasses()
            R.id.info -> goToInfo()
            R.id.settings -> goToSettings()
            R.id.export -> goToExport()
            R.id.import_fragment -> goToImport()
        }

        return super.onOptionsItemSelected(item)
    }

    private fun goToMyLibpasses() {
        navController.navigate(R.id.passesFragment)
    }

    private fun goToInfo() {
        if (navController.currentDestination!!.id == R.id.infoFragment)
            return
        navController.navigate(R.id.infoFragment)
    }

    private fun goToSettings() {
        if (navController.currentDestination!!.id == R.id.settingsFragment)
            return
        navController.navigate(R.id.settingsFragment)
    }

    private fun goToExport() {
        if (navController.currentDestination!!.id == R.id.exportFragment)
            return
        navController.navigate(R.id.exportFragment)
    }

    private fun goToImport() {
        if (navController.currentDestination!!.id == R.id.importFragment)
            return
        navController.navigate(R.id.importFragment)
    }

    private fun suggestUpdating(latestVersionName: String) {
        // Save when update was suggested last time
        mViewModel.sharedPrefs.edit().putString(
            DATE_UPDATE_SUGGESTED, LocalDate.now().toString()
        ).apply()

        UpdateDialog.newInstance(latestVersionName).show(supportFragmentManager)
    }

    /**
     * Opens direct uri to .apk in browser. .apk should be automatically downloaded
     */
    override fun update() {
        openInBrowser(APK_LOCATION)
    }

    class UpdateDialog : DialogFragment() {

        private val updater get() = requireActivity() as Updater

        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

            val currentVersionName = BuildConfig.VERSION_NAME
            val latestVersionName = requireArguments().getString(LATEST_VERSION_NAME)

            val dialog = DialogBuilder(requireContext())
                .setMessage(getString(R.string.update_app, currentVersionName, latestVersionName))
                .setNegativeButton(R.string.no_thanks) { _, _ -> }
                .setPositiveButton(R.string.update) { _, _ ->
                    updater.update()
                }
                .create()

            dialog.setCanceledOnTouchOutside(false)

            return dialog
        }

        fun show(manager: FragmentManager) {
            show(manager, TAG)
        }

        companion object {

            const val TAG = "UpdateDialog"

            /** Key for args */
            private const val LATEST_VERSION_NAME = "latestVersion"

            /**
             * Puts [latestVersionName] to args
             */
            @JvmStatic
            fun newInstance(latestVersionName: String) = UpdateDialog().apply {
                arguments = bundleOf(LATEST_VERSION_NAME to latestVersionName)
            }
        }
    }
}

typealias DialogBuilder = AlertDialog.Builder

interface Updater {

    /**
     * Called when user submits they'd like to update
     */
    fun update()
}
