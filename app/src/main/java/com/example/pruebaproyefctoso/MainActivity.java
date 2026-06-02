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
        
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
    }
}
