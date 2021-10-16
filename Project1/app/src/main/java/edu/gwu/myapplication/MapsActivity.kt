package edu.gwu.myapplication

import android.content.Intent
import android.location.Address
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.button.MaterialButton
import edu.gwu.myapplication.databinding.ActivityMapsBinding
import org.jetbrains.anko.doAsync

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private var currentAddress: Address? = null
    private lateinit var adapter: newsAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var cardView: CardView
    private lateinit var textView: TextView




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.title = "News by Location"
        // Sets up the XML Layout using using ViewBinding
        // https://developer.android.com/topic/libraries/view-binding
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        cardView = findViewById(R.id.map_card)

        recyclerView = findViewById(R.id.newsrecycle)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment

        // Trigger the map to start loading
        mapFragment.getMapAsync(this)
    }

    fun updateCurrentAddress(address: Address) {
        currentAddress = address
        var currAddress = address.getAddressLine(0)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val newsAPI = getString(R.string.newsAPI)
        val newsManager: newsManager = newsManager()


        googleMap.setOnMapLongClickListener { coords: LatLng ->
            googleMap.clear()


            // Geocoding should be done on a background thread - it involves networking
            // and has the potential to cause the app to freeze (Application Not Responding error)
            // if done on the UI Thread and it takes too long.
            doAsync {
                val geocoder: Geocoder = Geocoder(this@MapsActivity)

                // In Kotlin, you can assign the result of a try-catch block. Both the "try" and
                // "catch" clauses need to yield a valid value to assign.
                val results: List<Address> = try {
                    geocoder.getFromLocation(coords.latitude, coords.longitude, 10)
                } catch (exception: Exception) {
                    // Uses the error logger to print the error
                    Log.e("MapsActivity", "Geocoding failed", exception)

                    // Uses System.out.println to print the error
                    exception.printStackTrace()

                    listOf()
                }


                if(results != null){
                    var articles: List<news> = newsManager.retrieveMapNews(newsAPI, results)
                    adapter = newsAdapter(articles)
                    // Move back to the UI Thread now that we have some results to show.
                    // The UI can only be updated from the UI Thread.
                    runOnUiThread {
                        if (results.isNotEmpty()) {
                            // Potentially, we could show all results to the user to choose from,
                            // but for our usage it's sufficient enough to just use the first result.
                            // The Geocoder's first result is often the "best" one in terms of its accuracy / confidence.
                            val firstResult: Address = results[0]
                            val postalAddress: String = firstResult.getAddressLine(0)
                            val adminArea = results.first().adminArea

                            //Log.d("MapsActivity", "First result: $postalAddress")

                            googleMap.addMarker(
                                MarkerOptions().position(coords).title(adminArea)
                            )

                            // Add a map marker where the user tapped and pan the camera over
                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(coords, 10.0f))

                            recyclerView.adapter = adapter
                            recyclerView.layoutManager = LinearLayoutManager(this@MapsActivity)

                            updateCurrentAddress(firstResult)
                        } else {
                            Log.d("MapsActivity", "No results from geocoder!")

                            val toast = Toast.makeText(
                                this@MapsActivity,
                                getString(R.string.geocoder_no_results),
                                Toast.LENGTH_LONG
                            )
                            toast.show()
                        }
                    }
                }
            }
        }
    }
}