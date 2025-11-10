package New.Service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import New.Exception.IntException;
import New.Exception.NotFoundException;
import New.Manager.VerkaufManager;
import New.Objekte.Artikel;
import New.Objekte.ArtikelBestellung;
import New.Objekte.Bestellung;
import New.Objekte.Mitglieder;
import New.Objekte.Zahlung;

public class VerkaufService extends BaseService {

	private SucheService sucheService;
	private Map<Integer, Integer> warenkorb;
	private VerkaufManager verkaufManager;

	public VerkaufService(Connection connection, Scanner scanner) {
		super(connection, scanner);
		this.sucheService = new SucheService(connection, scanner);
		this.warenkorb = new HashMap<>();
		try {
			this.verkaufManager = new VerkaufManager();
		} catch (Exception e) {
			System.out.println("Fehler beim Initialisieren des VerkaufManagers: " + e.getMessage());
		}
	}

	public void start() {
		boolean zurueck = false;
		while (!zurueck) {
			System.out.println("==== Verkaufsverwaltung ====");
			System.out.println("1 - Suche");
			System.out.println("2 - Verkauf starten");
			System.out.println("3 - Zurück zum Hauptmenü");
			System.out.print("Bitte wählen: ");
			String eingabe = scanner.nextLine();

			switch (eingabe) {
			case "1":
				sucheService.start();

				if (sucheService.exitToMainMenu) {
					sucheService.exitToMainMenu = false;
					zurueck = true;
				}
				break;
			case "2":
				starteVerkaufsprozess();
				break;
			case "3":
				zurueck = true;
				break;
			default:
				System.out.println("Ungültige Eingabe! Bitte erneut versuchen.");
			}
		}
	}

	private void starteVerkaufsprozess() {
		warenkorb.clear();

		boolean exitVerkauf = false;
		int tab = 1;

		while (!exitVerkauf) {
			// ❌ ENTFERNE: Tab-Menü mit Hervorhebung (wird in den Tab-Methoden gedruckt)

			// Aktuellen Tab anzeigen
			switch (tab) {
			case 1:
				zeigeWarenkorb();
				break;
			case 2:
				zeigeKategorie("Getränke");
				break;
			case 3:
				zeigeKategorie("Shakes");
				break;
			case 4:
				zeigeKategorie("NEM");
				break;
			case 5:
				zeigeKategorie("Non food");
				break;
			default:
				System.out.println("(Tab nicht belegt)");
			}

			// Tab-Navigation mit 0 für Artikel entfernen (nur beim Warenkorb)
			if (tab == 1 && !warenkorb.isEmpty()) {
				System.out.print("\nTab auswählen (1-5), 6: Abschluss, 7: Abbrechen, 0: Artikel entfernen\n");
			} else {
				System.out.print("\nTab auswählen (1-5), 6: Abschluss, 7: Abbrechen\n");
			}

			String tabEingabe = scanner.nextLine();

			if (tabEingabe.isBlank())
				continue;

			if (tabEingabe.equals("0") && tab == 1 && !warenkorb.isEmpty()) {
				artikelEntfernen();
			} else if (tabEingabe.equals("6")) {
				if (verkaufAbschliessen()) {
					exitVerkauf = true;
				}
			} else if (tabEingabe.equals("7")) {
				System.out.println("Verkauf abgebrochen.");
				warenkorb.clear();
				exitVerkauf = true;
			} else {
				try {
					int tabWahl = Integer.parseInt(tabEingabe);
					if (tabWahl >= 1 && tabWahl <= 5) {
						tab = tabWahl;
					}
				} catch (NumberFormatException ex) {
					System.out.println("Ungültige Tab-Nummer!");
				}
			}
		}
	}

