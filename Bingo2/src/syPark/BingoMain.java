package syPark;

import java.util.Scanner;

public class BingoMain {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("202212046 박서영");
		
		Scanner scanner = new Scanner(System.in);
		
		System.out.print("이름을 입력하시오 : ");
		
		String userName = scanner.nextLine();
		BingoGame bing = new BingoGame(userName);
		
		String[][] W1 = bing.makeVoc("voc.txt");
		String[][] W2 = bing.makeVoc("voc.txt");
		
		int Nsize = 0;
		while(true) {
			System.out.print("빙고판의 크기를 입력하시오(범위제한 3~7) : ");
			int N = scanner.nextInt();
			scanner.nextLine();
			Nsize = N;
			
			if(N >= 3 && N <= 7)
				break; 
		}
		// 빙고판 생성
		String[][] bingoTUser = bing.makeBingoT(userName, Nsize, W1);
		String[][] bingoTCom = bing.makeBingoT("Com", Nsize, W2);
		
		// 가중치판 생성
		int[][] wCom = new int[Nsize][Nsize];
		int[][] wComw = bing.wPut(Nsize, wCom);
		
		int[] winLose = new int[3];
		winLose = bing.getFile("winlose.txt");
		
		while(true) {
			System.out.print("영단어를 입력하시오 : ");
			String uWord = scanner.nextLine();
			boolean hanuWord = bing.boolWord(Nsize, bingoTUser, uWord);
			
			if(hanuWord == true) {
				String hWord1 = bing.transWord(uWord);
				System.out.println(uWord + "(" + hWord1 + ")를 선택하였습니다");
			}
			
			bing.checkWord(Nsize, bingoTUser, bingoTCom, uWord, wComw);
			
			bing.printArray(userName, Nsize, bingoTUser);
			bing.printArray("Com", Nsize, bingoTCom);
			
			String cWord = bing.chooseCom(Nsize, wComw, bingoTCom);
			System.out.println("컴퓨터가 영단어를 선택했습니다.");
			boolean hancWord = bing.boolWord(Nsize,  bingoTCom, cWord);
			
			if(hancWord == true) {
				String hWord2 = bing.transWord(cWord);
				System.out.println(cWord + "(" + hWord2 + ")를 선택하였습니다");
			}
			
			bing.checkWord(Nsize, bingoTUser, bingoTCom, cWord, wComw);
			
			bing.printArray(userName, Nsize, bingoTUser);
			bing.printArray("Com", Nsize, bingoTCom);
			
			int sumUser = bing.countBingo(Nsize, bingoTUser);
			int sumCom = bing.countBingo(Nsize, bingoTCom);
			
			int win = bing.sumWin(sumUser, sumCom);
			
			if(win == 0) {
				System.out.println(userName + " 승리!");
				winLose[0]++;
				bing.setFile("winlose.txt", winLose, userName);
				break;
			}
			else if(win == 1) {
				System.out.println("Com 승리!");
				winLose[1]++;
				bing.setFile("winlose.txt", winLose, userName);
				break;
			}
			
			if(bing.allopen(Nsize, bingoTUser, bingoTCom) == true) {
				System.out.println("무승부");
				winLose[2]++;
				bing.setFile("winlose.txt", winLose, userName);
				break;
			}
		}	
		
		scanner.close();
	}

}
