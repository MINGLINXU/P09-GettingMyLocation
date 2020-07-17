package sg.edu.rp.webservices.p09_gettingmylocation;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.io.File;
import java.io.FileWriter;


public class MyService extends Service {

    boolean started;

    FusedLocationProviderClient client;
    LocationRequest mLocationRequest;
    LocationCallback mLocationCallback;

    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        Log.d("Service", "Service created");
        super.onCreate();
        client = LocationServices.getFusedLocationProviderClient(this);
        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setSmallestDisplacement(100);

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult != null) {
                    Location data = locationResult.getLastLocation();
                    double lat= data.getLatitude();
                    double lng= data.getLongitude();
                    String msg = "Lat: " + lat + ", Lng: " + lng;
                    try {
                        String folderLocation = Environment.getExternalStorageDirectory().getAbsolutePath() + "/P09";
                        File targetFile = new File(folderLocation, "data.txt");
                        FileWriter writer = new FileWriter(targetFile, true);
                        writer.write(msg+"\n");
                        writer.flush();
                        writer.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        };

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(started == false){
            started = true;
            Toast.makeText(this, "Service is running", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(this, "Service is still running", Toast.LENGTH_SHORT).show();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Service is stop", Toast.LENGTH_SHORT).show();
    }

    private boolean checkPermission() {
        int permissionCheck_Coarse = ContextCompat.checkSelfPermission(
                MyService.this, Manifest.permission.ACCESS_COARSE_LOCATION);
        int permissionCheck_Fine = ContextCompat.checkSelfPermission(
                MyService.this, Manifest.permission.ACCESS_FINE_LOCATION);

        if (permissionCheck_Coarse == PermissionChecker.PERMISSION_GRANTED
                || permissionCheck_Fine == PermissionChecker.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

}
