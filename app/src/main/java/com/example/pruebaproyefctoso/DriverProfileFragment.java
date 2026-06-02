package com.example.pruebaproyefctoso;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.pruebaproyefctoso.databinding.FragmentDriverProfileBinding;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class DriverProfileFragment extends Fragment {
    private FragmentDriverProfileBinding binding;
    private FirebaseFirestore db; // Instancia de Firebase

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDriverProfileBinding.inflate(inflater, container, false);
        db = FirebaseFirestore.getInstance(); // Inicializar Firebase
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.toolbarProfile.setNavigationOnClickListener(v -> NavHostFragment.findNavController(this).navigateUp());

        binding.btnSendRating.setOnClickListener(v -> sendRatingToFirebase());
    }

    private void sendRatingToFirebase() {
        float rating = binding.ratingBarDriver.getRating();
        String comment = binding.etRatingComment.getText().toString().trim();

        if (rating == 0) {
            Toast.makeText(getContext(), "Por favor selecciona una puntuación", Toast.LENGTH_SHORT).show();
            return;
        }

        binding.btnSendRating.setEnabled(false);
        binding.btnSendRating.setText("Enviando...");

        // Preparar datos para Firestore
        Map<String, Object> data = new HashMap<>();
        data.put("puntos", rating);
        data.put("comentario", comment);
        data.put("fecha", Timestamp.now());

        // Guardar en la colección "calificaciones"
        db.collection("calificaciones")
                .add(data)
                .addOnSuccessListener(documentReference -> {
                    if (isAdded()) {
                        Toast.makeText(getContext(), "¡Gracias por calificar!", Toast.LENGTH_SHORT).show();
                        NavHostFragment.findNavController(this).navigateUp();
                    }
                })
                .addOnFailureListener(e -> {
                    if (isAdded()) {
                        Toast.makeText(getContext(), "Error al conectar con la nube", Toast.LENGTH_SHORT).show();
                        resetButton();
                    }
                });
    }

    private void resetButton() {
        if (binding != null) {
            binding.btnSendRating.setEnabled(true);
            binding.btnSendRating.setText(getString(R.string.send_rating));
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}