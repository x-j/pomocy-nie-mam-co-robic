from django.test import TestCase, Client
import json

class GlobalTestCase(TestCase):

	def test_get_widnows(self):

		client = Client()

		response = client.get('/get_windows/')

		self.assertEqual(response.content, b'Windows 10')


	def test_post(self):

		client = Client()

		json_data = json.dumps({})
		response = client.post('/save_timetable/', {'event_list': json_data})

		self.assertEqual(response.content, b'ASdf')
