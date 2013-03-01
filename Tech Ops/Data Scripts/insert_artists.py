import csv


artist_list = set()
artist_list_upper = set()


with open('thepit.csv', 'rb') as csvfile:
	csvreader = csv.reader(csvfile, delimiter=',')
	for row in csvreader:
		artist_list.add(row[0].lower())
		artist_list_upper.add(row[0])

for artist in artist_list_upper:
	print "INSERT INTO backend_artist (name, date_created, date_modified, is_deleted) VALUES (\"" + artist + "\", now(), now(), 0);"



