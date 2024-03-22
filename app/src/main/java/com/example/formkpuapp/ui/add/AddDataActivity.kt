package com.example.formkpuapp.ui.add

import android.Manifest
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.formkpuapp.R
import com.example.formkpuapp.database.FormData
import com.example.formkpuapp.databinding.ActivityAddDataBinding
import com.example.formkpuapp.helper.DateHelper
import com.example.formkpuapp.helper.ViewModelFactory
import com.example.formkpuapp.ui.camera.CameraActivity
import com.example.formkpuapp.ui.utils.rotateFile
import com.example.formkpuapp.ui.utils.uriToFile
import java.io.File
import java.util.*

class AddDataActivity : AppCompatActivity() {

    private var binding: ActivityAddDataBinding? = null
    private val _activityAddDataBinding get() = binding
    private var getFile: File? = null
    private var isEdit = false
    private var formData: FormData? = null
    private lateinit var addDataViewModel: AddDataViewModel

    companion object {
        const val CAMERA_X_RESULT = 200
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
        const val EXTRA_FORM = "extra_form"
        const val ALERT_DIALOG_CLOSE = 10
        const val ALERT_DIALOG_DELETE = 20
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddDataBinding.inflate(layoutInflater)
        setContentView(_activityAddDataBinding?.root)

        setDatePicker(binding?.edtDate!!)
        binding?.btnAddress?.setOnClickListener {
            val intent = Intent(this, MapsActivity::class.java)
            resultLauncher.launch(intent)
        }

        binding?.btnGallery?.setOnClickListener { startGallery() }
        binding?.btnCamera?.setOnClickListener {
            if (allPermissionsGranted()) {
                startCameraX()
            } else {
                ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
            }
        }

        addDataViewModel = obtainViewModel(this@AddDataActivity)

        formData = intent.getParcelableExtra(EXTRA_FORM)
        if (formData != null){
            isEdit = true
        } else {
            formData = FormData()
        }

        val actionBarTitle: String
        val btnTitle: String

        if (isEdit) {
            actionBarTitle = getString(R.string.change)
            btnTitle = getString(R.string.update)
            if (formData != null) {
                formData?.let { formData ->
                    binding?.edtNik?.setText(formData.nik)
                    binding?.edtName?.setText(formData.nama)
                    binding?.edtNoHand?.setText(formData.noHand)
                    when(formData.gender) {
                        "Laki - Laki" -> binding?.rdMale?.isChecked = true
                        "Perempuan" -> binding?.rdFemale?.isChecked = true
                        else -> { }
                    }
                    binding?.edtDate?.setText(formData.tanggal)
                    binding?.edtAddress?.setText(formData.lokasi)
                    binding?.edtLat?.setText(formData.latitude.toString())
                    binding?.edtLong?.setText(formData.longitude.toString())
                    if (formData.gambar != null) {
                        val bitmap = BitmapFactory.decodeByteArray(formData?.gambar, 0, formData?.gambar?.size ?: 0)
                        binding?.previewImageView?.setImageBitmap(bitmap)
                    } else {
                        Glide.with(applicationContext)
                            .load(R.drawable.ic_placeholder_scan) // Ganti dengan resource ID placeholder yang sesuai
                            .into(binding?.previewImageView!!)
                    }
                }
            }
        } else {
            actionBarTitle = getString(R.string.add)
            btnTitle = getString(R.string.save)
        }

        supportActionBar?.title = actionBarTitle
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding?.btnSubmit?.text = btnTitle

        binding?.btnSubmit?.setOnClickListener {
            val nik = binding?.edtNik?.text.toString().trim()
            val nama = binding?.edtName?.text.toString().trim()
            val nomorHP = binding?.edtNoHand?.text.toString().trim()
            val gender = when {
                binding?.rdMale?.isChecked == true -> "Laki - Laki"
                binding?.rdFemale?.isChecked == true -> "Perempuan"
                else -> {
                    ""
                }
            }
            val tanggal = binding?.edtDate?.text.toString().trim()
            val alamat = binding?.edtAddress?.text.toString().trim()
            val latitude = binding?.edtLat?.text.toString().trim()
            val longitude = binding?.edtLong?.text.toString().trim()

            when{
                nik.isEmpty() -> { binding?.edtNik?.error = getString(R.string.empty) }
                nik.length != 16 -> { binding?.edtNik?.error = getString(R.string.charAlert) }
                nama.isEmpty() -> { binding?.edtName?.error = getString(R.string.empty) }
                nomorHP.isEmpty() -> { binding?.edtNoHand?.error = getString(R.string.empty) }
                tanggal.isEmpty() -> { binding?.edtDate?.error = getString(R.string.empty) }
                alamat.isEmpty() -> { binding?.edtAddress?.error = getString(R.string.empty) }
                else -> {
                    formData.let { form ->
                        form?.nik = nik
                        form?.nama = nama
                        form?.noHand = nomorHP
                        form?.gender = gender
                        form?.lokasi = alamat
                        form?.latitude = latitude.toDoubleOrNull()
                        form?.latitude = longitude.toDoubleOrNull()
                        if (getFile != null){
                            val imgByteArray = getFile?.readBytes()
                            form?.gambar = imgByteArray
                        }
                    }
                    if (isEdit){
                        addDataViewModel.update(formData as FormData)
                        showToast(getString(R.string.changed))
                    } else {
                        formData.let { form ->
                            form?.date = DateHelper.getCurrentDate()
                        }
                        addDataViewModel.insert(formData as FormData)
                        showToast(getString(R.string.added))
                    }
                    finish()
                }
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        if (isEdit){
            menuInflater.inflate(R.menu.menu, menu)
        }

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.action_delete -> showAlertDialog(ALERT_DIALOG_DELETE)
            android.R.id.home -> showAlertDialog(ALERT_DIALOG_CLOSE)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    override fun onBackPressed() {
        super.onBackPressed()
        showAlertDialog(ALERT_DIALOG_CLOSE)
    }

    private fun obtainViewModel(activity: AppCompatActivity): AddDataViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[AddDataViewModel::class.java]
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Pilih Gambar")
        launcherIntentGallery.launch(chooser)
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg = result.data?.data as Uri
            selectedImg.let { uri ->
                val myFile = uriToFile(uri, this@AddDataActivity)
                getFile = myFile
                binding?.previewImageView?.setImageURI(uri)
            }
        }
    }

    private fun startCameraX() {
        val intent = Intent(this, CameraActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERA_X_RESULT) {
            val myFile = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                it.data?.getSerializableExtra("picture", File::class.java)
            } else {
                @Suppress("DEPRECATION")
                it.data?.getSerializableExtra("picture")
            } as? File
            val isBackCamera = it.data?.getBooleanExtra("isBackCamera", true) as Boolean
            myFile?.let { file ->
                rotateFile(file, isBackCamera)
                getFile = file
                binding?.previewImageView?.setImageBitmap(BitmapFactory.decodeFile(file.path))
            }
        }
    }

