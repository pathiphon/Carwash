package com.chococard.carwash.ui.main.map

import android.content.Context
import com.chococard.carwash.data.models.User
import com.chococard.carwash.util.extension.loadCircle
import com.chococard.carwash.util.extension.setMarker
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MyLocation(
    context: Context,
    googleMap: GoogleMap?,
    latLng: LatLng,
    user: User?
) {

    init {
        MapFragment.sMarkerMyLocation?.remove()

        context.loadCircle(user?.image) {
            if (googleMap != null) {
                MapFragment.sMarkerMyLocation = googleMap.addMarker(
                    MarkerOptions().apply {
                        position(latLng)
                        icon(BitmapDescriptorFactory.fromBitmap(context.setMarker(it)))
                        title(user?.fullName)
                    }
                )
            }
        }
    }

}
