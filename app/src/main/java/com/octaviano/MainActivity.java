package com.octaviano;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.octaviano.analizarColor.AnalizarColor;
import com.octaviano.compartirIMG.CompartirFotografia;
import com.octaviano.fotografia.Fotografia;
import com.octaviano.procesarIMG.ProcesarFotografia;

import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Fotografia fotografia;
    private ImageView image;
    private Bitmap imageBitmap;


    private static boolean initOpenCV = false;

    static { initOpenCV = OpenCVLoader.initDebug(); }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab =  findViewById(R.id.fab);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        fotografia = new Fotografia(MainActivity.this);
        image = findViewById(R.id.image);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            fotografia.loadFromCamera();
        } else if (id == R.id.nav_gallery) {
            fotografia.loadFromGallery();
        }else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {
            new CompartirFotografia(MainActivity.this).execute(fotografia.getFotografia());
        } else if (id == R.id.nav_save) {
            if (imageBitmap != null) {
                if (fotografia.save(imageBitmap))
                    Toast.makeText(MainActivity.this,
                            R.string.imagnen_guardada,
                            Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(MainActivity.this,
                            R.string.imagnen_no_guardada,
                            Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this,
                        R.string.imagen_null,
                        Toast.LENGTH_SHORT).show();
            }

        } else if(id == R.id.nav_ayuda){
            startActivity(new Intent(MainActivity.this,InformacionRb.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if ( requestCode == Fotografia.REQUEST_CODE_GALLERY){
                if (fotografia.getBitmat(data)) {
                    imageBitmap = fotografia.getFotografia();
                    ProcesarFotografia pF = new ProcesarFotografia();
                    Mat mat = pF.toMat(fotografia.getFotografia());
                    //mat = pF.getGrayScale(mat);
                    if (mat != null){
                        //pF.toBitmap(mat,fotografia.getFotografia())
                        AnalizarColor aColor = new AnalizarColor();
                        Mat res = aColor.analizarColor(mat,AnalizarColor.LEUKOCORIA_LEVEL_LOW);
                        image.setImageBitmap(pF.toBitmap(res,fotografia.getFotografia()));
                    }

                } else {
                    Toast.makeText(MainActivity.this,
                            R.string.imagen_no_cargada,
                            Toast.LENGTH_SHORT).show();
                }

            } else if (requestCode == Fotografia.REQUEST_CODE_CAMERA){
                Bundle extras = data.getExtras();
                imageBitmap = (Bitmap) extras.get("data");
                image.setImageBitmap(imageBitmap);
            }
        }
    }


}
