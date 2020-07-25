package com.bustrc101.ammu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraUpdate;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.bustrc101.ammu.model.Data;

public class LiveLocation extends AppCompatActivity implements OnMapReadyCallback,ValueEventListener {
    private MapView mapView;
    private MapboxMap mapboxMap;
    private LatLng currentPosition = new LatLng(64.900932, -18.167040);
    private GeoJsonSource geoJsonSource;
    private ValueAnimator animator;
    private SharedPreferences mPref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(getApplicationContext(), getString(R.string.access_token));
        setContentView(R.layout.activity_live_location);
        mPref = PreferenceManager.getDefaultSharedPreferences(this);
        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        DatabaseReference database = FirebaseDatabase.getInstance().getReference("Users").child("Driver");
        database.child(mPref.getString("TrackId","wV4Ljs3YuJMc6WLJIIsNJvo4a0J2")).addValueEventListener(this);
    }

    @Override
    public void onMapReady(@NonNull final MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;

        geoJsonSource = new GeoJsonSource("source-id",
                Feature.fromGeometry(Point.fromLngLat(10.8505,
                        76.2711)));

        mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull final Style style) {
                style.addImage("marker-icon-id",
                        BitmapFactory.decodeResource(
                                LiveLocation.this.getResources(), R.drawable.mapbox_marker_icon_default));

                style.addSource(geoJsonSource);

                SymbolLayer symbolLayer = new SymbolLayer("layer-id", "source-id");
                symbolLayer.withProperties(PropertyFactory.iconImage("marker-icon-id"));
                style.addLayer(symbolLayer);
            }
        });
    }

                // Map is set up and the style has loaded. Now you can add data or make other map adjustments
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Log.d("Count ", "" + dataSnapshot.getChildrenCount());
                            Log.d("DATABSE", dataSnapshot.toString());

                            try {
                                if (animator != null && animator.isStarted()) {
                                    currentPosition = (LatLng) animator.getAnimatedValue();
                                    animator.cancel();
                                }

                                Data items = dataSnapshot.getValue(Data.class);
                                LatLng point = new LatLng(items.getDlat(), items.getDlan());
                                animator = ObjectAnimator
                                        .ofObject(latLngEvaluator, currentPosition, point)
                                        .setDuration(2000);
                                animator.addUpdateListener(animatorUpdateListener);
                                animator.start();
                                currentPosition = point;



//                                GeoJsonSource geoJsonSource = new GeoJsonSource("source-id", Feature.fromGeometry(
//                                        Point.fromLngLat(items.getDlan(), items.getDlat())));
//                                style.addSource(geoJsonSource);
//
//                                SymbolLayer symbolLayer = new SymbolLayer("layer-id", "source-id");
//                                symbolLayer.withProperties(
//                                        PropertyFactory.iconImage("marker-icon-id")
//                                );
//                                style.addLayer(symbolLayer);
//
                                mapboxMap.animateCamera(CameraUpdateFactory.newLatLngZoom(point,14));



                                Toast.makeText(getApplicationContext(), "Last Updated :" + items.getDtime(), Toast.LENGTH_SHORT).show();


                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
    private final ValueAnimator.AnimatorUpdateListener animatorUpdateListener =
            new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    LatLng animatedPosition = (LatLng) valueAnimator.getAnimatedValue();
                    geoJsonSource.setGeoJson(Point.fromLngLat(animatedPosition.getLongitude(), animatedPosition.getLatitude()));
                }
            };

    private static final TypeEvaluator<LatLng> latLngEvaluator = new TypeEvaluator<LatLng>() {

        private final LatLng latLng = new LatLng();

        @Override
        public LatLng evaluate(float fraction, LatLng startValue, LatLng endValue) {
            latLng.setLatitude(startValue.getLatitude()
                    + ((endValue.getLatitude() - startValue.getLatitude()) * fraction));
            latLng.setLongitude(startValue.getLongitude()
                    + ((endValue.getLongitude() - startValue.getLongitude()) * fraction));
            return latLng;
        }
    };




    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }
}
