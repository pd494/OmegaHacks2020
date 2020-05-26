// BY GOHITHA VENKLURI, PRASANTH DENDUKURI, RAGHAV PUNNAM

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;


/*TeacherInterface is the side of the software will allow the teacher to control and create the test. While those two
 * functions are not currently implemented given the time constraint, the structure and appearance of the interface are
 * implemented in this code. There is a home screen which shows the home page for the class that the teacher teaches,
 * which in this example is a Math Class. From here, the teacher can click a button called "Create New Exam", which
 * will take them to a panel with multiple text fields into which they can enter questions. There are also two check boxes
 * at the top that will allow the teacher to control what happens to the student if they are detected to be attempting
 * to cheat. They can either close the test window, clear the student's answer, or both.*/

public class TeacherInterface 
{

	public TeacherInterface()
	{
		
	}
	public static void main(String[] args) 
	{
		TeacherInterface ti = new TeacherInterface();
		ti.run();
	}
	
	/**
	 * run() creates a JFrame, to which the card layout parent panel is added
	 */
	
	public void run()
	{
		JFrame frame = new JFrame("Lockdown Browser - Teacher");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ParentPanel parent = new ParentPanel();
		frame.getContentPane().add(parent);
		frame.setSize(1280, 800);
		frame.setLocation(0, 0);
		frame.setVisible(true);
	}
}

// holds all the panels in a card layout
class ParentPanel extends JPanel
{
	private CardLayout teacherCards; // layout manager to switch between panels
	public ParentPanel()
	{
		// setup the card layout
		teacherCards = new CardLayout();
		setLayout(teacherCards);
		setBackground(new Color(153,255,255));
		// set up the panels
		HomePagePanel hpp = new HomePagePanel(this, teacherCards);
		AssignmentPanel ap = new AssignmentPanel();
		LogoScreen ls = new LogoScreen(this, teacherCards);
		// add the panels
		add(ls, "loading");
		add(hpp, "home");
		add(ap, "assignments");
	}
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
	}
}

// briefly displays the logo on a loading screen
class LogoScreen extends JPanel implements ActionListener
{
	private ParentPanel a;  // instance to navigate the card layout
	private CardLayout c; // instance to navigate the card layout
	private Timer goToNext; // timer that goes to the next panel after four seconds
	private JLabel image; // holds the logo
	public LogoScreen(ParentPanel ap, CardLayout cards)
	{
		c = cards;
		a = ap;
		// setup the label and add it
		ImageIcon imageicon2 = new ImageIcon("logotest.jpg");
		image = new JLabel("");
		image.setIcon((scaleImage(imageicon2, 6000,400)));
		image = new JLabel(imageicon2);
		setBackground(Color.WHITE);
		image.setPreferredSize(new Dimension(600,600));
		add(image, BorderLayout.CENTER);
		// setup timer that goes to next panel after 4 sec
		goToNext = new Timer(4000, this);
		goToNext.setRepeats(false);
		goToNext.start();
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		g.setFont(new Font("SansSerif", Font.BOLD, 30));
		g.drawString("LOADING...", 570, 650);
	}
	
	// provided from StackOverflow: -
	//https://stackoverflow.com/questions/6714045/how-to-resize-jlabel-imageicon
	//not produced by either Gohitha, Prasanth, or Raghav.
	// method produced by Kyle Phillips, a user on StackOverflow- https://stackoverflow.com/users/5244891/kyle-phillips
	public ImageIcon scaleImage(ImageIcon icon, int w, int h)
	{
		int nw = icon.getIconWidth();
		int nh = icon.getIconHeight();

		if (icon.getIconWidth() > w) {
			nw = w;
			nh = (nw * icon.getIconHeight()) / icon.getIconWidth();
		}

		if (nh > h) {
			nh = h;
			nw = (icon.getIconWidth() * nh) / icon.getIconHeight();
		}

		return new ImageIcon(icon.getImage().getScaledInstance(nw, nh, Image.SCALE_DEFAULT));
	}