	private void zeigeWarenkorb() {
	    // ✅ Tab-Menü VOR Warenkorb anzeigen
	    zeigeTabMenu("Warenkorb");

	    if (warenkorb.isEmpty()) {
	        System.out.println("\nWarenkorb ist leer.");
	        return;
	    }

	    // ✅ NEUE, SAUBERE TABELLE
	    System.out.printf("%-30s | %5s | %12s | %12s%n", "Name", "Menge", "Einzelpreis", "Summe");
	    System.out.println("=".repeat(70));

	    double gesamtsumme = 0.0;

	    try {
	        for (Map.Entry<Integer, Integer> entry : warenkorb.entrySet()) {
	            Artikel artikel = verkaufManager.getArtikelDAO().findById(entry.getKey());
	            if (artikel != null) {
	                int menge = entry.getValue();
	                double summe = artikel.getEinzelpreis() * menge;
	                gesamtsumme += summe;

	                String name = artikel.getName() != null ? artikel.getName() : "-";
	                
	                // Name auf maximal 30 Zeichen begrenzen
	                if (name.length() > 30) {
	                    name = name.substring(0, 27) + "...";
	                }

	                // ✅ FORMATIERTE AUSGABE
	                System.out.printf("%-30s | %5d | %12s | %12s%n",
	                    name,
	                    menge,
	                    String.format("%,.2f €", artikel.getEinzelpreis()),
	                    String.format("%,.2f €", summe)
	                );
	            }
	        }
	        
	        System.out.println("=".repeat(70));
	        System.out.printf("%-30s   %5s   %12s   %12s%n", "", "", "Gesamtsumme:", String.format("%,.2f €", gesamtsumme));

	    } catch (Exception e) {
	        System.out.println("Fehler beim Anzeigen des Warenkorbs: " + e.getMessage());
	    }
	}


	private void zeigeTabMenu(String aktiveKategorie) {
		System.out.print("1 ");
		if (aktiveKategorie.equals("Warenkorb")) {
			System.out.print("=== Warenkorb === ");
		} else {
			System.out.print("Warenkorb ");
		}
		System.out.print("| 2 ");

		if (aktiveKategorie.equalsIgnoreCase("Getränke")) {
			System.out.print("=== Getränke === ");
		} else {
			System.out.print("Getränke ");
		}
		System.out.print("| 3 ");

		if (aktiveKategorie.equalsIgnoreCase("Shakes")) {
			System.out.print("=== Shakes === ");
		} else {
			System.out.print("Shakes ");
		}
		System.out.print("| 4 ");

		if (aktiveKategorie.equalsIgnoreCase("NEM")) {
			System.out.print("=== NEM === ");
		} else {
			System.out.print("NEM ");
		}
		System.out.print("| 5 ");

		if (aktiveKategorie.equalsIgnoreCase("Non food")) {
			System.out.println("=== Non food ===");
		} else {
			System.out.println("Non food");
		}
	}

	private void zeigeKategorie(String kategorie) {
	    try {
	        List<Artikel> alleArtikel = verkaufManager.getArtikelDAO().findAll();

	        List<Artikel> kategorieArtikel = new ArrayList<>();
	        for (Artikel artikel : alleArtikel) {
	            if (artikel.getKategorie() != null && artikel.getKategorie().equalsIgnoreCase(kategorie)) {
	                kategorieArtikel.add(artikel);
	            }
	        }

	        if (kategorieArtikel.isEmpty()) {
	            System.out.println("Keine Artikel in dieser Kategorie vorhanden.");
	            System.out.println("\nEnter drücken zum Fortfahren...");
	            scanner.nextLine();
	            return;
	        }

	        boolean bleibenInKategorie = true;

	        while (bleibenInKategorie) {
	            // ✅ Tab-Menü VOR jeder Darstellung
	            zeigeTabMenu(kategorie);

	            // ✅ NEUE, SAUBERE TABELLE
	            System.out.printf("%-30s | %12s | %-40s%n", "Name", "Einzelpreis", "Kommentar");
	            System.out.println("=".repeat(90));

	            for (Artikel artikel : kategorieArtikel) {
	                String name = artikel.getName() != null ? artikel.getName() : "-";
	                String kommentar = artikel.getKommentar() != null ? artikel.getKommentar() : "-";

	                // Namen und Kommentar begrenzen
	                if (name.length() > 30) {
	                    name = name.substring(0, 27) + "...";
	                }
	                if (kommentar.length() > 40) {
	                    kommentar = kommentar.substring(0, 37) + "...";
	                }

	                // ✅ FORMATIERTE AUSGABE
	                System.out.printf("%-30s | %12s | %-40s%n",
	                    name,
	                    String.format("%,.2f €", artikel.getEinzelpreis()),
	                    kommentar
	                );
	            }
	            
	            System.out.println("=".repeat(90));

	            System.out.print("\nArtikel hinzufügen? (ArtikelID oder 0 zum Verlassen): ");
	            String eingabe = scanner.nextLine();

	            if (eingabe.equals("0")) {
	                bleibenInKategorie = false;
	            } else if (!eingabe.isBlank()) {
	                try {
	                    int artikelID = Integer.parseInt(eingabe);

	                    Artikel gewählterArtikel = null;
	                    for (Artikel artikel : kategorieArtikel) {
	                        if (artikel.getArtikelID() == artikelID) {
	                            gewählterArtikel = artikel;
	                            break;
	                        }
	                    }

	                    if (gewählterArtikel != null) {
	                        int menge = 1;
	                        int neueGesamtanzahl = warenkorb.getOrDefault(gewählterArtikel.getArtikelID(), 0) + menge;
	                        warenkorb.put(gewählterArtikel.getArtikelID(), neueGesamtanzahl);

	                        if (neueGesamtanzahl > 1) {
	                            System.out.println("✓ 1x " + gewählterArtikel.getName()
	                                + " zum Warenkorb hinzugefügt! (Gesamt: " + neueGesamtanzahl + "x)");
	                        } else {
	                            System.out
	                                .println("✓ 1x " + gewählterArtikel.getName() + " zum Warenkorb hinzugefügt!");
	                        }
	                        System.out.println();
	                    } else {
	                        System.out.println("✗ ArtikelID nicht in dieser Kategorie gefunden!");
	                        System.out.println();
	                    }
	                } catch (NumberFormatException e) {
	                    System.out.println("✗ Ungültige Eingabe!");
	                    System.out.println();
	                }
	            }
	        }

	    } catch (Exception e) {
	        System.out.println("Fehler beim Anzeigen der Kategorie: " + e.getMessage());
	        e.printStackTrace();
	    }
	}


