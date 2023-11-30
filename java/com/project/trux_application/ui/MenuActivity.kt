package com.project.trux_application.ui


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.project.trux_application.databinding.ActivityMenuBinding


class MenuActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMenuBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

    override fun onResume() {
        super.onResume()
        binding.ivPersonal.setOnClickListener {
            val intent = Intent(this, UploadDocument::class.java)
            startActivity(intent)
        }
        binding.tvpersonal.setOnClickListener {
            val intent = Intent(this, UploadDocument::class.java)
            startActivity(intent)
        }
        binding.ivCargo.setOnClickListener {
            val intent = Intent(this, CargoDeclarationActivity::class.java)
            startActivity(intent)
        }

        binding.tvcargo.setOnClickListener {
            val intent = Intent(this, CargoDeclarationActivity::class.java)
            startActivity(intent)
        }

        binding.ivTruck.setOnClickListener {
            val intent = Intent(this, UploadingTruckDocsActivity::class.java)
            startActivity(intent)
        }
        binding.tvTrucks.setOnClickListener {
            val intent = Intent(this, UploadingTruckDocsActivity::class.java)
            startActivity(intent)
        }

    }
}