	// after 4 seconds, goes to next panelp
	public void actionPerformed(ActionEvent e)
	{
		c.next(a);
	}
}

// first panel, homepage of the class that the teacher teaches
class HomePagePanel extends JPanel implements ActionListener
{
	// navigation
	private ParentPanel parent;
	private CardLayout cards;
	private JButton backButton; // used to go to previous panel
	
	// titles & labels
    private JLabel assignments;
    private JLabel exam;
    private JLabel past;
	
	public HomePagePanel(ParentPanel p, CardLayout c)
	{
		parent = p; // instance of parent panel to call method to navigate
		cards = c; // instance of card layout to navigate
		setBackground(Color.GRAY);
		setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		
		// set up back button
		backButton = new JButton("back");
        backButton.addActionListener(this);
        backButton.setPreferredSize(new Dimension(400,100));
        
        Font font = new Font("SansSerif", Font.BOLD, 25);
        
        //setLayout(new GridLayout(5,1,0,0)); 
        
        // set up labels and buttons
        assignments = new JLabel("Your exams: ");
        assignments.setFont(font);
        
        // set up label that shows the picture of the math test
        JLabel img2 = new JLabel();
		ImageIcon imageicon2 = new ImageIcon("mathtest.jpg");
		img2.setIcon((scaleImage(imageicon2, 600,600)));
		
		// set up the label 
		exam = new JLabel("Unit 5 Exam: 4/23/2020");
		exam.setPreferredSize(new Dimension(5,5));
		exam.setFont(font);

		// set up button that goes to the next panel
		JButton expiredExam = new JButton("Create New Exam");
		expiredExam.addActionListener(this);
		
		// set up labels and buttons for expired exams
		past = new JLabel("Unit 4 Exam: 4/6/2020");
		past.setFont(font);
		JButton expired= new JButton("Unit 5 Exam. 5/18/20");
		JButton past2 = new JButton("Unit 4 Exam. 4/18/2020");
		JLabel past3 = new JLabel("Unit 3 Exam. 3/18/2020");
		past3.setFont(font);
		JLabel temp = new JLabel("");
		temp.setFont(font);
		//temp.setVisible(false);
		
		
		// add components to panel
		add(img2);
        add(assignments);
        add(exam); 
        add(expiredExam);
        add(past);
        add(expired);
        add(past3);
        add(past2);
        add(temp);
        add(backButton);
	}
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		g.setFont(new Font("SansSerif", Font.BOLD, 50));
		g.drawString("Welcome to Math Period 1", 600, 90);
	}
	public void actionPerformed(ActionEvent e)
	{
		if(e.getActionCommand().equals("Create New Exam"));
		{
			cards.next(parent);
		}
	}
	
	// provided from StackOverflow: -
	//https://stackoverflow.com/questions/6714045/how-to-resize-jlabel-imageicon
	//not produced by either Gohitha, Prasanth, or Raghav.
	// method produced by Kyle Phillips, a user on StackOverflow- https://stackoverflow.com/users/5244891/kyle-phillips
	public ImageIcon scaleImage(ImageIcon icon, int w, int h)
	{
		int nw = icon.getIconWidth();
		int nh = icon.getIconHeight();

		if (icon.getIconWidth() > w) {
			nw = w;
			nh = (nw * icon.getIconHeight()) / icon.getIconWidth();
		}

		if (nh > h) {
			nh = h;
			nw = (icon.getIconWidth() * nh) / icon.getIconHeight();
		}

		return new ImageIcon(icon.getImage().getScaledInstance(nw, nh, Image.SCALE_DEFAULT));
	}
}

