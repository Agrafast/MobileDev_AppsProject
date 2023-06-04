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





## Progress Notes
- navArgument still dummy


## TODO
- Integrate HomeScreen
  > Ambil data user dan list tanaman dari firebase
- Integrate DetailScreen
  > Ambil data detail dan tutorial tanamana dari firebase
  > Tambahkan fungsionalitas untuk favorite/myplant
- Integrate UserPlantScreen
  > Ambil data dari firebase
  > Tambahkan fungsionalitas untuk hapus favorite
- Offline first (HomeScreen dan UserPlantScreen)
  > Fungsionalitas untuk simpan data ke local
  > Ketika data local tidak ada, baru check ke internet
  > Ketika di background tetap mengambil dari internet dan membandingkan jika ada perubahan
- Autentikasi