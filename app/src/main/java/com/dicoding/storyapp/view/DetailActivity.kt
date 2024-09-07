package com.dicoding.storyapp.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.dicoding.storyapp.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
    private lateinit var binding : ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val photo = intent.getStringExtra("key_photo")
        val nama = intent.getStringExtra("key_nama")
        val desc = intent.getStringExtra("key_desc")

        Glide.with(this)
            .load(photo)
            .into(binding.detailImage)
        binding.detailNama.text = nama
        binding.detailDesc.text = desc
    }
}