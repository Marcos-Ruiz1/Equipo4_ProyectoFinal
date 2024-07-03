
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

object AuthManager {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    var currentUser: FirebaseUser? = auth.currentUser

    val currentUserId: String?
        get() = auth.currentUser?.uid

    fun refreshUser() {
        currentUser = auth.currentUser
    }
}
