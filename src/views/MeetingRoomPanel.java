/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package views;

import entities.TwitterFeed;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import models.Customer;
import models.MeetingRoomMovement;
import java.util.List;
import javax.swing.JFrame;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import sandbox.KeyReader;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

/**
 *
 * @author Jon
 */

public class MeetingRoomPanel extends JPanel {
    public static final int TABLEWIDTH = 410;
    public static final int TABLEHEIGHT = 58;
    public static final int PROJECTORWIDTH = 300;
    public static final int PROJECTORHEIGHT = 300;
    public static final int TWITTERWIDTH = 30;
    public static final int TWITTERHEIGHT = 24;

    private Rectangle table;
    private Rectangle projector;
    private Rectangle twitter;
    private MeetingRoomMovement characterMovement;
    private Customer student;
    private TwitterFeed twitterFeed;

    private JLabel temp = new JLabel();

    public MeetingRoomPanel(Customer inf_Student, MeetingRoomMovement inf_charMovement) {
        super();
        student = inf_Student;
        characterMovement = inf_charMovement;
        setSize(800, 600);
        setLayout(null);
        add(temp);
        temp.setBounds(200, 200, 200, 200);
        init();
        placeStations();
        this.addKeyListener(characterMovement);

        this.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e)
            {
//                System.out.println(e.getPoint());
                temp.setText(e.getPoint().toString());
            }
        });
        this.setFocusable(true);
    }

    private void init() {
        table = new Rectangle();
        projector = new Rectangle();
        twitter = new Rectangle();
    }

    private void placeStations() {
        student.setBounds(400, 500, student.width, student.height);
        table.setBounds(180, 245, TABLEWIDTH, TABLEHEIGHT);
        projector.setBounds(670, 400, PROJECTORWIDTH, PROJECTORHEIGHT);
        twitter.setBounds(158, 121, TWITTERWIDTH, TWITTERHEIGHT);
        
    }
    
    public Rectangle getTable()
    {
        return table;
    }
    
    public Rectangle getProjector()
    {
        return projector;
    }
    
    public Rectangle getTwitter(){
        return twitter;
    }
    
    JFrame timelineFrame;
    JTextArea timelineTweets;
    JScrollPane timelineScrollPane;
    
    
    public void initTimeline(){
        timelineFrame = new JFrame("@SIM_IST Timeline");
        
        timelineFrame.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        
        
        timelineTweets = new JTextArea();
        Font font = new Font("Gotham Narrow", Font.BOLD, 12);
        timelineTweets.setFont(font);
        timelineTweets.setEditable(false);
        timelineScrollPane = new JScrollPane(timelineTweets);
            c.fill = GridBagConstraints.HORIZONTAL;
            c.weightx = 0;
            c.ipady = 200;
            c.gridx = 0;
            c.gridy = 0;
            c.gridwidth = 3;
            timelineFrame.add(timelineScrollPane, c);
        
        KeyReader keys = new KeyReader();
        
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey(keys.getConsumerKey())
                .setOAuthConsumerSecret(keys.getConsumerSecret())
                .setOAuthAccessToken(keys.getAccessToken())
                .setOAuthAccessTokenSecret(keys.getAccessTokenSecret());
        TwitterFactory tf = new TwitterFactory(cb.build());
        Twitter twitter = tf.getInstance();
        try{
            System.out.println("timeline retreval worked");
        
        
        List<Status> statuses = twitter.getHomeTimeline();
        for(Status status : statuses){
            timelineTweets.append("@"+status.getUser().getScreenName()+" : "+status.getText()+"\n"+"\n");
            timelineTweets.setLineWrap(true);
            timelineTweets.setWrapStyleWord(true);
            timelineTweets.setCaretPosition(0);
            System.out.println("@"+status.getUser().getName()+" : "+status.getText());
        }
        
        
        }catch (TwitterException te){
            System.out.print("timeline retreval failed");
            te.printStackTrace();
        }
        
        timelineFrame.pack();
        timelineFrame.setSize(600, 300);
        timelineFrame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        timelineFrame.setLocationRelativeTo(null);
        timelineFrame.setVisible(true);
    }
    
    
    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);
        
        

        g.drawImage(new ImageIcon("meetingRoom.png").getImage(), 0, 0, null);
        g.drawImage(new ImageIcon("twitter.png").getImage(), 15, 15, null);
        g.drawImage(new ImageIcon(characterMovement.getAnimation()).getImage(), student.x, student.y, null);

    }

}
