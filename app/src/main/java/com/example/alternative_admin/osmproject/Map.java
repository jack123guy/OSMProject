package com.example.alternative_admin.osmproject;

/**
 * Created by alternative-admin on 2017/6/12.
 */

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.osmdroid.DefaultResourceProxyImpl;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedOverlay;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.ScaleBarOverlay;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.io.IOException;
import java.util.ArrayList;

public class Map extends Activity {
    private MapView mapview;
    private MapController mcontrol;
    private ScaleBarOverlay scale;
    public LocationManager locationManager = null;
    private ItemizedOverlay<OverlayItem> iconOverlay;

    private Button back;
    private Button Info;

    private final Point mpoint = new Point();

    protected final Paint mpaint = new Paint();

    double lat = 22.998519;
    double lng = 120.220106;
    DefaultResourceProxyImpl defaultResourceProxyImpl;
    String url = "http://www.cwb.gov.tw/V7/forecast/taiwan/Tainan_City.htm";
    String info;
    Location location;
    GeoPoint myLocation;
    //private MyLocationNewOverlay mMyLocationOverlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialmap();

        //Get Location
        location = getMyLocation();
        if(location == null){
            //Testing Locaition on the emulator
            myLocation = new GeoPoint(lat,lng);
            MyLocationNewOverlay myLocationOverlay = new MyLocationNewOverlay(this, mapview);
            mcontrol.setCenter(myLocation);
            mapview.getOverlays().add(myLocationOverlay);
            Toast.makeText(getApplicationContext(), "Test location in emulator", Toast.LENGTH_SHORT).show();
        }
        else{
            myLocation = new GeoPoint(location);
            MyLocationNewOverlay myLocationOverlay = new MyLocationNewOverlay(this, mapview);
            mcontrol.setCenter(myLocation);
            mapview.getOverlays().add(myLocationOverlay);
        }

        AddMarker();
        draw(null, mapview, true);
        //Add Scale Bar
        scale = new ScaleBarOverlay(this);
        mapview.getOverlays().add(scale);

        //Add Button
        back = (Button) findViewById(R.id.back);
        Info = (Button) findViewById(R.id.Info);
        back.setOnClickListener(button_set);
        Info.setOnClickListener(button_set);


    }
    public void draw(final Canvas c, MapView map, final boolean shadow){
        if(shadow) return;
        final MapView.Projection pj = map.getProjection();
        pj.toMapPixels(myLocation, mpoint);
        c.drawText("Tainan Weather",mpoint.x,mpoint.y,this.mpaint);
    }



    //Set MapView
    public void initialmap(){
        mapview = (MapView) this.findViewById(R.id.mapview);
        mapview.setTileSource(TileSourceFactory.MAPQUESTOSM);
        mapview.setBuiltInZoomControls(true);
        mapview.setMultiTouchControls(true);
        mapview.setClickable(true);
        mcontrol = (MapController) this.mapview.getController();
        mcontrol.setZoom(15);
    }

    private View.OnClickListener button_set = new View.OnClickListener(){
        public void onClick(View v){
            //TODO Auto-generated method stub
            switch(v.getId()){
                case R.id.back:
                    mcontrol.setCenter(myLocation);
                    break;
                case R.id.Info:
                    new Thread(runnable).start();
                    Toast.makeText(getApplicationContext(), "Work", Toast.LENGTH_SHORT).show();
                    break;


            }
        }
    };
	/*
	public void Addtext(){
		//Create Overlay for marker
				OverlayItem myOverlayItem =
						new OverlayItem("Here","Current Position", myLocation);
				String marker =
						"lan";
				myOverlayItem.setMarker(marker);

				ArrayList<OverlayItem> overlayArray =
						new ArrayList<OverlayItem>();
				overlayArray.add(myOverlayItem);

				defaultResourceProxyImpl = new DefaultResourceProxyImpl(this);
				this.iconOverlay = new ItemizedIconOverlay<OverlayItem>(overlayArray, marker,null, defaultResourceProxyImpl);
				mapview.getOverlays().add(iconOverlay);

	}
	 */


    public void AddMarker(){
        //Create Overlay for marker
        OverlayItem myOverlayItem =
                new OverlayItem("Here","Current Position", myLocation);
        Drawable marker =
                ResourcesCompat.getDrawable(getResources(),R.drawable.markpin,null);
        myOverlayItem.setMarker(marker);

        ArrayList<OverlayItem> overlayArray =
                new ArrayList<OverlayItem>();
        overlayArray.add(myOverlayItem);

        defaultResourceProxyImpl = new DefaultResourceProxyImpl(this);
        this.iconOverlay = new ItemizedIconOverlay<OverlayItem>(overlayArray, marker,null, defaultResourceProxyImpl);
        mapview.getOverlays().add(iconOverlay);

    }

    Runnable runnable = new Runnable(){
        @Override
        public void run(){
            try{
                Document doc = Jsoup.connect(url).get();
                Elements weather = doc.select("td");
                info = weather.get(0).text();
            }catch(IOException e){
                //TODO Auto-generated catch block
                e.printStackTrace();
            }
            //handler.sendEmptyMessage(0);
        }
    };
	/*
	@SuppressLint("HandlerLeak")
	Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg){
			super.handleMessage(msg);
			Info.setText(info);
		}
	};
	*/

    Location getMyLocation(){
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if ( ContextCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED ){
            ActivityCompat.requestPermissions( this, new String[] {  android.Manifest.permission.ACCESS_COARSE_LOCATION  },
                    MY_PERMISSION_ACCESS_COARSE_LOCATION);
        }
        return locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

    }
}

