package de.mide.android.komplementaerfarben;

import static de.mide.android.komplementaerfarben.MainActivity.TAG4LOGGING;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import java.util.Random;


/**
 * Eine Instanz dieser ViewModel-Klasse ist für die MainActivity erzeugt und speichert die erzeugten
 * Farben auch über eine Display-Rotation hinaus (weil dabei die Activity neu erzeugt wird).
 */
public class ZweiFarbenViewModel extends ViewModel {

    /** RGB-Farbcode für die obere Farbe. */
    private int _farbe1 = -1;

    /** RGB-Farbcode für die untere Farbe; komplementär zu der oberen Farbe. */
    private int _farbe2 = -1;

    /** Zufallsgenerator für die Erzeugung der Zahlenwerte für die einzelnen Farbkomponenten. */
    private final Random _zufallsGenerator = new Random();


    /**
     * Konstruktor.
     */
    public ZweiFarbenViewModel() {

        Log.i( TAG4LOGGING, "ViewModel erzeugt" );
    }


    /**
     * Methode zur Abfrage, ob schon Zufallsfarben erzeugt wurden.
     *
     * @return {@code true} gdw. noch keine Zufallsfarben erzeugt wurden
     */
    public boolean farbenNochNichtErzeugt() {

        return _farbe1 == -1;
    }


    /**
     * Methode um einen RGB-Farbcode als int-Wert zu erzeugen.
     *
     * @param rot Rot-Anteil von 0..255
     *
     * @param gruen Grün-Anteil von 0..255
     *
     * @param blau Blau-Anteil von 0..255
     *
     * @return RGB-Farbcode als ein int-Wert, mit voller Deckkraft (Alpha-Wert=255)
     */
    private int rotGruenBlauZuRGB( int rot, int gruen, int blau ) {

        final int alphaWert = 0xff; // volle Deckkraft

        return ( alphaWert << 24 ) |
               ( rot       << 16 ) |
               ( gruen     << 8  ) |
                 blau;
    }


    /**
     * Methode erzeugt zwei neue Farben, die komplementär zueinander sind.
     */
    public void neueFarbenErzeugen() {

        int rot1   = _zufallsGenerator.nextInt( 256 ); // 0..255
        int gruen1 = _zufallsGenerator.nextInt( 256 );
        int blau1  = _zufallsGenerator.nextInt( 256 );

        _farbe1 = rotGruenBlauZuRGB( rot1, gruen1, blau1 );


        int rot2   = 255 - rot1;
        int gruen2 = 255 - gruen1;
        int blau2  = 255 - blau1;

        _farbe2 = rotGruenBlauZuRGB( rot2, gruen2, blau2 );
    }


    /**
     * Getter für die obere Farbe.
     *
     * @return RGB-Farbcode für die obere Farbe
     */
    public int getFarbe1() { return _farbe1; }


    /**
     * Getter für die untere Farbe.
     *
     * @return RGB-Farbcode für die untere Farbe
     */
    public int getFarbe2() { return _farbe2; }


    /**
     * Hex-Code von Farbe 1.
     *
     * @return RGB-Hex-Code, z.B. "#F59864"
     */
    public String getFarbe1Hex() { return rgb2hex( _farbe1 ); }


    /**
     * Hex-Code von Farbe 2.
     *
     * @return RGB-Hex-Code, z.B. "69F45F"
     */
    public String getFarbe2Hex() { return rgb2hex( _farbe2 ); }


    /**
     * Hilfsmethode, um RGB-Farbcode als int-Wert in Hex-String umzuwandeln.
     *
     * @param rgbCode RGB-Farbcode, der in Hex-String umgewandelt werden soll
     *
     * @return Farbe als RGB-Hex-Code, z.B. "#69F45F"
     */
    private static String rgb2hex ( int rgbCode ) {

        return String.format( "#%06X", ( 0xFFFFFF & rgbCode ) );
    }


    /**
     * String mit beiden Farb-Codes.
     *
     * @return Hex-Codes der beiden Strings, z.B. "#F59864 / #0A679B"
     */
    @Override
    public String toString() {

        if ( farbenNochNichtErzeugt() ) {

            return "Farben noch nicht erzeugt";

        } else {

            return getFarbe1Hex() + " / " + getFarbe2Hex();
        }
    }

}