// place where the teacher creates the questions and can manage the student's test
class AssignmentPanel extends JPanel implements ActionListener, KeyListener
{
	private ParentPanel parent; // navigate the card layout
	private CardLayout cards; // navigate card layout
	private PrintWriter writer; // write to text file
	private JTextField question1; // first question
	private JCheckBox clearField; // checkbox to clear the field if the student is detected to move their mouse outside the window
	private JCheckBox closeWindow; // checkbox to close the window if the student is detected to move their mouse outside the window
	private JTextField question2, question3, question4; // the other three questions, the fourth one is added if the teacher chooses to
	 
	public AssignmentPanel() 
	{
		loadFile();
		
		// set up the checkboxes
		clearField = new JCheckBox("clear the answers");
		closeWindow = new JCheckBox("close the window");
		clearField.setFont(new Font("Serif", Font.BOLD, 30));
		closeWindow.setFont(new Font("Serif", Font.BOLD, 30));

		setBackground(new Color(153,153,255));
		
		// set up first text field
		question1 = new JTextField("Enter Text For Question 1: ");
		question1.setPreferredSize(new Dimension(300, 30));
		question1.addActionListener(this);
		
		// set up 2nd text field
		question2 = new JTextField("Enter Text For Question 2: ");
		question2.setPreferredSize(new Dimension(300, 30));
	
		// set up 3rd text field
		question3 = new JTextField("Enter Text For Question 3: ");
		question3.setPreferredSize(new Dimension(300, 30));

		// set up 4th text field
		question4 = new JTextField("Enter Text For Question 4: ");
		question4.setPreferredSize(new Dimension(300, 30));
		question4.setVisible(false);
		
		setLayout(new GridLayout(8,1));
		
		// set up checkbox label
		JLabel label = new JLabel("If a student attempts to open another application: ");
		label.setFont(new Font("Serif", Font.BOLD, 30));
		
		// button with an icon that allows the teacher to add another question
		JButton image = new JButton(new ImageIcon("addnewq.png"));
		image.setActionCommand("new");
		image.addActionListener(new ImageButtonHandler());
		
		// adding the components to the panel
		add(label);
		add(clearField);
		add(closeWindow);
		add(question1);
		add(question2);
		add(question3);
		add(question4);
		add(image);
	}
	
	// listens to the add new question button
	class ImageButtonHandler implements ActionListener
	{
	
		public void actionPerformed(ActionEvent e) {
			if(e.getActionCommand().equals("new"))
			{
				question4.setVisible(true);
			}
		}
		
	}
	
	// load the output file
	public void loadFile()
	{
		String outFileName = "text.txt";
		File file = new File(outFileName);
		try
		{
			writer = new PrintWriter(file);
		}
		catch(Exception e)
		{
			System.out.print("not found");
		}
		
	}
	

	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
	}
	public void actionPerformed(ActionEvent e)
	{
		String text = question1.getText();
		System.out.println(text);
		writer.println(text); // print the text written by the teacher into a text file
	}

	
	public void keyTyped(KeyEvent e) {
		
	}

	
	public void keyPressed(KeyEvent e) 
	{
		int code = e.getKeyCode();
		if(code == KeyEvent.VK_SHIFT)
		{
			
		}
		
		
	}

	
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	// provided from StackOverflow: -
	//https://stackoverflow.com/questions/6714045/how-to-resize-jlabel-imageicon
	//not produced by either Gohitha, Prasanth, or Raghav.
	// method produced by Kyle Phillips, a user on StackOverflow- https://stackoverflow.com/users/5244891/kyle-phillips
	public ImageIcon scaleImage(ImageIcon icon, int w, int h)
	{
		int nw = icon.getIconWidth();
		int nh = icon.getIconHeight();

		if (icon.getIconWidth() > w) {
			nw = w;
			nh = (nw * icon.getIconHeight()) / icon.getIconWidth();
		}

		if (nh > h) {
			nh = h;
			nw = (icon.getIconWidth() * nh) / icon.getIconHeight();
		}

		return new ImageIcon(icon.getImage().getScaledInstance(nw, nh, Image.SCALE_DEFAULT));
	}
	
	

}

