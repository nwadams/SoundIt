import csv


music_list = set()
music_list_upper = set()
album_list = set()
artist_list = set()
category_list = set()

with open('thepit.csv', 'rb') as csvfile:
	csvreader = csv.reader(csvfile, delimiter=',')
	for row in csvreader:
		music_list.add(row[1].lower())
		music_list_upper.add(row[1])
		artist_list.add(row[0])
		album_list.add(row[2])
		category_list.add(row[3])
		album_query = "(SELECT id from backend_album where name=\"" + row[2] + "\")"
		artist_query = "(SELECT id from backend_artist where name=\"" + row[0] + "\")"
		category_query = "(SELECT id from backend_musiccategory where name=\"" + row[3] + "\")"
		insert_music_query = "INSERT INTO backend_musictrack (name, album_id, artist_id, track_URL, category_id, date_created, date_modified) VALUES (\"" + row[1] + "\", " + album_query + ", " + artist_query + ", \"none\", " + category_query + ", now(), now());"
		print insert_music_query