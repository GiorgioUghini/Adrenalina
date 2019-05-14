package models;

import errors.InvalidAmmoException;

public class AmmoCard extends Card {
    private int red;
    private int blue;
    private int yellow;
    private boolean drawPowerup;

    /** Creates the ammo card
     * @param red number of red ammos to draw, must be between 0 and 3
     * @param yellow number of red ammos to draw, must be between 0 and 3
     * @param blue number of red ammos to draw, must be between 0 and 3
     * @param drawPowerup if true you can draw a powerup from the deck */
    public AmmoCard(int red, int yellow, int blue, boolean drawPowerup){
        if(!(validateAmmo(red) && validateAmmo(yellow) && validateAmmo(blue))) throw new InvalidAmmoException();
        this.red = red;
        this.blue = blue;
        this.yellow = yellow;
        this.drawPowerup = drawPowerup;
    }

    public int getRed(){return red;}
    public int getYellow(){return yellow;}
    public int getBlue(){return blue;}
    public boolean hasPowerup(){return drawPowerup;}

    private boolean validateAmmo(int n){
        return (n>=0 && n<=3);
    }
}