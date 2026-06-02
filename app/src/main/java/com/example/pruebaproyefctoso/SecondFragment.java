package com.example.pruebaproyefctoso;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import com.example.pruebaproyefctoso.databinding.FragmentSecondBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;

public class SecondFragment extends Fragment {
    private FragmentSecondBinding binding;
    private FirebaseAuth mAuth;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSecondBinding.inflate(inflater, container, false);
        mAuth = FirebaseAuth.getInstance();
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.btnRegister.setOnClickListener(v -> registerUser());
        binding.tvLogin.setOnClickListener(v -> NavHostFragment.findNavController(this).navigateUp());
        binding.btnBackRegister.setOnClickListener(v -> getActivity().onBackPressed());
    }

    private void registerUser() {
        String name = binding.etName.getText().toString().trim();
        String email = binding.etEmailRegister.getText().toString().trim();
        String pass = binding.etPasswordRegister.getText().toString().trim();

        if (email.isEmpty() || pass.isEmpty() || name.isEmpty()) {
            Toast.makeText(getContext(), "Por favor llena todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        binding.btnRegister.setEnabled(false);
        binding.btnRegister.setText("Creando cuenta...");

        mAuth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Guardar el nombre en el perfil de Firebase
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName(name).build();
                        task.getResult().getUser().updateProfile(profileUpdates);

                        Toast.makeText(getContext(), "¡Cuenta creada con éxito!", Toast.LENGTH_SHORT).show();
                        NavHostFragment.findNavController(this).navigate(R.id.action_SecondFragment_to_FirstFragment);
                    } else {
                        Toast.makeText(getContext(), "Error: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        binding.btnRegister.setEnabled(true);
                        binding.btnRegister.setText("Crear Cuenta");
                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}