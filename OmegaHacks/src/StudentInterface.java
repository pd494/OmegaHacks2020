// BY GOHITHA VENKLURI, PRASANTH DENDUKURI, RAGHAV PUNNAM

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.io.File;
import java.io.IOException;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;

/**
 * StudentInterface is the interface of a student in the LockTest app. StudentInterface contains all necessary panels
 * for the interface. The interface of a student includes the home screen, which has buttons that leads to the classes
 * the students are in. After a student presses one of the buttons that leads to his classes, a new interface is introduced.
 * That interface contains all exams the student previously took, and will take in the future. If the student presses on a current exam,
 * it leads to a new interfaces, the exam interface. In the exam interface, the student is faced with the questions that the teacher
 * created for the student. On the top, is their webcam, so the teacher can see the student at all times. The exam interface is structured 
 * so that if the student takes the mouse outside of the exam panel, all current progress(writing and selected answers are lost). If the time
 * runs out for the exam, the student is taken back to the home screen.
 *
 */
public class StudentInterface
{

	private JFrame frame; // holds the entire interface
	private JTextArea answer; // text area where the student will answer their free response question
	private JLabel display; // displays the time that the student has left
	private ExamPanel ep; // instance of the panel where the test is taken

	public StudentInterface()
	{

	}

	// runs the entire program
	public static void main(String[] args)
	{
		StudentInterface md = new StudentInterface();
		md.run();
	}

	/**
	 * run() creates a JFrame, which holds the panel BackgroudPanel, which is
	 * essentially the "main menu" - the first screen the user sees after opening
	 * the app. //
	 */
	public void run()
	{
		frame = new JFrame("Lockdown Browser - Student");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		AllPanelHolder aph = new AllPanelHolder();
		frame.getContentPane().add(aph);
		frame.setSize(1280, 800);
		frame.setLocation(0, 0);
		frame.setVisible(true);

	}

	/**
	 * holds all panels, and facilitates a card layout, so that panels can be switched between
	 * based on the users actions.
	 *
	 */
	public class AllPanelHolder extends JPanel
	{
		private CardLayout cards; // layout manager that allows to switch between panels
		
		/** 
		 * creates all neccessary components
		 */
		public AllPanelHolder()
		{
			CardLayout cards = new CardLayout();
			setLayout(cards);
			
			// adding all the panels to the card layout
			LogoScreen ls = new LogoScreen(this, cards);
			add(ls, "loading");

			BackgroundPanel bp = new BackgroundPanel(this, cards);
			add(bp, "start");

			MathClassPanel mcp = new MathClassPanel(this, cards);
			add(mcp, "math");

			ep = new ExamPanel(this, cards);
			add(ep, "exam");

		}
		
		/**
		 * displays the LockTest logo image in the loading screen
		 * 
		 */
		class LogoScreen extends JPanel implements ActionListener
		{
			private AllPanelHolder a; // instance of parent panel to navigate card layout
			private CardLayout c; // instance of card layout to navigate
			private Timer goToNext; // timer that allows the loading screen to close after 3 sec
			private JLabel image; // holds the logo

			public LogoScreen(AllPanelHolder ap, CardLayout cards)
			{
				c = cards;
				a = ap;
				ImageIcon imageicon2 = new ImageIcon("logotest.jpg");
				image = new JLabel("");
				image.setIcon((scaleImage(imageicon2, 6000, 400)));
				image = new JLabel(imageicon2);
				setBackground(Color.WHITE);
				image.setPreferredSize(new Dimension(600, 600));
				add(image, BorderLayout.CENTER);
				goToNext = new Timer(3000, this);
				goToNext.setRepeats(false);
				goToNext.start();
			}
			
			/**
			 * writes the "Loading" text
			 */
			public void paintComponent(Graphics g)
			{
				super.paintComponent(g);
				g.setFont(new Font("SansSerif", Font.BOLD, 30));
				g.drawString("LOADING...", 570, 650);
			}

			//provided from StackOverflow: -
			//https://stackoverflow.com/questions/6714045/how-to-resize-jlabel-imageicon
			//not produced by either Gohitha, Prasanth, or Raghav.
			//method produced by Kyle Phillips, a user on StackOverflow- https://stackoverflow.com/users/5244891/kyle-phillips
			public ImageIcon scaleImage(ImageIcon icon, int w, int h)
			{
				int nw = icon.getIconWidth();
				int nh = icon.getIconHeight();

				if (icon.getIconWidth() > w)
				{
					nw = w;
					nh = (nw * icon.getIconHeight()) / icon.getIconWidth();
				}

				if (nh > h) {
					nh = h;
					nw = (icon.getIconWidth() * nh) / icon.getIconHeight();
				}

				return new ImageIcon(icon.getImage().getScaledInstance(nw, nh, Image.SCALE_DEFAULT));
			}
			
