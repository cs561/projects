%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%%%    __                                                                  %%%%
%%%%   <- ) 	     CS561 - Verteilte Systeme                              %%%%
%%%%   /( \\ 	     JaMi Cloud Rendering Project                           %%%%
%%%%   \\_\\_> 	     (c) Jan Ebbe, Michael Schneider                        %%%%
%%%%   \" \"	                                                            %%%%
%%%%                                                                        %%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

Ziel dieser Software ist, eine rechenaufwendige Aufgabe - wie die Berechnung
eines 3-dimensionalen Bildes mittels Raytracing - auf beliebig viele Rechner
zu verteilen und so die Geschwindigkeit der gesamten Berechnung zu beschleu-
nigen.

Die Software besteht aus drei Teilen:
	- RaytraceServer bestehend aus einem JAR-File und einem dazugehörenden
		Ordner "mesh", in dem die benötigten Meshes und Texturen abgelegt
		sind
	- RaytraceServerAndroid ist ein APK-File, das auf einem Androidgerät
		installiert werden kann
	- RaytraceClient ist eine Weboberfläche zum Steuern des Cloud Render-
		ers
		
--------------------------------------------------------------------------------

RaytraceServer
	
	Der Server kann über raytrace_server.jar ohne Parameter gestartet werden.
	Es wird ausgelesen, wie viele Kerne der Rechner hat und es werden ent-
	sprechend viele Render-Threads gestartet. Dabei werden auch virtuelle Kerne
	berücksichtigt.
	Zu jedem Thread wird aufsteigend und beginnend mit 1337 ein Port geöffnet.
	Beim Start des Servers öffnet sich eine Console, die Informationen zu
	gestarteten Serverthreads liefert und Einsatzbereitschaft anzeigt.
	
	Der Server hat eine fixe Szene vorprogrammiert, die auf externe Anfrage
	linienweise gerendert und zurückgegeben wird.
	Auf den offenen Ports werden HTTP-Anfragen entegengenommen und verarbeitet.
	Mittels GET-Request kann über folgende Synthax die Berechnung einer Linie
	in Auftrag gegeben werden:
	
	http://[server]:[port]/render-[width]-[height]-[line]-[maxReflection]
	
	Nach der Berechnung wird ein Bytearray mit je einem Byte für Rot, Grün, Blau
	und den Alphakanal zurückgesendet.
	
	Die Applikation ist komplett in Java programmiert und enthält keine externen
	Libraries. Getestet wurde der Server mit Java 1.7, sollte aber bis mind-
	estens Version 1.5 abwärtskompatibel sein.
	
RaytraceServerAndroid

	Im Wesentlichen arbeitet die Androidversion des Servers genau gleich wie die
	Desktopversion. Der einzige Unterschied ist, dass hier eine andere, einfach-
	ere Szene gerendert und zurückgegeben wird. Hier sind alle benötigten Datei-
	en im APK enthalten.
	
	Die APK lässt sich komfortabel installieren, falls die Installation von Apps
	aus unbekannten Quellen erlaubt wurde. Sobald die App gestartet wurde, er-
	scheint der gleiche Output wie auf der Desktopversion und das Gerät geht
	nicht mehr in den Ruhezustand.
	
RaytraceClient

	Das Interface, über das man das System steuern kann ist komplett in HTML5
	und JavaScript programmiert. Aufgerufen werden kann das Ganze über die
	Datei index.html.
	Voraussetzung für das Versenden von XMLHTTP-Requests an andere Server ist,
	dass beim Browser Cross-Site-Scripting erlaubt wird. Getestet wurde der 
	Client mit Google Chrome v25 und Internet Explorer 11 mit entsprechend de-
	aktivierten Sicherheitsmechanismen. Hierzu muss man Google Chrome mit dem
	Parameter --disable-web-security starten bzw. in den Internet Optionen des
	Internet Explorers Cross-Site-Scripting (CSS) erlauben.
	Firefox hingegen blockt in der aktuellen Version die ausgehenden Verbind-
	ungen.
	
	Die Oberfläche des Clients gliedert sich in 4 Teile:
	
		Render Frame
		Hier werden die von den Servern zurückgesendeten Daten visualisiert.
		
		Settings
		Hier kann die Bildgrösse und maximale Reflexionszahl definiert und der
		Rendervorgang ausgelöst werden
		
		Rendering Statistics
		Ein paar Informationen zum durchgeführten Rendering
		
		Server
		Hier können neue Server eingetragen werden
		
		Server List
		Eine Liste aller derzeit eingetragenen Server. Hier können die Server
		auch wieder aus der Liste ausgetragen werden.
		
	Wird alles korrekt eingestellt und der Rendervorgang ausgelöst, verschickt
	die Seite asynchrone XMLHTTP-Anfragen an alle Server in der Liste und visu-
	alisiert die zurückgesendeten Daten als Bild im Render Frame.