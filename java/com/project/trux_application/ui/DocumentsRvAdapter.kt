import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.bumptech.glide.Glide
import com.project.trux_application.databinding.DocumentListBinding
import com.project.trux_application.model.SavedDocuments

class DocumentsRvAdapter(private val categories: List<SavedDocuments>):Adapter<DocumentsViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):DocumentsViewHolder{


        val binding = DocumentListBinding.inflate(LayoutInflater.from(parent.context))
        return DocumentsViewHolder(binding)
    }


    override fun onBindViewHolder(holder: DocumentsViewHolder, position: Int) {


        val documents = categories.get(position)
        holder.bind(documents)

    }
    override fun getItemCount(): Int {

        return categories.size
    }
}
class DocumentsViewHolder( var binding: DocumentListBinding): RecyclerView.ViewHolder(binding.root){
    fun bind(document:SavedDocuments){
        Glide.with(binding.ivDocument.context)
            .load(document.documentImage)
            .into(binding.ivDocument)
        val formattedDocumentType = when(document.documentType){
            "passport" -> "Passport"
            "health_certificate" -> "Health Certificate"
            "driving_license" -> "Driving License"
            else -> "Invalid Document"
        }
        binding.tvDocuments.text = formattedDocumentType
    }

}

