package es.netrunners;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.os.Bundle;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Point;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends FragmentActivity {

	private GoogleMap mMap = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setUpMapIfNeeded();
	}

	@Override
	protected void onResume() {
		super.onResume();
		setUpMapIfNeeded();
	}

	private void setUpMapIfNeeded() {
		if (mMap == null) {
			mMap = ((SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.map)).getMap();
			if (mMap != null) {
				setUpMap();
			}
		}
	}

	private void setUpMap() {
		mMap.setMyLocationEnabled(true);

		mMap.setOnMapClickListener(new OnMapClickListener() {
			@Override
			public void onMapClick(LatLng point) {
				Projection proj = mMap.getProjection();
				Point coord = proj.toScreenLocation(point);
				Toast.makeText(
						MainActivity.this,
						"Lat: " + point.latitude + "\n" + "Lng: "
								+ point.longitude + "\n" + "X: " + coord.x
								+ " - Y: " + coord.y, Toast.LENGTH_SHORT)
						.show();
			}
		});

		mMap.setOnMarkerClickListener(new OnMarkerClickListener() {
			@Override
			public boolean onMarkerClick(Marker marker) {
				marker.showInfoWindow();
				return true;
			}
		});
		
		mMap.setOnMapLongClickListener(new OnMapLongClickListener() {

			@Override
			public void onMapLongClick(LatLng point) {
				añadirMarcador(point);
			}
		});


	}

	private void añadirMarcador(final LatLng point) {

		// Diálogo para introducir datos
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		final View v = getLayoutInflater().inflate(R.layout.dialog, null);

		builder.setView(v);
		builder.setTitle("Nuevo Punto")
				.setCancelable(false)
				.setPositiveButton("Añadir",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {

								EditText ettitulo = (EditText) v
										.findViewById(R.id.editText_titulo);
								EditText etdesc = (EditText) v
										.findViewById(R.id.editText_descripcion);

								String titulo = ettitulo.getText().toString();
								String descripcion = etdesc.getText()
										.toString();

								// Añadir marcador
								mMap.addMarker(new MarkerOptions()
										.position(point).title(titulo)
										.snippet(descripcion));
							}
						})
				.setNegativeButton("Cancelar",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.cancel();
							}
						});

		AlertDialog alert = builder.create();
		alert.show();

	}

}
