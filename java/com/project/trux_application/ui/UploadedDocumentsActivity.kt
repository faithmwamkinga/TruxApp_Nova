package com.project.trux_application.ui

import DocumentsRvAdapter
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.cloudinary.android.MediaManager
import com.project.trux_application.R
import com.project.trux_application.databinding.ActivityUploadedDocumentsBinding
import com.project.trux_application.viewModel.CargoDocumentsViewModel
import com.project.trux_application.viewModel.DocumentViewModel
import com.project.trux_application.viewModel.TruckDocumentsViewModel
import com.squareup.picasso.Picasso


class UploadedDocumentsActivity : AppCompatActivity() {
    lateinit var binding:ActivityUploadedDocumentsBinding
    val documentViewModel:DocumentViewModel by viewModels()
    val truckDocsViewModel:TruckDocumentsViewModel by viewModels()
    private val cargoDaocumentsViewModel: CargoDocumentsViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_uploaded_documents)
        binding = ActivityUploadedDocumentsBinding.inflate(layoutInflater)
        setContentView(binding.root)
//
        documentViewModel.fetchDocuments().observe(this, Observer { documents->
            val adaptor = DocumentsRvAdapter(documents)
            binding.rvPersonalDoc.layoutManager = LinearLayoutManager(this)
            binding.rvPersonalDoc.adapter = adaptor
//            Toast.makeText(baseContext, "Document Upload Successful", Toast.LENGTH_LONG).show()

        })
        truckDocsViewModel.fetchTruckDocuments().observe(this, Observer { truckDocuments ->
            val adaptor = TruckDocumentsAdaptor(truckDocuments)
            binding.rvTruckDocs.layoutManager = LinearLayoutManager(this)
            binding.rvTruckDocs.adapter = adaptor
//            Toast.makeText(baseContext, "Document Upload Successful", Toast.LENGTH_LONG).show()

        })
        cargoDaocumentsViewModel.fetchCargoDocuments().observe(this, Observer { cargoDocuments ->
            val adaptor = CargoDocumentsAdaptor(cargoDocuments)
            binding.rvCargoDocs.layoutManager = LinearLayoutManager(this)
            binding.rvCargoDocs.adapter = adaptor
            Toast.makeText(baseContext, "Document Upload Successful", Toast.LENGTH_LONG).show()

        })
        documentViewModel.documentError.observe(this,Observer{error->
            Toast.makeText(this,error,Toast.LENGTH_LONG).show()
            //Todo
        })


    }

    override fun onResume() {
        super.onResume()
        binding.btnSubmit.setOnClickListener {
            var intent = Intent(this, SubmitDocsActivity::class.java)
            startActivity(intent)
        }

        binding.floatingActionButton2.setOnClickListener {
            var intent = Intent(this, MenuActivity::class.java)
            startActivity(intent)
        }
        binding.buttonCancel.setOnClickListener {
            var intent = Intent(this, UploadDocument::class.java)
            startActivity(intent)
        }
    }
}