			/**
			 * if the user clicks the math class button, it takes him to the the Math class panel
			 **/
			public void actionPerformed(ActionEvent e)
			{
				c.next(a);
			}
		}

		/** holds all panels to facilitate a BorderLayout **/
		class BackgroundPanel extends JPanel
		{
			private AllPanelHolder parent; 
			private CardLayout cl;

			public BackgroundPanel(AllPanelHolder a, CardLayout c)
			{
				cl = c;
				parent = a;
				Color color = new Color(154, 194, 246);
				setBackground(color);
				setLayout(new BorderLayout(0, 0));

				TitlePanel tp = new TitlePanel();
				tp.setOpaque(true);
				add(tp, BorderLayout.CENTER);

				ImagePanel ip = new ImagePanel();
				ip.setOpaque(true);
				add(ip, BorderLayout.NORTH);

				ClassButton cb = new ClassButton(a, cl);
				add(cb, BorderLayout.SOUTH);

			}

			/**
			 * 
			 * @param g Graphics instance that allows us to modify graphics
			 */
			public void paintComponent(Graphics g)
			{
				super.paintComponent(g);
			}

		}

		/**
		 * holds the title "LockTest"
		 */
		class TitlePanel extends JPanel
		{
			public TitlePanel()
			{

				setBackground(new Color(192, 222, 254));
				this.setPreferredSize(new Dimension(100, 100));

				Font font = new Font("SansSerif", Font.BOLD, 25);
				JLabel title = new JLabel("LockTest: A secure app for Remote Testing");
				title.setBackground(new Color(248, 248, 255));
				title.setFont(font);
				title.setPreferredSize(new Dimension(530, 200));
				add(title);
			}

			public void paintComponent(Graphics g)
			{
				super.paintComponent(g);
			}
		}

		/**
		 * holds the Images in the center
		 *
		 */
		class ImagePanel extends JPanel
		{
			public ImagePanel()
			{

				super();
				this.setOpaque(true);
				this.setPreferredSize(new Dimension(100, 300));
				setLayout(new BorderLayout(50, 0));
				Color color = new Color(217, 217, 253);
				setLayout(new BorderLayout());
				setBackground(color);

				JLabel img2 = new JLabel();
				ImageIcon imageicon2 = new ImageIcon("testimg2.png");
				img2.setIcon((scaleImage(imageicon2, 6000, 400)));
				add(img2, BorderLayout.WEST);

				JLabel img3 = new JLabel();
				ImageIcon imageicon3 = new ImageIcon("testimg3.jpg");
				img3.setIcon((scaleImage(imageicon3, 500, 550)));
				add(img3, BorderLayout.EAST);

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

			public void paintComponent(Graphics g)
			{
				super.paintComponent(g);
			}

		}

		/**
		 * this class contains the JButtons that lead the user to each of their 
		 * class that they are enrolled in.
		 *
		 */
		class ClassButton extends JPanel implements ActionListener
		{

			private JButton math; // connected to actionlistener, takes user to math class
			private JButton history;
			private JButton biology;

			private CardLayout cards;
			private AllPanelHolder aph;

			public ClassButton(AllPanelHolder a, CardLayout cl)
			{

				setLayout(new FlowLayout(FlowLayout.CENTER, 55, 15));

				Color color = new Color(154, 194, 246);
				setBackground(color);

				cards = cl;
				aph = a;
				
				// set up buttons
				math = new JButton("Math p1");
				math.addActionListener(this);
				math.setPreferredSize(new Dimension(300, 200));

				history = new JButton("History P2");
				history.setPreferredSize(new Dimension(300, 200));

				biology = new JButton("Biology P2");
				biology.setPreferredSize(new Dimension(300, 200));
				
				// add buttons
				add(math);
				add(history);
				add(biology);

			}
			
			/*
			 * if the user clicks on his math class buttons, 
			 * it takes him to the Math Class Panel,
			 * which is an interface with the students assignments and exams
			 */
			public void actionPerformed(ActionEvent e)
			{
				String comm = new String(e.getActionCommand());
				if (comm.equals("Math p1")) {
					cards.show(aph, "math");
				}
			}

			public void paintComponent(Graphics g)
			{
				super.paintComponent(g);
			}

		}
	}

	// panel where the student sees all their previous test and can click on the latest one to access the current test
	class MathClassPanel extends JPanel implements ActionListener
	{
		private AllPanelHolder a; // used to navigate card layout
		private CardLayout c; // used to navigate card layout
		private JButton backButton; // used to go to previous panel

