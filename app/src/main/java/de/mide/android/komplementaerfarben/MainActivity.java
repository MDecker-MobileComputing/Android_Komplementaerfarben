package de.mide.android.komplementaerfarben;

import static android.widget.Toast.LENGTH_LONG;
import static android.widget.Toast.makeText;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;


/**
 * Einzige Activity in der App.
 */
public class MainActivity extends AppCompatActivity {

    public static final String TAG4LOGGING = "KomplementFarben";

    /** ViewModel-Instance für die Verwaltung der von dieser Activity dargestellten Farben. */
    private ZweiFarbenViewModel _zweiFarbenViewModel = null;

    /** Farbfläche für Darstellung Farbe 1. */
    private View _farbe1View = null;

    /** Farbfläche für Darstellung Farbe 2. */
    private View _farbe2View = null;

    /** Element für Anzeige Farbcode 1. */
    private TextView _farbe1TextView = null;

    /** Element für Anzeige Farbcode 2. */
    private TextView _farbe2TextView = null;


    /**
     * Lifecycle-Methode, lädt Layout-Datei.
     */
    @Override
    protected void onCreate( Bundle savedInstanceState ) {

        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        _farbe1View = findViewById( R.id.view_farbe1 );
        _farbe2View = findViewById( R.id.view_farbe2 );

        _farbe1TextView = findViewById( R.id.textview_farbe1 );
        _farbe2TextView = findViewById( R.id.textview_farbe2 );

        _zweiFarbenViewModel =
                new ViewModelProvider( this ).get ( ZweiFarbenViewModel.class );

        if ( _zweiFarbenViewModel.farbenNochNichtErzeugt() ) {

            Log.i( TAG4LOGGING, "Noch keine Farben erzeugt." );
            neueFarbenErzeugenUndDarstellen();

        } else {

            farbenDarstellen();
            Log.i( TAG4LOGGING, "Alte Farben wiederhergestellt." );
        }

        actionBarKonfigurieren();
    }


    /**
     * Konfiguriert die ActionBar: Titel, Untertitel und Icon setzen.
     * Die Menü-Einträge werden in der Methode {@link #onCreateOptionsMenu(Menu)} hinzugefügt.
     */
    private void actionBarKonfigurieren() {

        ActionBar actionBar = getSupportActionBar();
        if ( actionBar == null ) {

            makeText( this, R.string.toast_actionbar_nicht_gefunden, LENGTH_LONG ).show();
            return;
        }

        actionBar.setTitle( R.string.app_name );

        actionBar.setDisplayShowHomeEnabled( true );
        actionBar.setIcon( R.mipmap.ic_launcher );
    }


    /**
     * Hinzufügen von Menu-Einträgen zur ActionBar.
     * <br><br>
     *
     * Das Event-Handling für die Einträge wird in der Methode
     * {@link #onOptionsItemSelected(MenuItem)} implementiert.
     *
     * @param menu  Menü-Objekt, zu dem die Einträge hinzugefügt werden sollen.
     */
    @Override
    public boolean onCreateOptionsMenu( Menu menu ) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate( R.menu.actionbar_eintraege, menu );

        return super.onCreateOptionsMenu( menu );
    }


    /**
     * Event-Handler für Menu-Items in der ActionBar.
     *
     * @param item  Menu-Item, welches gerade ein Event ausgelöst hat.
     *
     * @return Es wird {@code true} zurückgegeben, wenn wir in dieser
     *          Methode das Ereignis verarbeiten konnten, ansonsten
     *          der Wert der Super-Methode.
     */
    @Override
    public boolean onOptionsItemSelected( MenuItem item ) {

        final int selectedMenuId = item.getItemId();

        if ( selectedMenuId == R.id.actionbar_neuefarbe ) {

            neueFarbenErzeugenUndDarstellen();
            return true;

        } else if ( selectedMenuId == R.id.actionbar_zwischenablage ) {

            farbenInZwischenablageKopieren();
            return true;

        } else {

            return super.onOptionsItemSelected( item );
        }
    }


    /**
     * Aktuelle Farb-Codes in Zwischenablage kopieren
     */
    private void farbenInZwischenablageKopieren() {

        ClipboardManager clipboard = (ClipboardManager)
                getSystemService( CLIPBOARD_SERVICE );

        if ( clipboard == null ) {

            makeText( this, R.string.toast_clipboard_nicht_gefunden, LENGTH_LONG ).show();
            return;
        }


        ClipData clip =
                ClipData.newPlainText(
                        getString( R.string.clipboard_titel ),
                        _zweiFarbenViewModel.toString()
                );

        clipboard.setPrimaryClip( clip );
    }


    /**
     * Methode zum Erzeugen von zwei neuen Farben; die neuen Farben werden danach
     * auch dargestellt.
     */
    private void neueFarbenErzeugenUndDarstellen() {

        _zweiFarbenViewModel.neueFarbenErzeugen();

        Log.i( TAG4LOGGING, "Neue Farben erzeugt: " + _zweiFarbenViewModel.toString() );

        farbenDarstellen();
    }


    /**
     * Farben aus ViewModell dargestellen (Farbflächen und Anzeige Hex-Codes).
     */
    private void farbenDarstellen() {

        int farbe1 = _zweiFarbenViewModel.getFarbe1();
        int farbe2 = _zweiFarbenViewModel.getFarbe2();

        _farbe1View.setBackgroundColor( farbe1 );
        _farbe2View.setBackgroundColor( farbe2 );


        String hexStr1 = _zweiFarbenViewModel.getFarbe1Hex();
        String hexStr2 = _zweiFarbenViewModel.getFarbe2Hex();

        _farbe1TextView.setText( hexStr1 );
        _farbe2TextView.setText( hexStr2 );
    }

}