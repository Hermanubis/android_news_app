package edu.gwu.myapplication

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.location.Address
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
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
//        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_maps)

        cardView = findViewById(R.id.map_card)
        textView = findViewById(R.id.mapTerm)
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
        val preferences: SharedPreferences = getSharedPreferences("android-news", Context.MODE_PRIVATE)

        mMap = googleMap
        val newsAPI = getString(R.string.newsAPI)
        val newsManager: newsManager = newsManager()

        var lat: Double= preferences.getString("prevLat", "0.0")!!.toDouble()
        var lon: Double = preferences.getString("prevLon", "0.0")!!.toDouble()
        var sharedCoord = LatLng(lat, lon)

        doAsync {
            val geocoder: Geocoder = Geocoder(this@MapsActivity)

            // In Kotlin, you can assign the result of a try-catch block. Both the "try" and
            // "catch" clauses need to yield a valid value to assign.
            val results: List<Address> = try {
                geocoder.getFromLocation(sharedCoord.latitude, sharedCoord.longitude, 10)
            } catch (exception: Exception) {
                // Uses the error logger to print the error
//                    Log.e("MapsActivity", "Geocoding failed", exception)
//                    exception.printStackTrace()

                listOf()
            }

            if(results.isEmpty()){
                Toast.makeText(
                    this@MapsActivity,
                    getString(R.string.error_prevLocation),
                    Toast.LENGTH_LONG
                ).show()
            }
            else{
                val editor = preferences.edit()
                editor.putString("lat", sharedCoord.latitude.toString())
                editor.apply()
                val editor1 = preferences.edit()
                editor1.putString("lon", sharedCoord.longitude.toString())
                editor1.apply()
                val articles: List<news> = try {
                    newsManager.retrieveMapNews(newsAPI, results)
                } catch(exception: Exception) {
//                        Log.e("resultActivity", "Retrieving news failed!", exception)
                    listOf<news>()
                }

                runOnUiThread {
                    if(articles.isNotEmpty()){
                        adapter = newsAdapter(articles)
                        textView.text = "Results from ${results[0].countryName}"
                        recyclerView.adapter = adapter
                        // Potentially, we could show all results to the user to choose from,
                        // but for our usage it's sufficient enough to just use the first result.
                        // The Geocoder's first result is often the "best" one in terms of its accuracy / confidence.
                        val firstResult: Address = results[0]
                        val postalAddress: String = firstResult.getAddressLine(0)
                        val adminArea = results[0].adminArea
                        val country = results[0].countryName

                        mMap.addMarker(
                            MarkerOptions().position(sharedCoord).title(adminArea)
                        )

                        // Add a map marker where the user tapped and pan the camera over
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sharedCoord, 10.0f))

                        cardView.visibility = View.VISIBLE
                        recyclerView.adapter = adapter
                        recyclerView.layoutManager = LinearLayoutManager(this@MapsActivity, LinearLayoutManager.HORIZONTAL, false)

                        updateCurrentAddress(firstResult)
                    }
                    else{
                        Toast.makeText(
                            this@MapsActivity,
                            getString(R.string.error_Location_news),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }

        mMap.setOnMapLongClickListener { coords: LatLng ->
            mMap.clear()


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
//                    Log.e("MapsActivity", "Geocoding failed", exception)
//                    exception.printStackTrace()

                    listOf()
                }

                if(results.isEmpty()){
                    textView.text = getString(R.string.error_Location)
                    Toast.makeText(
                        this@MapsActivity,
                        getString(R.string.error_Location),
                        Toast.LENGTH_LONG
                    ).show()
                }
                else{
                    val editor = preferences.edit()
                    editor.putString("lat", coords.latitude.toString())
                    editor.apply()
                    val editor1 = preferences.edit()
                    editor1.putString("lon", coords.longitude.toString())
                    editor1.apply()
                    val articles: List<news> = try {
                        newsManager.retrieveMapNews(newsAPI, results)
                    } catch(exception: Exception) {
//                        Log.e("resultActivity", "Retrieving news failed!", exception)
                        listOf<news>()
                    }

                    runOnUiThread {
                        if(articles.isNotEmpty()){
                            adapter = newsAdapter(articles)
                            textView.text = "Results from ${results[0].countryName}"
                            recyclerView.adapter = adapter
                            // Potentially, we could show all results to the user to choose from,
                            // but for our usage it's sufficient enough to just use the first result.
                            // The Geocoder's first result is often the "best" one in terms of its accuracy / confidence.
                            val firstResult: Address = results[0]
                            val postalAddress: String = firstResult.getAddressLine(0)
                            val adminArea = results[0].adminArea
                            val country = results[0].countryName

                            mMap.addMarker(
                                MarkerOptions().position(coords).title(adminArea)
                            )

                            // Add a map marker where the user tapped and pan the camera over
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(coords, 10.0f))

                            cardView.visibility = View.VISIBLE
                            recyclerView.adapter = adapter
                            recyclerView.layoutManager = LinearLayoutManager(this@MapsActivity, LinearLayoutManager.HORIZONTAL, false)

                            updateCurrentAddress(firstResult)
                        }
                        else{
                            Toast.makeText(
                                this@MapsActivity,
                                getString(R.string.error_Location_news),
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }
            }
        }
    }
}