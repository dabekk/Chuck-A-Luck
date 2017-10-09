import java.util.Scanner;
public class ChuckALuck {
	public static final String BET1 = "Triple";
	public static final String BET2 = "Field";
	public static final String BET3 = "High";
	public static final String BET4 = "Low";
	public final static int SIDESONDICE = 6;
	public static void main(String[] args) {
		Wallet wallet = new Wallet();
		createCash(wallet);
		placeBet(wallet);
	}
	public static void createCash(Wallet wallet)		//method loadges money onto account
	{
		Scanner scan = new Scanner(System.in);
		System.out.print("How much money would you like to lodge(in euro): ");
		wallet.put(scan.nextDouble());
	}
	public static void placeBet(Wallet wallet)
	{
		Scanner scan = new Scanner(System.in);
		double initialMoney = wallet.check();
		int bet;
		String betType;
		System.out.println("Betting Options \n"
				+ "Type of Bet:   			Condition:			Payout (Odds): \n"
				+ "(1)Triple	All 3 dice show same number (but not 1s or 6s).		30:1 \n"
				+ "(2)Field	Total of 3 dice < 8 or total is > 12.			1:1 \n"
				+ "(3)High		Total of 3 dice > 10 (but not a high Triple).		1:1 \n"
				+ "(4)Low		Total of 3 dice < 11 (but not a low Triple).		1:1");
		do	//loop to keep allowing user to bet until they enter 0
		{
			do{ //loop to ensure user inputs a valid bet type
				System.out.print("Which bet would you like to take(1-4, 0 to quit): ");
				bet = scan.nextInt();
				switch(bet)					//switch statement selects chosen bet to pass into resolveBet()
				{
				case 1:
					betType = BET1;
					break;
				case 2:
					betType = BET2;
					break;
				case 3:
					betType = BET3;
					break;
				case 4:
					betType = BET4;
					break;
				default:
					betType = "invalid";
					break;
				}
				if(betType.equals("invalid") && bet != 0)
				{
					System.out.println("Your bet type was invalid, please enter another bet or 0 to quit.");
				}
			}while(betType.equals("invalid") && bet != 0);
			if(bet != 0 && wallet.check() > 0){
				resolveBet(betType, wallet);
			}
			else
			{
				if(wallet.check() <= 0 && bet != 0)
				{
					System.out.println("You do not have sufficient funds to make a bet.");
				}
			}
		}while(bet != 0);
		double currentWallet = wallet.check();
		summary(initialMoney, currentWallet);
		scan.close();
	}
	//method to display summary
	public static void summary(double initialWallet, double currentWallet)
	{
		System.out.println("Cash in the beginning: " + initialWallet + "\n"
				+ "Cash currently: " + currentWallet);
	}

	public static void resolveBet(String betType, Wallet wallet)
	{
		Scanner scan = new Scanner(System.in);
		double betSize;
		do
		{
			System.out.println("Your funds: " + wallet.check() +"\n"
					+ "How much would you like to place on this bet: ");
			betSize = scan.nextDouble();
			if(wallet.check() < betSize)			//ensures we have sufficient funds to play
			{
				System.out.println("You do not have sufficient funds to make this bet, please enter another bet size.");
			}
		}while(wallet.check() >= betSize && betSize < 0);
		
		//rolling dice
		Dice dice1 = new Dice(SIDESONDICE);
		Dice dice2 = new Dice(SIDESONDICE);
		Dice dice3 = new Dice(SIDESONDICE);
		dice1.roll();
		dice3.roll();
		dice2.roll();
		System.out.println("Dice: " + dice1.topFace() + " " + dice2.topFace() + " " + dice3.topFace());
		if(betType.equals(BET1))
		{
			if(dice1.topFace() == dice2.topFace() && dice2.topFace() == dice3.topFace() && dice1.topFace() != 1
					&& dice1.topFace() != 6)
			{
				wallet.put((betSize * 30));
				System.out.println("You win! Your new balance is: " + wallet.check());
			}
			else
			{
				wallet.get(betSize);
				System.out.println("You lose! Your new balance is: " + wallet.check());
			}
		}
		if(betType.equals(BET2))
		{
			if(dice1.topFace() + dice2.topFace() + dice3.topFace() > 12 || dice1.topFace() + dice2.topFace() + dice3.topFace() < 8)
			{
				wallet.put(betSize);
				System.out.println("You win! Your new balance is: " + wallet.check());
			}
			else
			{
				wallet.get(betSize);
				System.out.println("You lose! Your new balance is: " + wallet.check());
			}
		}
		if(betType.equals(BET3))
		{
			if(dice1.topFace() + dice2.topFace() + dice3.topFace() > 10 && 
					!((dice1.topFace() == dice2.topFace() && dice2.topFace() == dice3.topFace()) && dice1.topFace() >= 4))
			{
				wallet.put(betSize);
				System.out.println("You win! Your new balance is: " + wallet.check());
			}
			else
			{
				wallet.get(betSize);
				System.out.println("You lose! Your new balance is: " + wallet.check());
			}
		}
		if(betType.equals(BET4))
		{
			if(dice1.topFace() + dice2.topFace() + dice3.topFace() < 11 && 
					!((dice1.topFace() == dice2.topFace() && dice2.topFace() == dice3.topFace()) && dice1.topFace() <= 4))
			{
				wallet.put(betSize);
				System.out.println("You win! Your new balance is: " + wallet.check());
			}
			else
			{
				wallet.get(betSize);
				System.out.println("You lose! Your new balance is: " + wallet.check());
			}
		}
	}
}
