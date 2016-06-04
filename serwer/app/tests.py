from django.test import TestCase, Client
from django.contrib.auth.models import User
import json

class GlobalTestCase(TestCase):

    def test_register(self):
        User.objects.create_user(username='janek')

        client = Client()

        response = client.post('/register/',{
            "nickname": "janek"
        })
        result = response.json()
        self.assertFalse(result["result"])

        response = client.post('/register/',{
            "name": "asdf asdf",
            "password": "pass",
            "nickname": "asdf"
        })
        result = response.json()
        self.assertTrue(result["result"])

        self.assertTrue(
            User.objects.filter(username="asdf").exists()
        )


    def test_get_widnows(self):

        client = Client()

        response = client.get('/get_windows/')


    def test_post(self):

        client = Client()

        json_data = json.dumps({})
        response = client.post('/save_timetable/', {'event_list': json_data})
