package com.akyasis.havaspikeri.havaspikeri;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.loopj.android.http.*;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import cz.msebera.android.httpclient.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    final AsyncHttpClient client = new AsyncHttpClient();
    final RequestParams params = new RequestParams();

    int derece;
    String baslik,uyari,wserviceDurum;
    String msg = "Android : ";
    String secilenSehir="Kocaeli";
    ImageView resim;
    TextView txtBaslik,txtUyari,txtDerece;
    Spinner sehirler;
    Toast toastObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        baslik="-";
        uyari="-";

        txtBaslik=(TextView)findViewById(R.id.txtBaslik);
        txtUyari=(TextView)findViewById(R.id.txtUyari);
        txtDerece=(TextView)findViewById(R.id.txtDerece);
        sehirler = (Spinner) findViewById(R.id.drpSehir);

        String[] arraySpinner;

        arraySpinner = new String[] {
                "Adana", "Adıyaman", "Afyon", "Ağrı", "Amasya", "Ankara", "Antalya", "Artvin",
                "Aydın", "Balıkesir", "Bilecik", "Bingöl", "Bitlis", "Bolu", "Burdur", "Bursa", "Çanakkale",
                "Çankırı", "Çorum", "Denizli", "Diyarbakır", "Edirne", "Elazığ", "Erzincan", "Erzurum", "Eskişehir",
                "Gaziantep", "Giresun", "Gümüşhane", "Hakkari", "Hatay", "Isparta", "Mersin", "İstanbul", "İzmir",
                "Kars", "Kastamonu", "Kayseri", "Kırklareli", "Kırşehir", "Kocaeli", "Konya", "Kütahya", "Malatya",
                "Manisa", "Kahramanmaraş", "Mardin", "Muğla", "Muş", "Nevşehir", "Niğde", "Ordu", "Rize", "Sakarya",
                "Samsun", "Siirt", "Sinop", "Sivas", "Tekirdağ", "Tokat", "Trabzon", "Tunceli", "Şanlıurfa", "Uşak",
                "Van", "Yozgat", "Zonguldak", "Aksaray", "Bayburt", "Karaman", "Kırıkkale", "Batman", "Şırnak",
                "Bartın", "Ardahan", "Iğdır", "Yalova", "Karabük", "Kilis", "Osmaniye", "Düzce"
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arraySpinner);
        sehirler.setAdapter(adapter);

        sehirler.setSelection(40);
        sehirler.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
             secilenSehir = sehirler.getSelectedItem().toString();
                new HavaDurumu().execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
        txtBaslik.setText(baslik);
        txtUyari.setText(uyari);
        txtDerece.setText(String.valueOf(derece)+ " Derece");

        resim=(ImageView)findViewById(R.id.imgHavaDurumu);
        resim.setImageResource(R.drawable.gunesli);
    }

    //Openweathermap üzerinden JSON web service ile illere ait hava durumlarını çeker
    private class HavaDurumu extends AsyncTask<Void,Void,Void> {
        int tempNo;
        String descriptionN;
        String countryName;
        String name;
        Double enlem, boylam;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

           toastObject=  Toast.makeText(getApplicationContext(), "Hava Durumu Yükleniyor", Toast.LENGTH_SHORT);
            toastObject.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {


            String weatherUrl = "http://api.openweathermap.org/data/2.5/weather?q="+secilenSehir+"&appid=d8723167fb4faa45605ca86fde5a9c53";
            JSONObject jsonObject = null;

            try {
                String json = JSONParser.getJSONFromUrl(weatherUrl);
                try {
                    jsonObject = new JSONObject(json);
                } catch (JSONException e) {
                    Log.e("JSONPARSER", "Error creating Json Object" + e.toString());
                }

                //En baştaki json objesinden weather adlı array'ı çek
                JSONArray listArray = jsonObject.getJSONArray("weather");
                //weather'in ilk objesini çek
                JSONObject firstObj=listArray.getJSONObject(0);
                //Bu alanda description'i çek
                baslik = firstObj.getString("description");


                // objelerden main'i çek
                JSONObject main = jsonObject.getJSONObject("main");
                //Sıcaklık
                tempNo = main.getInt("temp");


                //Ulke
             //   JSONObject country = jsonObject.getJSONObject("sys");
               // countryName = country.getString("country");

                //koordinat

              //  JSONObject koord = jsonObject.getJSONObject("coord");
             //   enlem = koord.getDouble("lat");
                //boylam = koord.getDouble("lon");

            } catch (JSONException e) {
                Log.e("json", "doINbackgrond");

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void args) {

            //sehir.setText("Sehir: " + name);
            //  ulke.setText("Ülke: " + countryName);
            if ( tempNo!=0) {
                tempNo -= 273;
                derece = tempNo;
                txtDerece.setText("Sıcaklık: " + tempNo + "\u2103");
                //    koordinat.setText("Koordinatlar:\n Enlem: " + enlem + "\n Boylam: " + boylam);

                txtBaslik.setText(baslik);



                durumKontrol();


                webservice_calistir();
                toastObject.cancel();
            }
            else
                new HavaDurumu().execute();


        }

    }
        public void mesajGoster(String mesaj) {
        Toast.makeText(getApplicationContext(), mesaj, Toast.LENGTH_LONG)
                .show();
    }

    //Durumları Türkçeye Çevir ve Fotoğrafları Getir
    public  void durumKontrol()
    {

        Boolean kontrol=false;
        if ( baslik.equals("thunderstorm with rain"))
        {
            baslik="Gök Gürültülü Sağanak Yağışlı";
            //  uyari="Güneş kremini kullanmayı ihmal etme!";

            txtBaslik.setText(baslik);
            // txtUyari.setText(uyari);
            // txtDerece.setText(String.valueOf(derece)+ " Derece");

            resim.setImageResource(R.drawable.gok_gurultulu);

            kontrol=true;
        }

        if ( baslik.equals("snow") || baslik.equals("light snow") ||  baslik.equals("heavy snow") || baslik.equals("shower snow") || baslik.equals("light shower snow"))
        {
            baslik="Kar Yağışlı";
            //  uyari="Güneş kremini kullanmayı ihmal etme!";

            txtBaslik.setText(baslik);
            // txtUyari.setText(uyari);
            // txtDerece.setText(String.valueOf(derece)+ " Derece");

            resim.setImageResource(R.drawable.karli);

            kontrol=true;
        }

        if ( baslik.equals("light rain") || baslik.equals("moderate rain")|| baslik.equals("light intensity shower rain") || baslik.equals("shower rain") || baslik.equals("light shower rain") || baslik.equals("shower sleet") )
        {
            baslik="Yağışlı";
            //  uyari="Güneş kremini kullanmayı ihmal etme!";

            txtBaslik.setText(baslik);
            // txtUyari.setText(uyari);
            // txtDerece.setText(String.valueOf(derece)+ " Derece");

            resim.setImageResource(R.drawable.yagmurlu);

            kontrol=true;
        }

        if ( baslik.equals("rain") || baslik.equals("heavy intensity rain"))
        {
            baslik="Sağanak Yağışlı";
            //  uyari="Güneş kremini kullanmayı ihmal etme!";

            txtBaslik.setText(baslik);
            // txtUyari.setText(uyari);
            // txtDerece.setText(String.valueOf(derece)+ " Derece");

            resim.setImageResource(R.drawable.saganak_yagisli);

            kontrol=true;
        }
        if ( baslik.equals("clear sky") || baslik.equals("clear sky"))
        {
            baslik="Güneşli";
            //  uyari="Güneş kremini kullanmayı ihmal etme!";

            txtBaslik.setText(baslik);
            // txtUyari.setText(uyari);
            // txtDerece.setText(String.valueOf(derece)+ " Derece");

            resim.setImageResource(R.drawable.cok_gunesli);

            kontrol=true;
        }

        if ( baslik.equals("haze") || baslik.equals("mist"))
        {
            baslik="Sisli";
            //  uyari="Güneş kremini kullanmayı ihmal etme!";

            txtBaslik.setText(baslik);
            // txtUyari.setText(uyari);
            // txtDerece.setText(String.valueOf(derece)+ " Derece");

            resim.setImageResource(R.drawable.gunesli);

            kontrol=true;
        }

        if ( baslik.equals("broken clouds") || baslik.equals("overcast clouds") || baslik.equals("scattered clouds") || baslik.equals("few clouds"))
        {
            baslik="Parçalı Bulutlu";
            //  uyari="Güneş kremini kullanmayı ihmal etme!";

            txtBaslik.setText(baslik);
            // txtUyari.setText(uyari);
            // txtDerece.setText(String.valueOf(derece)+ " Derece");

            resim.setImageResource(R.drawable.gunesli);

            kontrol=true;
        }

        if(!kontrol)

        {

            //  uyari="Güneş kremini kullanmayı ihmal etme!";

            txtBaslik.setText(baslik);
            // txtUyari.setText(uyari);
            // txtDerece.setText(String.valueOf(derece)+ " Derece");

            resim.setImageResource(R.drawable.gunesli);

        }
    }

    //C# Web Service Üzerinden Sıcaklıkla İlgili Uyarıları Çeker
    public  void  webservice_calistir()
    {

        Thread th = new Thread() {

            @Override
            public void run() {

                WebServices ws = new WebServices();
                uyari=  ws.uyariOku(derece);




            }
        };
        th.start();

        try {
            th.join();
            txtUyari.setText(uyari);
            bildirim(uyari,String.valueOf( txtDerece.getText()),baslik);
        } catch (InterruptedException e) {
            // ...
        }
    }

    //Bildirim
    public void bildirim(String mesaj,String sicaklik, String durum)
    {
        NotificationManager bildirimYapici = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        PendingIntent butonlar = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Notification bildirim = new Notification.Builder(getApplicationContext())
                .setContentTitle("Hava Spikeri") // ana ve büyük yazı
                .setContentText(mesaj) // alttaki küçük yazı
                .setContentInfo(sicaklik)
                .setSubText(durum)
                .setPriority(Notification.PRIORITY_MAX) // butonları gösterir.Bunu yazmassan aşağı çekince gösterir
                .setSmallIcon(android.R.drawable.ic_dialog_info) // yukarda çıkan,bildirim panelini açında sağa geçen küçük ikon
                .setLargeIcon(((BitmapDrawable) getResources().getDrawable(android.R.drawable.ic_menu_info_details)).getBitmap()) // bildirim panelini açtıktan sonra yazının solundaki ana ikon
                .setTicker("Uyarı!") // yukarıda okunan yazı
                .setContentIntent(butonlar) // butonları gömer
                .setAutoCancel(true) // basıldığında kapama
                //.setDefaults(Notification.DEFAULT_SOUND)
                .setSound(uri) // ya da Settings.System.DEFAULT_NOTIFICATION_URI       ya da    .setDefaults(Notification.DEFAULT_SOUND)
                .setVibrate(new long[]{0000, 1000}) // ya da     .setDefaults(Notification.DEFAULT_VIBRATE)

                //.addAction(android.R.drawable.ic_menu_directions, "Aç", butonlar)
                //.addAction(android.R.drawable.ic_delete, "Kapat", butonlar)
                .build();
        bildirimYapici.notify(0, bildirim);
    }

    //YAŞAM DÖNGÜSÜ
    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        Log.d(msg, "onStart() metodu");
        Toast.makeText(getApplicationContext(), "Hoşgeldin", Toast.LENGTH_LONG)
                .show();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        Log.d(msg, "onResume() metodu");
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        Log.d(msg, "onPause() metodu");
    }

    @Override
    protected void onStop(){
        // TODO Auto-generated method stub
        super.onStop();
        Log.d(msg, "onStop() metodu");
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        Log.d(msg, "onDestroy() metodu");

        Toast.makeText(getApplicationContext(), "Görüşürüz", Toast.LENGTH_LONG)
                .show();
    }
}
