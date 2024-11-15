import javax.swing.JComponent;
public class Asteroid
{
    private int asteroidX;
    private int asteroidY;
    private boolean isDestroyed;
    private JComponent component;
    public Asteroid(JComponent c)
    {
        component=c;

        asteroidX=(int)(Math.random()*400);
        asteroidY=0;
        isDestroyed=false;
    }
    public int getAsteroidX()
    {
        return asteroidX;
    }
    public int getAsteroidY()
    {
        return asteroidY;
    }
    public boolean isDestroyed()
    {
        return isDestroyed;
    }
    public void setDestroyed(boolean d)
    {
        isDestroyed=d;
    }
    public void updateAsteroid()
    {
        if(!isDestroyed)

        {
            asteroidY+=2;
            if(asteroidY>component.getHeight())
            {
                asteroidY = -10;
                asteroidX=(int)(Math.random()*component.getWidth()+1);
            }
        }
    }
}
