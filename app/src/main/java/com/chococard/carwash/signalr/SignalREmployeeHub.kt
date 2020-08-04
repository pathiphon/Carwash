package com.chococard.carwash.signalr

import com.chococard.carwash.data.networks.BASE_URL
import com.chococard.carwash.data.networks.response.JobResponse
import com.google.gson.Gson
import com.microsoft.signalr.HubConnection
import com.microsoft.signalr.HubConnectionBuilder
import com.microsoft.signalr.HubConnectionState
import com.microsoft.signalr.TransportEnum

class SignalREmployeeHub(employerId: Int?, listener: SignalRListener) {

    private val hubConnection: HubConnection = HubConnectionBuilder
        .create("${BASE_URL}carwash/signalr/employeehub")
        .withTransport(TransportEnum.LONG_POLLING)
        .build()

    init {
        hubConnection.on("ReceiveEmployee$employerId", {
            val fromJson = Gson().fromJson(it, JobResponse::class.java)
            listener.onReceive(fromJson)
        }, String::class.java)

        startSignalR()
    }

    private fun startSignalR() {
        if (hubConnection.connectionState == HubConnectionState.DISCONNECTED)
            hubConnection.start()
    }

    interface SignalRListener {
        fun onReceive(job: JobResponse)
    }

}
