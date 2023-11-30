package com.project.trux_application.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.project.trux_application.databinding.DocumentListBinding
import com.project.trux_application.model.CargoDocsData
import com.project.trux_application.model.TruckDocsData

class CargoDocumentsAdaptor(private val categor:List<CargoDocsData>):RecyclerView.Adapter<CargoDocsViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):CargoDocsViewHolder{


            val binding = DocumentListBinding.inflate(LayoutInflater.from(parent.context))
            return CargoDocsViewHolder(binding)
        }


        override fun onBindViewHolder(holder: CargoDocsViewHolder, position: Int) {


            val documents = categor.get(position)
            holder.bind(documents)

        }
        override fun getItemCount(): Int {

            return categor.size
        }
    }
    class CargoDocsViewHolder( var binding: DocumentListBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(document: CargoDocsData){
            Glide.with(binding.ivDocument.context)
                .load(document.documentImage)
                .into(binding.ivDocument)
            val formattedDocumentType = when(document.documentType){
                "t1_document" -> "T1 Document"
                "c2_document" -> "C2 Document"
                "cargo_declaration" -> "Cargo Declaration"
                else -> "Invalid Document"
            }
            binding.tvDocuments.text = formattedDocumentType
        }

    }
