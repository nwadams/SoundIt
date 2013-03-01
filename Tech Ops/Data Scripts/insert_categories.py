import csv


category_list = set()
category_list_upper = set()


with open('thepit.csv', 'rb') as csvfile:
	csvreader = csv.reader(csvfile, delimiter=',')
	for row in csvreader:
		category_list.add(row[3].lower())
		category_list_upper.add(row[3])

for category in category_list_upper:
	print "INSERT INTO backend_musiccategory (name, date_created, date_modified, is_deleted) VALUES (\"" + category + "\", now(), now(), 0);"



