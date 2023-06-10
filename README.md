# AgraFast

## Task
### Done
- Integrate DetailScreen #Done
  - Ambil data detail dan tutorial tanamana dari firebase #Done
  - Tambahkan fungsionalitas untuk favorite/myplant #Done
  
- Integrate UserPlantScreen #Done
  - Ambil data dari firebase #Done
  - Tambahkan fungsionalitas untuk hapus favorite #Done

- Logo #Done
- Splash Screen #Done

- Integrate HomeScreen #Done
  - Ambil data list tanaman dari firebase #Done
  - Ambil data user  dari firebase #Done

- Autentikasi Basic (Login, Register) #Done
### TODO

- Offline first (HomeScreen dan UserPlantScreen)
  > Fungsionalitas untuk simpan data ke local
  > Ketika data local tidak ada, baru check ke internet
  > Ketika di background tetap mengambil dari internet dan membandingkan jika ada perubahan


### Optional 
- Resize Image before go to server
- Elevation based recomedation 
  ```agsl
  {
  "results": [
    {
      "elevation": 1608.637939453125,
      "location": {
        "lat": 39.7391536,
        "lng": -104.9847034
      },
      "resolution": 4.771975994110107
    }
  ],
  "status": "OK"
}
  ```
- OnBoarding
- Image Placeholder