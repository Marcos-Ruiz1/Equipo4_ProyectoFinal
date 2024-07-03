package rodriguez.rosa.evangelion30

import android.app.Application
import com.google.firebase.database.FirebaseDatabase

class PersistenceApp: Application() {

    override fun onCreate() {
        super.onCreate()

        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
    }

}