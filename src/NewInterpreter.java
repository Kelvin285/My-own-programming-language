import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

import javax.swing.JFrame;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.image.BufferedImage;


public class Interpreter {
	
	public static final int MAX_ARRAY = 4096;
	public static final int MAX_X = 1002;
	String file = "";
	
	
	
	public static final int END = 0x99;
	
	public static final int PNT = 0x01;
	public static final int INT = 0x10;
	public static final int INTAR = 0x11; //unused
	public static final int MOD = 0x100;
	public static final int LABEL = 0x101;
	public static final int JMP = 0x110;
	public static final int NADD = 0x111;
	public static final int NSUB = 0x1000;
	public static final int NMUL = 0x1001;
	public static final int NDIV = 0x1010;
	
	public static final int VADD = 0x1011;
	public static final int VSUB = 0x1100;
	public static final int VMUL = 0x1101;
	public static final int VDIV = 0x1110;
	
	public static final int NLESS = 0x1111;
	public static final int NGREATER = 0x10000;
	public static final int NEQUAL = 0x10001;
	public static final int NDIFF = 0x10010;
	
	public static final int VLESS = 0x10011;
	public static final int VGREATER = 0x10100;
	public static final int VEQUAL = 0x10101;
	public static final int VDIFF = 0x10110;
	public static final int VPNT = 0x10111;
	public static final int NPNT = 0x11000;
		
	public static final int NINDEX = 0x11001;
	public static final int VINDEX = 0x11010;
	
	public static final int SPEED = 0x11011;
	public static final int WINDOW = 0x11100;
	public static final int KEYBOARD = 0x11101;
	public static final int MOUSEX = 0x11110;
	public static final int MOUSEY = 0x11111;
	public static final int MOUSEBTN = 0x100000;
	public static final int WAIT = 0x100001;
	public static final int FILL = 0x100010;
	public static final int COLOR = 0x100011;
	public static final int NSET = 0x100100;
	public static final int VSET = 0x100101;
	
	public static final int SQRT = 0x100110;
	public static final int NPOW = 0x100111;
	public static final int VPOW = 0x101000;
	public static final int ROUND = 0x101001;
	public static final int SINE = 0x101010;
	public static final int COSINE = 0x101001;
	public static final int TAN = 0x101011;
	public static final int PI = 0x101100;
	public static final int RAD = 0x101101;
	public static final int DEG = 0x101110;
	public static final int ACOSINE = 0x101111;
	public static final int ASINE = 0x110000;
	public static final int ATAN = 0x110000;
	public static final int ABS = 0x110001;
   public static final int DRAW = 0x110010;
   public static final int HSET = 0x110011;
   public static final int NDRAW = 0x110100;
	
	public static boolean DEBUG = true;
	
	public static void main(String[] args) {
		if (args.length == 0) {
			args = new String[]{"test/TestCode"};
		}
		new Interpreter(args[0]);
	}
	
