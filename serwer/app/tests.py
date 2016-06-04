from django.test import TestCase, Client
from django.contrib.auth.models import User
import json

class GlobalTestCase(TestCase):

	def test_is_available(self):
		User.objects.create_user('janek')

		client = Client()

		response = client.get('/is_available/', {"nickname": "asdf"})
		result = response.json()
		self.assertFalse(result["result"])

		response = client.get('/is_available/', {"nickname": "janek"})
		result = response.json()
		self.assertTrue(result["result"])

	def test_get_widnows(self):

		client = Client()

		response = client.get('/get_windows/')


	def test_post(self):

		client = Client()

		json_data = json.dumps({})
		response = client.post('/save_timetable/', {'event_list': json_data})