	private void artikelEntfernen() {
		System.out.print("\nArtikel entfernen (ArtikelID eingeben oder Enter zum Abbrechen): ");
		String eingabe = scanner.nextLine();

		if (!eingabe.isBlank()) {
			try {
				int artikelID = Integer.parseInt(eingabe);

				if (warenkorb.containsKey(artikelID)) {
					int aktuelleAnzahl = warenkorb.get(artikelID);

					if (aktuelleAnzahl > 1) {
						// Anzahl um 1 reduzieren
						warenkorb.put(artikelID, aktuelleAnzahl - 1);
						System.out.println("✓ Anzahl um 1 reduziert. Neue Anzahl: " + (aktuelleAnzahl - 1));
					} else {
						// Artikel komplett entfernen (wenn Anzahl = 1)
						warenkorb.remove(artikelID);
						System.out.println("✓ Artikel komplett entfernt.");
					}
				} else {
					System.out.println("✗ ArtikelID nicht im Warenkorb!");
				}
			} catch (NumberFormatException e) {
				System.out.println("✗ Ungültige Eingabe.");
			}
		}
	}

	private boolean verkaufAbschliessen() {
		if (warenkorb.isEmpty()) {
			System.out.println("Warenkorb ist leer. Verkauf kann nicht abgeschlossen werden.");
			return false;
		}

		// 1. Warenkorb anzeigen
		zeigeTabMenu("Warenkorb");
		System.out.println();

		try {
			// 2. Zahlungsart auswählen
			int zahlungsID = wähleZahlungsart();
			if (zahlungsID == -1) {
				System.out.println("Verkauf abgebrochen.");
				return false;
			}

			// 3. Kunde suchen und auswählen
			Object kunde = sucheKunde();
			if (kunde == null) {
				System.out.println("Verkauf abgebrochen.");
				return false;
			}

			// 4. Zahlungsart validieren
			boolean istMitglied = kunde instanceof Mitglieder;
			String zahlungsart = getZahlungsartName(zahlungsID);

			if (!istMitglied) {
				System.out.println("✗ Nur Mitglieder können Bestellungen aufgeben!");
				return false;
			}

			if (!zahlungsart.equalsIgnoreCase("Barzahlung") && !zahlungsart.equalsIgnoreCase("Abbuchung")
					&& !zahlungsart.equalsIgnoreCase("SEPA-Lastschrift")) {
				System.out.println("✗ Ungültige Zahlungsart!");
				return false;
			}

			Mitglieder mitglied = (Mitglieder) kunde;

			// 5. Gesamtpreis berechnen
			double gesamtpreis = 0.0;
			for (Map.Entry<Integer, Integer> entry : warenkorb.entrySet()) {
				Artikel artikel = verkaufManager.getArtikelDAO().findById(entry.getKey());
				if (artikel != null) {
					gesamtpreis += artikel.getEinzelpreis() * entry.getValue();
				}
			}

			// 6. Abschluss-Bestätigung
			System.out.println("\n=== Verkaufsübersicht ===");
			System.out.println("Kunde: " + mitglied.getVorname() + " " + mitglied.getNachname() + " (Mitglied-ID: "
					+ mitglied.getMitgliederID() + ")");
			System.out.println("Zahlungsart: " + zahlungsart);
			System.out.println();

			zeigeWarenkorb();

			System.out.print("\nVerkauf wirklich abschließen? (ja/nein): ");
			String bestaetigung = scanner.nextLine();

			if (bestaetigung.equalsIgnoreCase("ja")) {
				// Bestellung in Datenbank speichern
				speichereBestellung(mitglied, zahlungsID, gesamtpreis);

				System.out.println("\n✓ Verkauf erfolgreich abgeschlossen und in Datenbank gespeichert!");
				warenkorb.clear();
				return true;
			} else {
				System.out.println("Verkauf wurde nicht abgeschlossen.");
				return false;
			}

		} catch (Exception e) {
			System.out.println("✗ Fehler beim Verkaufsabschluss: " + e.getMessage());
			e.printStackTrace();
		}

		return false;
	}

