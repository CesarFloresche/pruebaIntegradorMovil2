package com.example.pruebaproyefctoso;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.pruebaproyefctoso.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        
        // Se ocultó la ActionBar para que el diseño ocupe toda la pantalla como en los mockups
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
    }
}
