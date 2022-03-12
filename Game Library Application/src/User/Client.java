package User;

import java.util.ArrayList;

public class Client {
	protected float balance;
	protected ArrayList<Game> library = new ArrayList<Game>();

	public Client(float balance) {
		this.balance = balance;
	}

	public void buy_game(Game game) {
		boolean contains = false;
		for (int i = 0; i < library.size(); i++) {
			if (game.name.equals(library.get(i).name)) {
				contains = true;
			}
		}

		if (contains == false) {
			if (this.balance >= game.price) {
				library.add(game);
				this.balance = this.balance - game.price;
			} else {
				System.out.println("ERROR");
			}
		} else {
			System.out.println("ERROR");
		}

	}

	public void refund_game(Game game) {
		boolean contains = false;
		int gameIndex = -1;
		for (int i = 0; i < library.size(); i++) {
			if (game.name.equals(library.get(i).name)) {
				contains = true;
				gameIndex = i;
			}
		}
		if (contains == true) {
			library.remove(gameIndex);
			this.balance = this.balance + game.price;
		}
	}

	public void display_library() {
		System.out.print("Library: [");
		for (int i = 0; i < library.size(); i++) {
			if (i < library.size() - 1) {
				System.out.print(library.get(i).name + ",");
			} else {
				System.out.println(library.get(i).name + "]");
			}

		}
	}

	public float getBalance() {
		return this.balance;
	}

	public void setBalance(float balance) {
		this.balance = balance;
	}

}
