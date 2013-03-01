import csv


music_list = set()
music_list_upper = set()
album_list = set()
artist_list = set()
category_list = set()

base_url = "http://api.soundit.ca/static/album/"

with open('thepit.csv', 'rb') as csvfile:
	csvreader = csv.reader(csvfile, delimiter=',')
	for row in csvreader:
		if row[4]:
			update_album_query = "UPDATE backend_album SET image_URL=\'" + base_url + row[4] + "\' WHERE name=\'" + row[2] + "\';"
			print update_album_query