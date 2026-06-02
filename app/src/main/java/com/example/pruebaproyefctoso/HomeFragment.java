package com.example.pruebaproyefctoso;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import com.example.pruebaproyefctoso.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        updateUIBasedOnLoginState();

        // Navegación a Login
        binding.btnOpenLogin.setOnClickListener(v ->
                NavHostFragment.findNavController(this)
                        .navigate(R.id.action_HomeFragment_to_FirstFragment)
        );

        // Navegación a Tarifas
        binding.btnFares.setOnClickListener(v ->
                NavHostFragment.findNavController(this)
                        .navigate(R.id.action_HomeFragment_to_FaresFragment)
        );

        // Navegación a Historial / Reportes
        binding.btnHistory.setOnClickListener(v ->
                NavHostFragment.findNavController(this)
                        .navigate(R.id.action_HomeFragment_to_ReportsFragment)
        );

        // Navegación a Perfil del Chofer
        binding.btnProfile.setOnClickListener(v ->
                NavHostFragment.findNavController(this)
                        .navigate(R.id.action_HomeFragment_to_DriverProfileFragment)
        );

        // Navegación a Nueva Denuncia
        binding.fabComplaint.setOnClickListener(v ->
                NavHostFragment.findNavController(this)
                        .navigate(R.id.action_HomeFragment_to_ComplaintFragment)
        );

        // Cerrar sesión
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
            
            View innerView = binding.userInfoCard.getChildAt(0);
            if (innerView instanceof TextView) {
                ((TextView) innerView).setText(userName);
            }
        } else {
            binding.btnOpenLogin.setVisibility(View.VISIBLE);
            binding.userInfoCard.setVisibility(View.GONE);
            binding.btnLogout.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
