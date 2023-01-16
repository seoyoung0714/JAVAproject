package syPark;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Random;
import java.util.Scanner;

public class BingoGame {
	private String userName;
	private int vocsize;

	BingoGame(String userName){
		this.setUserName(userName);
	}
	public void	initWCom(int N) {
		
	}
	public void addWord(Word word) {
		
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {

		this.userName = userName;
	}
	public int[] getFile(String fileName) {
		int[] winLose = null;
		try {
			File file = new File(fileName);
			Scanner scanner = new Scanner(file);	
			String str = scanner.nextLine();
			String[] temp = str.split("\t");
			winLose = new int[3];
			
			for(int i = 0; i < temp.length; i++) {
				winLose[i] = Integer.valueOf(temp[i]);
			}
			scanner.close();
				
		} catch(FileNotFoundException e) {
			
		} 
		return winLose;
	}
	public void setFile(String fileName, int[] winlose, String userName) {
		try {
			File file = new File(fileName);
			FileWriter fw = new FileWriter(file,true);
			BufferedWriter writer = new BufferedWriter(fw);
			RandomAccessFile raf = new RandomAccessFile("winlose.txt", "rw");
			int sum = 0;
			raf.setLength(0);
			for(int i = 0; i < winlose.length; i++) {
				writer.write(Integer.toString(winlose[i]) + '\t');
				sum += winlose[i];
			}
			System.out.printf("%s" + "의 승률> " + "%.2f", userName, (winlose[0] /(float)sum * 100));
			System.out.println();
			System.out.printf("%s" + "의 승률> " + "%.2f", "Com", (winlose[1] /(float)sum * 100));
			System.out.println();
			writer.close();
			raf.close();
		} catch(FileNotFoundException e) {
			
		} catch(IOException e) {
			e.printStackTrace();
		}
	}	
	public String[][] makeVoc(String fileName) {
		int i = 0;
		int size = 0;
		String[][] word = new String[100][2];
		try {
			Scanner scanner = new Scanner(new File(fileName));
			while(scanner.hasNextLine()) {
				String str = scanner.nextLine();
				String[] temp = str.split("\t");
				this.addWord(new Word(temp[0].trim(), temp[1].trim()));
				word[i][0] = temp[0]; // 영어
				word[i][1] = temp[1]; // 한글
				i++;
				size++;
			}
			this.vocsize = size;
		} catch(FileNotFoundException e) {
			
		}
		return word;
	}
	public void checkWord(int N, String[][] bingoTUser, String[][] bingoTCom, String Word, int[][] wComw) {
		int kmax = 0;
		int lmax = 0;
		for(int i = 0; i < N; i++) {
			for(int j = 0; j < N; j++) {
				if(bingoTUser[i][j].equals(Word)) {
					bingoTUser[i][j] = "O";
					 kmax = j;
					 lmax = i;
				}
				if(bingoTCom[i][j].equals(Word)) {
					for(int k = 0; k < N; k++) {
						for(int l = 0; l < N; l++) {
							if(bingoTCom[k][l].equals("O"))
								break;
							wComw[k][kmax]++;
							wComw[lmax][l]++;
						}
					}
					if((kmax == lmax) || (kmax + lmax) == (N - 1)) {
						for(int k = 0; k < N; k++) {
							for(int l = 0; l < N; l++) {
								if(bingoTCom[k][l].equals("O"))
									break;
								if(kmax == lmax)
									wComw[k][k]++;
								if((kmax + lmax) == (N - 1))
									wComw[i][N - i - 1]++;
							}
						}
					}
					bingoTCom[i][j] = "O";
				}
			}
		}
	}
	public String[][] makeBingoT(String name, int N, String[][] W){
		String[][] bingoT = new String[N][N];
		
		if((N >= 3) && (N <= 7)) {
			Random r= new Random();
			int[] ra = new int[N * N];
			int t = 0;
			
			for(int k = 0; k < ra.length; k++) {
				ra[k] = r.nextInt(vocsize);
				for(int l = 0; l < k; l++) {
					if(ra[k] == ra[l]) {
						k--;
					}
				}
			}
			System.out.println("<" + name + ">");
			
			for(int i = 0; i < N; i++) {
				for(int j = 0; j < N; j++) {
						bingoT[i][j] = W[ra[t]][0];
						t++;
						System.out.printf("%20s", bingoT[i][j]);
						if(j == (N - 1)) {
							System.out.println();
							System.out.println();
						}
				}
			}
			System.out.println();
		}
		return bingoT;
	}
	public void printArray(String name, int N, String[][] bingoT) {
		System.out.println("<" + name + ">");
		
		for(int i = 0; i < N; i++) {
			for(int j = 0; j < N; j++) {
					System.out.printf("%20s", bingoT[i][j]);
					if(j == (N - 1)) {
						System.out.println();
						System.out.println();
					}
			}
		}
		System.out.println();
	}
	public String transWord(String Word) {
		String[][] w = makeVoc("voc.txt");
		int index = 0;
		
		for(int i = 0; i < vocsize; i++) {
			if(w[i][0].equals(Word)) {
				index = i;
				break;
			}
		}
		String hWord = w[index][1];
		
		return hWord;
	}
	public boolean boolWord(int N, String[][] bingoT, String Word) {
		for(int i = 0; i < N; i++) {
			for(int j = 0; j < N; j++) {
				if(bingoT[i][j].equals(Word)) {
					return true;
				}
			}
		}
		return false;
	}
	public int horiCount(int N, String[][] bingoT) {
		int countM = 0;
		for(int i = 0; i < N; i++) {
			int count = 0;
			for(int j = 0; j < N; j++) {
				if(bingoT[i][j].equals("O"))
						count++;
			}
			if(count == N) {
				countM++;
			}
		}
		return countM;
	}
	public int vertiCount(int N, String[][] bingoT) {
		int countM = 0;
		for(int i = 0; i < N; i++) {
			int count = 0;
			for(int j = 0; j < N; j++) {
				if(bingoT[j][i].equals("O"))
					count++;
			}
			if(count == N) {
				countM++;
			}
		}
		return countM;
	}
	public int diagCount(int N, String[][] bingoT) {
		int countM = 0;
		int count = 0;
		for(int i = 0; i < N; i++) {
			for(int j = 0; j < N; j++) {
				if((i == j) && (bingoT[i][j].equals("O"))) {
					count++;
				}
			}
			if(count == N) {
				countM++;
			}
		}
		count = 0;
		for(int i = 0; i < N; i++) {
			for(int j = 0; j < N; j++) {
				if(((i + j) == (N - 1)) && (bingoT[i][j].equals("O"))) {
					count++;
				}
			}
			if(count == N) {
				countM++;
			}
		}
		return countM;
	}
	public int countBingo(int N, String[][] bingoT) {
		int hori = horiCount(N, bingoT);
		int verti = vertiCount(N, bingoT);
		int diag = diagCount(N, bingoT);
		
		return hori + verti + diag;
	}
	public int sumWin(int sumUser, int sumCom) {
		if(sumUser > sumCom)
			return 0;
		else if(sumUser < sumCom)
			return 1;
		else
			return 2;
	}
	public boolean allopen(int N, String[][] bingoTUser, String[][] bingoTCom) {
		int u = 0;
		int c = 0;
		
		for(int i = 0; i < N; i++) {
			for(int j = 0; j < N; j++) {
				if(bingoTUser[i][j].equals("O")) {
					u++;
				}
			}
		}
		
		for(int i = 0; i < N; i++) {
			for(int j = 0; j < N; j++) {
				if(bingoTUser[i][j].equals("O")) {
					c++;
				}
			}
		}
		
		if((u == N * N) && (c == N * N)) {
			return true;
		}
		else if((u != N * N) && (c != N * N)) {
			return false;
		}
		else
			return false;
	}
	public int[][] wPut(int N, int[][] wPan) {
		for(int i = 0; i < N; i++) {
			wPan[i][i]++;
			wPan[i][N - i - 1]++;
		}
		return wPan;
	}
	public String chooseCom(int N, int[][] wComw, String[][] bingoTCom) {
		String cWord;
		int wMax = 0;
		int imax = 0;
		int jmax = 0;
		
		for(int i = 0; i < N; i++) {
			for(int j = 0; j < N; j++) {
				if(bingoTCom[i][j].equals("O"))
					break;
				if(wComw[i][j] > wMax) {
					wMax = wComw[i][j];
					imax = i;
					jmax = j;
				}
			}
		}
		for(int i = 0; i < N; i++) {
			for(int j = 0; j < N; j++) {
				if(bingoTCom[i][j].equals("O"))
					break;
				wComw[i][jmax]++;
				wComw[imax][j]++;
			}
		}
		if((imax == jmax) || (imax + jmax) == (N - 1)) {
			for(int i = 0; i < N; i++) {
				for(int j = 0; j < N; j++) {
					if(bingoTCom[i][j].equals("O"))
						break;
					if(imax == jmax)
						wComw[i][i]++;
					if((imax + jmax) == (N - 1))
						wComw[i][N - i - 1]++;
				}
			}
		}
		wComw[imax][jmax] = 0;
		
		cWord = bingoTCom[imax][jmax];
		
		return cWord;
	}
}
