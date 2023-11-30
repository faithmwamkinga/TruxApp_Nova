package com.project.trux_application.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.project.trux_application.databinding.DocumentListBinding
import com.project.trux_application.model.CargoDocsData
import com.project.trux_application.model.SavedDocuments
import com.project.trux_application.model.TruckDocsData

class TruckDocumentsAdaptor(private val category: List<TruckDocsData> ):RecyclerView.Adapter<TruckDocsViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):TruckDocsViewHolder{


            val binding = DocumentListBinding.inflate(LayoutInflater.from(parent.context))
            return TruckDocsViewHolder(binding)
        }


        override fun onBindViewHolder(holder: TruckDocsViewHolder, position: Int) {


            val documents = category.get(position)
            holder.bind(documents)

        }
        override fun getItemCount(): Int {

            return category.size
        }
    }
    class TruckDocsViewHolder( var binding: DocumentListBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(document: TruckDocsData){
            Glide.with(binding.ivDocument.context)
                .load(document.documentImage)
                .into(binding.ivDocument)

            val formattedDocumentType = when(document.documentType){
                "insurance" -> "Insurance"
                "transit goods documents" -> "Certified Transit"
                else -> "Invalid Document"
            }
            binding.tvDocuments.text = formattedDocumentType
        }

    }