		private JLabel assignments;
		private JLabel exam;
		private JLabel past;
		private JButton expiredExam;

		public MathClassPanel(AllPanelHolder aph, CardLayout cards)
		{
			setLayout(new GridLayout(5, 5));

			setBackground(new Color(173, 216, 230));
			a = aph;
			c = cards;
			
			// set up navigation buttons
			backButton = new JButton(new ImageIcon("newback.png"));
			backButton.addActionListener(this);
			backButton.setActionCommand("back");
			backButton.setPreferredSize(new Dimension(400, 200));

			Font font = new Font("SansSerif", Font.BOLD, 30);
			JTextField field = new JTextField("yooo");

			// setLayout(new GridLayout(5,1,0,0));

			assignments = new JLabel("Your exams: ");
			assignments.setFont(font);

			JLabel img2 = new JLabel();
			ImageIcon imageicon2 = new ImageIcon("mathtest.jpg");
			img2.setIcon((scaleImage(imageicon2, 600, 600)));
 
			exam = new JLabel("Unit 5 Exam: 4/23/2020");
			exam.setPreferredSize(new Dimension(5, 5));
			exam.setFont(font);

			expiredExam = new JButton(new ImageIcon("newexamtoday2.png"));
			expiredExam.addActionListener(this);

			past = new JLabel("\tUnit 4 Exam: 4/6/2020");
			past.setFont(font);

			// set up image labels
			JButton expired = new JButton(new ImageIcon("newexpired.png"));
			
			JButton past2 = new JButton(new ImageIcon("newexpired.png"));
			JLabel past3 = new JLabel("	Unit 3 Exam. 3/18/2020");
			past3.setFont(font);

			// add components
			add(img2);
			add(assignments);
			add(exam);
			add(expiredExam);
			add(past);
			add(expired);

			add(past3);
			add(past2);

			add(backButton);

		}
		
		// navigate card layout
		public void actionPerformed(ActionEvent e)
		{
			if (e.getActionCommand().equals("back")) {
				c.previous(a);
			}

			if (e.getSource() == expiredExam) {

				c.show(a, "exam");
			}
		}

		public void paintComponent(Graphics g)
		{
			super.paintComponent(g);
		}

		//provided from StackOverflow: -
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
	
	/**
	 * panel that contains the actual exam that the user takes, with all the 
	 * questions that the teacher made
	 * @author prasa
	 *
	 */
	class ExamPanel extends JPanel implements MouseListener, FocusListener, KeyListener
	{
		private JTextArea exam;
		private JRadioButton choice1, choice2, choice3, choice4; // answer choices
		private ButtonGroup bg;
		private Robot robot;
		private boolean outsideWindow;
		private boolean clicked;
		private boolean clear;
		private int x;
		private int y;
		private JScrollBar scroll;
		private AllPanelHolder aph;
		private CardLayout c;

		public ExamPanel(AllPanelHolder allPanelHolder, CardLayout cards)
		{
			c = cards;
			aph = allPanelHolder;
			addMouseListener(this);
			addKeyListener(this);
			addFocusListener(this);
			setFocusable(true);
			clicked = false;
			outsideWindow = false;
			clear = false;
			setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

			ImagePanel ip = new ImagePanel();
			add(ip);

			Question1 q1 = new Question1();
			add(q1);

			Question2 q2 = new Question2();
			add(q2);

			TimerPanel tp = new TimerPanel();
			add(tp);

		}

		public void paintComponent(Graphics g)
		{
			super.paintComponent(g);
		}

		public void mouseClicked(MouseEvent e)
		{
			

		}

		public void mousePressed(MouseEvent e)
		{
			clicked = true;
		}

		public void mouseReleased(MouseEvent e)
		{

		}

		public void mouseEntered(MouseEvent e)
		{

		}
		
		// clears the users answers if the user tried to open another application
		// or go outside the exam panel
		public void mouseExited(MouseEvent e)
		{
			outsideWindow = true;
			detectMouse();
			answer.setText("");
		}

		public void detectMouse()
		{

		}

		public void focusGained(FocusEvent e)
		{

		}

		public void focusLost(FocusEvent e)
		{
			answer.setText("");
		}

		public void keyTyped(KeyEvent e)
		{

		}
		
		// clears the text field if the user tries to alt tab(switch tabs to exit the exam panel)
		public void keyPressed(KeyEvent e)
		{
			if (e.getKeyCode() == KeyEvent.VK_ALT || e.getKeyCode() == KeyEvent.VK_TAB) {
				answer.setText("");
			}
		}

