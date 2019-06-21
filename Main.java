package application;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 *	Glowna klasa Main rozszerzajaca Application
 * @author azane
 *
 */
public class Main extends Application {

	//Zmienne
	static int predkosc = 10;
	static int szerokosc = 20;
	static int wysokosc = 20;

	static int trybGry = 0;

	static int kolorWeza = 0;

	static int wynik = 0;

	static List<Segment> waz = new ArrayList<>();

	static Kierunek kierunek = Kierunek.PRAWO;

	static boolean koniecGry = false;

	static boolean klawiszZajety = false;

	static Random losuj = new Random();



	/**
	 * Metoda rozpoczynaj¹ca dzialanie programu
	 */

	@Override
	public void start(Stage primaryStage) {
		try {
			noweJedzenie();

			VBox root = new VBox();
			Canvas c = new Canvas(szerokosc*Segment.rozmiarSegmentu, wysokosc*Segment.rozmiarSegmentu);
			Canvas menu = new Canvas(szerokosc*Segment.rozmiarSegmentu, wysokosc*Segment.rozmiarSegmentu);

			GraphicsContext gc = c.getGraphicsContext2D();
			GraphicsContext gmenu = c.getGraphicsContext2D();

			root.getChildren().add(c);

			/**
			 * Metoda odpowiedzialna za dzialanie aplikacji podczas trybu gry
			 */
			AnimationTimer timergame = new AnimationTimer() {
				long lastTick = 0;

				public void handle(long now) {
					if(lastTick == 0) {
						lastTick = now;
						tick(gc);
						return;
					}

					if(now - lastTick > 1000000000 / predkosc) {
						lastTick = now;
						tick(gc);
					}
				}
			};

			/**
			 * Metoda odpowiedzialna za dzialanie aplikacji podczas przebywania w MENU
			 */
			AnimationTimer timermenu = new AnimationTimer() {
				long lastTick = 0;

				public void handle(long now) {
					if(lastTick == 0) {
						lastTick = now;
						tickMenu(gmenu);
						return;
					}

					if(now - lastTick > 1000000000 / predkosc) {
						lastTick = now;
						tickMenu(gmenu);
					}
				}
			};
			timermenu.start();

			/**
			 * Metoda odpowiedzialna za odczyt klawiszy(zmiana kierunkow, sterowanie po menu, reset itp)
			 */
			Scene scene = new Scene(root, szerokosc*Segment.rozmiarSegmentu, wysokosc*Segment.rozmiarSegmentu);
			scene.addEventFilter(KeyEvent.KEY_PRESSED, key -> {
				if((key.getCode() == KeyCode.W || key.getCode() == KeyCode.UP) && kierunek != Kierunek.DOL) {
					kierunek = Kierunek.GORA;
				}
				if((key.getCode() == KeyCode.D || key.getCode() == KeyCode.RIGHT) && kierunek != Kierunek.LEWO) {
					kierunek = Kierunek.PRAWO;
				}
				if((key.getCode() == KeyCode.S || key.getCode() == KeyCode.DOWN) && kierunek != Kierunek.GORA) {
					kierunek = Kierunek.DOL;
				}
				if((key.getCode() == KeyCode.A || key.getCode() == KeyCode.LEFT) && kierunek != Kierunek.PRAWO) {
					kierunek = Kierunek.LEWO;
				}
				if(key.getCode() == KeyCode.R && koniecGry == true) {
					waz.clear();
					waz.add(new Segment(szerokosc/2, wysokosc/2));
					waz.add(new Segment(szerokosc/2, wysokosc/2));
					waz.add(new Segment(szerokosc/2, wysokosc/2));
					waz.add(new Segment(szerokosc/2, wysokosc/2));
					predkosc = 10;
					wynik = 0;
					noweJedzenie();
					koniecGry = false;
				}
				if(key.getCode() == KeyCode.ESCAPE && koniecGry == true) {
					timergame.stop();
					timermenu.start();
				}

				if(key.getCode() == KeyCode.F1) {
					timermenu.stop();
					koniecGry = false;
					timergame.start();
				}

				if(trybGry == 1) {
					if(key.getCode() == KeyCode.F2) {
						trybGry=0;
					}
				}
				else {
					if(key.getCode() == KeyCode.F2) {
						trybGry++;
					}
				}

				if(kolorWeza == 3) {
					if(key.getCode() == KeyCode.F3) {
						kolorWeza=0;
					}
				}
				else {
					if(key.getCode() == KeyCode.F3) {
						kolorWeza++;
					}
				}

				if(key.getCode() == KeyCode.F4) {
					System.exit(0);
				}
			});

			//Dodawanie poczatkowych segmentow weza
			waz.add(new Segment(szerokosc/2, wysokosc/2));
			waz.add(new Segment(szerokosc/2, wysokosc/2));
			waz.add(new Segment(szerokosc/2, wysokosc/2));
			waz.add(new Segment(szerokosc/2, wysokosc/2));


			primaryStage.setScene(scene);
			primaryStage.setTitle("MICHAL_FABIANSKI_I7Y2S1_JTP_PROJEKT");
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Metoda odpowiedzialna za generowanie planszy podczas gry przy odswiezaniu obrazu
	 * @param gc
	 */
	public static void tick(GraphicsContext gc) {
		if(koniecGry) {
			gc.setFill(Color.RED);
			gc.setFont(new Font("", 50));
			gc.fillText("PRZEGRALES!", (szerokosc*Segment.rozmiarSegmentu/2)-125, wysokosc*Segment.rozmiarSegmentu/2);
			gc.setFont(new Font("", 25));
			gc.fillText("ESC - Wyjscie do menu", (szerokosc*Segment.rozmiarSegmentu/2)-100, (wysokosc*Segment.rozmiarSegmentu/2)+60);
			gc.setFont(new Font("", 25));
			gc.fillText("R - Restart gry", (szerokosc*Segment.rozmiarSegmentu/2)-50, (wysokosc*Segment.rozmiarSegmentu/2)+90);
			return;
		}

		for(int i=waz.size()-1; i>=1; i--) {
			waz.get(i).x = waz.get(i-1).x;
			waz.get(i).y = waz.get(i-1).y;
		}


		switch(kierunek) {

		case DOL:
			waz.get(0).y++;
			if(waz.get(0).y > wysokosc-1) {
				if(trybGry == 0) {
					waz.get(0).y = 0;
				}
				else {
					koniecGry = true;
				}
			}
			break;

		case GORA:
			waz.get(0).y--;
			if(waz.get(0).y < 0) {
				if(trybGry == 0) {
					waz.get(0).y = wysokosc;
				}
				else {
					koniecGry = true;
				}
			}
			break;

		case LEWO:
			waz.get(0).x--;
			if(waz.get(0).x < 0) {
				if(trybGry == 0) {
					waz.get(0).x = szerokosc;
				}
				else {
					koniecGry = true;
				}
			}
			break;

		case PRAWO:
			waz.get(0).x++;
			if(waz.get(0).x > szerokosc-1) {
				if(trybGry == 0) {
					waz.get(0).x = 0;
				}
				else {
					koniecGry = true;
				}
			}
			break;
		}

		if(Jedzenie.jedzenieX == waz.get(0).x && Jedzenie.jedzenieY == waz.get(0).y) {
			waz.add(new Segment(-1, -1));
			wynik++;
			noweJedzenie();
		}

		for(int i=1; i<waz.size(); i++) {
			if(waz.get(0).x == waz.get(i).x && waz.get(0).y == waz.get(i).y){
				koniecGry = true;
			}
		}

		//Tlo
		gc.setFill(Color.BLACK);
		gc.fillRect(0, 0, szerokosc*Segment.rozmiarSegmentu, wysokosc*Segment.rozmiarSegmentu);


		//Wynik
		gc.setFill(Color.WHITE);
		gc.setFont(new Font("", 30));
		gc.fillText("Wynik: " + wynik, 10, 30);


		//Losowanie koloru jedzenia
		Color losowykolor = Color.WHITE;

		switch(Jedzenie.kolorjedzenia) {
		case 0: losowykolor= Color.PINK; break;
		case 1: losowykolor= Color.YELLOW; break;
		case 2: losowykolor= Color.PURPLE; break;
		case 3: losowykolor= Color.BLUE; break;
		case 4: losowykolor= Color.ORANGE; break;
		}

		gc.setFill(losowykolor);
		gc.fillOval(Jedzenie.jedzenieX*Segment.rozmiarSegmentu, Jedzenie.jedzenieY*Segment.rozmiarSegmentu, Segment.rozmiarSegmentu, Segment.rozmiarSegmentu);

		for(Segment c: waz) {
			if(kolorWeza == 0) {
			gc.setFill(Color.LIGHTGREEN);
			gc.fillRect(c.x*Segment.rozmiarSegmentu, c.y*Segment.rozmiarSegmentu, Segment.rozmiarSegmentu-1, Segment.rozmiarSegmentu-1);
			gc.setFill(Color.GREEN);
			gc.fillRect(c.x*Segment.rozmiarSegmentu, c.y*Segment.rozmiarSegmentu, Segment.rozmiarSegmentu-2, Segment.rozmiarSegmentu-2);
			}
			else if(kolorWeza == 1) {
			gc.setFill(Color.CORAL);
			gc.fillRect(c.x*Segment.rozmiarSegmentu, c.y*Segment.rozmiarSegmentu, Segment.rozmiarSegmentu-1, Segment.rozmiarSegmentu-1);
			gc.setFill(Color.CHOCOLATE);
			gc.fillRect(c.x*Segment.rozmiarSegmentu, c.y*Segment.rozmiarSegmentu, Segment.rozmiarSegmentu-2, Segment.rozmiarSegmentu-2);
			}
			else if(kolorWeza == 2) {
			gc.setFill(Color.PINK);
			gc.fillRect(c.x*Segment.rozmiarSegmentu, c.y*Segment.rozmiarSegmentu, Segment.rozmiarSegmentu-1, Segment.rozmiarSegmentu-1);
			gc.setFill(Color.HOTPINK);
			gc.fillRect(c.x*Segment.rozmiarSegmentu, c.y*Segment.rozmiarSegmentu, Segment.rozmiarSegmentu-2, Segment.rozmiarSegmentu-2);
			}
			else if(kolorWeza == 3) {
			gc.setFill(Color.CADETBLUE);
			gc.fillRect(c.x*Segment.rozmiarSegmentu, c.y*Segment.rozmiarSegmentu, Segment.rozmiarSegmentu-1, Segment.rozmiarSegmentu-1);
			gc.setFill(Color.AQUA);
			gc.fillRect(c.x*Segment.rozmiarSegmentu, c.y*Segment.rozmiarSegmentu, Segment.rozmiarSegmentu-2, Segment.rozmiarSegmentu-2);
			}
		}

	}

	/**
	 * Metoda odpowiedzialna za generowanie planszy podczas przebywaniu w MENU przy odswiezaniu obrazu
	 * @param gc
	 */
	public static void tickMenu(GraphicsContext gc) {
		//Tlo
		gc.setFill(Color.BLACK);
		gc.fillRect(0, 0, szerokosc*Segment.rozmiarSegmentu, wysokosc*Segment.rozmiarSegmentu);

		//Menu
		gc.setFill(Color.WHITE);
		gc.setFont(new Font("", 20));
		gc.fillText("[F1] ROZPOCZNIJ GRE", 100, 170);
		if(trybGry == 0) {
			gc.fillText("[F2] TRYB GRY: BEZ KRAWEDZI", 100, 210);
		}
		if(trybGry == 1) {
			gc.fillText("[F2] TRYB GRY: Z KRAWEDZIAMI", 100, 210);
		}
		if(kolorWeza == 0) {
			gc.fillText("[F3] KOLOR WEZA: ZIELONY", 100, 250);
		}
		else if(kolorWeza == 1) {
			gc.fillText("[F3] KOLOR WEZA: POMARANCZOWY", 100, 250);
		}
		else if(kolorWeza == 2) {
			gc.fillText("[F3] KOLOR WEZA: ROZOWY", 100, 250);
		}
		else if(kolorWeza == 3) {
			gc.fillText("[F3] KOLOR WEZA: NIEBIESKI", 100, 250);
		}

		gc.fillText("[F4] WYJDZ Z APLIKACJI", 100, 290);

		if(trybGry == 2) {
			trybGry = 0;
		}

		if(kolorWeza == 4) {
			kolorWeza = 0;
		}

	}

	/**
	 * Metoda odpowiedzialna za tworzenie nowego 'jedzenia' na planszy
	 */
	public static void noweJedzenie() {
		start: while(true) {
			Jedzenie.jedzenieX = losuj.nextInt(szerokosc);
			Jedzenie.jedzenieY = losuj.nextInt(wysokosc);

			for(Segment c : waz) {
				if(c.x == Jedzenie.jedzenieX && c.y == Jedzenie.jedzenieY) {
					continue start;
				}
			}
			Jedzenie.kolorjedzenia = losuj.nextInt(5);
			predkosc++;
			break;
		}
	}

	/**
	 * Glowna metoda pozwalajaca na dzialanie programu
	 * @param args
	 */
	public static void main(String[] args) {
		launch(args);
	}
}
