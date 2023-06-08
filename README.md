# AgraFast


## Architecture Explanation
- data -> Datasource stuffs
- data.local -> Room, SharedPrefs
- data.network -> Retrofit
- di -> Dependency Injection using Hilt
- domain -> App stuff
- domain.model -> Model stuff that used in app, not the same as local and network model
- ui -> ui stufff
- ui.navigation -> compose navigation stuff
- ui.screen -> structered based the app feature screen

## Task
### Done
- Integrate DetailScreen #Done
  > Ambil data detail dan tutorial tanamana dari firebase #Done
  > Tambahkan fungsionalitas untuk favorite/myplant #Done
  
- Integrate UserPlantScreen
  > Ambil data dari firebase #Done
  > Tambahkan fungsionalitas untuk hapus favorite #Done

- Logo #Done
- Splash Screen #Done

### TODO
- Integrate HomeScreen
  > Ambil data list tanaman dari firebase #Done
  > Ambil data user  dari firebase #WIP by Hafiz

- Offline first (HomeScreen dan UserPlantScreen)
  > Fungsionalitas untuk simpan data ke local
  > Ketika data local tidak ada, baru check ke internet
  > Ketika di background tetap mengambil dari internet dan membandingkan jika ada perubahan

- Autentikasi

### Optional
- Resize Image before go to server
- Elevation based recomedation
- OnBoarding
- 