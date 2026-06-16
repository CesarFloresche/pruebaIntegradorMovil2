package com.example.pruebaproyefctoso;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.pruebaproyefctoso.databinding.FragmentHomeBinding;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.CustomZoomButtonsController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private MapView map = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Configuración necesaria para osmdroid
        Context ctx = requireContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        // Es importante establecer un user agent único
        Configuration.getInstance().setUserAgentValue(ctx.getPackageName());
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        map = binding.map;
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setMultiTouchControls(true);
        map.getZoomController().setVisibility(CustomZoomButtonsController.Visibility.NEVER);
        // Configuración inicial del mapa (Cochabamba, Bolivia)
        GeoPoint startPoint = new GeoPoint(-17.3935, -66.1570);
        map.getController().setZoom(15.0);
        map.getController().setCenter(startPoint);
        // Añadir
        Marker startMarker = new Marker(map);
        startMarker.setPosition(startPoint);
        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        startMarker.setTitle("Mi Ubicación");
        map.getOverlays().add(startMarker);
        // Ejemplo trazo
        Polyline line = new Polyline();
        List<GeoPoint> routePoints = new ArrayList<>();
        routePoints.add(new GeoPoint(-17.3935, -66.1570));
        routePoints.add(new GeoPoint(-17.3950, -66.1580));
        routePoints.add(new GeoPoint(-17.4180, -66.1560));
        line.setPoints(routePoints);
        line.setColor(0xFF104EDF); // Azul primario
        line.setWidth(10f);
        map.getOverlays().add(line);

        updateUIBasedOnLoginState();

        // Navegación
        binding.btnOpenLogin.setOnClickListener(v ->
                NavHostFragment.findNavController(this)
                        .navigate(R.id.action_HomeFragment_to_FirstFragment)
        );

        binding.btnFares.setOnClickListener(v ->
                NavHostFragment.findNavController(this)
                        .navigate(R.id.action_HomeFragment_to_FaresFragment)
        );

        binding.btnHistory.setOnClickListener(v ->
                NavHostFragment.findNavController(this)
                        .navigate(R.id.action_HomeFragment_to_ReportsFragment)
        );

        binding.btnProfile.setOnClickListener(v ->
                NavHostFragment.findNavController(this)
                        .navigate(R.id.action_HomeFragment_to_DriverProfileFragment)
        );

        binding.fabComplaint.setOnClickListener(v ->
                NavHostFragment.findNavController(this)
                        .navigate(R.id.action_HomeFragment_to_ComplaintFragment)
        );

        binding.btnLogout.setOnClickListener(v -> {
            SharedPreferences prefs = getActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
            prefs.edit().putBoolean("is_logged_in", false).remove("user_name").apply();
            updateUIBasedOnLoginState();
        });
    }

    private void updateUIBasedOnLoginState() {
        SharedPreferences prefs = getActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        boolean isLoggedIn = prefs.getBoolean("is_logged_in", false);
        String userName = prefs.getString("user_name", "Usuario Demo");

        if (isLoggedIn) {
            binding.btnOpenLogin.setVisibility(View.GONE);
            binding.userInfoCard.setVisibility(View.VISIBLE);
            binding.btnLogout.setVisibility(View.VISIBLE);
            binding.tvUserNameHome.setText(userName);
        } else {
            binding.btnOpenLogin.setVisibility(View.VISIBLE);
            binding.userInfoCard.setVisibility(View.GONE);
            binding.btnLogout.setVisibility(View.GONE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (map != null) map.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (map != null) map.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
