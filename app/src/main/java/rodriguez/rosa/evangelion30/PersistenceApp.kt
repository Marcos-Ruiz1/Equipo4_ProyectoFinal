package rodriguez.rosa.evangelion30

import android.app.Application
import com.google.firebase.database.FirebaseDatabase
import com.jakewharton.threetenabp.AndroidThreeTen

class PersistenceApp: Application() {

    override fun onCreate() {
        super.onCreate()

        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
        AndroidThreeTen.init(this)
    }

}