	private int wähleZahlungsart() {
		System.out.println("\n=== Zahlungsart wählen ===");

		try {
			// Hole alle Zahlungsarten aus der Datenbank
			List<Zahlung> zahlungsarten = verkaufManager.getZahlungDAO().findAll();

			if (zahlungsarten.isEmpty()) {
				System.out.println("Keine Zahlungsarten verfügbar!");
				return -1;
			}

			// Zeige Zahlungsarten
			System.out.println("ZahlungsID | Bezeichnung");
			System.out.println("-----------------------------");
			for (Zahlung zahlung : zahlungsarten) {
				System.out.printf("%-10d | %s%n", zahlung.getZahlungID(), zahlung.getZahlungsart());
			}
			System.out.println("-----------------------------");

			System.out.print("\nZahlungsart auswählen (ZahlungsID oder 0 zum Abbrechen): ");
			String eingabe = scanner.nextLine();

			if (eingabe.equals("0")) {
				return -1;
			}

			int zahlungID = Integer.parseInt(eingabe);

			// Prüfe, ob ID existiert
			for (Zahlung zahlung : zahlungsarten) {
				if (zahlung.getZahlungID() == zahlungID) {
					return zahlungID;
				}
			}

			System.out.println("✗ Ungültige ZahlungsID!");
			return -1;

		} catch (NumberFormatException e) {
			System.out.println("✗ Ungültige Eingabe!");
			return -1;
		} catch (Exception e) {
			System.out.println("Fehler beim Laden der Zahlungsarten: " + e.getMessage());
			return -1;
		}
	}

