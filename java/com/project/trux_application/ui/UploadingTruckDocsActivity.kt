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
import com.project.trux_application.R
import com.project.trux_application.databinding.ActivityUploadingTruckDocsBinding
import com.project.trux_application.model.SavedDocuments
import com.project.trux_application.model.TruckDocsData
import com.project.trux_application.model.TruckDocsRequest
import com.project.trux_application.repository.TruckDocumentRepository
import com.project.trux_application.utils.Utility
import com.project.trux_application.viewModel.TruckDocumentsViewModel
import com.squareup.picasso.Picasso
import java.io.File
import java.io.FileOutputStream
import java.util.Calendar
import java.util.Locale

class UploadingTruckDocsActivity : AppCompatActivity() {
    private val truckDocsRepository = TruckDocumentRepository()
    private lateinit var binding: ActivityUploadingTruckDocsBinding
    private lateinit var editTextDate: EditText
    private lateinit var editTextExpiryDate: EditText
    private val calendar = Calendar.getInstance()
    private var selectedImageUri: Uri? = null
    private val truckDocsViewModel:TruckDocumentsViewModel by viewModels()
    private lateinit var btnSaveTruck: Button
    lateinit var selectedImagePath:String
    lateinit var   selectedTruckDocumentRadioButton:String
    lateinit var sharedPreferences: SharedPreferences
    private var authToken:String? = null


    private val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
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
           binding.ivUploadTruckDoc.setImageBitmap(bitmap)


            try {
                Picasso.get().load(selectedImageUri).into(binding.ivUploadTruckDoc)
                Toast.makeText(this, "File uploaded successfully", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Log.e("Error", "Error loading image: $e")
                Toast.makeText(this, "Error loading image", Toast.LENGTH_SHORT).show()
            }
        }
        else {
            Toast.makeText(this, "Invalid file selection", Toast.LENGTH_SHORT).show()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadingTruckDocsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE)


        // Retrieve the authentication token
        authToken = sharedPreferences.getString("auth_token", null)

        btnSaveTruck = binding.btnSaveTruck
        editTextDate = binding.etTruckIssueDate
        editTextExpiryDate = binding.etTruckExpiryDate

        val radioGroupDocumentType = findViewById<RadioGroup>(R.id.radioGroupDocumentType)

        radioGroupDocumentType.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.rbInsurance-> {
                    Log.d("RADIO", "Personal is selected")
                    selectedTruckDocumentRadioButton= "insurance"
                }
                R.id.rbTransit -> {
                    Log.d("RADIO", "Truck is selected")
                    selectedTruckDocumentRadioButton= "transit goods documents"
                }
            }
        }

    }

    override fun onResume() {
        super.onResume()

        binding.ivUploadTruckDoc.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        super .onResume()
        binding.btnCancelTruck.setOnClickListener {
            val intent = Intent(this, MenuActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnSaveTruck.setOnClickListener {
            if (validateTruckDocument()) {
                val intent = Intent(this, UploadedDocumentsActivity::class.java)
                startActivity(intent)
                saveDocumentToDb()
            }

        }
        //Todo view bindning
    }

    fun saveDocumentToDb(){

        val expiryDate = binding.etTruckExpiryDate.text.toString()
        val issueDate = binding.etTruckIssueDate.text.toString()
        val referenceNumber = binding.etTruckRefNo.text.toString()
        val documentType = binding.etTruckDocType.text.toString()
        val documentImage = binding.ivUploadTruckDoc.toString()

        val savedTruckDocument = TruckDocsData(
            expiryDate = expiryDate,
            issueDate=issueDate,
            referenceNumber = referenceNumber,
            documentType =  selectedTruckDocumentRadioButton,
            documentImage = selectedImagePath,

        )
        truckDocsViewModel.saveTruckDocumentToDb(savedTruckDocument)
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

    fun showDatePickerExpiryDialog(view: View) {
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

    private fun validateTruckDocument():Boolean {
        clearErrors()


        val expiryDate = binding.etTruckExpiryDate.text.toString()
        val issueDate = binding.etTruckIssueDate.text.toString()
        val referenceNumber = binding.etTruckRefNo.text.toString()
        val documentType = binding.etTruckDocType.text.toString()
        val documentImage = binding.ivUploadTruckDoc.toString()


        var error = false
        if (issueDate.isBlank()) {
            binding.tilTruckIssueDate.error = "Issue date is required"
            error = true
        }
        if (expiryDate.isBlank()) {
            binding.tilTruckExpiryDate.error = "Expiry date is required"
            error = true
        }


        if (!error){
            val truckDocsRequest= TruckDocsRequest(
                expiryDate = expiryDate,
                issueDate=issueDate,
                referenceNumber = referenceNumber,
                documentType =  selectedTruckDocumentRadioButton,
                documentImage = selectedImagePath,
                token = authToken!!
            )
            truckDocsViewModel.postTruckDocs(truckDocsRequest,selectedImagePath, authToken!!)
        }
        return !error
    }
    fun clearErrors() {
//        binding.tilPersonalDocumentName.error = null
        binding.tilTruckIssueDate.error = null
        binding.tilTruckExpiryDate.error = null
    }





}