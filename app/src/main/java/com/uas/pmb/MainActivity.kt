import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import com.uas.pmb.ui.screens.PmbNavGraph
import com.uas.pmb.ui.theme.PmbTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PmbTheme { // Sesuaikan dengan nama tema projectmu
                Surface(color = MaterialTheme.colorScheme.background) {
                    PmbNavGraph()
                }
            }
        }
    }
}