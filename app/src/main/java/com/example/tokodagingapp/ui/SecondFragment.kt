package com.example.tokodagingapp.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.tokodagingapp.R
import com.example.tokodagingapp.application.MealApp
import com.example.tokodagingapp.databinding.FragmentSecondBinding
import com.example.tokodagingapp.model.Meal
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMarkerDragListener {

    private var _binding: FragmentSecondBinding? = null

    private val binding get() = _binding!!
    private lateinit var applicationContext: Context
    private val mealViewModel: MealViewModel by viewModels {
        MealViewModelFactory((applicationContext as MealApp).repository)
    }
    private val args: SecondFragmentArgs by navArgs()
    private  var meal: Meal? = null
    private lateinit var mMap: GoogleMap
    private var currentLatLang: LatLng? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val cameraRequestCode = 2
    override fun onAttach(context: Context) {
        super.onAttach(context)
        applicationContext = requireContext().applicationContext
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        meal = args.meal
        if (meal != null){
            binding.deleteButton.visibility = View.VISIBLE
            binding.saveButton.text = "Ubah"
            binding.mealEditText.setText(meal?.name)
            binding.addressEditText.setText(meal?.address)
            binding.countEditText.setText(meal?.count)

        }

        //binding google map
        val mapFragment = childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        chechkPermision()

        val name = binding.mealEditText.text
        val address = binding.addressEditText.text
        val count = binding.countEditText.text


        binding.saveButton.setOnClickListener {
            if (name.isEmpty()){
                Toast.makeText(context, "Jenis daging tidak boleh kosong", Toast.LENGTH_SHORT).show()
            }else if (address.isEmpty()) {
                Toast.makeText(context, "Alamat tidak boleh kosong", Toast.LENGTH_SHORT).show()
            }else if (count.isEmpty()) {
                Toast.makeText(context, "Jumlah pesanan tidak boleh kosong", Toast.LENGTH_SHORT).show()
            }else{
                if (meal == null){
                    val meal =Meal(0, name.toString(), count.toString(),address.toString(), currentLatLang?.latitude, currentLatLang?.longitude )
                    mealViewModel.insert(meal)
                }else{
                    val meal =Meal(meal?.id!!, name.toString(), count.toString(),address.toString(),currentLatLang?.latitude, currentLatLang?.longitude )
                    mealViewModel.update(meal)
                }

                findNavController().popBackStack()
            }

        }
        binding.deleteButton.setOnClickListener {
            meal?.let {mealViewModel.delete(it) }
            findNavController().popBackStack()
        }
        binding.cameraButton.setOnClickListener {
            checkCameraPermission()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        //implement drag marker

        val uiSetting = mMap.uiSettings
        uiSetting.isZoomControlsEnabled = true
        mMap.setOnMarkerDragListener(this)
    }

    override fun onMarkerDrag(p0: Marker) {
    }

    override fun onMarkerDragEnd(marker: Marker) {
        val newPosition = marker.position
        currentLatLang = LatLng(newPosition.latitude, newPosition.longitude)
        Toast.makeText(context, currentLatLang.toString(), Toast.LENGTH_SHORT).show()
    }

    override fun onMarkerDragStart(p0: Marker) {
    }

    private fun chechkPermision(){
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(applicationContext)
        if (ContextCompat.checkSelfPermission(
            applicationContext,
            android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        ){
            getCurrentLocation()
        }else{
            Toast.makeText(applicationContext, "Akses Lokasi di Tolak", Toast.LENGTH_SHORT).show()
        }
    }
    private fun getCurrentLocation(){
        //mengecekjika perission tidka di setujui maka akan berhenti di kondisi if
        if (ContextCompat.checkSelfPermission(
                applicationContext,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ){
            return
        }
        //untuk test current location coba run di device atau build apk nya terus di install di hp masing-masing
        fusedLocationClient.lastLocation
            .addOnSuccessListener {location ->
                if (location != null){
                    var latLang = LatLng(location.latitude, location.longitude)
                    currentLatLang = latLang
                    var title = "Marker"
                    if (meal != null){
                        title = meal?.name.toString()
                        val newCurrentLocation = LatLng(meal?.latitude!!, meal?.longitude!!)
                        latLang = newCurrentLocation
                    }
                    val markerOption = MarkerOptions()
                        .position(latLang)
                        .title(title)
                        .draggable(true)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_meal_32))
                    mMap.addMarker(markerOption)
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLang,15f))
                }
            }

    }
    private fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(
                applicationContext,
                android.Manifest.permission.CAMERA) !=PackageManager.PERMISSION_GRANTED){
            activity?.let {
                ActivityCompat.requestPermissions(
                    it,
                    arrayOf(android.Manifest.permission.CAMERA),
                    cameraRequestCode
                )
            }
        }else{
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(cameraIntent, cameraRequestCode)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == cameraRequestCode){
            val photo: Bitmap = data?.extras?.get("data") as Bitmap
            binding.photoImageView.setImageBitmap(photo)
        }else{}
    }
}