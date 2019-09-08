package games.maze;

import java.util.ArrayList;
import java.util.Random;

import games.maze.cases.FreeCase;
import games.maze.cases.WallCase;

public class LabyGenerator {

	private Case[][] lab;
	private int n;
	private int m;
	private Board board;

	Random r = new Random();

	public LabyGenerator(Board board, int n, int m) {
		this.board = board;
		if (n % 2 == 0) {
			this.n = n;
		} else {
			this.n = n + 1;
		}
		if (m % 2 == 0) {
			this.m = m;
		} else {
			this.m = m + 1;
		}
		this.lab = new Case[n][m];
		creerLabMur();
		genLab();
	}

	public int getNbCol() {
		return this.m;
	}

	public int getNbLig() {
		return this.n;
	}

	public Case[][] getLab() {
		return this.lab;
	}

	public void creerLabMur() {
		for (int i = 0; i < this.n; i++) {
			Case[] lig = new Case[m];
			for (int j = 0; j < this.m; j++) {
				WallCase c = new WallCase(i, j, board.getSize(), false);
				lig[j] = c;
			}
			this.lab[i] = lig;
		}
	}

	public void genLab() {
		ArrayList<Case> liste = new ArrayList<Case>();
		int laVariableQuiFaitPlaisirAAmos = board.getSize();
		Case d = new FreeCase(r.nextInt(this.n / 2) * 2, r.nextInt(this.m / 2) * 2, board.getSize());
		liste.add(d);
		int choix;
		int l = liste.size();
		Case pos;
		int test1;
		int test2;
		int test3;
		int test4;
		int test5;
		while (!liste.isEmpty()) {
			choix = r.nextInt(l);
			pos = liste.get(choix);
			test1 = 0;
			test2 = 0;
			test3 = 0;
			test4 = 0;
			test5 = 0;
			int ze = 0;
			while (test1 == 0 || test2 == 0 || test3 == 0 || test4 == 0 || test5 == 0 || (test1 != 1 && test2 != 1 && test3 != 1 && test4 != 1 && test5 != 1)) {
				ze = r.nextInt(4);
				if (ze == 0) {
					if (pos.getI() != 0 && this.lab[pos.getI() - 2][pos.getJ()] instanceof WallCase) {
						this.lab[pos.getI() - 2][pos.getJ()] = new FreeCase(pos.getI() - 2, pos.getJ(), laVariableQuiFaitPlaisirAAmos);
						this.lab[pos.getI() - 1][pos.getJ()] = new FreeCase(pos.getI() - 1, pos.getJ(), laVariableQuiFaitPlaisirAAmos);
						test1 = 1;
						pos = this.lab[pos.getI() - 2][pos.getJ()];
						liste.add(pos);
					} else {
						test1 = 2;
					}
				}
				if (ze == 1) {
					if (pos.getI() != (n - 2) && this.lab[pos.getI() + 2][pos.getJ()] instanceof WallCase) {
						this.lab[pos.getI() + 2][pos.getJ()] = new FreeCase(pos.getI() + 2, pos.getJ(), laVariableQuiFaitPlaisirAAmos);
						this.lab[pos.getI() + 1][pos.getJ()] = new FreeCase(pos.getI() + 1, pos.getJ(), laVariableQuiFaitPlaisirAAmos);
						test2 = 1;
						pos = this.lab[pos.getI() + 2][pos.getJ()];
						liste.add(pos);
					} else {
						test2 = 2;
					}
				}
				if (ze == 2) {
					if (pos.getJ() != 0 && this.lab[pos.getI()][pos.getJ() - 2] instanceof WallCase) {
						this.lab[pos.getI()][(pos.getJ() - 2)] = new FreeCase(pos.getI(), pos.getJ() - 2, laVariableQuiFaitPlaisirAAmos);
						this.lab[pos.getI()][(pos.getJ() - 1)] = new FreeCase(pos.getI(), pos.getJ() - 1, laVariableQuiFaitPlaisirAAmos);
						test3 = 1;
						pos = this.lab[pos.getI()][(pos.getJ() - 2)];
						liste.add(pos);
					} else {
						test3 = 2;
					}
				}
				if (ze == 3) {
					if (pos.getJ() != (m - 2) && this.lab[pos.getI()][(pos.getJ() + 2)] instanceof WallCase) {
						this.lab[pos.getI()][(pos.getJ() + 2)] = new FreeCase(pos.getI(), pos.getJ() + 2, laVariableQuiFaitPlaisirAAmos);
						this.lab[pos.getI()][(pos.getJ() + 1)] = new FreeCase(pos.getI(), pos.getJ() + 1, laVariableQuiFaitPlaisirAAmos);
						test4 = 1;
						pos = this.lab[pos.getI()][(pos.getJ() + 2)];
						liste.add(pos);
					} else {
						test4 = 2;
					}
					if (test1 == 2 && test2 == 2 && test3 == 2 && test4 == 2) {
						liste.remove(pos);
						test5 = 1;
					}
				}
			}
			for (int i = n / 2; i <= n / 2 + 1; i++) {
				for (int j = m / 2; j <= m / 2 + 1; j++) {
					this.lab[i][j] = new FreeCase(i, j, laVariableQuiFaitPlaisirAAmos);
				}
			}
		}
		/*for(int i = 0; i< this.n; i++) {
			for(int j = 0; j< this.m; j++) {
				int p = r.nextInt(100);
				if(p<= 20) {
					this.lab[i][j] = new FreeCase(i, j, laVariableQuiFaitPlaisirAAmos);
				}
			}
		}*/
		for (int j = 0; j < this.m - 1; j++) {
			this.lab[1][j] = new FreeCase(1, j, laVariableQuiFaitPlaisirAAmos);
			this.lab[n - 2][j] = new FreeCase(n - 2, j, laVariableQuiFaitPlaisirAAmos);
		}
		for (int i = 0; i < this.n - 1; i++) {
			this.lab[i][1] = new FreeCase(i, 1, laVariableQuiFaitPlaisirAAmos);
			this.lab[i][m - 2] = new FreeCase(i, m - 2, laVariableQuiFaitPlaisirAAmos);
		}
		for (int i = 0; i < this.n; i++) {
			this.lab[i][0] = new WallCase(i, 0, laVariableQuiFaitPlaisirAAmos, false);
			this.lab[i][m - 1] = new WallCase(i, m - 1, laVariableQuiFaitPlaisirAAmos, false);
		}
		for (int j = 0; j < this.m; j++) {
			this.lab[0][j] = new WallCase(0, j, laVariableQuiFaitPlaisirAAmos, false);
			this.lab[n - 1][j] = new WallCase(n - 1, j, laVariableQuiFaitPlaisirAAmos, false);
		}
	}

}