	private Object sucheKunde() {
		System.out.println("\n=== Kunde suchen ===");
		System.out.println("1 - Nach MitgliederID suchen");
		System.out.println("2 - Nach Vorname und Nachname suchen");
		System.out.print("Bitte wählen (oder 0 zum Abbrechen): ");

		String auswahl = scanner.nextLine();

		if (auswahl.equals("0")) {
			return null;
		}

		try {
			if (auswahl.equals("1")) {
				// Suche nach MitgliederID
				System.out.print("MitgliederID eingeben: ");
				String idStr = scanner.nextLine();
				int mitgliederID = Integer.parseInt(idStr);

				// Suche Mitglied
				Mitglieder mitglied = verkaufManager.getMitgliederDAO().findById(mitgliederID);

				if (mitglied != null) {
					zeigeMitgliedDetails(mitglied);
					System.out.print("\nDiesen Kunden verwenden? (ja/nein): ");
					if (scanner.nextLine().equalsIgnoreCase("ja")) {
						return mitglied;
					}
				} else {
					System.out.println("✗ Kein Mitglied mit dieser ID gefunden!");
				}

			} else if (auswahl.equals("2")) {
				// Suche nach Vorname und Nachname
				System.out.print("Vorname eingeben: ");
				String vorname = scanner.nextLine();

				System.out.print("Nachname eingeben: ");
				String nachname = scanner.nextLine();

				// Suche in Mitglieder
				List<Mitglieder> mitglieder = verkaufManager.getMitgliederDAO().searchByName(vorname, nachname);

				// Suche in Interessenten (falls du eine InteressentenDAO hast)
				// List<Interessent> interessenten =
				// verkaufManager.getInteressentenDAO().searchByName(vorname, nachname);

				if (mitglieder.isEmpty()) {
					System.out.println("✗ Keine Ergebnisse gefunden!");
					return null;
				}

				// Zeige Ergebnisse
				System.out.println("\n=== Suchergebnisse ===");
				System.out.println("MitgliederID | Vorname           | Nachname          | Typ");
				System.out.println("-------------------------------------------------------------------");

				for (Mitglieder m : mitglieder) {
					System.out.printf("%-12d | %-17s | %-17s | Mitglied%n", m.getMitgliederID(), m.getVorname(),
							m.getNachname());
				}

				System.out.println("-------------------------------------------------------------------");

				System.out.print("\nMitgliederID auswählen (oder 0 zum Abbrechen): ");
				String idStr = scanner.nextLine();

				if (idStr.equals("0")) {
					return null;
				}

				int ausgewählteID = Integer.parseInt(idStr);

				for (Mitglieder m : mitglieder) {
					if (m.getMitgliederID() == ausgewählteID) {
						return m;
					}
				}

				System.out.println("✗ Ungültige Auswahl!");
			}

		} catch (NumberFormatException e) {
			System.out.println("✗ Ungültige Eingabe!");
		} catch (Exception e) {
			System.out.println("Fehler bei der Kundensuche: " + e.getMessage());
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Speichert eine Bestellung in der Datenbank
	 */
	private void speichereBestellung(Mitglieder mitglied, int zahlungID, double gesamtpreis)
			throws SQLException, IntException, NotFoundException {
		try {
			// 1. Bestellung-Objekt erstellen
			Bestellung bestellung = new Bestellung();
			bestellung.setMitgliederID(mitglied.getMitgliederID());
			bestellung.setZahlungID(zahlungID);
			bestellung.setGesamtpreis(gesamtpreis);
			bestellung.setBestelldatum(new java.sql.Timestamp(System.currentTimeMillis()));

			// 2. Bestellung in DB einfügen (BestellungID wird automatisch generiert)
			verkaufManager.getBestellungDAO().insert(bestellung);

			int bestellungID = bestellung.getBestellungID();
			System.out.println("✓ Bestellung gespeichert (BestellungID: " + bestellungID + ")");

			// 3. Alle Artikel der Bestellung speichern
			for (Map.Entry<Integer, Integer> entry : warenkorb.entrySet()) {
				int artikelID = entry.getKey();
				int menge = entry.getValue();

				// Artikel-Objekt holen für Einzelpreis
				Artikel artikel = verkaufManager.getArtikelDAO().findById(artikelID);
				if (artikel != null) {
					double aufaddiert = artikel.getEinzelpreis() * menge;

					// ArtikelBestellung-Objekt erstellen
					ArtikelBestellung artikelBestellung = new ArtikelBestellung(bestellungID, artikelID, menge,
							aufaddiert);

					// ArtikelBestellung in DB einfügen
					verkaufManager.getArtikelBestellungDAO().insert(artikelBestellung);
				}
			}

			System.out.println("✓ " + warenkorb.size() + " Artikel zur Bestellung hinzugefügt");

			// ✅ Quittung drucken
			verkaufManager.druckeBestellungsQuittung(bestellungID);

		} catch (SQLException e) {
			System.out.println("✗ Fehler beim Speichern der Bestellung: " + e.getMessage());
			throw e;
		}
	}

	private void zeigeMitgliedDetails(Mitglieder mitglied) {
		System.out.println("\n=== Kunde gefunden ===");
		System.out.println("MitgliederID: " + mitglied.getMitgliederID());
		System.out.println("Name: " + mitglied.getVorname() + " " + mitglied.getNachname());
		System.out.println("E-Mail: " + (mitglied.getMail() != null ? mitglied.getMail() : "-"));
		System.out.println("Telefon: " + (mitglied.getTelefon() != null ? mitglied.getTelefon() : "-"));
	}

	private String getZahlungsartName(int zahlungsID) {
		try {
			Zahlung zahlung = verkaufManager.getZahlungDAO().findById(zahlungsID);
			return zahlung != null ? zahlung.getZahlungsart() : "Unbekannt";
		} catch (Exception e) {
			return "Unbekannt";
		}
	}

}