		public void keyReleased(KeyEvent e)
		{

		}
		
		// holds the webcamm of the user
		class ImagePanel extends JPanel
		{
			private JLabel title;

			public ImagePanel()
			{
				setBackground(new Color(154, 194, 246));
				setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));

				JLabel img2 = new JLabel();
				// setting up webcam placeholder
				ImageIcon imageicon2 = new ImageIcon("student.jpg");
				img2.setIcon((scaleImage(imageicon2, 500, 500)));
				add(img2);

				Font font = new Font("Arial", Font.BOLD, 20);
				title = new JLabel(
						"Unit 5 Exam. All forms of cheating are not allowed. The teacher is always notified of your activity during"
								+ " this exam. Good Luck!");
				title.setFont(font);
				title.setForeground(new Color(0, 76, 153));
				add(title);
			}

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
		
		
		/**
		 * holds the first question of the exam
		 * 
		 *
		 */
		class Question1 extends JPanel
		{
			private Timer timer;
			private int seconds;
			private JLabel display;

			private JLabel question;

			public Question1()
			{
				setBackground(new Color(192, 222, 254));
				setPreferredSize(new Dimension(100, 100));
				Font font = new Font("Arial", Font.BOLD, 15);
				question = new JLabel("1. What is the value of x in the equation 3x + 4 = 16?");
				question.setFont(font);
				setLayout(new FlowLayout(FlowLayout.CENTER, 500, 20));
				question.setForeground(new Color(0, 76, 153));
				add(question);

				Q1Choices choices1 = new Q1Choices();
				add(choices1);
			}

		}

		class Q1Choices extends JPanel
		{
			private JRadioButton choice1, choice2, choice3, choice4;
			private ButtonGroup bg;

			public Q1Choices()
			{
				setPreferredSize(new Dimension(100, 200));

				setBackground(new Color(192, 222, 254));
				choice1 = new JRadioButton("b = 4");
				choice2 = new JRadioButton("b = 12");
				choice3 = new JRadioButton("b = 9");
				choice4 = new JRadioButton("b = 5");

				bg = new ButtonGroup();
				bg.add(choice1);
				bg.add(choice2);
				bg.add(choice3);
				bg.add(choice4);

				choice1.setBackground(new Color(221, 160, 221));
				choice2.setBackground(new Color(221, 160, 221));
				choice3.setBackground(new Color(221, 160, 221));
				choice4.setBackground(new Color(221, 160, 221));
				add(choice1);
				add(choice2);
				add(choice3);
				add(choice4);
			}
		}
		
		/** holds the second question of the exam**/
		class Question2 extends JPanel
		{

			private JLabel question;

			public Question2()
			{
				setBackground(new Color(192, 222, 254));

				Font font = new Font("Arial", Font.BOLD, 15);
				question = new JLabel("2. Explain the pythagorean theorem. ");
				question.setFont(font);

				setLayout(new FlowLayout(FlowLayout.CENTER, 500, 20));

				question.setForeground(new Color(0, 76, 153));
				add(question);

				Q2Choices q2 = new Q2Choices();
				add(q2);

			}

		}
		
		
		class Q2Choices extends JPanel
		{

			public Q2Choices()
			{

				setBackground(new Color(192, 222, 254));
				answer = new JTextArea(""); // answer is the text area where the user enters his answers
				answer.setPreferredSize(new Dimension(400, 100));
				answer.setLineWrap(true);
				answer.setBackground(new Color(221, 160, 221));
				answer.setForeground(Color.BLACK);
				answer.setTransferHandler(null);
				add(answer);

			}

		}
		
		/** this class is the panel which displays the amount of time the user has left **/
		class TimerPanel extends JPanel implements ActionListener
		{

			private int seconds;
			private Timer timer;
			public TimerPanel()
			{

				setBackground(new Color(0, 0, 0));
				Font font = new Font("Arial", Font.BOLD, 22);
				setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));

				Font font1 = new Font("Arial", Font.PLAIN, 20);
				timer = new Timer(1000, this);
				seconds = 55;

				display = new JLabel("");
				display.setFont(font1);
				display.setForeground(new Color(205, 255, 229));
				timer.start();

			}
			
			// runs the timer and displays it on screen
			public void actionPerformed(ActionEvent e)
			{
				add(display);
				if (seconds > 0)
				{
					seconds--;

					display.setText(
						"The window will close if the time hits zero. Make sure to finish within time. Time left: (in seconds): "
						+ seconds);
				}
				else
				{

					display.setText("time is up");
					timer.stop();
					ep.setVisible(false);
					c.show(aph, "start");
				}

			}

		}
		// ends
	}

}
