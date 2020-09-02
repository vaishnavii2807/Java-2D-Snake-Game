import java.awt.*;                                           // create graphical user interface ( GUI ) objects, such as buttons, scroll bars, and windows
import java.awt.event.*;                                     // defines classes and interfaces used for event handling , like key event,key listener
import javax.swing.*;                                        // provides interfaces, so that the program runs the same on all platforms
import java.util.*;                                          // contains collection framework, collection classes

public class Snake {
     // GUI components
    private JPanel board;            
    private JButton[] snakeBodyPart;
    private JButton bonusfood;
    private JTextArea scoreViewer;

    // Constants
    private final int SNAKE_RUNNING_SPEED_FASTEST = 25;                  // for level 3
    private final int SNAKE_RUNNING_SPEED_FASTER = 50;                   // for level 2
    private final int SNAKE_RUNNING_SPEED_FAST = 100;                    // for level 1
    private final int BOARD_WIDTH = 700;
    private final int BOARD_HEIGHT = 550;
    private final int SCORE_BOARD_HEIGHT = 20;
    private final int SNAKE_LENGTH_DEFAULT = 4;
    private final int SNAKE_BODY_PART_SQURE = 10;      
    private final int BONUS_FOOD_SQURE = 15;
    private final Point INIT_POINT = new Point(100, 150);            // the snake begins to move from x=100 and y=150

    // Others values
    private enum GAME_TYPE {NO_MAZE, BORDER};                          //Game type is a class containing the methods i.e No_maze, border
    private int selectedSpeed = SNAKE_RUNNING_SPEED_FAST;             // default values when the game will begin (selectedSpeed is the method
    private GAME_TYPE selectedGameType = GAME_TYPE.NO_MAZE;           // selected game type is a variable where the values will be stored
    private int totalBodyPart;                                       // variable to calculate growth of snake
    private int directionX;                                          // variable for left
    private int directionY;                                         // variable for right
    private int score;                                              // counts score
    private Point pointOfBonusFood = new Point();                   //point where bonus food will be generated
    private boolean isRunningLeft;                                 // variable of direction of boolean type                  
    private boolean isRunningRight;                                // Point() is a class in java to get the location 
    private boolean isRunningUp;     
    private boolean isRunningDown;
    private boolean isBonusFoodAvailable;
    private boolean isRunning;
    private Random random = new Random();                         //creating new object of random


