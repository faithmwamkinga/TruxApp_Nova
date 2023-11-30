package com.project.trux_application.ui

import android.app.DatePickerDialog
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.project.trux_application.R
import com.project.trux_application.databinding.ActivityCargoDeclarationBinding
import com.project.trux_application.model.CargoDocsData
import com.project.trux_application.model.CargoDocsRequest
import com.project.trux_application.model.TruckDocsData
import com.project.trux_application.repository.CargoDocumentRepository
import com.project.trux_application.utils.Utility
import com.project.trux_application.viewModel.CargoDocumentsViewModel
import com.squareup.picasso.Picasso
import java.io.File
import java.io.FileOutputStream
import java.util.Calendar
import java.util.Locale

class CargoDeclarationActivity : AppCompatActivity() {
    private val cargoDocumentRepository = CargoDocumentRepository()
    private lateinit var binding: ActivityCargoDeclarationBinding
    private lateinit var editTextDate: EditText
    private lateinit var editTextExpiryDate: EditText
    private val calendar = Calendar.getInstance()
    private var selectedImageUri: Uri? = null
    private val cargoDaocumentsViewModel: CargoDocumentsViewModel by viewModels()
    private lateinit var btnSaveCargoDocs:Button
    lateinit var selectedImagePath:String
    lateinit var selectedCargoDocumentRadioButton:String
    lateinit var sharedPreferences: SharedPreferences
    private var authToken:String? = null

    private val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()){uri->
        selectedImageUri = uri
        if (selectedImageUri!=null){
            val filePath = selectedImageUri?.path
            val bitmap = BitmapFactory.decodeFile(filePath)
            val inputStream = contentResolver.openInputStream(selectedImageUri!!)
            val fileName = Utility.generateFilename("image_",".png")
            val file = File(filesDir,fileName)
            val outPutStream = FileOutputStream(file)
            inputStream!!.copyTo(outPutStream)
            selectedImagePath = file.path
            binding.ivUpload.setImageBitmap(bitmap);

            try {
                Picasso.get().load(selectedImageUri).into(binding.ivUpload)
                Toast.makeText(this, "File Uploaded successfully",Toast.LENGTH_LONG).show()
            }catch (e:Exception){
                Log.e("Error","Error loading image: $e")
                Toast.makeText(this, "Error loading image",Toast.LENGTH_LONG).show()
            }
        }else{
            Toast.makeText(this,"Invalid file selection", Toast.LENGTH_LONG).show()
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCargoDeclarationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // SharedPreferences
        sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE)
        authToken = sharedPreferences.getString("auth_token", null)

        editTextDate = binding.etDate
        editTextExpiryDate = binding.etCargoExpiryDate
        btnSaveCargoDocs = binding.btnSaveCargoDocs

        val radioGroupDocumentType= findViewById<RadioGroup>(R.id.radioGroupDocumentType)

        radioGroupDocumentType.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.rct1Doc -> {
                    Log.d("RADIO", "Personal is selected")
                    selectedCargoDocumentRadioButton= "t1_document"
                }
                R.id.rc2Doc -> {
                    Log.d("RADIO", "Truck is selected")
                    selectedCargoDocumentRadioButton= "c2_document"
                }
                R.id.rccargoDeclaration3-> {
                    Log.d("RADIO", "Cargo is selected")
                    selectedCargoDocumentRadioButton= "cargo_declaration"
                }
            }
        }
    }

    override fun onResume() {
        binding.ivUpload.setOnClickListener {

            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))

        }
        super.onResume()
        binding.btnCancelCargoDocs.setOnClickListener {
            val intent = Intent(this, MenuActivity ::class.java)
            startActivity(intent)
            finish()

        }
        binding.btnSaveCargoDocs.setOnClickListener {
            if (validateCargoForm()) {
                val intent = Intent(this, UploadedDocumentsActivity::class.java)
                startActivity(intent)
                saveDocumentToDb()

            }
        }
    }
    fun saveDocumentToDb(){

        val referenceNumber = binding.etRef.text.toString()
        val cargo= binding.etGoods.text.toString()
        val cargoTones = binding.etTonnes.text.toString().toDouble()
        val cargoIssueDate = binding.etDate.text.toString()
        val cargoExpiryDate = binding.etCargoExpiryDate.text.toString()
        val documentImage = binding.ivUpload.toString()

        val savedCargoDocument = CargoDocsData(
            referenceNumber = referenceNumber,
            documentType = selectedCargoDocumentRadioButton,
            issueDate = cargoIssueDate,
            expiryDate = cargoExpiryDate,
            cargo = cargo,
            cargoTones =cargoTones,
            documentImage = selectedImagePath,
            )
        cargoDaocumentsViewModel.saveCargoDocumentToDb(savedCargoDocument)
    }
    fun showDatePickerDialog(view: View) {
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                editTextDate.setText(
                    String.format(
                        Locale.getDefault(),
                        "%04d-%02d-%02d",
                        selectedYear,
                        selectedMonth + 1,
                        selectedDay
                    )
                )
            },
            year,
            month,
            day
        )

        datePickerDialog.show()
    }
    fun showDatePickerExpiryCargoDialog(view: View) {
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val dateDPicker = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                editTextExpiryDate.setText(
                    String.format(
                        Locale.getDefault(),
                        "%04d-%02d-%02d",
                        selectedYear,
                        selectedMonth + 1,
                        selectedDay
                    )
                )
            },
            year,
            month,
            day
        )

        dateDPicker.show()
    }
    private fun validateCargoForm():Boolean {
        clearErrors()


        val referenceNumber = binding.etRef.text.toString()
        val cargo= binding.etGoods.text.toString()
        val cargoTones = binding.etTonnes.text.toString().toDouble()
        val cargoIssueDate = binding.etDate.text.toString()
        val documentImage = binding.ivUpload.toString()
        val cargoExpiryDate = binding.etCargoExpiryDate.text.toString()


        var error = false

        if (referenceNumber.isBlank()) {
            binding.tilRef.error = "Last Name is required"
            error = true
        }

        if (cargo.isBlank()) {
            binding.tilGoods.error = "Vehicle Number is required"
            error = true
        }

        if (cargoIssueDate.isBlank()) {
            binding.tilDate.error = "Date is required"
            error = true
        }
        if (cargoExpiryDate.isBlank()) {
            binding.tilCargoExpiry.error = "Date is required"
            error = true
        }
        if (!error){
            val cargoDocsRequest = CargoDocsRequest(
                referenceNumber = referenceNumber,
                documentType = selectedCargoDocumentRadioButton,
                issueDate = cargoIssueDate,
                expiryDate = cargoExpiryDate,
                cargo = cargo,
                cargoTones =cargoTones,
                documentImage = selectedImagePath,
                token = authToken!!
            )

            cargoDaocumentsViewModel.postCargoDocs(cargoDocsRequest,selectedImagePath, authToken!!)
        }
        return !error

    }

    fun clearErrors() {
        binding.tilDate.error = null
        binding.tilRef.error = null
        binding.tilGoods.error = null
        binding.tilTonnes.error = null

    }
}

