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
import com.project.trux_application.databinding.ActivityUploadDocumentBinding
import com.project.trux_application.model.DocumentRequest
import com.project.trux_application.viewModel.DocumentViewModel
import com.squareup.picasso.Picasso
import java.util.Calendar
import java.util.Locale
import kotlin.Exception
import com.project.trux_application.R
import com.project.trux_application.model.SavedDocuments
import com.project.trux_application.repository.DocumentRepository
import com.project.trux_application.utils.Utility
import java.io.File
import java.io.FileOutputStream



class UploadDocument : AppCompatActivity() {
    private val documentRepository = DocumentRepository()
    private lateinit var binding: ActivityUploadDocumentBinding
    private lateinit var editTextDate: EditText
    private lateinit var editTextExpiryDate: EditText
    private val calendar = Calendar.getInstance()
    private var selectedImageUri: Uri? = null
    private val documentsViewModel: DocumentViewModel by viewModels()
    private lateinit var buttonSaveDoc: Button
    lateinit var selectedImagePath:String
    lateinit var selectedDocumentRadioButton:String
    lateinit var sharedPreferences:SharedPreferences
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
            binding.ivAddPersonalImage.setImageBitmap(bitmap);

            try {
                Picasso.get().load(selectedImageUri).into(binding.ivAddPersonalImage)
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
        binding = ActivityUploadDocumentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // SharedPreferences
        sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE)

        authToken = sharedPreferences.getString("auth_token", null)
        Log.e("authToken","token failed")


        buttonSaveDoc = binding.buttonSavePersonalDoc

        editTextDate = binding.etDatePersonal
        editTextExpiryDate = binding.etPersonalDocExpiryDate

        //radio button

        val radioGroupDocumentType = findViewById<RadioGroup>(R.id.radioGroupDocumentType)

        radioGroupDocumentType.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.rbpassport -> {
                    // Handle the Personal radio button selection
                    // You can put your logic here
                    Log.d("RADIO", "Personal is selected")
                    selectedDocumentRadioButton = "passport"
                }
                R.id.rbdrivinglicense -> {
                    // Handle the Truck radio button selection
                    // You can put your logic here
                    Log.d("RADIO", "Truck is selected")
                    selectedDocumentRadioButton = "driving_license"
                }
                R.id.rbhealth-> {
                    // Handle the Cargo radio button selection
                    // You can put your logic here
                    Log.d("RADIO", "Cargo is selected")
                    selectedDocumentRadioButton = "health_certificate"
                }
            }
        }


    }
    override fun onResume() {

    binding.ivAddPersonalImage.setOnClickListener {
        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }
        super.onResume()

 binding.buttonCancelPersonalDoc.setOnClickListener {
            val intent = Intent(this, MenuActivity::class.java)
            startActivity(intent)
            finish()

        }

    binding.buttonSavePersonalDoc.setOnClickListener {
        if (validateDocument()) {
            val intent = Intent(this, UploadedDocumentsActivity::class.java)
            startActivity(intent)
            saveDocumentToDb()
        }
    }

        documentsViewModel.documentsLiveData.observe(this, Observer { response ->
            Toast.makeText(baseContext, "Document Upload Successful", Toast.LENGTH_LONG).show()


        })

        documentsViewModel.documentError.observe(this, Observer { str ->
            Toast.makeText(this,str,Toast.LENGTH_LONG).show()
        })
    }
    fun saveDocumentToDb(){

        val expiryDate = binding.etPersonalDocExpiryDate.text.toString()
        val issueDate = binding.etDatePersonal.text.toString()
        val referenceNumber = binding.etPersonalNumber.text.toString()
        val documentType = binding.tvPersonalDocType.text.toString()
        val documentImage = binding.ivAddPersonalImage.toString()

        val savedDocument = SavedDocuments(
            referencenumber = referenceNumber,
            issueDate = issueDate,
            expiryDate = expiryDate,
            documentImage = selectedImagePath,
            documentType = selectedDocumentRadioButton,

        )
        documentsViewModel.ssaveDocumentToDb(savedDocument)


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

    private fun validateDocument():Boolean {
        clearErrors()

        val expiryDate = binding.etPersonalDocExpiryDate.text.toString()
        val issueDate = binding.etDatePersonal.text.toString()
        val referenceNumber = binding.etPersonalNumber.text.toString()


        var error = false
        if (issueDate.isBlank()) {
            binding.tilDatePersonal.error = "Issue date is required"
            error = true
        }
        if (expiryDate.isBlank()) {
            binding.tilPersonalDocExpiryDate.error = "Expiry date is required"
            error = true
        }

        if (!error){
            val documentRequest= DocumentRequest(
                expiryDate = expiryDate,
                issueDate=issueDate,
                referencenumber = referenceNumber,
                documentType = selectedDocumentRadioButton,
                token = authToken!!
            )
            documentsViewModel.postPersonalDocs(documentRequest,selectedImagePath, authToken!!)


        }
        return !error
    }
    fun clearErrors() {
//        binding.tilPersonalDocumentName.error = null
        binding.tilDatePersonal.error = null
        binding.tilPersonalDocExpiryDate.error = null
    }
//    fun saveDocumentToDb(document: SavedDocuments) {
//        documentRepository.saveDocumentToDb(document)
//    }



}

