import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.asclepius.data.CanionRepository
import com.dicoding.asclepius.view.ArticleViewModel

class ArticleViewModelFactory(private val canionRepository: CanionRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ArticleViewModel::class.java)) {
            return ArticleViewModel(canionRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}


