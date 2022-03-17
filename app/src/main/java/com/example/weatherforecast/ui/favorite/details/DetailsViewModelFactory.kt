package com.example.weatherforecast.ui.favorite.details

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherforecast.ui.favorite.FavoriteViewModel

class DetailsViewModelFactory(var context: Context) : ViewModelProvider.NewInstanceFactory()  {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return FavoriteDetailsViewModel(context) as T
    }
}
