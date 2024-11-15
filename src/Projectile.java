public class Projectile
{
    private int xPos;
    private int yPos;
    public static int SPEED=8;//(int)(Math.random()*5)+1;
    public Projectile(int x, int y)
    {
        xPos=x;
        yPos=y;
    }
    public int getX()
    {
        return xPos;
    }
    public int getY()
    {
        return yPos;
    }
    public void updateProjectilePosition()
    {
        yPos-=SPEED;
    }
}
