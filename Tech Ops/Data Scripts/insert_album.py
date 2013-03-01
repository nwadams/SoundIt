import csv


album_list = set()
album_list_upper = set()


with open('thepit.csv', 'rb') as csvfile:
	csvreader = csv.reader(csvfile, delimiter=',')
	for row in csvreader:
		row_2 = row[2]
		if row_2.endswith('\\'):

			row_2 = row_2.rpartition('\\')[0]
		album_list.add(row[2].lower())
		album_list_upper.add(row[2])

for album in album_list_upper:
	print "INSERT INTO backend_album (name, date_created, date_modified, is_deleted) VALUES (\"" + album + "\", now(), now(), 0);"


# playlist_votes_list = []
# for item in playlist_items:
# 	new_object = NewObject()
# 	new_object.is_voted = "";
# 	playlist_votes_list.add(new_object)



