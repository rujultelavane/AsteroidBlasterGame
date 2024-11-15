import javax.swing.*;
import java.awt.Rectangle;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.awt.Color;
import javax.swing.JComponent;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;

public class Game extends JComponent
{//implements ActionListener{
    //declare instance variables here, review UML diagram
    private int shipX;
    private int shipY;
    private int time=1000; //1000 = abt a min
    private int lives=3;
    private int asteroidsHit;
    private boolean gameOver=false;
    ArrayList<Projectile> projectiles;
    ArrayList<Asteroid> asteroids;
    ArrayList<Rectangle> enemyRectangles;
    Rectangle playerRectangle;
    Timer timer;
    JFrame frame;
    JComponent component;
    //private Image image = new Image("nyan_cat.png").getImage();
    private Image image;
    boolean allDestroyed=false;
    private int color=-1;


    public Game(JFrame frame){
        this.frame = frame;
        this.component=component;
        asteroids = new ArrayList<>();
        projectiles = new ArrayList<>();
        enemyRectangles = new ArrayList<>();

        shipX = frame.getWidth()/2;
        shipY = frame.getHeight()-100;
        playerRectangle = new Rectangle(shipX, shipY, 59, 30);
        setFocusable(true);
        addKeyListener(new KeyAdapter(){
            @Override
            public void keyPressed(KeyEvent event){
                handleKeyPress(event);
            }
        });
        timer = new Timer(60, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                if(!gameOver)
                {
                    updateScreen();
                    frame.repaint();
                }
            }
        });
        timer.start();


    }
    public void updateEnemyRectangles()
    {
        for(int x=0; x<asteroids.size(); x++)
        {
            enemyRectangles.get(x).setLocation(asteroids.get(x).getAsteroidX(), asteroids.get(x).getAsteroidY());
        }
    }
    private void handleKeyPress(KeyEvent event)
    {
        int keycode = event.getKeyCode();
        if (shipX + 15 <= frame.getWidth() && keycode == event.VK_RIGHT)
            shipX += 15;
        if (shipX - 15 >= 0 && keycode == event.VK_LEFT)
            shipX -= 15;
        if (shipY + 15 <= frame.getHeight() && keycode == event.VK_UP)
            shipY -= 15;
        if (shipY - 15 >= 0 && keycode == event.VK_DOWN)
            shipY += 15;
        if (keycode == event.VK_SPACE) {
            shoot();
            Toolkit.getDefaultToolkit().beep(); //BEEPs when u shoot a projectile
            // but i am not really a fan of this because i don't like the mac beep its kinda icky
            //i wanted to use \007 but intellij just prints out "BEL" instead of making the noise
            //so im stuck with this unless i actually wanna use sound libraries and write a whole bunch of extra code
            //which i don't
        }
        playerRectangle.setLocation(shipX, shipY);
    }
    public void shoot()
    {
        Projectile proj=new Projectile(shipX+20, shipY);
        projectiles.add(proj);

    }
    private void checkForAsteroidCollisions()
    {
        for(int x=0; x<enemyRectangles.size(); x++)
        {
            Rectangle rect = enemyRectangles.get(x);
            if(rect.intersects(playerRectangle))
            {
                enemyRectangles.remove(x);
                asteroids.remove(x);
                lives--;
            }
        }
    }
    private void generateNewAsteroid()
    {
        int rand=(int)(Math.random()*350);
        if(rand<=5)
        {
            Asteroid newAst=new Asteroid(this);
            asteroids.add(newAst);

            Rectangle newEne=new Rectangle(newAst.getAsteroidX(), newAst.getAsteroidY(), 30, 30);
            enemyRectangles.add(newEne);
        }
    }
    private void removeAsteroid(int index)
    {
        asteroids.get(index).setDestroyed(true);
        asteroids.remove(index);
        enemyRectangles.remove(index);
    }
    private void updateAsteroidLocation()
    {
        if(asteroids.size()>0)
        {
            for(int x=0; x<asteroids.size(); x++)
            {
                asteroids.get(x).updateAsteroid();
            }
        }
    }
    private void checkProjectileCollisions()
    {
        for(int x=0; x<asteroids.size(); x++)
        {
            for(int y=0; y<projectiles.size(); y++)
            {
                Projectile temp= projectiles.get(y);
                Asteroid ast=asteroids.get(x);
                temp.updateProjectilePosition();
                if(temp.getX()>frame.getWidth() || temp.getY()>frame.getHeight())
                {
                    projectiles.remove(y);
                    y--;
                }
                else if (enemyRectangles.get(x).intersects(new Rectangle (temp.getX(), temp.getY(), 30, 30)))
                {
                    projectiles.remove(y);
                    y--;
                    removeAsteroid(x);
                    x--;
                    asteroidsHit++;
                    frame.repaint();
                }
            }
        }
    }
    public void updateProjectiles()
    {
        if (projectiles.size() > 0)
        {
            for(int x=0; x<projectiles.size(); x++)
                projectiles.get(x).updateProjectilePosition();
        }

    }
    private void updateScreen()
    {
        checkForAsteroidCollisions();
        updateAsteroidLocation();
        generateNewAsteroid();
        checkProjectileCollisions();
        updateProjectiles();
    }
    private void drawShip(Graphics graphics)
    {
        /*int[] xVals= {shipX, shipX, shipX+50, shipX+50};
        int[] yVals= {shipY, shipY+50, shipY+50, shipY};//{shipY, shipY+2, shipY+6, shipY+8, shipY+6, shipY+2, shipY+0, shipY+0, shipY-1, shipY-2, shipY-3, shipY-2, shipY-1, shipY-1, shipX+0};
        switch(lives)
        {
            case 3:
                graphics.setColor(Color.GREEN);
                break;
            case 2:                                                 //original square ship
                graphics.setColor(Color.YELLOW);
                break;
            default:
                graphics.setColor(Color.RED);
        }
        graphics.fillPolygon(xVals, yVals, 4);*/

        //idk if u need to change the path names of these cuz theyre specific to my computer but im attaching the images i used
        //resolution of these kinda suck so that it fits a good ship size
        String imagePath = "/Users/rujultelavane/Downloads/nyan_cat.png";//makes rocket into nyan cat HEHEHEH
        if (lives==1)
            imagePath = "/Users/rujultelavane/Downloads/dying_nyan.png"; //when have one life left, evil nyan
        if(lives==2)
            imagePath = "/Users/rujultelavane/Downloads/yellow_nyan.png"; //yellow
        ImageIcon icon=new ImageIcon(imagePath);
        image=icon.getImage();
        ImagePanel imagePanel = new ImagePanel(imagePath);
        add(imagePanel);

    }
    private void drawAsteroids(Graphics graphics)
    {
        for(int x=0; x<asteroids.size(); x++)
        {
            if(!asteroids.get(x).isDestroyed())
            {
                graphics.setColor(Color.DARK_GRAY);
                graphics.fillOval(asteroids.get(x).getAsteroidX(), asteroids.get(x).getAsteroidY(), 30, 30); //graphics.fillOval((int)(Math.random()*frame.getWidth()), 0, 30, 30);
                updateEnemyRectangles();
            }
        }
    }
    private void drawProjectiles(Graphics graphics)
    {

        for(int x=0; x<projectiles.size(); x++)
        {
            graphics.setColor(projectileColor());
            graphics.fillRect(projectiles.get(x).getX(), projectiles.get(x).getY(), 6, 6);
        }
    }
    private Color projectileColor()
    {
        color++;
        if(color==6)
            color=0;
        if(color==0)
            return new Color(254,6,5); //red
        if(color==1)
            return new Color(251,148,0); //orange
        if(color==2)
            return new Color(251,222,0); //yellow
        if(color==3)
            return new Color(22, 189, 19); //green
        if(color==4)
            return new Color(2,137,228); //blue

        return new Color(155,12,249); //purple
    }
    private void setEndScreenText(Graphics graphics, Color color, String str)
    {
        graphics.setColor(color);
        graphics.drawString(str, frame.getWidth()/2-(str.length()*2+(str.length()/2))-25, frame.getHeight()/2); //futile attempts to center
    }
    private void setGameOver(Graphics graphics)
    {
        if(asteroids.size()==0)
            allDestroyed=true;
        else allDestroyed=false;

        if(time==0 && lives!=0)
        {
            if(allDestroyed)
                setEndScreenText(graphics, Color.GREEN, "ALL ASTEROIDS DESTROYED, YOU WIN!");
            else setEndScreenText(graphics, Color.YELLOW, "YOU'RE TOO SLOW LOSER");
        }
        else setEndScreenText(graphics, Color.RED, "YOU DIED IDIOT");
    }

    protected void paintComponent(Graphics graphics)
    {
        graphics.setColor(new Color(2,50,102));
        graphics.fillRect(0, 0, frame.getWidth(), frame.getHeight());

        graphics.setColor(new Color(251,155,227));
        graphics.drawString(("Asteroids Hit: "+asteroidsHit), 5, 20);

        graphics.setColor(new Color(246,196,157));
        graphics.drawString(("Time Left: "+time), frame.getWidth()-115, 20);

        graphics.setColor(new Color	(255,177,221));
        graphics.drawString(("Lives Left: "+lives), 5, 40);

        graphics.drawImage(image, shipX, shipY, this);

        graphics.setColor(new Color (82, 119, 155)); //star background modification
        for(int n=0;n<50;n++)
        {
            int x = (int) (Math.random() * frame.getWidth());
            int y = (int) (Math.random() * frame.getHeight());
            if(n%7==0)
                graphics.fillOval(x, y, 4, 4);
        }

        if(!gameOver)
        {
            drawShip(graphics);
            drawProjectiles(graphics);
            drawAsteroids(graphics);
            time--;
        }
        if(lives<=0 || time<=0) {
            gameOver = true;
            setGameOver(graphics);
        }
    }

}
class ImagePanel extends JPanel {
    private Image image;

    public ImagePanel(String imagePath) {
        ImageIcon ship = new ImageIcon(imagePath);
        image = ship.getImage();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

    }
}