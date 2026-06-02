package com.example.pruebaproyefctoso;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.pruebaproyefctoso.databinding.FragmentComplaintBinding;
import com.google.android.material.chip.Chip;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ComplaintFragment extends Fragment {
    private FragmentComplaintBinding binding;
    private FirebaseFirestore db;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentComplaintBinding.inflate(inflater, container, false);
        db = FirebaseFirestore.getInstance();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.toolbarComplaint.setNavigationOnClickListener(v -> NavHostFragment.findNavController(this).navigateUp());

        binding.btnSendComplaint.setOnClickListener(v -> sendComplaintToFirebase());
    }

    private void sendComplaintToFirebase() {
        String plate = binding.etPlate.getText().toString().trim();
        String details = binding.etDetails.getText().toString().trim();
        
        int selectedChipId = binding.chipGroupComplaint.getCheckedChipId();
        if (plate.isEmpty() || selectedChipId == View.NO_ID) {
            Toast.makeText(getContext(), "Ingresa la placa y selecciona un tipo de queja", Toast.LENGTH_SHORT).show();
            return;
        }

        Chip chip = binding.getRoot().findViewById(selectedChipId);
        String complaintType = chip.getText().toString();

        // Bloquear botón para evitar múltiples clics
        binding.btnSendComplaint.setEnabled(false);
        binding.btnSendComplaint.setText("Guardando en la nube...");

        // Preparar datos para Firebase Firestore
        Map<String, Object> complaint = new HashMap<>();
        complaint.put("placa", plate);
        complaint.put("tipo_queja", complaintType);
        complaint.put("detalles", details);
        complaint.put("fecha", Timestamp.now());

        // Guardar en la colección "denuncias"
        db.collection("denuncias")
                .add(complaint)
                .addOnSuccessListener(documentReference -> {
                    if (isAdded()) {
                        Toast.makeText(getContext(), "¡Denuncia guardada con éxito!", Toast.LENGTH_SHORT).show();
                        NavHostFragment.findNavController(this).navigateUp();
                    }
                })
                .addOnFailureListener(e -> {
                    if (isAdded()) {
                        Toast.makeText(getContext(), "Error al conectar con Firebase: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        resetButton();
                    }
                });
    }

    private void resetButton() {
        if (binding != null) {
            binding.btnSendComplaint.setEnabled(true);
            binding.btnSendComplaint.setText("Enviar Denuncia");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