	public Interpreter(String f) {
		file = "";
		try {
			Scanner scanner = new Scanner(new File(f));
			while (scanner.hasNextLine()) {
				file += scanner.nextLine() + "\n";
			}
			scanner.close();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		file = file.replace("\t", "");
		String str = "";
		String[] str1 = file.split("\n");
		for (String s : str1) {
			char[] c = s.toCharArray();
			for (int i = 0; i < c.length; i++) {
				if (i + 1 < c.length - 1) {
					if (c[i] == '/' && c[i + 1] == '/')
						break;
				}
				str += c[i];
			}
			str += "\n";
		}
		
		String[] data = str.split("\n");
		
		HashMap<String, Integer> labels = new HashMap<String, Integer>();
		HashMap<String, Integer> ints = new HashMap<String, Integer>();

		
		for (int i = 0; i < data.length; i++) {
			if (data[i].equals(""))
				continue;
			String[] words = data[i].split(" ");
			if (words.length == 0)
				continue;
			if (words[0].startsWith(":")) {
				setMemoryLocation2(i, 0, LABEL);
				labels.put(words[0].replace(":", ""), i);
			}
		}
		
		for (int i = 0; i < data.length; i++) {
			if (data[i].equals(""))
				continue;
			String[] words = data[i].split(" ");
			if (words.length == 0)
				continue;
			
			if (words[0].equals("pnt") && words.length > 1) {
				setMemoryLocation2(i, 0, PNT);
				
				int j = 1;
				for (int a = 1; a < words.length; a++) {
					String s = words[a];

					for (char c : s.toCharArray()) {
						setMemoryLocation2(i, j, (int)c);
						j++;
					}
					setMemoryLocation2(i, j, (int)' ');
					j++;
				}
			}
			
			if (words[0].equals("jmp") && words.length > 1) {
				setMemoryLocation2(i, 0, JMP);
				setMemoryLocation2(i, 1, labels.get(words[1]));
			}
			if (words[0].equals("decimal") && words.length > 1) {
				
				setMemoryLocation2(i, 0, INT);
				ints.put(words[1], i);
				if (words.length > 2) {
					for (int a = 2; a < words.length; a++)
					setMemoryLocation2(i, a - 1, Float.parseFloat(words[a]));
				} else {
					setMemoryLocation2(i, 1, 0);
				}
				
			}
         
         if (words[0].equals("x16") && words.length > 1) {
				
				setMemoryLocation2(i, 0, INT);
				ints.put(words[1], i);
				if (words.length > 2) {
					for (int a = 2; a < words.length; a++) {
					   setMemoryLocation2(i, a - 1, (float)Integer.decode(words[a]));
                  System.out.println("int: " + Integer.decode(words[a]) + ", float: " + (float)Integer.decode(words[a]));
               }
				} else {
					setMemoryLocation2(i, 1, 0);
				}
				
			}

			
			if (words[0].equals("mod") && words.length > 1) {
				
				int location = ints.get(words[1]);
				setMemoryLocation2(i, 0, MOD);
				setMemoryLocation2(i, 1, location);
				
				if (words.length == 2) {
					setMemoryLocation2(i, 2, 1);
				} else {
					setMemoryLocation2(i, 2, 1 + Float.parseFloat(words[2]));
				}
			}
			
			if (words[0].equals("nadd") && words.length > 1) {
				setMemoryLocation2(i, 0, NADD);
				setMemoryLocation2(i, 1, Float.parseFloat(words[1]));
			}
			
			if (words[0].equals("nsub") && words.length > 1) {
				setMemoryLocation2(i, 0, NSUB);
				setMemoryLocation2(i, 1, Float.parseFloat(words[1]));
			}
			
			if (words[0].equals("nmul") && words.length > 1) {
				setMemoryLocation2(i, 0, NMUL);
				setMemoryLocation2(i, 1, Float.parseFloat(words[1]));
			}
			
			if (words[0].equals("ndiv") && words.length > 1) {
				setMemoryLocation2(i, 0, NDIV);
				setMemoryLocation2(i, 1, Float.parseFloat(words[1]));
			}
			
			if (words[0].equals("nless") && words.length > 1) {
				setMemoryLocation2(i, 0, NLESS);
				setMemoryLocation2(i, 1, Float.parseFloat(words[1]));
			}
			
			if (words[0].equals("ngreater") && words.length > 1) {
				setMemoryLocation2(i, 0, NGREATER);
				setMemoryLocation2(i, 1, Float.parseFloat(words[1]));
			}
			
			if (words[0].equals("nequal") && words.length > 1) {
				setMemoryLocation2(i, 0, NEQUAL);
				setMemoryLocation2(i, 1, Float.parseFloat(words[1]));
			}
			
			if (words[0].equals("vadd") && words.length > 1) {
				setMemoryLocation2(i, 0, VADD);
				setMemoryLocation2(i, 1, ints.get(words[1]));
				if (words.length > 2) {
					setMemoryLocation2(i, 2, Integer.parseInt(words[2]));
				}
			}
			
			if (words[0].equals("vsub") && words.length > 1) {
				setMemoryLocation2(i, 0, VSUB);
				setMemoryLocation2(i, 1, ints.get(words[1]));
				if (words.length > 2) {
					setMemoryLocation2(i, 2, Integer.parseInt(words[2]));
				}
			}
			
			if (words[0].equals("vmul") && words.length > 1) {
				setMemoryLocation2(i, 0, VMUL);
				setMemoryLocation2(i, 1, ints.get(words[1]));
				if (words.length > 2) {
					setMemoryLocation2(i, 2, Integer.parseInt(words[2]));
				}
			}
			
			if (words[0].equals("vdiv") && words.length > 1) {
				setMemoryLocation2(i, 0, VDIV);
				setMemoryLocation2(i, 1, ints.get(words[1]));
				if (words.length > 2) {
					setMemoryLocation2(i, 2, Integer.parseInt(words[2]));
				}
			}
			
			if (words[0].equals("vless") && words.length > 1) {
				setMemoryLocation2(i, 0, VLESS);
				setMemoryLocation2(i, 1, ints.get(words[1]));
				if (words.length > 2) {
					setMemoryLocation2(i, 2, Integer.parseInt(words[2]));
				}
			}
			
			if (words[0].equals("vgreater") && words.length > 1) {
				setMemoryLocation2(i, 0, VGREATER);
				setMemoryLocation2(i, 1, ints.get(words[1]));
				if (words.length > 2) {
					setMemoryLocation2(i, 2, Integer.parseInt(words[2]));
				}
			}
			
			if (words[0].equals("vequal") && words.length > 1) {
				setMemoryLocation2(i, 0, VEQUAL);
				setMemoryLocation2(i, 1, ints.get(words[1]));
				if (words.length > 2) {
					setMemoryLocation2(i, 2, Integer.parseInt(words[2]));
				}
			}
			
			if (words[0].equals("vdiff") && words.length > 1) {
				setMemoryLocation2(i, 0, VDIFF);
				setMemoryLocation2(i, 1, ints.get(words[1]));
				if (words.length > 2) {
					setMemoryLocation2(i, 2, Integer.parseInt(words[2]));
				}
			}
			
			if (words[0].equals("vpnt")) {
				setMemoryLocation2(i, 0, VPNT);
			}
			
			if (words[0].equals("nindex") && words.length > 1) {
				setMemoryLocation2(i, 0, NINDEX);
				setMemoryLocation2(i, 1, Float.parseFloat(words[1]));
			}
			
			if (words[0].equals("vindex") && words.length > 1) {
				setMemoryLocation2(i, 0, NINDEX);
				setMemoryLocation2(i, 1, ints.get(words[1]));
				if (words.length > 2) {
					setMemoryLocation2(i, 2, Integer.parseInt(words[2]));
				}
			}
			if (words[0].equals("end")) {
				setMemoryLocation(i, 0, END);
			}
			if (words[0].equals("speed") && words.length > 1) {
				setMemoryLocation(i, 0, SPEED);
				setMemoryLocation(i, 1, Float.parseFloat(words[1]));
			}
			
			if (words[0].equals("nset") && words.length > 1) {
				setMemoryLocation2(i, 0, NSET);
				setMemoryLocation2(i, 1, Float.parseFloat(words[1]));
			}
         
         if (words[0].equals("hset") && words.length > 1) {
				setMemoryLocation2(i, 0, HSET);
				setMemoryLocation2(i, 1, Integer.decode(words[1]));
			}
			
			if (words[0].equals("vset") && words.length > 1) {
				setMemoryLocation2(i, 0, VSET);
				setMemoryLocation2(i, 1, ints.get(words[1]));
				if (words.length > 2) {
					setMemoryLocation2(i, 2, Integer.parseInt(words[2]));
				}
			}
			
			if (words[0].equals("sqrt")) {
				setMemoryLocation2(i, 0, SQRT);
			}
			
			if (words[0].equals("round")) {
				setMemoryLocation2(i, 0, ROUND);
			}
			
			if (words[0].equals("sine") || words[0].equals("sin")) {
				setMemoryLocation2(i, 0, SINE);
			}
			
			if (words[0].equals("cosine") || words[0].equals("cos")) {
				setMemoryLocation2(i, 0, COSINE);
			}
			
			if (words[0].equals("tangent") || words[0].equals("tan")) {
				setMemoryLocation2(i, 0, TAN);
			}
			
			if (words[0].equals("asine") || words[0].equals("asin")) {
				setMemoryLocation2(i, 0, ASINE);
			}
			
			if (words[0].equals("acosine") || words[0].equals("acos")) {
				setMemoryLocation2(i, 0, ACOSINE);
			}
			
			if (words[0].equals("atangent") || words[0].equals("atan")) {
				setMemoryLocation2(i, 0, ATAN);
			}
			
			if (words[0].equals("pi")) {
				setMemoryLocation2(i, 0, PI);
			}
			
			if (words[0].equals("rad")) {
				setMemoryLocation2(i, 0, RAD);
			}
			
			if (words[0].equals("deg")) {
				setMemoryLocation2(i, 0, DEG);
			}
			
			if (words[0].equals("abs")) {
				setMemoryLocation2(i, 0, ABS);
			}
			
			if (words[0].equals("npow") && words.length > 1) {
				setMemoryLocation2(i, 0, NPOW);
				setMemoryLocation2(i, 1, Float.parseFloat(words[1]));
			}
			
			if (words[0].equals("vpow") && words.length > 1) {
				setMemoryLocation2(i, 0, VPOW);
				setMemoryLocation2(i, 1, ints.get(words[1]));
				if (words.length > 2) {
					setMemoryLocation2(i, 2, Integer.parseInt(words[2]));
				}
			}
			
			if (words[0].equals("window") && words.length > 2) {
				setMemoryLocation(i, 0, WINDOW);
				setMemoryLocation(i, 1, (int)Float.parseFloat(words[1]));
				setMemoryLocation(i, 2, (int)Float.parseFloat(words[2]));
				
				if (words.length > 3) {
					int j = 3;
					for (int a = 4; a < words.length; a++) {
						String s = words[a];

						for (char c : s.toCharArray()) {
							setMemoryLocation2(i, j, (int)c);
							j++;
						}
						setMemoryLocation2(i, j, (int)' ');
						j++;
					}
				}
			}
         
         if (words[0].equals("draw") && words.length > 3) {
            setMemoryLocation(i, 0, DRAW);
            setMemoryLocation(i, 1, ints.get(words[1]));
            setMemoryLocation(i, 2, ints.get(words[2]));
            setMemoryLocation(i, 3, Integer.decode(words[3]));
         }
         
          if (words[0].equals("ndraw") && words.length > 3) {
            setMemoryLocation(i, 0, NDRAW);
            setMemoryLocation(i, 1, ints.get(words[1]));
            setMemoryLocation(i, 2, ints.get(words[2]));
            setMemoryLocation(i, 3, ints.get(words[3]));
           }
			
		}
		
		
		
		setMemoryLocation2(data.length, 0, END);
		setMemoryLocation2(data.length + 1, 0, END);
		
		new Thread() {
			public void run() {
				while (stop[0] == 0) {
					javaside();
					try {
						Thread.sleep(1);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}.start();
		
		
		
		
		while (stop[0] == 0) {
			run();
		}
		
		javaside();
	}
	
	public JFrame frame;
   public BufferedImage image;
	
	public void javaside() {
		
		if (window[0] == 1) {
         window[0] = 0;
			String s = "";
			for (int i = 1; i < windowBuffer.length; i++) {
				s += (char)windowBuffer[i];
				windowBuffer[i] = 0;
			}
			s = s.trim();
			
			frame = new JFrame(s);
			frame.setSize(window[1], window[2]);
			frame.setLocationRelativeTo(null);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setVisible(true);
         
         image = (BufferedImage)frame.createImage(500, 500);
		}
		
		if (printBuffer[0] == 1) {
			printBuffer[0] = 0;
			String s = "";
			for (int i = 1; i < printBuffer.length; i++) {
				s += (char)printBuffer[i];
				printBuffer[i] = 0;
			}
			s = s.trim();
			System.out.println(s);
			
		}
		if (printBuffer[0] == 2) {
			printBuffer[0] = 0;
			System.out.println(printBuffer[1]);
		}
		if (printBuffer[0] == 3) {
			printBuffer[0] = 0;
			System.out.println(getMemoryLocation(modify[0], modify[1]));
		}
      
      if (image == null)
      return;
      
      Graphics g = image.getGraphics();
      
      for (int x = 0; x < 500; x++) {
         for (int y = 0; y < 500; y++) {
            image.setRGB(x, y, drawBuffer[x + y * 500]);
         }
      }
      
      g = frame.getGraphics();
      g.drawImage(image, 0, 0, frame.getWidth(), frame.getHeight(), null);

	}
	 
	
	 
	public final int[] line = {0};
	 
	public final int[] line2 = {0};
	
	public float[] programMemory = new float[MAX_ARRAY * MAX_X];
	public int[] printBuffer = new int[MAX_X];
	public int[] modify = {MAX_ARRAY - 1, MAX_X - 1};
	public final int[] stop = {0};
	public int[] speed = {1};
	public int[] window = {0, 0, 0};
	public int[] windowBuffer = new int[MAX_X];
   public int[] drawBuffer = new int[500*500];
	 
	public final void setMemoryLocation2(int x, int y, float data) {
		programMemory[x + y * MAX_ARRAY] = data;
		if (DEBUG == true) {
			if (y == 0) {
				System.out.print("\n"+data);
			} else {
				System.out.print(" " + data);
			}
		}
	}
	
	public final void setMemoryLocation(int x, int y, float data) {
		programMemory[x + y * MAX_ARRAY] = data;
//		if (DEBUG == true) {
//			if (y == 0) {
//				System.out.print("\n"+data);
//			} else {
//				System.out.print(" " + data);
//			}
//		}
	}
	
	public final float getMemoryLocation(int x, int y) {
		return programMemory[x + y * MAX_ARRAY];
	}
	 
	public final void parseVPrint(int x, int i) {
		printBuffer[i] = (int)getMemoryLocation(modify[0], modify[1]);
	}
	
	public final void parseNPrint(int x, int i) {
		printBuffer[i] = (int)getMemoryLocation(x, i);
	}
	
	public final void parsePrint(int x, int i) {
		printBuffer[i] = (int)getMemoryLocation(x, i);
	}
	 
	public final void parse() {
		int read = 0, pnt = 1, end = 2, jmp = 3, npnt = 4, vpnt = 5, exit = 6;
		int state = 0;
		
//		for (int i = 0; i < MAX_X; i++) {
			if (state == read) {
//				if (0 == 0) {
					if (getMemoryLocation(line[0], 0) == SPEED) {
						speed[0] = (int)getMemoryLocation(line[0], 1);
						state = exit;
					}
					if (getMemoryLocation(line[0], 0) == PNT) {
						printBuffer[0] = 1;
						state = pnt;
					}
					if (getMemoryLocation(line[0], 0) == VPNT) {
						printBuffer[0] = 3;
						state = vpnt;
					}
					if (getMemoryLocation(line[0], 0) == END) {
						stop[0] = 1;
						state = end;
					}
					if (getMemoryLocation(line[0], 0) == JMP) {
						line[0] = (int)getMemoryLocation(line[0], 1);
						state = jmp;
					}
					if (getMemoryLocation(line[0], 0) == MOD) {
						modify[0] = (int)getMemoryLocation(line[0], 1);
						modify[1] = (int)getMemoryLocation(line[0], 2);
						state = exit;
					}
					if (getMemoryLocation(line[0], 0) == NADD) {
						setMemoryLocation(modify[0], modify[1], getMemoryLocation(modify[0], modify[1]) + getMemoryLocation(line[0], 1));
						state = exit;
					}
					if (getMemoryLocation(line[0], 0) == NSUB) {
						setMemoryLocation(modify[0], modify[1], getMemoryLocation(modify[0], modify[1]) - getMemoryLocation(line[0], 1));
						state = exit;
					}
					if (getMemoryLocation(line[0], 0) == NMUL) {
						setMemoryLocation(modify[0], modify[1], getMemoryLocation(modify[0], modify[1]) * getMemoryLocation(line[0], 1));
						state = exit;
					}
					if (getMemoryLocation(line[0], 0) == NDIV) {
						setMemoryLocation(modify[0], modify[1], getMemoryLocation(modify[0], modify[1]) / getMemoryLocation(line[0], 1));
						state = exit;
					}
					
					if (getMemoryLocation(line[0], 0) == VADD) {
						setMemoryLocation(modify[0], modify[1], getMemoryLocation(modify[0], modify[1]) + getMemoryLocation((int)getMemoryLocation(line[0], 1), (int)getMemoryLocation(line[0], 2)));
						state = exit;
					}
					if (getMemoryLocation(line[0], 0) == VSUB) {
						setMemoryLocation(modify[0], modify[1], getMemoryLocation(modify[0], modify[1]) - getMemoryLocation((int)getMemoryLocation(line[0], 1), (int)getMemoryLocation(line[0], 2)));
						state = exit;
					}
					if (getMemoryLocation(line[0], 0) == VMUL) {
						setMemoryLocation(modify[0], modify[1], getMemoryLocation(modify[0], modify[1]) * getMemoryLocation((int)getMemoryLocation(line[0], 1), (int)getMemoryLocation(line[0], 2)));
						state = exit;
					}
					if (getMemoryLocation(line[0], 0) == VDIV) {
						setMemoryLocation(modify[0], modify[1], getMemoryLocation(modify[0], modify[1]) / getMemoryLocation((int)getMemoryLocation(line[0], 1), (int)getMemoryLocation(line[0], 2)));
						state = exit;
					}
					
					if (getMemoryLocation(line[0], 0) == NINDEX) {
						modify[1] = (int)getMemoryLocation(line[0], 1);
						state = exit;
					}
					if (getMemoryLocation(line[0], 0) == VINDEX) {
						modify[1] = (int)getMemoryLocation((int)getMemoryLocation(line[0], 1), (int)getMemoryLocation(line[0], 2));
						state = exit;
					}
					
					if (getMemoryLocation(line[0], 0) == NLESS) {
						if (getMemoryLocation(modify[0], modify[1]) >= getMemoryLocation(line[0], 1))
							line[0]++;
						state = exit;
					}
					if (getMemoryLocation(line[0], 0) == NGREATER) {
						if (getMemoryLocation(modify[0], modify[1]) <= getMemoryLocation(line[0], 1))
							line[0]++;
						state = exit;
					}
					if (getMemoryLocation(line[0], 0) == NEQUAL) {
						if (getMemoryLocation(modify[0], modify[1]) != getMemoryLocation(line[0], 1))
							line[0]++;
						state = exit;
					}
					if (getMemoryLocation(line[0], 0) == NDIFF) {
						if (getMemoryLocation(modify[0], modify[1]) == getMemoryLocation(line[0], 1))
							line[0]++;
						state = exit;
					}
					
					if (getMemoryLocation(line[0], 0) == VLESS) {
						if (getMemoryLocation(modify[0], modify[1]) >= getMemoryLocation((int)getMemoryLocation(line[0], 1), (int)getMemoryLocation(line[0], 2)))
							line[0]++;
						state = exit;
					}
					if (getMemoryLocation(line[0], 0) == VGREATER) {
						if (getMemoryLocation(modify[0], modify[1]) <= getMemoryLocation((int)getMemoryLocation(line[0], 1), (int)getMemoryLocation(line[0], 2)))
							line[0]++;
						state = exit;
					}
					if (getMemoryLocation(line[0], 0) == VEQUAL) {
						if (getMemoryLocation(modify[0], modify[1]) != getMemoryLocation((int)getMemoryLocation(line[0], 1), (int)getMemoryLocation(line[0], 2)))
							line[0]++;
						state = exit;
					}
					if (getMemoryLocation(line[0], 0) == VDIFF) {
						if (getMemoryLocation(modify[0], modify[1]) == getMemoryLocation((int)getMemoryLocation(line[0], 1), (int)getMemoryLocation(line[0], 2)))
							line[0]++;
						state = exit;
					}
					if (getMemoryLocation(line[0], 0) == NSET) {
						setMemoryLocation(modify[0], modify[1], (int)getMemoryLocation(line[0], 1)); 
						state = exit;
					}
					if (getMemoryLocation(line[0], 0) == VSET) {
						setMemoryLocation(modify[0], modify[1], getMemoryLocation((int)getMemoryLocation(line[0], 1), (int)getMemoryLocation(line[0], 2)));
						state = exit;
					}
               
               if (getMemoryLocation(line[0], 0) == HSET) {
                  setMemoryLocation(modify[0], modify[1], (int)getMemoryLocation(line[0], 1)); 
						state = exit;
               }
					
					if (getMemoryLocation(line[0], 0) == NPOW) {
						setMemoryLocation(modify[0], modify[1], (float)Math.pow(getMemoryLocation(modify[0], modify[1]), getMemoryLocation(line[0], 1))); 
						state = exit;
					}
					
					if (getMemoryLocation(line[0], 0) == VPOW) {
						setMemoryLocation(modify[0], modify[1], (float)Math.pow(getMemoryLocation(modify[0], modify[1]), getMemoryLocation((int)getMemoryLocation(line[0], 1), (int)getMemoryLocation(line[0], 2)))); 
						state = exit;
					}
					
					if (getMemoryLocation(line[0], 0) == SQRT) {
						setMemoryLocation(modify[0], modify[1], (float)Math.sqrt(getMemoryLocation(modify[0], modify[1]))); 
						state = exit;
					}
					
					if (getMemoryLocation(line[0], 0) == ROUND) {
						setMemoryLocation(modify[0], modify[1], (int)(getMemoryLocation(modify[0], modify[1]))); 
						state = exit;
					}
					
					if (getMemoryLocation(line[0], 0) == ABS) {
						setMemoryLocation(modify[0], modify[1], Math.abs(getMemoryLocation(modify[0], modify[1]))); 
						state = exit;
					}
					
					if (getMemoryLocation(line[0], 0) == SINE) {
						setMemoryLocation(modify[0], modify[1], (float)Math.sin(getMemoryLocation(modify[0], modify[1]))); 
						state = exit;
					}
					
					if (getMemoryLocation(line[0], 0) == COSINE) {
						setMemoryLocation(modify[0], modify[1], (float)Math.cos(getMemoryLocation(modify[0], modify[1]))); 
						state = exit;
					}
					
					if (getMemoryLocation(line[0], 0) == TAN) {
						setMemoryLocation(modify[0], modify[1], (float)Math.tan(getMemoryLocation(modify[0], modify[1]))); 
						state = exit;
					}
					
					if (getMemoryLocation(line[0], 0) == ASINE) {
						setMemoryLocation(modify[0], modify[1], (float)Math.asin(getMemoryLocation(modify[0], modify[1]))); 
						state = exit;
					}
					
					if (getMemoryLocation(line[0], 0) == ACOSINE) {
						setMemoryLocation(modify[0], modify[1], (float)Math.acos(getMemoryLocation(modify[0], modify[1]))); 
						state = exit;
					}
					
					if (getMemoryLocation(line[0], 0) == ATAN) {
						setMemoryLocation(modify[0], modify[1], (float)Math.atan(getMemoryLocation(modify[0], modify[1]))); 
						state = exit;
					}
					
					if (getMemoryLocation(line[0], 0) == RAD) {
						setMemoryLocation(modify[0], modify[1], (float)Math.toRadians(getMemoryLocation(modify[0], modify[1]))); 
						state = exit;
					}
					
					if (getMemoryLocation(line[0], 0) == DEG) {
						setMemoryLocation(modify[0], modify[1], (float)Math.toDegrees(getMemoryLocation(modify[0], modify[1]))); 
						state = exit;
					}
					
					if (getMemoryLocation(line[0], 0) == PI) {
						setMemoryLocation(modify[0], modify[1], (float)Math.PI); 
						state = exit;
					}
               
               if (getMemoryLocation(line[0], 0) == WINDOW) {
                  window[0] = 1;
                  window[1] = (int)getMemoryLocation(line[0], 1);
                  window[2] = (int)getMemoryLocation(line[0], 2);
                  for (int a = 3; a < MAX_X; a++) {
                     windowBuffer[a - 1] = (int)getMemoryLocation(line[0], a);
                  }
                  state = exit;
               }
               
               
               
               if (getMemoryLocation(line[0], 0) == DRAW) {
                  drawBuffer[(int)getMemoryLocation((int)getMemoryLocation(line[0], 1), 1) + (int)getMemoryLocation((int)getMemoryLocation(line[0], 2), 1) * 500] = (int)getMemoryLocation(line[0], 3);
                  state = exit;
               }
               
               if (getMemoryLocation(line[0], 0) == NDRAW) {
                  drawBuffer[(int)getMemoryLocation((int)getMemoryLocation(line[0], 1), 1) + (int)getMemoryLocation((int)getMemoryLocation(line[0], 2), 1) * 500] = (int)getMemoryLocation((int)getMemoryLocation(line[0], 3), 1);
                  state = exit;
               }
//				}
			}
			
			if (state == pnt) {
				for (int i = 1; i < MAX_X; i++)
				parsePrint(line[0], i);
			}else
			if (state == npnt) {
				for (int i = 1; i < MAX_X; i++)
				parseNPrint(line[0], i);
				state = exit;
			}else
			if (state == vpnt) {
				for (int i = 1; i < MAX_X; i++)
				parseVPrint(line[0], i);
				state = exit;
			}
			
//		}
		line[0]++;
		
	}
	
	
	
	public void run() {
		if (speed[0] <= 0)
		{
			stop[0] = 1;
			return;
		}
		for (int i = 0; i < speed[0]; i++)
			parse();
		
		
	}
	
	

}
