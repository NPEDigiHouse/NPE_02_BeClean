package com.example.npe_02_beclean.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.npe_02_beclean.Helpers.Util;
import com.example.npe_02_beclean.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mapbox.api.directions.v5.DirectionsCriteria;
import com.mapbox.api.directions.v5.MapboxDirections;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.core.constants.Constants;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.layers.LineLayer;
import com.mapbox.mapboxsdk.style.layers.Property;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.mapboxsdk.utils.BitmapUtils;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineJoin;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineWidth;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, Callback<DirectionsResponse> {

    private MapView mapView;
    private MapboxMap mapboxMap;
    private String cityNameOriginal, stateNameOriginal, countryNameOriginal;
    private String cityNameDestination, stateNameDestination;
    Point origin, destination;
    private MapboxDirections client;
    private static final String ROUTE_LAYER_ID = "route-layer-id";
    private static final String ROUTE_SOURCE_ID = "route-source-id";
    private static final String ICON_LAYER_ID = "icon-layer-id";
    private static final String ICON_SOURCE_ID = "icon-source-id";
    private static final String RED_PIN_ICON_ID = "red-pin-icon-id";
    Bundle b;
    private TextView tvOriginalLocation, tvDestinationLocation, tvAddressUser, tvNameUser;
    private ImageButton btnBack;
    private Button btnConfirm;
    private double distance = 0.0; // km
    private double duration = 0.0; // minute

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // map attributes
        Mapbox.getInstance(this, getString(R.string.mapbox_access_token));
        setContentView(R.layout.activity_map);

        mapView = findViewById(R.id.mv_map);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        Intent i= getIntent();
        b = i.getExtras();

        // initialize widgets
        View bs = findViewById(R.id.ll_data_map);
        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(bs);
        bottomSheetBehavior.setPeekHeight(60);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

        tvOriginalLocation = findViewById(R.id.tv_original_location);
        tvOriginalLocation.setText("Jl. AP. Pettarani, Makassar");

        tvDestinationLocation = findViewById(R.id.tv_destination_location);
        tvAddressUser = findViewById(R.id.tv_address_user);
        tvNameUser = findViewById(R.id.tv_name_user);

        btnBack = findViewById(R.id.ib_back_map);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnConfirm = findViewById(R.id.btn_confirm_map);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MapActivity.this, String.format("%.2f km %.2f min", distance, duration), Toast.LENGTH_SHORT).show();
            }
        });

        // set user data
        setUserData();

    }


    @Override
    public void onMapReady(@NonNull MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;
        mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                origin = Point.fromLngLat(119.4361434,-5.1456845);
                if(b!=null)
                {
                    destination = (Point) b.get("position");
                }else{
                    destination = Point.fromLngLat(119.503707,-5.1258033);
                }

                // get distance and duration order
                distance = getDistance(-5.1456845, 119.4361434, destination.latitude(), destination.longitude());
                duration = getDuration(distance);

                setDataLocation();
                initSource(style);
                initLayers(style);
                CameraPosition position = new CameraPosition.Builder()
                        .target(new LatLng(destination.latitude(),destination.longitude()))
                        .zoom(11)
                        .tilt(3)
                        .build();
                mapboxMap.getUiSettings().setZoomGesturesEnabled(false);
                mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(position),1000);
                getRoute(mapboxMap, origin, destination);
            }
        });
    }

    private void setUserData() {
        // get user data from firebase
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference()
                .child("users")
                .child(Util.getUserIdLocal(this));
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // set data to widgets
                tvNameUser.setText(snapshot.child("name").getValue().toString());
                tvAddressUser.setText(snapshot.child("address").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MapActivity.this, "Terjadi kesalahan pada database.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private double getDistance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist * 1.60934);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    private double getDuration(double distance) {
        return distance / 40 * 60;
    }

    private void setDataLocation() {
        Geocoder geocoder = new Geocoder(MapActivity.this, Locale.getDefault());
        List<Address> originalAddress = null;
        List<Address> destinationAddress = null;
        try {
            originalAddress = geocoder.getFromLocation(origin.latitude(), origin.longitude(), 1);
            destinationAddress = geocoder.getFromLocation(destination.latitude(), destination.longitude(), 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        cityNameOriginal = originalAddress.get(0).getAddressLine(0);
//        stateNameOriginal = originalAddress.get(0).getAddressLine(1);
//        countryNameOriginal = originalAddress.get(0).getAddressLine(2);
        cityNameDestination = destinationAddress.get(0).getAddressLine(0);
//        stateNameDestination = destinationAddress.get(0).getAddressLine(1);

        tvAddressUser.setText(cityNameOriginal);
        tvDestinationLocation.setText(cityNameDestination);
    }

    private void initSource(@NonNull Style loadedMapStyle) {
        loadedMapStyle.addSource(new GeoJsonSource(ROUTE_SOURCE_ID));
        GeoJsonSource iconGeoJsonSource = new GeoJsonSource(ICON_SOURCE_ID, FeatureCollection.fromFeatures(new Feature[] {
                Feature.fromGeometry(Point.fromLngLat(origin.longitude(), origin.latitude())),
                Feature.fromGeometry(Point.fromLngLat(destination.longitude(), destination.latitude()))}));
        loadedMapStyle.addSource(iconGeoJsonSource);
    }
    private void initLayers(@NonNull Style loadedMapStyle) {
        LineLayer routeLayer = new LineLayer(ROUTE_LAYER_ID, ROUTE_SOURCE_ID);
        routeLayer.setProperties(
                PropertyFactory.lineCap(Property.LINE_CAP_ROUND),
                lineJoin(Property.LINE_JOIN_ROUND),
                lineWidth(5f),
                lineColor(Color.parseColor("#009688"))
        );
        loadedMapStyle.addLayer(routeLayer);
        loadedMapStyle.addImage(RED_PIN_ICON_ID, BitmapUtils.getBitmapFromDrawable(
                getResources().getDrawable(R.drawable.ic_location_pin)));
        loadedMapStyle.addLayer(new SymbolLayer(ICON_LAYER_ID, ICON_SOURCE_ID).withProperties(
                PropertyFactory.iconImage(RED_PIN_ICON_ID),
                PropertyFactory.iconIgnorePlacement(true),
                PropertyFactory.iconAllowOverlap(true),
                PropertyFactory.iconOffset(new Float[] {0f, -9f})));
    }


    @Override
    public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
        if(response.body()==null){
            Toast.makeText(this, "No Routes Found", Toast.LENGTH_SHORT);
            return;
        }else if(response.body().routes().size()<1){
            Toast.makeText(this, "No Routes Found", Toast.LENGTH_SHORT).show();
            return;
        }

        DirectionsRoute currentRoute = response.body().routes().get(0);

        if(mapboxMap!=null){
            mapboxMap.getStyle(new Style.OnStyleLoaded() {
                @Override
                public void onStyleLoaded(@NonNull Style style) {
                    GeoJsonSource source = style.getSourceAs(ROUTE_SOURCE_ID);
                    if(source!=null){
                        source.setGeoJson(LineString.fromPolyline(currentRoute.geometry(), Constants.PRECISION_6));
                    }
                }
            });
        }
    }

    @Override
    public void onFailure(Call<DirectionsResponse> call, Throwable t) {
    }
    private void getRoute(MapboxMap mapboxMap, Point origin, Point destination) {
        client = MapboxDirections.builder()
                .origin(origin)
                .destination(destination)
                .overview(DirectionsCriteria.OVERVIEW_FULL)
                .profile(DirectionsCriteria.PROFILE_DRIVING)
                .accessToken(Mapbox.getAccessToken())
                .build();
        client.enqueueCall(this);
    }
}