    public Snake() {
        //initialize all variables.
        resetDefaultValues();                                    //method containing default startup of the game
        // initialize GUI.
        init();                                                  //method in gui
        // Create Initial body of a snake.
        createInitSnake();                                       //method with default snake body
        // Initialize Thread.
        isRunning = true;                                        // to check if isRunning  = true
        createThread(); 
    }
    public void init() {                        
        JFrame frame = new JFrame("Snake");                      // Naming the JFrame "Snake"
        frame.setSize(708, 625);                                // the size of the JFrame should be equal to JPanel board

        //Create Menu bar with functions
        setJMenueBar(frame);                                    //frame is passed as a parameter

        // Start of UI design
        JPanel scorePanel = new JPanel();                      // an area where components can be designed
        scoreViewer = new JTextArea("Score ==>" + score);      // object score viewer is delclared as TextArea, since it will display the score only
        scoreViewer.setEnabled(false);                         // set enabled is kept false so that the sore cannot be modified by the user
        scoreViewer.setBackground(Color.BLACK);                // setting textArea score viewer to Black

        board = new JPanel();                                  //creating board object for movement of snake
        board.setLayout(null);                                 //using null layout of jpanel to track the cellin form of (x,y) coordinates
        board.setBounds(0, 0, BOARD_WIDTH, BOARD_HEIGHT);      //used in a situation to set size and position (0,0, .. marks where the top left corner of the applet will be placed)
        board.setBackground(Color.BLACK);                      // setting Backgroud colour as black
        scorePanel.setLayout(new GridLayout(0, 1));            // creates a grid layout with specific rows and column
        scorePanel.setBounds(0, BOARD_HEIGHT, BOARD_WIDTH, SCORE_BOARD_HEIGHT); //creating the folowing wrt jPanel as a whole
        scorePanel.add(scoreViewer);                           // will add the updated score

        frame.getContentPane().setLayout(null);                 //used to hold objects that were defined before
        frame.getContentPane().add(board);                      // board objects are held
        frame.getContentPane().add(scorePanel);                 // score panel objects are held
        frame.setVisible(true);                                 //frame is Visible to user
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);   // method set to close the jFrame when user clicks on exit. 
        frame.addKeyListener(new KeyAdapter() {                 //using listener with its adapter class                                       
            public void keyPressed(KeyEvent e) {                // e is an event of key for reading key pressed by the user
                snakeKeyPressed(e);                             // class with parameter e passed
            }
        });
        frame.setResizable(false);                             //size of frame cannot be changed 
    }

    public void setJMenueBar(JFrame frame) {                  //method setMenuBar used to set the drop down list under "Snake" (eg. selecting levels)                

        JMenuBar mymbar = new JMenuBar();                    // created a menu bar named mymbar

        JMenu game = new JMenu("Game");                      // creating a menu game under which we have option "new game" , "exit"
        JMenuItem newgame = new JMenuItem("New Game");       // under game
        JMenuItem exit = new JMenuItem("Exit");              // under game
        newgame.addActionListener(new ActionListener() {     // creating action listener to record the response of user, what component is clicked on
            public void actionPerformed(ActionEvent e) {     //changes to be made in the game after recording the user response, using event e
                startNewGame();                              // class startNewGame is called by default when user selects new game 
            }
        });
        exit.addActionListener(new ActionListener() {         // ActionListener when the user response is "exit"
            public void actionPerformed(ActionEvent e) {      // changes made in the game to exit
                System.exit(0);                               // syntax to exit the program
            }
        });
        game.add(newgame);                                    // drop down option under game
        game.addSeparator();                                  // a Jswing framework here used to divide new game and exit components
        game.add(exit);                                       // dropdown option under game
        mymbar.add(game);                                     // "game" option will be displayed on the menu bar

        JMenu type = new JMenu("Type");                       // creating an option "Type"in menu bar
        JMenuItem noMaze = new JMenuItem("No Maze");          // "no maze" option under type
        noMaze.addActionListener(new ActionListener() {       // Action Listener when user selects "no_maze" option
            public void actionPerformed(ActionEvent e) {      // through event e changes will happen in game when user selects no_maze option
                selectedGameType = GAME_TYPE.NO_MAZE;         // when user selects "no_maze" again start new game class is called
                board.setBorder(BorderFactory.createLineBorder(Color.WHITE,5));  
                startNewGame();
            }
        });
        JMenuItem border = new JMenuItem("Border Maze");         // creating option 'border maze' under "Type"
        border.addActionListener(new ActionListener() {          // to record response when user selects "BorderMaze"
            public void actionPerformed(ActionEvent e) {         // through event changes will happen in game when user selects border maze
                selectedGameType = GAME_TYPE.BORDER;                           
                startNewGame();   // startNewGame() method called after user selects border_maze
                board.setBorder(BorderFactory.createLineBorder(Color.RED,5));  // will create a border of color=red and thickness =5
            }
        });
        type.add(noMaze);                                         // creating an item under "Type"
        type.add(border);                                         // creating an item "border" under type
        mymbar.add(type);                                         // "Type" option will be displayed on the menu bar

        JMenu level = new JMenu("Level");                         // creating an option "level" on the menu bar
        JMenuItem level1 = new JMenuItem("Level 1");              // level1 option under level created
        level1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                selectedSpeed = SNAKE_RUNNING_SPEED_FAST;         //assigning variable 'SNAKE_RUNNING_SPEED_FAST=100' to selected speed
                startNewGame();
            }
        });
        JMenuItem level2 = new JMenuItem("Level 2");              //"level2" option created in drop down under level
        level2.addActionListener(new ActionListener() {           // level2 option under level created
            public void actionPerformed(ActionEvent e) {
                selectedSpeed = SNAKE_RUNNING_SPEED_FASTER;        //assigning variable 'SNAKE_RUNNING_SPEED_FAST=50' to selected speed
                startNewGame();
            }
        });
        JMenuItem level3 = new JMenuItem("Level 3");              //"level3" option created in drop down under level
        level3.addActionListener(new ActionListener() {           // level3 option under level created
            public void actionPerformed(ActionEvent e) {
                selectedSpeed = SNAKE_RUNNING_SPEED_FASTEST;      //assigning variable 'SNAKE_RUNNING_SPEED_FAST=50' to selected speed
                startNewGame();
            }
        });
        level.add(level1);                                         // creating level1 option under level
        level.add(level2);                                         // creating level 2 option under level
        level.add(level3);                                         //creating level 3 otion under level
        mymbar.add(level);                                         // creating a 'menu' option on the menu bar

        
        frame.setJMenuBar(mymbar);                                // displaying the menu bar
    }
    public void resetDefaultValues() {
        snakeBodyPart = new JButton[200];                  
        totalBodyPart = SNAKE_LENGTH_DEFAULT;                   //snake body initially
        directionX = SNAKE_BODY_PART_SQURE;                 
        directionY = 0;                                     
        score = 0;
        isRunningLeft = false;                                  // running left set to false because snake go backward
        isRunningRight = true;                                 //snake can go right
        isRunningUp = true;                                    // snake cant go up
        isRunningDown = true;                                  //snake cant go down
        isBonusFoodAvailable = false;                         //initially no bonus food will be given
    }

    void startNewGame() {                                    // method for new game
        resetDefaultValues();                                // method for default setup
        board.removeAll();                                  // To clear the exixting elements    (removeAll() is a class)
        createInitSnake();                                  // snake of default size will appear on screen since its a new game
        scoreViewer.setText("Score==>" + score);            //score viewr object is used to display the score using 'score' variable
        isRunning = true;                                   //As the game window opens snake starts moving by default
    }

    /**
     * This method is responsible to initialize the snake with four body part.
     */
    public void createInitSnake() {
        // Location of the snake's head.
        int x = (int) INIT_POINT.getX();                                //implicit typecasting into int
        int y = (int) INIT_POINT.getY();                                 //implicit typecasting into int

        // Initially the snake has three body part.
        for (int i = 0; i < totalBodyPart; i++) {
            snakeBodyPart[i] = new JButton();            
            snakeBodyPart[i].setBounds(x, y, SNAKE_BODY_PART_SQURE, SNAKE_BODY_PART_SQURE);
            snakeBodyPart[i].setBackground(Color.GREEN);                                          // Setting snake body to grey
            board.add(snakeBodyPart[i]);
            // Set location of the next body part of the snake.
            x = x - SNAKE_BODY_PART_SQURE;
        }

        createFood();      
    }

    /**
     * The last part of this snake is treated as a food, which has not become a body part of the snake yet.
     * This food will be the body part if and only if when snake head will touch it.
    */
    void createFood() {
        int randomX = SNAKE_BODY_PART_SQURE + (SNAKE_BODY_PART_SQURE * random.nextInt(48));        //variable snake body part sq=10+ (10*48) wrt x
        int randomY = SNAKE_BODY_PART_SQURE + (SNAKE_BODY_PART_SQURE * random.nextInt(23));        //variable snake body part sq=10+ (10*23) wrt y

        snakeBodyPart[totalBodyPart] = new JButton();
        snakeBodyPart[totalBodyPart].setEnabled(false);                                            //no action will be performed if clicked on snake                                         
        snakeBodyPart[totalBodyPart].setBounds(randomX, randomY, SNAKE_BODY_PART_SQURE, SNAKE_BODY_PART_SQURE);
        board.add(snakeBodyPart[totalBodyPart]);

        totalBodyPart++;                                                                             
    }

    private void createBonusFood() {             //declared AS JButton so that, when snakes hesd touches the food , button is placed                                                       
        bonusfood = new JButton();
        bonusfood.setEnabled(false);             
        //Set location of the bonus food.
        int bonusFoodLocX = SNAKE_BODY_PART_SQURE * random.nextInt(50);                           //location of bonus food wrt x,(10*50)
        int bonusFoodLocY = SNAKE_BODY_PART_SQURE * random.nextInt(25);                          //location of bonus food wrt x,(10*25)

        bonusfood.setBounds(bonusFoodLocX, bonusFoodLocY, BONUS_FOOD_SQURE, BONUS_FOOD_SQURE);
        pointOfBonusFood = bonusfood.getLocation();                                             // getLocation is the method which is called by the object bonusfood, the value is the stored in pointofBonusFood   
        board.add(bonusfood);                                                                   // adding element bonus food on board
        isBonusFoodAvailable = true;                             
    }

    /**
     * Process next step of the snake.
     * And decide what should be done.
     */
    void processNextStep() {
        boolean isBorderTouched = false;                                                //isbordertouched is set false snake will not hit the border immediately when the game has started
        // Generate new location of snake's head.
        int newHeadLocX = (int) snakeBodyPart[0].getLocation().getX() + directionX;          //implicit type casting int         
        int newHeadLocY = (int) snakeBodyPart[0].getLocation().getY() + directionY;

        //last part of the snake is food.
        int foodLocX = (int) snakeBodyPart[totalBodyPart - 1].getLocation().getX();
        int foodLocY = (int) snakeBodyPart[totalBodyPart - 1].getLocation().getY();

        // Check if snake crosses the border of the board
        if (newHeadLocX >= BOARD_WIDTH - SNAKE_BODY_PART_SQURE) {           //head location greater tha equal to board width -10, snake dies and starts back from cell no.=0
            newHeadLocX = 0;                         //restarts, hence back to 0.
            isBorderTouched = true;
        } else if (newHeadLocX <= 0) {                                      //left border of the gamebox  
            newHeadLocX = BOARD_WIDTH - SNAKE_BODY_PART_SQURE;
            isBorderTouched = true;  
        } else if (newHeadLocY >= BOARD_HEIGHT - SNAKE_BODY_PART_SQURE) {    //bottom border (exceeds the coordinate values)
            newHeadLocY = 0;                                               //snake dies hence reset to 0.
            isBorderTouched = true;
        } else if (newHeadLocY <= 0) {                                        //upper border of gamebox      
            newHeadLocY = BOARD_HEIGHT - SNAKE_BODY_PART_SQURE;
            isBorderTouched = true;
        }

        // Check if snake has touched the food
        if (newHeadLocX == foodLocX && newHeadLocY == foodLocY) {  
            // Set score
            score += 5;                                                         //score will be incremented by 5
            scoreViewer.setText("Score==>" + score);                            // displaying the updated score

            // Check bonus food should be given or not                          
            if (score % 50 == 0 && !isBonusFoodAvailable) {                     //score is a multiple of 50
                createBonusFood();                                              // methpd that will create bonus food on screen
            }
            // Create new food.
            createFood();                                                       //after eating bonus food again simple food will be displayed
        }

        // to Check if the snake has touched the bonus food
        if (isBonusFoodAvailable &&
                pointOfBonusFood.x <= newHeadLocX &&
                pointOfBonusFood.y <= newHeadLocY &&
                (pointOfBonusFood.x + SNAKE_BODY_PART_SQURE) >= newHeadLocX &&  
                (pointOfBonusFood.y + SNAKE_BODY_PART_SQURE) >= newHeadLocY) {        // condition of snake eating bonus food
            board.remove(bonusfood);                                                  //bonus food will vanish once consumed
            score += 100;                                                             // if snake eats bonus food score will be incremented by 100
            scoreViewer.setText("Score ==>" + score);                                 // score will be displayed           
            isBonusFoodAvailable = false;                                             // it is already eaten and no longer available on screen                                           
        }
        
        //To Check if game is over
        if(isGameOver(isBorderTouched, newHeadLocX, newHeadLocY)) {                     // parameters that will affect gameover class
           scoreViewer.setText("GAME OVER	" + score);                             //final score will be displayed 
           isRunning = false;                                                           //since the game is over the final score will be displayed
           return;
        } else {
            // Move the entire snake body to forword.
            moveSnakeForward(newHeadLocX, newHeadLocY);                                 // Affecting movement the movement of the snake
        }

        board.repaint();                                                                // everytime that the snake moves its body remains grey and remaining cells of the board are painted white
    }

    /**
     * This method is responsible to detect if game is over or not
     * Game should be over while snake is touched by border or by itself.
     */
    private boolean isGameOver(boolean isBorderTouched, int headLocX, int headLocY) {  //method to check if game is over, factors affecting our past as arguments   
        switch(selectedGameType) {                                                    //selected game type is checked 
            case BORDER:                                                             // if game type is border
                if(isBorderTouched) {                                                // if border is touched the game will be over
                    return true;                                                     // if border is touched we return true i.e. game is over
                }
                break;
            default:
                break;                                                            // if none of the above conditions are satisfied, game is not over
        }
        
        for (int i = SNAKE_LENGTH_DEFAULT; i < totalBodyPart - 2; i++) {           
            Point partLoc = snakeBodyPart[i].getLocation();
            //System.out.println("("+partLoc.x +", "+partLoc.y+")  ("+headLocX+", "+headLocY+")");    //game ends if the snake bites itself
            if (partLoc.equals(new Point(headLocX, headLocY))) {                   
                return true;   
            }
        }

        return false;                //                                                                  
    }

    /**
     * Every body part should be placed to location of the front part.
     */
    public void moveSnakeForward(int headLocX, int headLocY) {
        for (int i = totalBodyPart - 2; i > 0; i--) {                           
            Point frontBodyPartPoint = snakeBodyPart[i - 1].getLocation();      
            snakeBodyPart[i].setLocation(frontBodyPartPoint);                 
        }
        snakeBodyPart[0].setBounds(headLocX, headLocY, SNAKE_BODY_PART_SQURE, SNAKE_BODY_PART_SQURE);   
    }

    public void snakeKeyPressed(KeyEvent e) {
        // snake should move to left when player pressed left arrow key
        if (isRunningLeft == true && e.getKeyCode() == 37) {                    // 37 is the value of the left arrow key
            directionX = -SNAKE_BODY_PART_SQURE;                                // means snake move right to left by 10 pixel, beacuse snake body part square = 10
            directionY = 0;
            isRunningRight = false;                                             // means snake cant move from left to right (cant go backwards)
            isRunningUp = true;                                                 // means snake can move from down to up
            isRunningDown = true;                                               // means snake can move from up to down
        }
        // snake should move to up when player pressed up arrow key
        if (isRunningUp == true && e.getKeyCode() == 38) {                       // 38 is the value of upward arrow key
            directionX = 0;
            directionY = -SNAKE_BODY_PART_SQURE;                                 // means snake moves from down to up by 10 pixel
            isRunningDown = false;                                               // means snake cant move from up to down(cant go  downwards wrt up key)
            isRunningRight = true;                                               // means snake can move from left to right
            isRunningLeft = true;                                                // means snake can move from right to left
        }
        // snake should move to right when player presses right arrow keys      
        if (isRunningRight == true && e.getKeyCode() == 39) {                    //value of right arrow key is 39
            directionX = +SNAKE_BODY_PART_SQURE;                                 // means snake move from left to right by 10 pixel , because bodypart square =10
            directionY = 0;
            isRunningLeft = false;
            isRunningUp = true;
            isRunningDown = true;
        }
        // snake should move to down when player pressed down arrow
        if (isRunningDown == true && e.getKeyCode() == 40) {                     //value for down arrow key
            directionX = 0;                                                      //as snake is moving on y axis
            directionY = +SNAKE_BODY_PART_SQURE;                                // means snake move from left to right by 10 pixel
            isRunningUp = false;
            isRunningRight = true;
            isRunningLeft = true;
        }
    }

    private void createThread() {                                               
        // start thread
        Thread thread = new Thread() {                             

            public void run() {
                runIt();                                           // the snake will keep running
            }
        };
        thread.start();                                            // go to runIt() method
    }

    public void runIt() {                                                       
        while (true) {
            if(isRunning) {
                // Process what should be next step of the snake.
                processNextStep();
                try {                                                   
                    Thread.sleep(selectedSpeed);
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                }
            }
        }
    }
}

    