    private fun setDatePicker(dateEditText: EditText) {

        val myCalendar = Calendar.getInstance()

        val datePickerOnDataSetListener = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, monthOfYear)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateLabel(myCalendar, dateEditText)
        }

        dateEditText.setOnClickListener {
            DatePickerDialog(this@AddDataActivity, datePickerOnDataSetListener, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show()
        }
    }

    private val resultLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == MapsActivity.RESULT_CODE && result.data != null) {
            val valueLat = result.data!!.getDoubleExtra(MapsActivity.EXTRA_LATITUDE, 0.0)
            val valueLong = result.data!!.getDoubleExtra(MapsActivity.EXTRA_LONGITUDE, 0.0)
            val valueAddress = result.data!!.getStringExtra(MapsActivity.EXTRA_ADDRESS)

            if(valueAddress != null && valueLat != 0.0 && valueLong != 0.0){
                binding?.edtAddress?.setText(valueAddress)
                binding?.edtLat?.setText(valueLat.toString())
                binding?.edtLong?.setText(valueLong.toString())
            }


        }
    }



    private fun updateLabel(myCalendar: Calendar, dateEditText: EditText) {
        val myFormat = "dd-MMM-yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.UK)
        dateEditText.setText(sdf.format(myCalendar.time))
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    this,
                    "Tidak mendapatkan permission.",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun showAlertDialog(type: Int) {
        val isDialogClose = type == ALERT_DIALOG_CLOSE
        val dialogTitle: String
        val dialogMessage: String
        if (isDialogClose) {
            dialogTitle = getString(R.string.cancel)
            dialogMessage = getString(R.string.message_cancel)
        } else {
            dialogMessage = getString(R.string.message_delete)
            dialogTitle = getString(R.string.delete)
        }
        val alertDialogBuilder = AlertDialog.Builder(this)
        with(alertDialogBuilder) {
            setTitle(dialogTitle)
            setMessage(dialogMessage)
            setCancelable(false)
            setPositiveButton(getString(R.string.yes)) { _, _ ->
                if (!isDialogClose) {
                    addDataViewModel.delete(formData as FormData)
                    showToast(getString(R.string.deleted))
                }
                finish()
            }
            setNegativeButton(getString(R.string.no)) { dialog, _ -> dialog.cancel() }
        }
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

}