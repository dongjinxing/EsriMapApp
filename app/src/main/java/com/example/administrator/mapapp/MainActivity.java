package com.example.administrator.mapapp;

import android.content.Context;
import android.location.LocationManager;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.content.Intent;
import android.widget.Toast;


import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISLocalTiledLayer;
import com.esri.android.map.event.OnSingleTapListener;
import com.esri.core.symbol.PictureMarkerSymbol;


import java.io.File;


public class MainActivity extends ActionBarActivity {

    private static final int FILE_SELECT_CODE = 0;
    private EditText et;
    private MapView mMapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // create the path to local tpk
        File demoDataFile = Environment.getExternalStorageDirectory();
        String offlineDataSDCardDirName = this.getResources().getString(
                R.string.offline_dir);
        String filename = this.getResources().getString(R.string.local_tpk);

        // create the url
        String basemap = demoDataFile + File.separator
                + offlineDataSDCardDirName + File.separator + filename;
        String basemapurl = "file://" + basemap;

        // create the mapview
        mMapView = (MapView) findViewById(R.id.map);
        // create the local tpk
        ArcGISLocalTiledLayer localTiledLayer = new ArcGISLocalTiledLayer(basemapurl);
        // add the layer
        mMapView.addLayer(localTiledLayer);
        // enable panning over date line
        mMapView.enableWrapAround(true);
        // set Esri logo
        mMapView.setEsriLogoVisible(true);

        mMapView.setOnSingleTapListener(m_OnSingleTapListener);

        EditText et = (EditText) findViewById(R.id.editText1);
        GraphicsLayer gLayerGps = new GraphicsLayer();
        mMapView.addLayer(gLayerGps);

        Button btnChooseFile = (Button)findViewById(R.id.btnChooseFile);
        btnChooseFile.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v)
            {
                showFileChooser();
                //showPointList();
            }
        });


        PictureMarkerSymbol locationSymbol = new PictureMarkerSymbol(this.getResources()
                .getDrawable(R.drawable.aa));

        LocationManager locMag;

        locMag = (LocationManager) this
                .getSystemService(Context.LOCATION_SERVICE);


    }

    OnSingleTapListener m_OnSingleTapListener = new OnSingleTapListener() {

        public void onSingleTap(float x, float y) {
            et.setText("X:" + String.format("%f", x) + ",Y:" + String.format("%f", y));
            com.esri.core.geometry.Point pt = mMapView.toMapPoint(x, y);
            et.setText("X:" + String.format("%f", pt.getX()) + ",Y:" + String.format("%f", pt.getY()));
        }
    };

    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Select a File to Upload"),
                    FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "Please install a File Manager.",
                    Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
