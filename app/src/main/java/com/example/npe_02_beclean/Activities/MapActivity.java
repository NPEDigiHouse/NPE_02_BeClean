package com.example.npe_02_beclean.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.npe_02_beclean.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
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
    TextView tvOriginalLocation, tvDestinationLocation, tvOffice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getString(R.string.mapbox_access_token));
        setContentView(R.layout.activity_map);
        mapView = findViewById(R.id.mv_map);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        View bs = findViewById(R.id.ll_data_map);
        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(bs);
        bottomSheetBehavior.setPeekHeight(60);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);


        tvOriginalLocation = findViewById(R.id.tv_original_location);
        tvDestinationLocation = findViewById(R.id.tv_destination_location);
        tvOffice = findViewById(R.id.tv_cleaner_office);
        Intent i= getIntent();
        b = i.getExtras();

    }


    @Override
    public void onMapReady(@NonNull MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;
        mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                origin = Point.fromLngLat(119.4299981,-5.184244);
                if(b!=null)
                {
                    destination = (Point) b.get("position");
                }else{
                    destination = Point.fromLngLat(119.503707,-5.1258033);
                }
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

        tvOriginalLocation.setText(cityNameOriginal);
        tvOffice.setText(cityNameOriginal);
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