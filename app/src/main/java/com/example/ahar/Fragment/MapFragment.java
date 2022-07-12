package com.example.ahar.Fragment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.ahar.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by Amirul Simon on 05/05/21.
 * Updated by Istiak Saif 08/05/21.
 */
public class MapFragment extends Fragment {

    private FusedLocationProviderClient client;
    private SupportMapFragment mapFragment;
    private int REQUEST_CODE = 111;
    private ConnectivityManager connectivityManager;
    private NetworkInfo networkInfo;
    private GoogleMap map,googlemap;
    private Geocoder geocoder;
    private double selectedlat, selectedlng;
    private List<Address> addressList;
    private String string;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.maps);

        client = LocationServices.getFusedLocationProviderClient(getActivity());

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation();
        }else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION
            },REQUEST_CODE);
        }
    }

    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Task<Location> task = client.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location!= null){
                    mapFragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(GoogleMap googleMap) {
                            map = googleMap;
                            googlemap = googleMap;
                            getAddress(location.getLatitude(),location.getLongitude());
//                            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
//
//                            MarkerOptions markerOptions = new MarkerOptions().position(latLng).title(string);
//
//                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));
//
//                            googleMap.addMarker(markerOptions).showInfoWindow();

                            map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                                @Override
                                public void onMapClick(LatLng latLng) {
                                    connectionCheck();
                                    if(networkInfo.isConnected() && networkInfo.isAvailable()){

                                        selectedlat = latLng.latitude;
                                        selectedlng = latLng.longitude;

                                        getAddress(selectedlat,selectedlng);

                                    }else {
                                        Toast.makeText(getActivity(), "Connection check", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == REQUEST_CODE){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getCurrentLocation();
            }
        }else {
            Toast.makeText(getActivity(), "Permission Denied", Toast.LENGTH_SHORT).show();
        }
    }

    private void connectionCheck(){
        connectivityManager = (ConnectivityManager) getActivity().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connectivityManager.getActiveNetworkInfo();

    }
    private void getAddress(double mlat, double mlng){
        geocoder = new Geocoder(getActivity(), Locale.getDefault());
        if(mlat != 0){
            try {
                addressList = geocoder.getFromLocation(mlat,mlng,1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(addressList != null){
                String maddress = addressList.get(0).getAddressLine(0);

                string = maddress;

                if(maddress != null){
                    MarkerOptions markerOptions = new MarkerOptions();
                    LatLng latLng = new LatLng(mlat,mlng);
                    markerOptions.position(latLng).title(string);
                    map.addMarker(markerOptions).showInfoWindow();
                    googlemap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));
                    googlemap.addMarker(markerOptions).showInfoWindow();
                }else {
                    Toast.makeText(getActivity(), "Something error", Toast.LENGTH_SHORT).show();
                }
            }else {
                Toast.makeText(getActivity(), "Something error", Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(getActivity(), "LatLng null", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        return view;
    }
}