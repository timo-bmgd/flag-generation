import ij.ImageJ;
import ij.ImagePlus;
import ij.gui.GenericDialog;
import ij.gui.NewImage;
import ij.plugin.PlugIn;
import ij.process.ImageProcessor;

public class FlagGenerator implements PlugIn {

    final static String[] choices = {
            "Deutsche Flagge",
            "Niederländische Fahne",
            "Eindimensionaler Verlauf",
            "Zweidimensionaler Verlauf",
            "Tschechien Fahne",
            "Japanische Fahne"
    };

    private String choice;

    public static void main(String args[]) {
        ImageJ ij = new ImageJ(); // neue ImageJ Instanz starten und anzeigen
        ij.exitWhenQuitting(true);
        FlagGenerator imageGeneration = new FlagGenerator();
        imageGeneration.run("");
    }

    public void run(String arg) {

        int width  = 566;  // Breite
        int height = 400;  // Hoehe

        // RGB-Bild erzeugen
        ImagePlus imagePlus = NewImage.createRGBImage("GLDM_U1", width, height, 1, NewImage.FILL_BLACK);
        ImageProcessor ip = imagePlus.getProcessor();

        // Array to access image pixels
        int[] pixels = (int[])ip.getPixels();

        dialog();

        if ( choice.equals(choices[0]) ) {
            generateGermanFlag(width, height, pixels);
        }
        if ( choice.equals(choices[1]) ) {
            generateNetherlandsFlag(width, height, pixels);
        }
        if ( choice.equals(choices[2]) ) {
            generateGradient(width, height, pixels);
        }
        if ( choice.equals(choices[3]) ) {
            generateComplicated(width, height, pixels);
        }
        if ( choice.equals(choices[4]) ) {
            generateCzech(width, height, pixels);
        }
        if ( choice.equals(choices[5]) ) {
            generateJapan(width, height, pixels);
        }

        imagePlus.show();
        imagePlus.updateAndDraw();
    }

    private void generateGermanFlag(int width, int height, int[] pixels) {
        for (int y=0; y<height; y++) {
            for (int x=0; x<width; x++) {
                int pos = y*width + x;

                int r = 0;
                int g = 0;
                int b = 0;
                if(y>=(height/3)) {
                    r = 255;
                    g = 0;
                    b = 0;
                }
                if(y>=((height/3)*2)){
                    r = 255;
                    g = 255;
                    b = 0;
                }

                pixels[pos] = 0xFF000000 | (r << 16) | (g << 8) |  b;
            }
        }
    }

    private void generateNetherlandsFlag(int width, int height, int[] pixels) {
        for (int y=0; y<height; y++) {
            for (int x=0; x<width; x++) {
                int pos = y*width + x;

                //ROT
                int r = 255;
                int g = 0;
                int b = 0;
                //WEISS
                if(y>=(height/3)) {
                    r = 255;
                    g = 255;
                    b = 255;
                }
                //BLAU
                if(y>=((height/3)*2)){
                    r = 0;
                    g = 0;
                    b = 255;
                }

                pixels[pos] = 0xFF000000 | (r << 16) | (g << 8) |  b;
            }
        }
    }

    private void generateGradient(int width, int height, int[] pixels) {
        for (int y=0; y<height; y++) {
            for (int x=0; x<width; x++) {
                int pos = y*width + x;

                int r = 255;
                int g = 255;
                int b = 0;

                //Die Steigung m festlegen
                double m = 255/(double) width;
                // Dann als double die Farbstärke des Pixels berechnen
                double gradient = m * (double) x;
                //Gradient gibt nun an, wie stark eine Farbe von links nach rechts geht.

                r -= (int) gradient;
                b = (int) gradient;

                pixels[pos] = 0x80000000 | (r << 16) | (g << 8) |  b;
            }
        }
    }

    private void generateComplicated(int width, int height, int[] pixels) {
        for (int y=0; y<height; y++) {
            for (int x=0; x<width; x++) {
                int pos = y*width + x;

                int r = 0;
                int g = 0;
                int b = 0;

                //Die Steigung m festlegen
                double gradientX = (255/(double) width) * (double) x;
                double gradientY = (255/(double) height) * (double) y;

                //horizontalen Schwarz/Rot Verlaufs bei gleichzeitigem
                // vertikalen Schwarz/Blau Verlauf

                r = (int) gradientX;
                b = (int) gradientY;


                // Werte zurueckschreiben
                pixels[pos] = 0x80000000 | (r << 16) | (g << 8) |  b;
            }
        }
    }

    private void generateCzech(int width, int height, int[] pixels) {
        for (int y=0; y<height; y++) {
            for (int x=0; x<width; x++) {
                int pos = y*width + x;

                int r = 255;
                int g = 255;
                int b = 255;

                int mid = height/2;

                if(y>=mid){
                    r = 255;
                    g = 0;
                    b = 0;
                }
                if(y<mid && x<=y ){
                    r = 10;
                    g = 10;
                    b = 200;
                }
                if(y>=mid && x<=height-y ){
                    r = 10;
                    g = 10;
                    b = 200;
                }

                pixels[pos] = 0xFF000000 | (r << 16) | (g << 8) |  b;
            }
        }
    }

    private boolean inRange(int px,int py, int cx, int cy, int radius){
        double a = Math.abs(px - cx);
        double b = Math.abs(py - cy);
        double c = Math.abs(Math.sqrt(Math.pow(a, 2) + Math.pow(b, 2)));
        if(c <= (double)radius){
            return true;
        }else{
            return false;
        }

    }
    private void generateJapan(int width, int height, int[] pixels) {
        for (int y=0; y<height; y++) {
            for (int x=0; x<width; x++) {
                int pos = y*width + x;

                int r = 255;
                int g = 255;
                int b = 255;
                int radius = 75;

                if(inRange(x,y,width/2,height/2, radius)){
                    g = 0;
                    b = 0;
                }

                pixels[pos] = 0xFF000000 | (r << 16) | (g << 8) |  b;
            }
        }
    }
    private void dialog() {
        // Dialog fuer Auswahl der Bilderzeugung
        GenericDialog gd = new GenericDialog("Flagge");

        gd.addChoice("Bildtyp", choices, choices[0]);

        gd.showDialog();	// generiere Eingabefenster

        choice = gd.getNextChoice(); // Auswahl uebernehmen

        if (gd.wasCanceled())
            System.exit(0);
    }
}